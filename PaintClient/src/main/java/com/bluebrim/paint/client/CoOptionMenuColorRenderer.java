package com.bluebrim.paint.client;

import java.awt.*;

import javax.swing.*;

import com.bluebrim.paint.shared.*;
import com.bluebrim.swing.client.CoOptionMenu.*;


public class CoOptionMenuColorRenderer extends DefaultRenderer
{
	private Color m_color;
	private int m_iconSize = -1;

	public CoOptionMenuColorRenderer()
	{
		setIcon(
			new Icon()
			{
				public int getIconHeight() { return m_iconSize; }
				public int getIconWidth() { return m_iconSize; }
				
				public void paintIcon( Component c, Graphics g, int x, int y )
				{
					g.translate( x, y );
					if
						( m_color == null )
					{
						g.setColor( c.getForeground() );
						g.drawLine( 2, 2, m_iconSize - 3, m_iconSize - 3 );
						g.drawLine( m_iconSize - 3, 2, 2, m_iconSize - 3 );
					} else {
						g.setColor( m_color );
						g.fillRect( 0, 0, m_iconSize - 1, m_iconSize - 1 );
					}
					g.translate( -x, -y );
				}
			}
		);
	}

		
	public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
	{
		m_iconSize = 0;
		Insets i = getInsets();
		m_iconSize = this.getPreferredSize().height - i.top - i.bottom;
		
		if
			( value instanceof CoColorIF )
		{
			CoColorIF c = (CoColorIF) value;
			m_color = c.getPreviewColor();
			return super.getListCellRendererComponent( list, c.getName(), index, isSelected, cellHasFocus );
		} else {
			m_color = null;
			return super.getListCellRendererComponent( list, " ", index, isSelected, cellHasFocus );
		}
	}
}