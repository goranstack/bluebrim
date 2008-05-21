package com.bluebrim.layoutmanager;

/**
 * Contains the methods nessery to place a rectangle in a
 * larger rectangle with other objects in the way.
 * Creation date: (2000-07-12 15:49:56)
 * @author: Arvid Berg & Masod Jalalian 
 */
import java.awt.geom.*;
import java.util.*;

public class CoRectanglePlacement {
/**
 * CoRectanglePlacement constructor comment.
 */
public CoRectanglePlacement() {
	super();
}
/**
 * Chose a point among the points which has the 
 * smalest distance to corner calculated with dist.calculateDistance() 
 * Creation date: (2000-07-13 09:51:09)
 * @return java.awt.geom.Point2D
 * @param rect java.awt.geom.Rectangle2D
 * @param points java.util.Collection
 * @param corner java.awt.geom.Point2D
 * @param align int
 */
public Point2D choseBestPoint(Rectangle2D rect, Collection points, 
							  Point2D corner, int align, com.bluebrim.layout.impl.shared.layoutmanager.CoCalculateDistanceIF dist) 
{
	Point2D place=new Point2D.Double(-1,-1);
	double value=Double.MAX_VALUE;
	double widthAdj=0,heightAdj=0;					
	Point2D disp=CoCornerUtilities.calculateDisplacement(align);
	widthAdj=disp.getX();
	heightAdj=disp.getY();
	double tmp;
	Iterator iter=points.iterator();
	while(iter.hasNext())
	{		
		Point2D tP=(Point2D) iter.next();		
		Point2D tmpPoint=new Point2D.Double(tP.getX()+(widthAdj<0?-1:1)*rect.getWidth(),
							                tP.getY()+(heightAdj<0?-1:1)*rect.getHeight());
		tmp=dist.calculateDistance(tmpPoint,corner);

		if(tmp<value && CoCornerUtilities.areEqual(align,CoCornerUtilities.LEFT))
		{
			value=tmp;			
			place.setLocation(tP.getX()+widthAdj*rect.getWidth(),
				tP.getY()+heightAdj*rect.getHeight());
		}
		else
		if(tmp<=value && CoCornerUtilities.areEqual(align,CoCornerUtilities.RIGHT))
		{
			value=tmp;			
			place.setLocation(tP.getX()+widthAdj*rect.getWidth(),
				tP.getY()+heightAdj*rect.getHeight());
		}	
	}
	return place;
}
/**
 * Takes a Rectangle2D object and return a list
 * with rectangles covering the area of container but not rect.
 * Creation date: (2000-07-03 14:47:57)
 * @return java.util.List
 * @param lockedRect Rectanglel2D the rectangle that covers the container
 * @param container Rectangle2D the bounding rect
 */
public List getFreeRects(Rectangle2D rect,Rectangle2D container) 
{	
	List resultList=new LinkedList();
	Point2D curent=new Point2D.Double(container.getX(),container.getY()),//0,0
			pos=new Point2D.Double();//0,0
	double width=0,height=0;
	/////////////////////////// Start ///////////////
	// left
	width=Math.abs(rect.getX()-curent.getX());//0,0
	height=Math.abs(container.getHeight());
	pos.setLocation(curent);
	//curent.setLocation(rect.getX(),container.getY());
	Rectangle2D outRect=new Rectangle2D.Double(pos.getX(),pos.getY(),width,height);
	if(!outRect.isEmpty())
		resultList.add(outRect);
	// top
	width=Math.abs(container.getWidth());
	height=Math.abs(rect.getY()-curent.getY());
	pos.setLocation(curent);
	//curent.setLocation(curent.getX(),rect.getY()+rect.getHeight());
	outRect=new Rectangle2D.Double(pos.getX(),pos.getY(),width,height);
	if(!outRect.isEmpty())
		resultList.add(outRect);		
	// bottom
	width=Math.abs(container.getWidth());
	height=Math.abs(container.getY()+container.getHeight()-(rect.getY()+rect.getHeight()));
	pos.setLocation(curent.getX(),rect.getY()+rect.getHeight());
	//curent.setLocation(rect.getX()+rect.getWidth(),container.getY());
	outRect=new Rectangle2D.Double(pos.getX(),pos.getY(),width,height);
	if(!outRect.isEmpty())
		resultList.add(outRect);
	// right
	width=Math.abs(container.getX()+container.getWidth()-(rect.getX()+rect.getWidth()));
	height=Math.abs(container.getHeight());
	pos.setLocation(rect.getX()+rect.getWidth(),curent.getY());
	curent.setLocation(rect.getX()+rect.getWidth(),container.getY());
	outRect=new Rectangle2D.Double(pos.getX(),pos.getY(),width,height);
	if(!outRect.isEmpty())
		resultList.add(outRect);
	return resultList;
}
/**
 * Gets free rectangles that can container rect
 * Creation date: (2000-07-04 09:39:52)
 * @return java.util.List
 * @param rect java.awt.geom.Rectangle2D
 * @param collection java.util.Collection collection of free space to use
 */
public List getFreeRects(Rectangle2D rect, List collection) 
{
	List l=new LinkedList();
	Iterator it=collection.iterator();
	while(it.hasNext())
	{
		Rectangle2D re=(Rectangle2D)it.next();		
		Rectangle2D boundRect=new Rectangle2D.Double();
		boundRect.setRect(re);
		if(boundRect.getWidth()>=rect.getWidth() && 
		  boundRect.getHeight()>=rect.getHeight())
			l.add(boundRect);		
	}
	return l;
}
/**
 * For each rectangle in rects calculate whitch point to be used acording to aligment.
 * Creation date: (2000-07-11 09:03:50)
 * @return java.util.List
 * @param java.util.Collection rects
 * @param int align
 */
public List getPoints(Collection rects, int align) 
{
	List resultList=new LinkedList();
	Iterator iter=rects.iterator();
	while(iter.hasNext())
	{
		Rectangle2D rect=(Rectangle2D)iter.next();
		Point2D rp=new Point2D.Double();
		switch(align)
		{
			case CoCornerUtilities.BOTTOM_LEFT:
				rp.setLocation(rect.getX(),rect.getY()+rect.getHeight());
				break;		
			case CoCornerUtilities.BOTTOM_RIGHT:
				rp.setLocation(rect.getX()+rect.getWidth(),rect.getY()+rect.getHeight());
				break;
			case CoCornerUtilities.TOP_LEFT:
				rp.setLocation(rect.getX(),rect.getY());
				break;
			case CoCornerUtilities.TOP_RIGHT:
				rp.setLocation(rect.getX()+rect.getWidth(),rect.getY());
				break;
		}
		resultList.add(rp);
	}
	return resultList;
}
/**
 * For each rectangle in rects calculate whitch point to be used acording to aligment.
 * Creation date: (2000-07-11 09:03:50)
 * @return java.util.List
 * @param java.util.Collection rects
 * @param int align
 */
public List getPointsWithGap(Collection rects, int align) 
{
	List resultList=new LinkedList();
	double gap=20;
	if(rects.size()==1)
		gap=0;
	Iterator iter=rects.iterator();
	while(iter.hasNext())
	{
		Rectangle2D rect=(Rectangle2D)iter.next();
		Point2D rp=new Point2D.Double();
		switch(align)
		{
			case CoCornerUtilities.BOTTOM_LEFT:
				rp.setLocation(rect.getX(),rect.getY()+rect.getHeight()-gap);
				break;		
			case CoCornerUtilities.BOTTOM_RIGHT:
				rp.setLocation(rect.getX()+rect.getWidth(),rect.getY()+rect.getHeight()-gap);
				break;
			case CoCornerUtilities.TOP_LEFT:
				rp.setLocation(rect.getX(),rect.getY()+gap);
				break;
			case CoCornerUtilities.TOP_RIGHT:
				rp.setLocation(rect.getX()+rect.getWidth(),rect.getY()+gap);
				break;
		}
		resultList.add(rp);
	}
	return resultList;
}
/**
 * For each rectangle in rects calculate whitch point to be used acording to aligment.
 * Creation date: (2000-07-11 09:03:50)
 * @return java.util.List
 * @param java.util.Collection rects
 * @param int align
 */
public List getPointsWithGap(Collection rects, int align, double tGap, Rectangle2D container) 
{
	List resultList=new LinkedList();
	double gap=tGap;
	
	Iterator iter=rects.iterator();
	while(iter.hasNext())
	{
		gap=tGap;
		Rectangle2D rect=(Rectangle2D)iter.next();
		Point2D rp=new Point2D.Double();
		switch(align)
		{
			case CoCornerUtilities.BOTTOM_LEFT:
				if(rect.getMaxY()==container.getMaxY())
					gap=0;
				rp.setLocation(rect.getX(),rect.getY()+rect.getHeight()-gap);
				break;		
			case CoCornerUtilities.BOTTOM_RIGHT:
				if(rect.getMaxY()==container.getMaxY())
					gap=0;
				rp.setLocation(rect.getX()+rect.getWidth(),rect.getY()+rect.getHeight()-gap);
				break;
			case CoCornerUtilities.TOP_LEFT:
				if((rect.getMinY())==container.getMinY())
					gap=0;
				rp.setLocation(rect.getX(),rect.getY()+gap);
				break;
			case CoCornerUtilities.TOP_RIGHT:
				if(rect.getMinY()==container.getMinY())
					gap=0;
				rp.setLocation(rect.getX()+rect.getWidth(),rect.getY()+gap);
				break;
		}
		resultList.add(rp);
	}
	return resultList;
}
/**
 * Removes all smaler rectangles that covers the same area as a biger.
 * Creation date: (2000-07-05 12:12:49)
 * @return java.util.List
 * @param collection java.util.Collection
 */
public List reduceRectangles(Collection collection) 
{
	List l=new LinkedList(collection);
		java.util.Iterator it=collection.iterator();
		while(it.hasNext())
		{
			Rectangle2D re=(Rectangle2D)it.next();
			List remove=new java.util.LinkedList();
			Iterator iter=l.iterator();
			while(iter.hasNext())
			{			
				Rectangle2D re2=(Rectangle2D)iter.next();			
				if(re2.equals(re)) continue;
				if(re.contains(re2))
					remove.add(re2);			
			}		
			l.removeAll(remove);
		}		
	return l;
}
/**
 * Removes the rectangles that intersect inRect and replace them with
 * smaler rectangles that does't covers inRect.
 * Creation date: (2000-07-04 15:02:26)
 * @return java.util.List
 * @param rect java.awt.geom.Rectangle2D
 * @param list java.util.List
 */
public List removeRect(Rectangle2D inRect, List list) 
{	
	List resultList = new LinkedList();
	Iterator iter = list.iterator();
	while(iter.hasNext())
	{
		Rectangle2D testRect=(Rectangle2D)iter.next();
		Rectangle2D part=inRect.createIntersection(testRect);
		if(inRect.intersects(testRect))
		{			
			resultList.addAll(getFreeRects(part,testRect));
		}
		else
			resultList.add(testRect);
	}
	return resultList;
}
}
