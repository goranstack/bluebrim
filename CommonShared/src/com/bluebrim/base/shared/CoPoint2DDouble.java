package com.bluebrim.base.shared;

import java.awt.geom.*;
import java.io.*;

/**
 * Serializable version of Point2D.Double.
 * Creation date: (2000-06-07 13:49:43)
 * @author: Dennis
 */
 
public class CoPoint2DDouble extends Point2D.Double implements Serializable
{
public CoPoint2DDouble()
{
	super();
}
public CoPoint2DDouble( double x, double y )
{
	super( x, y );
}
public CoPoint2DDouble( Point2D p )
{
	this( p.getX(), p.getY() );
}
private void readObject( ObjectInputStream stream ) throws IOException, ClassNotFoundException
{

	x = stream.readDouble();
	y = stream.readDouble();
}
private void writeObject( ObjectOutputStream stream ) throws IOException
{
	stream.writeDouble( getX() );
	stream.writeDouble( getY() );
}
}
