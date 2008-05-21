package com.bluebrim.layout.impl.shared;



/**
 * Interface for a serializable and mutable baseline grid
 * 
 * @author: Dennis Malmstr�m
 */

public interface CoBaseLineGridIF extends CoImmutableBaseLineGridIF
{
void set( double y0, double dy );
void setDeltaY( double dy );
void setY0( double y0 );
}
