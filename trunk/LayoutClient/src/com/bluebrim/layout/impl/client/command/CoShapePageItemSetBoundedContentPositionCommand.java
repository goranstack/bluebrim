package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.transact.shared.*;

/**
 * 
 *
 * @author: Dennis Malmström
 */

public class CoShapePageItemSetBoundedContentPositionCommand extends CoShapePageItemCommand
{





	protected double m_x;
	protected double m_y;

protected CoShapePageItemSetBoundedContentPositionCommand( String name )
{
	super( name );
}

public CoUndoCommand doExecute()
{
	CoUndoCommand undo = new CoShapePageItemSetBoundedContentPositionUndoCommand( getName(), m_targetView.getShapePageItem() );

	getBoundedContentView().getPageItemBoundedContent().setScaleAndPosition( Double.NaN, Double.NaN, m_x, m_y );

	return undo;
}

protected CoPageItemBoundedContentView getBoundedContentView()
{
	return (CoPageItemBoundedContentView) ( (CoContentWrapperPageItemView) m_targetView ).getContentView();
}

public void prepare( CoShapePageItemView targetView, double x, double y )
{
	prepare( targetView );
	
	m_x = x;
	m_y = y;

	if ( Double.isNaN( m_x ) ) m_x = getBoundedContentView().getX();
	if ( Double.isNaN( m_y ) ) m_y = getBoundedContentView().getY();
}


}