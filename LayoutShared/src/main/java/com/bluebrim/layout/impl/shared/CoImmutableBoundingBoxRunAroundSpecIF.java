package com.bluebrim.layout.impl.shared;


/**
 * Interface for a serializable and immutable bounding box run around specification.
 * The run around shape is bounding box of the page items shape.
 * 
 * @author: Dennis Malmström
 */

public interface CoImmutableBoundingBoxRunAroundSpecIF extends CoImmutableRunAroundSpecIF
{
	public static String BOUNDING_BOX_RUN_AROUND_SPEC = "BOUNDING_BOX_RUN_AROUND_SPEC";
boolean doUseStroke();
public double getBottom();
public double getLeft();
public double getRight();
public double getTop();
}
