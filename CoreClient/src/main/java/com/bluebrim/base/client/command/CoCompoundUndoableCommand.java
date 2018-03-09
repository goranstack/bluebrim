package com.bluebrim.base.client.command;

import java.util.ArrayList;
import java.util.List;

import com.bluebrim.transact.shared.CoUndoCommand;
import com.bluebrim.transact.shared.CoUndoableCommand;

/**
 * 
 * Creation date: (2001-07-05 14:42:51)
 * @author: Dennis
 */
 
public class CoCompoundUndoableCommand extends CoUndoableCommand
{
	private List m_commands;

public static final CoCompoundUndoableCommand INSTANCE = new CoCompoundUndoableCommand();
	
private CoCompoundUndoableCommand()
{
	super( "" );
}
public void abort()
{
	int I = m_commands.size();

	for
		( int i = 0; i < I; i++ )
	{
		( (CoUndoableCommand) m_commands.get( i ) ).abort();
	}
}
public void add( CoUndoableCommand c )
{
	m_commands.add( c );
}
public CoUndoCommand doExecute()
{
	List undoCommands = new ArrayList();
	
	int I = m_commands.size();

	for
		( int i = 0; i < I; i++ )
	{
		CoUndoCommand undo = ( (CoUndoableCommand) m_commands.get( i ) ).doExecute();
		if ( undo == null ) return null;
		undoCommands.add( undo );
	}

	return new CoCompoundUndoCommand( getName(), undoCommands );
}
public void finish()
{
	int I = m_commands.size();

	for
		( int i = 0; i < I; i++ )
	{
		( (CoUndoableCommand) m_commands.get( i ) ).finish();
	}
}
public void prepare()
{
	int I = m_commands.size();

	for
		( int i = 0; i < I; i++ )
	{
		( (CoUndoableCommand) m_commands.get( i ) ).prepare();
	}
}
public void reset( String name )
{
	setName( name );
	m_commands = new ArrayList();
}
}
