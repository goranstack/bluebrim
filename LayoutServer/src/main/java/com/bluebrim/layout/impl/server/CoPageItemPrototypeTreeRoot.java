package com.bluebrim.layout.impl.server;

import java.util.Iterator;
import java.util.List;


import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoPageItemPreferencesIF;
import com.bluebrim.layout.impl.shared.CoPageItemPrototypeIF;
import com.bluebrim.layout.impl.shared.CoPageItemPrototypeTreeNodeRIF;
import com.bluebrim.layout.impl.shared.CoPageItemPrototypeTreeRootIF;
import com.bluebrim.layout.impl.shared.CoPageLayoutAreaIF;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.impl.shared.view.CoCompositePageItemView;
import com.bluebrim.layout.impl.shared.view.CoShapePageItemView;
import com.bluebrim.layout.shared.*;

/**
 * Root of a tree structure that holds CoPageItemPrototypeCollection's
 *
 * @author: Dennis
 */
 
public class CoPageItemPrototypeTreeRoot extends CoPageItemPrototypeTreeNode implements CoPageItemPrototypeTreeRootIF
{

	public static final String XML_TAG = "page-item-prototype-tree-root";
	private CoPageItemPreferencesIF m_pageItemPreferences;
	protected CoCompositePageItem m_parentOfAllPrototypes;

	private class ParentOfAllPrototypes extends CoCompositePageItem
	{
		protected CoShapePageItemView createView( CoCompositePageItemView parent, int detailMode )
		{
			return null;
		}
		protected CoShapePageItemView newView( CoCompositePageItemView parent, int detailMode )
		{
			return null;
		}
		
		public String getName()
		{
			return null;
		}
		
		public String getType()
		{
			return null;
		}

		protected Iterator getSiblingsToRunAround( CoShapePageItem child )
		{
			return null;
		}
		
