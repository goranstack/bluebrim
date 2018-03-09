package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoCustomGridLineUndoCommand extends CoUndoCommand
{
	protected CoCustomGridIF m_grid;
	
	protected double m_fromX;
	protected double m_toX;

	protected double m_fromY;
	protected double m_toY;
public CoCustomGridLineUndoCommand( String name, CoCustomGridIF grid, double fromX, double toX, double fromY, double toY )
{
	super( name );

	m_grid = grid;
	
	m_fromX = fromX;
	m_toX = toX;

	m_fromY = fromY;
	m_toY = toY;
}
public boolean doRedo()
{
	if
		( ! Double.isNaN( m_fromX ) )
	{
		m_grid.removeXPosition( m_fromX );
	}
	
	if
		( ! Double.isNaN( m_toX ) )
	{
		m_grid.addXPosition( m_toX );
	}
	
	if
		( ! Double.isNaN( m_fromY ) )
	{
		m_grid.removeYPosition( m_fromY );
	}
	
	if
		( ! Double.isNaN( m_toY ) )
	{
		m_grid.addYPosition( m_toY );
	}
	
	return true;
}
public boolean doUndo()
{
	if
		( ! Double.isNaN( m_toX ) )
	{
		m_grid.removeXPosition( m_toX );
	}
	
	if
		( ! Double.isNaN( m_fromX ) )
	{
		m_grid.addXPosition( m_fromX );
	}
	
	if
		( ! Double.isNaN( m_toY ) )
	{
		m_grid.removeYPosition( m_toY );
	}
	
	if
		( ! Double.isNaN( m_fromY ) )
	{
		m_grid.addYPosition( m_fromY );
	}
	
	return true;
}
}
