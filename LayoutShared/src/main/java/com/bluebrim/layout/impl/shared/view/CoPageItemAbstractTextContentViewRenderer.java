package com.bluebrim.layout.impl.shared.view;

import java.awt.*;
import java.awt.geom.*;

import com.bluebrim.base.shared.*;

/**
 * Renderer for CoPageItemAbstractTextContentView
 * 
 * @author: Dennis Malmström
 */

public class CoPageItemAbstractTextContentViewRenderer extends CoPageItemContentViewRenderer
{




public void paint( CoPaintable g, CoPageItemView v, Rectangle bounds )
{
	g.addComment( "TEXT CONTENT" );
	
	CoPageItemAbstractTextContentView view = (CoPageItemAbstractTextContentView) v;
	
	paintGrid( g, view );

	if
		( view.m_activeTextEditor == null )
	{
		Shape clip = g.getClip(); // TEMPORARY FIX: prevents "fuzzy" text
		if ( clip != null ) g.setClip( clip.getBounds2D() ); // TEMPORARY FIX: prevents "fuzzy" text
		
		view.m_documentView.paint( g );
		
		if ( clip != null ) g.setClip( clip ); // TEMPORARY FIX: prevents "fuzzy" text
	}
}

protected void paintGrid( CoPaintable g, CoPageItemAbstractTextContentView v )
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
			double w = v.m_owner.m_shape.getWidth();
			Line2D l = new Line2D.Double( 0, y, w, y );

			double Y = v.m_owner.m_shape.getHeight();
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