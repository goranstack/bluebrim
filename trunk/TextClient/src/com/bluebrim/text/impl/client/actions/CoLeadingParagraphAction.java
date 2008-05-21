package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Text editor action that set leading values.
 * 
 * @author: Dennis Malmström
 */

public class CoLeadingParagraphAction extends CoFloatParagraphAction
{
public CoLeadingParagraphAction( String actionName )
{
	super( CoTextConstants.LEADING, actionName );
}
public void doit( JTextPane editor, ActionEvent e )
{
	int start = editor.getSelectionStart();
	int end = editor.getSelectionEnd();
		
	String s = e.getActionCommand();
	if
		( ( s == null ) || ( s.length() == 0 ) || ( s == CoTextConstants.NO_VALUE ) )
	{
		clearParagraphAttribute( editor, m_attribute );
		return;
	}

	CoLeading l = CoLeading.parse( s );
	if
		( l == null )
	{
		clearParagraphAttribute( editor, m_attribute );
		return;
	}

	MutableAttributeSet attr = new com.bluebrim.text.shared.CoSimpleAttributeSet();
	CoStyleConstants.set( attr, m_attribute, l );
	setParagraphAttributes( editor, attr, false, true );

	Object o = e.getSource();
	if
		( o instanceof JTextComponent )
	{
		( (JTextComponent) o ).setText( CoLeading.format( l ) );
	}
}
}