package com.ecec.rweber.multispeak;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;

/**
 * @author rweber
 *
 * This class should be extended by service endpoints such as MR (meter reading), OA (outage managment), etc. 
 */
public abstract class MultiSpeakService {
	protected MultiSpeakClient g_client = null;
	
	public MultiSpeakService(MultiSpeakEndpoint endpoint){
		g_client = new MultiSpeakClient(endpoint);
	}
	
	/**
	 *  All MultiSpeak Endpoints should implement this
	 * 
	 * @return List of methods accepted by this server
	 */
	public List<String> getMethods(){
		List<String> result = new ArrayList<String>();
		
		Element methods = MultiSpeak.getResult(g_client.sendRequest("GetMethods"),"GetMethods");
		
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
	 * @return a list of errors returned by this ping, if empty the ping was a success
	 */
	public List<String> ping(){
		List<String> result = new ArrayList<String>();
		
		Element pingResult = MultiSpeak.getResult(g_client.sendRequest("PingURL"),"PingURL");
		
		if(pingResult != null)
		{
			Iterator<Element> children = pingResult.getChildren("errorObject").iterator();
			
			while(children.hasNext())
			{
				result.add(children.next().getAttributeValue("errorString"));
			}
		}
		
		return result;
	}
}
