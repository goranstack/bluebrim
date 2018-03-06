package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.text.shared.*;

/**
 * Text editor action that clears all character style attributes from selected characters.
 * 
 * @author: Dennis Malmström
 */

public class CoPlainCharacterAction extends CoStyledTextAction
{
/**
 * CoAction constructor comment.
 */
public CoPlainCharacterAction( String name )
{
	super( name );
}
public void doit(JTextPane editor, ActionEvent e)
{
	doit( editor, (com.bluebrim.text.shared.CoStyledDocumentIF) getStyledDocument( editor ) );
}

public static void doit(JTextPane editor, com.bluebrim.text.shared.CoStyledDocumentIF doc )
{
	int start = editor.getSelectionStart();
	int len = editor.getSelectionEnd() - start;
	
	MutableAttributeSet attr;
	if 	
		(len != 0)
	{
		doc.unsetCharacterAttributes( start, len, CoTextConstants.CHARACTER_STYLE_ATTRIBUTE_SET );
		doc.unsetCharacterAttributes( start, len, CoTextConstants.CHARACTER_FONT_ATTRIBUTE_SET );
	}
	else
	{
		attr = editor.getInputAttributes();
		attr.removeAttributes( CoTextConstants.CHARACTER_STYLE_ATTRIBUTE_SET.getAttributeNames() );
		attr.removeAttributes( CoTextConstants.CHARACTER_FONT_ATTRIBUTE_SET.getAttributeNames() );
	}
}
}