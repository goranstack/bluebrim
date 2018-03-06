package com.bluebrim.layout.impl.client.command;

import java.util.*;

import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.transact.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoCustomOperationUndoCommand extends CoUndoCommand
{
	protected CoPageItemOperationIF m_operation;
	protected CoLayoutEditor m_editor;
	protected List m_operands;
public CoCustomOperationUndoCommand( String name, CoPageItemOperationIF operation, CoLayoutEditor editor, List operands )
{
	super( name );

	m_operation = operation;
	m_editor = editor;
	m_operands = operands;
}
public boolean doRedo()
{
	m_operation.redoit( m_editor, m_operands );
	
	return true;
}
public boolean doUndo()
{
	m_operation.undoit( m_editor, m_operands );
	
	return true;
}
}
