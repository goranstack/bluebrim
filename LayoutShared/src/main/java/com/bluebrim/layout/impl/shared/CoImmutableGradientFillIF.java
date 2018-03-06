package com.bluebrim.layout.impl.shared;

import com.bluebrim.paint.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Interface for a serializable and immutable gradient fill style
 * 
 * @author: Dennis Malmström
 */
public interface CoImmutableGradientFillIF extends CoImmutableFillStyleIF {
	public static String GRADIENT_FILL = "gradient_fill";
	public double getAngle();
	public double getBlendLength();

	public boolean getCyclic();
	public float getShade1();
	public float getShade2();
	public double getX1();
	public double getX2();
	public double getY1();
	public double getY2();

	public CoColorIF getColor1();

	public CoColorIF getColor2();

	CoRef getColorId1();

	CoRef getColorId2();
}