package com.bluebrim.layoutmanager;

import java.awt.geom.*;
import java.util.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
/**
 * This class holds the configuration of the final or the intermediate
 * solution by maintaining the collection of those objects that have
 * been placed at that level
 * @author Murali Ravirala
 * @version 1.0
 */


public class SolutionConfig implements Comparable {
	// the container that holds the target object and the rectangles that
	// have been placed into it

	private RectangularContainer container;

	// holds the periphery of the rectangles that have been placed into the
	// container; used for efficiency purposes to find out the number of
	// possible locations the next rectangle can fit into
	private Vector periphery;

	// holds the actual locations where the rectangles in this configuration
	// have been placed
	private Vector locations;

	// gives the area occupied by the rectangles that are held by this
	// container
	private double occupiedArea;

	// gives the cost of this configuration which is the area of the
	// target rectangle that will remain unoccupied in the final configuration

	private double cost;

	// holds the total distance of all the collection rectangles from
	// the corner
	private double distance;


	// holds the next rectangle from the collection rectangles to be
	// considered; it need not be the same as the elements in the collection
	// because we might have had to skip through some rectangles due to
	// constraints
	private int nextElement;


	/**
	 * Creates a configuration with the given container, periphery,
	 * cost and the locations
	 * @param container  the container for this configuration, which holds
	 *                   target and the set of collection rectangles that
	 *                    have been placed into the target
	 * @param periphery  holds the periphery of the rectangles that have
	 *                   been placed into the target for this configuration
	 * @param locs    holds the actual locations for the rectangles placed
	 *
	 */

	public SolutionConfig(RectangularContainer container, Vector periphery,
				  double cost, Vector locs) {
		this.container = container;
		this.periphery = periphery;
		this.cost = cost; // set the cost
		occupiedArea = 0; // initial occupied area is null
		// check if the input location vector is null
		if(locs == null)
			locations = new Vector(); // allocate a new vector
		else
			locations = (Vector)locs.clone(); // make a copy of the input

		// initialise the next element, the actual value is set
		// using the setNextElement method
		if(container.getCollection() != null)
			nextElement = container.getCollection().count();
		else
			nextElement = 0;
	} // Config
	/**
	 * Creates a configuration with the given container, periphery,
	 * cost and the locations
	 * @param container  the container for this configuration, which holds
	 *                   target and the set of collection rectangles that
	 *                    have been placed into the target
	 * @param periphery  holds the periphery of the rectangles that have
	 *                   been placed into the target for this configuration
	 * @param locs    holds the actual locations for the rectangles placed
	 *
	 */

	public SolutionConfig(RectangularContainer container, Vector periphery,
				  long cost, Vector locs) {
		this.container = container;
		this.periphery = periphery;
		this.cost = cost; // set the cost
		occupiedArea = 0; // initial occupied area is null
		// check if the input location vector is null
		if(locs == null)
			locations = new Vector(); // allocate a new vector
		else
			locations = (Vector)locs.clone(); // make a copy of the input

		// initialise the next element, the actual value is set
		// using the setNextElement method
		if(container.getCollection() != null)
			nextElement = container.getCollection().count();
		else
			nextElement = 0;
	} // Config
	/**
	 * Adds a rectangle to the end of the collection list for the container
	 * of this configuration
	 * @param Rectangle   the rectangle to be added
	 */

	public void addItem(CoLayoutableIF rect) {
		container.getCollection().addElement(rect);
		nextElement++; // we have operated upon one more input collection rectangles
	} // addToCollection
	/**
	 * Adds a location to the location vector at the end
	 * To be called when a decision has been made to place a particular
	 * rectangle at the point given in the final configuration
	 * @param p  the location where the next rectangle is to be placed
	 */

	public void addLocation(Point2D p) {
		locations.addElement(p);
	} // addLocation
/**
 * @author Mats Åström (2000-05-23 11:21:15)
 * @return int
 * @param config Object
 */
public int compareTo(Object config) {
	
	double diffCost=cost-((SolutionConfig)config).cost;
	if(diffCost==0)
	{
		double diffDist=distance-((SolutionConfig)config).distance;
		if(diffDist<0)
			return -1;
		else if(diffDist>0)
				return 1;
			else return 0;
	}
	else if(diffCost<0)
			return -1;
		else return 1;
}
	/**
	 * Compares whether the argument is the same as itself
	 * @param o  the object that is to be compared
	 */

	public boolean equals(Object config) {

		// check if the costs are the same
		if(cost == ((SolutionConfig)config).cost) {
			// check if this config has a lesser distance
			if(distance > ((SolutionConfig)config).distance) {
				return false;
			} else {
				return true;
			} // if-else
		} else {
			return false;
		} // if-else
	} // equals()
	/**
	 * Gets the container for this configuration
	 * @return RectangularContainer  the container for this configuration
	 */

