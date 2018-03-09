package com.bluebrim.text.impl.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Fönster i vilket textkommentarer visas/editeras.
 */
 
class CoCommentWindow extends JWindow implements ActionListener
{
	private JTextField m_textfield;

	private ActionListener m_endOfEditAction;
public CoCommentWindow( Frame owner )
{
	super( owner );
	
	m_textfield = new JTextField();
	m_textfield.setForeground( Color.red );
	getContentPane().add( m_textfield );
}
public void actionPerformed( ActionEvent ev )
{
	if
		( m_endOfEditAction != null )
	{
		m_endOfEditAction.actionPerformed( ev );
	}
	
	setVisible( false );
	
	m_textfield.removeActionListener( this );
}
public void display( String comment, int x, int y )
{
	FontMetrics metrics = m_textfield.getFontMetrics( m_textfield.getFont() );
	Dimension d = m_textfield.getPreferredSize();
	Insets i = m_textfield.getInsets();

	d.width = Math.max( metrics.stringWidth( comment ), 10 * metrics.charWidth( ' ' ) );
	d.width += i.left + i.right;
	
	m_textfield.setPreferredSize( d );
	m_textfield.setText( comment );
	m_textfield.setEditable( false );
	
	pack();
	setLocation( x, y - d.height );
	setVisible( true );
}
public void edit( ActionListener a )
{
	Dimension d = m_textfield.getPreferredSize();
	FontMetrics metrics = m_textfield.getFontMetrics( m_textfield.getFont() );
	d.width += 40 * metrics.charWidth( ' ' );
	m_textfield.setSize( d );
	m_textfield.setEditable( true );

	m_endOfEditAction = a;

	m_textfield.addActionListener( this );
	
	pack();

	m_textfield.requestFocus();
	
//	m_textfield.setCaretPosition( m_textfield.getText().length() );
	m_textfield.selectAll();
}
}
