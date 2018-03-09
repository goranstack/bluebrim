package com.bluebrim.xml.shared;

import org.apache.crimson.tree.*;

import com.bluebrim.xml.impl.shared.*;

/**
 * A CoXmlProducer is the opposite of CoXmlConsumer and produces an XML
 * document from a business object.
 * Read more about XML at http://www.w3c.org. A good tutorial can be found at:
 * http://www.xml.com/xml/pub/w3j/s3.walsh.html
 */
public class CoXmlProducer implements CoXmlProducerIF {
	private XmlDocument m_xmlDocument;

	/**
	 *	Object used to synchronize the creation of XmlDocuments, where
	 *  deadlocks could appear otherwise
	 */
	private static Integer m_sync = new Integer(1);;

protected String formatOutput(Object obj)
{
	if (obj instanceof Boolean)
	{
		boolean value = ((Boolean) obj).booleanValue();
		return value ? "X" : "-";
	}
	return CoXmlBuilder.formatOutput(obj);
}
	public XmlDocument getXmlDocument() {
		return m_xmlDocument;
	}
/**
 * Creates an XmlDocument for an CoXmlExportEnabledIF instance
 * <p>
 * Creation date: (1999-09-02 13:22:18)
 */
public XmlDocument produceStateXML(Object xObj, CoXmlContext context) throws CoXmlGenerationException
{
	m_xmlDocument = new XmlDocument();
	if(context.useDtd()) {
		m_xmlDocument.setDoctype("", context.getDtdName(), "");
	}

	try {
		CoXmlVisitorIF visitor = new CoXmlVisitor(m_xmlDocument, context);
		visitor.exportTop(xObj);

	} catch(Exception e) {
		e.printStackTrace();
		throw(new CoXmlGenerationException("Error when generating XML: " + e.getMessage()));
	}
	return m_xmlDocument;
}
}