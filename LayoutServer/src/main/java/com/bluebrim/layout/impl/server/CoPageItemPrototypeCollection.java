package com.bluebrim.layout.impl.server;

import java.text.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * A collection of page item prototypes.
 *
 * @author: Dennis Malmström
 */

public class CoPageItemPrototypeCollection extends CoObject implements CoPageItemPrototypeCollectionIF//, CoPropertyChangeListener
{
	private List m_prototypes;
	private boolean m_dirty;
protected CoPageItemPrototypeCollection()
{
	m_prototypes = new ArrayList();
}

public void addPageItemPrototype( CoPageItemPrototypeIF prototype )
{
	m_prototypes.add( prototype );
	
//	prototype.addPropertyChangeListener( this );

	markDirty();
}

/**
 * This method exist only to please xml-import code. Feels bad. /Göran S.
 *
 */
public void add(CoLayoutTemplate layoutTemplate) {
	addPageItemPrototype((CoPageItemPrototypeIF)layoutTemplate);
}

public String getFactoryKey()
{
	return FACTORY_KEY;
}

public List getLayoutHolders()
{
	return getPageItemPrototypes();
}

public List getPageItemHolders()
{
	return getPageItemPrototypes();
}

public CoPageItemPrototypeIF getPageItemPrototype( String name )
{
	Iterator i = m_prototypes.iterator();
	while
		( i.hasNext() )
	{
		CoPageItemPrototypeIF p = (CoPageItemPrototypeIF) i.next();
		if ( p.getName().equals( name ) ) return p;
	}
	return null;
}
public List getPageItemPrototypes()
{
	return m_prototypes;
}

public List getLayoutTemplates()
{
	return m_prototypes;
}

private void markDirty()
{
	m_dirty = !m_dirty;

	if (com.bluebrim.base.shared.debug.CoAssertion.SIMULATION_SUPPORT)
		com.bluebrim.base.shared.debug.CoAssertion.addChangedObject( this );
}
// one of the prototypes changed -> consider the collection changed

public void propertyChange( CoPropertyChangeEvent ev )
{
	markDirty();
}

public void removeLayoutTemplates(List templates) {
	removePageItemPrototypes(templates);
}

public void removePageItemPrototypes( List prototypes )
{
	for
		( int i = 0; i < prototypes.size(); i++ )
	{
		if
			( prototypes.get( i ) instanceof CoPageItemPrototypeIF )
		{
			CoPageItemPrototypeIF p = (CoPageItemPrototypeIF) prototypes.get( i );
			m_prototypes.remove( p );
		
//			p.removePropertyChangeListener( this );

			markDirty();
		}
	}
}
public String toString()
{
	return m_prototypes.toString();
}

public CoLayoutTemplate createAndAddLayoutTemplate( String name, String description, CoLayout layout ) {
	return addPageItemPrototype(name, description, (CoShapePageItemIF)layout);
}

public CoPageItemPrototypeIF addPageItemPrototype( String name, String description, CoShapePageItemIF pageItem )
{
	CoPageItemPrototypeIF p = new CoPageItemPrototype( name, description, pageItem );
	addPageItemPrototype( p );
	return p;
}


public void removePageItemPrototype( CoPageItemPrototypeIF p )
{
	m_prototypes.remove( p );
//	p.removePropertyChangeListener( this );
	markDirty();
}

public CoPageItemPrototypeIF copyPageItemPrototype( CoPageItemPrototypeIF p, String nameOfCopy )
{
	int i = m_prototypes.indexOf( p );

	if ( nameOfCopy == null ) nameOfCopy = p.getName();
	
	p = p.deepClone();
	
	while
		( getPageItemPrototype( nameOfCopy ) != null )
	{
		nameOfCopy += "_";
	}
	
	p.setName( nameOfCopy );

	if
		( i == -1 )
	{
		m_prototypes.add( p );
	} else {
		m_prototypes.add( i + 1, p );
	}
	
//	p.addPropertyChangeListener( this );

	markDirty();

	return p;
}

public static CoPageItemPrototypeCollection create()
{
	CoPageItemPrototypeCollection p = new CoPageItemPrototypeCollection();
	return p;
}

public static CoPageItemPrototypeCollection createAdEditorTools()
{
	String[] tools = {	CoPageItemPrototypeFactoryIF.TEXT_BOX,
						CoPageItemPrototypeFactoryIF.IMAGE_BOX,
						CoPageItemPrototypeFactoryIF.LAYOUT_AREA,
						CoPageItemPrototypeFactoryIF.LINE,
						CoPageItemPrototypeFactoryIF.ORTHOGONAL_LINE,
						CoPageItemPrototypeFactoryIF.POLYGON,
						CoPageItemPrototypeFactoryIF.CURVE		
	};
	
	return createTools(tools, new ArrayList()); 
}

public static CoPageItemPrototypeCollection createEditorialEditorTools()
{
	String[] tools = {	CoPageItemPrototypeFactoryIF.TEXT_BOX,
						CoPageItemPrototypeFactoryIF.WORKPIECE_TEXT_BOX,
						CoPageItemPrototypeFactoryIF.IMAGE_BOX,
						CoPageItemPrototypeFactoryIF.LAYOUT_BOX,
						CoPageItemPrototypeFactoryIF.LAYOUT_AREA,
						CoPageItemPrototypeFactoryIF.LINE,
						CoPageItemPrototypeFactoryIF.ORTHOGONAL_LINE,
						CoPageItemPrototypeFactoryIF.POLYGON,
						CoPageItemPrototypeFactoryIF.CURVE		
	};
	
	List deleteableTools = new ArrayList(1);
	deleteableTools.add(CoPageItemPrototypeFactoryIF.ORTHOGONAL_LINE);
	return createTools(tools, deleteableTools); 
}



public static CoPageItemPrototypeCollection createTools(String[] tools, List deleteableTools)
{
	CoPageItemPrototypeFactoryIF prototypeFactory 	= (CoPageItemPrototypeFactoryIF)CoFactoryManager.getFactory(CoPageItemPrototypeIF.FACTORY_KEY);
	CoPageItemPrototypeCollection prototypeCollection = new CoPageItemPrototypeCollection();	
	String name;
	String description;
	
	for (int i=0; i < tools.length; i++) {
		name = tools[i];		
		CoShapePageItemIF pageItem = prototypeFactory.getPrototypeItem(name);
 		Object[] params = {pageItem.getType()};
		description =  MessageFormat.format(CoPageItemToolResources.getName( CoPageItemToolConstants.CREATE_TOOL_NAME ),params);
		CoPageItemPrototype prototype = new CoPageItemPrototype(name, description, pageItem);
		if (deleteableTools.contains(name))
			prototype.setDeleteable(true);
		else
			prototype.setDeleteable(false);
		prototypeCollection.addPageItemPrototype(prototype);		
	};
	return prototypeCollection;
}

public int indexOf( com.bluebrim.layout.impl.shared.CoPageItemHolderIF pih )
{
	return m_prototypes.indexOf( pih );
}

public int indexOf( CoLayoutHolder layoutHolder )
{
	return m_prototypes.indexOf( layoutHolder );
}

public void renameLayoutTemplate( CoLayoutTemplate template, String newName )
{
	renamePageItemPrototype((CoPageItemPrototypeIF)template, newName);
}

public void renamePageItemPrototype( CoPageItemPrototypeIF prototype, String name )
{
	prototype.setName( name );
	markDirty();
}
}