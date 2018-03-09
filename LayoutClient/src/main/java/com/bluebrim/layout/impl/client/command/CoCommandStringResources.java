package com.bluebrim.layout.impl.client.command;

import java.util.*;

import com.bluebrim.resource.shared.*;

public class CoCommandStringResources extends CoOldResourceBundle implements CoCommandConstants
{
	public static CoOldResourceBundle rb = null;
	
	static final Object[][] contents =
	{
		{ PLACE_BOOKED_IMAGE, "Place Booked Image" },
		{ PLACE_BOOKED_TEXT, "Place Booked Text" },
		{ PLACE_BOOKED_LAYOUT, "Place Booked Layout" },
		{ PLACE_BOOKED_WORKPIECE, "Place Booked Workpiece" },

		{ SET_EMBEDDED_PATH_SHAPE, "Embedded Path Shape" },
		{ SET_COLUMN_GRID_SPREAD, "Column Grid Spred" },
		{ SET_PAGE_SPREAD, "Spread" },
		{ SET_COLUMN_GRID_LEFT_OUTSIDE_SENSITIVE, "Consider Spread" },
		{ SET_RECURSIVE_LEVEL_COUNT, "Recursive Level Count" },
		{ SET_POSITION, "Position" },
		{ SET_DIMENSIONS, "Size" },
		{ RESHAPE, "Reshape" },
	//	{ SET_SHAPE, "form" },
		{ SET_DO_RUN_AROUND, "Run Around" },
		{ SET_ROTATION, "Rotation" },
		{ SET_LOCATION_LOCKED, "Location Locked" },
		{ SET_DIMENSIONS_LOCKED, "Dimension Locked" },
		{ SET_SUPRESS_PRINTOUT, "Supress Printout" },
		{ SET_LAYOUT_MANAGER, "Layout Manager" },
		{ SET_ROW_LAYOUT_MANAGER_GAP, "Row Layout Manager Gap" },
		{ SET_RECTANGLE_LAYOUT_MANAGER_GAP, "Rectangle Layout Manager Gap" },
		{ SET_DISTANCE_CALCULATOR, "Distance Calculator" },
		{ SET_LOCATION_SPEC, "Location Specification" },
		{ SET_LOCATION_SPEC_AGGRESSIVE, "Aggresive Location" },
		{ SET_CORNER_LOCATION_SPEC_INSET_X, "placeringsavstånd" },
		{ SET_WIDTH_SPEC, "Width Specification" },
		{ SET_HEIGHT_SPEC, "Height Specification" },
		{ SET_ABSOLUTE_WIDTH_SPEC_DISTANCE, "bredd" },
		{ SET_CONTENT_WIDTH_SPEC_ABSOLUTE_OFFSET, "yttre horisontel marginal" },
		{ SET_CONTENT_WIDTH_SPEC_RELATIVE_OFFSET, "yttre horisontel marginal" },
		{ SET_PROPORTIONAL_WIDTH_SPEC_PROPORTION, "proportionell bredd" },
		{ SET_ABSOLUTE_HEIGHT_SPEC_DISTANCE, "höjd" },
		{ SET_CONTENT_HEIGHT_SPEC_ABSOLUTE_OFFSET, "yttre vertikal marginal" },
		{ SET_CONTENT_HEIGHT_SPEC_RELATIVE_OFFSET, "yttre vertikal marginal" },
		{ SET_PROPORTIONAL_HEIGHT_SPEC_PROPORTION, "proportionell höjd" },
		{ SET_FILL_STYLE, "Fill Style" },
		{ SET_CYCLIC_BLEND, "Cyclic Blend" },
		{ SET_FILL_SHADE, "tonvärde, fyllningsfärg" },
		{ SET_BLEND_SHADE, "tonvärde, övertoningsfärg" },
		{ SET_BLEND_ANGLE, "Blend Angle" },
		{ SET_BLEND_CYCLE_LENGTH, "cykellängd" },
		{ SET_FILL_COLOR, "fyllningsfärg" },
		{ SET_BLEND_COLOR, "övertoningsfärg" },
		{ SET_FILL_PATTERN, "fyllnadsmönster" },
		{ SET_RUN_AROUND_SPEC, "infällningsprincip" },
		{ SET_RUN_AROUND_USE_STROKE, "beakta infällningsram" },
		{ SET_SHAPE_RUN_AROUND_MARGIN, "infällningsmarginal" },
		{ SET_BOUNDS_RUN_AROUND_LEFT_MARGIN, "infällningsmarginal" },
		{ SET_BOUNDS_RUN_AROUND_RIGHT_MARGIN, "infällningsmarginal" },
		{ SET_BOUNDS_RUN_AROUND_TOP_MARGIN, "infällningsmarginal" },
		{ SET_BOUNDS_RUN_AROUND_BOTTOM_MARGIN, "infällningsmarginal" },
		{ SET_STROKE_ALIGNMENT, "ramjustering" },
		{ SET_STROKE_SYMMETRY, "ramsymmetri" },
		{ SET_STROKE, "rammönster" },
		{ SET_STROKE_EFFECTIVE_SHAPE, "rama effektiva formen" },
		{ SET_STROKE_WIDTH, "rambredd" },
		{ SET_STROKE_FOREGROUND_COLOR, "ramfärg" },
		{ SET_STROKE_FOREGROUND_SHADE, "ton, ramfärg" },
		{ SET_STROKE_BACKGROUND_COLOR, "ramfärg" },
		{ SET_STROKE_BACKGROUND_SHADE, "ton, ramfärg" },
		{ SET_COLUMN_GRID_DERIVED, "härledda kolumner" },
		{ SET_COLUMN_SPACING, "kolumnmellanrum" },
		{ SET_COLUMN_GRID_LEFT_MARGIN, "vänstermarginal" },
		{ SET_COLUMN_GRID_RIGHT_MARGIN, "högermarginal" },
		{ SET_COLUMN_GRID_TOP_MARGIN, "topmarginal" },
		{ SET_COLUMN_GRID_BOTTOM_MARGIN, "bottenmarginal" },
		{ SET_COLUMN_COUNT, "kolumnantal" },
		{ SET_BASE_LINE_GRID_DERIVED, "härledda baslinjer" },
		{ SET_BASELINE_GRID_Y0, "första baslinje" },
		{ SET_BASELINE_GRID_DY, "baslinjemellanrum" },
		{ SET_PAGE_ITEM_CONTENT, "innehåll" },
		{ ADJUST_HEIGHT_TO_TEXT, "anpassa höjd till text" },
		{ ADD_ACCEPTED_TAGS, "lägg till visade taggar" },
		{ REMOVE_ACCEPTED_TAGS, "ta bort visade taggar" },
		{ SET_TEXT_LOCK, "textlås" },
		{ SET_VERTICAL_ALIGNMENT_TYPE, "vertiakl justering" },
		{ SET_FIRST_BASE_LINE_TYPE, "första baslinjen" },
		{ SET_TEXT_FIRST_BASE_LINE_OFFSET, "" },
		{ SET_TEXT_VERTICAL_ALIGNMENT_MAX_INTERVAL, "" },
		{ SET_TEXT_TOP_MARGIN, "topmarginal, text" },
		{ SET_TEXT_BOTTOM_MARGIN, "bottenmarginal, text" },
		{ SET_TEXT_LEFT_MARGIN, "vänstermarginal, text" },
		{ SET_TEXT_RIGHT_MARGIN, "högermarginal, text" },
		{ SET_TEXT_TAG, "ordningstag, text" },
		{ SET_TEXT, "text" },
		{ SET_TEXT_INSERTION_REQUEST, "montera bokad text" },
		{ SET_BOUNDED_CONTENT_X, "x, innehåll" },
		{ SET_BOUNDED_CONTENT_Y, "y, innehåll" },
		{ SET_BOUNDED_CONTENT_SCALE_X, "x-skala, innehåll" },
		{ SET_BOUNDED_CONTENT_SCALE_Y, "y-skala, innehåll" },
		{ SET_BOUNDED_CONTENT_FLIP_X, "x-spegling, innehåll" },
		{ SET_BOUNDED_CONTENT_FLIP_Y, "y-spegling, innehåll" },
		{ SET_BOUNDED_CONTENT_LOCK, "innehållslås" },
		{ SET_BOUNDED_CONTENT_POSITION, "position, innehåll" },
		{ ADD_CAPTION, "lägg in bildtext" },
		{ REMOVE_CAPTION, "ta bort bildtext" },
		{ SET_CAPTION_POSITION, "position, bildtext" },
		{ ADJUST_BOUNDED_CONTENT, "anpassa" },
		{ SET_IMAGE_TAG, "ordningstag, bild" },
		{ SET_IMAGE, "bild" },
		{ SET_IMAGE_INSERTION_REQUEST, "montera bokad bild" },
		{ SET_LAYOUT_TAG, "ordningstag. layout" },
		{ SET_LAYOUT, "layout" },
		{ SET_LAYOUT_INSERTION_REQUEST, "montera bokad layout" },
		{ SET_LAYOUT_ORDER, "layoutordning" },
		{ SET_CHILDREN_LOCKED, "barnlås" },
		{ BRING_FORWARD, "flytta fram" },
		{ BRING_TO_FRONT, "lägg längst fram" },
		{ SEND_BACKWARDS, "flyta bakåt" },
		{ SEND_TO_BACK, "lägg längst bak" },
		{ REORDER_CURVE, "vräng kurva" },
		{ SET_CURVE_CLOSED, "stäng kurva" },
		{ SET_BOXED_LINE_HORIZONTAL, "orientering, blocklinje" },
		{ SET_BOXED_LINE_MARGIN, "marginal, blocklinje" },
		{ SET_CORNER_RADIUS, "hörnradie" },
		{ SET_LINE_X1, "x1" },
		{ SET_LINE_Y1, "y1" },
		{ SET_LINE_X2, "x2" },
		{ SET_LINE_Y2, "y2" },
		{ MOVE_CUSTOM_GRID_LINE, "flytta hjälplinje" },
		{ SET_ACCEPTS_WORKPIECE, "acceptera alster" },
		{ SET_WORKPIECE_LOCK, "alsterlås" },
		{ SET_WORKPIECE, "alster" },
		{ SET_WORKPIECE_INSERTION_REQUEST, "montera bokat alster" },
		{ MOVE_TO_FIRST_LAYOUT_POSITION, "högsta layoutprioritet" },
		{ MOVE_TOWARDS_FIRST_LAYOUT_POSITION, "högre layoutprioritet" },
		{ MOVE_TO_LAST_LAYOUT_POSITION, "lägsta layoutprioritet" },
		{ MOVE_TOWARDS_LAST_LAYOUT_POSITION, "lägre layoutprioritet" },
		{ SET_PAGE_SIZE, "sidstorlek" },
		{ SET_TOPIC, "avdelning" },
		{ PERFORM_CUSTOM_OPERATION, "användardefinierad operation" },
	};
/**
  Sätter om rb när Locale har ändrats. 
 */
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoCommandStringResources.class.getName());
	return rb;
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
	System.err.println( e );
		return aKey;
	}			
}
/**
  Sätter om rb när Locale har ändrats. 
 */
public static void resetBundle ( ) {
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoCommandStringResources.class.getName());
	rb.resetLookup();
}
}