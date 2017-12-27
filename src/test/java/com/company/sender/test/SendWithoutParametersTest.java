package com.company.sender.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.company.sender.SenderISO;

public class SendWithoutParametersTest {

	SenderISO senderISO = new SenderISO();

	@Test
	public void test() {
		System.out.println("Inside SendWithoutParametersTest");
		assertNotNull(senderISO.createAndSend());
	}

}
