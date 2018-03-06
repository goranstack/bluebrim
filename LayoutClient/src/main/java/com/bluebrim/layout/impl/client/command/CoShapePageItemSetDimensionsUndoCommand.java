package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemSetDimensionsUndoCommand extends CoShapePageItemSetGeometryUndoCommand
{
	protected double m_w;
	protected double m_h;
public CoShapePageItemSetDimensionsUndoCommand( String name, CoShapePageItemIF target )
{
	super( name, target );

	java.awt.geom.Rectangle2D d = m_target.getCoShape().getBounds2D();
	m_w = d.getWidth();
	m_h = d.getHeight();
}
public boolean doRedo()
{
	return doUndo();
}
public boolean doUndo()
{
	preSetGeometry();
	
	double w = m_w;
	double h = m_h;

	java.awt.geom.Rectangle2D d = m_target.getCoShape().getBounds2D();
	
	m_w = d.getWidth();
	m_h = d.getHeight();

	m_target.getMutableCoShape().setSize( w, h );

	postSetGeometry();

	return true;
}
}
