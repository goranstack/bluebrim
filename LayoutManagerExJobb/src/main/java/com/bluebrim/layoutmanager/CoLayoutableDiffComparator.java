package com.bluebrim.layoutmanager;

/**
 * Compares two CoLayoutableIF acording to the differens between 
 * the width and the height. 
 * Creation date: (2000-06-26 09:05:05)
 * @author: Arvid Berg & Masod Jalalian
 */
import com.bluebrim.layout.shared.*;
 
public class CoLayoutableDiffComparator implements java.util.Comparator {
/**
 * LayoutableAreaComparator constructor comment.
 */
public CoLayoutableDiffComparator() {
	super();
}
/**
 * compare method comment.
 */
public int compare(Object o1, Object o2) 
{
	CoLayoutableIF oo1=(CoLayoutableIF)o1;
	CoLayoutableIF oo2=(CoLayoutableIF)o2;
	double diffWH1=Math.abs((int)(oo1.getLayoutWidth()-oo1.getLayoutHeight()));
	double diffWH2=Math.abs((int)(oo2.getLayoutWidth()-oo2.getLayoutHeight()));	
	double diffWH=diffWH1-diffWH2;
	if(diffWH<0)
		return 1;
	if(diffWH>0)
		return -1;
	return 0;
	
}
}
