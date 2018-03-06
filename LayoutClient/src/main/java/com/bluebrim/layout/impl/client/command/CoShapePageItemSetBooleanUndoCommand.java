package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemSetBooleanUndoCommand extends CoShapePageItemUndoCommand
{
	protected final CoShapePageItemSetBooleanCommand m_command;
	protected boolean m_originalValue;
	protected boolean m_newValue;
public CoShapePageItemSetBooleanUndoCommand( String name, CoShapePageItemIF target, CoShapePageItemSetBooleanCommand command, boolean originalValue, boolean newValue )
{
	super( name, target );
	
	m_command = command;
	m_originalValue = originalValue;
	m_newValue = newValue;
}
public boolean doRedo()
{
	m_command.setBoolean( m_target, m_newValue );
	return true;
}
public boolean doUndo()
{
	m_command.setBoolean( m_target, m_originalValue );
	return true;
}
}
