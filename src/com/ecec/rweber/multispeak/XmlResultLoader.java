package com.ecec.rweber.multispeak;

import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Implementing class will take the result of a MultiSpeak call and use the result xml object to populate the class
 * 
 * @author rweber
 *
 */
public interface XmlResultLoader {

	/**
	 * @param xml element containing Multispeak results to load into this object
	 */
	public void load(Element xml, Namespace n);
}
