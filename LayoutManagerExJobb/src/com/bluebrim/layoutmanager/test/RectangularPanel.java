package com.bluebrim.layoutmanager.test;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.layoutmanager.*;
/**
 * This class displays the collection rectangles on the screen with
 * their proper orientation, which could be either BOTTOM_LEFT,
 * BOTTOM_RIGHT, TOP_LEFT, and TOP_RIGHT
 */

class RectangularPanel extends Panel implements CornerConstants {

	// the color pallete to be used in coloring the collection rectangles
	static Color[] pallete = { Color.blue, Color.cyan, Color.green, Color.magenta,
							   Color.orange, Color.pink, Color.red, Color.yellow };


	private int current; // holds the current orientation
	private SolutionConfig brconfig; // the configuration object for bottom right position
	private SolutionConfig blconfig; // the configuration object for bottom left position
	private CoLayoutableIF target; // the target rectangle

	private Vector locations;

	private ViewManager viewManager;

	/**
	 * Creates the rectangular panel with a default orientation of BOTTOM_RIGHT
	 * for the collection rectangles
	 * @param config   the configuration object for the solution
	 */

	RectangularPanel(SolutionConfig config, ViewManager viewManager) {
		this.viewManager = viewManager;
		this.brconfig = config;  // default position is BOTTOM RIGHT
		current = BOTTOM_RIGHT; // the default position
		// get the locations of the rectangles to be displayed
		Vector points = config.getLocations();
		target = config.getTarget();

		locations = Transform.transform(false, false, 0, 0, null, points);
		// create a copy of all the locations without any transformation

		setSize((int)target.getLayoutWidth(), (int)target.getLayoutHeight());
		setBackground(Color.lightGray);
		setVisible(true);
	} // RectangularCanvas()
	/**
	 * Draws the grid and the rectangles with respect to the current corner
	 * @param location  specifies the corner where the rectangles are to be drawn
	 */

	public void draw(int location) {

		// if the new location is the same as the current, then do nothing
		if(location == current)
			return;

		current = location;
		// get all the locations
		Vector points = null;
		// get all the rectangles to be drawn on the screen
		MrCollection collection = null;
		int trgWidth = (int)target.getLayoutWidth(), trgHeight = (int)target.getLayoutHeight();


		// check which is the corner given
		switch(location) {

			case BOTTOM_LEFT:

				// check if the solution has been already computed
				if(blconfig == null) {
				   // if not, then wait until the solution is computed
					while(viewManager.appController.mutex == true)
						// means that the computation is still going on
						// so busy wait
						;
					blconfig = viewManager.appController.arranger.getConfig();
				} // if

				collection = blconfig.getContainer().getCollection();
				locations = Transform.transform(false, false, trgWidth, trgHeight,
											 collection, blconfig.getLocations());
			break;

			case BOTTOM_RIGHT:

				collection = brconfig.getContainer().getCollection();
				locations = Transform.transform(false, false, trgWidth, trgHeight,
											 collection, brconfig.getLocations());
			break;

			case TOP_LEFT:
				// check if the solution has been already computed
				if(blconfig == null) {
				   // if not, then wait until the solution is computed
					while(viewManager.appController.mutex == true)
						// means that the computation is still going on
						// so busy wait
						;
					blconfig = viewManager.appController.arranger.getConfig();
				} // if
				collection = blconfig.getContainer().getCollection();
				locations = Transform.transform(false, true, trgWidth, trgHeight,
											collection, blconfig.getLocations());
			break;

			case TOP_RIGHT:
				// transform the points along the y-axis
				collection = brconfig.getContainer().getCollection();
				locations = Transform.transform(false, true, trgWidth, trgHeight,
												collection, brconfig.getLocations());
			break;
		} // switch

		repaint();
	} // draw()
	/**
	 * Draws the horizontal and vertical grids for display
	 * @param g  the graphics context
	 * @param horzColor  the color to be used for drawing the horizontal lines
	 * @param vertColor  the color to be used for drawing the vertical line
	 */


	private void drawGrids(Graphics g, Color horzColor, Color vertColor) {

	   int vertGridWidth, horzGridWidth;


	   if((current == BOTTOM_LEFT) || (current == BOTTOM_RIGHT)) {
			horzGridWidth = (int)viewManager.appController.constraint.getHorizontalBottomWidth();
			drawHorizontalGrids(g, horzGridWidth, BOTTOM_LEFT, horzColor);
	   } else {
			horzGridWidth = (int)viewManager.appController.constraint.getHorizontalBottomWidth();
			drawHorizontalGrids(g, horzGridWidth, TOP_LEFT, horzColor);
		} // if-else


		if(current == BOTTOM_LEFT || current == TOP_LEFT) {
			vertGridWidth = (int)viewManager.appController.constraint.getVerticalLeftWidth();
			drawVerticalGrids(g, vertGridWidth, BOTTOM_LEFT, vertColor);
		} else {
			vertGridWidth = (int)viewManager.appController.constraint.getVerticalRightWidth();
			drawVerticalGrids(g, vertGridWidth, BOTTOM_RIGHT, vertColor);
		} // if-else
	} // drawGrids()
	/**
	 * Draws the horizontal lines for display
	 * @param g  the graphics context
	 * @param height the distance between the horizontal lines
	 * @param side specifies whether it is to be drawn starting from
	 *             bottom or from top
	 * @param color  the color to be used for drawing the vertical line
	 */

