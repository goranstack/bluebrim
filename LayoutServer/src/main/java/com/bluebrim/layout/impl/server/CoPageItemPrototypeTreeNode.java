package com.bluebrim.layout.impl.server;

//import com.bluebrim.system.impl.client.CoSystemUIConstants;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.browser.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Node in a tree structure that holds CoPageItemPrototypeCollection's
 *
 * @author: Dennis
 */

public class CoPageItemPrototypeTreeNode extends CoObject implements CoPageItemPrototypeTreeNodeIF
{
	protected String m_name;
	protected List m_children = new ArrayList();
	
	protected PageItemPrototypeCollection m_prototypes = new PageItemPrototypeCollection();
	protected CoAppendingListProxy m_elements = new CoAppendingListProxy( null, null );
	
	private boolean   m_dirty;
	protected boolean m_isRenameable = true;
	protected boolean m_isDeleteable = true;
	protected boolean m_isEditable = true;

	public static final String XML_DELETEABLE = "deleteable";
	public static final String XML_EDITABLE = "editable";
	public static final String XML_NAME = "name";
	public static final String XML_RENAMEABLE = "renameable";
	public static final String XML_TAG = "page-item-prototype-tree-node";

	private static class PageItemPrototypeCollection extends CoPageItemPrototypeCollection
	{
		private boolean m_isMutable = false;
		
		private void enforceImmutability()
		{
			com.bluebrim.base.shared.debug.CoAssertion.assertTrue( m_isMutable, "Attempt to change immutable page item prototype collection." );
		}
		
		public void addPageItemPrototype( CoPageItemPrototypeIF prototype )
		{
			enforceImmutability();
			super.addPageItemPrototype( prototype );
		}
		
		public CoPageItemPrototypeIF addPageItemPrototype( String name, String description, CoShapePageItemIF pageItem )
		{
			enforceImmutability();
			return super.addPageItemPrototype( name, description, pageItem );
		}
		
		public CoPageItemPrototypeIF copyPageItemPrototype( CoPageItemPrototypeIF p, String nameOfCopy )
		{
			enforceImmutability();
			return super.copyPageItemPrototype( p, nameOfCopy );
		}

		public void removePageItemPrototype( CoPageItemPrototypeIF p )
		{
			enforceImmutability();
			super.removePageItemPrototype( p );
		}

		public void removePageItemPrototypes( List prototypes )
		{
			enforceImmutability();
			super.removePageItemPrototypes( prototypes );
		}

		
		public void _addPageItemPrototype( CoPageItemPrototypeIF prototype )
		{
			m_isMutable = true;
			super.addPageItemPrototype( prototype );
			m_isMutable = false;
		}
		
		public CoPageItemPrototypeIF _addPageItemPrototype( String name, String description, CoShapePageItemIF pageItem )
		{
			m_isMutable = true;
			CoPageItemPrototypeIF p = super.addPageItemPrototype( name, description, pageItem );
			m_isMutable = false;
			return p;
		}

