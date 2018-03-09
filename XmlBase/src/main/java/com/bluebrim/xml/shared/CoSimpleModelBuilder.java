package com.bluebrim.xml.shared;
import org.w3c.dom.*;

/**
 * This is the base modelbuilder to subclass when building noncomposite objects.
 * It has empty default implementations for the add-methods.
 */
abstract public class CoSimpleModelBuilder extends com.bluebrim.xml.shared.CoModelBuilder {

/**
 * CoSimpleModelBuilder constructor comment.
 */
public CoSimpleModelBuilder(com.bluebrim.xml.shared.CoXmlParserIF parser) {
	super(parser);
}



/**
 * Add the element to the model.
 * Default is that we do not handle subelements.
 */
public void addNode(Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	// This method intentionally left blank ;-)
}

/**
 * Ignore any sub models.
 */
public void addSubModel(String name, Object model, com.bluebrim.xml.shared.CoXmlContext context)
	throws com.bluebrim.xml.shared.CoXmlReadException
{
	// This method intentionally left blank ;-)
}
}