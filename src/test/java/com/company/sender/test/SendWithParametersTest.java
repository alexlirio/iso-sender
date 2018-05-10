package com.company.sender.test;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import com.company.sender.SenderISO;

public class SendWithParametersTest {

	SenderISO senderISO = new SenderISO();

	@Test
	public void test() {
		System.out.println("Inside SendWithParametersTest");
		
		Map<String, String> argumentos = new TreeMap<String, String>();
		argumentos.put(SenderISO.PREFIX_ARG + SenderISO.HOST_ARG, "127.0.0.1");
		argumentos.put(SenderISO.PREFIX_ARG + SenderISO.PORT_ARG, "12000");
		argumentos.put(SenderISO.PREFIX_ARG + SenderISO.TIMEOUT_ARG, "300000");
		argumentos.put(SenderISO.PREFIX_ARG + SenderISO.XML_ISO_PACKAGER_FILE_ARG, "cfg/packager.xml");
		argumentos.put(SenderISO.PREFIX_ARG + SenderISO.JSON_REQUEST_ARG + "_1", "{\"header\":\"0000000000\",\"0\":\"0305\",\"3\":\"009500\",\"4\":\"000000000199\",\"11\":\"000001\",\"12\":\"235959\",\"13\":\"1231\",\"41\":\"POS80217\",\"42\":\"000000000083917\",\"61.1\":\"21\",\"61.2\":\"999999999\",\"61.3\":\"TEST001\",\"62\":\"1.00b01p01#9.51b27#PWWIN#ECF4BBFBC1C2\"}");
		argumentos.put(SenderISO.PREFIX_ARG + SenderISO.JSON_REQUEST_ARG + "_2", "{\"header\":\"0000000000\",\"0\":\"0305\",\"3\":\"009500\",\"4\":\"000000000199\",\"11\":\"000002\",\"12\":\"235959\",\"13\":\"1231\",\"41\":\"POS80217\",\"42\":\"000000000083917\",\"61.1\":\"21\",\"61.2\":\"999999999\",\"61.3\":\"TEST001\",\"62\":\"1.00b01p01#9.51b27#PWWIN#ECF4BBFBC1C2\"}");
		argumentos.put(SenderISO.PREFIX_ARG + SenderISO.JSON_REQUEST_ARG + "_3", "{\"header\":\"0000000000\",\"0\":\"0305\",\"3\":\"009500\",\"4\":\"000000000199\",\"11\":\"000003\",\"12\":\"235959\",\"13\":\"1231\",\"41\":\"POS80217\",\"42\":\"000000000083917\",\"61.1\":\"21\",\"61.2\":\"999999999\",\"61.3\":\"TEST001\",\"62\":\"1.00b01p01#9.51b27#PWWIN#ECF4BBFBC1C2\"}");
		argumentos.put(SenderISO.PREFIX_ARG + SenderISO.JSON_REQUEST_ARG + "_4", "{\"header\":\"0000000000\",\"0\":\"0305\",\"3\":\"009500\",\"4\":\"000000000199\",\"11\":\"000004\",\"12\":\"235959\",\"13\":\"1231\",\"41\":\"POS80217\",\"42\":\"000000000083917\",\"61.1\":\"21\",\"61.2\":\"999999999\",\"61.3\":\"TEST001\",\"62\":\"1.00b01p01#9.51b27#PWWIN#ECF4BBFBC1C2\"}");
		
		assertNotNull(senderISO.createAndSend(argumentos));
	}

}
