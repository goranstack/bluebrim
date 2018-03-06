package com.bluebrim.xml.shared;
import java.io.*;
import java.net.*;

import org.apache.crimson.parser.*;
import org.apache.crimson.tree.*;
import org.xml.sax.*;

import com.bluebrim.resource.shared.*;

/**
 * A CoXmlConsumer is a "factory object" which produces object models
 * from XML. An instance has an array of services for reading XML from
 * different sources and return a constructed business object.
 *
 * The state of the CoXmlConsumer could consist of the directory on disk
 * where it looks for files or some other reference to a source of XML.
 *
 * When called upon the CoXmlConsumer creates an XmlDocument and instantiates
 * a CoXmlParser (or subclass) to parse the XmlDocument.
 *
 * Read more about XML at http://www.w3c.org. A good tutorial can be found at:
 * http://www.xml.com/xml/pub/w3j/s3.walsh.html
 */
public class CoXmlConsumer {
	private CoXmlContext m_context = null;

/**
 * CoXmlConsumer constructor comment.
 */
public CoXmlConsumer(CoXmlContext context) {
	super();
	m_context = context;
}
/**
 * Read a model from an XmlDocument using the default CoBasicXmlParser.
 */
public Object readModelFrom(XmlDocument document) throws CoXmlReadException {
	return (new CoBasicXmlParser(document, m_context)).produceModel(m_context);
}
/**
 * Read the model from a file and validate.
 */
public Object readModelFrom(File file) throws CoXmlReadException {
	return readModelFrom(file, true);
}
/**
 * Read the model from a file.
 */
public Object readModelFrom(File file, boolean validate) throws CoXmlReadException {
	try {
		return readModelFrom(file.toURL(), validate);
	} catch (MalformedURLException e) {
		e.printStackTrace();
		return null;
	}
}
	public Object readModelFrom(InputStream is) throws CoXmlReadException {
		return readModelFrom(new InputSource(is), false);
	}
/**
 * Read the model from a resource and validate.
 */
public Object readModelFrom(Class anchor, String fileName) throws CoXmlReadException {
	return readModelFrom(CoResourceLoader.getURL(anchor, fileName));
}
/**
 * Read the model from a URL and validate.
 */
public Object readModelFrom(URL url) throws CoXmlReadException {
	return readModelFrom(url, true);
}
/**
 * Read the model from a URL.
 */
public Object readModelFrom(URL url, boolean validate) throws CoXmlReadException {
	XmlDocument doc = null;
	m_context.setUrl(url);

	try {
		return readModelFrom(Resolver.createInputSource(url, false), validate);
	} catch(IOException ioe) {
		throw new CoXmlReadException(ioe.toString());
	}
}
protected Object readModelFrom(InputSource input, boolean validate) throws CoXmlReadException {
	XmlDocument doc = null;
	try {
		doc = XmlDocument.createXmlDocument(input, false /*validate*/);
	} catch (Throwable t) {
		String msg = t.getMessage();
		if (t instanceof FileNotFoundException) {
			msg = t.getMessage();
		}
		t.printStackTrace(System.out);
		int lineNo = -1;
		if (t instanceof SAXParseException) {
			lineNo = ((SAXParseException) t).getLineNumber();
		}
		throw (new CoXmlReadException("Error when reading XML file: " + msg + ", line:" + lineNo));
	}
	return readModelFrom(doc);
}
/**
 * Read the model from a string and validate.
 *
 * PENDING: Rename this method (drop the XML part).
 */
public Object readModelFromXML(String xml) throws CoXmlReadException {
	return readModelFromXML(xml, true);
}
/**
 * Read the model from a String, optionally validating.
 *
 * PENDING: Rename this method (drop the XML part).
 */
public Object readModelFromXML(String xml, boolean validate) throws CoXmlReadException{
	XmlDocument doc = null;
	try {
		// turn the filename into an input source
		InputSource input = new InputSource(new StringReader(xml));

		// turn it into an in-memory object
		doc = XmlDocument.createXmlDocument(input, validate);
	} catch (Throwable t) {
		t.printStackTrace();
	}

	return readModelFrom(doc);
}
}