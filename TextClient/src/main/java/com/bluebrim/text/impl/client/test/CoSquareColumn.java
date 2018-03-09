package com.bluebrim.text.impl.client.test;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.impl.shared.*;

/**
 * 
 */
class CoSquareColumn extends CoAbstractColumn
{
	protected float m_d;
/**
 *
 */
public CoSquareColumn( CoRectangle2DFloat bounds, float d )
{
	super( bounds );
	
	m_d = d;
}
public void getMinimalRange( float y0, float y1, float[] range )
{
	if
		( y1 > m_bounds.height )
	{
		return;
	}

	if
		( y1 > m_d )
	{
		range[ WIDTH ] = 0;
		return;
	}
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
	
	float x0 = ( m_bounds.width - m_d ) / 2;
	float x1 = m_bounds.width - x0;

	if
		( y > m_d )
	{
		x1 = x0;
	}

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
	return ( y <= m_d );
}
public boolean isRectangular()
{
	return false;
}
}
