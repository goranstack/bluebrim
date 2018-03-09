package com.bluebrim.text.impl.server.xml;
import com.bluebrim.content.impl.server.xml.CoContentXmlBuilder;

/**
 * Builds XML for a CoText instance
 * Creation date: (1999-09-17 12:50:43)
 * @author: 
 */
public class CoTextXmlBuilder extends CoContentXmlBuilder
{
public CoTextXmlBuilder() {
	super();
}


protected String getTag() {
	return com.bluebrim.text.impl.server.CoTextContent.XML_TAG;
}

}