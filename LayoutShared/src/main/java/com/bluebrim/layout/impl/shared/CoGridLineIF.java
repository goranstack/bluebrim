package com.bluebrim.layout.impl.shared;

import java.awt.geom.*;
import java.io.*;

/**
 * RMI-enabling interface for CoGridLine.
 * 
 * @author: Dennis Malmström
 */

public interface CoGridLineIF extends Serializable, Cloneable {
	Object clone() throws CloneNotSupportedException;
	CoGridLineIF forcedIntersect(double x, double y, double w, double h);
	double getLength();
	Line2D getShape();
	double getX();
	double getY();
	CoGridLineIF intersect(double x, double y, double w, double h);
	boolean snap(double x, double y, double w, double h, double range, Point2D p, Point2D d);
	boolean snap(double x, double y, double range, int edgeMask, int dirMask, Point2D p, Point2D d);
	boolean snap(double x, double y, double range, int edgeMask, Point2D p, Point2D d);
	void translateBy(double dx, double dy);

	int COLUMN = 2;
	int CUSTOM = 4;
	int EDGE = 0;
	int GAP = 3;
	int MARGIN = 1;

	int getType();
}