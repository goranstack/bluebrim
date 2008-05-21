package com.bluebrim.layoutmanager;

import java.awt.geom.*;
import java.util.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
/**
 * @author Mats Åström (2000-05-19 11:26:35)
 */
public class LayoutManager {
/**
 * LayoutManager constructor comment.
 */
public LayoutManager() {
	super();
}
/**
 * @author Mats Åström (2000-05-19 11:27:45)
 * @param container com.bluebrim.layout.impl.shared.CoLayoutableContainerIF
 */
public static void layout(CoLayoutableContainerIF container) 
{	
	RectangularCollection collection=new RectangularCollection();

	Iterator iter=container.getLayoutChildren().iterator();
	while(iter.hasNext())
	{
		collection.addElement((CoLayoutableIF)iter.next());
	}
	
	//RectangularContainer newContainer=new RectangularContainer(container, collection);
	GridArrangement arr=new GridArrangement();
	
	SolutionConfig config=(SolutionConfig)arr.place(container,collection,new MrConstraint(100, 10, 10, 100, 100),GridArrangement.BOTTOM_RIGHT);
	Vector locations=config.getLocations();
	MrCollection coll=config.getContainer().getCollection();
	
	for (int i = 0; i < locations.size(); i++)
	{
		((CoLayoutableIF)coll.getElement(i)).setLayoutX( ((Point2D)locations.elementAt(i)).getX());
		((CoLayoutableIF)coll.getElement(i)).setLayoutY( ((Point2D)locations.elementAt(i)).getY());	
		
	}
	for (int i = locations.size(); i< coll.count(); i++)
	{
		((CoLayoutableIF)coll.getElement(i)).setLayoutX(0);
		((CoLayoutableIF)coll.getElement(i)).setLayoutY(container.getLayoutHeight()-((CoLayoutableIF)coll.getElement(i)).getLayoutHeight());
	}
}
}
