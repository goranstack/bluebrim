package com.bluebrim.base.shared;

/**
 * Interface for a serializable and mutable lines
 * 
 * @author: Dennis Malmström
 */

public interface CoLineIF extends CoImmutableLineIF, CoShapeIF 
{
	public final static String LINE		= "line";
	public final static String ORTHOGONAL_LINE		= "orthogonal_line";
public void setGeometry (double x1, double y1, double x2, double y2);
public void setX1 (double x);
public void setX2 (double x);
public void setY1 (double y);
public void setY2 (double y);
}