		public void _removePageItemPrototypes( List prototypes )
		{
			m_isMutable = true;
			super.removePageItemPrototypes( prototypes );
			m_isMutable = false;
		}

	}

public CoPageItemPrototypeTreeNode(String name)
{
	m_name = name;
}


public CoLayoutTemplateRootFolder makeToRootFolderAsWell(CoLayoutTemplateRootFolder ownerRoot) {
	return new CoPageItemPrototypeSubtreeRoot( (CoPageItemPrototypeTreeRootIF)ownerRoot, this );

}

public CoPageItemPrototypeTreeNodeRIF add( String name )
{
	CoPageItemPrototypeTreeNodeIF x = new CoPageItemPrototypeTreeNode( name );

	m_children.add( x );
	
	markDirty();

	return x;
}
public CoPageItemPrototypeIF add( String name, String description, CoShapePageItemIF pageItem )
{
	CoPageItemPrototypeIF p = m_prototypes._addPageItemPrototype( name, description, pageItem );
	
	markDirty();

	return p;
}
public void add( CoPageItemPrototypeIF prototype )
{
	m_prototypes._addPageItemPrototype( prototype );
	
	markDirty();
}
public CoPageItemPrototypeTreeNodeRIF add( CoPageItemPrototypeTreeNodeRIF node )
{
	m_children.add( node );
	
	markDirty();

	return node;
}
protected void addContentsTo( List l )
{
	l.addAll( m_prototypes.getPageItemPrototypes() );

	int I = m_children.size();
	for
		( int i = 0; i < I; i++ )
	{
		l.add( ( (CoPageItemPrototypeTreeNode) m_children.get( i ) ).getStructure() );
	}
}
protected void copyTo( CoPageItemPrototypeTreeNode dest )
{
	Iterator i = m_prototypes.getPageItemPrototypes().iterator();
	while
		( i.hasNext() )
	{
		dest.m_prototypes.addPageItemPrototype( ( (CoPageItemPrototypeIF) i.next() ).deepClone() );
	}

	i = m_children.iterator();
	while
		( i.hasNext() )
	{
		dest.m_children.add( ( (CoPageItemPrototypeTreeNodeIF) i.next() ).deepClone() );
	}
}
public CoPageItemPrototypeTreeNodeRIF deepClone()
{
	CoPageItemPrototypeTreeNode n = new CoPageItemPrototypeTreeNode( m_name );

	copyTo( n );

	return n;
}

public CoLayoutTemplateFolder getSubFolderByName(String name) {
	return getChildNode(name);
}


public CoPageItemPrototypeTreeNodeRIF getChildNode(String name) {

	Iterator i = getChildrenNodes().iterator();
	while(i.hasNext()){
		CoPageItemPrototypeTreeNodeRIF node = (CoPageItemPrototypeTreeNodeRIF) i.next();
		if(node.getName().equals(name))
			return node;
		node.getChildNode(name);	
	}	
	return null;
}
public List getChildrenNodes() {
	return m_children;
}
public List getElements()
{
	updateElements();
	
	return m_elements;
}
public String getFactoryKey()
{
	return FACTORY_KEY;
}
public String getIconName()
{
	return "BlueFolderElement.gif";
}
public String getIconResourceAnchor()
{
	return CoPageItemPrototypeTreeNode.class.getName();
}
public String getIdentity()
{
	return m_name;
}
public String getName()
{
	return m_name;
}
public CoPageItemPrototypeCollectionIF getPrototypes() // immutable
{
	return m_prototypes;
}
public List getStructure()
{
	List l = new ArrayList();

	l.add( m_name );
	
	addContentsTo( l );
	
	return l;
}
public CoTreeCatalogElementIF getTreeCatalogElement()
{
	return this;
}
public String getType()
{
	return FACTORY_KEY;
}
public boolean isDeleteable()
{
	return m_isDeleteable;
}
public boolean isEditable()
{
	return m_isEditable;
}
public boolean isRenameable()
{
	return m_isRenameable;
}

public CoLayoutTemplate getLayoutTemplateByName(String name) {
	return lookupPageItemPrototype(name);
}

public CoPageItemPrototypeIF lookupPageItemPrototype(String name) {

	CoPageItemPrototypeIF prototype = getPrototypes().getPageItemPrototype(name);
	
	if(prototype == null){		
		Iterator nodes = getChildrenNodes().iterator();
		while(nodes.hasNext())
			((CoPageItemPrototypeTreeNodeIF) nodes.next()).lookupPageItemPrototype(name);
	}
		return prototype; 
}
protected final void markDirty()
{
	m_dirty = ! m_dirty;
	
	// Generate notification when running in non-distributed mode.
	if ( com.bluebrim.base.shared.debug.CoAssertion.SIMULATION_SUPPORT ) com.bluebrim.base.shared.debug.CoAssertion.addChangedObject( this );
}
public void remove( CoPageItemPrototypeTreeNodeRIF child )
{
	m_children.remove( child );

	markDirty();
}
public void removePageItemPrototypes( List prototypes )
{
	m_prototypes._removePageItemPrototypes( prototypes );
}
	public void setDeleteable( boolean b )
	{
		m_isDeleteable = b;
	}
	public void setEditable( boolean b )
	{
		m_isEditable = b;
	}
public void setName( String name )
{
	m_name = name;
}
void setPageItemParent( CoCompositePageItem parent )
{
	if
		( m_children != null )
	{
		int I = m_children.size();
		for
			( int i = 0; i < I; i++ )
		{
			( (CoPageItemPrototypeTreeNode) m_children.get( i ) ).setPageItemParent( parent );
		}
	}

	List prototypes = m_prototypes.getPageItemPrototypes();
	int I = prototypes.size();
	for
		( int i = 0; i < I; i++ )
	{
		( (CoShapePageItem) ( (CoPageItemPrototypeIF) prototypes.get( i ) ).getPageItem() ).setParent( parent );
	}
	
}
	public void setRenameable( boolean b )
	{
		m_isRenameable = b;
	}
public String toString()
{
	return m_name;
}
protected void updateElements()
{
	m_elements.set( m_children, m_prototypes.getPageItemPrototypes() );
}
public void xmlAddSubModel( String name, Object subModel, com.bluebrim.xml.shared.CoXmlContext context )
{
	boolean didHandle = false;
	
	if
		( name == null )
	{
		if
			( subModel instanceof CoPageItemPrototype )
		{
			add( (CoPageItemPrototype) subModel );
			didHandle = true;
		} else if
			( subModel instanceof CoPageItemPrototypeTreeNode )
		{
			add( (CoPageItemPrototypeTreeNode) subModel );
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
public void xmlDone()
{
}
public void xmlInit( Map attributes, com.bluebrim.xml.shared.CoXmlContext context )
{
	setName( com.bluebrim.xml.shared.CoXmlUtilities.parseString( (String) attributes.get( XML_NAME ), getName() ) );

	setRenameable( com.bluebrim.xml.shared.CoXmlUtilities.parseBoolean( (String) attributes.get( XML_RENAMEABLE ), isRenameable() ).booleanValue() );
	setDeleteable( com.bluebrim.xml.shared.CoXmlUtilities.parseBoolean( (String) attributes.get( XML_DELETEABLE ), isDeleteable() ).booleanValue() );
	setEditable( com.bluebrim.xml.shared.CoXmlUtilities.parseBoolean( (String) attributes.get( XML_EDITABLE ), isEditable() ).booleanValue() );
}
public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
	visitor.exportAttribute( XML_NAME, getName() );
	visitor.exportAttribute( XML_RENAMEABLE, ( isRenameable() ? Boolean.TRUE : Boolean.FALSE ).toString() );
	visitor.exportAttribute( XML_DELETEABLE, ( isDeleteable() ? Boolean.TRUE : Boolean.FALSE ).toString() );
	visitor.exportAttribute( XML_EDITABLE, ( isEditable() ? Boolean.TRUE : Boolean.FALSE ).toString() );
	
	visitor.export( m_children.iterator() );
	visitor.export( m_prototypes.getPageItemPrototypes().iterator() );
}

}