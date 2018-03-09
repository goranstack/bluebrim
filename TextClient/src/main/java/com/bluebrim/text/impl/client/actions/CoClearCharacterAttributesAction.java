package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.text.shared.*;

/**
 * Text editor action that clears all character attributes from selected characters.
 * 
 * @author: Dennis Malmström
 */

public class CoClearCharacterAttributesAction extends CoStyledTextAction
{
public CoClearCharacterAttributesAction( String name )
{
	super( name );
}
public void doit(JTextPane editor, ActionEvent e)
{
	int start = editor.getSelectionStart();
	int len = editor.getSelectionEnd() - start;
	
	MutableAttributeSet attr = null;
	if 	
		( len != 0 )
	{
		com.bluebrim.text.shared.CoStyledDocumentIF doc = (com.bluebrim.text.shared.CoStyledDocumentIF)editor.getStyledDocument();
		doc.unsetCharacterAttributes( start, len, CoTextConstants.CHARACTER_FONT_ATTRIBUTE_SET );
		doc.unsetCharacterAttributes( start, len, CoTextConstants.CHARACTER_STYLE_ATTRIBUTE_SET );
	} else {
		attr = editor.getInputAttributes();
		if
			(attr != null)
		{
			attr.removeAttributes( CoTextConstants.CHARACTER_FONT_ATTRIBUTE_SET.getAttributeNames() );
			attr.removeAttributes( CoTextConstants.CHARACTER_STYLE_ATTRIBUTE_SET.getAttributeNames() );
		}
	}
}
}
