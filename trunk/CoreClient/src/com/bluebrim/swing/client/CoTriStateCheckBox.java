package com.bluebrim.swing.client;

import java.awt.event.ItemEvent;

import javax.swing.JCheckBox;

public class CoTriStateCheckBox extends JCheckBox implements CoTriStateBooleanIF
{
	private Boolean m_state = null;
	private boolean m_asIs = false;
public CoTriStateCheckBox()
{
	this( null );
}
public CoTriStateCheckBox( String text )
{
	super( text, CoTriStateCheckBoxIcon.TheInstance );
}
protected void fireItemStateChanged(ItemEvent event)
{
	setTriState( CoTriStateUtilities.nextState( m_state ) );

	super.fireItemStateChanged( event );
}
public Boolean getTriState()
{
	return m_state;
}
public boolean isAsIs()
{
	return m_asIs;
}
public void setAsIs()
{
	if ( m_asIs ) return;

	m_asIs = true;
	m_state = null;
}
public void setTriState( Boolean b )
{
	boolean dirty = ( b != m_state ) || m_asIs;
	m_state = b;
	m_asIs = false;
	if
		( dirty )
	{
		repaint();
	}
}
}