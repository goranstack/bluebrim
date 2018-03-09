package com.bluebrim.layout.impl.client.tools;

import java.awt.*;
import java.awt.geom.*;

import com.bluebrim.layout.impl.client.view.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Object for maintaining the target of a tool operation in progress
 * 
 * @author: Dennis Malmström
 */

public class CoTargetViewKeeper
{
	protected CoAbstractTool m_owner; // owning tool
	
	protected CoCompositePageItemView m_targetView; // target

	protected static final float m_strokeWidth = 5; // target indicator stroke width

	// drawing cache
	protected Stroke m_cachedStroke;
	protected double m_cachedScale;
public CoTargetViewKeeper( CoAbstractTool owner )
{
	m_owner = owner;
}

protected void drawShapeOfTarget( Graphics2D g )
{
	if
		(
			( m_targetView != null )
		&&
			( ! ( m_targetView instanceof CoRootView ) )
		)
				
	{
		AffineTransform t = g.getTransform();
		g.transform( m_targetView.getAffineTransform() );

		g.draw( m_targetView.getEffectiveCoShape().getShape() );

		g.setTransform( t );
	}
}



// hook for validating target, return null if v is not a valid target

protected CoCompositePageItemView checkValidTargetView( CoCompositePageItemView v )
{
	return v;
}

public CoCompositePageItemView getTargetView()
{
	return m_targetView;
}

public void setTargetView( CoCompositePageItemView v )
{
	v = checkValidTargetView( v );
	
	if
		( v != m_targetView )
	{
		Graphics2D g = m_owner.getXORGraphics();

		double scale = com.bluebrim.base.shared.CoBaseUtilities.getXScale( g.getTransform() );
		if
			(
				( m_cachedStroke == null )
			||
				( m_cachedScale != scale )
			)
		{
			m_cachedScale = scale;
			m_cachedStroke = new BasicStroke( (float) ( m_strokeWidth / m_cachedScale ) );
		}
		
		Stroke s = g.getStroke();
		g.setStroke( m_cachedStroke );
		
		drawShapeOfTarget( g );
		m_targetView = v;
		drawShapeOfTarget( g );

		g.setStroke( s );
	}
}
}