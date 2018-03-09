package com.bluebrim.layout.impl.client.editor;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.*;

import com.bluebrim.base.client.command.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * 
 * Creation date: (2001-05-07 10:00:30)
 * @author: Dennis
 */

public class CoLayoutEditorPageItemTreePane extends CoPageItemTreePane implements MouseListener
{
	private CoLayoutEditor m_editor;


	static class SetLocationSpecCommand extends CoShapePageItemSetObjectCommand
	{
		public SetLocationSpecCommand()
		{
			super( CoCommandStringResources.getName( "SET_LOCATION_SPEC" ) );
		}
		
		public Object getObject( CoShapePageItemView targetView )
		{
			return targetView.getLocationSpec();
		}

		public void setObject( CoShapePageItemIF target, Object ls )
		{
			target.setLayoutSpecs( (CoImmutableLocationSpecIF) ls, null, null );
		}

		protected CoUndoCommand createUndoCommand( String name, CoShapePageItemView targetView, CoShapePageItemSetObjectCommand command, Object originalObject, Object newObject )
		{
			return new CoShapePageItemSetLayoutSpecUndoCommand( name, targetView.getShapePageItem(), command, originalObject, newObject );
		}
	};

public CoLayoutEditorPageItemTreePane( CoLayoutEditor e )
{
	super( e.getUIBuilder(), e.getWorkspace().getSelectionManager() );

	m_editor = e;

	addMouseListener( this );

	ActionListener a = new ActionListener() { public void actionPerformed( ActionEvent ev ) { setLocationSpec( ev.getActionCommand() ); } };
	
	registerKeyboardAction( a, CoImmutableCornerLocationSpecIF.TOP_LEFT,     KeyStroke.getKeyStroke( KeyEvent.VK_Q, KeyEvent.ALT_MASK ), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
	registerKeyboardAction( a, CoImmutableCornerLocationSpecIF.TOP_RIGHT,    KeyStroke.getKeyStroke( KeyEvent.VK_E, KeyEvent.ALT_MASK ), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
	registerKeyboardAction( a, CoImmutableCornerLocationSpecIF.BOTTOM_LEFT,  KeyStroke.getKeyStroke( KeyEvent.VK_Z, KeyEvent.ALT_MASK ), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
	registerKeyboardAction( a, CoImmutableCornerLocationSpecIF.BOTTOM_RIGHT, KeyStroke.getKeyStroke( KeyEvent.VK_C, KeyEvent.ALT_MASK ), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );

	registerKeyboardAction( a, CoImmutableTopLocationIF.TOP_LOCATION,        KeyStroke.getKeyStroke( KeyEvent.VK_W, KeyEvent.ALT_MASK ), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
	registerKeyboardAction( a, CoImmutableBottomLocationIF.BOTTOM_LOCATION,  KeyStroke.getKeyStroke( KeyEvent.VK_X, KeyEvent.ALT_MASK ), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
	registerKeyboardAction( a, CoImmutableLeftLocationIF.LEFT_LOCATION,      KeyStroke.getKeyStroke( KeyEvent.VK_A, KeyEvent.ALT_MASK ), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
	registerKeyboardAction( a, CoImmutableRightLocationIF.RIGHT_LOCATION,    KeyStroke.getKeyStroke( KeyEvent.VK_D, KeyEvent.ALT_MASK ), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
	registerKeyboardAction( a, CoImmutableCenterLocationIF.CENTER_LOCATION,  KeyStroke.getKeyStroke( KeyEvent.VK_S, KeyEvent.ALT_MASK ), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
	
	registerKeyboardAction( a, CoImmutableCornerLocationSpecIF.TOP_OUTSIDE,    KeyStroke.getKeyStroke( KeyEvent.VK_Q, KeyEvent.ALT_MASK | KeyEvent.CTRL_MASK ), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
	registerKeyboardAction( a, CoImmutableCornerLocationSpecIF.TOP_INSIDE,     KeyStroke.getKeyStroke( KeyEvent.VK_E, KeyEvent.ALT_MASK | KeyEvent.CTRL_MASK ), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
	registerKeyboardAction( a, CoImmutableCornerLocationSpecIF.BOTTOM_OUTSIDE, KeyStroke.getKeyStroke( KeyEvent.VK_Z, KeyEvent.ALT_MASK | KeyEvent.CTRL_MASK ), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
	registerKeyboardAction( a, CoImmutableCornerLocationSpecIF.BOTTOM_INSIDE,  KeyStroke.getKeyStroke( KeyEvent.VK_C, KeyEvent.ALT_MASK | KeyEvent.CTRL_MASK ), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
}
private void handleObjectMenu( MouseEvent e )
{
	if ( ! m_editor.getWorkspace().isEnabled() ) return;
	if ( ! e.isPopupTrigger() ) return;

	TreePath tp = getPathForLocation( e.getX(), e.getY() );

	if ( tp.getLastPathComponent() == getModel().getRoot() ) return;

	CoShapePageItemView v = (CoShapePageItemView) tp.getLastPathComponent();

	m_editor.postPopupMenu( e, this, v );
}
public void mouseClicked( MouseEvent e )
{
	if
		( e.getClickCount() > 1 )
	{
	} else {
		handleObjectMenu( e );
	}
}
public void mouseEntered( MouseEvent e )
{
}
public void mouseExited( MouseEvent e )
{
}
public void mousePressed( MouseEvent e )
{
	handleObjectMenu( e );
}
public void mouseReleased( MouseEvent e )
{
	handleObjectMenu( e );
}
private void setLocationSpec( String specKey )
{
	int I = m_selectionManager.getSelectedViewCount();

	if
		( I > 0 )
	{
		CoLayoutSpecFactoryIF f = (CoLayoutSpecFactoryIF) CoFactoryManager.getFactory( CoLayoutSpecIF.LAYOUT_SPEC );
		
		CoCompoundUndoableCommand.INSTANCE.reset( "SET LOCATION SPEC" );
		for
			( int i = 0; i < I; i++ )
		{
			SetLocationSpecCommand c = new SetLocationSpecCommand();
			c.prepare( m_selectionManager.getSelectedView( i ).getView(), f.getLocationSpec( specKey ) );
			CoCompoundUndoableCommand.INSTANCE.add( c );
		}

		CoCompoundUndoableCommand.INSTANCE.prepare();

		m_editor.getCommandExecutor().doit( CoCompoundUndoableCommand.INSTANCE, null );
	}
}
}
