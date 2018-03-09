package com.bluebrim.layoutmanager;

import java.awt.geom.*;
import java.util.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Implements the actual for placement of elements from the collection
 * into the container such that our objective of maximum area coverage
 * is achieved with the given constraints on the maximum percentage
 * and placing of a rectangle to the top or left of another rectangle.
 * Uses Branch-and-Bound strategy to reach to the solution by trying to
 * choose an element and the location that will maximize our
 * objective in the final configuration
 */

public class Arrangement implements PlacementPolicy, CornerConstants {

   // the maximum percentage of the target rectangle that can be covered
   protected double maxpercent;
   // the actual target rectangle
   protected CoLayoutableIF target;
   // the input collection of rectangles from which the elements are to be
   // placed in the target so that the objective is met
   protected RectangularCollection collection;

   // holds any partial solution that we may have obtained
   // since we may not be able to find the best solution (the one that
   // either fits all the elements from the collection into the target
   // or the maximum percentage specified has been occupied) due to the
   // given constraints on placement of rectangles, we have to keep track
   // of any intermediate solution that we may find

   protected SolutionConfig solution;

   // holds the set of configurations that have been currently generated
   // by the algorithm; the elements in the list are arranged in a prioritized
   // based on the cost for each configuration

   protected PriorityList list;

   // the constraints to be obeyed while placing the rectangles
   protected MrConstraint constraint;

   // the corner where the rectangles have to be placed
   protected int corner;


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
		Point2D p1, p2, p3;
		double width = rect.getLayoutWidth(), height = rect.getLayoutHeight();
		// loop through all the elements in the periphery
		for(int j=0; j < periphery.size()-2; j+=2) {
			p1 = (Point2D)periphery.elementAt(j);
			p2 = (Point2D)periphery.elementAt(j+1);
			p3 = (Point2D)periphery.elementAt(j+2);
			// check if the rectangle can be fit in the current location
			if((width <= (p2.getX() - p1.getX())) &&
			   (height <= (p3.getY() - p2.getY()))) {
				  // System.out.println("Can..place ");
				  return true;
			} // if
		} // for

