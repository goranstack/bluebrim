package com.bluebrim.menus.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ItemEvent;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.UIManager;

import com.bluebrim.swing.client.CoTriStateBooleanIF;
import com.bluebrim.swing.client.CoTriStateCheckBoxIcon;
import com.bluebrim.swing.client.CoTriStateUtilities;

public class CoTriStateCheckBoxMenuItem extends JCheckBoxMenuItem implements CoTriStateBooleanIF
{
	private Boolean m_state = null;
	private boolean m_asIs = false;

	private static Color m_selectedBackground = UIManager.getColor( "MenuItem.selectionBackground" ) ;
public CoTriStateCheckBoxMenuItem()
{
	this( null );
}
public CoTriStateCheckBoxMenuItem( String text )
{
	super( text );
}
protected void fireItemStateChanged(ItemEvent event)
{
	setTriState( CoTriStateUtilities.nextState( m_state ) );

	super.fireItemStateChanged( event );
}
public Icon getSelectedIcon()
{
	return CoTriStateCheckBoxIcon.TheInstance;
}
public Boolean getTriState()
{
	return m_state;
}
public boolean isAsIs()
{
	return m_asIs;
}
protected void paintComponent( Graphics g )
{
	super.paintComponent( g );

	Icon icon = getSelectedIcon();

	int h = icon.getIconHeight();
	int i = ( getHeight() - h ) / 2;

	if
		( isArmed() )
	{
		g.setColor( m_selectedBackground );
	} else {
		g.setColor( getBackground() );
	}
	g.fillRect( i, i, h, h );
	icon.paintIcon( this, g, i, i );

}
public void setAsIs()
{
	if ( m_asIs ) return;

	m_asIs = true;
	m_state = null;
	repaint();
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
