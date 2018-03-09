package com.bluebrim.layoutmanager;

/**
 * This class holds the constraints on the placement of rectangles from the
 * collection into the target rectangle viz. the maximum percentage, width of horiontal
 * grids, and width vertical grids
 */

public class MrConstraint {

	protected double horizontalBottom; //the width of the horizontal grids from bottom
	protected double horizontalTop; //the width of the horizontal grids from top
	protected double leftVertical; //the width of the vertical grids from left
	protected double rightVertical; //the width of the vertical grids from right
	protected double maxpercent;

	/**
	 * Constructs a new constraint with the specified values
	 * @param percent  the maximum percentage of the target rectangles that
	 *                 can be occupied by the rectangles in the collection
	 * @param horzBottom  the width of the horizontal grids from bottom
	 * @param horzTop     the width of the horizontal grids from top
	 * @param leftVert    the width of the vertical grids from left
	 * @param rightVert   the width of the vertical grids from right
	 */

	public MrConstraint(double percent, double horzBottom, double horzTop, double leftVert, double rightVert) {
		this.maxpercent = percent;
		this.horizontalBottom = horzBottom;
		this.horizontalTop = horzTop;
		this.leftVertical = leftVert;
		this.rightVertical = rightVert;
	} // Constraint()
	/**
	 * Gets the width of the horizontal grids from bottom
	 * @return double  the width of horizontal grids
	 */

	public double getHorizontalBottomWidth() {
		return horizontalBottom;
	} // getHorizontalBottomWidth()
	/**
	 * Gets the width of the horizontal grids from top
	 * @return double  the width of horizontal grids
	 */

	public double getHorizontalTopWidth() {
		return horizontalTop;
	} // getHorizontalBottomWidth()
	/**
	 * Returns the maximum percentage of the target rectangles that can be occupied
	 * @return double  the maximum percentage
	 */

	public double getMaxPercent() {
		return maxpercent;
	}
	/**
	 * Gets the width of the vertical grids from left
	 * @return double the width of vertical grids from left
	 */

	public double getVerticalLeftWidth() {
		return leftVertical;
	} // getLeftVerticalWidth()
	/**
	 * Gets the width of the vertical grids from right
	 * @return double the width of vertical grids from right
	 */

	public double getVerticalRightWidth() {
		return rightVertical;
	} // getRightVerticalWidth()
} // Constraint
