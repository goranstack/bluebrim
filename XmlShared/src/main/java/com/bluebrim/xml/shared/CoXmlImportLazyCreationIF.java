package com.bluebrim.xml.shared;
import java.util.*;

import org.w3c.dom.*;

/**
 * This is a marker interface indicating that your class needs to know its children before
 * it can be constructed (during XML import).  If you implement this interface, you must
 * implement the following (static) factory method:
 * <p>
 * <pre>
 *   public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Collection subModels, Node node, com.bluebrim.xml.shared.CoXmlContext context);
 * </pre>
 * <ul>
 *   <li>The <code>superModel</code> parameter is a reference to the object that will own
 * this one.  It can be safely ignored by almost every class.
 *   <li>The <code>subModels</code> parameter is a <code>Collection</code> of
 * <code>com.bluebrim.xml.shared.CoNamedObject</code>s representing what will become the children of
 * this <code>Object</code>.
 *   <li>The <code>node</code> parameter is a reference to the XML <code>ElementNode</code>
 * that represents the new object.  Many classes will extract some parameters from the
 * <code>node</code>.
 * </ul>
 * <p>
 * Also, for this marker interface to be effective, the
 * {@link com.bluebrim.xml.server.CoModelBuilder builder class} for this class must implement the
 * {@link CoModelBuilderIF#xmlCreateModel(Object, Collection, Node, com.bluebrim.xml.shared.CoXmlContext)}
 * method.
 * {@link com.bluebrim.xml.impl.server.CoXmlImportEnabledModelBuilder The builder for com.bluebrim.xml.shared.CoXmlEnabledIF classes}
 * do, otherwise you'll have to check.
 * <p>
 * Creation date: (2001-09-20)
 * 
 * @author Johan Walles
 */
public interface CoXmlImportLazyCreationIF {
}