package com.bluebrim.layout.impl.server.xml;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.layout.impl.server.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.xml.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-04-18 14:01:39)
 * @author: Dennis
 */

public class CoLayoutEditorModelXmlWrapper implements CoXmlEnabledIF {
	public static final String XML_TAG = "layout-editor-model-xml-wrapper";
	// XML Attribute
	public static final String XML_NAME = "layout-editor-model-name";

	private List m_layers = new ArrayList();
	private CoLayoutEditorModel m_model;
	private String m_name;
	/*
	 * Used at XML import
	 * Helena Rankegård 2001-10-29
	 */

	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		return new CoLayoutEditorModelXmlWrapper(node, context);
	}

	/*
	 * Contructor used for XML import.
	 * Helena Rankegård 2001-10-29
	 */

	protected CoLayoutEditorModelXmlWrapper(Node node, CoXmlContext context) {
		super();

		// xml init
		NamedNodeMap map = node.getAttributes();

		m_model = null;
		m_layers.clear();
		m_name = CoModelBuilder.getAttrVal(map, XML_NAME, "model 1");
	}

	public CoLayoutEditorModelXmlWrapper(CoLayoutEditorModel model) {
		super();

		m_model = model;
	}

	public CoLayoutEditorModel getModel() {
		return m_model;
	}

	/*
	 * Used at XML import
	 * Helena Rankegård 2001-10-29
	 */

	public void xmlAddSubModel(String name, Object subModel, CoXmlContext context) {
		if (subModel instanceof CoPageItemIF) {
			CoPageLayoutArea plla = (CoPageLayoutArea) subModel;

			//		CoIssuePageLayer.createForTesting( plla.getName(), CoLayoutEditorTest.m_issue, CoLayoutEditorTest.m_format, plla );
			m_layers.add(subModel);
		}
	}

	/*
	 * Used at XML import
	 * Helena Rankegård 2001-10-29
	 */

	public void xmlImportFinished(Node node, CoXmlContext context) {
		m_model = new CoLayoutEditorModel(m_layers, m_name);
	}

	/*
	 * Used at XML export
	 * Helena Rankegård 2001-10-29
	 */

	public void xmlVisit(CoXmlVisitorIF visitor) {
		visitor.exportAttribute(XML_NAME, m_model.getName());

		int I = m_model.getLayerCount();
		for (int i = 0; i < I; i++) {
			visitor.export(m_model.getLayer(i).m_pageItem);
		}
	}
}