package com.bluebrim.layout.impl.shared;

import com.bluebrim.paint.shared.*;

/**
 * Interface for a serializable and mutable gradient fill style
 * 
 * @author: Dennis Malmström
 */
public interface CoGradientFillIF extends CoFillStyleIF, CoImmutableGradientFillIF {
	public void setAngle(double a);
	public void setBlendLength(double a);

	public void setCyclic(boolean cyclic);
	public void setPoint1(double x1, double y1);
	public void setPoint2(double x2, double y2);
	public void setShade1(float shade);
	public void setShade2(float shade);

	public void setColor1(CoColorIF color);

	public void setColor2(CoColorIF color);
}