package com.bluebrim.layoutmanager;

import java.awt.geom.*;
import java.util.*;

import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;


public class GridArrangement extends Arrangement {

	/**
	 * Returns whether we can place the current rectangle into the configuration
	 * at some location
	 * @param config  the current configuration
	 * @param rect   the rectangle that is to be placed into the config
	 * @return boolean  indicates whether we can place the rectangle
	 *                  into the target or not
	 */

	protected boolean canPlace(SolutionConfig config, CoLayoutableIF rect) {

		Vector periphery = config.getPeriphery();
		double width = rect.getLayoutWidth(), height = rect.getLayoutHeight();
		Point2D p1, p2, p3;

	   // check which corner is selected and return the locations where
	   // we can place the current rectangle
		switch(corner) {

			case BOTTOM_RIGHT:
				return super.canPlace(config, rect);

			case BOTTOM_LEFT:

				for(int j=0; j < periphery.size()-2; j+=2) {
					p1 = (Point2D)periphery.elementAt(j);
					p2 = (Point2D)periphery.elementAt(j+1);
					p3 = (Point2D)periphery.elementAt(j+2);
				   // check if the rectangle can be fit in the current location
				   if((height <= (p1.getY() - p2.getY())) &&
					  (width <= (p3.getX() - p2.getX()))) {
					 // if so, then add it to the location vector
					 return true;
				   } // if
				} // for

				break;

			case TOP_LEFT :

				for(int j=0; j < periphery.size()-2; j+=2) {
					p1 = (Point2D)periphery.elementAt(j);
					p2 = (Point2D)periphery.elementAt(j+1);
					p3 = (Point2D)periphery.elementAt(j+2);
				   // check if the rectangle can be fit in the current location
				   if((height <= (p2.getY() - p1.getY())) &&
					  (width <= (p3.getX() - p2.getX()))) {
					 // if so, then add it to the location vector
					 return true;
				   } // if
				} // for

				break;

			case TOP_RIGHT:

				for(int j=0; j < periphery.size()-2; j+=2) {
					p1 = (Point2D)periphery.elementAt(j);
					p2 = (Point2D)periphery.elementAt(j+1);
					p3 = (Point2D)periphery.elementAt(j+2);
				   // check if the rectangle can be fit in the current location
				   if((height <= (p3.getY() - p2.getY())) &&
					  (width <= (p2.getX() - p1.getX()))) {
					 // if so, then add it to the location vector
					 return true;
				   } // if
				} // for

				break;

			default:
			   System.out.println("Fatal Error: Invalid code received in canPlace.");
		} // switch

		return false;
	} // canPlace()
	/**
	 * Computes the location where the chosen rectangle is to be placed
	 * @param temp  the current configuration
	 * @param rect  the rectangle that is to be placed
	 * @param p  the location of the corner where the rectangle is to be placed
	 */

	protected Point2D computePlacingPosition(SolutionConfig current, CoLayoutableIF rect, Point2D p) {
		Point2D position = null;

		switch(corner) {

			case BOTTOM_LEFT:
				//System.out.println("In derived...computePlacing..");
				position = computePlacingPositionBottomLeft(current, rect, p);
				break;

			case BOTTOM_RIGHT:
				position = computePlacingPositionBottomRight(current, rect, p);
				break;

			case TOP_LEFT:
				position = computePlacingPositionTopLeft(current, rect, p);
				break;

			case TOP_RIGHT:
				position = computePlacingPositionTopRight(current, rect, p);
				break;

			default:
				System.out.println("Invalid position in generateNewPeriphery..");
		} // switch

		return position;
	}
	/**
	 * Computes the location where the chosen rectangle is to be placed
	 * @param temp  the current configuration along the BOTTOM LEFT corner
	 * @param rect  the rectangle that is to be placed
	 * @param p  the location of the corner where the rectangle is to be placed
	 */


