package com.bluebrim.swing.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ItemEvent;

import javax.swing.Icon;
import javax.swing.JToggleButton;

public class CoTriStateToggleButton extends JToggleButton implements CoTriStateBooleanIF
{
	private Boolean m_state = null;
	private boolean m_asIs = false;
	
	private Icon m_nullIcon; 
	private Icon m_trueIcon; 
	private Icon m_falseIcon;
/**
 * CoTriStateRadioButton constructor comment.
 */
public CoTriStateToggleButton()
{
	super();
}
/**
 * CoTriStateRadioButton constructor comment.
 * @param text java.lang.String
 */
public CoTriStateToggleButton(String text)
{
	super(text);
}
/**
 * CoTriStateRadioButton constructor comment.
 * @param text java.lang.String
 * @param icon com.sun.java.swing.Icon
 */
public CoTriStateToggleButton(String text, Icon icon)
{
	super(text, icon);
}
/**
 * CoTriStateRadioButton constructor comment.
 * @param icon com.sun.java.swing.Icon
 */
public CoTriStateToggleButton(Icon icon)
{
	super(icon);
}
/**
 * CoTriStateRadioButton constructor comment.
 * @param icon com.sun.java.swing.Icon
 */
public CoTriStateToggleButton( Icon nullIcon, Icon trueIcon, Icon falseIcon )
{
	super( nullIcon );
	
	m_nullIcon = nullIcon;
	m_trueIcon = trueIcon;
	m_falseIcon = falseIcon;
}
protected void fireItemStateChanged(ItemEvent event)
{
	setTriState( CoTriStateUtilities.nextState( m_state ) );

	setIcon();
	
	super.fireItemStateChanged( event );
}
public Color getBackground()
{
	if ( m_asIs ) return m_asIsColor;
	else return super.getBackground();
}
public Boolean getTriState()
{
	return m_state;
}
public boolean isAsIs()
{
	return m_asIs;
}
public boolean isContentAreaFilled()
{
	return false;
}
public boolean isOpaque()
{
	return m_asIs;
}
protected void paintBorder(Graphics g)
{
	if
		( m_asIs )
	{
		
		CoTriStateUtilities.drawEtchedBorder( g,
								                          0, 0, getWidth(), getHeight(),
			                                    getBackground().darker().darker(),
			                                    getBackground().brighter().brighter() );
			                                    
	} else if
		( m_state == null )
	{
		CoTriStateUtilities.drawEtchedBorder( g,
								                          0, 0, getWidth(), getHeight(),
			                                    getBackground().darker().darker(),
			                                    getBackground().brighter().brighter() );
	} else if
		( m_state.booleanValue() )
	{
		CoTriStateUtilities.drawBorder( g, 
								                    0, 0, getWidth(), getHeight(),
			                              getBackground().darker(),
			                              getBackground().brighter() );
	} else {
		CoTriStateUtilities.drawBorder( g,
								                    0, 0, getWidth(), getHeight(),
			                              getBackground().brighter().brighter(),
			                              getBackground().darker().darker() );
	}
}
public void setAsIs()
{
	if ( m_asIs ) return;
	m_asIs = true;
	m_state = null;

	setIcon();
}
private void setIcon()
{
	if
		( m_nullIcon != null )
	{
		if
			( ( m_state == null ) || m_asIs )
		{
			setIcon( m_nullIcon );
		} else if
			( m_state.booleanValue() )
		{
			setIcon( m_trueIcon );
		} else {
			setIcon( m_falseIcon );
		}
	}
}
public void setTriState( Boolean b )
{
	boolean dirty = ( b != m_state ) || m_asIs;
	m_state = b;
	m_asIs = false;
	setIcon();
	if
		( dirty )
	{
		repaint();
	}
}
}