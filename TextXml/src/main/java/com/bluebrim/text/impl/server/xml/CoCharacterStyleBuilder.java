package com.bluebrim.text.impl.server.xml;

import org.apache.crimson.tree.*;

import com.bluebrim.text.impl.server.*;
import com.bluebrim.xml.shared.*;

/**
 * Creation date: (2001-04-20 09:16:34)
 * @author: Dennis
 */

public class CoCharacterStyleBuilder extends CoXmlBuilder {

	public CoCharacterStyleBuilder() {
		super();
	}

	public CoCharacterStyleBuilder(XmlDocument xmlDoc) {
		super(xmlDoc);
	}

	public CoCharacterStyleBuilder(XmlDocument xmlDoc, CoXmlBuilderIF builder) {
		super(xmlDoc, builder);
	}

	protected String getTag() {
		return CoCharacterStyle.XML_TAG;
	}
}