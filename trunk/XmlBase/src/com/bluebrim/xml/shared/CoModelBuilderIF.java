package com.bluebrim.xml.shared;
import java.io.*;
import java.util.*;

import org.w3c.dom.*;

public interface CoModelBuilderIF {
/**
 * When the parsing process encounters an element without a registered parser,
 * this method is called.
 */
public void addNode(Node node, CoXmlContext context);


/**
 * When the parser has encountered a named sub model, this method is called
 * on the parent's builder.
 *
 * @see addSubModel(Object, CoXmlContext)
 * @see CoXmlVisitorIF#export(CoXmlWrapperFlavor, String, CoXmlExportEnabledIF)
 * @see CoXmlVisitorIF#exportBinary(InputStream, String, String)
 */
public void addSubModel(String parameter, Object subModel, CoXmlContext context)
	throws CoXmlReadException;


/**
 * Return the <code>Class</code> of which <code>createModel</code> will create an
 * instance (given the same parameters).
 *
 * @param superModel The object where the current model will be aggregated.
 *
 * @param node The <code>node</code> that initiated the call to this method.
 *
 * @return May return <code>null</code> in case of "don't know".
 */
public Class classToBuild(
	final Object superModel,
	final Node node, 
	final CoXmlContext context);

/**
 * Create an empty model.  Later in the parser process this model will get filled in by
 * calls to {@link addSubModel(Object, CoXmlContext)} and
 * {@link addSubModel(String, Object, CoXmlContext)}.
 * <p>
 * This version of <code>createModel</code> accepts a reference to the super model, i.e.
 * the model where the current model will be aggregated.
 * <p>
 * Creation date: (2001-04-09 09:54:05)
 *
 * @param superModel The object where the current model will be aggregated.
 *
 * @param node The <code>node</code> that initiated the call to this method.
 *
 * @param subModels The children that this model will receive
 */
public void createModel(Object superModel, Collection subModels, Node node, CoXmlContext context)
	throws CoXmlReadException;


/**
 * Create an empty model.  Later in the parser process this model will get filled in by
 * calls to {@link addSubModel(Object, CoXmlContext)} and
 * {@link addSubModel(String, Object, CoXmlContext)}.
 * <p>
 * This version of <code>createModel</code> accepts a reference to the super model, i.e.
 * the model where the current model will be aggregated.
 * <p>
 * Creation date: (2001-04-09 09:54:05)
 *
 * @param superModel The object where the current model will be aggregated.
 *
 * @param node The <code>node</code> that initiated the call to this method.
 */
public void createModel(Object superModel, Node node, CoXmlContext context)
	throws CoXmlReadException;


/**
 * @return The {@link createModel(Node, CoXmlContext) created} and filled in model.
 *
 * @see addSubModel(String, Object, CoXmlContext)
 */
public Object getModel(Node node, CoXmlContext context);


/**
 * Copy information from the given builder.
 *
 * @param builder Source
 */
public void initializeFrom(CoModelBuilderIF builder);


/**
 * Reset the model builder's internal state (if any).
 */
public void resetState();


/**
 * FIXME: If you know what this method does, please fix this comment.
 *   //Johan Walles, 2001-06-15 11:10:38
 */
public void setParser(CoXmlParserIF parser);


/**
 * This method is called whenever an object has been read from an XML file.
 * <p>
 * Creation date: (2001-04-12 14:12:28)
 *
 * @param attributes The attributes to the XML node representing this object.
 */
public void xmlImportFinished(Node node, CoXmlContext context)
	throws CoXmlReadException;
}