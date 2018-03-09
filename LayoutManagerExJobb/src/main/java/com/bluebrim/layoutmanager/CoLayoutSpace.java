package com.bluebrim.layoutmanager;

/**
 * Contains methods for layouting objects to a area.
 * Creation date: (2000-06-14 11:19:24)
 * @author: Arvid Berg & Masod Jalalian 
 */
import java.awt.geom.*;
import java.util.*;

import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.shared.*;

public class CoLayoutSpace 
{
	private List columns;
	protected int columnCount;
	protected double columnWidth;
	protected double columnHeight;
	private List columnArray;
	protected CoImmutableColumnGridIF grid;
/**
 * LayoutArea constructor comment.
 */
public CoLayoutSpace() 
{
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-14 14:56:52)
 * @param x double
 * @param y double
 * @param width double
 * @param height double
 * @param lockedObjects java.util.Collection
 */
public CoLayoutSpace(CoLayoutableContainerIF parent, Collection lockedObjects) 
{
	grid=parent.getColumnGrid();
	columnWidth=grid.getColumnWidth();
	columnHeight=grid.getBottomMarginPosition();
	columnArray=new LinkedList();
	columns = new LinkedList();
	double x=grid.getLeftMarginPosition();
	double y=grid.getTopMarginPosition();
	double posX=x;
	columnCount=0;//grid.getColumnCount();
	for (int i = 0;((posX+columnWidth)<parent.getLayoutWidth()); i++)
	{
		posX=grid.snap( posX,y, 
		Double.POSITIVE_INFINITY, CoGeometryConstants.LEFT_EDGE_MASK, 
			 CoGeometryConstants.TO_RIGHT_MASK, false, null ).getX();
		CoColumnElement col=new CoColumnElement(posX,y,columnWidth,columnHeight);
		posX+=columnWidth;
		//CoColumnElement col=new CoColumnElement(x+i*columnWidth,y,columnWidth,columnHeight);
		col.getIntervalls().add(new com.bluebrim.layoutmanager.CoInterval(y,columnHeight));
		columns.add(col);
	}
	columnCount=columns.size();
	/*- Create free intervalls */
	/*- Same as update intervalls i place???, only the forloop, for afected columns replace intervalls*/
	if(lockedObjects!=null)
	{
		Iterator iter=lockedObjects.iterator();
		while(iter.hasNext())
		{
			CoLayoutableIF rect=(CoLayoutableIF)iter.next();
			com.bluebrim.layoutmanager.CoInterval rectIntervall=new com.bluebrim.layoutmanager.CoInterval(rect.getY(),rect.getY()
				+rect.getLayoutHeight());
			int startColumn=getColumn(new Point2D.Double(rect.getX(),rect.getY()));
			int stopColumn=getColumn(new Point2D.Double(rect.getX()+rect.getLayoutWidth(),rect.getY()));
			if(startColumn<0) startColumn=0;
			if(stopColumn<0) stopColumn=columns.size()-1;
			//for(int i=0;i<getRectColumnCount(rect);i++)
			for(int i=startColumn;i<=stopColumn;i++)
			{
				//CoColumnElement e=((CoColumnElement)columns.get(startColumn+i));
				CoColumnElement e=((CoColumnElement)columns.get(i));
				e.setIntervalls(com.bluebrim.layoutmanager.CoInterval.remove(e.getIntervalls(),rectIntervall));
			}	
		}
	}	
}


/**
 * Insert the method's description here.
 * Creation date: (2000-06-19 10:54:01)
 * @param list java.awt.List
 */
public void arange(List list) 
{
	java.util.Collections.sort(list,new CoLayoutableColumnComparator());	
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-15 15:07:14)
 * @return java.awt.geom.Point2D
 * @param rect com.bluebrim.layout.impl.shared.CoLayoutableIF
 */
public Point2D calculatePosition(CoLayoutableIF rect) 
{
	CoSolution choice=new CoSolution(),tempSolve;
	for(int i=0;i<columnArray.size();i++)
	{
		Iterator iter1=((List)columnArray.get(i)).iterator();
		double cost=Double.MAX_VALUE;
		while(iter1.hasNext())
		{
			com.bluebrim.layoutmanager.CoInterval ival=(com.bluebrim.layoutmanager.CoInterval)iter1.next();
			if(ival.getSize()<rect.getLayoutHeight())
				continue;
			cost=cost(i,ival,rect);
			tempSolve=new CoSolution(i,ival,cost);
			if(tempSolve.compareTo(choice)<=0)
				choice=tempSolve;
		}
	}
	
	Point2D point=null;
	if(choice.getColumn()!=-1)
	{			
		CoColumnElement e=(CoColumnElement)columns.get(choice.getColumn());
		point=new Point2D.Double(e.getX(),
			choice.getIntervall().getStop()-rect.getLayoutHeight());		
	}
	return point;
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-14 11:29:44)
 * @param rect com.bluebrim.layout.impl.shared.CoLayoutableIF
 */
public Point2D canPlace(CoLayoutableIF rect) 
{	
	/*- If first time or size_in_columns is not same as last*/
	//int rectColumnCount=(int)Math.ceil(rect.getLayoutWidth()/ columnWidth);
	int rectColumnCount=getRectColumnCount(rect);
	if((columnCount-columnArray.size()+1)!=rectColumnCount)
	{
		/*- Create lists for (columnCount-size_in_columns+1)*/
		columnArray.clear();
		for (int i = 0; i < (columnCount-rectColumnCount+1); i++)
		{		
			List freeList=new LinkedList();
			freeList.addAll( ((CoColumnElement)columns.get(i)).getIntervalls());
			for(int j=1;j<rectColumnCount;j++)
			{
				freeList=com.bluebrim.layoutmanager.CoInterval.merge(freeList,((CoColumnElement)columns.get(j+i)).getIntervalls()); 
			}
			columnArray.add(freeList);//Intervall.merge(freeList));
		}			
	}
	/*- For each intervall in lists compare and store best choice*/
	return calculatePosition(rect);		
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
	return Math.sqrt((Math.pow((columnCount-(col+1))*columnWidth,2)+
			Math.pow(columnHeight-intervall.getStop(),2)));
}
/**
 * Calculates the cost for placing the 
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
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-20 14:22:41)
 * @return int
 * @param p java.awt.geom.Point2D
 */
public int getColumn(Point2D p) 
{
	Iterator iter=columns.iterator();
	int count=-1;
	while(iter.hasNext())	
	{		
		CoColumnElement co=(CoColumnElement)iter.next();
		count++;
		if( (p.getX()>=(co.getX()-0.5)) &&
				(p.getX()<=(co.getX()+co.getWidth()+0.5)) )		
			return count;						
	}
	return -1;
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-20 13:58:46)
 * @return int
 */
public int getColumnCount() {
	return columnCount;
}
/**
 * Insert the method's description here.
 * Creation date: (2000-06-20 10:24:31)
 * @return int
 */
protected int getRectColumnCount(CoLayoutableIF rect) 
{	
	double tmp=( rect.getLayoutWidth()/ 
		(grid.getColumnWidth()+grid.getColumnSpacing()) );
	return (int)Math.ceil(tmp);
}
/**
 * Updates the free intervall list by removing rect at p
 * Creation date: (2000-06-14 15:21:10)
 * @param p java.awt.geom.Point2D
 * @param rect com.bluebrim.layout.impl.shared.CoLayoutableIF
 */
public void place(Point2D p, CoLayoutableIF rect) 
{
	com.bluebrim.layoutmanager.CoInterval rectinter=new com.bluebrim.layoutmanager.CoInterval(rect.getY(),rect.getY()+rect.getLayoutHeight());
	// getColumn return in witch column p is
	int column=getColumn(p);
	int rectColumnCount=getRectColumnCount(rect);//(int)Math.ceil(rect.getLayoutWidth()/ columnWidth);	
	// Change all the efected columnsets from curent-(rectColumnCount-1) to curent+(rectColumnCount-1)
	for(int i=(i=column-rectColumnCount+1)<0?0:i;
		i<(column+rectColumnCount) && 
			i<(columnCount-rectColumnCount+1);i++)			
	{
		columnArray.set(i,com.bluebrim.layoutmanager.CoInterval.remove((List)columnArray.get(i),rectinter));			
	}
	// do this one time for each column that is covered with rect
	for(int i = 0; i < rectColumnCount; i++)
	{
		CoColumnElement e=((CoColumnElement)columns.get(column+i));
		e.setIntervalls(com.bluebrim.layoutmanager.CoInterval.remove(e.getIntervalls(),rectinter));		
	}
}
}
