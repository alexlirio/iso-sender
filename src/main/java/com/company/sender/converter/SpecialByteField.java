package com.company.sender.converter;

import java.nio.ByteBuffer;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

public class SpecialByteField {

	private final static Logger log = Logger.getLogger(SpecialByteField.class);

	private TreeMap fields;

	/**
	 * Constructor
	 **/
	public SpecialByteField() {
		fields = new TreeMap();
	}

	/**
	 * Constructor. Build the fields TreeMap with content param data.
	 */
	public SpecialByteField(byte[] content) {
		fields = new TreeMap();
		// implement parse here
		int len = content.length;
		int position = 0;
		int size;
		byte[] value;
		String id;
		String str;
		// SortedMap aux = fields;

		while (len > position) {
			str = ISOUtil.bcd2str(content, position, 4, true);
			// slen += ISOUtil.bcd2str(content[1]);
			size = Integer.parseInt(str);

			position += 2;

			id = ISOUtil.bcd2str(content, position, 2, true);

			position += 1;
			// str = ISOUtil.bcd2str(content, position, size, true);
			// value = Integer.parseInt(str);

			value = new byte[size - 1];
			for (int i = 0; i < size - 1; i++) {
				value[i] = content[position + i];
			}

			fields.put(id, value);

			position += size - 1;

		}

	}
	
	/**
	 * Constructor. Build the fields TreeMap with content string data.
	 */
	public SpecialByteField(String content) {
		fields = new TreeMap();
		int len = content.length();
		int position = 0;
		int size;
		String id;
		String value;

		while (len > position) {
			size = Integer.parseInt(content.substring(position, position+4)) * 2;
			position += 4;

			id = content.substring(position, position+2);

			value = content.substring(position+2, position+2+size-2);
			position += size;
			
			fields.put(id, DatatypeConverter.parseHexBinary(value));
		}
		
	}
	
	/**
	 * Inserts special byte subfield
	 * 
	 * @param id of the field inside field 62
	 * @value value of the field (array of bytes)
	 */
	public void set(String id, byte[] value) throws ISOException {
		if (id.length() > 2) {
			log.error("ERROR: SPECIAL FIELD ID MUST HAVE SIZE 2");
			throw new ISOException("SPECIAL FIELD ID MUST HAVE SIZE <= 2");
		}
		if (id.length() == 1) {
			id = "0" + id;
		}

		// Se o valor passado for null ou vazio n�o seta o sub-campo.
		//
		if (value != null && value.length > 0) {
			fields.put(id, value);
		}
	}

	/**
	 * Inserts special byte subfield
	 * 
	 * @param id of the field to be written
	 * @value value of the field (string)
	 */
	public void set(String id, String value) throws ISOException {
		set(id, value.getBytes());
	}

	/**
	 * Gets special byte subfield value
	 * 
	 * @param id of the field to be found
	 */
	public byte[] getValue(String id) {
		// return (String) fields.get(id);
		return (byte[]) fields.get(id);
	}

	/**
	 * Read the fields TreeMap and returns an array of bytes ready to put into iso
	 * message
	 * 
	 * @return byte[]
	 */
	public byte[] getBytes() {

		ByteBuffer bb = ByteBuffer.allocate(1024);

		String id;
		String size;
		byte[] value;

		SortedMap aux = fields;

		try {

			while (!aux.isEmpty()) {
				id = (String) aux.firstKey();
				value = (byte[]) fields.get(id);

				size = ISOUtil.zeropad(Integer.toString(value.length + 1), 4); // +1 por causa do id!!
				bb.put(ISOUtil.str2bcd(size.substring(0, 2), true));
				bb.put(ISOUtil.str2bcd(size.substring(2, 4), true));
				bb.put(ISOUtil.str2bcd(id, true));
				bb.put(value);
				aux.remove(id);
			}

		} catch (ISOException e) {
			log.error(e.getMessage(), e);
			return null;
		}

		bb.flip();
		byte[] out = new byte[bb.limit()];
		bb.get(out, 0, bb.limit());
		return out;
	}

}
