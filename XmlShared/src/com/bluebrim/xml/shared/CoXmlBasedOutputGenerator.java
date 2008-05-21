package com.bluebrim.xml.shared;

import org.apache.crimson.tree.*;

import com.bluebrim.xml.impl.shared.*;

/**
 * Super class for XML-based output generators
 * Creation date: (2000-04-18 15:24:40)
 * @author: 
 */
public abstract class CoXmlBasedOutputGenerator extends CoAbstractOutputGenerator {
	protected CoXmlProducerIF m_xmlProducer;

	public CoXmlBasedOutputGenerator() {
		super();
		m_xmlProducer = new CoXmlProducer();
	}

	public Object clone() throws CloneNotSupportedException {
		CoXmlBasedOutputGenerator clone = (CoXmlBasedOutputGenerator) super.clone();
		clone.m_xmlProducer = new CoXmlProducer();
		return clone;
	}

	public void dropOutput() {
		// Drop the xml producer which holds the XML doc containing the output
		m_xmlProducer = null;
	}

	public XmlDocument getXmlDocument() {
		return m_xmlProducer == null ? null : m_xmlProducer.getXmlDocument();
	}
}