		public String getFactoryKey()
		{
			return null;
		}
	}


public CoLayoutTemplateFolder createAndAddFolderTo(CoLayoutTemplateFolder subFolder,  String folderName) {
	return addTo( (CoPageItemPrototypeTreeNodeRIF)subFolder, folderName );
}


public boolean isDeleteable()
{
	return false;
}
public void remove( List nodes )
{
	remove( this, nodes );
	markDirty();
}
protected static void remove( CoPageItemPrototypeTreeNode parent, List nodes )
{
	parent.m_children.removeAll( nodes );
	parent.removePageItemPrototypes( nodes );

	Iterator i = parent.m_children.iterator();

	while
		( i.hasNext() )
	{
		remove( (CoPageItemPrototypeTreeNode) i.next(), nodes );
	}
}

public void rename( CoPageItemPrototypeIF node, String name )
{
	node.setName( name );
	markDirty();
}



public CoPageItemPrototypeTreeRoot( String name, CoPageItemPreferencesIF pageItemPreferences )
{
	super( name );

	if
		( pageItemPreferences != null )
	{
		m_pageItemPreferences = pageItemPreferences;

		m_parentOfAllPrototypes = new ParentOfAllPrototypes();
		m_parentOfAllPrototypes.setDerivedColumnGrid( false );

		double columnWidth = CoLengthUnit.MM.from( 43 );
		double columnSpacing = CoLengthUnit.MM.from( 3 );
		int columnCount = (int) ( ( CoShapePageItemIF.INFINITE_DIMENSION + columnSpacing ) / ( columnWidth + columnSpacing ) );
		
		m_parentOfAllPrototypes.getMutableCoShape().setWidth( CoShapePageItemIF.INFINITE_DIMENSION );//( columnWidth + columnSpacing ) * columnCount - columnSpacing );
		m_parentOfAllPrototypes.getMutableCoShape().setHeight( CoShapePageItemIF.INFINITE_DIMENSION );//( columnWidth + columnSpacing ) * columnCount - columnSpacing );

		m_parentOfAllPrototypes.getMutableColumnGrid().setColumnCount( columnCount );
		m_parentOfAllPrototypes.getMutableColumnGrid().setColumnSpacing( columnSpacing );

		double tmp = columnCount * ( columnWidth + columnSpacing ) - columnSpacing;
		m_parentOfAllPrototypes.getMutableColumnGrid().setRightMargin( CoShapePageItemIF.INFINITE_DIMENSION - tmp );
	}
}

public void addFolder(CoLayoutTemplateFolder subFolder) {
	addTo(this, (CoPageItemPrototypeTreeNodeRIF)subFolder);
}
	
public void addTemplate(CoLayoutTemplate layoutTemplate) {
	addTo(this, (CoPageItemPrototypeIF)layoutTemplate);
}
	

public CoLayoutTemplateRootFolder makeToRootFolderAsWell(CoLayoutTemplateRootFolder ownerRoot) {
	throw new UnsupportedOperationException("This is already a root");
}


private void postAddTo( CoPageItemPrototypeIF prototype )
{
	if
		( m_parentOfAllPrototypes != null )
	{
		if
			( ! ( prototype.getPageItem() instanceof CoPageLayoutAreaIF ) )
		{
			prototype.getPageItem().setPosition( 0, 0 );
			m_parentOfAllPrototypes.add( prototype.getPageItem() );
		}
	}

	( (CoShapePageItem) prototype.getPageItem() ).setPageItemPreferences( m_pageItemPreferences );

}

public CoPageItemPrototypeTreeNodeRIF addTo( CoPageItemPrototypeTreeNodeRIF parent, String name )
{
	CoPageItemPrototypeTreeNodeRIF n = ( (CoPageItemPrototypeTreeNodeIF) parent ).add( name );
	
	markDirty();
	return n;
}

public void createAndAddLayoutTemplateTo( CoLayoutTemplateFolder folder, String templateName, String templateDescription, CoLayout layout ) {
	addTo( (CoPageItemPrototypeTreeNodeRIF)folder, templateName, templateDescription, (CoShapePageItemIF)layout);
}


public void addTo( CoPageItemPrototypeTreeNodeRIF parent, String name, String description, CoShapePageItemIF pageItem )
{
	CoPageItemPrototypeIF p = ( (CoPageItemPrototypeTreeNodeIF) parent ).add( name, description, pageItem );

	postAddTo( p );

	markDirty();
}

public void addTo( CoPageItemPrototypeTreeNodeRIF parent, CoPageItemPrototypeIF prototype )
{
	( (CoPageItemPrototypeTreeNode) parent ).add( prototype );

	postAddTo( prototype );

	markDirty();
}

public CoPageItemPrototypeTreeNodeRIF addTo( CoPageItemPrototypeTreeNodeRIF parent, CoPageItemPrototypeTreeNodeRIF node )
{
	( (CoPageItemPrototypeTreeNodeIF) parent ).add( node );

	applyPostAddTo( node );

	/*
	if
		( m_textStyleStyleSupplyingParent != null )
	{
		( (CoPageItemPrototypeTreeNode) node ).setPageItemParent( m_textStyleStyleSupplyingParent );
	}
	*/
	
	markDirty();
	return node;
}

private void applyPostAddTo( CoPageItemPrototypeTreeNodeRIF n )
{
	Iterator i = n.getPrototypes().getPageItemPrototypes().iterator();
	while
		( i.hasNext() )
	{
		postAddTo( (CoPageItemPrototypeIF) i.next() );
	}

	i = n.getChildrenNodes().iterator();
	while
		( i.hasNext() )
	{
		applyPostAddTo( (CoPageItemPrototypeTreeNode) i.next() );
	}
}

public void removeFrom( CoPageItemPrototypeTreeNodeRIF parent, CoPageItemPrototypeTreeNodeRIF child )
{
	( (CoPageItemPrototypeTreeNodeIF) parent ).remove( child );
	markDirty();
}

public void rename( CoPageItemPrototypeTreeNodeRIF node, String name )
{
	( (CoPageItemPrototypeTreeNode) node ).setName( name );
	markDirty();
}


}