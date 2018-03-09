package com.bluebrim.text.impl.client.actions;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Text editor action that set character or (depending on selection) paragraph attribute string values.
 * 
 * @author: Dennis Malmstr�m
 */

public class CoStringCharacterOrParagraphAction extends CoStringAction
{
public CoStringCharacterOrParagraphAction(Object attribute, String actionName)
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
		clearCharacterOrParagraphAttribute(editor, m_attribute);
	} else {
		CoStyleConstants.set(attr, m_attribute, s);
		setCharacterOrParagraphAttributes(editor, attr, false);
	}
}
}