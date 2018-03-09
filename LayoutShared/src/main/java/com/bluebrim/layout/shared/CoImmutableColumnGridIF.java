package com.bluebrim.layout.shared;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Interface for a serializable and immutable column grid
 * 
 * @author: Dennis Malmström
 */

public interface CoImmutableColumnGridIF extends CoImmutableSnapGridIF {
	public static String COLUMN_GRID = "columngrid";

	// detail mask values (see method CoImmutableSnapGridIF.getShape)
	public static final int MARGINS = 1;
	public static final int COLUMN_SPACINGS = 2;
	public static final int COLUMNS = 4;

	CoImmutableColumnGridIF derive(double x, double y, double w, double h);
	int getColumnCount();
	CoColumnGeometryIF getColumnGeometry(CoImmutableShapeIF shape);

	public double getWidthFor(int numberOfColumns);	

	double getBottomMargin();
	double getBottomMarginPosition();
	double getLeftMargin();
	double getLeftMarginPosition();

	double getRightMargin();
	double getRightMarginPosition();
	double getTopMargin();
	double getTopMarginPosition();

	double getColumnSpacing();
	double getColumnWidth();
	double getHeight();
	double getWidth();	

	public double getLiveAreaWidth();
	public double getLiveAreaHeight();

	boolean isDerived();

	public boolean isLeftOutsideSensitive();

	public boolean isSpread();
}