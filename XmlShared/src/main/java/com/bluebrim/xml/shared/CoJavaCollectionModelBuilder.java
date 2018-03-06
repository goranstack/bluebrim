package com.bluebrim.xml.shared;

import java.util.*;

import org.apache.crimson.tree.*;
import org.w3c.dom.*;

/**
 * This class is used for building <code>Collection</code>s from XML.
 * <p>
 * Creation date: (2001-05-29 15:22:07)
 * 
 * @author Johan Walles
 */
public class CoJavaCollectionModelBuilder extends CoModelBuilder {

	private Collection m_collection;
	public final static String XML_TAG = "java-collection";

	public CoJavaCollectionModelBuilder(CoXmlParserIF parser) {
		super(parser);
	}

	/**
	 * This method should never be called.
	 */
	public void addNode(Node node, CoXmlContext context) {
		//Ignoring unrecognized Collection subnode
	}

	/**
	 * Add an item to the collection.
	 */
	public void addSubModel(String name, Object model, CoXmlContext context) {
		getCollection().add(model);
	}

	/**
	 * Creation date: (2001-05-30 16:44:59)
	 * 
	 * @author Johan Walles
	 * 
	 * @return A guaranteed non-<code>null</code> <code>Collection</code>
	 */
	private Collection getCollection() {
		if (m_collection == null) {
			m_collection = new LinkedList();
		}

		return m_collection;
	}

	/**
	 * Update the named iterator and return it.
	 */
	public Object getModel(Node node, CoXmlContext context) {
		((CoNamedObject) m_model).setObject(getCollection().iterator());

		return super.getModel(node, context);
	}

	public void createModel(Object superModel, Node node, CoXmlContext context) {
		String collectionName = ((ElementNode) node).getAttribute("name");

		m_model = new CoNamedObject(superModel, collectionName);
	}
}