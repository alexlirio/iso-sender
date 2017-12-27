package com.company.sender;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Logger;
import org.jpos.iso.BaseChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.channel.NACChannel;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.RotateLogListener;
import org.jpos.util.SimpleLogListener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.company.sender.converter.UtilConverter;


public class SenderISO {

	private final static Logger log = Logger.getLogger(SenderISO.class);
	private final static org.jpos.util.Logger jposLogger = new org.jpos.util.Logger();
	
	public final static String PREFIX_ARG = "-";
	public final static String HOST_ARG = "host";
	public final static String PORT_ARG = "port";
	public final static String CHANNEL = "channel";
	public final static String TIMEOUT_ARG = "timeout";
	public final static String XML_ISO_PACKAGER_FILE_ARG = "xml_iso_packager_file";
	public final static String JSON_REQUEST_ARG = "json_request";
	
	private GenericPackager packager;
	private String host;
	private int port;
	private String channel;
	private int timeout;
	private String xmlIsoPackagerFile;
	private List<String> jsonsRequest;
	
	
	public SenderISO() {
		super();
		carregaJposLogListeners();
	}
	
	public static void main(String[] args) {
		
		String ret = null;
		
		SenderISO senderISO = new SenderISO();
		Map<String, String> argsMap = new TreeMap<String, String>();
		
		for (int i = 0; i < args.length-1; i=i+2) {
			argsMap.put(args[i], args[i+1]);
		}
		
		ret = senderISO.createAndSend(argsMap);
		
		log.info("Retorno: " + ret);
	}
	
	public String createAndSend() {
		loadArgs(new TreeMap<String, String>());
		return send();
	}

	public String createAndSend(Map<String, String> argsMap) {
		loadArgs(argsMap);
		return send();
	}

