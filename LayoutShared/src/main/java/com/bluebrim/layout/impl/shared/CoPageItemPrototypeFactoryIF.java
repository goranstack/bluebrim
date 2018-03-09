package com.bluebrim.layout.impl.shared;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;


/**
 * RMI-enbling interface for CoPageItemPrototypeFactory.
 * 
 * @author: Dennis Malmström
 */

public interface CoPageItemPrototypeFactoryIF extends CoFactoryIF {
	public static String LAYOUT_AREA = "layout_area";
	public static String RECTANGLE = "rectangle";
	public static String LINE = "line";
	public static String CURVE = "curve";
	public static String POLYGON = "polygon";
	public static String ORTHOGONAL_LINE = "orthogonal_line";
	public static String TEXT_BOX = "text_box";
	public static String LAYOUT_BOX = "layout_box";
	public static String WORKPIECE_TEXT_BOX = "workpiece_text_box";
	public static String CLOSED_POLYGON_IMAGE_BOX = "closed_polygon_image_box";
	public static String IMAGE_BOX = "image_box";

	public CoShapePageItemIF getPrototypeItem(String key);
}