	protected Point2D computePlacingPositionBottomLeft (SolutionConfig current, CoLayoutableIF rect, Point2D p) {
		return p;
	} // computePlacingPositionBottomLeft()
	/**
	 * Computes the location where the chosen rectangle is to be placed
	 * @param temp  the current configuration along the BOTTOM RIGHT corner
	 * @param rect  the rectangle that is to be placed
	 * @param p  the location of the corner where the rectangle is to be placed
	 */


	protected Point2D computePlacingPositionBottomRight (SolutionConfig current, CoLayoutableIF rect, Point2D p) {
		return super.computePlacingPosition(current, rect, p);
	} // computePlacingPositionBottomRight()
	/**
	 * Computes the location where the chosen rectangle is to be placed
	 * @param temp  the current configuration along the TOP LEFT corner
	 * @param rect  the rectangle that is to be placed
	 * @param p  the location of the corner where the rectangle is to be placed
	 */

	protected Point2D computePlacingPositionTopLeft (SolutionConfig current, CoLayoutableIF rect, Point2D p) {
		return new Point2D.Double(p.getX(), p.getY() - rect.getLayoutHeight());
	} // computePlacingPositionTopLeft()
	/**
	 * Computes the location where the chosen rectangle is to be placed
	 * @param temp  the current configuration along the TOP RIGHT corner
	 * @param rect  the rectangle that is to be placed
	 * @param p  the location of the corner where the rectangle is to be placed
	 */

	protected Point2D computePlacingPositionTopRight (SolutionConfig current, CoLayoutableIF rect, Point2D p) {
		return new Point2D.Double(p.getX() - rect.getLayoutWidth(), p.getY() - rect.getLayoutHeight());
	} // computePlacingPositionTopRight()
	/**
	 * Computes the distance of all the collection rectangles from
	 * bottom right corner
	 * @param current  the current configuration
	 * @param rect    the next rectangle that is to be placed into this config
	 * @param p     the location of the corner where the rectangle is to be placed
	 * @return long  the distance of all the rectangles in the collection and
	 *               the current one from the bottom-right corner
	 */

	protected double evaluateDistance(SolutionConfig current, CoLayoutableIF rect, Point2D p) {
		// the distance of all the points is the distance of the parent
		// plus the distance of the centre of the current rectangle from
		// the corner
		// first compute the centre of the given rectangle from the
		// bottom right corner
		double x, y, x1, y1, distance = 0;


		switch(corner) {

			case BOTTOM_RIGHT:
				return super.evaluateDistance(current, rect, p);

			case BOTTOM_LEFT:
				x = p.getX() + ((double)rect.getLayoutWidth())/2; // x coordinate of the centre
				y = p.getY() + ((double)rect.getLayoutHeight())/2;  // y coordinate of the centre
				distance = Math.sqrt(x*x + y*y); // distance from the bottom left corner
				break;

			case TOP_LEFT:
				x = p.getX() + ((double)rect.getLayoutWidth())/2; // x coordinate of the centre
				y = p.getY() - ((double)rect.getLayoutHeight())/2;  // y coordinate of the centre
				y1 = target.getLayoutHeight()- y;  // distance from the top left corner
				distance = Math.sqrt(x*x + y1*y1);
				break;

			case TOP_RIGHT:
				x = p.getX() - ((double)rect.getLayoutWidth())/2; // x coordinate of the centre
				y = p.getY() - ((double)rect.getLayoutHeight())/2;  // y coordinate of the centre
				x1 = target.getLayoutWidth() - x;
				y1 = target.getLayoutHeight()- y;  // distance from the tpo right corner
				distance = Math.sqrt(x1*x1 + y1*y1);
				break;

			default:
				System.out.println("Fatal Error: Illegal value for corner in evaluateDistance");

		} // switch

		//System.out.println("...computed distance is " + (distance + current.getDistance()));
		return distance + current.getDistance(); // total distance is the current
											 // plus that of the parent
	} // evaluateDistance()
	/**
	 * Evaluates the possible locations in the current configuration
	 * where we can place the given rectangle
	 * @param config  the current configuration into which the given
	 *                rectangle is to be placed
	 * @param rect   the rectangle that is to be placed
	 * @return Vector  the set of locations where we can place the rectangle
	 *                 into the configuration
	 */

