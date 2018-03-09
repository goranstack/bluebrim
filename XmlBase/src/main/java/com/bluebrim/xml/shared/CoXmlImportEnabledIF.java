package com.bluebrim.xml.shared;
import java.util.*;

import org.w3c.dom.*;

/**
 * Implement this interface if you wish to be able to import yourself from XML.  However,
 * since you probably want export too, you should implement <code>com.bluebrim.xml.shared.CoXmlEnabledIF</code>
 * instead.
 * <p>
 * <em>Notice</em> that if you implement this interface, you must also implement the
 * following (static) factory method:
 * <pre>
 *   public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context);
 * </pre>
 * <ul>
 *   <li>The <code>superModel</code> parameter is a reference to the object that will own
 * this one.  It can be safely ignored by almost every class.
 *   <li>The <code>node</code> parameter is a reference to the XML <code>ElementNode</code>
 * that represents the new object.  Many classes will extract some parameters from the
 * <code>node</code>.
 * </ul>
 * <p>
 * Creation date: (2001-06-14 09:51:45)
 * 
 * @author Johan Walles
 *
 * @see com.bluebrim.xml.shared.CoXmlImportLazyCreationIF
 */
public interface CoXmlImportEnabledIF {


/**
 * When the parser has created a sub model to this node, this method will
 * be called.  If the sub model has an identifier, it is passed to this
 * method.
 *
 * @param parameter A sub model identifier (can be <code>null</code>)
 *
 * @param subMmodel The sub model.  The <code>subModel</code> may be a
 * {@link java.io.InputStream InputStream}.  In that case, the actual model is a
 * binary chunk which can be read from that <code>InputStream</code>.
 * Another special case is when you have XML:ed a <code>Collection</code>.  In
 * that case the <code>subModel</code> will be an {@link java.util.Iterator Iterator}
 * from which the original <code>Collection</code> can be reconstructed.
 *
 * @see com.bluebrim.xml.shared.CoXmlVisitorIF#export(CoXmlWrapperFlavor, String, com.bluebrim.xml.shared.CoXmlExportEnabledIF)
 * @see com.bluebrim.xml.shared.CoXmlVisitorIF#export(String, Collection)
 * @see java.io.InputStream
 */
public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context)
	throws com.bluebrim.xml.shared.CoXmlReadException;


/**
 * This method is called after an object and all its sub-objects have
 * been read from an XML file.
 * <p>
 * Creation date: (2001-04-12 14:12:28)
 */
public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context)
	throws com.bluebrim.xml.shared.CoXmlReadException;
}