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
	
	/**
	 * @param endpoint a valid MultiSpeakEndpoint - connection information
	 */
	public MultiSpeakService(MultiSpeakEndpoint endpoint){
		m_log = Logger.getLogger(this.getClass());
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
			Namespace soapNamespace = response.getRootElement().getNamespace();
			
			//first get the body
			Element body = response.getRootElement().getChild("Body", soapNamespace);
			if(body != null)
			{
				//get the response child
				Element child1 = null;
				
				Iterator<Element> bodyIter = body.getChildren().iterator();
				Element temp = null;
				while(bodyIter.hasNext() && child1 == null)
				{
					temp = bodyIter.next();
					
					if(temp.getName().equals(method + "Response"))
					{
						child1 = temp;
					}
				}
				
				if(child1 != null)
				{
					Iterator<Element> responseIter = child1.getChildren().iterator();
					
					while(responseIter.hasNext() && result == null)
					{
						temp = responseIter.next();
						
						if(temp.getName().equals(method + "Result"))
						{
							result = temp;
						}
					}
					
					if(result == null)
					{
						//some methods (who knows why) use this as their root node for the response 
						result = child1;
					}
				}
			}
		}
		
		return result;
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
		
		Element pingResult = this.call("PingURL");
		
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
	public Element call(String method) {
		return this.call(method, createParams(new String[]{}));
	}
	
	public Element call(String method, String[] params){
		return this.call(method,createParams(params));
	}
	
	/**
	 * Call a Multispeak method with the given parameters. Results are returned as an XML element of the payload
	 * 
	 * @param method the method to send to the MultiSpeak Service
	 * @param params any parameters to pass, can be null
	 * @return the result element with the SOAP envelope stripped off - can be NULL
	 */
	public Element call(String method, List<Element> params) {
		Element result = null;
		
		try{
			//send the request
			Document xmlResponse = m_client.sendRequest(method,params);
		
			//send the request and parse the result
			result = this.getResult(xmlResponse,method);
		}
		catch(MultiSpeakException e)
		{
			m_log.error(e.getMessage(),e);
		}
		
		
		return result;
	}
}
