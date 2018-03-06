package com.bluebrim.base.shared;

import java.awt.geom.*;

/**
 * Interface for a serializable and mutable shape defined by a sequence of control points
 * 
 * @author: Dennis Malmström
 */

public interface CoCurveShapeIF extends CoBoundingShapeIF, CoImmutableCurveShapeIF
{
	public final static String DO_RESHAPE_POLYGON = "DO_RESHAPE_POLYGON";
// add control point (x,y) at end of control point sequence

Point2D addPoint( double x, double y );

// remove control point (x,y)

void removePoint( double x, double y );
// reverse control point order

void reorderPoints();
void setClosed( boolean b );

// insert a copy of control point at index i at index i (or at end of sequence if i out of bounds)

Point2D insertPoint( int index );
}