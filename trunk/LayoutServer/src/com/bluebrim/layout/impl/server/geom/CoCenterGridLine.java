package com.bluebrim.layout.impl.server.geom;

import java.awt.geom.*;

/**
 * Implementation of center grid line.
 * 
 * @author: Dennis Malmström
 */

public class CoCenterGridLine extends CoVerticalGridLine
{




public boolean snap( double x, double y, double range, int edgeMask, int dirMask, Point2D p, Point2D d )
{
	if
		( ( edgeMask & CENTER_EDGE_MASK )  == 0 )
	{
		return false;
	}
	
	return super.snap( x, y, range, edgeMask, dirMask, p, d );
}
public String toString()
{
	return "grid line (C) " + "( " + m_x + ", " + m_y + " ) " + m_length;
}

public CoCenterGridLine( double x, double y, double length, int type )
{
	super( x, y, length, type );
}
}