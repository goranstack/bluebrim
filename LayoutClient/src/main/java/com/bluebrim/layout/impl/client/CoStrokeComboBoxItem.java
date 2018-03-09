package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;
import javax.swing.border.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.geom.*;
import com.bluebrim.swing.client.*;

/**
 * Class used when storing CoStrokeIFs in a ComboBoxModel.
 * Note: it only works if the inner class CoStrokeComboBoxItem.ComboBoxModel
 * is used.
 *
 * @author: Dennis
 */

public class CoStrokeComboBoxItem
{
	private String m_text;
	private Icon m_icon;
	private com.bluebrim.stroke.shared.CoStrokeIF m_stroke;



	public static class ComboBoxModel extends DefaultComboBoxModel
	{
		public void setSelectedItem( Object o )
		{
			int I = getSize();
			for
				( int i = 0; i < I; i++ )
			{
				if
					( getElementAt( i ).equals( o ) )
				{
					super.setSelectedItem( getElementAt( i ) );
					return;
				}
			}
			super.setSelectedItem( o );
		}
	};

	
	public static class OptionMenuRenderer extends CoOptionMenu.DefaultRenderer
	{
		public OptionMenuRenderer()
		{
		}
			
		public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
		{
			if
				( value instanceof CoStrokeComboBoxItem )
			{
				CoStrokeComboBoxItem i = (CoStrokeComboBoxItem) value;
				Component c = super.getListCellRendererComponent( list, ( i != null ) ? i.m_text : null, index, isSelected, cellHasFocus );
				setIcon( ( i != null ) ? i.m_icon : null );
				return c;
			} else {
				Component c = super.getListCellRendererComponent( list, ( value != null ) ? value.toString() : " ", index, isSelected, cellHasFocus );
				setIcon( null );
				return c;
			}
		}
	};

		/*
		
	public static class OptionMenuRenderer extends CoOptionMenu.AbstractRenderer
	{
		public OptionMenuRenderer()
		{
			super();
		}
		
		public void configure( Object value, JLabel l, JList list, int index, boolean isSelected, boolean cellHasFocus )
		{
			if
				( value instanceof CoStrokeComboBoxItem )
			{
				CoStrokeComboBoxItem i = (CoStrokeComboBoxItem) value;
				l.setText( ( i != null ) ? i.m_text : null );
				l.setIcon( ( i != null ) ? i.m_icon : null );
			} else {
				l.setText( ( value != null ) ? value.toString() : "" );
				l.setIcon( null );
			}

		}

	};
*/
	protected static Border m_noFocusBorder = new EmptyBorder(1,1,1,1);
public CoStrokeComboBoxItem( com.bluebrim.stroke.shared.CoStrokeIF stroke )
{
	super();

	m_stroke = stroke;
	m_text = m_stroke.getName();

	com.bluebrim.stroke.shared.CoStrokeRenderer r = new com.bluebrim.stroke.shared.CoStrokeRenderer();
	r.setShape( new CoLine( 5, 10, 95, 10 ) );
	com.bluebrim.stroke.impl.shared.CoStrokeProperties s = new com.bluebrim.stroke.impl.shared.CoStrokeProperties();
	s.setWidth( 10 );
	s.setStroke( m_stroke );
	s.setAlignment( s.ALIGN_CENTER );
	r.setStrokeProperties( s );

	Image i = new BufferedImage( 100, 20, BufferedImage.TYPE_INT_RGB );
	i.getGraphics().fillRect( 0, 0, 100, 20 );

	CoScreenPaintable p = CoScreenPaintable.wrap( i.getGraphics() );
	r.paint( p );
	p.releaseDelegate();
	
	m_icon = new ImageIcon( i );
}
public boolean equals( Object o )
{
	if
		( o instanceof com.bluebrim.stroke.shared.CoStrokeIF )
	{
		return m_stroke.equals( o );
	} else {
		return super.equals( o );
	}
}
public com.bluebrim.stroke.shared.CoStrokeIF getStroke()
{
	return m_stroke;
}
}