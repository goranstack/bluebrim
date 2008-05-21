package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

/**
 * Subclass of CoCircularStateAction that effect paragraph attribute values.
 * 
 * @author: Dennis Malmström
 */

public class CoCircularStateParagraphAction extends CoCircularStateAction
{
public CoCircularStateParagraphAction(Object attribute, Object[] states)
{
	this(attribute, "Co-Mstate-" + attribute, states);
}


public CoCircularStateParagraphAction(Object attribute, String name, Object[] states)
{
	super(attribute, name, states);
}


public void doit( JTextPane editor, ActionEvent e )
{
	try
	{
		Object value = getSelectedParagraphAttributeValue( editor, m_attribute );

		int i;
		if
			( value == null )
		{
			for ( i = 0; i < m_states.length; i++ ) if ( m_states[ i ] == null ) break;
		} else {
			for ( i = 0; i < m_states.length; i++ ) if ( value.equals( m_states[ i ] ) ) break;
		}
		i++;
		if ( i >= m_states.length ) i = 0;

		value = m_states[ i ];

		if
			( value == null )
		{
			clearParagraphAttribute( editor, m_attribute );
		} else {
			MutableAttributeSet attr = new com.bluebrim.text.shared.CoSimpleAttributeSet();
			attr.addAttribute( m_attribute, value );
			setParagraphAttributes( editor, attr, false, true );
		}
	}
	catch ( IllegalArgumentException ex )
	{
	}
}
}