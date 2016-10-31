package com.ecec.rweber.multispeak;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
	 * This is a helper function for debugging purposes to print results
	 * 
	 * @param xml the xml element to print
	 * @return the xml element as a string
	 */
	public static String printXML(Element xml){
		String result = "";
		
		if(xml != null)
		{
			XMLOutputter output = new XMLOutputter();
			output.setFormat(Format.getPrettyFormat());
			
			result =  output.outputString(xml);
		}
		
		return result;
	}
	
	/**
	 * @param d the date as a MultiSpeak datetime object (yyyy-MM-dd'T'HH:mm:ss.SSSZ)
	 * @param timezone the timezone as a standard abbreviation, CDT, PDT, etc
	 * @return the result as a java date
	 */
	public static Date parseDate(String d, String timezone){
		Date result = null;
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.US);
		formatter.setTimeZone(TimeZone.getTimeZone(timezone));		//need to change this
		
		try {
			result = formatter.parse(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