	protected Vector evaluateLocations(SolutionConfig config, CoLayoutableIF rect) {

		Vector locations = null;
		Vector periphery = config.getPeriphery();
		double width = rect.getLayoutWidth(), height = rect.getLayoutHeight();
		Point2D p1, p2, p3;

	   // check which corner is selected and return the locations where
	   // we can place the current rectangle
		switch(corner) {

			case BOTTOM_RIGHT:
				return super.evaluateLocations(config, rect);

			case BOTTOM_LEFT:

				locations = new Vector();
				for(int j=0; j < periphery.size()-2; j+=2) {
					p1 = (Point2D)periphery.elementAt(j);
					p2 = (Point2D)periphery.elementAt(j+1);
					p3 = (Point2D)periphery.elementAt(j+2);
				   // check if the rectangle can be fit in the current location
				   if((height <= (p1.getY() - p2.getY())) &&
					  (width <= (p3.getX() - p2.getX()))) {
					 // if so, then add it to the location vector
					 locations.addElement(new Integer(j));
				   } // if
				} // for

				break;

			case TOP_LEFT :

				locations = new Vector();
				for(int j=0; j < periphery.size()-2; j+=2) {
					p1 = (Point2D)periphery.elementAt(j);
					p2 = (Point2D)periphery.elementAt(j+1);
					p3 = (Point2D)periphery.elementAt(j+2);
				   // check if the rectangle can be fit in the current location
				   if((height <= (p2.getY() - p1.getY())) &&
					  (width <= (p3.getX() - p2.getX()))) {
					 // if so, then add it to the location vector
					 locations.addElement(new Integer(j));
				   } // if
				} // for

				break;

			case TOP_RIGHT:

				locations = new Vector();
				for(int j=0; j < periphery.size()-2; j+=2) {
					p1 = (Point2D)periphery.elementAt(j);
					p2 = (Point2D)periphery.elementAt(j+1);
					p3 = (Point2D)periphery.elementAt(j+2);
				   // check if the rectangle can be fit in the current location
				   if((height <= (p3.getY() - p2.getY())) &&
					  (width <= (p2.getX() - p1.getX()))) {
					 // if so, then add it to the location vector
					 locations.addElement(new Integer(j));
				   } // if
				} // for

				break;

			default:
			   System.out.println("Fatal Error: Invalid code received in evaluateLocations.");
		} // switch

		return locations;
	} // evaluateLocations()
	/**
	 * Generates the new periphery for the resulting after placing the given
	 * rectangle into the current configuration along the BOTTOM LEFT corner
	 * @param periphery  the periphery for the current configuration
	 * @param rect   the rectangle that is to be into the configuration
	 * @param index  the index of the location in the periphery that
	 * @return Vector  the new periphery resulting after placing this rectangle
	 */

