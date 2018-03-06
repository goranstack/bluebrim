package com.bluebrim.layoutmanager;

/**
 * Compare two intervalls if they are "> == <" according first to start then to stop values 
 * Creation date: (2000-06-08 10:02:35)
 * @author: Arvid Berg & Masod Jalalian 
 */

public class CoIntervalComparator implements java.util.Comparator {
/**
 * ComparatorIntervall constructor comment.
 */
public CoIntervalComparator() 
{
	super();
}
/**
 * Compare two Intervalls
 */
public int compare(Object o1, Object o2) 
{
	com.bluebrim.layoutmanager.CoInterval ob1=(com.bluebrim.layoutmanager.CoInterval)o1;
	com.bluebrim.layoutmanager.CoInterval ob2=(com.bluebrim.layoutmanager.CoInterval)o2;
	double start=ob2.getStart()-ob1.getStart();	
	if( start!=0 )
		return (int)start;
	else
	{
		double stop=ob2.getStop()-ob1.getStop();
		if(stop!=0)
			return (int)stop;
	}
	return 0;
}
}
