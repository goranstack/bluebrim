package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;

/**
 * Text editor action that inserts a macro.
 * 
 * @author: Dennis Malmström
 */

public class CoMacroAction extends CoStyledTextAction {
/**
 * CoAction constructor comment.
 */
public CoMacroAction( String name )
{
	super( name );
}
public void doit(JTextPane editor, ActionEvent e)
{
	String macro = e.getActionCommand();
	int offset = editor.getSelectionStart();
	int length = editor.getSelectionEnd() - offset;
	com.bluebrim.text.shared.CoStyledDocumentIF doc = (com.bluebrim.text.shared.CoStyledDocumentIF) getStyledDocument(editor);
	doc.insertMacro(offset, length, macro);
}
}
