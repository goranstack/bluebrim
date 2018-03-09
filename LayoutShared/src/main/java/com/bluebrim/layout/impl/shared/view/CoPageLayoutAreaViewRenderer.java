package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.base.shared.*;

/**
 * Renderer for CoPageLayoutAreaView
 * 
 * @author: Dennis Malmström
 */

public class CoPageLayoutAreaViewRenderer extends CoCompositePageItemViewRenderer
{
	private static Stroke m_stroke = new BasicStroke( 0 );


public void outline( CoPaintable g, CoShapePageItemView v, Rectangle bounds )
{
	CoPageLayoutAreaView V = (CoPageLayoutAreaView) v;

	g.setColor( Color.black );
	g.setStroke( m_stroke );

	com.bluebrim.base.shared.CoShapeIF s = (com.bluebrim.base.shared.CoShapeIF) v.m_shape;
	if
		( V.m_isLeftSideOfSpread || V.m_isRightSideOfSpread )
	{
		s = s.deepClone();
		s.setWidth( s.getWidth() / 2 );
	}

	g.draw( s.getShape() );
}

protected void paintContents( CoPaintable g, CoShapePageItemView v, Rectangle bounds )
{
	CoPageLayoutAreaView V = (CoPageLayoutAreaView) v;

	if
		( V.m_isLeftSideOfSpread )
	{
		Shape prevClip = g.pushClip();
		g.clip( new java.awt.geom.Rectangle2D.Double( 0, 0, v.getWidth(), v.getHeight() ) );
		
		super.paintContents( g, v, bounds );

		g.popClip( prevClip );
		
	} else if
		( V.m_isRightSideOfSpread )
	{
		Shape prevClip = g.pushClip();
		g.clip( new java.awt.geom.Rectangle2D.Double( 0, 0, v.getWidth(), v.getHeight() ) );
		
		double dx = - v.getWidth();
		g.translate( dx, 0 );
		
		super.paintContents( g, v, bounds );
		
		g.untranslate( dx, 0 );
		
		g.popClip( prevClip );

	} else {
		
		super.paintContents( g, v, bounds );
	}
}

protected void paintGrid( CoPaintable g, CoCompositePageItemView v, Rectangle bounds )
{
	super.paintGrid( g, v, bounds );

	if
		( g.getRenderingHint( PAINT_CUSTOM_GRID ) == PAINT_CUSTOM_GRID_ON )
	{
		CoPageLayoutAreaView V = (CoPageLayoutAreaView) v;

		if
			( V.m_customGridRenderer == null )
		{
			V.m_customGridRenderer = new CoGridRenderer();

			V.m_customGridRenderer.setGrid( V.m_customGrid );
		}
		
		g.setColor( CUSTOM_GRID_COLOR );
		V.m_customGridRenderer.paint( g );
	}
}
}