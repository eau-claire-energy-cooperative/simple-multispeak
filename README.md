# Simple Multispeak Library

### Overview
The goal of this library is to provide a simple, extendable, interface for communicating with a webservice implementing the [Multispeak Standard](http://www.multispeak.org/Pages/default.aspx). This is done via XML based HTTP calls utilizing the [JDOM 2 XML](http://www.jdom.org/) library. SOAP based libraries and auto stub generation (such as [AXIS](http://axis.apache.org/axis2/java/core/)) are not being used as WSDL files returned by vendor implementations of MultiSpeak are often incomplete and fail parsing tests. 

Creating a useable program using this library will involve defining a MultiSpeakService java class and defining what methods you want to use. Vendor implementations of Multispeak often do not implement every method in the standard so using the built in getMethods() function will return a list of available methods. A ping() function is also available to test basic connectivity. 

### Getting Started

This library defines a service description class called MultiSpeakEndpoint. This class contains the connection information for the service you wish to connect to. Usage is as follows: 

```java

	//no authentication
	MultiSpeakEndpoint cis = new MultiSpeakEndpoint("http://127.0.0.1/CB_Server");
	
	//with authentication
	MultiSpeakEndpoint cis = new MultiSpeakEndpoint("http://127.0.0.1/CB_Server","user","pass","appname");

````

Once the connection information is established a class extending MultiSpeakService must be created to communicate with the endpoint and return information. For example, this simple service implements the GetServiceLocationByMeterNo Multispeak method as well as a helper class to contain the information. 

```java

import java.util.HashMap;
import java.util.Map;
import org.jdom2.Element;
import com.ecec.rweber.multispeak.MultiSpeakEndpoint;
import com.ecec.rweber.multispeak.MultiSpeakService;

public class CBService extends MultiSpeakService {

	public CBService(MultiSpeakEndpoint endpoint) {
		//call to parent to set the endpoint information
		super(endpoint);
		
	}

	public ServiceLocation getServiceByMeter(String meterLoc){
		ServiceLocation result = null;
		
		//params to a method are passed as a map of key/value pairs
		Map<String,String> params = new HashMap<String,String>();
		params.put("meterNo",meterLoc);
		
		Element location = this.call("GetServiceLocationByMeterNo",params);
		
		//the returned information is a JDOM Element that represents the XML returned in the payload. 
		if(location != null)
		{
			result = new ServiceLocation(location);
		}
		
		return result;
	}
}

```

```java

import org.jdom2.Element;
import com.ecec.rweber.multispeak.MultiSpeak;
import com.ecec.rweber.multispeak.XmlResultLoader;

//XmlResultLoader is a helper class to convert the xml into the java object structure
public class ServiceLocation implements XmlResultLoader{
	private int accountNumber = 0;
	private int memberNumber = 0;
	
	public ServiceLocation(Element xml){
		this.load(xml);
	}
	
	public int getAccountNumber() {
		return accountNumber;
	}

	public int getMemberNumber() {
		return memberNumber;
	}

	@Override
	public void load(Element xml) {
		accountNumber = Integer.parseInt(xml.getChildText("accountNumber",MultiSpeak.MULTISPEAK_RESULT_NAMESPACE));
		memberNumber = Integer.parseInt(xml.getChildText("custID",MultiSpeak.MULTISPEAK_RESULT_NAMESPACE));
	}

}

````