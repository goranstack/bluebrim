package com.bluebrim.text.impl.client.actions;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Text editor action that set character attribute string values.
 * 
 * @author: Dennis Malmström
 */

public class CoStringCharacterAction extends CoStringAction
{
public CoStringCharacterAction(Object attribute, String actionName)
{
	super(attribute, actionName);
}
public void doit(JTextPane editor, ActionEvent e)
{
	int start = editor.getSelectionStart();
	int end = editor.getSelectionEnd();
	
	MutableAttributeSet attr;
	if
		(start != end)
	{
		attr = new com.bluebrim.text.shared.CoSimpleAttributeSet();
	} else {
		attr = getStyledEditorKit(editor).getInputAttributes();
	}
	
	String s = e.getActionCommand();
	if ( s == CoTextConstants.NO_VALUE ) s = null;
	
	Component c = (Component) e.getSource();
	if
		( c instanceof Co3DToggleButton )
	{
		if ( ! ( (Co3DToggleButton) c ).isSelected() ) s = null;
	}

	if
		( s == null )
	{
		clearCharacterAttribute(editor, m_attribute);
	} else {
		CoStyleConstants.set(attr, m_attribute, s);
		setCharacterAttributes(editor, attr, false);
	}
}
}