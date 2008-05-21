package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.layout.shared.*;

/**
 * Renderer for grids.
 * 
 * @author: Dennis Malmström
 */

public class CoGridRenderer
{
	protected CoImmutableSnapGridIF m_grid; // the grid being rendered

	// rendering cache
	protected double m_cachedScale = Double.NaN;
	protected Shape m_cachedShape;
	protected Stroke m_cachedStroke;
	protected boolean m_isCacheValid = false;

public void setGrid( CoImmutableSnapGridIF grid )
{
	m_grid = grid;

	m_isCacheValid = false;
}

public void paint( com.bluebrim.base.shared.CoPaintable g )
{
	double scale = g.getScale();//com.bluebrim.base.shared.CoBaseUtilities.getXScale( g.getTransform() );
	
	if
		( m_grid != null )
	{
		if
			(
				( scale != m_cachedScale )
			||
				( ! m_isCacheValid )
			)
		{
			// refresh cache
			m_cachedScale = scale;
			m_cachedShape = m_grid.getShape( 0 );
			m_cachedStroke = new BasicStroke( (float) Math.abs( 1 / m_cachedScale ) );
			m_isCacheValid = true;
		}

		if
			( m_cachedShape != null )
		{
			g.setStroke( m_cachedStroke );
			g.draw( m_cachedShape );
		}
	}
}
}