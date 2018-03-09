package com.bluebrim.layout.impl.server.xml;

import com.bluebrim.layout.impl.server.*;
import com.bluebrim.text.impl.server.xml.*;


/**
 * Builds XML for a CoImage instance
 * Creation date: (1999-09-17 12:50:43)
 * @author: 
 */
public class CoLayoutContentXmlBuilder extends CoTextXmlBuilder {
public CoLayoutContentXmlBuilder() {
	super();
}

protected String getTag()
{
	return CoLayoutContent.XML_TAG;
}
}