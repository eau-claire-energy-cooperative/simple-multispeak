package com.ecec.rweber.multispeak;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

public class MultiSpeakResult {
	private Document xml = null;
	private String method = null;
	
	MultiSpeakResult(Document xml, String method){
		this.xml = xml;
		this.method = method;
	}
	
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
}
