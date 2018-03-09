package com.bluebrim.base.shared;

import java.awt.geom.*;

/**
 * Interface for a serializable and immutable shape defined by a sequence of control points
 * 
 * @author: Dennis Malmström
 */

public interface CoImmutableCurveShapeIF extends CoImmutableShapeIF
{
// get index of control point at (x,y), -1 if no such point present

int getIndexOfPoint( double x, double y );
Point2D getPoint( int i );
int getPointCount();
boolean isClosed();
}
