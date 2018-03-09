package com.bluebrim.text.impl.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;

/**
 * 
 * 
 * @author: Dennis
 */
 
public class CoTextSearchDialog extends CoDialog
{
	private CoTextField m_textField;
	private CoAbstractTextEditor m_editor;
	
	private String m_previousTarget;
	private boolean m_forward;
public CoTextSearchDialog( Dialog owner, CoAbstractTextEditor editor )
{
	super( owner );

	init( editor );
}
public CoTextSearchDialog( Frame owner, CoAbstractTextEditor editor )
{
	super( owner );

	init( editor );
}
private void init( CoAbstractTextEditor editor )
{
	m_editor = editor;
	
	m_textField = new CoTextField();
	m_textField.setColumns( 20 );

	getContentPane().add( m_textField );

	m_textField.getDocument().addDocumentListener(
		new DocumentListener()
		{
			public void changedUpdate( DocumentEvent ev )
			{
			}
			
			public void insertUpdate( DocumentEvent ev )
			{
				targetChanged();
			}
			
			public void removeUpdate( DocumentEvent ev )
			{
				targetChanged();
			}
		}	
	);

	ActionListener listener =
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				if
					( isVisible() )
				{
					if
						( ( m_textField.getText().length() == 0 ) && ( m_previousTarget != null ) )
					{
						m_textField.setText( m_previousTarget );
					} else {
						m_editor.search( true );
					}
				}
			}
		};
		
	m_textField.registerKeyboardAction( listener,
		                                  KeyStroke.getKeyStroke( KeyEvent.VK_S, KeyEvent.CTRL_MASK ),
		                                  JComponent.WHEN_FOCUSED );
		

	listener =
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				if
					( isVisible() )
				{
					if
						( ( m_textField.getText().length() == 0 ) && ( m_previousTarget != null ) )
					{
						m_textField.setText( m_previousTarget );
					} else {
						m_editor.search( false );
					}
				}
			}
		};

	m_textField.registerKeyboardAction( listener,
		                                  KeyStroke.getKeyStroke( KeyEvent.VK_R, KeyEvent.CTRL_MASK ),
		                                  JComponent.WHEN_FOCUSED );
}
public void setForward( boolean forward )
{
	m_forward = forward;
}
public void setVisible( boolean v )
{
	if
		( ! isVisible() && v )
	{
		m_previousTarget = m_textField.getText();
		m_textField.setText( "" );
	}
	
	super.setVisible( v );
}
private void targetChanged()
{
	if
		( isVisible() )
	{
		m_editor.search( m_textField.getText(), m_forward );
	}
}
}
