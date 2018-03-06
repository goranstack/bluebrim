package com.bluebrim.layout.impl.server;

import java.util.List;

import com.bluebrim.base.shared.CoObject;
import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.layout.impl.shared.CoPageItemPrototypeCollectionIF;
import com.bluebrim.layout.impl.shared.CoPageItemPrototypeIF;
import com.bluebrim.layout.impl.shared.CoPageItemPrototypeTreeNodeRIF;
import com.bluebrim.layout.impl.shared.CoPageItemPrototypeTreeRootIF;
import com.bluebrim.layout.shared.*;
import com.bluebrim.layout.shared.CoLayout;
import com.bluebrim.layout.shared.CoLayoutTemplate;
import com.bluebrim.layout.shared.CoLayoutTemplateFolder;
import com.bluebrim.layout.shared.CoLayoutTemplateRootFolder;

/**
 * Root of a subtree structure that holds CoPageItemPrototypeCollection's
 *
 * @author: Dennis
 */
 
public class CoPageItemPrototypeSubtreeRoot extends CoObject implements CoPageItemPrototypeTreeRootIF, CoPageItemPrototypeTreeNodeIF
{

	
	private int m_modCount = 0;
	private CoPageItemPrototypeTreeRootIF m_root;
	private CoPageItemPrototypeTreeNodeIF m_subtreeRoot;
	
public CoPageItemPrototypeSubtreeRoot( CoPageItemPrototypeTreeRootIF root, CoPageItemPrototypeTreeNodeIF subtreeRoot )
{
	super();

	m_root = root;
	m_subtreeRoot = subtreeRoot;
}


public void addFolder(CoLayoutTemplateFolder subFolder) {
	addTo(this, (CoPageItemPrototypeTreeNodeRIF)subFolder);
}
	
public void addTemplate(CoLayoutTemplate layoutTemplate) {
	addTo(this, (CoPageItemPrototypeIF)layoutTemplate);
}
	

public CoLayoutTemplateFolder createAndAddFolderTo(CoLayoutTemplateFolder folder,  String name) {
	return addTo( (CoPageItemPrototypeTreeNodeRIF)folder, name );
}
	
public void createAndAddLayoutTemplateTo( CoLayoutTemplateFolder folder, String templateName, String templateDescription, CoLayout layout ) {
	addTo( (CoPageItemPrototypeTreeNodeRIF)folder, templateName, templateDescription, (CoShapePageItemIF)layout);
}
	
public CoLayoutTemplateFolder getSubFolderByName(String name) {
	return getChildNode(name);
}

public CoLayoutTemplateRootFolder makeToRootFolderAsWell(CoLayoutTemplateRootFolder ownerRoot) {
	throw new UnsupportedOperationException("This is already a root");
}

public CoLayoutTemplate getLayoutTemplateByName(String name) {
	return lookupPageItemPrototype(name);
}


public boolean isDeleteable()
{
	return m_subtreeRoot.isDeleteable();
}
public void remove( List nodes )
{
	m_root.remove( nodes );
	markDirty();
}


public void rename( CoPageItemPrototypeIF node, String name )
{
	m_root.rename( node, name );
	markDirty();
}


public CoPageItemPrototypeTreeNodeRIF add( String name )
{
	CoPageItemPrototypeTreeNodeRIF n = m_subtreeRoot.add( name );
	markDirty();
	return n;
}

public CoPageItemPrototypeIF add( String name, String description, CoShapePageItemIF pageItem )
{
	CoPageItemPrototypeIF p = m_subtreeRoot.add( name, description, pageItem );
	markDirty();
	return p;
}

public void add( CoPageItemPrototypeIF prototype )
{
	m_subtreeRoot.add( prototype );
	markDirty();
}

public CoPageItemPrototypeTreeNodeRIF add( CoPageItemPrototypeTreeNodeRIF node )
{
	CoPageItemPrototypeTreeNodeRIF n = m_subtreeRoot.add( node );
	markDirty();
	return n;
}

public CoPageItemPrototypeTreeNodeRIF addTo( CoPageItemPrototypeTreeNodeRIF parent, String name )
{
	CoPageItemPrototypeTreeNodeRIF n = m_root.addTo( parent, name );
	markDirty();
	return n;
}

public void addTo( CoPageItemPrototypeTreeNodeRIF parent, String name, String description, CoShapePageItemIF pageItem )
{
	m_root.addTo( parent, name, description, pageItem );
	markDirty();
}

public void addTo( CoPageItemPrototypeTreeNodeRIF parent, CoPageItemPrototypeIF prototype )
{
	m_root.addTo( parent, prototype );
	markDirty();
}

public CoPageItemPrototypeTreeNodeRIF addTo( CoPageItemPrototypeTreeNodeRIF parent, CoPageItemPrototypeTreeNodeRIF node)
{
	CoPageItemPrototypeTreeNodeRIF n = m_root.addTo( parent, node );
	markDirty();
	return n;
}

public CoPageItemPrototypeTreeNodeRIF deepClone()
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, "Illegal call" );
	return null;
}

public CoPageItemPrototypeTreeNodeRIF getChildNode( String name )
{
	return m_subtreeRoot.getChildNode( name );
}

public List getChildrenNodes()
{
	return m_subtreeRoot.getChildrenNodes();
}

public List getElements()
{
	return m_subtreeRoot.getElements();
}

public String getFactoryKey()
{
	return m_subtreeRoot.getFactoryKey();
}

public String getIconResourceAnchor()
{
	return m_subtreeRoot.getIconResourceAnchor();
}

public String getIdentity()
{
	return m_subtreeRoot.getIdentity();
}

public String getName()
{
	return m_subtreeRoot.getName();
}

public CoPageItemPrototypeCollectionIF getPrototypes()
{
	return m_subtreeRoot.getPrototypes();
}

public List getStructure()
{
	return m_root.getStructure();
}

public CoTreeCatalogElementIF getTreeCatalogElement()
{
	return this;//m_subtreeRoot.getTreeCatalogElement();
}

public String getType()
{
	return m_subtreeRoot.getType();
}

public boolean isEditable()
{
	return m_subtreeRoot.isEditable();
}

public boolean isRenameable()
{
	return m_subtreeRoot.isRenameable();
}

public CoPageItemPrototypeIF lookupPageItemPrototype( String name )
{
	return m_subtreeRoot.lookupPageItemPrototype( name );
}

private void markDirty()
{
	m_modCount++;
}

public void remove( CoPageItemPrototypeTreeNodeRIF node )
{
	m_subtreeRoot.remove( node );
	markDirty();
}

public void removeFrom( CoPageItemPrototypeTreeNodeRIF parent, CoPageItemPrototypeTreeNodeRIF child )
{
	m_root.removeFrom( parent, child );
	markDirty();
}

public void removePageItemPrototypes( java.util.List prototypes )
{
	m_subtreeRoot.removePageItemPrototypes( prototypes );
	markDirty();
}

public void rename( CoPageItemPrototypeTreeNodeRIF node, String name )
{
	m_root.rename( node, name );
	markDirty();
}

public void setDeleteable( boolean b )
{
	m_subtreeRoot.setDeleteable( b );
	markDirty();
}

public void setName( String name )
{
	m_subtreeRoot.setName( name );
	markDirty();
}

public void setRenameable( boolean b )
{
	m_subtreeRoot.setRenameable( b );
	markDirty();
}

/**
 *	Used by a com.bluebrim.xml.shared.CoXmlVisitorIF instance when visiting an object.
 *  The object then uses the com.bluebrim.xml.shared.CoXmlVisitorIF interface to feed the
 *  visitor with state information
 */
public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {}
}