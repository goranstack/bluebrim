package com.bluebrim.text.impl.client.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Implementation of a handle for dragging about horizontal margins.
 * The model of this component must implement CoHorizontalMarginController.MarginHolder
 * 
 * @author: Dennis Malmström
 */
 
public class CoHorizontalMarginController implements MouseMotionListener
{
	// interface for model
	public interface MarginHolder
	{
		int getHorizontalMargin();
		void setHorizontalMargin( int m );
		JComponent getComponent();
	};


	private MarginHolder m_marginHolder; // model

	private Cursor m_originalCursor = m_cursor;
	
	public static final int SIZE = 3;
	private final static Cursor m_cursor = Cursor.getPredefinedCursor( Cursor.E_RESIZE_CURSOR );
public CoHorizontalMarginController( MarginHolder mh )
{	
	m_marginHolder = mh;

	m_marginHolder.getComponent().addMouseMotionListener( this );
}
public void mouseDragged( MouseEvent ev )
{
	if ( m_marginHolder.getComponent().getCursor() != m_cursor ) return;
	
	int x = ev.getX();

	if
		( x > m_marginHolder.getComponent().getWidth() / 2 )
	{
		x = m_marginHolder.getComponent().getWidth() - x;
	}

	m_marginHolder.setHorizontalMargin( x );
}
public void mouseMoved(MouseEvent ev)
{
	if
		( m_marginHolder.getComponent().getCursor() != m_cursor )
	{
		m_originalCursor = m_marginHolder.getComponent().getCursor();
	}
	
		
	int y = ev.getY() + m_marginHolder.getComponent().getY();
	
	if
		( y <= SIZE )
	{
		int x = ev.getX();

		if
			( Math.abs( x - m_marginHolder.getHorizontalMargin() ) <= SIZE )
		{
			m_marginHolder.getComponent().setCursor( m_cursor );
			return;
		}

		if
			( Math.abs( ( m_marginHolder.getComponent().getWidth() - x ) - m_marginHolder.getHorizontalMargin() ) <= SIZE )
		{
			m_marginHolder.getComponent().setCursor( m_cursor );
			return;
		}
	}

	m_marginHolder.getComponent().setCursor( m_originalCursor );
}
// paint the handle

public void paint( Graphics g )
{
	g.setColor( Color.black );
	
	int y0 = - m_marginHolder.getComponent().getY();
	
	int Y = CoHorizontalMarginController.SIZE;
	int W = m_marginHolder.getComponent().getWidth();
	
	for
		( int y = 0, x = m_marginHolder.getHorizontalMargin() - Y, w = 2 * Y - 1; y < Y; y++, x++, w -= 2 )
	{
		g.drawLine( x, y0 + y, x + w, y0 + y );
		g.drawLine( W - x, y0 + y, W - ( x + w ), y0 + y );
	}
}
}
