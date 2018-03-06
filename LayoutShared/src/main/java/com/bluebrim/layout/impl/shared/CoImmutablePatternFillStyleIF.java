package com.bluebrim.layout.impl.shared;


/**
 * Insert the type's description here.
 * Creation date: (2001-04-27 09:48:29)
 * @author: 
 */

public interface CoImmutablePatternFillStyleIF extends CoImmutableFillStyleIF
{
	String PATTERN_FILL = "PATTERN_FILL";
	
	String CHESSBOARD = "chessboard";
	String SCALES = "scales";
	String MESH = "mesh";
	String DOTS = "dots";

public String getPattern();
}
