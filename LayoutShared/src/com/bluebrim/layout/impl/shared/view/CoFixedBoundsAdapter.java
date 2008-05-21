package com.bluebrim.layout.impl.shared.view;

import java.awt.*;
import java.awt.geom.*;

import com.bluebrim.base.shared.*;

/**
 * A subclass to CoFixedBoundsAdapter that also forces a fixed (by scaling it) size upon the view.
 * 
 * @author: Dennis Malmström
 */

public class CoFixedBoundsAdapter extends CoFixedPositionAdapter
{



	protected boolean m_centeredX;
	protected boolean m_centeredY;











public final void setChildSize( double w, double h )
{
	m_fixedBounds.setSize( w, h );
}
public final void setChildSize( Dimension2D childSize )
{
	if
		( childSize == null )
	{
		setChildSize( 0, 0 );
	} else {
		setChildSize( childSize.getWidth(), childSize.getHeight() );
	}
}

	private Shape m_clip;
	protected Dimension2D m_fixedBounds = new CoDimension2D();
	// temporary state saved between calls to prePaint and postPaint
	private double m_scale;

public CoFixedBoundsAdapter( Container container, Point2D childPosition, Dimension2D childSize, boolean centeredX, boolean centeredY, boolean useOuterStrokes )
{
	super( container, childPosition, useOuterStrokes );

	setChildSize( childSize );

	m_centeredX = centeredX;
	m_centeredY = centeredY;
}

protected double calculateChildHeight( CoShapePageItemView view )
{
	return view.getHeight() - ( m_useOuterStrokes ? view.getStrokeProperties().getOutsideWidth() : 0 );
}

protected double calculateChildWidth( CoShapePageItemView view )
{
	return view.getWidth() - ( m_useOuterStrokes ? view.getStrokeProperties().getOutsideWidth() : 0 );
}

public void postPaint( CoShapePageItemView view, CoPaintable g, Rectangle bounds )
{
	g.untranslate( -m_dx, -m_dy );
	g.unscale( m_scale, m_scale );
}

public void prePaint( CoShapePageItemView view, CoPaintable g, Rectangle bounds )
{
	m_dx = view.getX() - childX( view );
	m_dy = view.getY() - childY( view );
	m_scale = 1;

	double cw = calculateChildWidth( view );
	double ch = calculateChildHeight( view );

	// scale to fit, keep aspect ratio
	if
		( m_fixedBounds.getWidth() > 0 )
	{
		double sx = m_fixedBounds.getWidth() / cw;
		double sy = m_fixedBounds.getHeight() / ch;
		m_scale = Math.min( sx, sy );
	}

	// allign
	if
		( m_centeredX )
	{
		m_dx -= ( m_fixedBounds.getWidth() / m_scale - cw ) / 2;
	}

	if
		( m_centeredY )
	{
		m_dy -= ( m_fixedBounds.getHeight() / m_scale - ch ) / 2;
	}

	// paint
	g.scale( m_scale, m_scale );
	g.translate( -m_dx, -m_dy );
}

public void prepareHitTestGraphicsFor( Graphics2D g, CoShapePageItemView view )
{
	super.prepareHitTestGraphicsFor( g, view );

	double sx = 1;
	double sy = 1;

	if
		( m_fixedBounds.getWidth() > 0 )
	{
		sx = m_fixedBounds.getWidth() / view.getWidth();
		sy = m_fixedBounds.getHeight() / view.getHeight();
	}

	double s = Math.min( sx, sy );
	
	g.scale( s, s );
}

public final void set( Dimension2D childSize, boolean centeredX, boolean centeredY )
{
	setChildSize( childSize );

	m_centeredX = centeredX;
	m_centeredY = centeredY;
}
}