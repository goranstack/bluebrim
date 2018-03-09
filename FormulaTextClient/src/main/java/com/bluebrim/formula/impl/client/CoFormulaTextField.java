package com.bluebrim.formula.impl.client;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.formula.shared.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.client.*;

/*
 *
 */

public class CoFormulaTextField extends CoTextField {
	private CoPopupMenu m_popupMenu = null;
	private int m_x = 0;
	private int m_y = 0;

	//---------------------------------------------------------

	// action to show popup menu with variables to use
	public static final String showPopupMenuAction = "ShowPopupMenuAction";
	private static class ShowPopupMenuAction extends TextAction {
		public ShowPopupMenuAction() {
			super(showPopupMenuAction);
		}
		public void actionPerformed(ActionEvent e) {
			CoFormulaTextField textField = (CoFormulaTextField) e.getSource();
			if (textField.m_popupMenu == null)
				return;
			textField.m_popupMenu.show(textField, textField.m_x, textField.m_y);
		}
	}
public CoFormulaTextField() {
	super();
	
	init();
}
private void init () {
	setDocument( new CoFormulaStyledDocument() );
  	setCaret( new CoAtomAwareCaret() );

	// show popup menu when ctrl + space is pressed
	JTextComponent.KeyBinding[] defaultBindings = {
		new JTextComponent.KeyBinding(
						KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, ActionEvent.CTRL_MASK), 
			    		showPopupMenuAction ),
		new JTextComponent.KeyBinding(
						KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0 ), 
						DefaultEditorKit.deletePrevCharAction ),
		new JTextComponent.KeyBinding(
						KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 ), 
			           DefaultEditorKit.deleteNextCharAction )
	};
	
	Keymap map = getKeymap();
	
	Action[] localActions = {
		new ShowPopupMenuAction(),
		new CoAtomAwareDeletePrevAction(),
		new CoAtomAwareDeleteNextAction()
	};

	JTextComponent.loadKeymap(
		map, defaultBindings, TextAction.augmentList( getActions(), localActions ) );
}
protected void processKeyEvent(KeyEvent e)  {
	super.processKeyEvent(e);

	if ((e.getKeyCode() == KeyEvent.VK_TAB || e.getKeyChar() == '\t')) {
		postActionEvent();
		e.consume();
	}
}
protected void processMouseEvent( MouseEvent e )
{
	m_x = e.getX();
	m_y = e.getY();
	
	// popup menu
	if ( ( m_popupMenu != null ) && e.isPopupTrigger() ) {
		m_popupMenu.show(this, m_x, m_y);
		return;
	}

	super.processMouseEvent(e);
}
public void setPopupMenu (CoPopupMenu popupMenu) {	
	m_popupMenu = popupMenu;
}
public String toString () {
	if (getText() == null)
		return "CoFormulaTextField:";
	else
		return "CoFormulaTextField: " + getText();
}
}
