package com.bluebrim.stroke.shared;
import java.rmi.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.system.shared.*;

/**
 * Protocol for a collection of CoStrokeIF's.
 * 
 * @author: Dennis Malmström
 */

public interface CoStrokeCollectionIF extends CoObjectIF, Remote {
	// Default dashes
	public static final String ALL_DOTS = "all_dots";
	public static final String DASH_DOT = "dash_dot";
	public static final String DOTTED = "dotted";
	public static final String DOTTED2 = "dotted2";
	// Default stripes	
	public static final String DOUBLE = "double";
	public static final String THICK_THIN = "thick_thin";
	public static final String THICK_THIN_THICK = "thick_thin_thick";
	public static final String THIN_THICK = "thin_thick";
	public static final String THIN_THICK_THIN = "thin_thick_thin";
	public static final String TRIPLE = "triple";

	void addStroke(CoStrokeIF s);

	public CoStrokeIF createStroke();

	List getStrokes();

	void removeStroke(Object[] strokes);

	public void addDefaultStrokes();

	int getImmutableStrokeCount();

	CoStrokeIF getStroke(CoGOI goi);
}