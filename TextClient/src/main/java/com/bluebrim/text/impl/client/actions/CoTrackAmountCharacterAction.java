package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;
import java.text.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Text editor action that set tracking amount values.
 * 
 * @author: Dennis Malmström
 */

public class CoTrackAmountCharacterAction extends CoFloatCharacterAction
{
/**
 * CoTrackAmountCharacterAction constructor comment.
 * @param attribute java.lang.String
 * @param actionName java.lang.String
 */
public CoTrackAmountCharacterAction( Object attribute, String actionName )
{
	super( attribute, actionName );
}
public void doit( JTextPane editor, ActionEvent e )
{
	int start = editor.getSelectionStart();
	int end = editor.getSelectionEnd();
	if
		( start == end )
	{
		if ( start == 0 ) return;
		else start--;
	} else if
		( end - start > 1 )
	{
		end--;
	}
	
	MutableAttributeSet attr = new com.bluebrim.text.shared.CoSimpleAttributeSet();
	
	try
	{
		String s = e.getActionCommand();
		if ( ( s == null ) || ( s == CoTextConstants.NO_VALUE ) ) throw new ParseException( null, 0 );
		
		Float size = new Float( ( (Number) m_converter.parseObject( s ) ).floatValue() );
		CoStyleConstants.set( attr, m_attribute, size );

		StyledDocument doc = getStyledDocument( editor );
		doc.setCharacterAttributes( start, end - start, attr, false );
	}
	catch (ParseException nfe)
	{
		clearCharacterAttribute( editor, m_attribute );
	}
}
}