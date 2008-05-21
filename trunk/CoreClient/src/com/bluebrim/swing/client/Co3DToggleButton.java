package com.bluebrim.swing.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
/**
 * 
 */
public class Co3DToggleButton extends CoToggleButton implements CoAsIsCapable
{
	private boolean m_asIs = false;

	

	public static class ButtonGroup extends CoButtonGroup
	{
		private boolean m_performingSetAsIs;
		
		public void setSelected( ButtonModel bm, boolean flag )
		{
			if ( m_performingSetAsIs ) return;

			clearAsIs();
			super.setSelected( bm, flag );
		}

		public void setAsIs()
		{
			m_performingSetAsIs = true;
			for
				( int i = 0; i < buttons.size(); i++ )
			{
				Co3DToggleButton b = (Co3DToggleButton) buttons.elementAt( i );
				b.setAsIs();
			}
			m_performingSetAsIs = false;
		}

		public void deselect()
		{
			ButtonModel bm = getSelection();
			if ( bm != null ) bm.setSelected( false );
			clearAsIs();
		}

		private void clearAsIs()
		{
			for
				( int i = 0; i < buttons.size(); i++ )
			{
				Co3DToggleButton b = (Co3DToggleButton) buttons.elementAt( i );
				b.m_asIs = false;
//				b.repaint();
			}
		}

	};
/**
 * Co3DToggleButton constructor comment.
 * @param icon javax.swing.Icon
 */
public Co3DToggleButton()
{
	super();
}
/**
 * Co3DToggleButton constructor comment.
 * @param icon javax.swing.Icon
 */
public Co3DToggleButton( String label )
{
	super( label );
}
/**
 * Co3DToggleButton constructor comment.
 * @param icon javax.swing.Icon
 */
public Co3DToggleButton( String label, javax.swing.Icon icon)
{
	super(label, icon);
}
/**
 * Co3DToggleButton constructor comment.
 * @param icon javax.swing.Icon
 */
public Co3DToggleButton(javax.swing.Icon icon)
{
	super(icon);
}
protected void fireItemStateChanged(ItemEvent event)
{
	boolean dirty = m_asIs;
	m_asIs = false;
	if
		( dirty )
	{
		repaint();
	}

	super.fireItemStateChanged( event );
}
public Color getBackground()
{
	if
		( m_asIs )
	{
		return m_asIsColor;
	} else {
		return super.getBackground();
	}
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
		( isSelected() )
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
	m_asIs = true;
//	repaint();
}
public void setSelected( boolean b )
{
	m_asIs = false;
	if ( b != isSelected() ) super.setSelected( b );
//	else repaint();
}
}