		// System.out.println("Cannot place..");
		return false;
	} // canPlace()
	/**
	 * Checks if we can place any more elements into the given container
	 * @param container the container that is to be checked
	 * @return boolean  indicates whether we can place any more elements
	 */

	public boolean canPlaceMoreElements(MrContainer container) {
		if(computePercentage(container) < maxpercent)
			return true;
		else
			return false;
	} // canPlaceMore()
	/**
	 * A utility method that computes the percentage area of the target
	 * occupied by the elements placed in the container
	 * @param container  the container of elements
	 * @return double  the percentage area occupied
	 */

	protected double computePercentage(MrContainer container) {
		double total = ((CoLayoutableIF)container.getContainer()).getArea();
		double area = occupiedArea(container.getCollection());
		// System.out.println("Area occupied is.." + area + " % is.." + ((double)area)/total);
		return ((double)area / total);
	} // computePercentage()
	/**
	 * A utility method that computes the percentage area of the target
	 * occupied by the elements placed in the container & the given rectangle
	 * @param collecton - the collection of rectangles whose area is to be computed
	 * @return double  the percentage area occupied
	 */

	protected double computePercentage(MrContainer container, CoLayoutableIF rect) {
		double total = ((CoLayoutableIF)container.getContainer()).getArea();
		double area = occupiedArea(container.getCollection()) + rect.getArea();
		// System.out.println("Area occupied is.." + area + " % is.." + ((double)area)/total);
		return ((double)area / total);
	} // computePercentage()
	/**
	 * Computes the location where the chosen rectangle is to be placed
	 * @param temp  the current configuration
	 * @param rect  the rectangle that is to be placed
	 * @param p  the location of the corner where the rectangle is to be placed
	 */

	protected Point2D computePlacingPosition(SolutionConfig temp, CoLayoutableIF rect, Point2D p) {
		return new Point2D.Double(p.getX() - rect.getLayoutWidth(), p.getY());
	} // computePlacingPosition()
	/**
	 * A utility method to copy the points from the old to the new periphery
	 * @param newPeriphery  the new vector into which the points are to be copied
	 * @param periphery   the vector from which the points are to be copied
	 */

	public void copyPoints(Vector newPeriphery, Vector periphery) {
		for(int i = 0; i < periphery.size(); i++)
			newPeriphery.addElement(new Point2D.Double( ((Point2D)periphery.elementAt(i)).getX(),((Point2D)periphery.elementAt(i)).getY() ));
	} // copyPoints()
	/**
	 * This method eliminates those rectangles from the input collection
	 * rectangles that have dimensions greater than that of the target
	 * This purging will help us to reduce the size of the solution space
	 * @param collection the input collection of rectangles
	 * @return RectangularCollection  the set of rectangles that can be
	 *            placed into the target
	 */

	protected RectangularCollection eliminateRectangles(RectangularCollection collection, CoLayoutableIF target) {
		// clone returns a vector of all items in the collection for
		// doing a deep copy
		RectangularCollection newCollection = new RectangularCollection((Vector)collection.clone());
		// loop through all the elements in the input collection
		for(int i = 0; i < newCollection.count(); i++) {
			CoLayoutableIF rect = (CoLayoutableIF)newCollection.getElement(i);
			// check if the current rectangle has dimensions exceeding
			// those of the target
			if((rect.getLayoutWidth() > target.getLayoutWidth()) ||
							  (rect.getLayoutHeight() > target.getLayoutHeight())) {
				newCollection.removeElement(i);
				// System.out.print("Eliminating rectangle.." + i);
				// rect.print();
			} // if
		} // for

		return newCollection;
	} // eliminateRectangles()
	/**
	 * Evaluates the cost for this configuration by computing the area of
	 * the area of the target rectangle that would remain unoccupied
	 * The value obtained is only an approximation to the actual value
	 */

	protected double evaluateCost(SolutionConfig current, MrCollection collection) {
		double total = 0, unoccupied = 0;
		boolean found = false;
		Vector periphery = current.getPeriphery();
	   // for all the remaining elements int the collection vector
		for(int i = current.nextElement(); i < collection.count(); i++) {
			CoLayoutableIF rect = (CoLayoutableIF)collection.getElement(i);
			double width = rect.getLayoutWidth();
			double height = rect.getLayoutHeight();
			total += rect.getArea();
			Point2D p1, p2, p3;
			found = false;

			// loop until we find a location in the periphery where
			// we can place this rectangle
			for(int j=0; (j < periphery.size()-2) && (found == false); j+=2) {
				p1 = (Point2D)periphery.elementAt(j);
				p2 = (Point2D)periphery.elementAt(j+1);
				p3 = (Point2D)periphery.elementAt(j+2);
				// check if the rectangle can be fit in the current location
				if((width <= (p2.getX() - p1.getX())) &&
					(height <= (p3.getY() - p2.getY()))) {
						found = true;
				 } // if
			} // for

			// we could not find any location where we can place this
			// rectangle so that area is going to remain unoccupied
			if(found == false)
				unoccupied += rect.getArea();
		} // for

		// The cost for this config is the area that may remain unoccupied
		// in the final configuration
		// Cost = {Area that would remain unoccupied in the final config }
		// Cost = [Total Area] - [Area of those rectangles that will fit in]
		//                     - [ Area occupied by the parent ]
		// Cost = [Total Area] - [Area of the collection rectangles -
		//                          Area of those that cannot fit in ]
		//                    - [ Area already occupied by the current config]

		double cost = current.getTarget().getArea() - (total - unoccupied)
					- current.getOccupiedArea();

		// System.out.println("evaluated cost for this config : " + cost);
		return cost;
	} // evaluateProfit()
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
		double x, y, x1, y1;

		x = p.getX() - ((double)rect.getLayoutWidth())/2; // x coordinate of the centre
		y = p.getY() + ((double)rect.getLayoutHeight())/2;  // y coordinate of the centre
		x1 = current.getTarget().getLayoutWidth()- x;  // distance from the bottom right corner
		double distance = Math.sqrt(x1*x1 + y*y);
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

		Vector locations = new Vector();
		Vector periphery = config.getPeriphery();
		double width = rect.getLayoutWidth(), height = rect.getLayoutHeight();
		Point2D p1, p2, p3;

		// System.out.println("size of periphery: " + periphery.size());

		for(int j=0; j < periphery.size()-2; j+=2) {
			p1 = (Point2D)periphery.elementAt(j);
			p2 = (Point2D)periphery.elementAt(j+1);
			p3 = (Point2D)periphery.elementAt(j+2);
			// check if the rectangle can be fit in the current location
			if((width <= (p2.getX() - p1.getX())) &&
			   (height <= (p3.getY() - p2.getY()))) {
				// if so, then add it to the location vector
				locations.addElement(new Integer(j));
			} // if
		} // for
		return locations;
	} // evaluateLocations()
	/**
	 * Generates the new periphery for the resulting after placing the given
	 * rectangle into the current configuration
	 * @param periphery  the periphery for the current configuration
	 * @param rect   the rectangle that is to be into the configuration
	 * @param index  the index of the location in the periphery that
	 * @return Vector  the new periphery resulting after placing this rectangle
	 */

	 protected Vector generateNewPeriphery(Vector periphery, CoLayoutableIF rect, int index) {

		Vector newPeriphery = new Vector(periphery.size());
		// copy the points from the periphery to the new periphery
		copyPoints(newPeriphery, periphery);

		// get the points that will be altered
		Point2D p1 = (Point2D)periphery.elementAt(index);
		Point2D p2 = (Point2D)periphery.elementAt(index+1);
		Point2D p3 = (Point2D)periphery.elementAt(index+2);

		// generate the new points as a result of placing this rectangle
		Point2D q1 = new Point2D.Double(p2.getX() - rect.getLayoutWidth(), p2.getY());
		Point2D q2 = new Point2D.Double(p2.getX() - rect.getLayoutWidth(), p2.getY() + rect.getLayoutHeight());
		Point2D q3 = new Point2D.Double(p2.getX(), p2.getY() + rect.getLayoutHeight());


		// insert the new points at the locations index + 1,
		// index + 2 and index + 3
		// overwrite the old element at index + 1 because that wont be visible now
		newPeriphery.setElementAt(q1, index+1); // override old p2
		newPeriphery.insertElementAt(q2, index+2);
		newPeriphery.insertElementAt(q3, index+3);

		Vector points = new Vector(4); // holds the index of the elements
									// to be deleted as a result of merging

		// We now have to see if we have to merge the corner points
		// check if we have to merge the new point with a point on the left
		if(index > 0) {
			p1 = (Point2D)newPeriphery.elementAt(index-1);
			p2 = (Point2D)newPeriphery.elementAt(index);
			p3 = (Point2D)newPeriphery.elementAt(index+1);
			// check if p1, p2 and p3
			if(p1.equals(p2) && p2.equals(p3)) {
				// merge p1, p2, and p3 into a single point
				// by deleting p2 and p3
				points.addElement(new Integer(index));
				points.addElement(new Integer(index));
			} // if
		} // if

		// check if we have to merge the new point with a point on the top
		if(index + 5 < periphery.size()) {
			p1 = (Point2D)newPeriphery.elementAt(index+3);
			p2 = (Point2D)newPeriphery.elementAt(index+4);
			p3 = (Point2D)newPeriphery.elementAt(index+5);
			// check if p1, p2 and p3 are equal
			if(p1.equals(p2) && p2.equals(p3)) {
				// merge p1, p2, and p3 into a single point
				// by deleting p2 and p3
				points.addElement(new Integer(index+3));
				points.addElement(new Integer(index+4));
			} // if
		} // if

		// remove those points at the index given by points vector
		for(int i = 0; i < points.size(); i++) {
			newPeriphery.removeElementAt(((Integer)points.elementAt(i)).intValue());
		} // for

		return newPeriphery;
	} // generateNewPeriphery()
	/**
	 * Generates the new configuration as a result of placing the given
	 * rectangle into the current configuration
	 * @param current  the current configuration
	 * @param rect    the next rectangle that is to be placed into this config
	 * @param index  the index of the location into the periphery for the
	 *               current configuration where the next rectangle is to be placed
	 * @return Config  the new config after placing the next rectangle
	 */

	protected SolutionConfig generateNextConfig(SolutionConfig current, CoLayoutableIF rect, int index) {
		Vector periphery = generateNewPeriphery(current.getPeriphery(), rect, index);
		// create a new configuration setting the cost to its old value
		// the value of cost for this new config is updated later
		Vector list = (Vector)((RectangularCollection)current.getContainer().getCollection()).clone();
		RectangularCollection rectcol = new RectangularCollection(list);
		RectangularContainer container = new RectangularContainer((CoLayoutableIF)current.getContainer().getContainer(), rectcol);
		SolutionConfig config = new SolutionConfig(container, periphery, current.getCost(), current.getLocations());
		// set the next element to be considered
		config.setNextElement(current.nextElement());
		// set the occupied area in this config
		config.setOccupiedArea(current.getOccupiedArea() + rect.getArea());
		// add the rectangle to the collection set for the new config
		config.addItem(rect);
		config.setDistance(evaluateDistance(current, rect,
							 (Point2D)current.getPeriphery().elementAt(index+1)));
		return config;
	} // generateNextConfig()
   /**
	* Initialises all initial config object, the periphery for it and location
	* vector and then adds this configuration to the priority list
	* @param target  the target rectangle for this problem instance
	* @param collection the set of collection rectangles to be placed
	* @param position a position constant
	*/

   protected void init(CoLayoutableIF target, RectangularCollection collection, MrConstraint c, int position) {
	   this.target = target;
	   // order the elements in the collection based on the area
	   collection.order();
	   // now eliminate those rectangles from the input that either base or
	   // height greater than the target
	   this.collection = eliminateRectangles(collection, target);
	   this.maxpercent = c.getMaxPercent();
	   constraint = c;
	   corner = position;
	   solution = null;
	   // set the initial periphery of three points: bottom-left corner,
	   // bottom-right corner and the top-right corner
	   Vector periphery = initializePeriphery();

	   // create the container & collection rectangle for the intial config
	   RectangularCollection rectcol = new RectangularCollection();
	   RectangularContainer container = new RectangularContainer(target, rectcol);
	   // create the intial config
	   SolutionConfig config = new SolutionConfig(container, periphery, 0, null);
	   config.setDistance(0); // set the distance for the inital node
	   list = new PriorityList();
	   // add it to the list
	   list.insert(config);
   } // init()                        
   /**
	* Sets the initial periphery of three points: bottom-left corner,
	* bottom-right corner and the top-right corner
	* @return Vector  the initial periphery of three points
	*/

   protected Vector initializePeriphery() {
	   Vector periphery = new Vector();
	   periphery.addElement(new Point2D.Double());
	   periphery.addElement(new Point2D.Double(target.getLayoutWidth(), 0));
	   periphery.addElement(new Point2D.Double(target.getLayoutWidth(), target.getLayoutHeight()));
	   return periphery;
   } // initializePeriphery()                     
	/**
	 * A utility method that computes the area occupied by the elements of
	 * the given collection in the target
	 * @param collecton - the collection of rectangles whose area is to be computed
	 * @return  int  the total area occupied
	 */

	protected double occupiedArea(MrCollection collection) {
		double area = 0;

		if(collection == null || collection.isEmpty())
			return 0;

		for(Enumeration e = collection.enumerate(); e.hasMoreElements(); )
			area += ((CoLayoutableIF)e.nextElement()).getArea();
		return area;
	} // occupiedArea()
	/**
	 * Checks if the given count exceeds elements in the collection
	 * @param count  the count of the elements in the collection
	 * @return boolean  a boolean indicating whether we are over with or not
	 */

	protected boolean over(int count) {
		return (count >= collection.count());
	} // over()
   /**
	* The function that implements the algorithm and decides the rectangles
	* that are to be placed into the target and the actual locations for those
	* locations
	* @param target  the target rectangle for the problem
	* @param collection  the collection of rectangles for this problem
	* @param position  a position constant
	* @return Object  the config object for the solution containing the
	*                 rectangles that have been chosen and the actual locations
	*                 for them
	*/

	public Object place(Object target, MrCollection collection, MrConstraint c, int position) {

		SolutionConfig config = null, temp = null;
		CoLayoutableIF rect = null;

		init((CoLayoutableIF)target, (RectangularCollection)collection, c, position);

		// the algorithm terminates when either we have found a best solution
		// (i.e. one that places either all the rectangles into the target or the
		// we have reached the maximum percentage condition) or we have
		// explored all the possible combinations ( the worst case condition)

		// check if we have any more elements to be processed
		while(!list.isEmpty()) {

			// remove the next element from the priority list
			// this element is the one having the least cost
			config = (SolutionConfig)list.remove();

			// check if we have found the best solution
			if(!canPlaceMoreElements(config.getContainer()) ||
							 config.getCount() == collection.count()) {
				// if we have filled the target rectangle to the capacity
				// or all the elements given have been added, then we
				// have found the best solution

				// System.out.println("best solution found...");
				return config; // we have found the solution
			} // if

			//System.out.println("Start Next element: " + config.nextElement());

			// if we have exhausted all the elements from the collection
			if(config.nextElement() < collection.count()) {
				// if not, get the next rectangle
				rect = (CoLayoutableIF)collection.getElement(config.nextElement());
				// check if we can place this rectangle into the configuration

				// if not, then loop through the remaining elements in the collection
				// until we find an element that can be placed according
				// to the given constraints
				while((config.nextElement() < collection.count()) &&
					((computePercentage(config.getContainer(), rect) > maxpercent)
					|| !canPlace(config, rect)) ) {

					config.skip();  // if not then skip it
					//System.out.println("Skipping..");
					//System.out.println("Start Next element: " + config.nextElement());

					// if we have not exhausted, then select the next rectangle
					if(config.nextElement() < collection.count())
						rect = (CoLayoutableIF)collection.getElement(config.nextElement());
				} // while
			} // if

			// check if we looped through all the elements in the collection
			// if so, we have found a partial solution which may be suboptimal
			if(config.nextElement() >= collection.count()) {
				// System.out.println("..partial solution found");

				// check if we have already found any other partial solution
				if(solution == null) {
					solution = config;
				} else {
					// we already have a solution, check which one occupies
					// more area and assign that as the solution
					if(config.getOccupiedArea() > solution.getOccupiedArea()) {
						solution = config;
					} else {
						// check if the values are equal
						if(config.getOccupiedArea() == solution.getOccupiedArea()) {
							// if so, check the one that has lesser value for distance
							if(config.getDistance() < solution.getDistance()) {
								// if the current solution has a lesser value, then assign
								// it as a solution
							   solution = config;
							} //if
						} // if
					} //if-else
				 } // if-else
			} else {

				// check if the area occupied after placing all the
				// rectangles that can fit is lesser than that already obtained
				// if so, then do not explore the current config as we have
				// already got a better solution
				// i.e. we prune the tree based on the current solution
				// to eliminate those configuration cannot yield a better solution
				// than the current one
				if((solution == null) || (upperBound(config, collection) >=
											  solution.getOccupiedArea())) {
					// at this stage rect points to the next rectangle that can
					// be placed into the target for the current configuration
					// now evaluate the locations where the rectangle can be placed
					// Note: locations contains the index of the element in the perihery
					// where the next rectangle can be placed and not the actual location

					// now we have a rectangle that can be placed
					Vector locations = evaluateLocations(config, rect);
					// System.out.println("Number of locations.." + locations.size());

					// loop through all the possible locations
					// generating a new configuration for each one
					for(int i = 0; i < locations.size(); i++) {
						//System.out.println("Location: " + i);
						// ((Point)config.getPeriphery().elementAt(((Integer)locations.elementAt(i)).intValue()).print();
						// now compute the location for this point in the final configuration
						Point2D p = new Point2D.Double( ((Point2D)(config.getPeriphery().elementAt(((Integer)locations.elementAt(i)).intValue()+1))).getX(),((Point2D)(config.getPeriphery().elementAt(((Integer)locations.elementAt(i)).intValue()+1))).getY());
						// generate the next config
						temp = generateNextConfig(config, rect, ((Integer)locations.elementAt(i)).intValue());
						// evaluate the cost for this config
						temp.setCost(evaluateCost(temp, collection));
						// add the location chosen for this config
						temp.addLocation(computePlacingPosition(temp, rect, p));
						// insert this config into the priority list
						list.insert((Comparable)temp);
						// System.out.println("inserted element .." + i + "..list size." + list.size());
					} // for
				}// if
			} // else
		} // while()

		// if we reach here then it implies we have had to search through
		// the whole tree as there was best solution found given the constraints
		// if so, then return the current best solution that we have
		return solution;
	} // place()
