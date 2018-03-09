package com.bluebrim.text.impl.client;

import com.bluebrim.gui.client.*;
import com.bluebrim.text.impl.shared.*;

/**
 * Creation date: (2000-05-31 15:30:44)
 * @author: Dennis
 */
 
public class CoLeadingTextfieldCounterPanel extends CoTextfieldCounterPanel
{
/**
 * CoLeadingTextfieldCounterPanel constructor comment.
 * @param textfield com.bluebrim.swing.client.CoTextField
 */
public CoLeadingTextfieldCounterPanel(com.bluebrim.swing.client.CoTextField textfield) {
	super(textfield);
}
protected void check()
{
	m_upButton.setEnabled( true );
	m_downButton.setEnabled( true );
}
protected void update( float delta )
{
	CoLeading l = CoLeading.parse( m_textfield.getText() );

	if ( l == null ) l = new CoLeading();
	
  if
		( l.isAuto() )
	{
		// auto -> offset
		l.setOffset( delta );
	} else if
		( l.isOffset() )
	{
		// offset
		l.setValue( l.getValue() + delta );
	} else {
		if
			( l.isProportional() )
		{
			// relative
			l.setValue( l.getValue() + delta * 10 );
		} else {
			// absolute
			l.setValue( l.getValue() + delta );
		}
	}
	
	m_textfield.setText( CoLeading.format( l ) );
	m_textfield.postActionEvent();
}
}