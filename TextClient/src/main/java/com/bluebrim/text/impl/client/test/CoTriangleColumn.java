package com.bluebrim.text.impl.client.test;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.impl.shared.*;

/**
 * 
 */
class CoTriangleColumn extends CoAbstractColumn
{
/**
 *
 */
public CoTriangleColumn( CoRectangle2DFloat bounds )
{
	super( bounds );
}
public void getMinimalRange( float y0, float y1, float[] range )
{
	return;
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
	
	float x0 = m_bounds.width - y;
	float x1 = m_bounds.width;

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
	return false;
}
public boolean isRectangular()
{
	return false;
}
}
