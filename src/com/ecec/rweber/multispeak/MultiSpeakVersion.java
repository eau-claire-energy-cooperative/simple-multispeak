package com.ecec.rweber.multispeak;

import org.jdom2.Namespace;


/**
 * This represents information about different version of the MultiSpeak protocol. These must be specified when setting up an MultiSpeakEndpoint to make sure the correct SOAP namespaces are used.  
 *  
 * @author rweber
 *
 * */
public enum MultiSpeakVersion {

	Version3(3.0, "http://www.multispeak.org/Version_3.0"),
	Version41(4.0, "http://www.multispeak.org/Version_4.1_Release");
	
	private double version = 0;  // the numerical version of this release
	private String namespace = null;  // the namespace use in SOAP XML requests
	
	MultiSpeakVersion(double version, String namespace) {
		this.version = version;
		this.namespace = namespace;
	}
	
	
	/**
	 * loads the correct MultiSpeakVersion enum object from the given version number
	 * 
	 * @param version Multispeak version to load object of
	 */
	public static MultiSpeakVersion loadVersion(double version) {
		MultiSpeakVersion result = null;
		
		if(version == Version3.getVersion())
		{
			result = MultiSpeakVersion.Version3;
		}
		else if(version == Version41.getVersion())
		{
			result = MultiSpeakVersion.Version41;
		}
		
		return result;
	}
	
	public double getVersion() {
		return version;
	}
	
	public Namespace getNamespace() {
		return Namespace.getNamespace("", this.namespace);
	}
	
	public String getActionURL(String method) {
		return this.namespace + "/" + method;
	}
	
	@Override
	public String toString() {
		return "Version " + this.getVersion();
	}
}
