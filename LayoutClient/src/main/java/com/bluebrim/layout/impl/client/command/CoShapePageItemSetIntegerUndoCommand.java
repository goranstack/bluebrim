package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemSetIntegerUndoCommand extends CoShapePageItemUndoCommand
{
	protected final CoShapePageItemSetIntegerCommand m_command;
	protected int m_originalValue;
	protected int m_newValue;
public CoShapePageItemSetIntegerUndoCommand( String name, CoShapePageItemIF target, CoShapePageItemSetIntegerCommand command, int originalValue, int newValue )
{
	super( name, target );
	
	m_command = command;
	m_originalValue = originalValue;
	m_newValue = newValue;
}
public boolean doRedo()
{
	m_command.setInteger( m_target, m_newValue );
	return true;
}
public boolean doUndo()
{
	m_command.setInteger( m_target, m_originalValue );
	return true;
}
}
