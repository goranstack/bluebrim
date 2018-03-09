package com.bluebrim.layout.impl.shared;

import com.bluebrim.layout.shared.*;

public interface CoImmutableCornerLocationSpecIF extends Cloneable, CoImmutableLocationSpecIF
{



	public static final String BOTTOM_OUTSIDE = "BOTTOM_OUTSIDE";
	public static final String BOTTOM_INSIDE = "BOTTOM_INSIDE";
	public static final String TOP_OUTSIDE = "TOP_OUTSIDE";
	public static final String TOP_INSIDE = "TOP_INSIDE";


	public static final String BOTTOM_LEFT = "BOTTOM_LEFT";
	public static final String BOTTOM_RIGHT = "BOTTOM_RIGHT";
	public static final String TOP_LEFT = "TOP_LEFT";
	public static final String TOP_RIGHT = "TOP_RIGHT";

int getXInset();
boolean isAggressive();
}
