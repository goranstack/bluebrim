package com.bluebrim.layoutmanager.test;

import java.awt.geom.*;
import java.util.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.layoutmanager.*;

public class Transform {

	public static Vector transform(boolean xtransform, boolean ytransform,
							int trgWidth, int trgHeight, MrCollection coll,
							Vector current) {

	   if((xtransform == false) && (ytransform == false)) {
		   return (Vector)current.clone();
	   } // if

	   Vector locations = new Vector();
	   for(int i = 0; i < current.size(); i++) {
		   double width = ((CoLayoutableIF)coll.getElement(i)).getLayoutWidth();
		   double height = ((CoLayoutableIF)coll.getElement(i)).getLayoutHeight();
		   Point2D p = new Point2D.Double( ((Point2D)current.elementAt(i)).getX(),((Point2D)current.elementAt(i)).getY());
		   if(xtransform == true) {
				p.setLocation(trgWidth - p.getX()-width,p.getY());
			} // if

		   if(ytransform == true) {
			  //System.out.println("Transforming along y-axis");
			  //System.out.println("Y value:" + p.getY());
			  p.setLocation(p.getX(),trgHeight - p.getY()-height);
			} // if
		   locations.addElement(p);
	   } // for
	   return locations;
	} // transform
} // class Transform