	protected Vector generateBottomLeftPeriphery(Vector periphery, CoLayoutableIF rect, int index) {

		double vertLeftWidth = constraint.getVerticalLeftWidth();
		double horzBottomWidth = constraint.getHorizontalBottomWidth();

		Vector newPeriphery = new Vector(periphery.size());
		// copy the points from the periphery to the new periphery
		copyPoints(newPeriphery, periphery);

		// get the points that will be altered
		Point2D p1 = (Point2D)periphery.elementAt(index);
		Point2D p2 = (Point2D)periphery.elementAt(index+1);
		Point2D p3 = (Point2D)periphery.elementAt(index+2);

		/* Here comes the code to adjust based on the constraints */
		// generate the new points as a result of placing this rectangle
		Point2D q1 = new Point2D.Double(p2.getX(), p2.getY() + rect.getLayoutHeight());
		Point2D q2 = new Point2D.Double(p2.getX() + rect.getLayoutWidth(), p2.getY() + rect.getLayoutHeight());
		Point2D q3 = new Point2D.Double(p2.getX() + rect.getLayoutWidth(), p2.getY());

		// adjust the periphery based on the boundary
		double newx, newy;



		   /* This is assuming it is required that no two rectangles should
		   touch each other */

		newx = (double)(((int)(q3.getX()/vertLeftWidth) + 1)*vertLeftWidth);
		newy = (double)(((int)(q1.getY()/horzBottomWidth) + 1)*horzBottomWidth);

		/*
		  This is assuming that if the rectangle boundaries match the
		  grids, then placing another rectangles to the side of it
		  is allowed. Just kept it for future use.

				 
		if(q3.getX()%((int)vertLeftWidth) == 0) {
			newx = q3.getX();
		} else {
			newx = (int)Math.round(((int)(q3.getX()/vertLeftWidth) + 1)*vertLeftWidth);
		}

		if(q1.getY()%((int)horzBottomWidth)== 0) {
			newy = q1.getY();
		} else {
			newy = (int)Math.round(((int)(q1.getY()/horzBottomWidth) + 1)*horzBottomWidth);
		}
		*/

		q1.setLocation(q1.getX(),newy);
		
		q2.setLocation(newx,newy);//setPoint(new Point(newx, newy));
		q3.setLocation(newx,q3.getY());

		Point2D r1 = null, r2 = null, r3 = null, r4 = null;

		if(q1.getX() + rect.getLayoutWidth() < q2.getX()) {
			r1 = new Point2D.Double(q1.getX() + rect.getLayoutWidth(), q2.getY());
			r2 = new Point2D.Double(r1.getX(),r1.getY());
		} // if

		if(rect.getLayoutHeight() + q3.getY() < q2.getY()) {
			r3 = new Point2D.Double(q2.getX(), q3.getY() + rect.getLayoutHeight());
			r4 = new Point2D.Double(r3.getX(),r3.getY());
		} // if

		int locn = index + 1;

		// overwrite the old element at index + 1 because
		// that wont be visible now
		newPeriphery.setElementAt(q1, locn++); // override old p2

		if(r1 != null) {
			newPeriphery.insertElementAt(r1, locn++);
			newPeriphery.insertElementAt(r2, locn++);
		} // if

		newPeriphery.insertElementAt(q2, locn++);

		if(r3 != null) {
			newPeriphery.insertElementAt(r3, locn++);
			newPeriphery.insertElementAt(r4, locn++);
		} // if

		newPeriphery.insertElementAt(q3, locn++);

		// since the elements are being removed from the end, the indexes
		// of the previous elements doesnt get disturbed
		if((locn + 1 < newPeriphery.size()) &&
				  (((Point2D)newPeriphery.elementAt(locn)).getX() < newx)) {
			newPeriphery.removeElementAt(locn+1);
			newPeriphery.removeElementAt(locn);

			// now point q3 is at location - 1
			if(locn + 1 < newPeriphery.size()) {
				Point2D s1 = (Point2D)newPeriphery.elementAt(locn);
				Point2D s2 = (Point2D)newPeriphery.elementAt(locn+1);
				if(q3.equals(s1) && q3.equals(s2)) {
					// merge the elements q3, s1, s2
					// dont swap the order of removal!!
					newPeriphery.removeElementAt(locn+1);
					newPeriphery.removeElementAt(locn);
				} // if
			} // if
		} else if((locn + 1 < newPeriphery.size()) &&
				  (((Point2D)newPeriphery.elementAt(locn)).getX() == newx)) {

			Point2D s1 = (Point2D)newPeriphery.elementAt(locn);
			Point2D s2 = (Point2D)newPeriphery.elementAt(locn+1);
			if(q3.equals(s1) && q3.equals(s2)) {
				// merge the elements q3, s1, s2
				// dont swap the order of removal
				newPeriphery.removeElementAt(locn+1);
				newPeriphery.removeElementAt(locn);
			} // if
		} // if-else

		// dont move this up, because the elements are being removed
		// from the middle (i.e. index), so the locations of the subsequent
		// points will get affected
		if((index > 0) && (((Point2D)newPeriphery.elementAt(index)).getY() < newy)) {
			newPeriphery.removeElementAt(index);
			newPeriphery.removeElementAt(index - 1);

			index--; // so now point q1 is at location index + 1
			if(index > 0) {
				Point2D s1 = (Point2D)newPeriphery.elementAt(index);
				Point2D s2 = (Point2D)newPeriphery.elementAt(index-1);
				if(q1.equals(s1) && q1.equals(s2)) {
					// merge the elements q1, s1, s2
					// dont swap the order of removal
					newPeriphery.removeElementAt(index);
					newPeriphery.removeElementAt(index-1);
				} // if
			} // if
		} else if((index > 0) &&
				(((Point2D)newPeriphery.elementAt(index)).getY() == newy)) {
			Point2D s1 = (Point2D)newPeriphery.elementAt(index);
			Point2D s2 = (Point2D)newPeriphery.elementAt(index-1);
			if(q1.equals(s1) && q1.equals(s2)) {
				// merge the elements q1, s1, s2
				// dont swap the order of removal
				newPeriphery.removeElementAt(index);
				newPeriphery.removeElementAt(index-1);
			} // if
		} // if-else

		return newPeriphery;
	} // generateBottomLeftPeriphery()
	/**
	 * Generates the new periphery for the resulting after placing the given
	 * rectangle into the current configuration along the BOTTOM RIGHT corner
	 * @param periphery  the periphery for the current configuration
	 * @param rect   the rectangle that is to be into the configuration
	 * @param index  the index of the location in the periphery that
	 * @return Vector  the new periphery resulting after placing this rectangle
	 */

