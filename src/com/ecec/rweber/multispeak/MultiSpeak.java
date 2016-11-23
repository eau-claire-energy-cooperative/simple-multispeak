package com.ecec.rweber.multispeak;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

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
	//some static namespace definitions that help with requests/responses
	public static final Namespace SOAP_NAMESPACE = Namespace.getNamespace("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
	public static final Namespace MULTISPEAK_NAMESPACE = Namespace.getNamespace("", "http://www.multispeak.org/Version_3.0");
	//public static final Namespace MULTISPEAK_RESULT_NAMESPACE = Namespace.getNamespace("ns2", "http://www.multispeak.org/Version_3.0");
	
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
	 * @param year
	 * @param monthOfYear
	 * @param dayOfMonth
	 * @param hourOfDay
	 * @param minuteOfHour
	 * @return the date as an ISO date string (suitable for Multispeak)
	 */
	public static String toMultispeakDate(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour){
		
		DateTime d = new DateTime(year,monthOfYear,dayOfMonth,hourOfDay,minuteOfHour,DateTimeZone.getDefault());
		
		return d.toString();
	}
}
