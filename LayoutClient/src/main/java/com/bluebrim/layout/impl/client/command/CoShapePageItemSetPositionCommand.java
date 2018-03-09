package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.transact.shared.*;

/**
 * Command for moving shape page items.
 *
 * @author: Dennis Malmström
 */

public class CoShapePageItemSetPositionCommand extends CoShapePageItemCommand
{





	protected double m_x;
	protected double m_y;

protected CoShapePageItemSetPositionCommand( String name )
{
	super( name );
}

public static CoShapePageItemSetPositionCommand create( String name, CoShapePageItemView targetView, double x, double y )
{
	CoShapePageItemSetPositionCommand c = new CoShapePageItemSetPositionCommand( name );
	c.prepare( targetView, x, y );
	return c;
}

public CoUndoCommand doExecute()
{
	CoUndoCommand undo = new CoShapePageItemSetPositionUndoCommand( getName(), m_targetView.getShapePageItem() );
	
	m_targetView.getShapePageItem().setPosition( m_x, m_y );

	return undo;
}

public void prepare( CoShapePageItemView targetView, double x, double y )
{
	prepare( targetView );
	
	m_x = x;
	m_y = y;

	if
		( Double.isNaN( m_x ) )
	{	
		m_x = m_targetView.getX();
	}
	
	if
		( Double.isNaN( m_y ) )
	{
		m_y = m_targetView.getY();
	}

//	setName( "x = " + m_x + ", y = " + m_y );
}
}