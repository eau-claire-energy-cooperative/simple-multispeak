package com.ecec.rweber.multispeak;

import java.util.List;

public class Test {

	public static void main(String[] args) {
		MultiSpeakEndpoint service = new MultiSpeakEndpoint("http://53053dbs:35000/cisservices/services/CB_ServerSoap","CANN", "CANN","CIS");
		MultiSpeakService cb = new MultiSpeakService(service);
		
		System.out.println(cb.ping());
	}

}
