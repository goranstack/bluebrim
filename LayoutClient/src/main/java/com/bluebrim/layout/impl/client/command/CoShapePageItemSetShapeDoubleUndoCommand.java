package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.shared.*;

/**
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemSetShapeDoubleUndoCommand extends CoShapePageItemSetGeometryUndoCommand
{
	protected final CoShapePageItemSetShapeDoubleCommand m_command;
	protected double m_originalValue;
	protected double m_newValue;
public CoShapePageItemSetShapeDoubleUndoCommand( String name, CoShapePageItemIF target, CoShapePageItemSetShapeDoubleCommand command, double originalValue, double newValue )
{
	super( name, target );
	
	m_command = command;
	m_originalValue = originalValue;
	m_newValue = newValue;
}
public boolean doRedo()
{
	preSetGeometry();

	m_command.setDouble( m_target, m_newValue );

	postSetGeometry();

	return true;
}
public boolean doUndo()
{
	preSetGeometry();

	m_command.setDouble( m_target, m_originalValue );

	postSetGeometry();

	return true;
}
}
