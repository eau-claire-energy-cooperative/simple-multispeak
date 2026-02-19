package com.ecec.rweber.multispeak;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Represents the returned results from a Multispeak endpoint call. If the method returns a payload the XML can be accessed. If the result returned an error the exception can be viewed. 
 * 
 * @author rweber
 *
 */
public class MultiSpeakResult {
	private Document xml = null;
	private MultiSpeakException exception = null;
	private String method = null;
	
	MultiSpeakResult(Document xml, String method){
		this.xml = xml;
		this.method = method;
	}
	
	MultiSpeakResult(MultiSpeakException ex, String method){
		this.exception = ex;
		this.method = method;
	}
	
	/**
	 * @return if the method call returned a valid XML result
	 */
	public boolean isSuccess() {
		// return true if xml is present
		return this.xml != null;
	}
	
	/**
	 * @return the payload body if isSuccess() is true, otherwise NULL
	 */
	public Element getResult(){
		Element result = null;
		
		if(xml != null)
		{	
			Namespace soapNamespace = xml.getRootElement().getNamespace();
			
			//first get the body
			Element body = xml.getRootElement().getChild("Body", soapNamespace);
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
	
	/**
	 * @return the payload header
	 */
	public Map<String,String> getHeader(){
		Map<String,String> result = new HashMap<String,String>();
		
		if(xml != null)
		{	
			Namespace soapNamespace = xml.getRootElement().getNamespace();
			
			//get the header
			Element header = xml.getRootElement().getChild("Header", soapNamespace);
			
			//get the Multispeak header - there should only be one child
			Element multiHeader = header.getChildren().get(0);
			
			//get the attributes
			List<Attribute> attributes = multiHeader.getAttributes();
			
			for(Attribute a : attributes)
			{
				result.put(a.getName(), a.getValue());
			}
		}
		
		return result;
	}
	
	/**
	 * @return true if the payload body has any data, false if empty
	 */
	public boolean containsData() {
		boolean result = true;
		
		Element resultElem = this.getResult();
		
		//if there are no children there is no data
		if(resultElem.getChildren().isEmpty())
		{
			result = false;
		}
		
		return result;
	}
	
	/**
	 * @return the exception if isSuccess() is false, otherwise NULL
	 */
	public MultiSpeakException getException() {
		return this.exception;
	}
}
