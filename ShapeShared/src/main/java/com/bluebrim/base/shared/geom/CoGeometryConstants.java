package com.bluebrim.base.shared.geom;

/**
 * A set of constants used for snapping against grid lines
 *
 * @author: Dennis
 */

public interface CoGeometryConstants
{
	// edge masks
	public static final int LEFT_EDGE_MASK = 1; // snap against left grid lines
	public static final int RIGHT_EDGE_MASK = 2; // snap against right grid lines
	public static final int TOP_EDGE_MASK = 4; // snap against top grid lines
	public static final int BOTTOM_EDGE_MASK = 8; // snap against bottom grid lines
	public static final int CENTER_EDGE_MASK = 16; // snap against center grid lines

	public static final int HORIZONTAL_EDGE_MASK = TOP_EDGE_MASK | BOTTOM_EDGE_MASK;
	public static final int VERTICAL_EDGE_MASK = LEFT_EDGE_MASK | RIGHT_EDGE_MASK | CENTER_EDGE_MASK;

	public static final int NO_EDGE_MASK = 0;
	public static final int ALL_EDGE_MASK = VERTICAL_EDGE_MASK | HORIZONTAL_EDGE_MASK;

	// direction mask
	public static final int TO_LEFT_MASK = 1; // snap in left direction
	public static final int TO_RIGHT_MASK = 2; // snap in right direction
	public static final int DOWN_MASK = 4; // snap in down direction
	public static final int UP_MASK = 8; // snap in up direction
	public static final int ALL_DIRECTIONS_MASK = TO_LEFT_MASK | TO_RIGHT_MASK | DOWN_MASK | UP_MASK;
}