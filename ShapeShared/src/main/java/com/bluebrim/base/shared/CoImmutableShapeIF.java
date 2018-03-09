package com.bluebrim.base.shared;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;

import com.bluebrim.xml.shared.*;

/**
 * Interface for a serializable and immutable geometrical shape
 * 
 * @author: Dennis Malmström
 */

public interface CoImmutableShapeIF extends Serializable, CoFactoryElementIF, CoXmlEnabledIF
{
	public static String SHAPE = "shape";
double calculateArea();
public CoShapeIF copyTranslatedBy( double x, double y ) ;
CoShapeIF createExpandedInstance( double delta );
public CoShapeIF createNewInstance();
// create an instance extracing the geometry from a given shape

CoShapeIF createNewInstanceFrom( CoImmutableShapeIF shape );
CoShapeIF deepClone();
public Rectangle2D getBounds2D();
public abstract double getHeight();
// a handle for moving the shape, see CoHandleIF

public CoHandleIF getMoveHandle();
public PathIterator getPathIterator(AffineTransform at);
public PathIterator getPathIterator(AffineTransform at, double flatness);
// handles for reshaping the shape, see CoReshapeHandleIF

public CoReshapeHandleIF[] getReshapeHandles();
// get the awt shape of this shape

Shape getShape();
public abstract double getWidth ( );
double getX ( );
double getY ( );
// Hit test using a Graphics2D
// Graphics2D hit testing can be slow
// Use this method only after the methods isInside and isOutside have failed.

public boolean hit( Graphics2D g, Rectangle r );
/**
 * Predicate used for hit testing the shape.
 * Returning false does not mean that the point is outside the shape.
 * It means that it is not certain that the point is inside the shape.
 * Since the method hit is available there is no point in making this method accurate if there is a performance penalty.
 * The point of this method is to quickly determine that the point is inside the shape or give up.
 *
 * The recomended algorithm for hit testing a shape is:
 * try isInside, if it is true then there is a hit
 * if not try isOutside, if it is true then there is no hit
 * if both these tests fail, use the method hit, it is allways accurate but can be slow
 */

boolean isInside( Point2D p, double strokeWidth, double outsideStrokeWidth );
// see method isInside

boolean isOutside( Point2D p, double strokeWidth, double outsideStrokeWidth );
}