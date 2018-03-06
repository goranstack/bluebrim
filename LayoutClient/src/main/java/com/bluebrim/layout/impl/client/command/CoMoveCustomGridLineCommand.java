package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * 
 *
 * @author: Dennis Malmström
 */

public class CoMoveCustomGridLineCommand extends CoUndoableCommand
{





	protected double m_fromX;
	protected double m_fromY;
	protected CoCustomGridIF m_grid;
	protected double m_toX;
	protected double m_toY;

protected CoMoveCustomGridLineCommand( String name )
{
	super( name );
}

public CoUndoCommand doExecute()
{
	CoUndoCommand undo = new CoCustomGridLineUndoCommand( getName(), m_grid, m_fromX, m_toX, m_fromY, m_toY );

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

	return undo;
}

public void prepare( CoCustomGridIF grid, double fromX, double toX, double fromY, double toY )
{
	prepare();

	m_grid = grid;
	
	m_fromX = fromX;
	m_toX = toX;
	m_fromY = fromY;
	m_toY = toY;
}
}