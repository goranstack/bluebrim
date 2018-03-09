package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemSetDoubleUndoCommand extends CoShapePageItemUndoCommand
{
	protected final CoShapePageItemSetDoubleCommand m_command;
	protected double m_originalValue;
	protected double m_newValue;
public CoShapePageItemSetDoubleUndoCommand( String name, CoShapePageItemIF target, CoShapePageItemSetDoubleCommand command, double originalValue, double newValue )
{
	super( name, target );
	
	m_command = command;
	m_originalValue = originalValue;
	m_newValue = newValue;
}
public boolean doRedo()
{
	m_command.setDouble( m_target, m_newValue );
	return true;
}
public boolean doUndo()
{
	m_command.setDouble( m_target, m_originalValue );
	return true;
}
}
