package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemSetBoundedContentPositionUndoCommand extends CoShapePageItemUndoCommand
{
	protected double m_x;
	protected double m_y;
public CoShapePageItemSetBoundedContentPositionUndoCommand( String name, CoShapePageItemIF target )
{
	super( name, target );

	m_x = getBoundedContent().getX();
	m_y = getBoundedContent().getY();
}
public boolean doRedo()
{
	return doUndo();
}
public boolean doUndo()
{
	double x = m_x;
	double y = m_y;

	CoPageItemBoundedContentIF bc = getBoundedContent();
	
	m_x = bc.getScaleX();
	m_y = bc.getScaleY();

	bc.setScaleAndPosition( Double.NaN, Double.NaN, x, y );

	return true;
}
protected CoPageItemBoundedContentIF getBoundedContent()
{
	return (CoPageItemBoundedContentIF) ( (CoContentWrapperPageItemIF) m_target ).getContent();
}
}
