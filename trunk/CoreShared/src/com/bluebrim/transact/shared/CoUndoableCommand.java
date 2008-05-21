package com.bluebrim.transact.shared;


/**
 */
 
public abstract class CoUndoableCommand
{
public CoUndoableCommand(String name)
{
	m_name = name;
}

/**
	Som default gör <code>abort</code> ingenting. 
	Omimplementeras vid behov i subklassen.
 */
public void abort ( )
{
}


/**
 */
public void finish ( )
{
}
/**
 */
public void prepare ( )
{
}

	private String m_name;

public abstract CoUndoCommand doExecute(); // returns null -> error

public final CoUndoCommand execute()
{
	prepare();

	CoUndoCommand undo = doExecute();
	
	if
		(	undo != null )
	{
		finish();
	} else {
		abort();
	}

	return undo;
}

public String getName()
{
	return m_name;
}

public void setName( String name )
{
	m_name = name;
}
}