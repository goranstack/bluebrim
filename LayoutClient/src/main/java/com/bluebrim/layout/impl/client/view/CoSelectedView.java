package com.bluebrim.layout.impl.client.view;

import java.awt.*;
import java.awt.geom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * A pair consisting of a shape page item view and a copy of its shape.
 * Instances of this class are used to manage selected views in a layout manager.
 *
 * Also see CoViewSelectionManager
 * 
 * @author: Dennis Malmström
*/

public class CoSelectedView implements CoPageItemView.Listener
{
	// handle view geometry
	public static final int HANDLE_SIZE = 5;
	public static final int HANDLE_RANGE = 5;


	public static final int HANDLE_OFFSET = - ( HANDLE_SIZE ) / 2;

	// the pair
	private CoShapePageItemView m_view;
	private CoShapeIF m_shape;

	// rendering cache
	private CoShapeIF m_outlineShape;
	private Stroke m_outlineStroke;
	private float m_scale;

	private static Rectangle2D m_rect = new Rectangle2D.Double(); // shape for painting handles



	private Renderer m_renderer = HANDLES_AND_OUTLINE_RENDERER;


	
	public interface Renderer
	{
		void paint( CoSelectedView v, CoPaintable g );
	};

	public static final Renderer HANDLES_AND_OUTLINE_RENDERER =
		new Renderer()
		{
			public void paint( CoSelectedView v, CoPaintable g )
			{
				paintHandlesAndOutline( v, g );
			}
		};

	public static class FillRenderer implements Renderer
	{
		private Color m_color;

		public FillRenderer( Color c )
		{
			m_color = c;
		}
		
		public void paint( CoSelectedView v, CoPaintable g )
		{
			fillShape( m_color, v, g );
		}
	};
public CoSelectedView( CoShapePageItemView view, Renderer r )
{
	m_renderer = r;
	setView( view );
}
public void destroy()
{
	setView( null );
}
public boolean doesOwnHandle( CoReshapeHandleIF h )
{
	CoReshapeHandleIF[] hs = m_shape.getReshapeHandles();
	for
		( int i = 0; i < hs.length; i++ )
	{
		if ( hs[ i ] == h ) return true;
	}

	return false;
}
// compare the views

public boolean equals( Object o )
{
	if
		( o instanceof CoSelectedView )
	{
		return m_view == ( (CoSelectedView) o ).m_view;
	} else if
		( o instanceof CoPageItemView )
	{
		return m_view == o;
	} else {
		return false;
	}
}

public CoShapeIF getShape()
{
	return m_shape;
}
public CoShapePageItemView getView()
{
	return m_view;
}
public void modelChanged()
{
	sync();
}


public void setView( CoShapePageItemView view )
{
	if
		( m_view != null )
	{
		m_view.removeViewListener( this );
	}
	
	m_view = view;
	sync();

	if
		( m_view != null )
	{
		m_view.addViewListener( this );
	}
}
private void sync()
{
	m_shape = ( m_view == null ) ? null : m_view.getCoShape().deepClone();
	m_outlineShape = null;
}

	public static final int MOVE_HANDLE_OFFSET = 10;

private static void fillShape( Color c, CoSelectedView v, CoPaintable p )
{
	// ugly, but it will have to do for now
	Graphics2D g = p.getGraphics2D();
	if ( g == null ) return;
	
	if ( v.m_shape == null ) return;

	AffineTransform t = g.getTransform();

	g.transform( v.m_view.getAffineTransform() );

	g.setColor( c );
	g.fill( v.m_shape.getShape() );

	g.setTransform( t );
}

public void paint( CoPaintable g )
{
	if ( m_renderer != null ) m_renderer.paint( this, g );
}

private static void paintHandlesAndOutline( CoSelectedView v, CoPaintable p )
{
	// ugly, but it will have to do for now
	Graphics2D g = p.getGraphics2D();
	if ( g == null ) return;
	
	if ( v.m_shape == null ) return;
	
	g.setColor( Color.black );

	AffineTransform t = g.getTransform();

	Stroke stroke = g.getStroke();
	float scale = (float) com.bluebrim.base.shared.CoBaseUtilities.getXScale( t );
	float k = 1 / scale;

	AffineTransform a = v.m_view.getAffineTransform();
	g.transform( a );

	if
		( ( v.m_outlineShape == null ) || ( scale != v.m_scale ) )
	{
		v.m_outlineStroke = new BasicStroke( k );
		v.m_outlineShape = v.m_shape.isClosedShape() ? v.m_shape.createExpandedInstance( k / 2 ) : v.m_shape.deepClone();
		v.m_outlineShape.setEditMode( true );
		v.m_scale = scale;
	}
	
	g.setStroke( v.m_outlineStroke );
	g.draw( v.m_outlineShape.getShape() );
	
	g.setStroke( stroke );


	
	CoReshapeHandleIF[] hs = v.m_shape.getReshapeHandles();

	float S = (float) ( HANDLE_SIZE * k );
	float s = (float) ( ( HANDLE_SIZE - 2 ) * k );
	
	for
		( int i = 0; i < hs.length; i++ )
	{
		m_rect.setRect( hs[ i ].getX() - S / 2,     hs[ i ].getY() - S / 2,     S, S );
		g.setColor( Color.black );
		g.fill( m_rect );

		m_rect.setRect( hs[ i ].getX() - S / 2 + k, hs[ i ].getY() - S / 2 + k, s, s );
		g.setColor( Color.white );
		g.fill( m_rect );
	}
	
	float ox = (float) ( - MOVE_HANDLE_OFFSET * k ) - S / 2;
	float oy = (float) - S / 2;

	m_rect.setRect( v.m_shape.getX() + ox,     v.m_shape.getY() + oy,     S, S );
	g.setColor( Color.black );
	g.fill( m_rect );

	m_rect.setRect( v.m_shape.getX() + ox + k, v.m_shape.getY() + oy + k, s, s );
	g.setColor( Color.white );
	g.fill( m_rect );

	g.setTransform( t );

}

public void repaint()
{
	m_view.repaintFrame( HANDLE_SIZE + 2 );
}

public void viewChanged( CoPageItemView.Event ev )//CoPageItemView view, boolean boundsChanged, int structuralChange, CoShapePageItemView child, int index )
{
	modelChanged();
}
}