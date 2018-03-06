package com.bluebrim.layoutmanager;

/**
 * Compares two CoLayoutableIF acording to column width then height.
 * Creation date: (2000-06-13 16:15:31)
 * @author: Arvid Berg & Masod Jalalian 
 */

import com.bluebrim.layout.shared.*;
public class CoLayoutableColumnComparator implements java.util.Comparator {
/**
 * LayoutableColumnComparator constructor comment.
 */
public CoLayoutableColumnComparator() 
{
	super();
}
/**
 * compare method comment.
 */
public int compare(Object o1, Object o2) 
{
	CoLayoutableIF oo1=(CoLayoutableIF)o1;
	CoLayoutableIF oo2=(CoLayoutableIF)o2;
	double w=(int)oo2.getLayoutWidth()-(int)oo1.getLayoutWidth();
	if(w==0)
	{
		double h=oo2.getLayoutHeight()-oo1.getLayoutHeight();
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
}
