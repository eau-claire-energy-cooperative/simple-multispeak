package com.ecec.rweber.multispeak;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.junit.jupiter.api.Test;


public class MultiSpeakUtilityTest {

	@Test
	public void testNoAuthEndpoint() {

		// version 3
		MultiSpeakEndpoint endpoint = new MultiSpeakEndpoint(MultiSpeakVersion.Version3, TestConstants.companyName, TestConstants.url);
		
		// test passed in values
		assertEquals(MultiSpeakVersion.Version3, endpoint.getVersion());
		assertEquals(TestConstants.companyName, endpoint.getCompany());
		assertEquals(TestConstants.url, endpoint.getURL().toString());
		
		// test blank values
		assertEquals(null, endpoint.getUsername());
		assertEquals(null, endpoint.getPassword());
		assertEquals(null, endpoint.getAppName());
		
		// make sure header does not contain blank values
		Element header = endpoint.createHeader();
		
		Element mHeader = header.getChild("MultiSpeakMsgHeader", endpoint.getVersion().getNamespace());
		
		assertEquals(MultiSpeakVersion.Version3.getVersion() + "", mHeader.getAttributeValue("Version"));
		assertEquals(null, mHeader.getAttributeValue("UserID"));
		assertEquals(null, mHeader.getAttributeValue("Password"));
	}
	
	@Test
	public void testAuthEndpoint() {

		// version 4
		MultiSpeakEndpoint endpoint = new MultiSpeakEndpoint(MultiSpeakVersion.Version41, TestConstants.companyName, TestConstants.url, TestConstants.username, TestConstants.password);
		
		// test basic values
		assertEquals(MultiSpeakVersion.Version41, endpoint.getVersion());
		assertEquals(TestConstants.companyName, endpoint.getCompany());
		assertEquals(TestConstants.url, endpoint.getURL().toString());
		
		// test auth
		assertEquals(TestConstants.username, endpoint.getUsername());
		assertEquals(TestConstants.password, endpoint.getPassword());
		assertEquals(null, endpoint.getAppName());
		
		// make sure header contains auth
		Element header = endpoint.createHeader();
		
		Element mHeader = header.getChild("MultiSpeakMsgHeader", endpoint.getVersion().getNamespace());
		
		assertEquals(MultiSpeakVersion.Version41.getVersion() + "", mHeader.getAttributeValue("Version"));
		assertEquals(TestConstants.username, mHeader.getAttributeValue("UserID"));
		assertEquals(TestConstants.password, mHeader.getAttributeValue("Pwd"));
	}
	
	@Test
	public void testFileEndpoint() throws JDOMException, IOException, MultiSpeakException {

		// load the resource file
		URL resource = getClass().getClassLoader().getResource("test_endpoint_success.xml");
		
		// load the endpoint
		MultiSpeakEndpoint endpoint = new MultiSpeakEndpoint(new File(resource.getFile()));
		
		// test basic values
		assertEquals(MultiSpeakVersion.Version3, endpoint.getVersion());
		assertEquals(TestConstants.companyName, endpoint.getCompany());
		assertEquals(TestConstants.url, endpoint.getURL().toString());
		
	}
	
	@Test
	public void testFileEndpointFailure() throws JDOMException, IOException, MultiSpeakException {

		// load the resource file
		URL resource = getClass().getClassLoader().getResource("test_endpoint_fail.xml");
		
		// loading should fail
		assertThrows(MultiSpeakException.class, () -> {
			new MultiSpeakEndpoint(new File(resource.getFile()));}
		);
		
		
	}
}
