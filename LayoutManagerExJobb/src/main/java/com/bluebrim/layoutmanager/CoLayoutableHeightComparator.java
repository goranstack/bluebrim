package com.bluebrim.layoutmanager;

/**
 * Compares two CoLayoutableIF acording to column height then width.
 * Creation date: (2000-06-26 09:05:05)
 * @author: Arvid Berg & Masod Jalalian
 */
import com.bluebrim.layout.shared.*;

public class CoLayoutableHeightComparator implements java.util.Comparator {
/**
 * LayoutableAreaComparator constructor comment.
 */
public CoLayoutableHeightComparator() {
	super();
}
/**
 * compare method comment.
 */
public int compare(Object o1, Object o2) 
{
	CoLayoutableIF oo1=(CoLayoutableIF)o1;
	CoLayoutableIF oo2=(CoLayoutableIF)o2;
	double h=(int)oo2.getLayoutHeight()-(int)oo1.getLayoutHeight();
	if(h==0)
	{
		double w=(int)oo2.getLayoutWidth()-(int)oo1.getLayoutWidth();	
		if(w>0)
			return 1;
		else
			if(w<0)
				return -1;
			else
				return 0;
	}
	else if(h>0)
		return 1;
		else
			return -1;
	
}
}
