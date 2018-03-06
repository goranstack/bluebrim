package com.bluebrim.base.client;

// JDK imports
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import com.bluebrim.gui.client.CoAbstractScrolledPanelLayout;



/**
 * Subklass till CoAbstractScrolledPanelLayout som används av CoScrolledHorizontalGridPanel.
 * Bör flyttas in i CoScrolledHorizontalGridPanel när miljön så tillåter.
 * @see CoScrolledHorizontalGridPanel
*/
public class CoScrolledHorizontalGridPanelLayout extends CoAbstractScrolledPanelLayout
{
	protected int m_columnCount = 3;

/**
 * Access-metod till kolumnantalet.
 * @return antal kolumner
 */
public int getColumnCount()
{
	return m_columnCount;
}
/**
 * Beräknar maximala storleken på view-containern.
 * @param parent view-containern
 * @return maximala storleken
*/
protected Dimension getMaximumScrolledPanelSize( Container parent )
{
	Component[] children = parent.getComponents();

		// beräkna högsta komponenthöjden
	int maximumHeight = 0;
	for
		( int i = 0; i < children.length; i++ )
	{
		maximumHeight = Math.max( maximumHeight, children[ i ].getMaximumSize().height );
	}
	
		// beräkna storleken
	int maxX = 0;
	int x = m_insets.left;
	int y = m_insets.top;
	
		// iterera över barnen
	for
		( int i = 0; i < children.length; i++ )
	{
			// radbrytning ?
		if
			( ( i > 0 ) && ( i % m_columnCount == 0 ) )
		{
			maxX = Math.max( maxX, x );
			y = y + maximumHeight + m_verticalSpacing;
			x = m_insets.left;
		}	
		maxX = Math.max( maxX, x );	// beräkna högsta radbredd

			// beräkna nästa barns position
		x = x + children[ i ].getMaximumSize().width + m_horizontalSpacing;
	}
	maxX = Math.max( maxX, x );	// beräkna högsta radbredd
	
	return new Dimension( maxX - m_horizontalSpacing, y + maximumHeight );
}
/**
 * Beräknar minimala storleken på view-containern.
 * @param parent view-containern
 * @return minimala storleken
*/
protected Dimension getMinimumScrolledPanelSize( Container parent )
{
	Component[] children = parent.getComponents();

		// beräkna högsta komponenthöjden
	int maximumHeight = 0;
	for
		( int i = 0; i < children.length; i++ )
	{
		maximumHeight = Math.max( maximumHeight, children[ i ].getMinimumSize().height );
	}
	
		// beräkna storleken
	int maxX = 0;
	int x = m_insets.left;
	int y = m_insets.top;
	
		// iterera över barnen
	for
		( int i = 0; i < children.length; i++ )
	{
			// radbrytning ?
		if
			( ( i > 0 ) && ( i % m_columnCount == 0 ) )
		{
			maxX = Math.max( maxX, x );
			y = y + maximumHeight + m_verticalSpacing;
			x = m_insets.left;
		}	
		maxX = Math.max( maxX, x );	// beräkna högsta radbredd

			// beräkna nästa barns position
		x = x + children[ i ].getMinimumSize().width + m_horizontalSpacing;
	}
	maxX = Math.max( maxX, x );	// beräkna högsta radbredd
	
	return new Dimension( maxX - m_horizontalSpacing, y + maximumHeight );
}
/**
 * Beräknar optimala storleken på view-containern.
 * @param parent view-containern
 * @return optimala storleken
*/
protected Dimension getPreferredScrolledPanelSize( Container parent )
{
	Component[] children = parent.getComponents();

		// beräkna högsta komponenthöjden
	int maximumHeight = 0;
	for
		( int i = 0; i < children.length; i++ )
	{
		maximumHeight = Math.max( maximumHeight, children[ i ].getPreferredSize().height );
	}
	
		// beräkna storleken
	int maxX = 0;
	int x = m_insets.left;
	int y = m_insets.top;
	
		// iterera över barnen
	for
		( int i = 0; i < children.length; i++ )
	{
			// radbrytning ?
		if
			( ( i > 0 ) && ( i % m_columnCount == 0 ) )
		{
			maxX = Math.max( maxX, x );
			y = y + maximumHeight + m_verticalSpacing;
			x = m_insets.left;
		}	
		maxX = Math.max( maxX, x );	// beräkna högsta radbredd

			// beräkna nästa barns position
		x = x + children[ i ].getPreferredSize().width + m_horizontalSpacing;
	}
	maxX = Math.max( maxX, x );	// beräkna högsta radbredd
	
	return new Dimension( maxX - m_horizontalSpacing, y + maximumHeight );
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
			( ( i > 0 ) && ( i % m_columnCount == 0 ) )
		{
				// radbryt
			maxX = Math.max( maxX, x );
			y = y + m_maximumHeight + m_verticalSpacing;
			x = m_insets.left;
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
/**
 * Access-metod för att ändra kolumnantalet.
 * @param cc antal kolumner
 */
public int setColumnCount( int cc )
{
	return m_columnCount = cc;
}
}