	public RectangularContainer getContainer() {
		return container;
	} // getContainer()
	/**
	 * Gets the cost for this configuaration
	 * @return long  a long value representing the cost for this configuration
	 */

	public double getCost() {
		return cost;
	} // getProfit()
	/**
	 * Gets the count of elements added to the collection
	 * for this configuration
	 */

	public int getCount() {
		return container.getCollection().count();
	} // getCount()
	/**
	 * Gets the distance of the elements in the collection set from
	 * a corner
	 * @return long  the distance of the elements in the collection form
	 *               the collection
	 */

	public double getDistance() {
		return distance;
	} // getDistance()
	/**
	 * Returns the location for a rectangle at the given index.
	 * Null is returned if the index does not exist
	 * @param index the index of the element whose location is required
	 * @return Point  the point specifying the location of the rectangle
	 */

	public Point2D getLocation(int index) {
		// check if the index exists
		if(index < locations.size()) {
			return (Point2D)locations.elementAt(index);
		} else {
			return null;
		} // if-else
	} // getLocation()
	/**
	 * Returns the location vector for this configuration
	 * @return  Vector the vector containing locations for the rectangles
	 *                 that have been put into this container
	 */

	public Vector getLocations() {
		return locations;
	} // getLocations()
	/**
	 * Gets the area occupied by the rectangles placed into the container
	 * in this configuration
	 * @return int  the area occupied by the collection rectangles in this
	 *              configuration
	 */

	public double getOccupiedArea() {
		return occupiedArea;
	} // getOccupiedArea()
	/**
	 * Gets the periphery vector for this configuration
	 * @return Vector the periphery vector
	 */

	public Vector getPeriphery() {
		return periphery;
	} // getPeriphery()
	/**
	 * A utility method to get the target rectangle into which the elements
	 * from the container are to placed
	 * @param Rectangle  the target rectangle
	 */

	public CoLayoutableIF getTarget() {
		return (CoLayoutableIF)container.getContainer();
	}
	/**
	 * Compares whether the argument is greater than itself
	 * @param o  the object that is to be compared
	 */

	public boolean isGreater(Object config) {
		// check if it has greater cost
		if(cost > ((SolutionConfig)config).cost) {
			return true;
		} else if(cost == ((SolutionConfig)config).cost) {
			// if both of them are equal, then check which one
			// has larger distance
			if(distance > ((SolutionConfig)config).distance)
				return true; // greater distance
			else
				return false; // smaller distance
		} else {
			return false; // it is smaller
		} // if-else
	} // isGreater()
	/**
	 * Compares whether the argument is lesser than itself
	 * @param o  the object that is to be compared
	 */

	public boolean isLesser(Object config) {
		if(cost < ((SolutionConfig)config).cost)
			return true;
		else
			return false;
	} // isSmaller()
	/**
	 * Returns the next element that is to be considered for placement
	 * @reutrn int the index of the next element in the input collection
	 *             that is to be considered for placement
	 */

	public int nextElement() {
		return nextElement;
	} // nextElement()
	/**
	 * Sets the cost for this configuration to the input
	 * @param  cost  the cost to be assigned to this configuration
	 */

	public void setCost(double cost) {
		this.cost = cost;
	} // setProfit()
	/**
	 * Sets the cost for this configuration to the input
	 * @param  cost  the cost to be assigned to this configuration
	 */

	public void setCost(long cost) {
		this.cost = cost;
	} // setProfit()
	/**
	 * Sets the distance of the elements in the collection set from
	 * a corner
	 * @param distance  the distance to be set
	 */

	public void setDistance(double distance) {
		this.distance = distance;
	} // setDistance()
	/**
	 * Sets the next element to the input value
	 * @param nextElement  the value to be assigned to nextElement
	 */

	public void setNextElement(int nextElement) {
		this.nextElement = nextElement;
	} // setNextElement()
	/**
	 * Sets the area occupied by this configuration to the given value
	 * @param area the area to be assigned to this configuration
	 */

	public void setOccupiedArea(double area) {
		occupiedArea = area;
	} // setOccupiedArea()
	/**
	 * Sets the area occupied by this configuration to the given value
	 * @param area the area to be assigned to this configuration
	 */

	public void setOccupiedArea(int area) {
		occupiedArea = area;
	} // setOccupiedArea()
	/**
	 * Skips an element from the input collection rectangles
	 * To be called when an element cannot be fitted due the percentage
	 * or placement constraints
	 */

	public void skip() {
		//((CoLayoutableIF)container.getCollection().getElement(nextElement)).setLayoutX(1);
		//((CoLayoutableIF)container.getCollection().getElement(nextElement)).setLayoutY(1);
		nextElement++;    
	} // skip()
} // class Config
