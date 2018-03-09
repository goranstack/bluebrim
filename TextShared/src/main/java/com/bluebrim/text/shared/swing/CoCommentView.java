package com.bluebrim.text.shared.swing;

import java.awt.*;

import javax.swing.text.*;

/**
 * Text view for comments in text.
 * 
 * @author: Dennis Malmström
 */

public class CoCommentView extends View
{
	private static final int m_height = 5;

	private static final int[] m_x = new int[] { 0, 0, 5 };
	private static final int[] m_y = new int[] { 0, 5, 5 };

	private Rectangle m_lastDrawnPosition;
public CoCommentView( Element elem )
{
	super(elem);
}
public boolean contains( int x, int y )
{
	if ( m_lastDrawnPosition == null ) return false;
	
	return
		( x >= m_lastDrawnPosition.x ) &&
		( y >= m_lastDrawnPosition.y ) &&
		( x <= m_lastDrawnPosition.x + 5 ) &&
		( y <= m_lastDrawnPosition.y + m_lastDrawnPosition.height );
}
public float getAlignment( int axis )
{
	switch
		( axis )
	{
		case View.Y_AXIS: 
			return 0.5f;
			
		default:
			return super.getAlignment( axis );
	}
}
public float getMaximumSpan( int axis )
{
	return getPreferredSpan( axis );
}
/**
 * 
 */
 
public float getMinimumSpan( int axis )
{
	return getPreferredSpan( axis );
}
/**
 * 
 */
 
public float getPreferredSpan( int axis )
{
	switch
		( axis )
	{
		case View.X_AXIS :
			return 0;
			
		case View.Y_AXIS :
			return m_height;
			
		default :
			throw new IllegalArgumentException("Invalid axis: " + axis);
	}
}
/**
 * 
 */
 
public Shape modelToView( int pos, Shape a, Position.Bias b ) throws BadLocationException
{
	int p0 = getStartOffset();
	int p1 = getEndOffset();
	if
		( ( pos >= p0 ) && ( pos <= p1 ) )
	{
		Rectangle r = a.getBounds();
		if
			( pos == p1 )
		{
			r.x += r.width;
		}
		r.width = 0;
		return r;
	}
	return null;
}
/**
 * 
 */
 
public void paint( Graphics g, Shape a )
{
	paint( (Graphics2D) g, a );
}
/**
 * 
 */
 
public void paint( Graphics2D g, Shape a )
{
	m_lastDrawnPosition = a.getBounds();

	int dx = m_lastDrawnPosition.x;
	int dy = m_lastDrawnPosition.y;
	
	g.setColor( Color.red );

	g.translate( dx, dy );
	g.fillPolygon( m_x, m_y, 3 );
	g.translate( - dx, - dy );

	m_lastDrawnPosition.x *= com.bluebrim.base.shared.CoBaseUtilities.getXScale( g.getTransform() );
	m_lastDrawnPosition.y *= com.bluebrim.base.shared.CoBaseUtilities.getYScale( g.getTransform() );
}
/**
 * 
 */
 
public void setSize( float width, float height )
{
}
/**
 * 
 */
 
public int viewToModel( float x, float y, Shape a, Position.Bias[] bias )
{
	Rectangle alloc = (Rectangle) a;
	if
		( x < alloc.x + ( alloc.width / 2 ) )
	{
		bias[0] = Position.Bias.Forward;
		return getStartOffset();
	}
	bias[0] = Position.Bias.Backward;
	return getEndOffset();
}
}