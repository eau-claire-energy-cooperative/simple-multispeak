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
	private final String MULTISPEAK_COMPANY = "Company";
	
	private MultiSpeakVersion m_version = null;
	private URL m_url = null;
	private Map<String,String> m_options = null;
	
	/**
	 * @param version the version of multispeak to use for SOAP requests
	 * @param company the name of the company as defined by the service endpoint
	 * @param url the full URL, including port if needed
	 */
	public MultiSpeakEndpoint(MultiSpeakVersion version, String company, String url){
		m_version = version;
		
		try {
			m_url = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	
		m_options = new HashMap<String,String>();
		this.setCompany(company);
	}
	
	/**
	 * @param version the version of multispeak to use for SOAP requests
	 * @param company the name of the company as defined by the service endpoint
	 * @param url the full URL, including port if needed
	 * @param username username of the endpoint
	 * @param password password of the endpoint
	 */
	public MultiSpeakEndpoint(MultiSpeakVersion version, String company, String url, String username, String password){
		this(version, company,url);
		this.setAuthentication(username, password);
	}
	
	protected Element createHeader(){
		Element header = new Element("Header",MultiSpeak.SOAP_NAMESPACE);
		Element msHeader = new Element("MultiSpeakMsgHeader", m_version.getNamespace());
		
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
	 * @param version the version of multispeak to use for SOAP requests
	 * @param company the name of the company as defined by the service endpoint
	 * @param url the full URL, including port if needed
	 * @param username username of the endpoint
	 * @param password password of the endpoint
	 * @param app appname as defined by the endpoint setup
	 */
	public MultiSpeakEndpoint(MultiSpeakVersion version, String company, String url, String username, String password, String app){
		this(version, company,url,username,password);
		this.setAppName(app);
	}
	
	public MultiSpeakVersion getVersion() {
		return m_version;
	}
	
	public URL getURL(){
		return m_url;
	}
	
	public String getUsername(){
		return m_options.get(MULTISPEAK_USERNAME);
	}
	
	public String getPassword(){
		return m_options.get(MULTISPEAK_PASSWORD);
	}
	
	public String getAppName(){
		return m_options.get(MULTISPEAK_APPNAME);
	}
	
	public String getCompany(){
		return m_options.get(MULTISPEAK_COMPANY);
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
	
	/**
	 * This could be used to set the company name after object creation, if necessary
	 * @param company name of the company as defined by the endpoint setup
	 */
	public void setCompany(String company){
		m_options.put(MULTISPEAK_COMPANY,company);
	}
}
