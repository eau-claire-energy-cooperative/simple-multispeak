package com.ecec.rweber.multispeak;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class MultiSpeakClient {
	private MultiSpeakService m_service = null;
	
	public MultiSpeakClient(MultiSpeakService service){
		m_service = service;
	}
	
	private String createEnvelope(String method, Map<String,String> params){
		Document result = new Document();
		
		//create the root node
		Element root = new Element("Envelope",MultiSpeak.SOAP_NAMESPACE);
		
		//add the header
		root.addContent(m_service.createHeader());
		
		//create the body and the method call
		Element body = new Element("Body",MultiSpeak.SOAP_NAMESPACE);
		
		Element xmlMethod = new Element(method);
		
		//check if there are any params
		if(params != null)
		{
			Iterator<String> keys = params.keySet().iterator();
			String aKey = null;
		
			while(keys.hasNext())
			{
				aKey = keys.next();
				Element aParam = new Element(aKey);
				aParam.setText(params.get(aKey));
			
				//add this to the xml method
				xmlMethod.addContent(aParam);
			}
		}
		
		body.addContent(xmlMethod);
		root.addContent(body);
		
		result.setRootElement(root);
		
		return new XMLOutputter().outputString(result);
	}
	
	public Document call(String request, String soapAction) throws MalformedURLException, IOException, JDOMException {
		
		//create the http connection
		URLConnection connection = m_service.getURL().openConnection();
		HttpURLConnection httpConn = (HttpURLConnection)connection;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
	
		//turn the envelope into a byte array
		byte[] buffer = new byte[request.length()];
		buffer = request.getBytes();
		bout.write(buffer);
		byte[] b = bout.toByteArray();
	
		// Set the appropriate HTTP parameters.
		httpConn.setRequestProperty("Content-Length",
		String.valueOf(b.length));
		httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		httpConn.setRequestProperty("SOAPAction", soapAction);
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		OutputStream out = httpConn.getOutputStream();
		
		//Write the content of the request to the outputstream of the HTTP Connection
		out.write(b);
		out.close();	
		 
		//Read the response
		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
		BufferedReader in = new BufferedReader(isr);
		String outputString = "";
		String aString = "";
		
		//Write the SOAP message response to a String.
		while ((aString = in.readLine()) != null) {
			outputString = outputString + aString;
		}

		//parse this into a document
		SAXBuilder builder = new SAXBuilder();
		
		//need a string reader to create an input stream
		return builder.build(new StringReader(outputString));
	}
	
	public Document sendRequest(String method){
		return this.sendRequest(method, null);
	}
	
	public Document sendRequest(String method, Map<String,String> params){
		Document response = null;
		String request = this.createEnvelope(method, params);
		
		//try and send the request to the endpoint
		try{
			response = this.call(request, m_service.getURL() + "/" + method);
		}
		catch(Exception e)
		{
			//if this triggers the result will be null 
			e.printStackTrace();
		}
		
		return response;
	}
}
