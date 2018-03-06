package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.text.impl.client.*;

/**
 * Text editor action that copies attribute values of selected text into the style paste buffer.
 * 
 * @author: Dennis Malmström
 */

public class CoCopyCharacterOrParagraphAttributesAction extends CoStyledTextAction
{
public CoCopyCharacterOrParagraphAttributesAction( String name )
{
	super( name );
}
public void doit( JTextPane editor, ActionEvent e )
{
	AttributeSet attr = getCharacterOrParagraphAttributes( editor, false );

	( (CoAbstractTextEditor) editor ).copyStyles( attr, isParagraphSelection( editor ) );
}
}
