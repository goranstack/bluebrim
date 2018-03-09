package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.gui.client.*;

/**
 * Text editor action that set character attribute float values.
 * The value is entered by the user in a dialog.
 * 
 * @author: Dennis Malmström
 */

public class CoOtherFloatCharacterAction extends CoFloatCharacterAction
{
	private String m_title;
	private String m_question;
	
	private static CoInputDialog m_dialog;
public CoOtherFloatCharacterAction( Object attribute,
	                                  String name,
	                                  String title,
	                                  String question )
{
	super(attribute, name);
	
	m_title = title;
	m_question = question;
}
public void doit(JTextPane pane, ActionEvent e)
{
	if
		( m_dialog == null )
	{
		m_dialog = new CoInputDialog( pane );
	}

	m_dialog.setLocationRelativeTo( pane );
	
	String value = m_dialog.openDialog( m_title, m_question );
	if ( value == null || value.equals( "" ) ) return;
	ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), value, e.getModifiers());
	super.doit(pane, ae);
}
}
