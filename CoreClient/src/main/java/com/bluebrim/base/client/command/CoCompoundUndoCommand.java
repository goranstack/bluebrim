package com.bluebrim.base.client.command;

import java.util.*;

import com.bluebrim.transact.shared.*;

/**
 * 
 * Creation date: (2001-07-05 14:35:34)
 * @author: Dennis
 */
 
public class CoCompoundUndoCommand extends CoUndoCommand
{
	private List m_commands;
public CoCompoundUndoCommand( String name, List commands )
{
	super( name );

	m_commands = commands;
}
public void abortRedo()
{
	int I = m_commands.size();

	for
		( int i = 0; i < I; i++ )
	{
		( (CoUndoCommand) m_commands.get( i ) ).abortRedo();
	}
}
public void abortUndo()
{
	int I = m_commands.size();

	for
		( int i = I - 1; i >= 0; i-- )
	{
		( (CoUndoCommand) m_commands.get( i ) ).abortUndo();
	}
}
public boolean doRedo()
{
	boolean b = true;
	
	int I = m_commands.size();

	for
		( int i = 0; i < I; i++ )
	{
		b = b && ( (CoUndoCommand) m_commands.get( i ) ).doRedo();
	}

	return b;
}
public boolean doUndo()
{
	boolean b = true;
	
	int I = m_commands.size();

	for
		( int i = I - 1; i >= 0; i-- )
	{
		b = b && ( (CoUndoCommand) m_commands.get( i ) ).doUndo();
	}

	return b;
}
public void finishRedo()
{
	int I = m_commands.size();

	for
		( int i = 0; i < I; i++ )
	{
		( (CoUndoCommand) m_commands.get( i ) ).finishRedo();
	}
}
public void finishUndo()
{
	int I = m_commands.size();

	for
		( int i = I - 1; i >= 0; i-- )
	{
		( (CoUndoCommand) m_commands.get( i ) ).finishUndo();
	}
}
public void prepareRedo()
{
	int I = m_commands.size();

	for
		( int i = 0; i < I; i++ )
	{
		( (CoUndoCommand) m_commands.get( i ) ).prepareRedo();
	}
}
public void prepareUndo()
{
	int I = m_commands.size();

	for
		( int i = I - 1; i >= 0; i-- )
	{
		( (CoUndoCommand) m_commands.get( i ) ).prepareUndo();
	}
}
}
