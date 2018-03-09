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
		{ SET_CORNER_LOCATION_SPEC_INSET_X, "placeringsavst�nd" },
		{ SET_WIDTH_SPEC, "Width Specification" },
		{ SET_HEIGHT_SPEC, "Height Specification" },
		{ SET_ABSOLUTE_WIDTH_SPEC_DISTANCE, "bredd" },
		{ SET_CONTENT_WIDTH_SPEC_ABSOLUTE_OFFSET, "yttre horisontel marginal" },
		{ SET_CONTENT_WIDTH_SPEC_RELATIVE_OFFSET, "yttre horisontel marginal" },
		{ SET_PROPORTIONAL_WIDTH_SPEC_PROPORTION, "proportionell bredd" },
		{ SET_ABSOLUTE_HEIGHT_SPEC_DISTANCE, "h�jd" },
		{ SET_CONTENT_HEIGHT_SPEC_ABSOLUTE_OFFSET, "yttre vertikal marginal" },
		{ SET_CONTENT_HEIGHT_SPEC_RELATIVE_OFFSET, "yttre vertikal marginal" },
		{ SET_PROPORTIONAL_HEIGHT_SPEC_PROPORTION, "proportionell h�jd" },
		{ SET_FILL_STYLE, "Fill Style" },
		{ SET_CYCLIC_BLEND, "Cyclic Blend" },
		{ SET_FILL_SHADE, "tonv�rde, fyllningsf�rg" },
		{ SET_BLEND_SHADE, "tonv�rde, �vertoningsf�rg" },
		{ SET_BLEND_ANGLE, "Blend Angle" },
		{ SET_BLEND_CYCLE_LENGTH, "cykell�ngd" },
		{ SET_FILL_COLOR, "fyllningsf�rg" },
		{ SET_BLEND_COLOR, "�vertoningsf�rg" },
		{ SET_FILL_PATTERN, "fyllnadsm�nster" },
		{ SET_RUN_AROUND_SPEC, "inf�llningsprincip" },
		{ SET_RUN_AROUND_USE_STROKE, "beakta inf�llningsram" },
		{ SET_SHAPE_RUN_AROUND_MARGIN, "inf�llningsmarginal" },
		{ SET_BOUNDS_RUN_AROUND_LEFT_MARGIN, "inf�llningsmarginal" },
		{ SET_BOUNDS_RUN_AROUND_RIGHT_MARGIN, "inf�llningsmarginal" },
		{ SET_BOUNDS_RUN_AROUND_TOP_MARGIN, "inf�llningsmarginal" },
		{ SET_BOUNDS_RUN_AROUND_BOTTOM_MARGIN, "inf�llningsmarginal" },
		{ SET_STROKE_ALIGNMENT, "ramjustering" },
		{ SET_STROKE_SYMMETRY, "ramsymmetri" },
		{ SET_STROKE, "ramm�nster" },
		{ SET_STROKE_EFFECTIVE_SHAPE, "rama effektiva formen" },
		{ SET_STROKE_WIDTH, "rambredd" },
		{ SET_STROKE_FOREGROUND_COLOR, "ramf�rg" },
		{ SET_STROKE_FOREGROUND_SHADE, "ton, ramf�rg" },
		{ SET_STROKE_BACKGROUND_COLOR, "ramf�rg" },
		{ SET_STROKE_BACKGROUND_SHADE, "ton, ramf�rg" },
		{ SET_COLUMN_GRID_DERIVED, "h�rledda kolumner" },
		{ SET_COLUMN_SPACING, "kolumnmellanrum" },
		{ SET_COLUMN_GRID_LEFT_MARGIN, "v�nstermarginal" },
		{ SET_COLUMN_GRID_RIGHT_MARGIN, "h�germarginal" },
		{ SET_COLUMN_GRID_TOP_MARGIN, "topmarginal" },
		{ SET_COLUMN_GRID_BOTTOM_MARGIN, "bottenmarginal" },
		{ SET_COLUMN_COUNT, "kolumnantal" },
		{ SET_BASE_LINE_GRID_DERIVED, "h�rledda baslinjer" },
		{ SET_BASELINE_GRID_Y0, "f�rsta baslinje" },
		{ SET_BASELINE_GRID_DY, "baslinjemellanrum" },
		{ SET_PAGE_ITEM_CONTENT, "inneh�ll" },
		{ ADJUST_HEIGHT_TO_TEXT, "anpassa h�jd till text" },
		{ ADD_ACCEPTED_TAGS, "l�gg till visade taggar" },
		{ REMOVE_ACCEPTED_TAGS, "ta bort visade taggar" },
		{ SET_TEXT_LOCK, "textl�s" },
		{ SET_VERTICAL_ALIGNMENT_TYPE, "vertiakl justering" },
		{ SET_FIRST_BASE_LINE_TYPE, "f�rsta baslinjen" },
		{ SET_TEXT_FIRST_BASE_LINE_OFFSET, "" },
		{ SET_TEXT_VERTICAL_ALIGNMENT_MAX_INTERVAL, "" },
		{ SET_TEXT_TOP_MARGIN, "topmarginal, text" },
		{ SET_TEXT_BOTTOM_MARGIN, "bottenmarginal, text" },
		{ SET_TEXT_LEFT_MARGIN, "v�nstermarginal, text" },
		{ SET_TEXT_RIGHT_MARGIN, "h�germarginal, text" },
		{ SET_TEXT_TAG, "ordningstag, text" },
		{ SET_TEXT, "text" },
		{ SET_TEXT_INSERTION_REQUEST, "montera bokad text" },
		{ SET_BOUNDED_CONTENT_X, "x, inneh�ll" },
		{ SET_BOUNDED_CONTENT_Y, "y, inneh�ll" },
		{ SET_BOUNDED_CONTENT_SCALE_X, "x-skala, inneh�ll" },
		{ SET_BOUNDED_CONTENT_SCALE_Y, "y-skala, inneh�ll" },
		{ SET_BOUNDED_CONTENT_FLIP_X, "x-spegling, inneh�ll" },
		{ SET_BOUNDED_CONTENT_FLIP_Y, "y-spegling, inneh�ll" },
		{ SET_BOUNDED_CONTENT_LOCK, "inneh�llsl�s" },
		{ SET_BOUNDED_CONTENT_POSITION, "position, inneh�ll" },
		{ ADD_CAPTION, "l�gg in bildtext" },
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
		{ SET_CHILDREN_LOCKED, "barnl�s" },
		{ BRING_FORWARD, "flytta fram" },
		{ BRING_TO_FRONT, "l�gg l�ngst fram" },
		{ SEND_BACKWARDS, "flyta bak�t" },
		{ SEND_TO_BACK, "l�gg l�ngst bak" },
		{ REORDER_CURVE, "vr�ng kurva" },
		{ SET_CURVE_CLOSED, "st�ng kurva" },
		{ SET_BOXED_LINE_HORIZONTAL, "orientering, blocklinje" },
		{ SET_BOXED_LINE_MARGIN, "marginal, blocklinje" },
		{ SET_CORNER_RADIUS, "h�rnradie" },
		{ SET_LINE_X1, "x1" },
		{ SET_LINE_Y1, "y1" },
		{ SET_LINE_X2, "x2" },
		{ SET_LINE_Y2, "y2" },
		{ MOVE_CUSTOM_GRID_LINE, "flytta hj�lplinje" },
		{ SET_ACCEPTS_WORKPIECE, "acceptera alster" },
		{ SET_WORKPIECE_LOCK, "alsterl�s" },
		{ SET_WORKPIECE, "alster" },
		{ SET_WORKPIECE_INSERTION_REQUEST, "montera bokat alster" },
		{ MOVE_TO_FIRST_LAYOUT_POSITION, "h�gsta layoutprioritet" },
		{ MOVE_TOWARDS_FIRST_LAYOUT_POSITION, "h�gre layoutprioritet" },
		{ MOVE_TO_LAST_LAYOUT_POSITION, "l�gsta layoutprioritet" },
		{ MOVE_TOWARDS_LAST_LAYOUT_POSITION, "l�gre layoutprioritet" },
		{ SET_PAGE_SIZE, "sidstorlek" },
		{ SET_TOPIC, "avdelning" },
		{ PERFORM_CUSTOM_OPERATION, "anv�ndardefinierad operation" },
	};
/**
  S�tter om rb n�r Locale har �ndrats. 
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
  Svara med det namn som h�r till nyckeln aKey.
  Saknas en resurs f�r aKey s� svara med denna.
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
  S�tter om rb n�r Locale har �ndrats. 
 */
public static void resetBundle ( ) {
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoCommandStringResources.class.getName());
	rb.resetLookup();
}
}