package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.text.shared.*;

/**
 * Text editor action that clears all paragraph attributes from selected characters.
 * 
 * @author: Dennis Malmström
 */

public class CoClearParagraphAttributesAction extends CoStyledTextAction
{
public CoClearParagraphAttributesAction( String name )
{
	super( name );
}
public void doit(JTextPane editor, ActionEvent e)
{
	int start = editor.getSelectionStart();
	int len = editor.getSelectionEnd() - start;
	
	com.bluebrim.text.shared.CoStyledDocumentIF doc = (com.bluebrim.text.shared.CoStyledDocumentIF)editor.getStyledDocument();
	doc.unsetParagraphAttributes( start, len, CoTextConstants.CHARACTER_FONT_ATTRIBUTE_SET );
	doc.unsetParagraphAttributes( start, len, CoTextConstants.CHARACTER_STYLE_ATTRIBUTE_SET );
	doc.unsetParagraphAttributes( start, len, CoTextConstants.PARAGRAPH_STYLE_ATTRIBUTE_SET );
}
}