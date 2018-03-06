package com.bluebrim.layout.impl.shared.view;


import java.awt.*;
import java.awt.geom.*;

import com.bluebrim.base.shared.*;





/**
 * A page item view adapter that forces the view to be displayed in a fixed postion.
 * 
 * @author: Dennis Malmström
 */

public class CoFixedPositionAdapter implements CoPageItemViewAdapter
{


//	protected CoShapePageItemView m_child; // the wrapped view

	// the fixed postition
	protected double m_childX;
	protected double m_childY;








public final double getChildX()
{
	return m_childX;
}
public final double getChildY()
{
	return m_childY;
}









public final void setChildPosition( double x, double y )
{
	m_childX = x;
	m_childY = y;
}
public final void setChildPosition( Point2D childPosition )
{
	if
		( childPosition == null )
	{
		setChildPosition( 0, 0 );
	} else {
		setChildPosition( childPosition.getX(), childPosition.getY() );
	}
}


















	protected Container m_container;
	// temporary state saved between calls to prePaint and postPaint
	protected double m_dx;
	protected double m_dy;
	protected boolean m_useOuterStrokes;

public CoFixedPositionAdapter( Container container, Point2D childPosition, boolean useOuterStrokes )
{
	m_useOuterStrokes = useOuterStrokes;
	m_container = container;
	setChildPosition( childPosition );
}

// hook for overriding fix position calculation

protected double childX( CoShapePageItemView view )
{
	return m_childX + ( m_useOuterStrokes ? view.getStrokeProperties().getOutsideWidth() : 0 );
}

// hook for overriding fix position calculation

protected double childY( CoShapePageItemView view )
{
	return m_childY + ( m_useOuterStrokes ? view.getStrokeProperties().getOutsideWidth() : 0 );
}

// delegate to wrapped view

public Container getContainer( CoShapePageItemView view )
{
	if
		( m_container != null )
	{
		return m_container;
	} else if
		( view.getParent() == null )
	{
		return null;
	} else {
		return view.getParent().getContainer();
	}
}

public void postPaint( CoShapePageItemView view, CoPaintable g, Rectangle bounds )
{
	g.untranslate( -m_dx, -m_dy );	// fixed child position
}

public void prePaint( CoShapePageItemView view, CoPaintable g, Rectangle bounds )
{
	m_dx = view.getX() - childX( view );
	m_dy = view.getY() - childY( view );

	g.translate( -m_dx, -m_dy );	// fixed child position
}

public void prepareAffineTransformFor( AffineTransform a, CoShapePageItemView view )
{
	// fixed child position
	double dx = childX( view ) - view.getX();
	double dy = childY( view ) - view.getY();
	a.translate( dx, dy );
}

public void prepareHitTestGraphicsFor( Graphics2D g, CoShapePageItemView view )
{
	// fixed child position
	double dx = childX( view ) - view.getX();
	double dy = childY( view ) - view.getY();
	g.translate( dx, dy );
}

public void transformFromParent( CoShapePageItemView view, Point2D p )
{
	// fixed child position
	double dx = childX( view );// - view.getX();
	double dy = childY( view );// - view.getY();

	p.setLocation( p.getX() - dx, p.getY() -+ dy );
}

public void transformToParent( CoShapePageItemView view, Point2D p )
{
	// fixed child position
	double dx = childX( view );// - view.getX();
	double dy = childY( view );// - view.getY();
	
	p.setLocation( p.getX() + dx, p.getY() + dy );
}
}