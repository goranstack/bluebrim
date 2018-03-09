package com.bluebrim.xml.shared;

import java.util.*;

import org.apache.crimson.tree.*;
import org.w3c.dom.*;

/**
 * This class is used for building <code>Map</code>s from XML.
 * <p>
 * Creation date: (2001-06-20 13:06:13)
 * 
 * @author Johan Walles
 */
public class CoJavaMapModelBuilder extends CoModelBuilder {
	public final static String XML_TAG = "java-map";
	private Map m_map = null;

	public CoJavaMapModelBuilder(CoXmlParserIF parser) {
		super(parser);
	}

	/**
	 * This method should never be called.
	 */
	public void addNode(Node node, CoXmlContext context) {
		if (node.getNodeType() != Node.TEXT_NODE) {
			System.out.println("Warning: Ignoring unrecognized Map subnode: " + node.getNodeName());
		}
	}

	/**
	 * Add an item to the collection.
	 */
	public void addSubModel(String name, Object subModel, CoXmlContext context) {
		if (subModel instanceof CoJavaMapEntry) {
			CoJavaMapEntry mapEntry = (CoJavaMapEntry) subModel;

			if (!mapEntry.isComplete()) {
				throw new UnsupportedOperationException("Incomplete map entry");
			}

			m_map.put(mapEntry.getKey(), mapEntry.getValue());
		}
	}

	public void createModel(Object superModel, Node node, CoXmlContext context) {
		String mapName = ((ElementNode) node).getAttribute("name");

		CoNamedObject model = new CoNamedObject(superModel, mapName);

		m_map = new HashMap();
		model.setObject(m_map);

		m_model = model;
	}
}