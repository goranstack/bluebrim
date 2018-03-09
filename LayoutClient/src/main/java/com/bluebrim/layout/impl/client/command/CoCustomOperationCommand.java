package com.bluebrim.layout.impl.client.command;

import java.util.*;

import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.transact.shared.*;

/**
 * 
 *
 * @author: Dennis Malmström
 */

public class CoCustomOperationCommand extends CoUndoableCommand
{





	protected CoLayoutEditor m_editor;
	protected List m_operands;
	protected CoPageItemOperationIF m_operation;

protected CoCustomOperationCommand( String name )
{
	super( name );
}

public CoUndoCommand doExecute()
{
	CoUndoCommand undo = ( m_operation.canBeUndone() ) ? new CoCustomOperationUndoCommand( getName(), m_operation, m_editor, m_operands ) : null;

	m_operation.doit( m_editor, m_operands );

	return undo;
}

public void prepare( CoPageItemOperationIF operation, CoLayoutEditor editor, List operands )
{
	prepare();

	m_operation = operation;
	m_editor = editor;
	m_operands = operands;

	setName( m_operation.getName() );
}
}