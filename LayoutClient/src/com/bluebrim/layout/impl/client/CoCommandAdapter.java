package com.bluebrim.layout.impl.client;

import java.awt.event.*;

import com.bluebrim.transact.shared.*;

/**
 *
 * @author: Dennis
 */
public class CoCommandAdapter implements ActionListener
{
	private static double EQUALITY_TOLERANCE = 1e-4;

	private boolean m_isValid;

	

	protected final CoUndoableCommand m_command;
	private final CoUndoableCommandExecutor m_executor;



	

public final void actionPerformed( ActionEvent ev )
{
	m_isValid = true;

	if
		( precondition( ev ) )
	{
		prepare();
		m_executor.doit( m_command, null );
	} else if
		( ! m_isValid )
	{
		handleInvalidValue( ev );
	}
}



public static boolean isNotEqual( double d0, double d1 )
{
	if ( Double.isNaN( d0 ) ) return true;
	if ( Double.isNaN( d1 ) ) return true;
	
	return Math.abs( d0 - d1 ) >= EQUALITY_TOLERANCE;
}







public CoCommandAdapter( CoUndoableCommandExecutor executor, CoUndoableCommand command )
{
	m_executor = executor;
	m_command = command;
}

protected void handleInvalidValue( ActionEvent ev )
{
}

protected boolean isCorrectDomain( ActionEvent ev )
{
	return true;
}

protected boolean isNewValue( ActionEvent ev )
{
	return true;
}

protected boolean isUpdateInProgress( ActionEvent ev )
{
	return false;
}

protected boolean isValueValid( ActionEvent ev )
{
	return true;
}

protected final boolean precondition( ActionEvent ev )
{
	return ! isUpdateInProgress( ev ) && isCorrectDomain( ev ) && isNewValue( ev ) && ( m_isValid == isValueValid( ev ) );
}

protected void prepare()
{
}
}