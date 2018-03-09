package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.text.shared.*;

/**
 * Text editor action that sets the paragraph tag of the seloected paragraphs.
 * 
 * @author: Dennis Malmström
 */

public class CoTagParagraphAction extends CoTagAction
{
public CoTagParagraphAction( String name )
{
	super( name );
}
public void doit(JTextPane editor, ActionEvent e)
{
	doit( editor, (com.bluebrim.text.shared.CoStyledDocumentIF) getStyledDocument( editor ), e.getActionCommand() );
}

public static void doit( JTextPane editor, com.bluebrim.text.shared.CoStyledDocumentIF doc, String tag )
{
	try
	{
		if ( tag == null ) tag = CoTextConstants.DEFAULT_TAG_NAME;
		int p0 = editor.getSelectionStart();
		int p1 = editor.getSelectionEnd();
		doc.setParagraphTag(p0, p1 - p0, tag);
	}
	catch (IllegalArgumentException ex)
	{
	}
}
}