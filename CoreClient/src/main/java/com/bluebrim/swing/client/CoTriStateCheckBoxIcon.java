package com.bluebrim.swing.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class CoTriStateCheckBoxIcon implements Icon
{
	public static CoTriStateCheckBoxIcon TheInstance = new CoTriStateCheckBoxIcon();
	
	private static final int m_iconSize = 13;
private CoTriStateCheckBoxIcon()
{
}
public int getIconHeight()
{
	return m_iconSize;
}
public int getIconWidth()
{
	return m_iconSize;
}
public void paintIcon( Component c, Graphics g, int x, int y )
{
	CoTriStateBooleanIF b = (CoTriStateBooleanIF) c;
	if
		( b.isAsIs() )
	{
		g.translate( x, y );
		g.setColor( CoTriStateBooleanIF.m_asIsColor );
		g.fillRect( 2, 2, m_iconSize - 5, m_iconSize - 5 );
		g.translate( -x, -y );

		CoTriStateUtilities.drawEtchedBorder( g,
								                          x,
								                          y,
								                          m_iconSize,
								                          m_iconSize,
			                                    c.getBackground().darker(),
			                                    c.getBackground().brighter() );
			                                    
	} else if
		( b.getTriState() == null )
	{
		if
			( ! (c instanceof com.bluebrim.menus.client.CoTriStateCheckBoxMenuItem ) )
		{
			CoTriStateUtilities.drawEtchedBorder( g,
								                            x,
								                            y,
								                            m_iconSize,
								                            m_iconSize,
			                                      c.getBackground().darker().darker(),
			                                      c.getBackground().brighter().brighter() );
		}
	} else if
		( b.getTriState().booleanValue() )
	{
		CoTriStateUtilities.drawBorder( g,
			                              x,
			                              y,
			                              m_iconSize,
			                              m_iconSize,
			                              c.getBackground().darker(),
			                              c.getBackground().brighter() );
		// green checkmark
		g.setColor( Color.green.darker() );
		g.fillRect( x + 3, y + 5, 2, m_iconSize - 8 );
		g.drawLine( x + ( m_iconSize - 4 ), y + 3, x + 5, y + ( m_iconSize - 6 ) );
		g.drawLine( x + ( m_iconSize - 4 ), y + 4, x + 5, y + ( m_iconSize - 5 ) );
	} else {
		CoTriStateUtilities.drawBorder( g,
			                              x,
			                              y,
			                              m_iconSize,
			                              m_iconSize,
			                              c.getBackground().brighter().brighter(),
			                              c.getBackground().darker().darker() );
		// red stopsign
		g.setColor( Color.red );
		g.drawOval( x + 2, y + 2, m_iconSize - 4, m_iconSize - 4 );
		g.drawLine( x + 4, y + 4, x + m_iconSize - 5, y + m_iconSize - 5 );
	}
}
}
