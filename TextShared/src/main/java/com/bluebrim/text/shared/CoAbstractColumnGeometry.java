package com.bluebrim.text.shared;

/**
 * Abstract superclass for column grid geometry.
 *
 * @author: Dennis Malmström
 */

public abstract class CoAbstractColumnGeometry implements com.bluebrim.text.shared.CoColumnGeometryIF
{
public boolean isEquivalentTo( com.bluebrim.text.shared.CoColumnGeometryIF g )
{
	int I = getColumnCount();
	int J = g.getColumnCount();
	if
		( I != J )
	{
		return false;
	}
	
	for
		( int i = 0; i < I; i++ )
	{
		if
			( ! getColumn( i ).isEquivalentTo( g.getColumn( i ) ) )
		{
			return false;
		}
	}
	
	return true;
}
public boolean isRectangular()
{
	int I = getColumnCount();
	for
		( int i = 0; i < I; i++ )
	{
		if ( ! getColumn( i ).isRectangular() ) return false;
	}
	return true;
}
}
