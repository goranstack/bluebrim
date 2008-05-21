package com.bluebrim.gui.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Class som hanterar att rita och returnera en "RubberBandSelection".
 */
 
public class CoRubberBand implements MouseMotionListener
{
	Graphics  m_g;
	Dimension m_size;
	Color     m_bg;
	Component m_component;
	Cursor    m_originalCursor;
	
	Rectangle m_rect = new Rectangle();
	

private Rectangle getDrawableRect()
{
	int x = m_rect.x;
	int y = m_rect.y;
	int width = m_rect.width;
	int height = m_rect.height;

		//Positiv höjd och bredd i rektangel.
	if ( width < 0 )
	{
		width = 0 - width;
		x = x - width + 1;
		if (x < 0)
		{
			width += x;
			x = 0;
		}
	}

	if (height < 0)
	{
		height = 0 - height;
		y = y - height + 1;
		if (y < 0)
		{
			height += y;
			y = 0;
		}
	}

		//Rita i Component, inte utanför.
	if ((x + width) > m_size.width)
	{
		width = m_size.width - x;
	}

	if ((y + height) > m_size.height)
	{
		height = m_size.height - y;
	}

	return new Rectangle( x, y, width, height );
}
public Rectangle getRectangle()
{
	return m_rect;
}
public boolean isActive()
{
	return m_component != null;
}
public void mouseDragged(MouseEvent e)
{
	move( e );		
}
public void mouseMoved(MouseEvent e)
{
	move( e );		
}
protected void move( MouseEvent e )
{
	int x = e.getX();
	int y = e.getY();
		
	rubberPaint(); //Ta bort den gamla rektangeln.
		
	m_rect.setSize( x - m_rect.x, y - m_rect.y );
		
	rubberPaint();// rita den nya...
}
private void rubberPaint()
{
		//Finns det en rektangel att rita?
	Rectangle box = getDrawableRect();
			
		//Sätt XOR -ritande
	m_g.setXORMode( m_bg );
	
		//Draw the box outline.
	m_g.drawRect( box.x, box.y, box.width - 1, box.height - 1 );
}
public void start( Component c, int x, int y )
{
	start( c, x, y, null );
}
public void start( Component c, int x, int y, Cursor cursor )
{
		// Ta fram uppgifer om händelsekällan
	m_component = c;
	
	m_size    = m_component.getSize();
	m_g       = m_component.getGraphics();
	m_bg      = m_component.getBackground();
		
	//Skapa markerat område innan något är markerat.
	m_rect = new Rectangle( x, y, 0, 0);
	
	if
		( cursor != null )
	{
		m_originalCursor = m_component.getCursor();
		m_component.setCursor( cursor );
	}	
	
	m_component.addMouseMotionListener( this );
}
public Rectangle stop()
{
	rubberPaint();
	m_component.removeMouseMotionListener( this );
	m_rect = getDrawableRect();
	
	if
		( m_originalCursor != null )
	{
		m_component.setCursor( m_originalCursor );
		m_originalCursor = null;
	}	
	
	m_component = null;
	m_size = null;
	m_bg = null;
	m_component = null;
	
	return m_rect;
}
}
