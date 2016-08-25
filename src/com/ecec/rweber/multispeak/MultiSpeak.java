package com.ecec.rweber.multispeak;

import org.jdom2.Namespace;

public class MultiSpeak {
	//some static namespace definitions that help with requests/responses
	public static final Namespace SOAP_NAMESPACE = Namespace.getNamespace("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
	public static final Namespace MULTISPEAK_NAMESPACE = Namespace.getNamespace("", "http://www.multispeak.org/Version_3.0");
	public static final Namespace MULTISPEAK_RESULT_NAMESPACE = Namespace.getNamespace("ns2", "http://www.multispeak.org/Version_3.0");
	
}
