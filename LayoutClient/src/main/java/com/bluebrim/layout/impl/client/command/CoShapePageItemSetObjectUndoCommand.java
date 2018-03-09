package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemSetObjectUndoCommand extends CoShapePageItemUndoCommand
{
	protected final CoShapePageItemSetObjectCommand m_command;
	protected Object m_originalValue;
	protected Object m_newValue;
public CoShapePageItemSetObjectUndoCommand( String name, CoShapePageItemIF target, CoShapePageItemSetObjectCommand command, Object originalValue, Object newValue )
{
	super( name, target );
	
	m_command = command;
	m_originalValue = originalValue;
	m_newValue = newValue;
}
public boolean doRedo()
{
	m_command.setObject( m_target, m_newValue );
	return true;
}
public boolean doUndo()
{
	m_command.setObject( m_target, m_originalValue );
	return true;
}
}
