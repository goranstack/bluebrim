package com.bluebrim.stroke.shared;

import com.bluebrim.paint.shared.*;

/**
 * Interface for a serializable and mutable stroke properties object
 * 
 * @author: Dennis Malmström
 */

public interface CoStrokePropertiesIF extends CoImmutableStrokePropertiesIF {
	String NON_SYMMETRIC = "NON_SYMMETRIC";
	String SYMMETRIC_BY_STRETCHING_CORNERS = "SYMMETRIC_BY_STRETCHING_CORNERS";
	String SYMMETRIC_BY_STRETCHING_DASH = "SYMMETRIC_BY_STRETCHING_DASH";
	String SYMEETRIC_BY_STRETCHING_DASH_GP = "SYMEETRIC_BY_STRETCHING_DASH_GP";

	void setAlignment(int a);

	void setAlignOffset(float ao);

	void setBackgroundShade(float s);

	void setForegroundShade(float s);

	void setStroke(CoStrokeIF mss);

	void setSymmetry(String s);

	void setWidth(float w);

	void setBackgroundColor(CoColorIF bgc);

	void setForegroundColor(CoColorIF bgc);

}