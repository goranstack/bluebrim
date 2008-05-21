package com.bluebrim.layout.impl.client.command;

import com.bluebrim.transact.shared.*;

/**
 * 
 * Creation date: (2001-07-02 15:37:11)
 * @author: Dennis
 */
 
public abstract class CoShapePageItemSetShapeDoubleCommand extends CoShapePageItemSetDoubleCommand
{

	
protected CoShapePageItemSetShapeDoubleCommand( String name )
{
	super( name );
}
protected CoUndoCommand createUndoCommand( double originalValue, double newValue )
{
	return new CoShapePageItemSetShapeDoubleUndoCommand( getName(), m_targetView.getShapePageItem(), this, originalValue, newValue );
}
}
