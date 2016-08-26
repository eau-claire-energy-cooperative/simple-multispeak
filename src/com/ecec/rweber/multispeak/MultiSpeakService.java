package com.ecec.rweber.multispeak;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
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
	 * Just a helper method to quickly get to the responses from a command by cutting off the soap specific stuff
	 * 
	 * @param response the response from a MultiSpeak command
	 * @param method the method name - important to build the right child attributes
	 * @return the element results from this command, could be null if document doesn't have it
	 */
	private Element getResult(Document response, String method){
		Element result = null;
		
		if(response != null)
		{			
			//first get the body
			Element body = response.getRootElement().getChild("Body", MultiSpeak.SOAP_NAMESPACE);
			
			if(body != null)
			{
				//get the response child
				Element child1 = body.getChild(method + "Response");
				
				if(child1 != null)
				{
					//get the result
					if(child1.getChild(method + "Result",MultiSpeak.MULTISPEAK_RESULT_NAMESPACE) != null)
					{
						result = child1.getChild(method + "Result",MultiSpeak.MULTISPEAK_RESULT_NAMESPACE);
					}
					else
					{
						//some methods (who knows why) use this as their root node for the response 
						result = child1;
					}
				}
			}
		}
		
		return result;
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
		Element result = this.getResult(m_client.sendRequest(method,params),method);
		
		return result;
	}
}
