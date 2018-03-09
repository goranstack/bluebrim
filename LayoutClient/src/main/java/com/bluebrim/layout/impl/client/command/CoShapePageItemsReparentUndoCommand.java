package com.bluebrim.layout.impl.client.command;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.transact.shared.*;

/**
 *
 * @author: Dennis Malmström
 */

public class CoShapePageItemsReparentUndoCommand extends CoUndoCommand
{
	protected CoShapePageItemIF m_children [];


	protected CoCompositePageItemIF m_oldParent;
	protected CoCompositePageItemIF m_newParent;
	protected CoPoint2DDouble m_positions [];

	protected CoPoint2DDouble m_originalPos [];












public CoShapePageItemsReparentUndoCommand( 
	String name, 
	CoShapePageItemIF children [],
	CoCompositePageItemIF oldParent, 
	CoCompositePageItemIF newParent, 
	CoPoint2DDouble pos []
) 
{
	super( name );
	
	m_children = children;
	m_oldParent = oldParent;
	m_newParent = newParent;
	m_positions = pos;

		
	if
		( m_positions != null )
	{
		m_originalPos = new CoPoint2DDouble [ m_children.length ];
		
		for
			( int i = 0; i < m_children.length; i++ )
		{
			if ( m_children[ i ] == null ) continue;
			m_originalPos[ i ] = new CoPoint2DDouble( m_children[ i ].getX(), m_children[ i ].getY() );
		}
	}

}

public boolean doRedo()
{
	for
		( int i = 0; i < m_children.length; i++ )
	{
		if
			( m_children[ i ].isSlave() )
		{
			m_children[ i ] = null;
		}
	}
/*
	boolean isNewParentsLayoutEngineActive = false;
	boolean isOldParentsLayoutEngineActive = false;

	if
		( m_newParent != null )
	{
		isNewParentsLayoutEngineActive = m_newParent.isLayoutEngineActive();
		m_newParent.setLayoutEngineActive( false );
	}

	if
		( m_oldParent != null )
	{
		isOldParentsLayoutEngineActive = m_oldParent.isLayoutEngineActive();
		m_oldParent.setLayoutEngineActive( false );
	}
*/

	
	if
		( m_newParent == null )
	{
		m_oldParent.remove( m_children );
	} else {

		m_newParent.add( m_children, m_positions );
/*
		if
			( m_positions != null )
		{
			for
				( int i = 0; i < m_children.length; i++ )
			{
				if ( m_children[ i ] == null ) continue;
				m_children[ i ].setPosition( m_positions[ i ].getX(), m_positions[ i ].getY() );
			}
		}
		*/
	}

/*
	if
		( m_newParent != null )
	{
		m_newParent.setLayoutEngineActive( isNewParentsLayoutEngineActive );
	}

	if
		( m_oldParent != null )
	{
		m_oldParent.setLayoutEngineActive( isOldParentsLayoutEngineActive );
	}
*/
	return true;
}

public boolean doUndo()
{
	/*
	boolean isNewParentsLayoutEngineActive = false;
	boolean isOldParentsLayoutEngineActive = false;

	if
		( m_newParent != null )
	{
		isNewParentsLayoutEngineActive = m_newParent.isLayoutEngineActive();
		m_newParent.setLayoutEngineActive( false );
	}

	if
		( m_oldParent != null )
	{
		isOldParentsLayoutEngineActive = m_oldParent.isLayoutEngineActive();
		m_oldParent.setLayoutEngineActive( false );
	}
*/



	
	if
		( m_oldParent == null )
	{
		m_newParent.remove( m_children );
	} else {
		m_oldParent.add( m_children, m_originalPos );
/*
		if
			( m_originalPos != null )
		{
			for
				( int i = 0; i < m_children.length; i++ )
			{
				if ( m_children[ i ] == null ) continue;
				m_children[ i ].setPosition( m_originalPos[ i ].getX(), m_originalPos[ i ].getY() );
			}
		}
		*/
	}

/*

	
	if
		( m_newParent != null )
	{
		m_newParent.setLayoutEngineActive( isNewParentsLayoutEngineActive );
	}

	if
		( m_oldParent != null )
	{
		m_oldParent.setLayoutEngineActive( isOldParentsLayoutEngineActive );
	}
*/
	return true;
}
}