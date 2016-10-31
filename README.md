# Simple Multispeak Library

### Overview
The goal of this library is to provide a simple, extendable, interface for communicating with a webservice implementing the [Multispeak Standard](http://www.multispeak.org/Pages/default.aspx). This is done via XML based HTTP calls utilizing the [JDOM 2 XML](http://www.jdom.org/) library. SOAP based libraries and auto stub generation (such as [AXIS](http://axis.apache.org/axis2/java/core/)) are not being used as WSDL files returned by vendor implementations of MultiSpeak are often incomplete and fail parsing tests. 

Creating a useable program using this library will involve defining a MultiSpeakService java class and defining what methods you want to use. Vendor implementations of Multispeak often do not implement every method in the standard so using the built in getMethods() function will return a list of available methods. A ping() function is also available to test basic connectivity. 

### Installation

The easiest way to use this class is to clone the repo and use Maven to build the jar. For any projects you create make sure that both the simple-multispeak.jar file and a copy of [JDOM](http://www.jdom.org/) are available to the class path. 

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

Once this is done, integrating this with any project you want to use is as simple as creating the class and calling the method. 

```java

public static void main(String[] args){
	MultiSpeakEndpoint cis = new MultiSpeakEndpoint("http://127.0.0.1/CB_Server"); //create the endpoint
	CBService cb = new CBService(cis); 	//create the service using this endpoing
	
	ServiceLocation sLocation = cb.getServiceByMeter("12345");
	
	//null check is here is required - if the call errors out in any way the response may be NULL
	if(sLocation != null)
	{
		System.out.println("Found location: " + sLocation.GetAccountNumber();
	}

}

````
***Note that the helper class implementing XmlResultLoader is optional. It is useful though to encapsulate responses into a Java Object, especially if these will contain other helpful methods used to process the data; or if you wish to export data in a more useful format - like JSON. ***

### Advanced Usage - Results Parsing

Multispeak will return results as an XML payload. Calls done using "this.call()" by a MultiSpeakService class will trim off returned XML and give back a JDOM Element that contains only the results of the call. *If the call should fail for any reason, such as wrong endpoint information, network down, MultiSpeak method doesn't exist, wrong parameters; the returned Element will be NULL*. The Element class can be iterated through directly, or more usefully passed along to a class implementing the XmlResultLoader interface to encapsulate the data. 

An example of the returned XML from the above example using GetServiceLocationByMeterNo would be: 

```xml

<ns2:GetServiceLocationByMeterNoResult xmlns:ns2="http://www.multispeak.org/Version_3.0" objectID="8808">
  <ns2:gridLocation>0000</ns2:gridLocation>
  <ns2:facilityID>0000</ns2:facilityID>
  <ns2:custID>1234</ns2:custID>
  <ns2:accountNumber>1234001</ns2:accountNumber>
  <ns2:servAddr1>8214 US Hwy 12</ns2:servAddr1>
  <ns2:servCity>Fall Creek</ns2:servCity>
  <ns2:servState>WI</ns2:servState>
  <ns2:servZip>54742</ns2:servZip>
  <ns2:servType />
  <ns2:revenueClass></ns2:revenueClass>
  <ns2:servStatus>1</ns2:servStatus>
  <ns2:billingCycle>1</ns2:billingCycle>
  <ns2:route>3</ns2:route>
  <ns2:acRecvBal>0.0</ns2:acRecvBal>
  <ns2:connectDate>1995-06-01T05:00:00.000Z</ns2:connectDate>
  <ns2:network>
    <ns2:boardDist>4</ns2:boardDist>
    <ns2:taxDist />
    <ns2:franchiseDist />
    <ns2:schoolDist />
    <ns2:district>01</ns2:district>
    <ns2:county>10</ns2:county>
    <ns2:cityCode>13</ns2:cityCode>
    <ns2:substationCode>EC</ns2:substationCode>
    <ns2:feeder>1</ns2:feeder>
    <ns2:phaseCd>A</ns2:phaseCd>
    <ns2:eaLoc name="5255" />
    <ns2:section>2</ns2:section>
    <ns2:township>23</ns2:township>
    <ns2:range>1</ns2:range>
    <ns2:linkedTransformer>
      <ns2:bankID>333</ns2:bankID>
      <ns2:unitList>
        <ns2:unitID>17</ns2:unitID>
      </ns2:unitList>
    </ns2:linkedTransformer>
  </ns2:network>
  <ns2:phoneList>
    <ns2:phoneNumber>
      <ns2:phone>8880001111</ns2:phone>
      <ns2:phoneType>Home</ns2:phoneType>
    </ns2:phoneNumber>
    <ns2:phoneNumber>
      <ns2:phone>8880002222</ns2:phone>
      <ns2:phoneType>Mobile</ns2:phoneType>
    </ns2:phoneNumber>
  </ns2:phoneList>
  <ns2:timezone DSTEnabled="true" UTCOffset="-6.0" name="Central Standard Time" />
</ns2:GetServiceLocationByMeterNoResult>

````
The common namespace defined by the Multispeak Spec can be referenced statically when obtaining information using MultiSpeak.MULTISPEAK_RESULT_NAMESACE. 
