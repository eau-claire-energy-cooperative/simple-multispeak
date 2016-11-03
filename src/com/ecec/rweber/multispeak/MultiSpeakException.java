package com.ecec.rweber.multispeak;

/**
 * 
 * Exception that gets thrown when the Multispeak Endpoint returns an error. Error message will be included as pulled from the SOAP response. 
 * 
 * @author rweber
 *
 */
public class MultiSpeakException extends Exception {
	private static final long serialVersionUID = 460759473769823762L;

	public MultiSpeakException(String message){
		super(message);
	}
}
