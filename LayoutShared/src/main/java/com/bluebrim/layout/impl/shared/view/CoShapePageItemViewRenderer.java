package com.bluebrim.layout.impl.shared.view;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.stroke.shared.*;

/**
 * Renderer for CoShapePageItemView
 * 
 * @author: Dennis Malmström
 */

public abstract class CoShapePageItemViewRenderer extends CoPageItemViewRenderer
{
	// Rendering stuff shared by all views

	// scale compensated stroke cache
	private static final Map m_dashedOutlineStrokes = new HashMap(); // [ Double( scale ) -> Stroke ]
	private static final Map m_solidOutlineStrokes 	= new HashMap(); // [ Double( scale ) -> Stroke ]

	private static Paint m_transparent = new Color( 0 );

	private static Paint m_layoutFailedPaint;
	static
	{
		BufferedImage i = new BufferedImage( 10, 10, BufferedImage.TYPE_INT_RGB );
		Graphics g = i.getGraphics();
		g.setColor( Color.white );
		g.fillRect( 0, 0, 10, 10 );
		g.setColor( Color.red );
		g.fillRect( 0, 0, 5, 5 );
		g.fillRect( 5, 5, 5, 5 );
		m_layoutFailedPaint = new TexturePaint( i, new Rectangle2D.Double( 0, 0, 10, 10 ) );
	}









// predicate deciding if outline is drawn

protected boolean doOutline( CoPaintable g, CoShapePageItemView v )
{
	return
		(
			( g.getRenderingHint( PAINT_OUTLINE ) == PAINT_OUTLINE_ON )
		||
			( ( g.getRenderingHint( PAINT_MODEL_OUTLINE ) == PAINT_MODEL_OUTLINE_ON ) && isTop( v ) )
		);
}

// fill view

protected void fill( CoPaintable g, CoShapePageItemView v, Rectangle bounds)
{
	if
		( v.m_fillStyle != null )
	{
		if
			( v.m_fillPaint == null )
		{
			v.m_fillPaint = v.m_fillStyle.getPaint( v.m_shape.getBounds2D() );
			if ( v.m_fillPaint == null ) v.m_fillPaint = m_transparent; // no color
		}

		if
			( v.m_fillPaint != m_transparent )
		{
			g.setPaint( v.m_fillPaint );
			g.fill( v.m_effectiveShape.getShape() );
		}
	}
}

// predicate deciding if outline is drawn

public boolean isTop( CoShapePageItemView v )
{
	if ( v.getParent() == null ) return true;
	if ( v.getParent().getParent() == null ) return true;
	return false;
}

// paint outline

protected void outline( CoPaintable g, CoShapePageItemView v, Rectangle bounds )
{
	// save rendering state
	Stroke s = g.getStroke();
	Color c = g.getColor();
	
	double scale = g.getScale();//CoBaseUtilities.getXScale( g.getTransform() );
	
	// set outline rendering state
	float k = (float) ( 1 / scale );

	Stroke stroke;
	if
		( g.getRenderingHint( PAINT_OUTLINE_STYLE ) == PAINT_OUTLINE_DASHED )
	{
		stroke = (Stroke) m_dashedOutlineStrokes.get( new Double( scale ) );
		if
			( stroke == null )
		{
			stroke = new BasicStroke( k, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float [] { k, k }, 0 );
			m_dashedOutlineStrokes.put( new Double( scale ), stroke );
		}
	} else {
		stroke = (Stroke) m_solidOutlineStrokes.get( new Double( scale ) );
		if
			( stroke == null )
		{
			stroke = new BasicStroke( k, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1 );
			m_solidOutlineStrokes.put( new Double( scale ), stroke );
		}
	}

	g.setColor( Color.gray );
	g.setStroke( stroke );

	g.draw( v.m_effectiveShape.createExpandedInstance( k / 2 ).getShape() );

	g.setStroke( s );
	g.setColor( c );
}

public void paint( CoPaintable g, CoPageItemView v, Rectangle bounds )
{
	CoShapePageItemView view = (CoShapePageItemView) v;

	if ( g.supressPaint( view.getSupressPrintout() ) ) return;
	
	if
		( DO_OPTIMIZE_PAINT && ( bounds != null ) )
	{
		if
			(
				( view.m_X + view.m_W < bounds.x )
			||
				( view.m_X > bounds.x + bounds.width )
			||
				( view.m_Y + view.m_H < bounds.y )
			||
				( view.m_Y > bounds.y + bounds.height )
			)
		{
			if
				( DO_TRACE_OPTIMIZED_PAINT )
			{
				System.err.println( "PAINT OPTIMIZER: skipped paint for " + view.getName() + "  " + bounds );
				System.err.println( "                                   " + view.m_X + " " + view.m_Y + " " + view.m_W + " " + view.m_H );
			}

			// no part of view is on screen -> no need to paint it
			return;
		}
	}

	g.addComment( "BEGIN " + view.getName() );

	boolean doTransform = ( view.m_transform != null ) && ! view.m_transform.isIdentity();
	boolean doTranslate = ( view.m_x != 0 ) || ( view.m_y != 0 );
	
	g.pushTransform();
	if ( doTranslate ) g.translate( view.m_x, view.m_y );
	if ( doTransform ) view.m_transform.applyOn( g );

	g.addComment( "FILL " + view.getName() );
	fill( g, view, bounds );
	
	g.addComment( "CONTENT " + view.getName() );
	paintContents( g, view, bounds );
	
	g.addComment( "STROKE " + view.getName() );
	stroke( g, view, bounds );
	
	if ( doOutline( g, view ) ) outline( g, view, bounds ); // decoration
	
	if ( doTransform ) view.m_transform.unapplyOn( g );
	if ( doTranslate ) g.untranslate( view.m_x, view.m_y );
	g.popTransform();

	g.addComment( "END " + view.getName() );
}

protected abstract void paintContents( CoPaintable g, CoShapePageItemView v, Rectangle bounds );

// stroke view

protected void stroke( CoPaintable g, CoShapePageItemView v, Rectangle bounds)
{
	if
		( v.m_layoutFailed )
	{
		// this is actually a fill but it must be performed here or it will be overpainted
		g.setPaint( m_layoutFailedPaint );
		g.fill( v.m_effectiveShape.getShape() );
	} else {
		if
			( v.m_strokeRenderer == null )
		{
			v.m_strokeRenderer = new CoStrokeRenderer();
			v.m_strokeRenderer.setStrokeProperties( v.m_strokeProperties );
			v.m_strokeRenderer.setShape( v.m_strokeEffectiveShape ? v.m_effectiveShape : v.m_shape );
		}

		Stroke s = g.getStroke();
		Paint p = g.getPaint();
		
		v.m_strokeRenderer.paint( g );

		g.setStroke( s );
		g.setPaint( p );
	}
}
}