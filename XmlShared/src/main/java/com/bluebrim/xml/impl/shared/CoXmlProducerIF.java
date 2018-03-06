package com.bluebrim.xml.impl.shared;

import org.apache.crimson.tree.*;

/**
 * A CoXmlProducer is the opposite of com.bluebrim.xml.server.CoXmlConsumer and produces an XML
 * document from a business object.
 * Read more about XML at http://www.w3c.org. A good tutorial can be found at:
 * http://www.xml.com/xml/pub/w3j/s3.walsh.html
 */
public interface CoXmlProducerIF {
	public final static String XML_TYPE 	= "propertyXml.type";
	public final static String XML_LEVEL 	= "propertyXml.level";
	public final static String XML_ATTR 	= "propertyXml.attrName";	
	public XmlDocument getXmlDocument();

/**
 * Creates an XmlDocument based on business object state for an com.bluebrim.xml.shared.CoXmlExportEnabledIF instance
 * Creation date: (1999-09-02 13:22:18)
 */
public XmlDocument produceStateXML(Object xObj, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlGenerationException;
}