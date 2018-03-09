package com.bluebrim.xml.shared;
import java.io.*;
import java.util.*;

import com.bluebrim.system.shared.*;

/**
 * An interface that captures common characteristics of XmlVisitors
 * Creation date: (1999-09-02 10:24:14)
 * @author: Mikael Printz
 */
public interface CoXmlVisitorIF {

/**
 * Export a <code>Character</code> to XML.  On import, it
 * will come back as a <code>Character</code>.
 */
public void export(String tag, Character exportMe);


/**
 * Export a <code>Number</code> to XML.
 * <p>
 * Creation date: (2001-05-31 09:52:56)
 * 
 * @author Johan Walles
 */
void export(String name, Number exportMe);


/**
 * Export a <code>Collection</code> with XML:able members.  On reading the
 * <code>Collection</code> back, an <code>Iterator</code> over the
 * <code>Collection</code> will be passed to the <code>addSubModel()</code>
 * method.
 * <p>
 * Creation date: (2001-05-29 10:03:56)
 * 
 * @author Johan Walles
 */
void export(String name, Collection exportMe);


/**
 * Export a <code>Map</code> with XML:able members.
 * XML:able means: being a String or Number (i.e., Float, Double, etc), or implementing
 * CoXmlExportEnabled.
 * <p>
 * Creation date: (2001-06-20 10:11:40)
 * 
 * @author Johan Walles
 */
void export(String name, Map exportMe);


/**
 * Exports xObj as a named element. This can be used when
 * an object has more than one complex children which it wants to
 * be able to distinguish. The child name will be added to the attribute
 * list of the element node in the XML-representation
 * <p>
 * Creation date: (1999-09-03 11:07:28)
 *
 * @deprecated Use {@link export(CoXmlWrapperFlavor, String, com.bluebrim.xml.shared.CoXmlExportEnabledIF)} instead.
 */
public void export(String childName, com.bluebrim.xml.shared.CoXmlExportEnabledIF xObj);


/**
 * Exports the objects referenced by the iterator as elements.  Alternatively you could use {@link export(String, Collection)}.
 * <p>
 * Creation date: (1999-09-03 11:07:28)
 */
public void export(Iterator iter);


/**
 * Add the node to the DOM tree.  Use with caution.
 */
void export(org.w3c.dom.Node node);


/**
 * Exports xObj as an element. This will cause a new node in the XML-tree to be created
 * for xObj.
 * <p>
 * Creation date: (1999-09-03 11:07:28)
 * 
 * @param xObj The XML-enabled object to be exported
 */
public com.bluebrim.xml.shared.CoXmlBuilderIF export(com.bluebrim.xml.shared.CoXmlExportEnabledIF xObj);


/**
 * Exports the objects referenced by the iterator wrapped in a <tt>wrapper</tt> node.
 * The objects pointed out by the iterator must be of type {@link com.bluebrim.xml.shared.CoXmlExportEnabledIF}.
 * <p>
 * Creation date: (2001-04-05 14:31:23)
 *
 * @param wrapperFlavor The wrapper type (<code>com.bluebrim.xml.server.CoXmlWrapperFlavors.NAMED</code> for example)
 *
 * @param wrapperParameter The wrapper parameter (<code>com.bluebrim.stroke.shared.CoStrokePropertiesIF.XML_BGCOLOR</code> for example)
 *
 * @param iterator The iterator pointing out what should be exported
 */
void export(CoXmlWrapperFlavor wrapperFlavor, String wrapperParameter, Iterator iterator);


/**
 * Exports the indicated object, wrapped using the specified wrapper flavor.
 * <p>
 * Most commonly this method is used for naming sub objects
 * (using <code>com.bluebrim.xml.server.CoXmlWrapperFlavors.NAMED</code>); a shape for example could
 * have both a background color and a foreground color.  For telling the
 * difference, put a wrapper <code>NAMED</code> <i>foreground</i> around one and
 * a wrapper named <i>background</i> around the other.  When reading the generated
 * XML, the name will be passed to
 * {@link CoModelBuilderIF#addSubModel(String, Object, com.bluebrim.xml.shared.CoXmlContext)}.
 * <p>
 * Creation date: (2001-04-04 14:19:47)
 *
 * @param wrapperFlavor The wrapper type (<code>com.bluebrim.xml.server.CoXmlWrapperFlavors.NAMED</code> for example)
 *
 * @param wrapperParameter The wrapper parameter (<code>com.bluebrim.stroke.shared.CoStrokePropertiesIF.XML_BGCOLOR</code> for example)
 *
 * @param xObj The object to be exported
 */

void export(CoXmlWrapperFlavor wrapperFlavor, String wrapperParameter, com.bluebrim.xml.shared.CoXmlExportEnabledIF xObj);


/**
 * Exports <code>value</code> as the value for the attribute named name
 * <p>
 * Creation date: (1999-09-03 11:07:28)
 *
 * @param name The name of the attribute
 *
 * @param value The attribute value
 */
public void exportAttribute(String name, String value);

public void exportAttachementFile(File attachement, String identifier);

public void exportAsGOIorObject(String name,  CoXmlExportEnabledIF object);

public void exportGOIAttribute(String name, CoGOI goi);

/**
 * Export binary data.
 * 
 * @param binarySource is a {@link java.io.InputStream} from which the XML framework
 *       will read the binary data.
 *
 * @param identifier will be stored and passed to the method receiving this binary
 *       data during import.
 * 
 * @param suffix is a suggested suffix (<code>.png</code> for example) that can be used if the
 *       binary data is stored in a file.
 */
void exportBinary(InputStream binarySource, String identifier, String suffix)
	throws IOException;



/**
 * Exports a String to the XML file
 *
 * @param tag What tag to use
 *
 * @param exportMe What to export
 */
 
 // FIXME: Is there any reason why this method couldn't be named just "export"?
 // FIXME:   //Johan Walles (2001-05-29 09:45:15)
 
public void exportString(String tag, String exportMe);


/**
 * This is the first visitor method that is called.
 *
 * @param obj The topmost object to export.
 */
public void exportTop(Object obj);


	public com.bluebrim.xml.shared.CoXmlContext getContext();


	public String getCurrentChildName();


	public Object getCurrentModel();
}