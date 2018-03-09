package com.bluebrim.layoutmanager;

/**
 * Compares two CoLayoutableIF acording to area.
 * Creation date: (2000-06-26 09:05:05)
 * @author: Arvid Berg & Masod Jalalian
 */
import com.bluebrim.layout.shared.*;
public class CoLayoutableAreaComparator implements java.util.Comparator {
/**
 * LayoutableAreaComparator constructor comment.
 */
public CoLayoutableAreaComparator() {
	super();
}
/**
 * compare method comment.
 */
public int compare(Object o1, Object o2) 
{
	CoLayoutableIF oo1=(CoLayoutableIF)o1;
	CoLayoutableIF oo2=(CoLayoutableIF)o2;
	double area1=oo1.getLayoutWidth()+oo1.getLayoutHeight();
	double area2=oo2.getLayoutWidth()+oo2.getLayoutHeight();	
	double a=area1-area2;
	if(a<0)
		return 1;
	if(a>0)
		return -1;
	return 0;
}
}
