package com.bluebrim.text.impl.client.actions;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Text editor action that set paragraph attribute string values.
 * 
 * @author: Dennis Malmström
 */

public class CoStringParagraphAction extends CoStringAction
{
public CoStringParagraphAction(Object attribute, String actionName)
{
	super(attribute, actionName);
}
public void doit(JTextPane editor, ActionEvent e)
{
	MutableAttributeSet attr = new com.bluebrim.text.shared.CoSimpleAttributeSet();
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
		clearParagraphAttribute(editor, m_attribute);
	} else {
		CoStyleConstants.set(attr, m_attribute, s);
		setParagraphAttributes(editor, attr, false, true);
	}
}
}