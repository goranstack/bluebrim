package com.bluebrim.font.impl.client;

import com.bluebrim.base.shared.geom.*;
import com.bluebrim.font.impl.shared.metrics.*;
import com.bluebrim.shape.client.*;

/**
 * 
 * 
 * @author: Dennis
 * @author Magnus Ihse <magnus.ihse@appeal.se> (2001-05-04 11:02:46)
 */
 
public class CoTrackingCurvePanel extends CoPolygonEditor
{

public CoTrackingCurvePanel()
{
	super();

	setYSpan( 10 );
	setXSpan( 72 );
}
public com.bluebrim.font.shared.metrics.CoTrackingMetrics getTrackingCurve()
{
	CoPolygonShape shape = getPolygon();

	int I = shape.getPointCount();
	
	double x = shape.getX();
	double y = shape.getY();
	double w = shape.getWidth();
	double h = shape.getHeight();
	
	float X [] = new float [ I ];
	float Y [] = new float [ I ];

	boolean notZero = false;

	for
		( int i = 0; i < X.length; i++ )
	{
		X[ i ] = (float) (shape.getPoint( i ).getX() * w + x);
		Y[ i ] = (float) (shape.getPoint( i ).getY() * h + y);
		if ( Y[ i ] != 0 ) notZero = true;
	}

	if ( notZero ) {
		return new CoTrackingMetricsImplementation(X,Y);
	} else {
		return null;
	}	
}
public void setTrackingCurve( CoTrackingMetricsImplementation c )
{
	CoPolygonShape shape = new CoPolygonShape();

	float x [] = c.getFontSizeIndex();
	float y [] = c.getTrackingValues();

	if
		( x == null )
	{
			shape.addPoint( 0, 0 );
			shape.addPoint( 72, 0 );
	} else {
		for
			( int i = 0; i < x.length; i++ )
		{
			shape.addPoint( x[ i ], y[ i ] );
		}
	}

	setPolygon( shape );
}
}
