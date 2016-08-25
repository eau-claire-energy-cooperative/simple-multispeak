package com.ecec.rweber.multispeak;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

/**
 * @author rweber
 *
 * This class represents a service endpoint such as MR (meter reading), OA (outage managment), etc. 
 */
public class MultiSpeakService {
	protected MultiSpeakClient m_client = null;
	
	public MultiSpeakService(MultiSpeakEndpoint endpoint){
		m_client = new MultiSpeakClient(endpoint);
	}
	
	/**
	 *  All MultiSpeak Endpoints should implement this
	 * 
	 * @return List of methods accepted by this server
	 */
	public List<String> getMethods(){
		List<String> result = new ArrayList<String>();
		
		Element methods = this.call("GetMethods");
		
		if(methods != null)
		{
			//get the children
			Iterator<Element> children = methods.getChildren("string", MultiSpeak.MULTISPEAK_RESULT_NAMESPACE).iterator();
			
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
		
		Element pingResult = this.call("PingURL");
		
		if(pingResult != null)
		{
			result = pingResult.getChildren().isEmpty();
		}
		
		return result;
	}
	
	/**
	 * This is a convienence method
	 * @param method the method to send to the MultiSpeak Service
	 * @return the result element with the SOAP envelope stripped off
	 */
	public Element call(String method){
		return this.call(method, null);
	}
	
	/**
	 * @param method the method to send to the MultiSpeak Service
	 * @param params any parameters to pass, can be null
	 * @return the result element with the SOAP envelope stripped off
	 */
	public Element call(String method, Map<String,String> params){
		
		//send the request and parse the result
		Element result = MultiSpeak.getResult(m_client.sendRequest(method,params),method);
		
		return result;
	}
}
