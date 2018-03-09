package com.bluebrim.image.impl.server.xml;

import com.bluebrim.image.impl.server.*;
import com.bluebrim.text.impl.server.xml.*;


/**
 * Builds XML for a CoImage instance
 * Creation date: (1999-09-17 12:50:43)
 * @author: 
 */
public class CoImageContentXmlBuilder extends CoTextXmlBuilder {
public CoImageContentXmlBuilder() {
	super();
}

protected String getTag() {
	return CoImageContent.XML_TAG;
}
}