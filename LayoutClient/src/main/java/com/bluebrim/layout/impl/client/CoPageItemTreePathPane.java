package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.bluebrim.layout.impl.shared.view.*;

/**
 * Component for displaying the path from the selected page item view to its top level ancestor.
 * The path is shown as a stack of icons.
 *
 * @author: Dennis
 */

public class CoPageItemTreePathPane extends JComponent implements CoPageItemView.Listener
{
	private CoPageItemEditorPanel m_editor;
	private CoShapePageItemView m_view;
	


	private Dimension m_size;
	
	private static final int m_spacing = 5;
	private static final int m_margin = 2;

	

	private static final Icon m_fallbackIcon = new Icon()
	{
		public int getIconWidth()
		{
			return 14;
		}
		public int getIconHeight()
		{
			return 10;
		}
		public void paintIcon( Component c, Graphics g, int x, int y )
		{
			g.setColor( Color.black );
			g.drawRect( x, y, 14, 10 );
		}
	};
public CoPageItemTreePathPane( CoPageItemEditorPanel editor )
{
	m_editor = editor;

	setView( null );

	addMouseListener(
		new MouseAdapter()
		{
			public void mouseClicked( MouseEvent ev )
			{
				selectClickedView( ev );
			}
		}
	);
}
public Dimension getPreferredSize()
{
	if
		( m_size != null )
	{
		return m_size;
	} else {
		return super.getPreferredSize();
	}
}
protected void paintComponent( Graphics g )
{
	super.paintComponent( g );

	int y = m_margin;
	for
		( int i = m_path.size() - 1; i >= 0; i-- )
	{
		Icon icon = (CoShapePageItemView) m_path.get( i );
		if ( icon == null ) icon = m_fallbackIcon;
		
		int x = ( m_size.width - m_margin * 2 - icon.getIconWidth() ) / 2 + m_margin;
		icon.paintIcon( this, g, x, y );
		y += icon.getIconHeight() + m_spacing;
		if
			( i > 0 )
		{
			g.setColor( Color.black );
			g.drawLine( m_size.width / 2, y - m_spacing, m_size.width / 2, y );
		}
	}

}
private void selectClickedView( MouseEvent ev )
{
	int y = ev.getY();
	
	int y0 = m_margin;

	int I = m_path.size();
	
	for
		( int i = I - 1; i >= 0; i-- )
	{
		Icon icon = (CoShapePageItemView) m_path.get( i );
		if ( icon == null ) icon = m_fallbackIcon;
		
		y0 += icon.getIconHeight();
		if
			( y0 > y )
		{
			CoShapePageItemView v = m_view;
			if
				( i > 0 )
			{
				while
					( i-- > 0 )
				{
					v = v.getParent();
				}

				m_editor.getSelectionManager().unselectAllViews();
				m_editor.getSelectionManager().select( v );
			}
			return;
		}
		y0 += m_spacing;
	}
}
public void setView( CoShapePageItemView v )
{
	Iterator pi = m_path.iterator();
	while
		( pi.hasNext() )
	{
		( (CoShapePageItemView) pi.next() ).removeViewListener( this );
	}
	
	m_view = v;
	
	m_path.clear();

	int w = 0;
	int h = m_margin;
	
	while
		( ( v != null ) && ( v != m_editor.getRootView() ) )
	{
		Icon i = v;

		if
			( i == null )
		{
			i = m_fallbackIcon;
		}
		v.addViewListener( this );
		m_path.add( v );

		w = Math.max( w, i.getIconWidth() );
		h += i.getIconHeight() + m_spacing;
		if ( m_editor.getRootView().isModelView( v ) ) break;
		v = v.getParent();
	}

	h += 2 * m_margin;
	w += 2 * m_margin;
	if
		( h > 0 )
	{
		h -= m_spacing;
		m_size = new Dimension( w, h );
	}
	
	revalidate();
	repaint();
}

	private List m_path = new ArrayList();



public void viewChanged( CoPageItemView.Event ev )//CoPageItemView view, boolean boundsChanged, int structuralChange, CoShapePageItemView child, int index )
{
	repaint();
}
}