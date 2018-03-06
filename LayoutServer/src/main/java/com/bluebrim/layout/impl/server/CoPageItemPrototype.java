package com.bluebrim.layout.impl.server;

import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.browser.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * A page item prototype is basically a page item with name and description attached.
 * Only the page item must be set, the other attributes can be null (if their UIs allow it).
 *
 * @author: Dennis Malmström
 */

public class CoPageItemPrototype extends CoObject implements CoPageItemPrototypeIF
{
	private CoShapePageItemIF m_prototype;
	private String m_name;
	private String m_description;

	protected boolean m_isRenameable = true;
	protected boolean m_isDeleteable = true;

	private boolean m_dirty;
	public static final String XML_DELETEABLE = "deleteable";
	public static final String XML_DESCRIPTION = "description";
	public static final String XML_NAME = "name";
	public static final String XML_RENAMEABLE = "renameable";
	public static final String XML_TAG = "page-item-prototype";

public CoPageItemPrototype( String name, String description, CoShapePageItemIF p )
{
	m_prototype = p;
	m_name = name;
	m_description = description;
}

public CoPageItemPrototypeIF deepClone()
{
	CoPageItemPrototype p = new CoPageItemPrototype( m_name, m_description, (CoShapePageItemIF) m_prototype.deepClone() );

	p.m_isDeleteable = m_isDeleteable; // ???
	p.m_isRenameable = m_isRenameable; // ???

	return p;
}
public String getDescription()
{
	return m_description;
}
public List getElements()
{
	return null;
}
public String getFactoryKey() 
{
	return FACTORY_KEY;
}
public String getIconName()
{
	return "CoPageItemPrototypeIF.gif";
}
public String getIconResourceAnchor() 
{
	return CoPageItemPrototypeIF.class.getName();
}
public String getIdentity() 
{
	return getName();
}
public String getName()
{
	return m_name;
}
public CoShapePageItemIF getPageItem()
{
	return m_prototype;
}

public CoTreeCatalogElementIF getTreeCatalogElement()
{
	return this;
}
public String getType()
{
	return getName();
}
public boolean isDeleteable()
{
	return m_isDeleteable;
}
public boolean isRenameable()
{
	return m_isRenameable;
}
protected void markDirty()
{
	m_dirty = ! m_dirty;
	
	if (com.bluebrim.base.shared.debug.CoAssertion.SIMULATION_SUPPORT)
		com.bluebrim.base.shared.debug.CoAssertion.addChangedObject( this );
}

public void setDeleteable( boolean b )
{
	m_isDeleteable = b;
	markDirty();
}
public void setDescription(String description)
{
	String old = m_description;
	
	m_description = description;
	markDirty();
	
	firePropertyChange( DESCRIPTION, old, m_description );
}
public void setName( String name )
{
	String old = m_name;
	
	m_name = name;
	markDirty();
	
	firePropertyChange( NAME, old, m_name );
}

public void setRenameable( boolean b )
{
	m_isRenameable = b;
	markDirty();
}
public void xmlAddSubModel( String name, Object subModel, com.bluebrim.xml.shared.CoXmlContext context )
{
	boolean didHandle = false;
	
	if
		( name == null )
	{
		if
			( subModel instanceof CoShapePageItemIF )
		{
			m_prototype = (CoShapePageItemIF) subModel;
			didHandle = true;
		}
	
	} else {
	}

	if
		( ! didHandle ) 
	{		
		System.out.println(getClass().getName() + "  Ignoring sub model : " + subModel );
	}
}
public void xmlInit( Map attributes, com.bluebrim.xml.shared.CoXmlContext context )
{
	setName( com.bluebrim.xml.shared.CoXmlUtilities.parseString( (String) attributes.get( XML_NAME ), getName() ) );
	setDescription( com.bluebrim.xml.shared.CoXmlUtilities.parseString( (String) attributes.get( XML_DESCRIPTION ), getDescription() ) );

	setRenameable( com.bluebrim.xml.shared.CoXmlUtilities.parseBoolean( (String) attributes.get( XML_RENAMEABLE ), isRenameable() ).booleanValue() );
	setDeleteable( com.bluebrim.xml.shared.CoXmlUtilities.parseBoolean( (String) attributes.get( XML_DELETEABLE ), isDeleteable() ).booleanValue() );
}
public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
	visitor.exportAttribute( XML_NAME, getName() );
	visitor.exportAttribute( XML_DESCRIPTION, getDescription() );
	visitor.exportAttribute( XML_RENAMEABLE, ( isRenameable() ? Boolean.TRUE : Boolean.FALSE ).toString() );
	visitor.exportAttribute( XML_DELETEABLE, ( isDeleteable() ? Boolean.TRUE : Boolean.FALSE ).toString() );
	
	visitor.export( getPageItem() );
}

public void xmlImportFinished(Node node, CoXmlContext context) throws CoXmlReadException {
	m_prototype.activateLayoutEngine();
}


public CoShapePageItemIF clonePageItem()
{
	return (CoShapePageItemIF) m_prototype.deepClone();
}

public CoLayout getLayoutCopy() {
	return clonePageItem();
}

public com.bluebrim.transact.shared.CoRef getPageItemId()
{
	return m_prototype.getId();
}

public List getLayouts()
{
	return getPageItems();
}


public List getPageItems()
{
	List l = new ArrayList();
	l.add( getPageItem() );
	return l;
}

public void layoutsChanged()
{
	pageItemsChanged();
}

public void pageItemsChanged()
{
//	firePropertyChange( "prototype", null, m_prototype );
	markDirty();
}
}