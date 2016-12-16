package com.ecec.rweber.multispeak;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * This is an abstract class representing a service endpoint such as MR (meter reading), OA (outage managment), etc.
 * 
 * To interact in a meaningful way with any MultiSpeak endpoint this class should be extended and specific methods implemented per vendor implementations of each Multispeak Service. This class can be created standalone with the call() method being used to pass method names as String objects.
 * 
 * @author rweber
 *
 * */
public class MultiSpeakService {
	protected Logger m_log = null;
	protected MultiSpeakClient m_client = null;
	private MultiSpeakResult m_lastResult = null;
	
	/**
	 * @param endpoint a valid MultiSpeakEndpoint - connection information
	 */
	public MultiSpeakService(MultiSpeakEndpoint endpoint){
		m_log = Logger.getLogger(this.getClass());
		m_client = new MultiSpeakClient(endpoint);
	}
	
	
	protected List<Element> createParams(String[] params){
		List<Element> result = new ArrayList<Element>();
		
		//we must have equal key/value pairs
		if(params.length > 0 && params.length % 2 == 0)
		{
			Element temp = null;
			for(int count = 0; count < params.length; count = count + 2)
			{
				temp = new Element(params[count]);
				temp.setText(params[count + 1]);
				
				result.add(temp);
			}
		}
		
		return result;
	}
	
	public MultiSpeakResult getLastResult(){
		return m_lastResult;
	}
	
	/**
	 *  All MultiSpeak Endpoints should implement this
	 * 
	 * @return List of methods accepted by this server
	 */
	public List<String> getMethods(){
		List<String> result = new ArrayList<String>();
		
		Element methods = this.call("GetMethods").getResult();
		
		if(methods != null)
		{
			//get the children
			Iterator<Element> children = methods.getChildren("string", methods.getNamespace()).iterator();
			
			while(children.hasNext())
			{
				result.add(children.next().getText());
			}
		}
		
		return result;
	}
	
	/**
	 * All MultiSpeak Endpoints should implement this
	 * 
	 * @return true/false if the ping was successful
	 */
	public boolean ping(){
		boolean result = false;
		
		Element pingResult = this.call("PingURL").getResult();
		
		if(pingResult != null)
		{
			result = pingResult.getChildren().isEmpty();
		}
		
		return result;
	}
	
	/**
	 * This is a convenience method for Multispeak calls with no parameters
	 * @param method the method to send to the MultiSpeak Service
	 * @return the result element with the SOAP envelope stripped off
	 */
	public MultiSpeakResult call(String method) {
		return this.call(method, createParams(new String[]{}));
	}
	
	public MultiSpeakResult call(String method, String[] params){
		return this.call(method,createParams(params));
	}
	
	/**
	 * Call a Multispeak method with the given parameters. Results are returned as an XML element of the payload
	 * 
	 * @param method the method to send to the MultiSpeak Service
	 * @param params any parameters to pass, can be null
	 * @return the result element with the SOAP envelope stripped off - can be NULL
	 */
	public MultiSpeakResult call(String method, List<Element> params) {
		MultiSpeakResult result = null;
		
		try{
			//send the request
			Document xmlResponse = m_client.sendRequest(method,params);
		
			//send the request and parse the result
			result = new MultiSpeakResult(xmlResponse,method);
		}
		catch(MultiSpeakException e)
		{
			m_log.error(e.getMessage(),e);
		}
		
		//set last result - even if it's null
		m_lastResult = result;
		
		return result;
	}
}
