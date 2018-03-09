package com.bluebrim.layoutmanager;

/**
 * Represent a intervall with a start and a stop value.
 * Creation date: (2000-06-06 09:16:57)
 * @author: Arvid Berg & Masod Jalalian 
 */
import java.util.*;

public class CoInterval implements Comparable{
	protected double m_start;
	protected double m_stop;
	public final static CoInterval ZERO=new CoInterval(0.0,0.0);
/**
 * Constructs a Intervall with start and stop set to zero.
 */
public CoInterval() 
{
	super();
	m_start=0;
	m_stop=0;
}
/**
 * Construct a intervall with start and stop acordingly.
 * Creation date: (2000-06-06 09:18:32)
 * @param start double
 * @param stop double
 */
public CoInterval(double start, double stop) 
{
	m_start=Math.min(start,stop);
	m_stop=Math.max(start,stop);
}
/**
 * Creates a new intervall with the same values as param
 * Creation date: (2000-06-07 15:29:49)
 * @param param columnalg.server.Intervall
 */
public CoInterval(CoInterval param) 
{
	m_start=param.getStart();
	m_stop=param.getStop();
}
/**
 * Compares this object with obj return 1 0 -1 depending if this > = < obj.
 * Creation date: (2000-06-06 09:22:28)
 * @return int
 * @param intervall columnalg.Intervall
 */
public int compareTo(Object obj) 
{
	double start=m_start-((CoInterval)obj).m_start;	
	if( start!=0 )
		return (int)start;
	else
	{
		double stop=m_stop-((CoInterval)obj).m_stop;
		if(stop!=0)
			return (int)stop;
	}
	return 0;
}
/**
 * Returns a collection of intervalls representng the result if intervall is removed form this.
 * Creation date: (2000-06-07 15:08:12)
 * @return columnalg.server.Intervall
 * @param intervall columnalg.server.Intervall
 */
public Collection difference(CoInterval intervall) 
{
	Collection result=new LinkedList();
	double difStart = Math.min(m_stop,intervall.m_start) - m_start;
	double difStop  = m_stop  - Math.max(m_start,intervall.m_stop);
	if(difStart > 0)
		result.add(new CoInterval(m_start,Math.min(m_stop,intervall.m_start)));
	if(difStop > 0)
		result.add(new CoInterval(Math.max(m_start,intervall.m_stop),m_stop));
	return result;
}
/**
 * Checks if this are equal to obj.
 * Creation date: (2000-06-08 09:26:54)
 * @return boolean
 * @param obj java.lang.Object
 */
public boolean equals(Object obj) 
{
	double start=m_start-((CoInterval)obj).m_start;
	double stop=m_stop-((CoInterval)obj).m_stop;
	if( start==0 && stop==0)
		return true;
	return false;
}
/**
 * return the size of the intervall
 * Creation date: (2000-06-07 10:00:46)
 * @return double
 */
public double getSize() 
{
	return Math.abs(m_stop-m_start);
}
/**
 * returns the start value.
 * Creation date: (2000-06-07 14:29:14)
 * @return double
 */
public double getStart() 
{
	return m_start;
}
/**
 * Return the stop value.
 * Creation date: (2000-06-07 14:29:14)
 * @return double
 */
public double getStop() 
{
	return m_stop;
}
/**
 * Return a collection of intervall as a result of the curent 
 * intervall intersection  with intervall.
 * Creation date: (2000-06-06 10:05:54)
 * @return columnalg.Intervall
 * @param intervall columnalg.Intervall
 */
public Collection intersection(CoInterval intervall) 
{
	Collection result=new LinkedList();
	double start=Math.max(m_start,intervall.m_start);
	double stop =Math.min(m_stop, intervall.m_stop);
	if(stop <=start)
		;
	else
		result.add(new CoInterval(start,stop));
	return result;
}
/**
 * Static method to invert a list of intervall with the universa defined by range.
 * Creation date: (2000-06-06 14:43:18)
 * @return java.util.List
 * @param list java.util.List
 * @param range Intervall a intervall from the top to the bottom of the 
 * list
 */
public static List invert(List list,CoInterval range) 
{
	if(range==null)
		range=new CoInterval(0,1000);
	List newlist=null;
	boolean isempty;
	sort(list);
	newlist=new ArrayList();
	CoInterval obj1,obj2;
	Iterator iter=list.iterator();
	if(iter.hasNext())
	{
		obj1=(CoInterval)iter.next();
		newlist.add(new CoInterval(range.m_start,obj1.m_start));
		while(iter.hasNext())
		{				
			obj2=(CoInterval)iter.next();				
			newlist.add(new CoInterval(obj1.m_stop,obj2.m_start));								
			obj1=obj2;
		}
		newlist.add(new CoInterval(obj1.m_stop,range.m_stop));
	}
	else
	{
		newlist.add(new CoInterval(range.m_start,range.m_stop));
	}	
	return newlist;
}
/**
 * Return true if the intervall is empty.
 * Creation date: (2000-06-06 11:56:12)
 * @return boolean
 */
public boolean isEmpty() 
{
	if(m_start==m_stop)
		return true;
	return false;
}
/**
 * Creates a newlist from list where all intersections have been removed.
 * Creation date: (2000-06-06 09:38:30)
 * @return java.util.Collection
 * @param col java.util.Collection
 */
public static List merge(List list) 
{
	List newlist=null;
	boolean isempty;
	Comparator comp=new com.bluebrim.layoutmanager.CoIntervalComparator();
	java.util.Collections.sort(list,comp);
	
	
	isempty=false;	
	newlist=list;
	do
	{
		isempty=false;
		list=newlist;	
		newlist=new ArrayList();
	CoInterval obj1,obj2;
	for (int i = 0; i < list.size(); i++)
	{
		obj1=(CoInterval)list.get(i);
		for(int j=i+1;j<list.size();j++)
		{
			isempty=true;
			obj2=(CoInterval)list.get(j);			
			newlist.addAll(obj1.intersection(obj2));			
		}
		
	}
	
	
	}while(isempty || list.size()!=newlist.size());
	return newlist;
}
/**
 * Return a new list where all intervall from list2 have been added to list and all
 * intersections have been removed.
 * Creation date: (2000-06-06 09:38:30)
 * @return java.util.Collection
 * @param col java.util.Collection
 */
public static List merge(List list,List list2) 
{
	List newlist=null;
	boolean isempty;
	//Comparator comp=new ComparatorIntervall();
	//java.util.Collections.sort(list,comp);
	newlist=new ArrayList();
	Iterator iterL1=list.iterator();
	while(iterL1.hasNext())
	{
		CoInterval obj1=(CoInterval)iterL1.next();
		Iterator iterL2=list2.iterator();
		while(iterL2.hasNext())
		{
			CoInterval obj2=(CoInterval)iterL2.next();
			newlist.addAll(obj1.intersection(obj2));				
		}
	}
	return newlist;	
}
/**
 * Removes an Intervall represented by intervall from a list of intervalls.
 * Creation date: (2000-06-14 13:42:47)
 * @return java.util.List 
 * @param list java.util.List
 * @param intervall columnalg.server.Intervall
 */
public static List remove(List list, CoInterval intervall) 
{
	List tempList=new LinkedList();
	Iterator iter=list.iterator();
	while(iter.hasNext())
	{
		CoInterval curentIntervall=(CoInterval)iter.next();
		Collection tempIntervall=curentIntervall.intersection(intervall);
		if(tempIntervall.size()!=0 )
		{			
			tempList.addAll(curentIntervall.difference(intervall));
		}
		else
			tempList.add(curentIntervall);
	}	
	return tempList;
}
/**
* Orders the elements in the collection in a specified order.
* Currently the ordering criterion is the area of the shape with
* the elements in the list being arranged in the descending order
* of priority
**/
public static void sort(List list) 
{
	// loop through the list
	for(int i = 0; i < list.size()-1; i++) 
	{
		for(int j = i + 1; j < list.size(); j++) 
		{
			// check if the adjacent elements are out of order
			if( (((Comparable)list.get(j)).compareTo(list.get(i)))>0 )
			{
				// if so, the swap the shapes
				Object temp = list.get(j);
				list.set(j,list.get(i));
				list.set(i,temp);
			} // if
		} // for
	} // for
} // sort()
/**
 * Return the union between this intervall and  intervall.
 * Creation date: (2000-06-06 14:23:15)
 * @return columnalg.server.Intervall
 * @param intervall columnalg.server.Intervall
 */
public Collection union(CoInterval intervall) 
{
	CoInterval first,second;
	Collection result=new LinkedList();
	
	if(this.compareTo(intervall)>0)
	{
		second=this;
		first=intervall;
	}
	else
	{
		second=intervall;
		first=this;
	}
	if( second.m_start <= first.m_stop )
	{
		result.add(new CoInterval(Math.min(first.m_start,second.m_start),
			Math.max(first.m_stop,second.m_stop )));
	}
	else
	{
		result.add(first);
		result.add(second);
	}
	return result;
}
}
