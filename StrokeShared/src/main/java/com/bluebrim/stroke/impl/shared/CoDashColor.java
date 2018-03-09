package com.bluebrim.stroke.impl.shared;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.stroke.shared.*;

/**
 * Abstract superclass for implementations of dash colors.
 * A dash color is an object that extracts a color and a color shade from a CoStrokePropertiesIF.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoDashColor extends CoObject implements CoDashColorIF {
	public static final String XML_TAG = "dash-color";
	public static final String XML_COLOR = "color";

	public void xmlInit(Map attributes) {
	}
}