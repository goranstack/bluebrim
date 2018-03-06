package com.bluebrim.layout.impl.shared;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Interface for a serializable and immutable baseline grid
 * 
 * @author: Dennis Malmström
 */
public interface CoImmutableBaseLineGridIF extends CoObjectIF, Cloneable, java.io.Serializable {
	public static String BASE_LINE_GRID = "baselinegrid";
	
	Object clone() throws CloneNotSupportedException;
	CoImmutableBaseLineGridIF deepClone();
	CoImmutableBaseLineGridIF derive(double y);
	CoBaseLineGeometryIF getBaseLineGeometry(double y);
	double getDeltaY();
	double getY0();
	double getY0Position();
	boolean isDerived();
}