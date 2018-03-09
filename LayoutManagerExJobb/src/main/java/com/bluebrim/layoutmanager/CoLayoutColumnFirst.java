package com.bluebrim.layoutmanager;

/**
 * A sub class of CoLayoutSpace with diffrent arange and cost methods.
 * Creation date: (2000-06-19 11:02:55)
 * @author: Arvid Berg & Masod Jalalian 
 */
import java.util.*;

import com.bluebrim.layout.shared.*;

public class CoLayoutColumnFirst extends CoLayoutSpace {
/**
 * LayoutColumnFirst constructor comment.
 */
public CoLayoutColumnFirst() {
	super();
}
/**
 * LayoutColumnFirst constructor comment.
 * @param x double
 * @param y double
 * @param cWidth double
 * @param cHeight double
 * @param cCount int
 * @param lockedObjects java.util.Collection
 */
public CoLayoutColumnFirst(com.bluebrim.layout.shared.CoLayoutableContainerIF p, java.util.Collection lockedObjects) 
{
	super(p, lockedObjects);
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-19 10:54:01)
 * @param list java.awt.List
 */
public void arange(List list) 
{
	//java.util.Collections.sort(list,new LayoutableDiffComparator());
	java.util.Collections.sort(list,new CoLayoutableColumnComparator());
	//java.util.Collections.sort(list,new LayoutableHeightComparator());
	//java.util.Collections.sort(list,new LayoutableAreaComparator());
	//java.util.Collections.shuffle(list);
}
/**
 * Calculates the cost for placing the 
 * a rect in column i at intervall intervall.
 * Creation date: (2000-06-15 09:56:34)
 * @return double the cost to place it here
 * @param col int the column
 * @param intervall columnalg.server.Intervall the interval to evaluate
 */
public double cost(int col, com.bluebrim.layoutmanager.CoInterval intervall) 
{
	//return Math.sqrt((Math.pow((columnCount-(col+1))*columnWidth,2)+
	//		Math.pow(columnHeight-intervall.getStop(),2)));
	return ((columnCount-(col+1))*columnWidth);
	//return (columnHeight-intervall.getStop());
	/*
	
	
	*/	
}
/**
 * Calculates the cost for placing 
 * a rect in column i at intervall intervall.
 * Creation date: (2000-06-15 09:56:34)
 * @return double the cost to place it here
 * @param col int the column
 * @param intervall columnalg.server.Intervall the interval to evaluate
 */
public double cost(int col, com.bluebrim.layoutmanager.CoInterval intervall,CoLayoutableIF rect) 
{
	 return Math.sqrt((Math.pow((columnCount-(col+1))*columnWidth,2)+
			Math.pow(columnHeight-intervall.getStop(),2)));
	//return ((columnCount-(col+1))*columnWidth);
	//return (columnHeight-intervall.getStop());

	//return intervall.getStop();
	//return rect.getLayoutHeight()-intervall.getSize();
	//return intervall.getSize()-rect.getLayoutHeight();

	/*double diff=rect.getLayoutHeight()-intervall.getSize();
	if(diff>(-68))
		diff=diff-68;
	
	return diff*1000+((columnCount-(col+1))*columnWidth);
	*/
}
}
