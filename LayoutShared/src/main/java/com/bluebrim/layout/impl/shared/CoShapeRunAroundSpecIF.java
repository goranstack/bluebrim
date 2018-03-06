package com.bluebrim.layout.impl.shared;


/**
 * Interface for a serializable and mutable shape run around specification.
 * 
 * @author: Dennis Malmström
 */

public interface CoShapeRunAroundSpecIF extends CoImmutableShapeRunAroundSpecIF, CoRunAroundSpecIF
{
public void setMargin( double m );

}