package com.bluebrim.font.impl.shared;
import java.io.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.debug.*;

/**
 * Trivial, immutable implementation of the com.bluebrim.font.shared.CoFontPostscriptData interface.
 * Creation date: (2001-04-24 11:26:33)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public class CoFontPostscriptDataImplementation implements com.bluebrim.font.shared.CoFontPostscriptData {
	private byte[] m_definition;
	private String m_name;











public CoFontPostscriptDataImplementation(byte[] definition, String name) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(definition, "definition");
	if (CoAssertion.ASSERT) CoAssertion.notNull(name, "name");
	
	m_definition = definition;
	m_name = name;
}

public byte[] getPostscriptDefinition() {
	return m_definition;
}

public String getPostscriptName() {
	return m_name;
}





public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) throws com.bluebrim.xml.shared.CoXmlWriteException {
	visitor.exportString(XML_POSTSCRIPT_NAME, m_name);
	try {
		visitor.exportBinary(new ByteArrayInputStream(m_definition), XML_POSTSCRIPT_DEFINITION, ".ps");
	} catch (IOException e) {
		throw new com.bluebrim.xml.shared.CoXmlWriteException("Error writing binary data of postscript font data", e);
	}
}


public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	if (subModel instanceof String) {
		if (XML_POSTSCRIPT_NAME.equals(parameter)) {
			m_name = (String) subModel;
		}
	} else if (subModel instanceof InputStream) {
		try {
			InputStream in = (InputStream) subModel;
			m_definition = new byte[in.available()];
			in.read(m_definition);
		} catch (IOException e) {
			throw new com.bluebrim.xml.shared.CoXmlReadException("Error reading inputstream of Postscript font data", e);
		}
	}

	// Otherwise, ignore for compatibility
}


public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	// Empty by design
}


/**
 * This constructor must only be used from the XML import.
 */
 
private CoFontPostscriptDataImplementation() {
}


/**
 * Called before XML import. See {@link com.bluebrim.xml.shared.CoXmlImportEnabledIF} for details.
 */
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	return new CoFontPostscriptDataImplementation();
}
}