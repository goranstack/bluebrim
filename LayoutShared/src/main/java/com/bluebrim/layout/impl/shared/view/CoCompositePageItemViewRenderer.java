package com.bluebrim.layout.impl.shared.view;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import com.bluebrim.base.shared.*;

/**
 * Renderer for CoLayoutAreaView
 * 
 * @author: Dennis Malmström
 */
 
public abstract class CoCompositePageItemViewRenderer extends CoShapePageItemViewRenderer
{














protected void paintAfterChildren( CoPaintable g, CoCompositePageItemView v, Rectangle bounds)
{
	if
		( DO_PAINT_GEOMETRY || DO_PAINT_PI || DO_PAINT_PIV )
	{
		g.setColor( java.awt.Color.black );
		String str = ( DO_PAINT_GEOMETRY ? "( " + v.m_X + ", " + v.m_Y + " x " + v.m_W + ", " + v.m_H + " ) " : "" ) +
		             ( DO_PAINT_PI ? v.m_pageItem.toString() : "" ) +
		             ( DO_PAINT_PIV ? v.toString() : "" );
		g.drawDecorationString( str, 5.0f, 15.0f, null );
	}
}

protected void paintBeforeChildren( CoPaintable g, CoCompositePageItemView v, Rectangle bounds)
{
}

protected void paintChildren( CoPaintable g, CoCompositePageItemView v, Rectangle bounds )
{
	Shape prevClip = null;
	if
		( v.getClipping() != null )
	{
		prevClip = g.pushClip();
		g.clip( v.getClipping() );
	}
		
	paintGrid( g, v, bounds );

	boolean skip = false;
	
	if
		( DO_OPTIMIZE_PAINT_CHILDREN )
	{
		// don't paint children for very small parents
		if
			(
				( v.m_W < 10 )
			&&
				( v.m_H < 10 )
			)
		{
			if
				( DO_TRACE_OPTIMIZED_PAINT )
			{
				System.err.println( "PAINT OPTIMIZER: skipped paint children for " + this );
			}
			skip = true;;
		}
	}

	if
		( ! skip )
	{
		Iterator e = v.m_children.iterator();
		while
			( e.hasNext() )
		{
			( (CoShapePageItemView) e.next() ).paint( g, bounds );
		};
	}
		
	if ( v.getClipping() != null ) g.popClip( prevClip );
}

protected void paintContents( CoPaintable g, CoShapePageItemView v, Rectangle bounds )
{
	CoCompositePageItemView view = (CoCompositePageItemView) v;
	
	paintBeforeChildren( g, view, bounds );
	paintChildren( g, view, bounds );
	paintAfterChildren( g, view, bounds );
}

protected void paintGrid( CoPaintable g, CoCompositePageItemView v, Rectangle bounds )
{
	if
		( g.getRenderingHint( PAINT_BASE_LINE_GRID ) == PAINT_BASE_LINE_GRID_ON )
	{
		if
			( v.m_baseLineGeometry.getDeltaY() > 0 )
		{
			Color c = g.getColor();
			Stroke s = g.getStroke();
			g.setColor( BASE_LINE_GRID_COLOR );
			g.setStroke( new BasicStroke( (float) ( 1 / g.getScale() ) ) );
			
			double y = v.m_baseLineGeometry.getY0();
			double w = v.m_shape.getWidth();
			Line2D l = new Line2D.Double( 0, y, w, y );

			double Y = v.m_shape.getHeight();
			while
				( y <= Y )
			{
				g.draw( l );
				y += v.m_baseLineGeometry.getDeltaY();
				l.setLine( 0, y, w, y );
			}
			g.setColor( c );
			g.setStroke( s );
		}
	}

	
	if
		( g.getRenderingHint( PAINT_COLUMN_GRID ) == PAINT_COLUMN_GRID_ON )
	{
		if
			( v.m_columnGridRenderer == null )
		{
			v.m_columnGridRenderer = new CoColumnGridRenderer();
			v.m_columnGridRenderer.setColumnGrid( v.m_columnGrid );
		}
			
		g.setColor( COLUMN_GRID_COLOR );
		v.m_columnGridRenderer.paint( g );
	}
}
}