	protected Vector generateBottomRightPeriphery(Vector periphery, CoLayoutableIF rect, int index) {

		double vertRightWidth = constraint.getVerticalRightWidth();
		double horzBottomWidth = constraint.getHorizontalBottomWidth();

		Vector newPeriphery = new Vector(periphery.size());
		// copy the points from the periphery to the new periphery
		copyPoints(newPeriphery, periphery);

		// get the points that will be altered
		Point2D p1 = (Point2D)periphery.elementAt(index);
		Point2D p2 = (Point2D)periphery.elementAt(index+1);
		Point2D p3 = (Point2D)periphery.elementAt(index+2);

		//super.generateNewPeriphery(periphery, rect, index);

		/* Here comes the code to adjust based on the constraints */
		// generate the new points as a result of placing this rectangle
		Point2D q1 = new Point2D.Double(p2.getX() - rect.getLayoutWidth(), p2.getY());
		Point2D q2 = new Point2D.Double(p2.getX() - rect.getLayoutWidth(), p2.getY() + rect.getLayoutHeight());
		Point2D q3 = new Point2D.Double(p2.getX(), p2.getY() + rect.getLayoutHeight());

		// overwrite the old element at index + 1 because that
		// wont be visible now

		double newx, newy; 

		 /* This is assuming that it is required that no two rectangles
			should touch each other */
			
/* Snap the point to the grid*/
// ta bort komentarer när testkör i appController
/*
		newx = target.getLayoutWidth() - (double)(((int)((target.getLayoutWidth() - q1.getX()) / vertRightWidth) + 1)*vertRightWidth);
		newy = (double)(((int)(q2.getY()/horzBottomWidth) + 1)*horzBottomWidth);
//*/
// Komentera bort när testkör i appController
//*
		Point2D d=new Point2D.Double(Double.MAX_VALUE,0), p;
		p=target.getColumnGrid().snap( q1.getX(),q2.getY(), Double.MAX_VALUE, CoGeometryConstants.RIGHT_EDGE_MASK, CoGeometryConstants.ALL_DIRECTIONS_MASK, true, d );
		//p=target.getColumnGrid().snap( q1.getX(),q2.getY(), rect.getLayoutWidth(), rect.getLayoutHeight(), Double.MAX_VALUE, d );		
		newx=p.getX();
		newy=p.getY();
	// if overlay move away	
		if(newx > q1.getX()) 
			newx=q1.getX();
		if(newy > q2.getY()) 
			newy=q2.getY();
// slut bort komentar */
		
		
		/*
		   This is assuming that if the rectangle boundaries match the
		   grids, then placing another rectangles to the side of it
		   is allowed

		if(q1.getX() % vertRightWidth == 0) {
			newx = q1.getX();
		}  else {
			newx = target.getWidth() - (int)Math.round(((int)(target.getWidth()-q1.getX())/vertRightWidth + 1)*vertRightWidth);
		} // if-else

		if(q2.getY() % horzBottomWidth == 0) {
			newy = q2.getY();
		} else {
			newy = (int)Math.round(((int)(q2.getY()/horzBottomWidth) + 1)*horzBottomWidth);
		} // if-else

	   */

		q1.setLocation(newx,q1.getY());
		q2.setLocation(newx,newy);//Point(new Point(newx, newy));
		q3.setLocation(q3.getX(),newy);

		//Point2D r1 = null, r2 = null, r3 = null, r4 = null;

		/*
		if(q3.getX() - rect.getLayoutWidth() > q2.getX()) {
			r1 = new Point2D.Double(q3.getX() - rect.getLayoutWidth(), q3.getY());		
		   r2 = new Point2D.Double(r1.getX(),r1.getY());
		} // if

		if(q1.getY() + rect.getLayoutHeight() < q3.getY()) {
			r3 = new Point2D.Double(q1.getX(), q1.getY() + rect.getLayoutHeight());
			r4 = new Point2D.Double(r3.getX(),r3.getY());
		} // if
		*/
		int locn = index + 1;

		// overwrite the old element at index + 1 because
		// that wont be visible now
		newPeriphery.setElementAt(q1, locn++); // override old p2

		/*
		if(r3 != null) {
			newPeriphery.insertElementAt(r3, locn++);
			newPeriphery.insertElementAt(r4, locn++);
		} // if
		*/
		newPeriphery.insertElementAt(q2, locn++);
		/*		
		if(r1 != null) {
			newPeriphery.insertElementAt(r1, locn++);
			newPeriphery.insertElementAt(r2, locn++);
		} // if
		*/
		newPeriphery.insertElementAt(q3, locn++);

		if((locn + 1 < newPeriphery.size()) &&
				   (((Point2D)newPeriphery.elementAt(locn)).getY() < newy)) {
			newPeriphery.removeElementAt(locn+1);
			newPeriphery.removeElementAt(locn);

			if(locn + 1 < newPeriphery.size()) {
				Point2D s1 = (Point2D)newPeriphery.elementAt(locn);
				Point2D s2 = (Point2D)newPeriphery.elementAt(locn+1);
				if(q3.equals(s1) && q3.equals(s2)) {
					// do not swap the order
					newPeriphery.removeElementAt(locn+1);
					newPeriphery.removeElementAt(locn);
				} // if
			} // if
		} else if((locn + 1 < newPeriphery.size()) &&
				 (((Point2D)newPeriphery.elementAt(locn)).getY() == newy)) {

			Point2D s1 = (Point2D)newPeriphery.elementAt(locn);
			Point2D s2 = (Point2D)newPeriphery.elementAt(locn+1);
			if(q3.equals(s1) && q3.equals(s2)) {
				// do not swap the order
				newPeriphery.removeElementAt(locn+1);
				newPeriphery.removeElementAt(locn);
			} // if
		} // if-else

		if((index > 0) && (((Point2D)newPeriphery.elementAt(index)).getX() > newx)) {
			// do not swap the order
			newPeriphery.removeElementAt(index);
			newPeriphery.removeElementAt(index-1);

			index--;
			if(index > 0) {
				Point2D s1 = (Point2D)newPeriphery.elementAt(index);
				Point2D s2 = (Point2D)newPeriphery.elementAt(index - 1);
				if(q1.equals(s1) && q1.equals(s2)) {
					newPeriphery.removeElementAt(index);
					newPeriphery.removeElementAt(index-1);
				} // if
			} // if
		} else if((index > 0) && (((Point2D)newPeriphery.elementAt(index)).getX() == newx)) {

			Point2D s1 = (Point2D)newPeriphery.elementAt(index);
			Point2D s2 = (Point2D)newPeriphery.elementAt(index - 1);

			if(q1.equals(s1) && q1.equals(s2)) {
				newPeriphery.removeElementAt(index);
				newPeriphery.removeElementAt(index-1);
			} // if
		} // if-else

		return newPeriphery;
	} // generateBottomRightPeriphery()
	/**
	 * Generates the new periphery for the resulting after placing the given
	 * rectangle into the current configuration
	 * @param periphery  the periphery for the current configuration
	 * @param rect   the rectangle that is to be into the configuration
	 * @param index  the index of the location in the periphery that
	 * @return Vector  the new periphery resulting after placing this rectangle
	 */

