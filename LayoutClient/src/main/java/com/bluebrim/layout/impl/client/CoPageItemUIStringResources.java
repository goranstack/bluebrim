package com.bluebrim.layout.impl.client;

import java.util.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.resource.shared.*;

/**
 * @author: Dennis
 * PENDING: Move LayoutEditor stuff to appropriate class
 */
 
public class CoPageItemUIStringResources extends CoOldResourceBundle
{
	public static CoOldResourceBundle rb = null;

	static final Object[][] contents =
	{
		{ com.bluebrim.layout.impl.client.operations.CoSetEditorialStrokeAndMargins.NAME, "Set stroke and margins" },

		{ CoPageItemToolbar.TOOLBAR_X, "x" },
		{ CoPageItemToolbar.TOOLBAR_Y, "y" },
		{ CoPageItemToolbar.TOOLBAR_W, "w" },
		{ CoPageItemToolbar.TOOLBAR_H, "h" },
		{ CoPageItemToolbar.TOOLBAR_IMAGE_X_SCALE, "x%" },
		{ CoPageItemToolbar.TOOLBAR_IMAGE_Y_SCALE, "y%" },
		{ CoPageItemToolbar.TOOLBAR_DERIVED, "Derived" },
		{ CoPageItemToolbar.TOOLBAR_COLUMN_COUNT, "No of columns" },
		{ CoPageItemToolbar.TOOLBAR_DO_RUN_AROUND, "Run around" },

		{ CoGradientFillIF.GRADIENT_FILL, "Gradient fill" },
		{ CoPatternFillStyleIF.PATTERN_FILL, "Pattern fill" },

		{ CoPageItemPageLayerPanel.PAGE_SIZE, "Page size" },
		{ CoPageItemPageLayerPanel.PAGE_SPREAD, "Spread" },
		{ CoPageItemPageLayerPanel.GRID_SPREAD, "Side by side grid" },


		{ CoPageItemPane.CONTENT_TAB,           "Content" },
		{ CoPageItemPane.GEOMETRY_TAB,           "Geometry" },
		{ CoPageItemPane.FILL_TAB,               "Fill" },
		{ CoPageItemPane.LAYOUT_TAB,             "Position and Size" },
		{ CoPageItemPane.STROKE_TAB, 		        "Stroke" },
		{ CoPageItemPane.IMAGE_TAB, 		          "Image" },
		{ CoPageItemPane.LAYOUT_CONTENT_TAB, 		  "Layout" },
		{ CoPageItemPane.TEXT_TAB, 		          "Text" },
		{ CoPageItemPane.RUN_AROUND_SPEC_TAB,   	"Run around" },
		{ CoPageItemPane.GRID_TAB,               "Columns and base lines" },
		{ CoPageItemPane.WORKPIECE_TEXT_TAB,       "Workpiece text" },
		{ CoPageItemPane.CHILD_LAYOUT_ORDER_TAB, "Child Layout Order" },
		{ CoPageItemPane.CHILD_Z_ORDER_TAB,      "Child Z Order" },
		{ CoPageItemPane.PAGE_LAYER_TAB,         "Page" },
		{ CoPageItemPane.LAYOUT_AREA_TAB,        "Layout area" },
		{ CoPageItemPane.PROTOTYPE_TAB,            "Tool" },

		{ CoPageItemPrototypeDataPanel.DESCRIPTION, "Description" },

		{ CoPageItemPane.LAYOUT_MANAGER_TAB,		"Layout Manager"},

		{ CoPageItemPropertyPanel.COLOR, "Color" },
		{ CoPageItemPropertyPanel.SHADE, "Shade" },
		
		{ CoPageItemZOrderPanel.TOP, "Send to Back" },
		{ CoPageItemZOrderPanel.UP, "Send Backward" },
		{ CoPageItemZOrderPanel.DOWN, "Bring Forward" },
		{ CoPageItemZOrderPanel.BOTTOM, "Bring to Front" },
		
		{ CoPageItemLayoutOrderPanel.TOP, "First place" },
		{ CoPageItemLayoutOrderPanel.UP, "Move up" },
		{ CoPageItemLayoutOrderPanel.DOWN, "Move down" },
		{ CoPageItemLayoutOrderPanel.BOTTOM, "Last place" },
		{ CoPageItemLayoutOrderPanel.SIZE_ORDER, "In size order" },
		
		{ CoPageItemGeometryPanel.ROTATION, "Rotation" },
		{ CoPageItemRectangularShapePanel.X, "x" },
		{ CoPageItemRectangularShapePanel.Y, "y" },
		{ CoPageItemRectangularShapePanel.WIDTH, "Width" },
		{ CoPageItemRectangularShapePanel.HEIGHT, "Height" },
		{ CoPageItemCornerShapePanel.RADIUS, "Radius" },
		{ CoPageItemCurveShapePanel.CLOSED, "Closed" },
		{ CoPageItemCurveShapePanel.REORDER, "Reorder" },
		{ CoPageItemLineShapePanel.X1, "Start X" },
		{ CoPageItemLineShapePanel.Y1, "Start Y" },
		{ CoPageItemLineShapePanel.X2, "End X" },
		{ CoPageItemLineShapePanel.Y2, "End Y" },	
		{ CoPageItemBoxedLineShapePanel.MARGIN, "Margin" },
		{ CoPageItemBoxedLineShapePanel.ORIENTATION, "Orientation" },
		{ CoPageItemGeometryPanel.DO_RUN_AROUND, 	"Run around" },
		{ CoPageItemGeometryPanel.SUPRESS_PRINTOUT, "Supress printout" },
		{ CoPageItemGeometryPanel.LOCATION_LOCKED, "Lock location" },
		{ CoPageItemGeometryPanel.DIMENSIONS_LOCKED, "Lock dimensions" },
		{ CoPageItemGeometryPanel.CHILDREN_LOCKED, "Lock children" },

		{ CoPageItemGridPanel.COLUMNS, "Columns" },
		{ CoPageItemGridPanel.DERIVED, "Derived" },
		{ CoPageItemGridPanel.MARGINS, "Margins"},
		{ CoPageItemGridPanel.LEFT, "Left margin"},
 		{ CoPageItemGridPanel.RIGHT, "Right margin"},
 		{ CoPageItemGridPanel.TOP, "Top margin"},
 		{ CoPageItemGridPanel.BOTTOM, "Bottom margin"},
 		{ CoPageItemGridPanel.COUNT, "No of columns"},
 		{ CoPageItemGridPanel.SPACING, "Spacing"},
 		{ CoPageItemGridPanel.OUTSIDE, "Outside"},
 		{ CoPageItemGridPanel.INSIDE, "Inside"},
 		{ CoPageItemGridPanel.LEFT_OUTSIDE_SENSITIVE, "Outside/inside sensitive"},

		{ CoPageItemGridPanel.BASELINE_GRID, "Base lines" },
		{ CoPageItemGridPanel.Y0, "First base line"},
		{ CoPageItemGridPanel.DELTA_Y, "Spacing"},

		{ CoPageItemStrokePanel.STYLE, "Style: "},			
		{ CoPageItemStrokePanel.THICKNESS, "Thickness: "},			
		{ CoPageItemStrokePanel.ALIGNMENT, "Alignment: "},			
		{ CoPageItemStrokePanel.ALIGN_INSIDE, "Inside"},			
		{ CoPageItemStrokePanel.ALIGN_CENTER, "Center"},			
		{ CoPageItemStrokePanel.ALIGN_OUTSIDE, "Outside"},			
		{ CoPageItemStrokePanel.FOREGROUND, "Foreground: "},			
		{ CoPageItemStrokePanel.BACKGROUND, "Background: "},	
	  { CoPageItemStrokePanel.SYMMETRY, "Symmetry" },

	  { com.bluebrim.stroke.shared.CoStrokePropertiesIF.NON_SYMMETRIC, "No" },
	  { com.bluebrim.stroke.shared.CoStrokePropertiesIF.SYMMETRIC_BY_STRETCHING_CORNERS, "Stretch corners" },
	  { com.bluebrim.stroke.shared.CoStrokePropertiesIF.SYMMETRIC_BY_STRETCHING_DASH, "Stretch dash"},
	  { com.bluebrim.stroke.shared.CoStrokePropertiesIF.SYMEETRIC_BY_STRETCHING_DASH_GP,"Stretch dash for General Path"},

		{ CoPageItemStrokePanel.STROKE_EFFECTIVE_SHAPE, "Stroke effective shape" },
		
		{ CoPageItemTextPanel.FIRST_BASELINE_TYPE, 			"Minimum" },
		{ CoPageItemTextPanel.FIRST_BASELINE_OFFSET, 				"Offset" },
		{ CoPageItemTextPanel.VERTICAL_ALIGNMENT_TYPE, 				"Type" },
		{ CoPageItemTextPanel.VERTICAL_ALIGNMENT_MAX_INTERVAL, 			"Inter paragraph max" },
		{ CoPageItemTextPanel.FIRST_BASELINE, 		"First Baseline" },
		{ CoPageItemTextPanel.VERTICAL_ALIGNMENT, 	"Vertical alignment" },
		{ CoPageItemTextPanel.TOP_MARGIN, 	"Top margin" },
		{ CoPageItemTextPanel.BOTTOM_MARGIN, 	"Bottom margin" },
		{ CoPageItemTextPanel.LEFT_MARGIN, 	"Left margin" },
		{ CoPageItemTextPanel.RIGHT_MARGIN, 	"Right margin" },

		{ CoPageItemBoundedContentPanel.X, "X" },
		{ CoPageItemBoundedContentPanel.Y, "Y" },
		{ CoPageItemBoundedContentPanel.SCALE_X, "X scale" },
		{ CoPageItemBoundedContentPanel.SCALE_Y, "Y scale" },
		{ CoPageItemBoundedContentPanel.FLIP_X, "Flip X" },
		{ CoPageItemBoundedContentPanel.FLIP_Y, "Flip Y" },
		{ CoPageItemBoundedContentPanel.CAPTION, "Caption" },
		{ CoPageItemBoundedContentPanel.CAPTION_TEXT_SHORTHAND, "T" },
		
		{ CoPageItemImagePanel.TAG, "Image tag" },
		{ CoPageItemImagePanel.SELECT, 	"Select image ..."},
		{ CoPageItemImagePanel.LOCK, 	"Image locked"},
		
		{ CoPageItemLayoutContentPanel.TAG, "Layout tag" },
		{ CoPageItemLayoutContentPanel.SELECT, 	"Select layout ..."},
		{ CoPageItemLayoutContentPanel.LOCK, 	"Layout locked"},
		{ CoPageItemLayoutContentPanel.RECURSIVE_LEVEL_COUNT, 	"Recursive levels"},	

		{ CoPageItemWorkPieceTextPanel.ACCEPTED, 	"Accepted tags"},
		{ CoPageItemWorkPieceTextPanel.AVAILABLE, 	"Not accepted tags"},
		{ CoPageItemWorkPieceTextPanel.TAG, 	"Text tag"},
		{ CoPageItemWorkPieceTextPanel.GROUPS, 	"Groups"},
		{ CoPageItemWorkPieceTextPanel.SELECT, 	"Select text ..."},

		{ CoNoneRunAroundSpecIF.NONE_RUN_AROUND_SPEC, "None" },
		{ CoShapeRunAroundSpecIF.SHAPE_RUN_AROUND_SPEC, "Shape" },
		{ CoBoundingBoxRunAroundSpecIF.BOUNDING_BOX_RUN_AROUND_SPEC, "Bounding box" },
		{ CoPageItemRunAroundSpecPanel.BOUNDS_LEFT, "Left margin" },
		{ CoPageItemRunAroundSpecPanel.BOUNDS_RIGHT, "Right margin" },
		{ CoPageItemRunAroundSpecPanel.BOUNDS_TOP, "Top margin" },
		{ CoPageItemRunAroundSpecPanel.BOUNDS_BOTTOM, "Bottom margin" },
		{ CoPageItemRunAroundSpecPanel.BOUNDS_USE_STROKE, "Use stroke" },
		{ CoPageItemRunAroundSpecPanel.SHAPE_MARGIN, "Outset" },
		{ CoPageItemRunAroundSpecPanel.SHAPE_USE_STROKE, "Use stroke" },
		
		{ CoLocationSpecIF.LOCATION_SPEC, "Location" },
		{ CoPageItemLayoutSpecPanel.LOCATION_AGGRESSIVE, "Aggressive" },
		{ CoPageItemLayoutSpecPanel.LOCATION_INSET,	"Column insets"},

		{ CoSizeSpecIF.WIDTH_SPEC, "Width" },
		{ CoSizeSpecIF.HEIGHT_SPEC, "Height" },
		{ CoPageItemLayoutSpecPanel.SIZE_PROPORTION, "Proportion" },
		{ CoPageItemLayoutSpecPanel.SIZE_DISTANCE, "Distance" }, 
		{ CoPageItemLayoutSpecPanel.SIZE_ABSOLUTE_OFFSET, "Absolute offset" },
		{ CoPageItemLayoutSpecPanel.SIZE_RELATIVE_OFFSET, "Relative offset" },

		{ CoPageItemGradientFillStylePanel.FILL, "Fill" },
		{ CoPageItemGradientFillStylePanel.BLEND, "Blend" },
		{ CoPageItemGradientFillStylePanel.STYLE, "Style" },
		{ CoPageItemGradientFillStylePanel.BLEND_LENGTH, "Blend length" },
		{ CoPageItemGradientFillStylePanel.ANGLE, "Angle" },
		{ CoPageItemGradientFillStylePanel.SOLID_BLEND, "Solid" },
		{ CoPageItemGradientFillStylePanel.CYCLIC_BLEND, "Cyclic" },
		
		{ CoPageItemLayoutAreaPanel.WORKPIECE_LOCK, "workpiece locked" },
		{ CoPageItemLayoutAreaPanel.ACCEPTS_WORKPIECE, "Accepts workpiece" },
		{ CoPageItemLayoutAreaPanel.CLEAR_WORKPIECE, "Clear workpiece" },
		{ CoPageItemLayoutAreaPanel.SELECT_WORKPIECE, "Select workpiece" },
		
		{ CoPageItemPropertyPanel.OPEN_ADM_UI, "Open ..." },
		


		
		
		
		// Column Grid
		{ CoColumnGridUI.COLUMN_GRID, "Columns"},
		{ CoColumnGridUI.MARGIN_GRID, "Margins"},
		{ CoColumnGridUI.LEFT_MARGIN, "Left margin"},
 		{ CoColumnGridUI.RIGHT_MARGIN, "Right margin"},
 		{ CoColumnGridUI.TOP_MARGIN, "Top margin"},
 		{ CoColumnGridUI.BOTTOM_MARGIN, "Bottom margin"},
 		{ CoColumnGridUI.COLUMN_COUNT, "No of columns"},
 		{ CoColumnGridUI.SPACING, "Spacing"},

// 	Etiketter
		{"SELECTION_TOOL",		"Selection tool "},
		{"CONTENT_TOOL",		"Content tool "},
		{"ZOOM_TOOL",			"Zoom tool "},
		{"SCROLLHAND_TOOL",		"Scrollhand tool "},
		{"ROTATION_TOOL",		"Rotation tool "},
		{"CHAIN_TOOL",			"Chain tool "},
		{"UNCHAIN_TOOL",		"Unchain tool "},

		{ CoPageItemPrototypeTreeUI.ADD, "Add new folder" },
		{ CoPageItemPrototypeTreeUI.REMOVE, "Delete" },
		{ CoPageItemPrototypeTreeUI.RENAME, "Rename" },
		{ CoPageItemPrototypeTreeUI.CUT, "Cut" },
		{ CoPageItemPrototypeTreeUI.COPY, "Copy" },
		{ CoPageItemPrototypeTreeUI.PASTE, "Paste" },
		{ CoPageItemPrototypeTreeUI.ADD_PROTOTYPE, "Add" },

		{CoPageItemPreferencesUI.COLORS_TAB,				"Colors"},
		{CoPageItemPreferencesUI.STROKES_TAB,				"Dashes and Stripes"},	
		{CoPageItemPreferencesUI.H_AND_J_TAB,				"Hyphenation and Justification"},	
		{CoPageItemPreferencesUI.TYPOGRAPHY_RULE_TAB,		"Character and Paragraph Style Rules"},	
		{CoPageItemPreferencesUI.TAG_CHAINS_TAB,			"Tag chains"},	
		{CoPageItemPreferencesUI.TAG_GROUPS_TAB,			"Tag groups"},	
		{CoPageItemPreferencesUI.AVAILABLE_FONTS_TAB,		"Available fonts"},	
		{CoPageItemPreferencesUI.CHARACTER_PROPERTIES_TAB,		"Characters"},	
		{CoPageItemPreferencesUI.PAGE_SIZES_TAB,		"Page sizes"},

	};	
/**
  Sätter om rb när Locale har ändrats. 
 */
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoPageItemUIStringResources.class.getName());
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
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoPageItemUIStringResources.class.getName());
	rb.resetLookup();
}
}