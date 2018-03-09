package com.bluebrim.text.impl.server;

import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Implementation of paragraph tag group.
 * 
 * @author: Dennis Malmström
 */

public class CoTagGroup extends CoObject implements com.bluebrim.text.shared.CoTagGroupIF
{
	private String m_name = "";
	private List m_members = new ArrayList(); // [ String ], paragraph tag names
	private long m_touch = 0;
	public static final String XML_MEMBERS = "members";
	public static final String XML_NAME = "name";
	public static final String XML_TAG = "tag-group";


public CoTagGroup()
{
	super();
}
public CoTagGroup( String name )
{
	super();

	setName( name );
}
public String getFactoryKey()
{
	return FACTORY_KEY;
}
public java.lang.String getIconResourceAnchor()
{
	return null;
}
public java.lang.String getIdentity()
{
	return getName();
}
public String getName()
{
	return m_name;
}
public List getTags()
{
	return m_members;
}
public java.lang.String getType()
{
	return getFactoryKey();
}
public void setName( String name )
{
	m_name = name;
	m_touch++;
}
public void setTags( List g )
{
	m_members = g;
	m_touch++;
}







public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel( Object superModel, org.w3c.dom.Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
	return ( (CoTagGroupCollectionIF) superModel ).createTagGroup( node.getAttributes().getNamedItem( XML_NAME ).getNodeValue() );
}



public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
	visitor.exportAttribute( XML_NAME, m_name );
	visitor.export( XML_MEMBERS, m_members );
}


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
public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	if
		( XML_MEMBERS.equals( parameter ) )
	{
		Iterator i = (Iterator) subModel;
		while
			( i.hasNext() )
		{
			m_members.add( i.next() );
		}
	}
}


/**
 * This method is called after an object and all its sub-objects have
 * been read from an XML file.
 * <p>
 * Creation date: (2001-08-28 15:52:36)
 */
public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
}
}