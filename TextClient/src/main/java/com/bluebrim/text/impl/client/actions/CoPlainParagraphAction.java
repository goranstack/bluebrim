package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.text.shared.*;

/**
 * Text editor action that clears all character style attributes from selected paragraphs.
 * 
 * @author: Dennis Malmström
 */

public class CoPlainParagraphAction extends CoStyledTextAction
{
/**
 * CoAction constructor comment.
 */
public CoPlainParagraphAction( String name )
{
	super( name );
}
public void doit(JTextPane editor, ActionEvent e)
{
	doit( editor, (com.bluebrim.text.shared.CoStyledDocumentIF) getStyledDocument( editor ) );
}

public static void doit( JTextPane editor, com.bluebrim.text.shared.CoStyledDocumentIF doc )
{
	int start = editor.getSelectionStart();
	int len = editor.getSelectionEnd() - start;
	
	doc.unsetParagraphAttributes( start, len, CoTextConstants.PARAGRAPH_STYLE_ATTRIBUTE_SET );
	doc.unsetParagraphAttributes( start, len, CoTextConstants.CHARACTER_FONT_ATTRIBUTE_SET );
	doc.unsetParagraphAttributes( start, len, CoTextConstants.CHARACTER_STYLE_ATTRIBUTE_SET );
}
}