	protected Vector generateNewPeriphery(Vector periphery, CoLayoutableIF rect, int index) {

		Vector newPeriphery = null;

		// check the current corner and appropriately return the next co-ordinate
		switch(corner) {

			case BOTTOM_LEFT:
				newPeriphery = generateBottomLeftPeriphery(periphery, rect, index);
				break;

			case BOTTOM_RIGHT:
				newPeriphery = generateBottomRightPeriphery(periphery, rect, index);
				break;

			case TOP_LEFT:
				newPeriphery = generateTopLeftPeriphery(periphery, rect, index);
				break;

			case TOP_RIGHT:
				newPeriphery = generateTopRightPeriphery(periphery, rect, index);
				break;

			default:
				System.out.println("Invalid position in generateNewPeriphery..");
		} // switch
		removeMidelPoints(newPeriphery);
		return newPeriphery;
	} // generateNewPeriphery()
	/**
	 * Generates the new periphery for the resulting after placing the given
	 * rectangle into the current configuration along the TOP LEFT corner
	 * @param periphery  the periphery for the current configuration
	 * @param rect   the rectangle that is to be into the configuration
	 * @param index  the index of the location in the periphery that
	 * @return Vector  the new periphery resulting after placing this rectangle
	 */


	protected Vector generateTopLeftPeriphery(Vector periphery, CoLayoutableIF rect, int index) {

		/*
		  This method is not being implemented completely because
		  we can use generateBottomLeftPeriphery to generate the config
		  and then transform it along the x-axis to get the configuration
		  for Top Left

		  So care should be taken to call generateBottomLeftPeriphery() from
		  outside even when top left is required, and then transformation should
		  be applied
		 */

		double vertLeftWidth = constraint.getVerticalLeftWidth();
		double horzTopWidth = constraint.getHorizontalTopWidth();

		Vector newPeriphery = new Vector(periphery.size());
		// copy the points from the periphery to the new periphery
		copyPoints(newPeriphery, periphery);

		// get the points that will be altered
		Point2D p1 = (Point2D)periphery.elementAt(index);
		Point2D p2 = (Point2D)periphery.elementAt(index+1);
		Point2D p3 = (Point2D)periphery.elementAt(index+2);

		/* Here comes the code to adjust based on the constraints */
		// generate the new points as a result of placing this rectangle
		Point2D q1 = new Point2D.Double(p2.getX(), p2.getY() - rect.getLayoutHeight());
		Point2D q2 = new Point2D.Double(p2.getX() + rect.getLayoutWidth(), p2.getY() - rect.getLayoutHeight());
		Point2D q3 = new Point2D.Double(p2.getX() + rect.getLayoutWidth(), p2.getY());

		// insert the new points at the locations index + 1,
		// index + 2 and index + 3
		// overwrite the old element at index + 1 because that wont be visible now
		newPeriphery.setElementAt(q1, index+1); // override old p2
		newPeriphery.insertElementAt(q2, index+2);
		newPeriphery.insertElementAt(q3, index+3);

		return newPeriphery;
	} // generateTopLeftPeriphery()
	/**
	 * Generates the new periphery for the resulting after placing the given
	 * rectangle into the current configuration along the TOP RIGHT corner
	 * @param periphery  the periphery for the current configuration
	 * @param rect   the rectangle that is to be into the configuration
	 * @param index  the index of the location in the periphery that
	 * @return Vector  the new periphery resulting after placing this rectangle
	 */


