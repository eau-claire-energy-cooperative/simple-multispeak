package com.ecec.rweber.multispeak;

import org.jdom2.Element;

/**
 * @author rweber
 *
 * Implementing class will take the result of a MultiSpeak call and use the result xml object to populate the class
 */
public interface XmlResultLoader {

	public void load(Element xml);
}
