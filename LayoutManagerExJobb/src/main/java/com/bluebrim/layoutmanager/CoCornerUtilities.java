package com.bluebrim.layoutmanager;

import java.awt.geom.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Helpfull variabels and methods.
 * Creation date: (2000-07-12 16:10:51)
 * @author: Arvid Berg & Masod Jalalian 
 */
public class CoCornerUtilities 
{
	public static final int RIGHT=0x01;
	public static final int LEFT=0x02;
	public static final int TOP=0x04;
	public static final int BOTTOM=0x08;

	public static final int BOTTOM_LEFT=BOTTOM|LEFT;	// 0x0A ,10
	public static final int BOTTOM_RIGHT=BOTTOM|RIGHT;	// 0x09 , 9
	public static final int TOP_LEFT=TOP|LEFT;			// 0x06 , 6
	public static final int TOP_RIGHT=TOP|RIGHT;		// 0x05 , 5
/**
 * Utilities constructor comment.
 */
public CoCornerUtilities() {
	super();
}
/**
 * Uses the CoCornerLocationSpec's to identify align
 * Creation date: (2000-07-20 15:45:38)
 * @return int
 * @param loc CoLocationSpecIF
 */
public static int alignLocSpec(CoImmutableLocationSpecIF loc) 
{
	if(loc instanceof CoCornerLocationSpecIF){
		CoCornerLocationSpecIF l=((CoCornerLocationSpecIF)loc);
		String orientation=l.getType();
		
		if(orientation.equals(CoCornerLocationSpecIF.BOTTOM_LEFT))
			return BOTTOM_LEFT;
		if(orientation.equals(CoCornerLocationSpecIF.BOTTOM_RIGHT))
			return BOTTOM_RIGHT;
		if(orientation.equals(CoCornerLocationSpecIF.TOP_LEFT))
			return TOP_LEFT;
		if(orientation.equals(CoCornerLocationSpecIF.TOP_RIGHT))
			return TOP_RIGHT;

	}
	return 0;
}
/**
 * Checks if mask is part of align
 * Creation date: (2000-07-20 15:21:22)
 * @return boolean
 * @param align int
 * @param mask int
 */
public static boolean areEqual(int align, int mask) 
{
	if( (align & mask)!=0 )
		return true;
	return false;
}
/**
 * Returns a point which contains the unit displacement in x and y from
 * the corner indicated by align to the top left corner.
 * Creation date: (2000-07-13 10:02:32)
 * @return java.awt.Point
 * @param align int
 */
public static Point2D calculateDisplacement(int align) 
{
	int widthAdj=0,heightAdj=0;					
	switch(align)
	{
		case BOTTOM_RIGHT:
			widthAdj=-1;
			heightAdj=-1;
			break;
		case BOTTOM_LEFT:
			widthAdj=0;
			heightAdj=-1;
			break;
		case TOP_RIGHT:
			widthAdj=-1;
			heightAdj=0;
			break;
		case TOP_LEFT:
			widthAdj=0;
			heightAdj=0;
			break;
	}	
	return new Point2D.Double(widthAdj,heightAdj);
}
}