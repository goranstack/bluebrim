package com.bluebrim.layout.impl.shared;

import com.bluebrim.layout.shared.*;





/**
 * Interface for a serializable and immutable custom snap grid
 * 
 * @author: Dennis Malmström
 */

public interface CoImmutableCustomGridIF extends CoImmutableSnapGridIF
{
	public static final String CUSTOM_GRID = "CUSTOM_GRID";
double findXPosition( double x, double range );
double findYPosition( double x, double range );
double getXPosition( int i );
int getXPositionCount();
double getYPosition( int i );
int getYPositionCount();
}
