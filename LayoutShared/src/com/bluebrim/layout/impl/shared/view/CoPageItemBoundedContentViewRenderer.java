package com.bluebrim.layout.impl.shared.view;

import java.awt.*;
import java.awt.geom.*;

import com.bluebrim.base.shared.*;

/**
 * Renderer for CoPageItemBoundedContentView
 * 
 * @author: Dennis Malmström
 */

public abstract class CoPageItemBoundedContentViewRenderer extends CoPageItemContentViewRenderer
{
	// Rendering stuff shared by all views

	protected static final Line2D m_line = new Line2D.Double();
	protected static final Rectangle2D m_rect = new Rectangle2D.Double();

	protected static final Stroke m_emptyStroke = new BasicStroke( 0 );
	protected static final Stroke m_isClippedStroke = new BasicStroke( 3 );
	
	private static final Shape m_isClippedIndicator;
	static
	{
		GeneralPath p = new GeneralPath();
		p.moveTo( 0, 0 );
		p.lineTo( -10, -10 );
		p.moveTo( -10, 0 );
		p.lineTo( 0, -10 );
		m_isClippedIndicator = p;
	}






protected abstract void doPaintContent( CoPaintable g, CoPageItemBoundedContentView v, Rectangle bounds );

public void paint( CoPaintable g, CoPageItemView v, Rectangle bounds )
{
	CoPageItemBoundedContentView view = (CoPageItemBoundedContentView) v;
	
	if
		( view.hasContent() )
	{
		paintContent( g, view, bounds );
	} else {
		paintNoContent( g, view, bounds );
	}
}

protected void paintContent( CoPaintable g, CoPageItemBoundedContentView v, Rectangle bounds )
{
	g.translate( v.m_x, v.m_y );
	g.scale( v.m_scaleX, v.m_scaleY );

	if
		( v.m_flipX )
	{
		g.translate( v.getContentWidth(), 0 );
		g.scale( -1, 1 );
	}

	if
		( v.m_flipY )
	{
		g.translate( 0, v.getContentHeight() );
		g.scale( 1, -1 );
	}

	doPaintContent( g, v, bounds );
	
	if
		( v.m_flipY )
	{
		g.unscale( 1, -1 );
		g.untranslate( 0, v.getContentHeight() );
	}

	if
		( v.m_flipX )
	{
		g.unscale( -1, 1 );
		g.untranslate( v.getContentWidth(), 0 );
	}

	g.unscale( v.m_scaleX, v.m_scaleY );
	g.untranslate( v.m_x, v.m_y );
	
	// decorations
	if
		( v.m_isClipped && ( g.getRenderingHint( PAINT_IMAGE_CLIP_INDICATOR ) == PAINT_IMAGE_CLIP_INDICATOR_ON ) )
	{
		g.translate( v.m_owner.getWidth() - 5, v.m_owner.getHeight() - 5 );
		g.setStroke( m_isClippedStroke );
		g.setColor( PAINT_IMAGE_CLIP_INDICATOR_COLOR );
		g.draw( m_isClippedIndicator );
		g.translate( - v.m_owner.getWidth() + 5, - v.m_owner.getHeight() + 5 );
	}
}

// no content -> paint the diagonals

protected void paintNoContent( CoPaintable g, CoPageItemBoundedContentView v, Rectangle bounds )
{
	g.setStroke( m_emptyStroke );
	double w = v.m_owner.m_shape.getWidth();
	double h = v.m_owner.m_shape.getHeight();
	
	g.setColor( Color.black );
	
	m_line.setLine( 0, 0, w, h );
	g.draw( m_line );
	
	m_line.setLine( w, 0, 0, h );
	g.draw( m_line );
}
}