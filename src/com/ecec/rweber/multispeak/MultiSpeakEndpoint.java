package com.ecec.rweber.multispeak;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jdom2.Element;

public class MultiSpeakEndpoint {
	//static variables for key names
	private final String MULTISPEAK_USERNAME = "UserID";
	private final String MULTISPEAK_PASSWORD = "Pwd";
	private final String MULTISPEAK_APPNAME = "AppName";
			
	private URL m_url = null;
	private Map<String,String> m_options = null;
	
	public MultiSpeakEndpoint(String url){
	
		try {
			m_url = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	
		m_options = new HashMap<String,String>();
	}
	
	public MultiSpeakEndpoint(String url, String username, String password){
		this(url);
		this.setAuthentication(username, password);
	}
	
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
	
	public void setAuthentication(String username, String password){
		m_options.put(MULTISPEAK_USERNAME, username);
		m_options.put(MULTISPEAK_PASSWORD, password);
	}
	
	public void setAppName(String app){
		m_options.put(MULTISPEAK_APPNAME, app);
	}
}
