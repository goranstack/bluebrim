package com.bluebrim.base.client;

// JDK imports
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import com.bluebrim.gui.client.CoAbstractScrolledPanelLayout;



/**
 * Subklass till CoAbstractScrolledPanelLayout som anv�nds av CoScrolledHorizontalFlowPanel.
 * B�r flyttas in i CoScrolledHorizontalFlowPanel n�r milj�n s� till�ter.
 * @see CoScrolledHorizontalFlowPanel
*/
public class CoScrolledHorizontalFlowPanelLayout extends CoAbstractScrolledPanelLayout
{

/**
 * Ber�knar maximala storleken p� view-containern.
 * @param parent view-containern
 * @return maximala storleken
*/
protected Dimension getMaximumScrolledPanelSize( Container parent )
{
	int h = 0;
	int w = 0;

		// iterera �ver barnen
	Component[] children = parent.getComponents();
	for
		( int i = 0; i < children.length; i++ )
	{
			// antag att barner arrangeras i en rad
		Dimension d = children[ i ].getMaximumSize();
		h = Math.max( h, d.height );
		w = w + d.width;
	}

		// l�gg till plats f�r mellanrum och marginaler
	h = h + m_insets.top + m_insets.bottom;
	w = w + m_insets.left + m_insets.right + m_horizontalSpacing * ( children.length - 1 );
	
	return new Dimension( w, h );
}
/**
 * Ber�knar minimala storleken p� view-containern.
 * @param parent view-containern
 * @return minimala storleken
*/
protected Dimension getMinimumScrolledPanelSize( Container parent )
{
	int h = 0;
	int w = 0;

		// iterera �ver barnen
	Component[] children = parent.getComponents();
	for
		( int i = 0; i < children.length; i++ )
	{
			// antag att barner arrangeras i en rad
		Dimension d = children[ i ].getMinimumSize();
		h = Math.max( h, d.height );
		w = w + d.width;
	}

		// l�gg till plats f�r mellanrum och marginaler
	h = h + m_insets.top + m_insets.bottom;
	w = w + m_insets.left + m_insets.right + m_horizontalSpacing * ( children.length - 1 );
	
	//return new Dimension( w, h );
	return new Dimension(100,100);
}
/**
 * Ber�knar optimala storleken p� view-containern.
 * @param parent view-containern
 * @return optimala storleken
*/
protected Dimension getPreferredScrolledPanelSize( Container parent )
{
	int h = 0;
	int w = 0;

		// iterera �ver barnen
	Component[] children = parent.getComponents();
	for
		( int i = 0; i < children.length; i++ )
	{
			// antag att barner arrangeras i en rad
		Dimension d = children[ i ].getPreferredSize();
		h = Math.max( h, d.height );
		w = w + d.width;
	}

		// l�gg till plats f�r mellanrum och marginaler
	h = h + m_insets.top + m_insets.bottom;
	w = w + m_insets.left + m_insets.right + m_horizontalSpacing * ( children.length - 1 );
	
	return new Dimension( w, h );
}
/**
 * Arrangera barnen.
 * @param parent den container som inneh�ller barnen
 * @param dx horisontellt scrollv�rde
 * @param dy vertikalt scrollv�rde
 * @return det utrymme som barnen kr�ver
*/
protected Dimension layoutScrolledPanel( Container parent, int dx, int dy )
{
		// h�mta barnen
	Component[] children = parent.getComponents();
	
		// h�mta och justera tillg�ngligt utrymme
	Dimension parentSize = parent.getSize();
	int availableWidth = parentSize.width - m_insets.left;
	if ( availableWidth < 0 ) return new Dimension();
	
		// ber�kna h�gsta komponenth�jden
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
			( x > m_insets.left )  // radbryt aldrig radens f�rsta barn
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
		
			// ber�kna h�gsta radbredd
		maxX = Math.max( maxX, x );
		
			// placera barnet
		children[ i ].setBounds( x - dx, y - dy, d.width, d.height );
		
			// ber�kna n�sta barns position
		x = x + d.width + m_horizontalSpacing;
	}

		// ber�kna h�gsta radbredd
	maxX = Math.max( maxX, x );
	
		// ber�kna och returner platsbehovet
	return new Dimension( maxX - m_horizontalSpacing, y + m_maximumHeight );
}
}
