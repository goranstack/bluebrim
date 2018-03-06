package com.bluebrim.base.client;

// JDK imports
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import com.bluebrim.gui.client.CoAbstractScrolledPanelLayout;



/**
 * Subklass till CoAbstractScrolledPanelLayout som anv�nds av CoScrolledHorizontalGridPanel.
 * B�r flyttas in i CoScrolledHorizontalGridPanel n�r milj�n s� till�ter.
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
 * Ber�knar maximala storleken p� view-containern.
 * @param parent view-containern
 * @return maximala storleken
*/
protected Dimension getMaximumScrolledPanelSize( Container parent )
{
	Component[] children = parent.getComponents();

		// ber�kna h�gsta komponenth�jden
	int maximumHeight = 0;
	for
		( int i = 0; i < children.length; i++ )
	{
		maximumHeight = Math.max( maximumHeight, children[ i ].getMaximumSize().height );
	}
	
		// ber�kna storleken
	int maxX = 0;
	int x = m_insets.left;
	int y = m_insets.top;
	
		// iterera �ver barnen
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
		maxX = Math.max( maxX, x );	// ber�kna h�gsta radbredd

			// ber�kna n�sta barns position
		x = x + children[ i ].getMaximumSize().width + m_horizontalSpacing;
	}
	maxX = Math.max( maxX, x );	// ber�kna h�gsta radbredd
	
	return new Dimension( maxX - m_horizontalSpacing, y + maximumHeight );
}
/**
 * Ber�knar minimala storleken p� view-containern.
 * @param parent view-containern
 * @return minimala storleken
*/
protected Dimension getMinimumScrolledPanelSize( Container parent )
{
	Component[] children = parent.getComponents();

		// ber�kna h�gsta komponenth�jden
	int maximumHeight = 0;
	for
		( int i = 0; i < children.length; i++ )
	{
		maximumHeight = Math.max( maximumHeight, children[ i ].getMinimumSize().height );
	}
	
		// ber�kna storleken
	int maxX = 0;
	int x = m_insets.left;
	int y = m_insets.top;
	
		// iterera �ver barnen
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
		maxX = Math.max( maxX, x );	// ber�kna h�gsta radbredd

			// ber�kna n�sta barns position
		x = x + children[ i ].getMinimumSize().width + m_horizontalSpacing;
	}
	maxX = Math.max( maxX, x );	// ber�kna h�gsta radbredd
	
	return new Dimension( maxX - m_horizontalSpacing, y + maximumHeight );
}
/**
 * Ber�knar optimala storleken p� view-containern.
 * @param parent view-containern
 * @return optimala storleken
*/
protected Dimension getPreferredScrolledPanelSize( Container parent )
{
	Component[] children = parent.getComponents();

		// ber�kna h�gsta komponenth�jden
	int maximumHeight = 0;
	for
		( int i = 0; i < children.length; i++ )
	{
		maximumHeight = Math.max( maximumHeight, children[ i ].getPreferredSize().height );
	}
	
		// ber�kna storleken
	int maxX = 0;
	int x = m_insets.left;
	int y = m_insets.top;
	
		// iterera �ver barnen
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
		maxX = Math.max( maxX, x );	// ber�kna h�gsta radbredd

			// ber�kna n�sta barns position
		x = x + children[ i ].getPreferredSize().width + m_horizontalSpacing;
	}
	maxX = Math.max( maxX, x );	// ber�kna h�gsta radbredd
	
	return new Dimension( maxX - m_horizontalSpacing, y + maximumHeight );
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
			( ( i > 0 ) && ( i % m_columnCount == 0 ) )
		{
				// radbryt
			maxX = Math.max( maxX, x );
			y = y + m_maximumHeight + m_verticalSpacing;
			x = m_insets.left;
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
/**
 * Access-metod f�r att �ndra kolumnantalet.
 * @param cc antal kolumner
 */
public int setColumnCount( int cc )
{
	return m_columnCount = cc;
}
}
