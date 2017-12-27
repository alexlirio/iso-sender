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
		argumentos.put(SenderISO.PREFIX_ARG + SenderISO.PORT_ARG, "11000");
		argumentos.put(SenderISO.PREFIX_ARG + SenderISO.TIMEOUT_ARG, "300000");
		argumentos.put(SenderISO.PREFIX_ARG + SenderISO.XML_ISO_PACKAGER_FILE_ARG, "cfg/packager.xml");
		argumentos.put(SenderISO.PREFIX_ARG + SenderISO.JSON_REQUEST_ARG + "_1", "{\"56.1\":\"21\",\"56.2\":\"999999999\",\"24\":\"015\",\"13\":\"0324\",\"14\":\"2210\",\"11\":\"000052\",\"12\":\"094616\",\"3\":\"009100\",\"2\":\"5328840000000188\",\"0\":\"0304\",\"42\":\"000000000083917\",\"41\":\"POS80217\",\"61\":\"1.00b01#9.53b02#PWWIN#F8BC128EB2FC\"}");
		argumentos.put(SenderISO.PREFIX_ARG + SenderISO.JSON_REQUEST_ARG + "_2", "{\"56.1\":\"21\",\"56.2\":\"999999999\",\"24\":\"015\",\"13\":\"0324\",\"14\":\"2210\",\"11\":\"000052\",\"12\":\"094616\",\"3\":\"009100\",\"2\":\"5328840000000188\",\"0\":\"0304\",\"42\":\"000000000083917\",\"41\":\"POS80217\",\"61\":\"1.00b01#9.53b02#PWWIN#F8BC128EB2FC\"}");
		argumentos.put(SenderISO.PREFIX_ARG + SenderISO.JSON_REQUEST_ARG + "_3", "{\"56.1\":\"21\",\"56.2\":\"999999999\",\"24\":\"015\",\"13\":\"0324\",\"14\":\"2210\",\"11\":\"000052\",\"12\":\"094616\",\"3\":\"009100\",\"2\":\"5328840000000188\",\"0\":\"0304\",\"42\":\"000000000083917\",\"41\":\"POS80217\",\"61\":\"1.00b01#9.53b02#PWWIN#F8BC128EB2FC\"}");
		argumentos.put(SenderISO.PREFIX_ARG + SenderISO.JSON_REQUEST_ARG + "_4", "{\"56.1\":\"21\",\"56.2\":\"999999999\",\"24\":\"015\",\"13\":\"0324\",\"14\":\"2210\",\"11\":\"000052\",\"12\":\"094616\",\"3\":\"009100\",\"2\":\"5328840000000188\",\"0\":\"0304\",\"42\":\"000000000083917\",\"41\":\"POS80217\",\"61\":\"1.00b01#9.53b02#PWWIN#F8BC128EB2FC\"}");
		
		assertNotNull(senderISO.createAndSend(argumentos));
	}

}
