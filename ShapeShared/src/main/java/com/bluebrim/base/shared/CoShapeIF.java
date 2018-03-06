package com.bluebrim.base.shared;


/**
 * Interface for a serializable and mutable geometrical shape
 * 
 * @author: Dennis Malmström
 */

public interface CoShapeIF extends CoImmutableShapeIF
{
	// interface that is notified when a mutable proxy is changed
	public interface Owner
	{
		// changed properties mask
		int X = 1;
		int Y = 2;
		int WIDTH = 4;
		int HEIGHT = 8;
	
		void shapeChanged( int properties );
	}
// create a "mutable proxy" for this shape

public CoShapeIF getMutableProxy( CoShapeIF.Owner owner );
// predicate indicating if this shape is always closed

boolean isClosedShape();
// assure that the shape has positive width and height

void normalize();
// implemented by shapes where the apperence changes when it is edited (curves whith control points)

void setEditMode( boolean b );
public abstract void setHeight (double height );
// keep shapes aspect (width/height) ratio when reshaping by handles

void setKeepAspectRatio( boolean b );
public abstract void setSize( double width, double height );
public void setTranslation (double x, double y );
public abstract void setWidth (double width );
public abstract void setX (double x );
public abstract void setY (double y );
public void translateBy (double x, double y );
}
