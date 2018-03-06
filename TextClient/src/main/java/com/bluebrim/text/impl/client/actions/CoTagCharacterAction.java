package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.text.shared.*;

/**
 * Text editor action that sets the character tag of the selected text.
 * 
 * @author: Dennis Malmström
 */

public class CoTagCharacterAction extends CoTagAction
{
public CoTagCharacterAction( String name )
{
	super( name );
}
public void doit( JTextPane editor, ActionEvent e )
{
	doit( editor, (com.bluebrim.text.shared.CoStyledDocumentIF) getStyledDocument( editor ), e.getActionCommand() );
/*
	String tag = e.getActionCommand();
	if ( tag == null ) tag = CoTextConstants.DEFAULT_TAG_NAME;
	try
	{
		int p0 = editor.getSelectionStart();
		int p1 = editor.getSelectionEnd();
		com.bluebrim.text.shared.CoStyledDocumentIF doc = (com.bluebrim.text.shared.CoStyledDocumentIF) getStyledDocument( editor );
		if
			( p0 != p1 )
		{
			doc.setCharacterTag( p0, p1 - p0, tag );
		} else {
			StyledEditorKit k = getStyledEditorKit( editor );
			MutableAttributeSet inputAttributes = k.getInputAttributes();
			inputAttributes.addAttributes( doc.getStyle( tag ) );
		}
	}
	catch (IllegalArgumentException ex)
	{
	}
	*/
}

public static void doit( JTextPane editor, com.bluebrim.text.shared.CoStyledDocumentIF doc, String tag )
{
	try
	{
		if ( tag == null ) tag = CoTextConstants.DEFAULT_TAG_NAME;
		int p0 = editor.getSelectionStart();
		int p1 = editor.getSelectionEnd();
		if
			( p0 != p1 )
		{
			doc.setCharacterTag( p0, p1 - p0, tag );
		} else {
			editor.getInputAttributes().addAttributes( doc.getStyle( tag ) );
		}
	}
	catch (IllegalArgumentException ex)
	{
	}
}
}