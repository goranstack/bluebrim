package com.bluebrim.layout.impl.shared;


/**
 * Interface for a serializable and immutable shape run around specification.
 * The run around shape is the page items shape.
 * 
 * @author: Dennis Malmström
 */

public interface CoImmutableShapeRunAroundSpecIF extends CoImmutableRunAroundSpecIF
{
	public static String SHAPE_RUN_AROUND_SPEC = "SHAPE_RUN_AROUND_SPEC";

public double getMargin();
}