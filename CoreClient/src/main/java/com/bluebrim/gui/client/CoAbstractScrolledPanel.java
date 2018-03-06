package com.bluebrim.gui.client;

// JDK imports
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import javax.swing.BorderFactory;
import javax.swing.JScrollBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.bluebrim.base.client.CoScrolledHorizontalFlowPanelLayout;
import com.bluebrim.swing.client.CoPanel;



/**
 * En abstrakt subklass till CoPanel som implementerar scrollning av en panel vars storlek
 * kan variera. En CoAbstractScrolledPanel måste ha en layout-manager som subklassats från
 * CoAbstractScrolledPanelLayout.
 * När barn ska läggas till eller tas bort så ska den container som returneras av
 * metoden getContentPane användas.
*/
public abstract class CoAbstractScrolledPanel
extends CoPanel
implements ChangeListener, ContainerListener, ComponentListener
{
	protected JScrollBar m_horizontalScrollBar;
	protected JScrollBar m_verticalScrollBar;
	protected CoPanel    m_view;  // Container i vilken barnen finns
		
	protected int m_horizontalScrollValue;
	protected int m_verticalScrollValue;
	
/**
 * Konstruktor
 * @param isDoubleBuffered @see CoPanel
 * @param layout @see CoPanel
 */
public CoAbstractScrolledPanel( CoAbstractScrolledPanelLayout layout, boolean isDoubleBuffered )
{
	super( layout, isDoubleBuffered );

		// skapa default marginaler
	setBorder( BorderFactory.createLoweredBevelBorder() );

		// skapa scrollbars
	m_horizontalScrollBar = new JScrollBar( JScrollBar.HORIZONTAL );
	m_verticalScrollBar = new JScrollBar( JScrollBar.VERTICAL );
	add( m_horizontalScrollBar );
	add( m_verticalScrollBar );
	
		// lyssna på scroll-events
	m_horizontalScrollBar.getModel().addChangeListener( this );
	m_verticalScrollBar.getModel().addChangeListener( this );

		// spara scroll-tillstånd
	m_horizontalScrollValue = m_horizontalScrollBar.getValue();
	m_verticalScrollValue = m_verticalScrollBar.getValue();
	
		// skapa container att ha barnen i
	m_view = new CoPanel( (LayoutManager2) null );
	m_view.setDoubleBuffered(isDoubleBuffered);
	add( m_view );
	
		// lyssna på container-events
	m_view.addContainerListener( this );
}
/**
 * Implementation av ContainerListener-gränssnittet.
 * @param e innehåller bland annat det nya barnet
*/
public void componentAdded( ContainerEvent e )
{
		// lyssna på barnets förändringar.
	e.getChild().addComponentListener( this );
	
		// rita om
	if
		( getParent() != null )
	{
		doLayout();
	}	
}
/**
 * Implementation av ComponentListener-gränssnittet.
 * @param e 
*/
public void componentHidden( ComponentEvent e )
{
		// rita om
	if ( getParent() != null ) repaint();
}
public void componentMoved( ComponentEvent e )
{
}
/**
 * Implementation av ContainerListener-gränssnittet.
 * @param e innehåller bland annat det barn om tagits bort. 
*/
public void componentRemoved( ContainerEvent e )
{
		// sluta lyssna på barnets förändringar.
	e.getChild().removeComponentListener( this );
	
		// rita om
	if
		( getParent() != null )
	{
		doLayout();
		repaint();
	}	
}
/**
 * Implementation av ComponentListener-gränssnittet.
 * @param e 
*/
public void componentResized( ComponentEvent e )
{
		// rita om
	if
		( getParent() != null )
	{
		doLayout();
		repaint();
	}	
}
/**
 * Implementation av ComponentListener-gränssnittet.
 * @param e 
*/
public void componentShown( ComponentEvent e )
{
		// rita om
	if ( getParent() != null ) repaint();
}
/**
 * Access-metod view-containern.
 * @return view-containern
*/
public CoPanel getContentPane()
{
	return m_view;
}
/**
 * Access-metod view-containerns storlekskrav.
 * Obs: ej nödvändigtvis samma som getContentPane().getSize().
 * @return view-containerns  storlekskrav
*/
public Dimension getContentPaneSize()
{
	return ( (CoAbstractScrolledPanelLayout) getLayout() ).getViewSize();
}
/**
 * Access-metod till layout-managerns horisontella mellanrum.
 * @return layout-managerns horisontella mellanrum.
 * @see CoScrolledHorizontalFlowPanelLayout
*/
public int getHorizontalSpacing()
{
	return ( (CoAbstractScrolledPanelLayout) getLayout() ).getHorizontalSpacing();
}
/**
 * Access-metod till layout-managerns marginaler.
 * @return layout-managerns marginaler
 * @see CoScrolledHorizontalFlowPanelLayout
*/
public Insets getInternalInsets()
{
	return ( (CoAbstractScrolledPanelLayout) getLayout() ).getInternalInsets();
}
/**
 * Access-metod till layout-managerns vertikala mellanrum.
 * @return layout-managerns vertikala mellanrum.
 * @see CoScrolledHorizontalFlowPanelLayout
*/
public int getVerticalSpacing()
{
	return ( (CoAbstractScrolledPanelLayout) getLayout() ).getVerticalSpacing();
}
public void setBackground(Color bkgColor)
{
	super.setBackground(bkgColor);
	if (m_view != null)
		m_view.setBackground(bkgColor);
}
public void setDoubleBuffered(boolean aFlag)
{
	super.setDoubleBuffered(aFlag);
	if (m_view != null)
		m_view.setDoubleBuffered(aFlag);	
}
/**
 * Set-metod för layout-managerns horisontella mellanrum.
 * @param s det nya mellanrumsvärdet
 * @see CoScrolledHorizontalFlowPanelLayout
*/
public int setHorizontalSpacing( int s )
{
	return ( (CoAbstractScrolledPanelLayout) getLayout() ).setHorizontalSpacing( s );
}
/**
 * Set-metod för layout-managerns marginaler.
 * @param i de nya marginalerna
 * @see CoScrolledHorizontalFlowPanelLayout
*/
public Insets setInternalInsets( Insets i )
{
	return ( (CoAbstractScrolledPanelLayout) getLayout() ).setInternalInsets( i );
}
public void setOpaque(boolean isOpaque)
{
	super.setOpaque(isOpaque);
	if (m_view != null)
		m_view.setOpaque(isOpaque);
}
/**
 * Set-metod för layout-managerns mellanrum.
 * @param s det nya mellanrumsvärdet
 * @see CoScrolledHorizontalFlowPanelLayout
*/
public void setSpacing( int hs, int vs )
{
	( (CoAbstractScrolledPanelLayout) getLayout() ).setSpacing( hs, vs );
}
/**
 * Set-metod för layout-managerns vertikala mmellanrum.
 * @param s det nya mellanrumsvärdet
 * @see CoScrolledHorizontalFlowPanelLayout
*/
public int setVerticalSpacing( int s )
{
	return ( (CoAbstractScrolledPanelLayout) getLayout() ).setVerticalSpacing( s );
}
/**
 * Implementation av ChangeListener-gränssnittet.
 * Anropas när användaren drar i en scrollbar.
 * @param e information om scrollningshändelsen.
*/
public void stateChanged( ChangeEvent e )
{
		// sampla nya scrollvärdena
	int horizontalScrollValue = m_horizontalScrollBar.getValue();
	int verticalScrollValue = m_verticalScrollBar.getValue();

		// beräkna delta
	int dx = horizontalScrollValue - m_horizontalScrollValue;
	int dy = verticalScrollValue - m_verticalScrollValue;

		// nollställ icke-aktiva scrollbars
	if
		( ( dx != 0 ) && ( ! m_horizontalScrollBar.isVisible() ) )
	{
		dx = 0;
		m_horizontalScrollValue = 0;
	}

	if
		( ( dy != 0 ) && ( ! m_verticalScrollBar.isVisible() ) )
	{
		dy = 0;
		m_verticalScrollValue = 0;
	}
	
		// sluta här om delta är noll
	if ( ( dx == 0 ) && ( dy == 0 ) ) return;
		
		// applicera deltat på alla barns position
	Component[] children = m_view.getComponents();

	for
		( int i = 0; i < children.length; i++ )
	{
		Point p = children[ i ].getLocation();
		p.x = p.x - dx;
		p.y = p.y - dy;
		children[ i ].setLocation( p );
	}

		// spara nya scrollvärdena
	m_horizontalScrollValue = horizontalScrollValue;
	m_verticalScrollValue = verticalScrollValue;
}
}
