package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.transact.shared.*;

/**
 * Command for moving shape page items.
 *
 * @author: Dennis Malmström
 */

public class CoShapePageItemSetDimensionsCommand extends CoShapePageItemCommand
{





	protected double m_height;
	protected double m_width;

protected CoShapePageItemSetDimensionsCommand( String name )
{
	super( name );
}

public CoUndoCommand doExecute()
{
	CoUndoCommand undo = new CoShapePageItemSetDimensionsUndoCommand( getName(), m_targetView.getShapePageItem() );
	
	m_targetView.getShapePageItem().getMutableCoShape().setSize( m_width, m_height );

	return undo;
}

public void prepare( CoShapePageItemView targetView, double w, double h )
{
	prepare( targetView );
	
	m_width = w;
	m_height = h;

	if ( Double.isNaN( m_width ) ) m_width = m_targetView.getWidth();
	if ( Double.isNaN( m_height ) ) m_height = m_targetView.getHeight();
}
}