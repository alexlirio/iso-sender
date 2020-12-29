package com.company.sender.converter;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.jpos.iso.ISOBinaryField;
import org.jpos.iso.ISOBinaryFieldPackager;
import org.jpos.iso.ISOBitMap;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOField;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;
import org.json.simple.JSONObject;


public class UtilConverter {
	
	private final static Logger log = Logger.getLogger(UtilConverter.class);
	
	public static ISOMsg getISO(JSONObject json, GenericPackager packager) {
		
		ISOMsg isoMsg = new ISOMsg();
		
		Iterator<?> iterator = json.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String)iterator.next();
			String value = json.get(key).toString();
			
			if (key.equalsIgnoreCase("header")) {
				isoMsg.setHeader(ISOUtil.hex2byte(value));
			} else if (Pattern.compile("^([\\d]{1,3})([\\.]{1}[\\d]{1,3})*$").matcher(key).matches()) {
				//Valid ISO Key 
				try {
					if (packager.getFieldPackager(Integer.parseInt(key)) instanceof ISOBinaryFieldPackager) {
						SpecialByteField specialByteField = new SpecialByteField(value);
						isoMsg.set(key, specialByteField.getBytes());
					} else {
						isoMsg.set (key, value);
					}
				} catch (ISOException e) {
					log.error(e.getClass().getSimpleName() + " in " + UtilConverter.class.getClass().getName() , e);
					e.printStackTrace();
				}
			} else {
				log.error(String.format("Invalid ISO Key(bit) format. Key(bit)=%s", key));
				new ISOException(String.format("Invalid ISO Key(bit) format. Key(bit)=%s", key)).printStackTrace();
			}
		}
		
		return isoMsg;
	}

	public static JSONObject getJSON(ISOMsg isoMsg) {
		Map<String, String> fieldsMap = getIsoFieldsMap(isoMsg);
		return new JSONObject(fieldsMap);
	}

	private static Map<String, String> getIsoFieldsMap(ISOMsg isoMsg) {
		
		Map<String, String> fields = new TreeMap<String, String>();
		
		Iterator fieldsInterator = isoMsg.getChildren().entrySet().iterator();
		while (fieldsInterator.hasNext()) {
			Map.Entry me = (Map.Entry)fieldsInterator.next();
			if (me.getValue() instanceof ISOBitMap) {
				//BITMAP
				continue;
			} else if (me.getValue() instanceof ISOField) {
				//FIELD
				fields.put(String.valueOf(me.getKey()), String.valueOf(((ISOField)me.getValue()).getValue()));
			} else if (me.getValue() instanceof ISOBinaryField) {
				//BINARY FIELD
				fields.put(String.valueOf(me.getKey()), new String(DatatypeConverter.parseHexBinary(((ISOBinaryField)me.getValue()).toString())));
			} else if (me.getValue() instanceof ISOMsg) {
				//SUBFIELD
				fields.putAll(getIsoSubFieldsMap(String.valueOf(me.getKey()), ((ISOMsg)me.getValue()), fields));
			} else {
				log.error("ERROR: Field " + me.getKey() + " has a class not implemented. Class=" + me.getValue().getClass().getName());
			}
		}
		
		return fields;
	}
	
	private static Map<String, String> getIsoSubFieldsMap(String idField, ISOMsg isoMsg, Map<String, String> fields) {
		
		Iterator fieldsInterator = isoMsg.getChildren().entrySet().iterator();
		while (fieldsInterator.hasNext()) {
			Map.Entry me = (Map.Entry)fieldsInterator.next();
			if (me.getValue() instanceof ISOBitMap) {
				//BITMAP
				continue;
			} else if (me.getValue() instanceof ISOField) {
				//FIELD
				fields.put(idField + "." + String.valueOf(me.getKey()), String.valueOf(((ISOField)me.getValue()).getValue()));
			} else if (me.getValue() instanceof ISOBinaryField) {
				//BINARY FIELD
				fields.put(idField + "." + String.valueOf(me.getKey()), new String(DatatypeConverter.parseHexBinary(((ISOBinaryField)me.getValue()).toString())));
			} else if (me.getValue() instanceof ISOMsg) {
				//SUBFIELD
				fields.putAll(getIsoSubFieldsMap(idField + "." + String.valueOf(me.getKey()), ((ISOMsg)me.getValue()), fields));
			} else {
				log.error("ERROR: Field " + idField + "." + String.valueOf(me.getKey()) + " has a class not implemented. Class=" + me.getValue().getClass().getName());
			}
		}
		
		return fields;
	}

}
