package com.ecec.rweber.multispeak;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

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
		m_log = LogManager.getLogger(this.getClass());
		m_client = new MultiSpeakClient(endpoint);
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
		
		MultiSpeakResult pingResult = this.call("PingURL");
		
		if(pingResult != null)
		{
			result = pingResult.getResult().getChildren().isEmpty();
		}
		
		return result;
	}
	
	/**
	 * This is a convenience method for Multispeak calls with no parameters
	 * @param method the method to send to the MultiSpeak Service
	 * @return the result element with the SOAP envelope stripped off
	 */
	public MultiSpeakResult call(String method) {
		return this.call(method, new String[]{});
	}
	
	/**
	 * Call a Multispeak method with the given parameters. Results are returned as an XML element of the payload
	 * 
	 * @param method the method to send to the MultiSpeak Service
	 * @param params any parameters to pass, can be null
	 * @return the result element with the SOAP envelope stripped off - can be NULL
	 */
	public MultiSpeakResult call(String method, String[] params) {
		MultiSpeakResult result = null;
		
		try{
			//send the request
			Document xmlResponse = m_client.sendRequest(method,params);
			
			//make sure there is a response
			if(xmlResponse != null)
			{
				//send the request and parse the result
				result = new MultiSpeakResult(xmlResponse,method);
			}
		}
		catch(MultiSpeakException e)
		{
			m_log.error(e.getMessage(),e);
		}
		
		//set last result - even if it's null
		m_lastResult = result;
		
		return result;
	}

	/**
	 * Calls a MultiSpeak SOAP method using a list of pre-built JDOM element parameters.
	 *
	 * @param method the method to send to the MultiSpeak Service
	 * @param params the list of JDOM Elements representing the method parameters
	 * @return the result element with the SOAP envelope stripped off - can be NULL
	 */
	public MultiSpeakResult call(String method, List<Element> params) {
		MultiSpeakResult result = null;

		try {
			Document xmlResponse = m_client.sendRequest(method, params);
			if (xmlResponse != null) {
				result = new MultiSpeakResult(xmlResponse, method);
			}
		} catch (MultiSpeakException e) {
			m_log.error(e.getMessage(), e);
		}

		m_lastResult = result;
		return result;
	}
}
