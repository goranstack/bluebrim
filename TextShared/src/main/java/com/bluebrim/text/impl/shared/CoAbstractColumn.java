package com.bluebrim.text.impl.shared;

import com.bluebrim.base.shared.*;

/**
 * Abstract superclass for column grid geometry columns.
 *
 * @author: Dennis Malmström
 */
 
public abstract class CoAbstractColumn implements com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF
{
	protected CoRectangle2DFloat m_bounds;

	// margin positions
	protected float m_x0min;
	protected float m_x1max;
/**
 *
 */
public CoAbstractColumn( CoRectangle2DFloat bounds )
{
	m_bounds = bounds;

	setMargins( 0, 0 );
}
public CoRectangle2DFloat getBounds()
{
	return m_bounds;
}
public void setMargins(float leftMargin, float rightMargin)
{
	m_x0min = leftMargin;
	m_x1max = m_bounds.width - rightMargin;
}
}
