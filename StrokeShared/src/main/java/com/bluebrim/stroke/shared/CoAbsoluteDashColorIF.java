package com.bluebrim.stroke.shared;


/**
 * RMI-enabling interface for class com.bluebrim.stroke.impl.shared.CoAbsoluteDashColor.
 * 
 * @author: Dennis Malmström
 */

public interface CoAbsoluteDashColorIF extends CoDashColorIF
{
	public static String ABSOLUTE_DASH_COLOR_SPEC = "absolute_dash_color_spec";

float getShade();

void setShade( float s );

com.bluebrim.paint.shared.CoColorIF getColor();

void setColor( com.bluebrim.paint.shared.CoColorIF c );
}