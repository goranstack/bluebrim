package com.bluebrim.layout.impl.client.command;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.transact.shared.*;

/**
 * Command for reshaping shape page items.
 *
 * @author: Dennis Malmström
 */

public class CoShapePageItemReshapeCommand extends CoShapePageItemCommand
{
	protected CoShapeIF m_shape;
	

	





	protected boolean m_isContentSticky;

protected CoShapePageItemReshapeCommand( String name )
{
	super( name );
}

public final CoUndoCommand doExecute()
{
	CoShapeIF oldShape = m_targetView.getCoShape().deepClone();
	oldShape.translateBy( - m_shape.getX(), - m_shape.getY() );

	CoUndoCommand undo = new CoShapePageItemReshapeUndoCommand( getName(), m_targetView.getShapePageItem(), oldShape, m_shape );
	
	m_targetView.getShapePageItem().setCoShape( m_shape );
	
	CoPageItemBoundedContentView bcv = m_isContentSticky ? (CoPageItemBoundedContentView) ( (CoContentWrapperPageItemView) m_targetView ).getContentView() : null;
	if
		( bcv != null )
	{
		bcv.getPageItemBoundedContent().setScaleAndPosition( bcv.getScaleX(), bcv.getScaleY(), bcv.getX(), bcv.getY() );
	}

	return undo;
}

public void prepare( CoShapePageItemView targetView, CoShapeIF shape, boolean isContentSticky )
{
	prepare( targetView );
	m_shape = shape;
	m_isContentSticky = isContentSticky;
}
}