/**
 * @author Mats Åström (2000-05-26 09:27:53)
 * @param periphery java.util.Vector
 */
public static void removeMidelPoints(Vector periphery) 
{
	for (int i = 0; (i < periphery.size()-2)&&(periphery.size()>2); i++)
	{
		
		Point2D p1=(Point2D) periphery.elementAt(i);
		Point2D p2=(Point2D) periphery.elementAt(i+1);
		Point2D p3=(Point2D) periphery.elementAt(i+2);

		//System.out.println("Before: "+periphery);	
		
		if( (p1.getY()==p3.getY()) || ( p1.getX()==p3.getX() ))
		{	
			periphery.removeElementAt(i+1);
			i--;
		}	
			//System.out.println("After:  "+periphery);	
	}
	
}
	/**
	 * Generates the upper bound on the area that can be occupied
	 * by placing the remaining elements from the collection
	 * This can be used to prune to solution tree
	 * @param config  the current configuration
	 * @param collection   the collection vector
	 * @return  int   the total area that can be occupied
	 */


	protected double upperBound(SolutionConfig config, MrCollection collection) {
	   // System.out.println("In method upper bound....");
		int index = config.nextElement();
		double total = config.getOccupiedArea();
		while(index < collection.count()) {
			CoLayoutableIF rect = (CoLayoutableIF)collection.getElement(index);
			if(canPlace(config, rect))
				total += rect.getArea();
			index++;
		} // while

		// System.out.println("upper bound over...." + total);
		return total;
	} // upperBound()
} // class Arrangement
