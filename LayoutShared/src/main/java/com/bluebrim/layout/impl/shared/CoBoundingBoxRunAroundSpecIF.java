package com.bluebrim.layout.impl.shared;


/**
 * Interface for a serializable and mutable bounding box run around specification.
 * 
 * @author: Dennis Malmström
 */

public interface CoBoundingBoxRunAroundSpecIF extends CoImmutableBoundingBoxRunAroundSpecIF, CoRunAroundSpecIF
{
public void setBottom( double b );
public void setLeft( double l );
public void setRight( double r );
public void setTop( double t );
public void setUseStroke( boolean b );
}
