package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.text.shared.*;

/**
 * Subclass of CoViewPanel that displays shape page item views.
 * The view can be scaled a fixed size.
 *
 * @author: Dennis
 */
 
public class CoShapePageItemViewPane extends CoViewPanel
{
	protected Point2D m_fixedViewPosition;
	protected Dimension2D m_fixedViewSize;

	protected boolean m_centerX;
	protected boolean m_centerY;
public CoShapePageItemViewPane( Point2D position,
	                              Dimension2D size,
	                              CoShapePageItemView view,
	                              double scale,
	                              boolean centerX,
	                              boolean centerY )
{
	super( (CoView) null, scale );

	m_renderingHints.put( CoPageItemViewRenderer.PAINT_COLUMN_GRID, CoPageItemViewRenderer.PAINT_COLUMN_GRID_ON );
	m_renderingHints.put( CoPageItemViewRenderer.PAINT_OUTLINE, CoPageItemViewRenderer.PAINT_OUTLINE_OFF );
	m_renderingHints.put( CoPageItemViewRenderer.PAINT_MODEL_OUTLINE, CoPageItemViewRenderer.PAINT_MODEL_OUTLINE_ON );
	m_renderingHints.put( CoPageItemViewRenderer.PAINT_OUTLINE_STYLE, CoPageItemViewRenderer.PAINT_OUTLINE_SOLID );
	m_renderingHints.put( CoTextRenderingHints.PAINT_DUMMY_TEXT, CoTextRenderingHints.PAINT_DUMMY_TEXT_ON );
	
	m_fixedViewPosition = position;
	m_fixedViewSize = size;
	
	m_centerX = centerX;
	m_centerY = centerY;
	
	setView( view );
}
public CoShapePageItemViewPane( Point2D position, Dimension2D size, CoShapePageItemView view, boolean centerX, boolean centerY )
{
	this( position, size, view, 1, centerX, centerY );
}
public CoShapePageItemViewPane( Point2D position, Dimension2D size, boolean centerX, boolean centerY )
{
	this( position, size, null, 1, centerX, centerY );
}
protected double getPreferredViewHeight() 
{
	return ( m_fixedViewSize == null ) ? super.getPreferredViewHeight() : m_fixedViewSize.getHeight();
}
protected double getPreferredViewWidth() 
{
	return ( m_fixedViewSize == null ) ? super.getPreferredViewWidth() : m_fixedViewSize.getWidth();
}

public void setView( CoShapePageItemView view )
{
	super.setView( wrappit( view ) );

	setToolTipText( ( view == null ) ? null : view.getName() );
}
public void setView( CoView view )
{
	setView( (CoShapePageItemView) view );
}
protected CoView wrappit( CoShapePageItemView view )
{
	if ( view == null ) return null;

	CoFixedPositionAdapter a = null;
	
	if
		( m_fixedViewSize != null )
	{
		a = new CoFixedBoundsAdapter( this, m_fixedViewPosition, m_fixedViewSize, m_centerX, m_centerY, useOutsideStrokeWidth() );
	} else {
		a = new CoFixedPositionAdapter( this, m_fixedViewPosition, useOutsideStrokeWidth() );
	}

	view.setAdapter( a );
	return view;	
}

public Dimension2D getFixedViewSize()
{
	return m_fixedViewSize;
}

private Container getViewContainer()
{
	return this;
}

protected double getViewsHeight() 
{
	if
		( useOutsideStrokeWidth() )
	{
		double H = 0;
		
		int I = m_views.size();
		for
			( int i = 0; i < I; i++ )
		{
			CoShapePageItemView v = (CoShapePageItemView) m_views.get( i );
			H = Math.max( H, v.getHeight() + v.getStrokeProperties().getOutsideWidth() * 2 );
		}

		return H;
	} else {
		return super.getViewsHeight();
	}
}

protected double getViewsWidth() 
{
	if
		( useOutsideStrokeWidth() )
	{
		double W = 0;
		
		int I = m_views.size();
		for
			( int i = 0; i < I; i++ )
		{
			CoShapePageItemView v = (CoShapePageItemView) m_views.get( i );
			W = Math.max( W, v.getWidth() + v.getStrokeProperties().getOutsideWidth() * 2 );
		}

		return W;
	} else {
		return super.getViewsWidth();
	}
}

public void setViews( List views ) // [ CoShapePageItemView ]
{
	List l = new ArrayList();

	int I = views.size();
	for
		( int i = 0; i < I; i++ )
	{
		l.add( wrappit( (CoShapePageItemView) views.get( i ) ) );
	}

	super.setViews( l );
}

protected void updateScale( int w, int h )
{
	double W = ( m_fixedViewSize != null ) ? m_fixedViewSize.getWidth() : getViewsWidth();
	double H = ( m_fixedViewSize != null ) ? m_fixedViewSize.getHeight() : getViewsHeight();
	
	if
		( W > 0 && H > 0 )
	{
		Insets i = getInsets();

		w -= i.left + i.right + 2;
		h -= i.top + i.bottom + 2;

		if ( w < 0 ) w = 0;
		if ( h < 0 ) h = 0;
	
		m_scale = Math.min( w / W, h / H );
	}
}

protected boolean useOutsideStrokeWidth()
{
	return true;
}
}