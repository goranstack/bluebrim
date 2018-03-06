package com.bluebrim.text.shared;
import com.bluebrim.base.shared.*;
import com.bluebrim.text.impl.shared.*;

/**
 * Rectangular column grid geometry columns.
 *
 * @author: Dennis Malmström
 */
 
public class CoRectangularColumn extends CoAbstractColumn
{
	// rectangle side positions
	protected float m_x0;
	protected float m_x1;

	// span cache
	protected float m_x;
	protected float m_w;
public CoRectangularColumn( float x, float y, float width, float height )
{
	this( x, y, width, height, 0, width );
}
/**
 *
 */
public CoRectangularColumn( float x, float y, float width, float height, float x0, float x1 )
{
	this( new CoRectangle2DFloat( x, y, width, height ), x0, x1 );
}
public CoRectangularColumn( CoRectangle2DFloat bounds )
{
	this( bounds, 0, bounds.width );
}
public CoRectangularColumn( CoRectangle2DFloat bounds, float x0, float x1 )
{
	super( bounds );

	m_x0 = x0;
	m_x1 = x1;

	setMargins( 0, 0 );
}
public CoRectangle2DFloat getBounds()
{
	return m_bounds;
}
public void getMinimalRange( float y0, float y1, float[] range )
{
	return;
}
public boolean getRange( float y, int index, float[] range )
{
	range[ X ] = ( y > m_bounds.height ) ? 0 : m_x;
	range[ WIDTH ] = ( y > m_bounds.height ) ? Float.MAX_VALUE : m_w;
	return false;
}
public boolean isEquivalentTo( com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF c )
{
	if ( ! c.isRectangular() ) return false;

	return getBounds().equals( c.getBounds() );
}
public boolean isNarrowing(float y)
{
	return false;
}
public boolean isRectangular()
{
	return true;
}
public void setMargins(float leftMargin, float rightMargin)
{
	m_x = Math.max( m_x0, leftMargin );

	m_w = Math.min( m_x1, m_bounds.width - rightMargin - leftMargin );
	m_w = Math.max( m_w, 1 );
}
}
