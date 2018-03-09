package com.bluebrim.layout.impl.server;

import java.util.*;
import java.awt.geom.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.*;

/**
 * Algorithm for calculating the bottom right positon of a new child in a layout area.
 * Creation date: (2000-06-19 20:19:53)
 * @author: Dennis
 */
 
public class CoPageItemBottomRightPosition extends CoAbstractPageItemPosition
{
	private double m_alpha = 45; // angle in degrees
	private double m_span = 0.5;
public double getAlpha()
{
	return m_alpha;
}
public double getSpan()
{
	return m_span;
}

public void setAlpha( double alpha )
{
	m_alpha = alpha;
}
public void setSpan( double span )
{
	m_span = span;
}

public CoPoint2DDouble place( CoCompositePageItemIF parent,
	                    				CoShapePageItemIF child,
	                    				CoPoint2DDouble pos // return value if != null
	              		 				)
{
	CoImmutableShapeIF s = child.getCoShape();

	double parentWidth = parent.getCoShape().getWidth();
	double w = s.getWidth();
	double h = s.getHeight();

	CoImmutableColumnGridIF columnGrid = parent.getColumnGrid();
	
	double D = parentWidth * m_span; // area resolution
	double k = Math.tan( Math.PI * m_alpha / 180 );
	int n = 1; // area counter
	
	double lmp = columnGrid.getLeftMarginPosition();
	double rmp = columnGrid.getRightMarginPosition();
	double bmp = columnGrid.getBottomMarginPosition();
	double tmp = columnGrid.getTopMarginPosition();
	double rm = columnGrid.getRightMargin();

	int c0 = columnGrid.getColumnCount();
	double cw = columnGrid.getColumnWidth();
	double cs = columnGrid.getColumnSpacing();
	int c = c0;
	
	double x = lmp + c * ( cw + cs ) - cs;
	double y = bmp;

	double Y1 = bmp - ( x - ( parentWidth - rm - D * n ) ) * k;

	List children = parent.getChildren();
	
	outer:
	while
		( true )
	{
		Iterator i = children.iterator();
		while
			( i.hasNext() )
		{
			CoLayoutableIF l = (CoLayoutableIF) i.next();
			// check for overlap
			if ( l.getX() > x ) continue;
			if ( l.getX() + l.getLayoutWidth() < x - w ) continue;
			if ( l.getY() > y ) continue;
			if ( l.getY() + l.getLayoutHeight() < y - h ) continue;

			// overlap -> try at top of overlapping child
			y = l.getY() - 1;
			
			if
				( ( y - h <= tmp ) || ( y < Y1 ) )
			{
				// out of y-space -> try next kolumn
				c--;
				x = lmp + c * ( cw + cs ) - cs;
				if
					( x - w < lmp )
				{
					// out of x-space
					if
						( Y1 < 0 )
					{
						// container scanned -> no pos found -> return null
						return null;
						/*
						if ( pos == null ) pos = new Point2D.Double();
						pos.setLocation( 0, 0 );
						break outer;
						*/
					}

					// return to rightmost x and try next area
					c = c0;
					x = lmp + c * ( cw + cs ) - cs;
					n++;
				}

				// calculate y-range
				Y1 = bmp - ( x - ( parentWidth - rm - D * n ) ) * k;
				y = bmp;
			}

			continue outer;
		}

		// no overlap -> return this position
		if ( pos == null ) pos = new CoPoint2DDouble();
		pos.setLocation( x - w, y - h );
		break outer;
	}
	
	return pos;

}
}