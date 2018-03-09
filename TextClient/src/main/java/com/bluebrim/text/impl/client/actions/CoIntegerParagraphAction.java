package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;
import java.text.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Text editor action that set paragraph attribute integer values.
 * 
 * @author: Dennis Malmström
 */

public class CoIntegerParagraphAction extends CoIntegerAction
{
public CoIntegerParagraphAction(Object attribute, String actionName)
{
	super(attribute, actionName);
}
public void doit(JTextPane editor, ActionEvent e)
{
	MutableAttributeSet attr = new com.bluebrim.text.shared.CoSimpleAttributeSet();
	
	try
	{
		String s = e.getActionCommand();
		if ( ( s == null ) || ( s == CoTextConstants.NO_VALUE ) ) throw new ParseException( null, 0 );
		
		Integer i = new Integer( ( (Number) m_converter.parseObject( s ) ).intValue() );

		CoStyleConstants.set(attr, m_attribute, i);
		setParagraphAttributes(editor, attr, false, true);
	}
	catch (ParseException nfe)
	{
		clearParagraphAttribute(editor, m_attribute);
	}
}
}