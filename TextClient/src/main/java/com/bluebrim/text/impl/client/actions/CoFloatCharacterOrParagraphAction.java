package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;
import java.text.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Text editor action that set character or (depending on selection) paragraph attribute float values.
 * 
 * @author: Dennis Malmström
 */

public class CoFloatCharacterOrParagraphAction extends CoFloatAction
{
public CoFloatCharacterOrParagraphAction(Object attribute, String actionName)
{
	super(attribute, actionName);
}
public void doit( JTextPane editor, ActionEvent e )
{
	int start = editor.getSelectionStart();
	int end = editor.getSelectionEnd();
		
	try
	{
		String s = e.getActionCommand();
		if ( ( s == null ) || ( s == CoTextConstants.NO_VALUE ) ) throw new ParseException( null, 0 );
		
		Float size = new Float( ( (Number) m_converter.parseObject( s ) ).floatValue() );
		MutableAttributeSet attr = new com.bluebrim.text.shared.CoSimpleAttributeSet();
		CoStyleConstants.set( attr, m_attribute, size );
		setCharacterOrParagraphAttributes( editor, attr, false );
	}
	catch (ParseException nfe)
	{
		clearCharacterOrParagraphAttribute( editor, m_attribute );
	}
}
}