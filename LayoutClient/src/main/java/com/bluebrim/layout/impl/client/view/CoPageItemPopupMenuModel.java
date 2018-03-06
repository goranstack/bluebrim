package com.bluebrim.layout.impl.client.view;

import java.util.*;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.menus.client.*;

/**
 * Class for defining page item view object menus.
 * Instances of this class are arranged in a tree where som branches are unconditional and som
 * are traversed only for specific page item view classes.
 *
 * Example: a popup menu that has the entry "x" for all views and the entry "w" for all layout area views.
 *
 * CoPageItemPopupMenuModel TOP
 *  m_subModels = [ CoPageItemPopupMenuModel "x" ]
 *  m_mappedSubModels = [ CoLayoutAreaView -> CoPageItemPopupMenuModel "w" ]
 * 
 * @author: Dennis
 */
 
public class CoPageItemPopupMenuModel
{
	private List m_subModels; // [ CoPageItemPopupMenuModel ], see method create
	
	private Map m_mappedSubModels; // [ Class -> CoPageItemPopupMenuModel ], see method create
// see method create

public void addSubModel( Class c, CoPageItemPopupMenuModel m )
{
	if ( m_mappedSubModels == null ) m_mappedSubModels = new HashMap();
	m_mappedSubModels.put( c, m );
}
// see method create

public void addSubModel( CoPageItemPopupMenuModel m )
{
	if ( m_subModels == null ) m_subModels = new ArrayList();
	m_subModels.add( m );
}
// see method create

public void addSubModels( List m )
{
	if ( m_subModels == null ) m_subModels = new ArrayList();
	m_subModels.addAll( m );
}
// The method that creates the object menu.
//
// CoPopupMenu m - the popup menu
// CoPageItemView v - the page item view for which the menu is posted

public void create( CoPopupMenu m, CoPageItemView v )
{
	// Recursively traverse the tree of unconditional CoPageItemPopupMenuModels.
	// Unconditional CoPageItemPopupMenuModels are activated regardless of the properties of v.
	if
		( m_subModels != null )
	{
		Iterator i = m_subModels.iterator();
		while
			( i.hasNext() )
		{
			( (CoPageItemPopupMenuModel) i.next() ).create( m, v );
		}
	}

	// traverse v's class path applying the appropriate mapped submodels
	if
		( m_mappedSubModels != null )
	{
		create( m, v, v.getClass() );
	}

	// do the content view as well
	if
		( v instanceof CoContentWrapperPageItemView )
	{
		v = ( (CoContentWrapperPageItemView) v ).getContentView();
		create( m, v, v.getClass() );
	}
}
private void create( CoPopupMenu m, CoPageItemView v, Class c )
{
	// start at the top of the class path
	Class sc = c.getSuperclass();
	if
		( sc != null )
	{
		create( m, v, sc );
	}

	// get the submodels that are mapped to c and let them build the menu
	if
		( m_mappedSubModels != null )
	{
		CoPageItemPopupMenuModel subModel = (CoPageItemPopupMenuModel) m_mappedSubModels.get( c );
		if
			( subModel != null )
		{
			subModel.create( m, v );
		}
	}
}
}
