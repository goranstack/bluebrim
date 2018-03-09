package com.bluebrim.layout.impl.client.command;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemReshapeUndoCommand extends CoShapePageItemSetGeometryUndoCommand
{
	protected CoShapeIF m_originalShape;
	protected CoShapeIF m_newShape;
	/*
	double m_contentX = Double.NaN;
	double m_contentY;
	double m_contentScaleX;
	double m_contentScaleY;
	*/


public CoShapePageItemReshapeUndoCommand( String name, CoShapePageItemIF target, CoShapeIF originalShape, CoShapeIF newShape )
{
	super( name, target );
	
	m_originalShape = originalShape;
	m_newShape = newShape;
}
public boolean doRedo()
{
	preSetGeometry();
	
	m_target.setCoShape( m_newShape );

	postSetGeometry();
	
	return true;
}
public boolean doUndo()
{
	preSetGeometry();
	
	m_target.setCoShape( m_originalShape );

	postSetGeometry();
	
	return true;
}
}
