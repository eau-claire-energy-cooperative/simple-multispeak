package com.ecec.rweber.multispeak;

import java.util.Iterator;
import java.util.List;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * High level class with some static members and functions for navigating Multispeak calls
 * 
 * @author rweber
 *  
 */
public final class MultiSpeak {
	// SOAP namespace definition
	public static final Namespace SOAP_NAMESPACE = Namespace.getNamespace("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
	
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
	 * helper method to convert returned xml datetime values to something more useable
	 * 
	 * @param d the datetime string as an ISO date (returned by Multispeak calls)
	 * @return the parsed string as a DateTime object
	 */
	public static DateTime fromMultispeakDate(String d){
		DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();

		return parser.parseDateTime(d);
	}
	
	/**
	 * helper method to convert a given date to a useable ISO date for xml calls
	 * @param year the year, ex 2020
	 * @param monthOfYear month of the year, 1-12
	 * @param dayOfMonth day of the month 1-X
	 * @param hourOfDay hour of the day 1-24
	 * @param minuteOfHour minute of the hour 1-59
	 * @return the date as an ISO date string (suitable for Multispeak)
	 */
	public static String toMultispeakDate(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour){
		
		DateTime d = new DateTime(year,monthOfYear,dayOfMonth,hourOfDay,minuteOfHour,DateTimeZone.getDefault());
		
		return d.toString();
	}
	
	/**
	 * Exensions to the Multispeak spec are passed in as {@code <exensionItem>} values within a list. This helper function takes the list and finds a given {@code <extName>} node within the list and returns the value
	 * 
	 * @param ext the element containing the list of extensionItem nodes
	 * @param extName the name of node to get the value from
	 * @return the extValue from the given node, null if it doesn't exist
	 */
	public static String findExensionItem(Element ext, String extName){
		String result = null;
		
		//get the namespace and list of elements
		Namespace n = ext.getNamespace();
		List<Element> extList = ext.getChildren("extensionsItem",n);
		Iterator<Element> iter = extList.iterator();
		Element anElem = null;
		
		//find the extension item that equals the passed in name
		while(iter.hasNext() && result == null)
		{
			anElem = iter.next();
			
			if(anElem.getChildText("extName",n).equals(extName))
			{
				result = anElem.getChildText("extValue",n);
			}
		}
		
		return result;
	}
}
