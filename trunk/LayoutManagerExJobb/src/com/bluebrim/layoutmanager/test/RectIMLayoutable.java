package com.bluebrim.layoutmanager.test;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * @author Mats Åström (2000-05-22 09:40:59)
 */
public class RectIMLayoutable implements CoLayoutableIF ,Comparable{
	protected double x;
	protected double y;
	protected double width;
	public double height;
	private boolean layoutsucc=false;
/**
 * RectIMLayoutable constructor comment.
 */
public RectIMLayoutable() {
	super();
}
/**
 * RectIMLayoutable constructor comment.
 */
public RectIMLayoutable(double w,double h) {
	super();
	x=0;
	y=0;
	width=w;
	height=h;
}
/**
 * compareTo method comment.
 */
public int compareTo(java.lang.Object o) 
{
	

	double w=getLayoutWidth()-((RectIMLayoutable)o).getLayoutWidth();
	if(w==0)
	{
		double h=getLayoutHeight()-((RectIMLayoutable)o).getLayoutHeight();
		if(h>0)
			return 1;
		else
			if(h<0)
				return -1;
			else
				return 0;
			
	}
	else if(w>0)
		return 1;
		else
			return -1;
		
}
/**
 * getArea method comment.
 */
public double getArea() {
	return getLayoutWidth()*getLayoutHeight();
}
/**
 * getBottomEdge method comment.
 */
public double getBottomEdge() {
	return 0;
}
/**
 * getColumnGrid method comment.
 */
public com.bluebrim.layout.shared.CoImmutableColumnGridIF getColumnGrid() {
	return null;
}
/**
 * getContentHeight method comment.
 */
public double getContentHeight() {
	return 0;
}
/**
 * getContentWidth method comment.
 */
public double getContentWidth() {
	return 0;
}
/**
 * getDoRunAround method comment.
 */
public boolean getDoRunAround() {
	return false;
}
/**
 * getInteriorHeight method comment.
 */
public double getInteriorHeight() {
	return height;
}
/**
 * getInteriorWidth method comment.
 */
public double getInteriorWidth() {
	return 0;
}
/**
 * getLayoutHeight method comment.
 */
public double getLayoutHeight() {
	return height;
}
/**
 * getLayoutParent method comment.
 */
public com.bluebrim.layout.shared.CoLayoutableContainerIF getLayoutParent() {
	return null;
}

/**
 * getLayoutWidth method comment.
 */
public double getLayoutWidth() {
	return width;
}
/**
 * getLeftEdge method comment.
 */
public double getLeftEdge() {
	return 0;
}
/**
 * getLocationSpec method comment.
 */
public com.bluebrim.layout.shared.CoImmutableLocationSpecIF getLocationSpec() {
	return null;
}
/**
 * getRightEdge method comment.
 */
public double getRightEdge() {
	return 0;
}
/**
 * getTopEdge method comment.
 */
public double getTopEdge() {
	return 0;
}
/**
 * getX method comment.
 */
public double getX() {
	return x;
}
/**
 * getY method comment.
 */
public double getY() {
	return y;
}
/**
 * hasValidLayout method comment.
 */
public boolean hasValidLayout() {
	return layoutsucc;
}
/**
 * invalidateHeight method comment.
 */
public void invalidateHeight() {}
/**
 * invalidateWidth method comment.
 */
public void invalidateWidth() {}
/**
 * isContentEmpty method comment.
 */
public boolean isContentEmpty() {
	return false;
}
/**
 * @author Mats Åström (2000-05-22 09:57:53)
 */
public void print() {System.out.print("Width="+width+" Height="+height);}
/**
 * reshapeToContentHeight method comment.
 */
public void reshapeToContentHeight() {}
/**
 * reshapeToContentWidth method comment.
 */
public void reshapeToContentWidth() {}
/**
 * setLayoutHeight method comment.
 */
public void setLayoutHeight(double height) {}
/**
 * setLayoutLocation method comment.
 */
public void setLayoutLocation(double x, double y) {}
/**
 * setLayoutSuccess method comment.
 */
public void setLayoutSuccess(boolean b) {layoutsucc=b;}
/**
 * setLayoutWidth method comment.
 */
public void setLayoutWidth(double width) {}
/**
 * setLayoutX method comment.
 */
public void setLayoutX(double x1) {x=x1;}
/**
 * setLayoutY method comment.
 */
public void setLayoutY(double y1) {y=y1;}

/**
 * getLayoutSpec method comment.
 */
public com.bluebrim.layout.shared.CoLayoutSpecIF getLayoutSpec() {
	return null;
}
}