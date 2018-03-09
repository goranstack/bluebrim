package com.bluebrim.layoutmanager;

/**
 * @author Arvid Berg & Masod Jalalian  (2000-05-19 11:26:35)
 */
import java.awt.geom.*;
import java.util.*;

import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.layout.shared.*;

public class CoAdPlacementLayoutManager extends CoLayoutManager implements CoAdPlacementLayoutManagerIF
{
/**
 * LayoutManager constructor comment.
 */
public CoAdPlacementLayoutManager() {
	super();
}
/**
 * doesSetSize method comment.
 */
public boolean doesSetSize() {
	return false;
}
/**
 * getFactoryKey method comment.
 */
public java.lang.String getFactoryKey() {
	return AD_PLACEMENT_LAYOUT_MANAGER;
}
/**
 * @author Arvid Berg & Masod Jalalian  (2000-05-19 11:27:45)
 * @param container com.bluebrim.layout.impl.shared.CoLayoutableContainerIF
 */
public void layout(CoLayoutableContainerIF container) 
{	
	RectangularCollection collection=new RectangularCollection();

	Iterator iter=container.getLayoutChildren().iterator();
	while(iter.hasNext())
	{
		CoLayoutableIF co=(CoLayoutableIF)iter.next();
		co.setLayoutSuccess(false);
		collection.addElement(co);
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
		((CoLayoutableIF)coll.getElement(i)).setLayoutSuccess(true);
		
	}
	for (int i = locations.size(); i< coll.count(); i++)
	{
		((CoLayoutableIF)coll.getElement(i)).setLayoutSuccess(false);

	}
}

	public final static String XML_TAG = "ad-placement-layout-manager";

public String xmlGetTag()
{
	return XML_TAG;
}

public String getLocalizedName()
{
    return CoExJobbLayoutManagerResources.getName(AD_PLACEMENT_LAYOUT_MANAGER);
}

}