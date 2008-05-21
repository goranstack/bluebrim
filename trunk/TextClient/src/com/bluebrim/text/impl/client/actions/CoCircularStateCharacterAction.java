package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

/**
 * Subclass of CoCircularStateAction that effect character attribute values.
 * 
 * @author: Dennis Malmström
 */

public class CoCircularStateCharacterAction extends CoCircularStateAction
{
public CoCircularStateCharacterAction( Object attribute, Object[] states )
{
	this( attribute, "Co-Mstate-" + attribute, states );
}


public CoCircularStateCharacterAction(Object attribute, String name, Object[] states)
{
	super(attribute, name, states);
}


public void doit( JTextPane editor, ActionEvent e )
{
	try
	{
		Object value = getSelectedCharacterAttributeValue( editor, m_attribute );

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
		
		int p0 = editor.getSelectionStart();
		int p1 = editor.getSelectionEnd();

		MutableAttributeSet attr;
		if
			( p0 != p1 )
		{
			attr = new com.bluebrim.text.shared.CoSimpleAttributeSet();
		} else {
			attr = getStyledEditorKit( editor ).getInputAttributes();
		}

		if
			( value == null )
		{
			if ( p0 == p1 ) attr.removeAttribute( m_attribute );
			clearCharacterAttribute( editor, m_attribute );
		} else {
			attr.addAttribute( m_attribute, value );
			setCharacterAttributes( editor, attr, false );
		}
	} catch ( IllegalArgumentException ex ) {
	}
}
}