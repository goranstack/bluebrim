package com.bluebrim.layout.impl.shared;
import com.bluebrim.base.shared.*;

/**
 * Interface for a serializable and immutable run around specification.
 * A run around specification is responsible for extracting the run around shape from a page item.
 * The run around shape is the shape that other page items run around.
 * 
 * @author: Dennis Malmström
 */

public interface CoImmutableRunAroundSpecIF extends java.io.Serializable, CoFactoryElementIF
{
CoRunAroundSpecIF deepClone();


boolean doUseStroke();


public double getBottomMargin();


public double getLeftMargin();


public double getRightMargin();


public double getTopMargin();
}