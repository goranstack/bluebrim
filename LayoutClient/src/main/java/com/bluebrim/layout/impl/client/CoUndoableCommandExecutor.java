package com.bluebrim.layout.impl.client;

import java.util.*;

import javax.swing.event.*;

import com.bluebrim.gemstone.client.*;
import com.bluebrim.transact.shared.*;


/**
 *
 * @author Dennis
 */
public class CoUndoableCommandExecutor
{

	private final List m_undoableEditListeners = new ArrayList();



public void doit( CoCommand c, Object target )
{
	CoTransactionUtilities.execute( c, target );
}
public void doit( CoUndoableCommand c, Object target )
{
	CoAbstractUndoCommand undo = CoTransactionUtilities.execute( c, target );

	if
		( undo != null )
	{
		fireUndoableEditEvents( undo );
	}
}







public void addUndoableEditListener( UndoableEditListener l )
{
	if ( ! m_undoableEditListeners.contains( l ) ) m_undoableEditListeners.add( l );
}

protected void fireUndoableEditEvents( CoAbstractUndoCommand undoCommand )
{
	UndoableEditEvent ev = new UndoableEditEvent( this, undoCommand );
	
	int I = m_undoableEditListeners.size();
	for
		( int i = 0; i < I; i++ )
	{
		UndoableEditListener l = (UndoableEditListener) m_undoableEditListeners.get( i );
		l.undoableEditHappened( ev );
	}
}

public void removeUndoableEditListener( UndoableEditListener l )
{
	m_undoableEditListeners.remove( l );
}
}