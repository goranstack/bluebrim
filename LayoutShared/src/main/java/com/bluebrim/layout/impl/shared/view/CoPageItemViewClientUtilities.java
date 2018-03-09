package com.bluebrim.layout.impl.shared.view;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.resource.shared.*;

/**
 * 
 * @author Dennis Malmström
 */
public abstract class CoPageItemViewClientUtilities
{

	static final int m_iconHeight = 18;
	static final int m_iconWidth = 20;
	static final int m_iconX = 2;
	static final int m_iconY = 2;
	static final Font m_iconFont = new Font( "monospaced", Font.PLAIN, m_iconHeight * 2 / 3 );
	static final Map m_iconShapes = createIconShapes();



protected static Map createIconShapes()
{
	Map m = new HashMap();
	
	float x = m_iconX;
	float y = m_iconY;
	float w = m_iconWidth - m_iconX * 2;
	float h = m_iconHeight - m_iconY * 2;

	
	m.put( CoRectangleShapeIF.RECTANGLE_SHAPE, new Rectangle( (int) x, (int) y, (int) w, (int) h ) );
	
	m.put( CoEllipseShapeIF.ELLIPSE_SHAPE, new Ellipse2D.Float( x, y, w, h ) );

	float r = Math.min( (float) m_iconWidth / 3, (float) m_iconHeight / 3 );
	m.put( CoRoundedCornerIF.ROUNDED_CORNER, new RoundRectangle2D.Float( x, y, w, h, r, r ) );
	
	m.put( CoLineIF.LINE, new Line2D.Float( x, y, w, h ) );

	{
		GeneralPath p = new GeneralPath();
		float d = Math.min( (float) m_iconWidth / 4, (float) m_iconHeight / 4 );
		p.moveTo( x + d,      y );
		p.lineTo( x + w - d, y );
		p.lineTo( x + w,     y + d );
		p.lineTo( x + w,     y + h - d );
		p.lineTo( x + w - d, y + h );
		p.lineTo( x + d,      y + h );
		p.lineTo( x,          y + h - d );
		p.lineTo( x,          y + d );
		p.closePath();
		m.put( CoBeveledCornerIF.BEVELED_CORNER, p );
	}

	{
		GeneralPath p = new GeneralPath();
		float d = Math.min( (float) m_iconWidth / 4, (float) m_iconHeight / 4 ) - 1;
		float k = (float) ( d * 6 / 7 );
		p.moveTo( x + d,      y ); 
		p.lineTo( x + w - d,  y );
		p.quadTo( x + w - k,  y + k,        x + w,      y + d );
		p.lineTo( x + w,      y + h - d );
		p.quadTo( x + w - k,  y + h - k,    x + w - d,  y + h );
		p.lineTo( x + d,      y + h );
		p.quadTo( x + k,      y + h - k,    x,          y + h - d );
		p.lineTo( x,          y + d );
		p.quadTo( x + k,      y + k,        x + d,      y );
		p.closePath();
		m.put( CoConcaveCornerIF.CONCAVE_CORNER, p );
	}

	{
		GeneralPath p = new GeneralPath();
		p.moveTo( x + w / 2, y ); 
		p.lineTo( x + w / 2, y + h );
		p.moveTo( x,         y + h / 2 );
		p.lineTo( x + w,     y + h / 2 );
		m.put( CoLineIF.ORTHOGONAL_LINE, p );
	}

	{
		GeneralPath p = new GeneralPath();
		p.moveTo( x,     y );
		p.lineTo( x + w, y );
		p.moveTo( x,     y + h / 2 );
		p.lineTo( x + w, y + h / 2 );
		p.moveTo( x,     y + h );
		p.lineTo( x + w, y + h );
		m.put( CoBoxedLineShapeIF.BOXED_LINE, p );
	}

	{
		GeneralPath p = new GeneralPath();
		p.moveTo( x + w / 2, y );
		p.lineTo( x, y );
		p.lineTo( x, y + h );
		p.lineTo( x + w / 2, y + h );
		p.lineTo( x + w, y + h / 2 );
		m.put( CoPolygonShapeIF.POLYGON_SHAPE, p );
	}

	{
		GeneralPath p = new GeneralPath();
		/*
		p.moveTo( x + w * 0.039892915, y + h * 0.2031459 );
		p.curveTo( x + w * 0.1296021,   y + h * 0.072555275,  x + w * 0.179632225,  y + h * 0.033131683, x + w * 0.25208965,  y + h * 0.010955914 );
		p.curveTo( x + w * 0.32454705,  y + h * -0.011219856, x + w * 0.4413372,    y + h * 0.225321675, x + w * 0.5263927,   y + h * 0.225321675 );
		p.curveTo( x + w * 0.6207556,   y + h * 0.225321675,  x + w * 0.69200965,   y + h * 0.04791553,  x + w * 0.7903447,   y + h * 0.010955914 );
		p.curveTo( x + w * 0.8886798,   y + h * -0.026003704, x + w * 0.98183935,   y + h * 0.02573976,  x + w * 0.99736595,  y + h * 0.240105535 );
		p.curveTo( x + w * 1.0128925,   y + h * 0.4544713,    x + w * 0.9611372,    y + h * 0.8240675,   x + w * 0.8058713,   y + h * 0.957122 );
		p.curveTo( x + w * 0.65060545,  y + h * 1.0901767,    x + w * 0.3504247,    y + h * 0.8758109,   x + w * 0.277967285, y + h * 0.8240675 );
		p.curveTo( x + w * 0.205509875, y + h * 0.772324,     x + w * 0.1071748,    y + h * 0.66144515,  x + w * 0.07094609,  y + h * 0.60230975 );
		p.curveTo( x + w * 0.034717387, y + h * 0.54317435,   x + w * -0.049816265, y + h * 0.333736565, x + w * 0.039892915, y + h * 0.2031459 );
		*/
		p.moveTo( x + w * (float) 0.039892915, y + h * (float) 0.2031459 );
		p.curveTo( x + w * (float) 0.1296021,   y + h * (float) 0.072555275,  x + w * (float) 0.179632225,  y + h * (float) 0.033131683, x + w * (float) 0.25208965,  y + h * (float) 0.010955914 );
		p.curveTo( x + w * (float) 0.32454705,  y + h * (float) -0.011219856, x + w * (float) 0.4413372,    y + h * (float) 0.225321675, x + w * (float) 0.5263927,   y + h * (float) 0.225321675 );
		p.curveTo( x + w * (float) 0.6207556,   y + h * (float) 0.225321675,  x + w * (float) 0.69200965,   y + h * (float) 0.04791553,  x + w * (float) 0.7903447,   y + h * (float) 0.010955914 );
		p.curveTo( x + w * (float) 0.8886798,   y + h * (float) -0.026003704, x + w * (float) 0.98183935,   y + h * (float) 0.02573976,  x + w * (float) 0.99736595,  y + h * (float) 0.240105535 );
		p.curveTo( x + w * (float) 1.0128925,   y + h * (float) 0.4544713,    x + w * (float) 0.9611372,    y + h * (float) 0.8240675,   x + w * (float) 0.8058713,   y + h * (float) 0.957122 );
		p.curveTo( x + w * (float) 0.65060545,  y + h * (float) 1.0901767,    x + w * (float) 0.3504247,    y + h * (float) 0.8758109,   x + w * (float) 0.277967285, y + h * (float) 0.8240675 );
		p.curveTo( x + w * (float) 0.205509875, y + h * (float) 0.772324,     x + w * (float) 0.1071748,    y + h * (float) 0.66144515,  x + w * (float) 0.07094609,  y + h * (float) 0.60230975 );
		p.curveTo( x + w * (float) 0.034717387, y + h * (float) 0.54317435,   x + w * (float) -0.049816265, y + h * (float) 0.333736565, x + w * (float) 0.039892915, y + h * (float) 0.2031459 );
		p.closePath();
		m.put( CoCubicBezierCurveShapeIF.CUBIC_BEZIER_CURVE, p );
	}

	return m;
}