	private void drawHorizontalGrids(Graphics g, int height, int side, Color color) {
		int trgWidth = (int)target.getLayoutWidth(), trgHeight = (int)target.getLayoutHeight();

		g.setColor(color);

		if(side == TOP_LEFT) {
			for(int i = 0; i < trgHeight; i += height) {
				g.drawLine(0, i, trgWidth, i);
			} // for
		} else {
			for(int i = trgHeight; i >= 0; i -= height) {
				g.drawLine(0, i, trgWidth, i);
			} // for
		} // if-else
	} // drawHorizontalGrids()
	/**
	 * Draws the horizontal lines for display
	 * @param g  the graphics context
	 * @param width the distance between the vertical lines
	 * @param side  specifies the direction from where to start drawing the lines
	 * @param color  the color to be used for drawing the vertical line
	 */

	private void drawVerticalGrids(Graphics g, int width, int side, Color c) {
		int trgWidth = (int)target.getLayoutWidth(), trgHeight = (int)target.getLayoutHeight();

		g.setColor(c);
		if(side == BOTTOM_LEFT) {
			for(int i = 0; i < trgWidth; i += width) {
				g.drawLine(i, 0, i, trgHeight);
			} // for
		} else if(side == BOTTOM_RIGHT) {
			for(int i = trgWidth; i >= 0; i -= width) {
				g.drawLine(i, 0, i, trgHeight);
			} // for
		} // if-else
	} // drawVerticalGrids()
	/**
	 * Gives the collection of rectangles for the current position
	 * @return Collection  the current set of collection rectangles
	 */

	private MrCollection getCollection() {
		switch(current) {
			case BOTTOM_LEFT: return blconfig.getContainer().getCollection();

			case BOTTOM_RIGHT: return brconfig.getContainer().getCollection();

			case TOP_LEFT: return blconfig.getContainer().getCollection();

			case TOP_RIGHT: return brconfig.getContainer().getCollection();

			default: return null;
		} // switch
	} // getCollection()
	/**
	 * Draws the actual rectangles on the panel
	 * @param g  the current graphics context to be used for drawing the
	 *           rectangles
	 */

	public void paint(Graphics g) {

		double x, y, tmp;
		double width = target.getLayoutWidth();
		double height = target.getLayoutHeight();
		CoLayoutableIF rect= null;
		MrCollection collection = getCollection();

		setForeground(Color.black);
		g.fillRect(0, 0, (int)width, (int)height);
		// for all the elements in the location vector (which is equal
		// to the number of rectangles in the collection), draw the
		// rectangles on the panel after transformation
		for(int i = 0; i < locations.size(); i++) {
			rect = (CoLayoutableIF)collection.getElement(i);
			// the values in the location vector are with respect to
			// normal cartesian x-y coordinate system
			// we transform the point to the correct position
			// for drawing
			Point2D p = (Point2D)locations.elementAt(i);
			x = p.getX();
			y = target.getLayoutHeight() - p.getY() - rect.getLayoutHeight();
			g.setColor(pallete[i%pallete.length]);
			g.fillRect((int)x, (int)y, (int)rect.getLayoutWidth(), (int)rect.getLayoutHeight());			
		} // for
		drawGrids(g, Color.darkGray, Color.darkGray);
		Vector per=brconfig.getPeriphery();
		g.setColor(Color.red);
		for (int i = 0; i < per.size()-1; i++){
			g.setColor(Color.blue);
			g.drawLine( 
				(int) (((Point2D)per.elementAt(i)).getX()),
				(int) (target.getLayoutHeight() - ((Point2D)per.elementAt(i)).getY()),
				(int) ( ((Point2D)per.elementAt(i+1)).getX()),
				(int) (target.getLayoutHeight() - ((Point2D)per.elementAt(i+1)).getY()) );
			g.setColor(Color.cyan);
			g.drawLine(
				(int) (((Point2D)per.elementAt(i)).getX()),
				(int) (target.getLayoutHeight() - ((Point2D)per.elementAt(i)).getY()),
				(int) ( ((Point2D)per.elementAt(i)).getX()),
				(int) (target.getLayoutHeight() - ((Point2D)per.elementAt(i)).getY()) );
			//System.out.println((Point2D)per.elementAt(i));	
		}
		//System.out.println((Point2D)per.elementAt(per.size()-1));	
	} // paint()
	/**
	 * Updates the current drawing on the frame
	 * @param g  the current graphics context to be used for drawing
	 */

	public void update(Graphics g) {
		paint(g);
	} // update()
} // RectangularPanel
