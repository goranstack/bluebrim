package com.bluebrim.xml.shared;

import java.io.*;

import org.w3c.dom.*;

/**
 *	Output generator that outputs state XML (XML representing business object
 *  state) primarily intended for import/export of business objects
 *
 */
public class CoXmlOutputGenerator extends CoXmlBasedOutputGenerator {
	private CoXmlContext m_context;

	public CoXmlOutputGenerator(CoXmlContext context) {
		super();
		m_context = context;
	}

	public void execute(Object obj) throws CoXmlGenerationException {
		m_xmlProducer.produceStateXML(obj, m_context);
	}


	public String getFactoryKey() {
		return XML_OUTPUT_GENERATOR;
	}

	public String getName() {
		return "XML";
	}

	public boolean isPropertyBased() {
		return false;
	}
	public boolean isToBeOutput(Node node) {
		return true;
	}

	public void output(OutputStream os, int options) throws CoOutputGenerationException {
		OutputStreamWriter osw = new OutputStreamWriter(os);
		output(osw);
	}

	public void output(Writer writer, int options) throws CoOutputGenerationException {
		try {
			// ENCODING???
			m_xmlProducer.getXmlDocument().write(writer, "UTF-8");
		} catch (Exception e) {
			throw (new CoOutputGenerationException("Exception when generating XML output: " + e.getMessage()));
		}
	}

	protected void outputNode(Writer writer, Node node) throws CoOutputGenerationException {
		// The XML output generator does not traverse the XML representation and thus does not
		// need any implementation here.
	}
}