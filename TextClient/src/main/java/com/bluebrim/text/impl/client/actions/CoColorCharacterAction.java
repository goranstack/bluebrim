package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Text editor action that sets the foreground color attribute of the selected text.
 * 
 * @author: Dennis Malmström
 */

public class CoColorCharacterAction extends CoColorAction
{

public void doit( JTextPane editor, ActionEvent e )
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

	String colorName = e.getActionCommand();
	if ( colorName == CoTextConstants.NO_VALUE ) colorName = null;

	if
		( colorName != null )
	{
		CoStyleConstants.set( attr, m_attribute, colorName );
		setCharacterAttributes( editor, attr, false );
	} else {
		clearCharacterAttribute( editor, m_attribute );
	}
}

/**
 * CoAction constructor comment.
 */
public CoColorCharacterAction( Object attribute, String name )
{
	super( attribute, name );
}
}