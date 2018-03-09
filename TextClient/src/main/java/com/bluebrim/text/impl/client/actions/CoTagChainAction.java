package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;

/**
 * Text editor action that sets the active paragraph tag chain.
 * 
 * @author: Dennis Malmström
 */

public class CoTagChainAction extends CoStyledTextAction
{
public CoTagChainAction( String name )
{
	super( name );
}
public void doit(JTextPane editor, ActionEvent e)
{
	String name = e.getActionCommand();
	com.bluebrim.text.shared.CoStyledDocumentIF doc = (com.bluebrim.text.shared.CoStyledDocumentIF) getStyledDocument(editor);
	
	doc.setActiveChain( name, editor.getCaretPosition() );
}
}