	private String send() {
		String ret = new String();
		String isoHeader = null;
		ISOMsg isoMsgRequest = null;
		ISOMsg isoMsgResponse = null;
		JSONObject jsonMsgRequest = null;
		JSONObject jsonMsgResponse = null;
		BaseChannel ch = null;
		
		for (String jsonRequest : jsonsRequest) {
			try {
				packager = new GenericPackager(xmlIsoPackagerFile);
				jsonMsgRequest = (JSONObject)new JSONParser().parse(jsonRequest);
				if (channel.equalsIgnoreCase("NACChannel")) {
					ch = new NACChannel();
				} else if (channel.equalsIgnoreCase("ASCIIChannel")) {
					ch = new ASCIIChannel();
				} else {
					throw new NoSuchFieldException("Required argument(s) 'channel' not supported. Supported values is 'NACChannel' or 'ASCIIChannel'.");
				}
				ch.setLogger(jposLogger, "Sender");
				ch.setPackager(packager);
				ch.setHost(host);
				ch.setPort(port);
				ch.setTimeout(timeout);
				ch.connect();
				
				if (jsonMsgRequest.containsKey("header")) {
					isoHeader = (String)jsonMsgRequest.get("header");
					jsonMsgRequest.remove("header");
				}
				
				log.info(String.format("JSON Request = %s", jsonMsgRequest.toJSONString()));
				isoMsgRequest = UtilConverter.getISO(jsonMsgRequest);
				
				if (isoHeader != null && !isoHeader.isEmpty()) {
					ch.setHeader(isoHeader);
					isoMsgRequest.setHeader(ISOUtil.str2bcd(isoHeader, true));
				}
				
				ch.send(isoMsgRequest);
				isoMsgResponse = ch.receive();
				ch.disconnect();
				jsonMsgResponse = UtilConverter.getJSON(isoMsgResponse);
				log.info(String.format("JSON Response = %s", jsonMsgResponse.toJSONString()));
				ret = ret.concat(jsonMsgResponse.toString()) + System.getProperty("line.separator");
			} catch (NoSuchFieldException e) {
				log.error(e.getClass().getSimpleName() + " in " + this.getClass().getSimpleName() , e);
				e.printStackTrace();
			} catch (ISOException e) {
				log.error(e.getClass().getSimpleName() + " in " + this.getClass().getSimpleName() , e);
				e.printStackTrace();
			} catch (IOException e) {
				log.error(e.getClass().getSimpleName() + " in " + this.getClass().getSimpleName() , e);
				e.printStackTrace();
			} catch (ParseException e) {
				log.error(e.getClass().getSimpleName() + " in " + this.getClass().getSimpleName() , e);
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	private void carregaJposLogListeners() {
		
		SimpleLogListener simpleLogListener = new SimpleLogListener(System.out);
		jposLogger.addListener(simpleLogListener);
		
		try {
			String logFileName = ((DailyRollingFileAppender)log.getParent().getAppender("FileAppender")).getFile();
			RotateLogListener rotateLogListener = new RotateLogListener(logFileName, 0, 5);
			jposLogger.addListener(rotateLogListener);
		} catch (NullPointerException e) {
			log.error(e.getClass().getSimpleName() + " in " + this.getClass().getSimpleName() , e);
			e.printStackTrace();
		} catch (ClassCastException e) {
			log.error(e.getClass().getSimpleName() + " in " + this.getClass().getSimpleName() , e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error(e.getClass().getSimpleName() + " in " + this.getClass().getSimpleName() , e);
			e.printStackTrace();
		}
	}

	private void loadArgs(Map<String, String> argsMap) {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("cfg/config.properties");
			prop.load(input);
			host = argsMap.get(PREFIX_ARG + HOST_ARG) != null ? argsMap.get(PREFIX_ARG + HOST_ARG) : prop.getProperty(HOST_ARG);
			port = Integer.parseInt(argsMap.get(PREFIX_ARG + PORT_ARG) != null ? argsMap.get(PREFIX_ARG + PORT_ARG) : prop.getProperty(PORT_ARG));
			channel = argsMap.get(PREFIX_ARG + CHANNEL) != null ? argsMap.get(PREFIX_ARG + CHANNEL) : prop.getProperty(CHANNEL);
			timeout = Integer.parseInt(argsMap.get(PREFIX_ARG + TIMEOUT_ARG) != null ? argsMap.get(PREFIX_ARG + TIMEOUT_ARG) : prop.getProperty(TIMEOUT_ARG));
			xmlIsoPackagerFile = argsMap.get(PREFIX_ARG + XML_ISO_PACKAGER_FILE_ARG) != null ? argsMap.get(PREFIX_ARG + XML_ISO_PACKAGER_FILE_ARG) : prop.getProperty(XML_ISO_PACKAGER_FILE_ARG);
			
			jsonsRequest = new ArrayList<String>();
			for (Map.Entry<?, ?> entry : argsMap.entrySet()) {
				String key = (String)entry.getKey();
				String value = (String)entry.getValue();
				if (key.startsWith(PREFIX_ARG + JSON_REQUEST_ARG)) {
					jsonsRequest.add(value);
				}
			}
			if (jsonsRequest.isEmpty()) {
				for (Map.Entry<?, ?> entry : prop.entrySet()) {
					String key = (String)entry.getKey();
					String value = (String)entry.getValue();
					if (key.startsWith(JSON_REQUEST_ARG)) {
						jsonsRequest.add(value);
					}
				}
			}
			if (jsonsRequest.isEmpty()) {
				throw new NoSuchFieldException("Required argument(s) 'json_request*' is empty."); 
			}
			
		} catch (IOException e) {
			log.error(e.getClass().getSimpleName() + " in " + this.getClass().getSimpleName() , e);
			e.printStackTrace();
		} catch (NumberFormatException e) {
			log.error(e.getClass().getSimpleName() + " in " + this.getClass().getSimpleName() , e);
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			log.error(e.getClass().getSimpleName() + " in " + this.getClass().getSimpleName() , e);
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					log.error(e.getClass().getSimpleName() + " in " + this.getClass().getSimpleName() , e);
					e.printStackTrace();
				}
			}
		}
		log.info(HOST_ARG + "=" + host);
		log.info(PORT_ARG + "=" + port);
		log.info(CHANNEL + "=" + channel);
		log.info(TIMEOUT_ARG + "=" + timeout);
		log.info(XML_ISO_PACKAGER_FILE_ARG + "=" + xmlIsoPackagerFile);
		
		for (String jsonRequest : jsonsRequest) {
			log.info(JSON_REQUEST_ARG + "=" + jsonRequest);
		}
	}

}
