package com.bluebrim.layoutmanager;

/**
 * This class computes the arrangement for placing the rectangles
 * To be used when the computation of arrangement is to be done in a thread
 * This class is used by AppController to fork another thread of computation
 * for the BOTTOM LEFT corner while the View Manager is still busy drawing
 * the objects on the screen
 *
 */
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

public class Arranger implements Runnable {

	private CoLayoutableIF target; // the target rectangle
	private MrConstraint constraint; // the constraints
	private MrCollection collection; // the collection rectangles
	private int position; // the position where the arrangement is to be done
	private SolutionConfig config; // the config object for the solution

	/**
	 * Initializes the various attributes
	 * @param target  the target rectangle
	 * @param collection  the set of collection rectangles given by the user
	 * @param constraint  the constraints on placement of the rectangles
	 * @param position    the position of the corner where the rectangles
	 *                    are to be placed
	 */
	public Arranger(CoLayoutableIF target, MrCollection collection,
					 MrConstraint constraint, int position ) {

		this.target = target;
		this.constraint = constraint;
		this.collection = collection;
		this.position = position;
	} // ArrangerThread()
	/**
	 * Returns the result of arrangement of collection rectangles along
	 * a corner
	 * @return Config  the configuration for the result
	 */

	public SolutionConfig getConfig() {
		return config;
	} // getConfig()
	/**
	 * Arranges the rectangles along the given corner
	 */

	public void run() {
		GridArrangement arr = new GridArrangement();
		config = (SolutionConfig)arr.place(target, collection, constraint, GridArrangement.BOTTOM_LEFT);
	} // run()
} // Arranger