	protected Vector generateTopRightPeriphery(Vector periphery, CoLayoutableIF rect, int index) {

		/*
		  This method is not being implemented completely because
		  we can use generateBottomRightPeriphery to generate the config
		  and then transform it along the x-axis to get the configuration
		  for Top Left

		  So care should be taken to call generateBottomrRightPeriphery()
		  from outside even when top left is required, and then transformation
		  should be applied
		 */

	   double vertRightWidth = constraint.getVerticalRightWidth();
	   double horzTopWidth = constraint.getHorizontalTopWidth();

		Vector newPeriphery = new Vector(periphery.size());
		// copy the points from the periphery to the new periphery
		copyPoints(newPeriphery, periphery);

		// get the points that will be altered
		Point2D p1 = (Point2D)periphery.elementAt(index);
		Point2D p2 = (Point2D)periphery.elementAt(index+1);
		Point2D p3 = (Point2D)periphery.elementAt(index+2);

		/* Here comes the code to adjust based on the constraints */
		// generate the new points as a result of placing this rectangle
		Point2D q1 = new Point2D.Double(p2.getX() - rect.getLayoutWidth(), p2.getY());
		Point2D q2 = new Point2D.Double(p2.getX() - rect.getLayoutWidth(), p2.getY() - rect.getLayoutHeight());
		Point2D q3 = new Point2D.Double(p2.getX(), p2.getY() - rect.getLayoutHeight());

		// insert the new points at the locations index + 1,
		// index + 2 and index + 3
		// overwrite the old element at index + 1 because that wont be visible now
		newPeriphery.setElementAt(q1, index+1); // override old p2
		newPeriphery.insertElementAt(q2, index+2);
		newPeriphery.insertElementAt(q3, index+3);

		return newPeriphery;
	} // generateTopRightPeriphery()
   /**
	* Sets the initial periphery of three points: bottom-left corner,
	* bottom-right corner and the top-right corner
	* @return Vector  the initial periphery of three points
	*/

