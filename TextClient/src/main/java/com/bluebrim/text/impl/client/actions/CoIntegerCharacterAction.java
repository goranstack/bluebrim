package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;
import java.text.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Text editor action that set character attribute integer values.
 * 
 * @author: Dennis Malmström
 */

public class CoIntegerCharacterAction extends CoIntegerAction
{
public CoIntegerCharacterAction(Object attribute, String actionName)
{
	super(attribute, actionName);
}
public void doit(JTextPane editor, ActionEvent e)
{
	int start = editor.getSelectionStart();
	int end = editor.getSelectionEnd();
	
	MutableAttributeSet attr;
	if
		( start != end )
	{
		attr = new com.bluebrim.text.shared.CoSimpleAttributeSet();
	} else {
		attr = getStyledEditorKit(editor).getInputAttributes();
	}
	
	try
	{
		String s = e.getActionCommand();
		if ( ( s == null ) || ( s == CoTextConstants.NO_VALUE ) ) throw new ParseException( null, 0 );
		
		Integer i = new Integer( ( (Number) m_converter.parseObject( s ) ).intValue() );
		
		CoStyleConstants.set(attr, m_attribute, i);
		setCharacterAttributes(editor, attr, false);
	}
	catch (java.text.ParseException nfe)
	{
		clearCharacterAttribute(editor, m_attribute);
	}
}
}