package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.text.impl.client.*;

/**
 * Text editor action that inserts a comment.
 * 
 * @author: Dennis Malmström
 */
 
public class CoCommentAction extends CoStyledTextAction
{
/**
 * 
 */
 
public CoCommentAction(String name)
{
	super(name);
}
/**
 * doit method comment.
 */
public void doit( JTextPane editor, ActionEvent e )
{
	( (CoAbstractTextEditor) editor ).insertComment( "" );
}
}
