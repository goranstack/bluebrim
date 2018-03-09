package com.bluebrim.layout.impl.shared.view;

import java.awt.*;
import java.awt.geom.*;

/**
 * Class for rendering a colored box showing color capability.
 * Also see CoColorCapabilityRenderer
 * 
 * @author: Dennis
 */
  
public class CoSingleColorCapabilityRenderer
{
	public static final float SIZE = 10;
	
	protected Color m_color; // null = any color

	protected static Rectangle2D m_shape = new Rectangle2D.Double();
public CoSingleColorCapabilityRenderer()
{
	m_color = null;
}
public CoSingleColorCapabilityRenderer( Color c )
{
	m_color = c;
}


public void paint( Graphics2D g, int pos )
{
	if
		( m_color != null )
	{
		g.setColor( m_color );
		m_shape.setRect( pos * SIZE, 0, SIZE, SIZE );
		g.fill( m_shape );
	} else {
		g.setColor( Color.cyan );
		m_shape.setRect( pos * SIZE, 0, SIZE, SIZE / 3 );
		g.fill( m_shape );
		
		g.setColor( Color.yellow );
		m_shape.setRect( pos * SIZE, SIZE / 3, SIZE, SIZE / 3 );
		g.fill( m_shape );
		
		g.setColor( Color.magenta );
		m_shape.setRect( pos * SIZE, SIZE * 2 / 3, SIZE, SIZE / 3 );
		g.fill( m_shape );
	}

	g.setColor( Color.black );
	m_shape.setRect( pos * SIZE, 0, SIZE, SIZE );
	g.draw( m_shape );
}
}