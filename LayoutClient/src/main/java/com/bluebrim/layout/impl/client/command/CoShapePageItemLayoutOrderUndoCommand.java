package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemLayoutOrderUndoCommand extends CoShapePageItemUndoCommand
{
	protected int m_pos;
public CoShapePageItemLayoutOrderUndoCommand( String name, CoShapePageItemIF target, int pos )
{
	super( name, target );
	
	m_pos = pos;
}
public boolean doRedo()
{
	doUndo();
	return true;
}
public boolean doUndo()
{
	if
		( m_pos != -1 )
	{
		m_pos = m_target.setLayoutOrder( m_pos );
	}
	return true;
}
}
