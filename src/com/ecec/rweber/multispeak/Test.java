package com.ecec.rweber.multispeak;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Test {

	public static void main(String[] args) {
		MultiSpeakService service = new MultiSpeakService("http://53053dbs:35000/cisservices/services/CB_ServerSoap","CANN", "CANN","CIS");
		
		MultiSpeakClient client = new MultiSpeakClient(service);
		
		Document result = client.sendRequest("GetMethods");
		
		if(result != null)
		{
			//just write the results
			XMLOutputter output = new XMLOutputter();
			output.setFormat(Format.getPrettyFormat());
			
			System.out.println(output.outputString(result));
		}
	}

}
