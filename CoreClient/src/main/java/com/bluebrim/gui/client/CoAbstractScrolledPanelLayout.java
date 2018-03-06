package com.bluebrim.gui.client;

// JDK imports
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

import javax.swing.JScrollBar;



/**
 * Layoutmanager vars subklasser används av subklasser till CoAbstractScrolledPanel.
 * Bör flyttas in i CoAbstractScrolledPanel när miljön så tillåter.
 * @see CoAbstractScrolledPanel
*/
public abstract class CoAbstractScrolledPanelLayout implements LayoutManager2
{
	protected int m_horizontalSpacing = 0;   // mellanrum
	protected int m_verticalSpacing = 0;     // mellanrum
	
	protected Insets m_insets = new Insets( 0, 0, 0, 0 );   // marginaler

	protected int m_maximumHeight = 1;
	
	protected Dimension m_viewSize = new Dimension( 0, 0 );
	

/**
 * @see LayoutManager2
*/
public void addLayoutComponent(Component comp, Object constraints) {}
/**
 * @see LayoutManager2
*/
public void addLayoutComponent(String name, Component comp) {}
/**
 * Access-metod till horisontella mellanrumsvärdet
 * @return horisontella mellanrumsvärdet
*/
public int getHorizontalSpacing()
{
	return m_horizontalSpacing;
}
/**
 * Access-metod till marginalerna.
 * @return marginalerna
*/
public Insets getInternalInsets()
{
	return m_insets;
}
/**
 * @see LayoutManager2
*/
public float getLayoutAlignmentX(Container target)
{
	return 0.0f;
}
/**
 * @see LayoutManager2
*/
public float getLayoutAlignmentY(Container target)
{
	return 0.0f;
}
/**
 * Beräknar maximala storleken på view-containern.
 * @param parent view-containern
 * @return maximala storleken
*/
protected abstract Dimension getMaximumScrolledPanelSize( Container parent );
/**
 * Beräknar minimala storleken på view-containern.
 * @param parent view-containern
 * @return minimala storleken
*/
protected abstract Dimension getMinimumScrolledPanelSize( Container parent );
/**
 * Beräknar optimala storleken på view-containern.
 * @param parent view-containern
 * @return optimala storleken
*/
protected abstract Dimension getPreferredScrolledPanelSize( Container parent );
/**
 * Access-metod till vertikala mellanrumsvärdet
 * @return vertikala mellanrumsvärdet
*/
public int getVerticalSpacing()
{
	return m_verticalSpacing;
}
/**
 * Returnerar view-containerns storlekskrav.
 * Obs: ej nödvändigtvis samma som dess storlek.
 * @return view-containerns storlekskrav
*/
public Dimension getViewSize()
{
	return m_viewSize;
}
/**
 * @see LayoutManager2
*/
public void invalidateLayout( Container target )
{
}
/**
 * Arrangera barnen och scrollars.
 * @param parent den panel som ska arrangeras
*/
public void layoutContainer( Container parent )
{
	JScrollBar horizontalScrollbar = (JScrollBar) parent.getComponent( 0 );
	JScrollBar verticalScrollbar = (JScrollBar) parent.getComponent( 1 );
	Container view = (Container) parent.getComponent( 2 );

	int left = parent.getInsets().left;
	int right = parent.getInsets().right;
	int top = parent.getInsets().top;
	int bottom = parent.getInsets().bottom;
	
		// försök utan scrollbars
	Dimension viewportSize = new Dimension( parent.getSize() );
	viewportSize.width -= left + right;
	viewportSize.height -= top + bottom;
	
		// arrangera view-panelens barn
	view.setBounds( left, top, viewportSize.width, viewportSize.height );
	m_viewSize = layoutScrolledPanel( view, 0, 0 );

		// nollställ horisontell scrollbar	
	if
		( ( m_viewSize.width <= viewportSize.width ) && horizontalScrollbar.isVisible() )
	{
		horizontalScrollbar.setVisible( false );	
		horizontalScrollbar.setValue( 0 );
	}
	
		// nollställ vertikal scrollbar	
	if
		( ( m_viewSize.height <= viewportSize.height ) && verticalScrollbar.isVisible() )
	{
		verticalScrollbar.setVisible( false );	
		verticalScrollbar.setValue( 0 );
	}
	
		// behövs scrollbars ?
	if
		( ( m_viewSize.width <= viewportSize.width ) && ( m_viewSize.height <= viewportSize.height ) )
	{
		return;
	}



	// scrollbars behövs

	int scrollbarWidth = horizontalScrollbar.getPreferredSize().height;

	int horizontalScrollbarValue = 0;
	int verticalScrollbarValue = 0;

		// vid behov aktivera vertikal scrollbar
	if
		( m_viewSize.height > viewportSize.height )
	{
			// dimensionera om viewport
		viewportSize.width -= scrollbarWidth;
		view.setBounds( left, top, viewportSize.width, viewportSize.height );

			// visa scrollbar
		verticalScrollbar.setVisible( true );
		verticalScrollbar.setBounds( viewportSize.width + left, top, scrollbarWidth, viewportSize.height );
		verticalScrollbarValue = verticalScrollbar.getValue();
		
			// arrangera view-panelens barn
		m_viewSize = layoutScrolledPanel( view, horizontalScrollbarValue, verticalScrollbarValue );

			// justera och sätt scrollbar-värde
		if ( verticalScrollbarValue > m_viewSize.height ) verticalScrollbarValue = m_viewSize.height - viewportSize.height;
		verticalScrollbar.setValues( verticalScrollbarValue, viewportSize.height, 0, m_viewSize.height );

			// iordningställ scrollbar
		verticalScrollbar.setUnitIncrement( 5 );
		verticalScrollbar.setBlockIncrement( m_maximumHeight + m_verticalSpacing );
	}

		// vid behov aktivera horisontel scrollbar
	if
		( m_viewSize.width > viewportSize.width )
	{
			// dimensionera om viewport
		viewportSize.height -= scrollbarWidth;
		view.setBounds( left, top, viewportSize.width, viewportSize.height );

			// visa scrollbar
		horizontalScrollbar.setVisible( true );
		horizontalScrollbar.setBounds( left, viewportSize.height + top, viewportSize.width, scrollbarWidth );
		horizontalScrollbarValue = horizontalScrollbar.getValue();
		verticalScrollbar.setBounds( viewportSize.width + left, top, scrollbarWidth, viewportSize.height );
		
			// justera scrollbar-värden
		if ( verticalScrollbarValue > m_viewSize.height ) verticalScrollbarValue = m_viewSize.height - viewportSize.height;
		if ( horizontalScrollbarValue > m_viewSize.width ) horizontalScrollbarValue = m_viewSize.width - viewportSize.width;

			// arrangera view-panelens barn
		m_viewSize = layoutScrolledPanel( view, horizontalScrollbarValue, verticalScrollbarValue );

			// sätt scrollbar-värden
		horizontalScrollbar.setValues( horizontalScrollbarValue, viewportSize.width, 0, m_viewSize.width );
		verticalScrollbar.setValues( verticalScrollbarValue, viewportSize.height, 0, m_viewSize.height );

			// iordningställ scrollbar
		horizontalScrollbar.setUnitIncrement( 5 );
		horizontalScrollbar.setBlockIncrement( viewportSize.width / 2 );
	}			

}
/**
 * Arrangera barnen i view-containern.
 * @param parent den container som innehåller barnen
 * @param dx horisontellt scrollvärde
 * @param dy vertikalt scrollvärde
 * @return det utrymme som barnen kräver
*/
protected abstract Dimension layoutScrolledPanel( Container parent, int dx, int dy );
/**
 * Beräknar maximala storleken.
 * @param parent den CoAbstractScrolledPanel-instans vars storlek ska beräknas
 * @return maximala storleken
*/
public Dimension maximumLayoutSize( Container parent )
{
	/*
	Component[] children = parent.getComponents();

	Dimension d0 = children[ 0 ].getMaximumSize();
	Dimension d1 = children[ 1 ].getMaximumSize();
	Dimension d2 = getMaximumScrolledPanelSize( (Container) children[ 2 ] );

	Insets i = parent.getInsets();
	
	int w = d2.width + d1.width + i.left + i.right;
	int h = d2.height + d0.height + i.top + i.bottom;
*/
	return new Dimension( Integer.MAX_VALUE, Integer.MAX_VALUE );// new Dimension( w, h );
}
/**
 * Beräknar minimala storleken.
 * @param parent den CoAbstractScrolledPanel-instans vars storlek ska beräknas
 * @return minimala storleken
*/
public Dimension minimumLayoutSize(Container parent)
{
	Dimension d = getMinimumScrolledPanelSize( (Container) parent.getComponent( 2 ) );
	Insets i = parent.getInsets();
	d.width += i.left + i.right;
	d.height += i.top + i.bottom;
	return d;
}
/**
 * Beräknar optimala storleken.
 * @param parent den CoAbstractScrolledPanel-instans vars storlek ska beräknas
 * @return optimala storleken
*/
public Dimension preferredLayoutSize(Container parent)
{
	Dimension d = getPreferredScrolledPanelSize( (Container) parent.getComponent( 2 ) );
	Insets i = parent.getInsets();
	d.width += i.left + i.right;
	d.height += i.top + i.bottom;
	return d;
}
/**
 * @see LayoutManager2
*/
public void removeLayoutComponent(Component comp) {}
/**
 * Set-metod för horisontellt mellanrum.
 * @param s nytt horisontellt mellanrum
*/
public int setHorizontalSpacing( int s )
{
	return m_horizontalSpacing = s;
}
/**
 * Set-metod för marginalerna.
 * @param i nya marginaler
*/
public Insets setInternalInsets( Insets i )
{
	return m_insets = i;
}
/**
 * Set-metod för mellanrum.
 * @param hs nytt horisontellt mellanrum
 * @param vs nytt vertikalt mellanrum
*/
public void setSpacing( int hs, int vs )
{
	setHorizontalSpacing( hs );
	setVerticalSpacing( vs );
}
/**
 * Set-metod för vertikalt mellanrum.
 * @param s nytt vertikalt mellanrum
*/
public int setVerticalSpacing( int s )
{
	return m_verticalSpacing = s;
}
}
