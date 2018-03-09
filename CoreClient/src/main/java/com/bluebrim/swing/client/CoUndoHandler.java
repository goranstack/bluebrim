package com.bluebrim.swing.client;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

import javax.swing.AbstractAction;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.bluebrim.base.client.command.CoUndoStringResources;
import com.bluebrim.menus.client.CoMenuItem;

public class CoUndoHandler implements UndoableEditListener
{
	
	private final AbstractAction m_redoAction =
		new AbstractAction( "UNDO" )
		{
			public void actionPerformed (ActionEvent e)
			{
				redo();
			}
		};
	private final AbstractAction m_undoAction =
		new AbstractAction( "REDO" )
		{
			public void actionPerformed (ActionEvent e)
			{
				undo();
			}
		};
	
	private final List m_undoMenuItems = new ArrayList();
	private final List m_redoMenuItems = new ArrayList();



	private final UndoManager m_undoManager = new UndoManager();
	{
		m_undoManager.setLimit( 0 ); // infinite
	}




	





public CoUndoHandler()
{
}
public void discardAllEdits()
{
	m_undoManager.discardAllEdits();

	// update menu items
	updateMenuItems();
}


private String getResourceString(String nm)
{
	String str;
	try
	{
		str = CoUndoStringResources.getName(nm);
	}
	catch (MissingResourceException mre)
	{
		str = null;
	}

	return str;
}


private void redo()
{
	if 
		( canRedo() )
	{
		try
		{
			m_undoManager.redo();
		}
		catch (CannotRedoException ex)
		{
			System.out.println(getResourceString("REDO_ERROR_MESSAGE") + ex);
			ex.printStackTrace();
		}
		// update menu items
		updateMenuItems();
	} else {
		java.awt.Toolkit.getDefaultToolkit().beep();
	}
}






private void undo()
{
	if 
		( canUndo() )
	{
		try
		{
			m_undoManager.undo();
		}
		catch (CannotUndoException ex)
		{
			System.out.println(getResourceString("UNDO_ERROR_MESSAGE") + ex);
			ex.printStackTrace();
		}
		// update menu items
		updateMenuItems();
	} else {
		java.awt.Toolkit.getDefaultToolkit().beep();
	}
}
public void undoableEditHappened(UndoableEditEvent e)
{
	m_undoManager.undoableEditHappened(e);

	updateMenuItems();
}
private void updateMenuItems()
{
	updateUndo();
	updateRedo();
}




	{
		m_undoManager.setLimit( 0 ); // infinite
	}

public void addRedoMenuItem( CoMenuItem menuitem )
{
	if
		( m_redoMenuItems.add( menuitem ) )
	{
		update( menuitem, canRedo(), getRedoPresentationName() );
		menuitem.addActionListener( m_redoAction );
	}
}

public void addUndoMenuItem( CoMenuItem menuitem )
{
	if
		( m_undoMenuItems.add( menuitem ) )
	{
		update( menuitem, canUndo(), getUndoPresentationName() );
		menuitem.addActionListener( m_undoAction );
	}
}

public boolean canRedo()
{
	return m_undoManager.canRedo();
}

public boolean canUndo()
{
	return m_undoManager.canUndo();
}

public AbstractAction getRedoAction()
{
	return m_redoAction;
}

private String getRedoPresentationName()
{
	String tmp = m_undoManager.getRedoPresentationName();

	if
		( tmp.equals( "Redo" ) ) // AbstractUndoableEdit.RedoName
	{
		return getResourceString( "Redo" );
	} else {
		return getResourceString( "Redo" ) + " " + tmp;
	}
}

public AbstractAction getUndoAction()
{
	return m_undoAction;
}

private String getUndoPresentationName()
{
	String tmp = m_undoManager.getUndoPresentationName();

	if
		( tmp.equals( "Undo" ) ) // AbstractUndoableEdit.UndoName
	{
		return getResourceString( "Undo" );
	} else {
		return getResourceString( "Undo" ) + " " + tmp;
	}
}

public void removeRedoMenuItem( CoMenuItem menuitem )
{
	if
		( m_redoMenuItems.remove( menuitem ) )
	{
		menuitem.removeActionListener( m_redoAction );
	}
}

public void removeUndoMenuItem( CoMenuItem menuitem )
{
	if
		( m_undoMenuItems.remove( menuitem ) )
	{
		menuitem.removeActionListener( m_undoAction );
	}
}

private void update( CoMenuItem mi, boolean enabled, String label )
{
	mi.setEnabled( enabled );
	mi.setText( label );
}

private void updateRedo()
{
	String str = getRedoPresentationName();
	boolean canRedo = canRedo();
	
	for
		( int i = 0; i < m_redoMenuItems.size(); i++ )
	{
		update( (CoMenuItem) m_redoMenuItems.get( i ), canRedo, str );
	}

}

private void updateUndo()
{
	String str = getUndoPresentationName();
	boolean canUndo = canUndo();
	
	for
		( int i = 0; i < m_undoMenuItems.size(); i++ )
	{
		update( (CoMenuItem) m_undoMenuItems.get( i ), canUndo, str );
	}

}
}