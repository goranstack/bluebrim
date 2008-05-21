package com.bluebrim.text.impl.client.test;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.impl.shared.*;

/**
 * 
 */
class CoHourGlassColumn extends CoAbstractColumn
{
/**
 *
 */
public CoHourGlassColumn( CoRectangle2DFloat bounds )
{
	super( bounds );
}
public void getMinimalRange( float y0, float y1, float[] range )
{
	if
		( y1 > m_bounds.height )
	{
		return;
	}

	if
		( ( y0 < m_bounds.width / 2 ) && ( y1 > m_bounds.width / 2 ) )
	{
		range[ WIDTH ] = 0;
		return;
	}
		
	float x0 = Math.min( y1, m_bounds.width - y1 );
	float x1 = Math.max( y1, m_bounds.width - y1 );

	x0 = Math.max( x0, range[ 0 ] );
	x1 = Math.min( x1, range[ 0 ] + range[ 1 ] );

	if ( x0 < m_x0min ) x0 = m_x0min;
	if ( x1 > m_x1max ) x1 = m_x1max;
	
	range[ X ] = x0;
	range[ WIDTH ] = x1 - x0;
}
public boolean getRange( float y, int index, float[] range )
{
	if
		( y > m_bounds.height )
	{
		range[ X ] = 0;
		range[ WIDTH ] = Float.MAX_VALUE;
		return false;
	}
	
	float x0 = Math.min( y, m_bounds.width - y );
	float x1 = Math.max( y, m_bounds.width - y );

	if ( x0 < m_x0min ) x0 = m_x0min;
	if ( x1 > m_x1max ) x1 = m_x1max;
	
	range[ X ] = x0;
	range[ WIDTH ] = x1 - x0;
	
	return false;
}
public boolean isEquivalentTo( com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF c )
{
	return false;
}
public boolean isNarrowing(float y)
{
	return y <= m_bounds.width / 2;
}
public boolean isRectangular()
{
	return false;
}
}
