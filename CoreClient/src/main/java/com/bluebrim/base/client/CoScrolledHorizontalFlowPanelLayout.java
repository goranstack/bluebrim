package com.bluebrim.base.client;

// JDK imports
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import com.bluebrim.gui.client.CoAbstractScrolledPanelLayout;



/**
 * Subklass till CoAbstractScrolledPanelLayout som används av CoScrolledHorizontalFlowPanel.
 * Bör flyttas in i CoScrolledHorizontalFlowPanel när miljön så tillåter.
 * @see CoScrolledHorizontalFlowPanel
*/
public class CoScrolledHorizontalFlowPanelLayout extends CoAbstractScrolledPanelLayout
{

/**
 * Beräknar maximala storleken på view-containern.
 * @param parent view-containern
 * @return maximala storleken
*/
protected Dimension getMaximumScrolledPanelSize( Container parent )
{
	int h = 0;
	int w = 0;

		// iterera över barnen
	Component[] children = parent.getComponents();
	for
		( int i = 0; i < children.length; i++ )
	{
			// antag att barner arrangeras i en rad
		Dimension d = children[ i ].getMaximumSize();
		h = Math.max( h, d.height );
		w = w + d.width;
	}

		// lägg till plats för mellanrum och marginaler
	h = h + m_insets.top + m_insets.bottom;
	w = w + m_insets.left + m_insets.right + m_horizontalSpacing * ( children.length - 1 );
	
	return new Dimension( w, h );
}
/**
 * Beräknar minimala storleken på view-containern.
 * @param parent view-containern
 * @return minimala storleken
*/
protected Dimension getMinimumScrolledPanelSize( Container parent )
{
	int h = 0;
	int w = 0;

		// iterera över barnen
	Component[] children = parent.getComponents();
	for
		( int i = 0; i < children.length; i++ )
	{
			// antag att barner arrangeras i en rad
		Dimension d = children[ i ].getMinimumSize();
		h = Math.max( h, d.height );
		w = w + d.width;
	}

		// lägg till plats för mellanrum och marginaler
	h = h + m_insets.top + m_insets.bottom;
	w = w + m_insets.left + m_insets.right + m_horizontalSpacing * ( children.length - 1 );
	
	//return new Dimension( w, h );
	return new Dimension(100,100);
}
/**
 * Beräknar optimala storleken på view-containern.
 * @param parent view-containern
 * @return optimala storleken
*/
protected Dimension getPreferredScrolledPanelSize( Container parent )
{
	int h = 0;
	int w = 0;

		// iterera över barnen
	Component[] children = parent.getComponents();
	for
		( int i = 0; i < children.length; i++ )
	{
			// antag att barner arrangeras i en rad
		Dimension d = children[ i ].getPreferredSize();
		h = Math.max( h, d.height );
		w = w + d.width;
	}

		// lägg till plats för mellanrum och marginaler
	h = h + m_insets.top + m_insets.bottom;
	w = w + m_insets.left + m_insets.right + m_horizontalSpacing * ( children.length - 1 );
	
	return new Dimension( w, h );
}
/**
 * Arrangera barnen.
 * @param parent den container som innehåller barnen
 * @param dx horisontellt scrollvärde
 * @param dy vertikalt scrollvärde
 * @return det utrymme som barnen kräver
*/
protected Dimension layoutScrolledPanel( Container parent, int dx, int dy )
{
		// hämta barnen
	Component[] children = parent.getComponents();
	
		// hämta och justera tillgängligt utrymme
	Dimension parentSize = parent.getSize();
	int availableWidth = parentSize.width - m_insets.left;
	if ( availableWidth < 0 ) return new Dimension();
	
		// beräkna högsta komponenthöjden
	m_maximumHeight = 1;
	for
		( int i = 0; i < children.length; i++ )
	{
		m_maximumHeight = Math.max( m_maximumHeight, children[ i ].getPreferredSize().height );
	}
	
		// arrangera barnen
	int maxX = 0;
	int x = m_insets.left;
	int y = m_insets.top;
	
	for
		( int i = 0; i < children.length; i++ )
	{
		Dimension d = children[ i ].getPreferredSize();
		
			// radbrytning ?
		if
			( x > m_insets.left )  // radbryt aldrig radens första barn
		{
			if
				( x + d.width > availableWidth )  // finns det plats
			{
					// radbryt
				maxX = Math.max( maxX, x );
				y = y + m_maximumHeight + m_verticalSpacing;
				x = m_insets.left;
			}	
		}
		
			// beräkna högsta radbredd
		maxX = Math.max( maxX, x );
		
			// placera barnet
		children[ i ].setBounds( x - dx, y - dy, d.width, d.height );
		
			// beräkna nästa barns position
		x = x + d.width + m_horizontalSpacing;
	}

		// beräkna högsta radbredd
	maxX = Math.max( maxX, x );
	
		// beräkna och returner platsbehovet
	return new Dimension( maxX - m_horizontalSpacing, y + m_maximumHeight );
}
}
