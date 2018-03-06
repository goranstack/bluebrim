package com.bluebrim.layout.impl.client.command;

import java.awt.geom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Command for reparenting page items.
 *
 * Also see CoPageItemReparentCommand
 *
 * @author: Dennis Malmström
 */

public class CoShapePageItemsReparentCommand extends CoUndoableCommand
{
	protected CoShapePageItemIF m_children [];


	protected CoCompositePageItemIF m_oldParent;
	protected CoCompositePageItemIF m_newParent;
	protected CoPoint2DDouble m_positions [];






private static CoPoint2DDouble [] createArrayOfCoPoint2DDouble( Point2D [] pos )
{
	if ( pos == null ) return null;

	CoPoint2DDouble [] a = new CoPoint2DDouble [ pos.length ];
	for
		( int i = 0; i < a.length; i++ )
	{
		a[ i ] = new CoPoint2DDouble( pos[ i ] );
	}

	return a;
}









	protected com.bluebrim.transact.shared.CoRef m_newParentId;
	protected com.bluebrim.transact.shared.CoRef m_oldParentId;

public CoShapePageItemsReparentCommand() 
{
	super( "REPARENT PAGE ITEMS" );
}

public CoUndoCommand doExecute()
{
	if
		( ( m_oldParent != null ) && ( m_newParent != null ) )
	{
		if
			( ( m_oldParent != null ) && ( m_oldParentId == null ) )
		{
			m_oldParentId = m_oldParent.getId();
		}

		if
			( ( m_newParent != null ) && ( m_newParentId == null ) )
		{
			m_newParentId = m_newParent.getId();
		}

		if
			( m_newParentId.equals( m_oldParentId ) )
		{
			// no operation
			return null;		
		}
	}

	int I = m_children.length;
	for
		( int i = 0; i < m_children.length; i++ )
	{
		if
			( ( m_children[ i ] != null ) && ( m_children[ i ].isSlave() ) )
		{
			m_children[ i ] = null;
			I--;
		}
	}

	if ( I == 0 ) return null;
	
	CoUndoCommand undo = new CoShapePageItemsReparentUndoCommand( getName(), m_children, m_oldParent, m_newParent, m_positions );
	
	if
		( m_newParent == null )
	{
		m_oldParent.remove( m_children );
	} else {
		m_newParent.add( m_children, m_positions );
	}

	return undo;
}

	// PENDING: z and layout orders
public void prepare(
	String name, 
	CoShapePageItemIF children [], 
	CoCompositePageItemIF oldParent,
	com.bluebrim.transact.shared.CoRef oldParentId,
	CoCompositePageItemIF newParent,
	com.bluebrim.transact.shared.CoRef newParentId
) 
{
	prepare( name, children, oldParent, oldParentId, newParent, newParentId, null );
}

public void prepare(
	String name, 
	CoShapePageItemIF children [], 
	CoCompositePageItemIF oldParent,
	com.bluebrim.transact.shared.CoRef oldParentId,
	CoCompositePageItemIF newParent,
	com.bluebrim.transact.shared.CoRef newParentId,
	Point2D pos []
) 
{
	prepare( name, children, oldParent, oldParentId, newParent, newParentId, createArrayOfCoPoint2DDouble( pos ) );
}

public void prepare( 
	String name, 
	CoShapePageItemIF children [],
	CoCompositePageItemIF oldParent,
	com.bluebrim.transact.shared.CoRef oldParentId,
	CoCompositePageItemIF newParent,
	com.bluebrim.transact.shared.CoRef newParentId,
	CoPoint2DDouble pos []
) 
{
	setName( name );
	
	m_children = children;
	m_oldParent = oldParent;
	m_newParent = newParent;
	m_positions = pos;

	m_oldParentId = oldParentId;
	m_newParentId = newParentId;
}
}