	protected Vector initializePeriphery() {
		Vector periphery = null;

		// check which corner is selected and return the three
		// initail points from that corner
		switch(corner) {

			case BOTTOM_RIGHT:
				periphery = super.initializePeriphery();
				break;

			case BOTTOM_LEFT:
				periphery = new Vector();
				periphery.addElement(new Point2D.Double(0, target.getLayoutHeight()));
				periphery.addElement(new Point2D.Double(0, 0));
				periphery.addElement(new Point2D.Double(target.getLayoutWidth(), 0));
				break;

			case TOP_LEFT:
				periphery = new Vector();
				periphery.addElement(new Point2D.Double(0, 0));
				periphery.addElement(new Point2D.Double(0, target.getLayoutHeight()));
				periphery.addElement(new Point2D.Double(target.getLayoutWidth(), target.getLayoutHeight()));
				break;

			case TOP_RIGHT:
				periphery = new Vector();
				periphery.addElement(new Point2D.Double(0, target.getLayoutHeight()));
				periphery.addElement(new Point2D.Double(target.getLayoutWidth(), target.getLayoutHeight()));
				periphery.addElement(new Point2D.Double(target.getLayoutWidth(), 0));
				break;

			default:
			   System.out.println("Fatal Error: Invalid position received in initializePeriphery");
		} // switch

		return periphery;
	} // initializePeriphery()
} // GridArrangement