	static Cursor LOCK_CURSOR = null;
	static final CoCompositePageItemIF.ViewState m_compositePageItemViewState = new CoCompositePageItemIF.ViewState();
	static final Color m_topicLayoutAreaIconBackgroundColor = new Color( 255, 255, 200 );
	static final CoContentWrapperPageItemIF.ViewState m_contentWrapperPageItemViewState = new CoContentWrapperPageItemIF.ViewState();
	static final CoPageItemView.Event m_event = new CoPageItemView.Event();
	static final Shape m_layoutAraeIconShape = createLayoutAreaIconShape();
	static final CoPageItemAbstractTextContentIF.ViewState m_pageItemAbstractTextContentViewState = new CoPageItemAbstractTextContentIF.ViewState();
	static final CoPageItemIF.ViewState m_pageItemContentViewState = new CoPageItemIF.ViewState();
	static final CoPageItemLayoutContentIF.ViewState m_pageItemLayoutContentViewState = new CoPageItemLayoutContentIF.ViewState();
	static final CoPageItemIF.ViewState m_pageItemViewState = new CoPageItemIF.ViewState();
	static final Color m_pageLayerLayoutAreaIconGridColor = new Color( 100, 100, 255 );
	static final Shape m_pageLayerLayoutAreaIconShape = createPageLayerLayoutAreaIconShape();
	static
	{
		Icon icon  = CoResourceLoader.loadIcon( CoPageItemAbstractTextContentView.class, "lock.gif" );
		if
			( icon instanceof ImageIcon )
		{
			LOCK_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor( ((ImageIcon)icon).getImage(), new Point( 15, 15 ), "" );
		}
	}

private static Shape createLayoutAreaIconShape()
{
	int h = CoPageItemViewClientUtilities.m_iconHeight - 2 * CoPageItemViewClientUtilities.m_iconY;
	int w = CoPageItemViewClientUtilities.m_iconWidth - 2 * CoPageItemViewClientUtilities.m_iconX;
	int x = CoPageItemViewClientUtilities.m_iconX;
	int y = CoPageItemViewClientUtilities.m_iconY;

	java.awt.geom.GeneralPath p = new java.awt.geom.GeneralPath();
	
	p.moveTo( x, y );
	p.lineTo( x + w / 2, y + h / 2 );
	p.lineTo( x, y + h / 2 );
	p.lineTo( x + w / 2, y );
	p.lineTo( x + w / 2, y + h / 2 );

	int dy = 2;
	while
		( dy < w / 2 )
	{
		p.moveTo( x + w / 2, y + dy );
		p.lineTo( x + w, y + dy );
		dy += 2;
	}
	while
		( dy < w )
	{
		p.moveTo( x, y + dy );
		p.lineTo( x + w, y + dy );
		dy += 2;
	}

	return p;
}

private static Shape createPageLayerLayoutAreaIconShape()
{
	int h = CoPageItemViewClientUtilities.m_iconHeight - 2 * CoPageItemViewClientUtilities.m_iconY + 1;
	int w = (int) ( h / 1.4f );
	int x = ( CoPageItemViewClientUtilities.m_iconWidth - w + 1 ) / 2;
	int y = CoPageItemViewClientUtilities.m_iconY;

	java.awt.geom.GeneralPath p = new java.awt.geom.GeneralPath();
	
	p.moveTo( x,     y );
	p.lineTo( x + w, y );
	p.lineTo( x + w, y + h);
	p.lineTo( x,     y + h );
	p.lineTo( x,     y );
	
	p.moveTo( x,     y + 2 );
	p.lineTo( x + w, y + 2 );
	
	p.moveTo( x,     y + h - 2 );
	p.lineTo( x + w, y + h - 2 );

	float dx = 2;
	while
		( dx < w )
	{
		p.moveTo( x + dx, y + 2 );
		p.lineTo( x + dx, y + h - 2 );
		dx += 2;
	}

	return p;
}
}