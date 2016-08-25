package com.ecec.rweber.multispeak;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class MultiSpeak {
	//some static namespace definitions that help with requests/responses
	public static final Namespace SOAP_NAMESPACE = Namespace.getNamespace("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
	public static final Namespace MULTISPEAK_NAMESPACE = Namespace.getNamespace("", "http://www.multispeak.org/Version_3.0");
	public static final Namespace MULTISPEAK_RESULT_NAMESPACE = Namespace.getNamespace("ns2", "http://www.multispeak.org/Version_3.0");
	
	/**
	 * This is a helper function for debugging other purposes to print results
	 * 
	 * @param xml the xml element to print
	 * @return the xml element as a string
	 */
	public static String printXML(Element xml){
		XMLOutputter output = new XMLOutputter();
		output.setFormat(Format.getPrettyFormat());
		
		return output.outputString(xml);
	}
	
	/**
	 * Just a helper method to quickly get to the responses from a command by cutting off the soap specific stuff
	 * 
	 * @param response the response from a MultiSpeak command
	 * @param method the method name - important to build the right child attributes
	 * @return the element results from this command, could be null if document doesn't have it
	 */
	public static Element getResult(Document response, String method){
		Element result = null;
		
		if(response != null)
		{			
			//first get the body
			Element body = response.getRootElement().getChild("Body", MultiSpeak.SOAP_NAMESPACE);
			
			if(body != null)
			{
				//get the response child
				Element child1 = body.getChild(method + "Response");
				
				if(child1 != null)
				{
					//get the result
					result = child1.getChild(method + "Result",MultiSpeak.MULTISPEAK_RESULT_NAMESPACE);
				}
			}
		}
		
		return result;
	}
}
