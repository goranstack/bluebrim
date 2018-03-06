package com.bluebrim.base.shared;

/**
 * Interface for a serializable and mutable rectangular shape with shaped corners
 * 
 * @author: Dennis Malmström
 */

public interface CoCornerRectangleIF extends CoBoundingShapeIF, CoImmutableCornerRectangleIF
{
	public final static String CORNER_RECTANGLE		= "corner_rectangle";
public void setCornerRadius(double cornerRadius);
}
