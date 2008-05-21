package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemSetPositionUndoCommand extends CoShapePageItemSetGeometryUndoCommand
{
	protected double m_x;
	protected double m_y;
public CoShapePageItemSetPositionUndoCommand( String name, CoShapePageItemIF target )
{
	super( name, target );

	m_x = target.getX();
	m_y = target.getY();
}
public boolean doRedo()
{
	return doUndo();
}
public boolean doUndo()
{
	preSetGeometry();
	
	double x = m_x;
	double y = m_y;
	
	m_x = m_target.getX();
	m_y = m_target.getY();

	m_target.setPosition( x, y );

	postSetGeometry();

	return true;
}
}
