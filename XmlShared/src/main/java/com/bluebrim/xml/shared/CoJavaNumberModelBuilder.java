package com.bluebrim.xml.shared;

import org.apache.crimson.tree.*;
import org.w3c.dom.*;

/**
 * This class is used for building <code>Collection</code>s from XML.
 * <p>
 * Creation date: (2001-05-29 15:22:07)
 * 
 * @author Johan Walles
 */
public class CoJavaNumberModelBuilder extends CoModelBuilder {
	public final static String XML_TAG = "java-number";

	public CoJavaNumberModelBuilder(CoXmlParserIF parser) {
		super(parser);
	}

	/**
	 * This method should never be called.
	 */
	public void addNode(Node node, CoXmlContext context) {
		System.out.println("Warning: Ignoring unexpected Number subnode: " + node.getNodeName());
	}

	/**
	 * This method should never be called.
	 */
	public void addSubModel(String name, Object model, CoXmlContext context) {
		System.out.println("Warning: Ignoring unexpected Number sub model: " + model.toString());
	}

	public void createModel(Object superModel, Node node, CoXmlContext context) {
		String name = ((ElementNode) node).getAttribute("name");
		String numberString = ((ElementNode) node).getAttribute("number");
		Number parsedNumber;

		m_model = new CoNamedObject(superModel, name);

		// Attempt to parse the number string as a double
		try {
			parsedNumber = new Double(numberString);
		} catch (NumberFormatException e) {
			parsedNumber = null;
		}

		if (parsedNumber == null) {
			System.out.println("Error parsing \"" + numberString + "\" into a Number!");
		}

		((CoNamedObject) m_model).setObject(parsedNumber);
	}
}