package com.bluebrim.layout.impl.shared;

import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.resource.shared.*;

/**
 */
public class CoPageItemStringResources extends CoOldResourceBundle 
{
	public static CoOldResourceBundle rb = null;

	static final Object[][] contents =
	{
		{CoPageItemImageContentIF.IMAGE_CONTENT,					"Image"},
		{CoPageItemTextContentIF.TEXT_CONTENT,						"Text"},
		{CoPageItemWorkPieceTextContentIF.WORKPIECE_TEXT_CONTENT,	"Projecting text"},			
		{CoPageItemNoContentIF.NO_CONTENT,							"Empty"},
		{CoPageItemLayoutContentIF.LAYOUT_CONTENT,					"Layout"},

		{CoLayoutAreaIF.LAYOUT_AREA,				"Layout area"},
		{CoDesktopLayoutAreaIF.DESKTOP_LAYOUT,		"Desktop"},		
		{CoPageLayoutAreaIF.PAGE_LAYER_LAYOUT_AREA, "Page"},		
		
		{ CoRectangleShapeIF.RECTANGLE_SHAPE, "Rectangle" },
		{ CoEllipseShapeIF.ELLIPSE_SHAPE, "Ellipse" },
		{ CoRoundedCornerIF.ROUNDED_CORNER, "Rounded corner" },
		{ CoBeveledCornerIF.BEVELED_CORNER, "Beveled corner" },
		{ CoConcaveCornerIF.CONCAVE_CORNER, "Concave corner" },
		{ CoBoxedLineShapeIF.BOXED_LINE, "Boxed line" },
		{ CoLineIF.LINE, "Line" },
		{ CoLineIF.ORTHOGONAL_LINE, "Orthogonal line" },
		{ CoPolygonShapeIF.POLYGON_SHAPE, "Polygon" },
		{ CoCubicBezierCurveShapeIF.CUBIC_BEZIER_CURVE, "Curve" },

		// CoColumnGrid
		{CoImmutableColumnGridIF.COLUMN_GRID,"Column Grid"},

		// Text position
		{CoPageItemAbstractTextContentIF.CAP_HEIGHT,		"CapHeight"},
		{CoPageItemAbstractTextContentIF.CAP_ACCENT,		"Cap + Accent"},
		{CoPageItemAbstractTextContentIF.ASCENT,			"Ascent"},
		
		{CoPageItemAbstractTextContentIF.ALIGN_TOP,			"Top"},
		{CoPageItemAbstractTextContentIF.ALIGN_CENTERED,	"Centered"},
		{CoPageItemAbstractTextContentIF.ALIGN_BOTTOM,		"Bottom"},
		{CoPageItemAbstractTextContentIF.ALIGN_JUSTIFIED,	"Justified"},

		
		// CoSizeSpec
		{CoNoSizeSpecIF.NO_SIZE_SPEC,		"Fixed size in points"},
		{CoNoSizeSpecIF.NO_WIDTH_SPEC,		"Fixed width in points"},
		{CoNoSizeSpecIF.NO_HEIGHT_SPEC,		"Fixed height in points"},
		{CoAbsoluteWidthSpecIF.ABSOLUTE_WIDTH_SPEC,		"Fixed width"},
		{CoAbsoluteHeightSpecIF.ABSOLUTE_HEIGHT_SPEC,		"Fixed height"},
		{CoFillWidthSpecIF.FILL_WIDTH_SPEC,			"Justified width"},
		{CoFillHeightSpecIF.FILL_HEIGHT_SPEC,			"Justified height"},
		{CoContentWidthSpecIF.CONTENT_WIDTH_SPEC,		"Width by content"},
		{CoContentHeightSpecIF.CONTENT_HEIGHT_SPEC,		"Height by content"},
		{CoProportionalWidthSpecIF.PROPORTIONAL_WIDTH_SPEC,	"Proportional width"},
		{CoProportionalHeightSpecIF.PROPORTIONAL_HEIGHT_SPEC,	"Proportional height"},
		
		// CoLocationSpec
		{CoNoLocationIF.NO_LOCATION,					"Anywere"},
		{CoCenterLocationIF.CENTER_LOCATION,				"In the middle"},
		{CoLeftLocationIF.LEFT_LOCATION,				"To the left"},
		{CoRightLocationIF.RIGHT_LOCATION,				"To the right"},
		{CoTopLocationIF.TOP_LOCATION,					"At the top"},
//		{CoTopLeftLocationIF.TOP_LEFT_LOCATION,			"In the upper left corner"},
//		{CoTopRightLocationIF.TOP_RIGHT_LOCATION,			"In the upper right corner"},
		{CoBottomLocationIF.BOTTOM_LOCATION,				"At the bottom"},
//		{CoBottomLeftLocationIF.BOTTOM_LEFT_LOCATION,		"In the bottom left corner"},
//		{CoBottomRightLocationIF.BOTTOM_RIGHT_LOCATION,	"In the bottom right corner"},

		//CoNamedGraphics
//		{NAMED_GRAPHICS, "Named graphics"},

		//CoNamedGraphicsCatalog
//		{NAMED_GRAPHICS_CATALOG, "Named graphics catalog"},

		// Modification dialog

	
/*
 		{ no_size_spec, "" },
*/


	};	
/**
  Sätter om rb när Locale har ändrats. 
 */
public static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoPageItemStringResources.class.getName());
	return rb;
}
/**
  Svara med det tecken som hör till nyckeln aKey.
  Saknas en resurs för aKey så svara med denna.
 */
public static char getChar(String aKey ) {
	try
	{
		return getBundle().getString(aKey).charAt(0);
	}
	catch (MissingResourceException e)
	{
		return aKey.charAt(0);
	}			
}
/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
/**
  Svara med det namn som hör till nyckeln aKey.
  Saknas en resurs för aKey så svara med denna.
 */
public static String getName(String aKey ) {
	try
	{
		return getBundle().getString(aKey);
	}
	catch (MissingResourceException e)
	{
		return aKey;
	}			
}
/**
  Sätter om rb när Locale har ändrats. 
 */
public static void resetBundle ( ) {
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoPageItemStringResources.class.getName());
	rb.resetLookup();
}
}