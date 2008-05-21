package com.bluebrim.layout.impl.shared;


/**
 * Interface for a serializable and mutable custom snap grid
 * 
 * @author: Dennis Malmström
 */

public interface CoCustomGridIF extends CoSnapGridIF, CoImmutableCustomGridIF
{
void addXPosition( double x );
void addYPosition( double y );
void removeAllXPositions();
void removeAllYPositions();
void removeXPosition( double x );
void removeXPosition( int i );
void removeYPosition( double x );
void removeYPosition( int i );
}
