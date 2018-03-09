package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Text editor action that sets the foreground color attribute of the selected paragraphs.
 * 
 * @author: Dennis Malmström
 */

public class CoColorParagraphAction extends CoColorAction
{

public void doit( JTextPane editor, ActionEvent e )
{
	int start = editor.getSelectionStart();
	int end = editor.getSelectionEnd();
	
	MutableAttributeSet attr = new com.bluebrim.text.shared.CoSimpleAttributeSet();

	String colorName = e.getActionCommand();
	if ( colorName == CoTextConstants.NO_VALUE ) colorName = null;

	if
		( colorName != null )
	{
		CoStyleConstants.set( attr, m_attribute, colorName );
		setParagraphAttributes( editor, attr, false, true );
	} else {
		clearParagraphAttribute( editor, m_attribute );
	}
}

/**
 * CoAction constructor comment.
 */
public CoColorParagraphAction( Object attribute, String name )
{
	super( attribute, name );
}
}