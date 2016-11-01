package com.ecec.rweber.multispeak;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jdom2.Element;

/**
 * This class represents the connection information for a given Multispeak endpoint. This is the starting point for creating any type of service communication. Endpoints can be created with or without authentication information depending on how they are setup. 
 * 
 * @author rweber
 *
 */
public class MultiSpeakEndpoint {
	//static variables for key names
	private final String MULTISPEAK_USERNAME = "UserID";
	private final String MULTISPEAK_PASSWORD = "Pwd";
	private final String MULTISPEAK_APPNAME = "AppName";
			
	private URL m_url = null;
	private Map<String,String> m_options = null;
	
	/**
	 * @param url the full URL, including port if needed
	 */
	public MultiSpeakEndpoint(String url){
	
		try {
			m_url = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	
		m_options = new HashMap<String,String>();
	}
	
	/**
	 * @param url the full URL, including port if needed
	 * @param username username of the endpoint
	 * @param password password of the endpoint
	 */
	public MultiSpeakEndpoint(String url, String username, String password){
		this(url);
		this.setAuthentication(username, password);
	}
	
	/**
	 * @param url the full URL, including port if needed
	 * @param username username of the endpoint
	 * @param password password of the endpoint
	 * @param app appname as defined by the endpoint setup
	 */
	public MultiSpeakEndpoint(String url, String username, String password, String app){
		this(url,username,password);
		this.setAppName(app);
	}
	
	protected URL getURL(){
		return m_url;
	}
	
	protected Element createHeader(){
		Element header = new Element("Header",MultiSpeak.SOAP_NAMESPACE);
		Element msHeader = new Element("MultiSpeakMsgHeader",MultiSpeak.MULTISPEAK_NAMESPACE);
		
		msHeader.setAttribute("Version","3.0");
		
		//add any options that exist
		Iterator<String> iter = m_options.keySet().iterator();
		String aKey = null;
		
		while(iter.hasNext())
		{
			aKey = iter.next();
			
			if(!m_options.get(aKey).isEmpty())
			{
				msHeader.setAttribute(aKey,m_options.get(aKey));
			}
		}
		
		header.addContent(msHeader);
		
		return header;
	}
	
	/**
	 *  This could be used to set authentication after object creation, if necessary
	 * 
	 * @param username username of the endpoint
	 * @param password passord of the endpoint 
	 */
	public void setAuthentication(String username, String password){
		m_options.put(MULTISPEAK_USERNAME, username);
		m_options.put(MULTISPEAK_PASSWORD, password);
	}
	
	/**
	 * This could be used to set the app name after object creation, if necessary
	 * @param app appname as defined by the endpoint setup
	 */
	public void setAppName(String app){
		m_options.put(MULTISPEAK_APPNAME, app);
	}
}
