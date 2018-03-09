package com.bluebrim.text.impl.client.test;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.impl.shared.*;

/**
 * 
 */
class CoSinusColumn extends CoAbstractColumn
{
/**
 *
 */
public CoSinusColumn( CoRectangle2DFloat bounds )
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

	double s = Math.sin( y1 / m_bounds.width * 4 ) * m_bounds.width / 4;
	float x0 = (float) ( s + m_bounds.width / 4 );
	float x1 = x0 + m_bounds.width / 2;
	
	if ( x0 < range[ X ] ) x0 = range[ X ];
	if ( x1 > range[ X ] + range[ WIDTH ] ) x1 = range[ X ] + range[ WIDTH ];
	
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
	
	double s = Math.sin( y / m_bounds.width * 4 ) * m_bounds.width / 4;
	float x0 = (float) ( s + m_bounds.width / 4 );
	float x1 = x0 + m_bounds.width / 2;
	
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
	return true;
}
public boolean isRectangular()
{
	return false;
}
}
