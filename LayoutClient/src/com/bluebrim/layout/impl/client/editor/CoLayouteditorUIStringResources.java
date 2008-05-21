package com.bluebrim.layout.impl.client.editor;

import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.resource.shared.*;

/**
 * @author Dennis
 * @version $Id: CoLayouteditorUIStringResources.java,v 1.1.1.1 2005/01/01 21:32:28 goran Exp $
 */
public class CoLayouteditorUIStringResources extends CoResources implements CoLayoutEditorClientConstants {
	public static CoResources RB = null;

	
	static final Object[][] contents =
	{
		{ CoEditText.SCALE, "Scale" },
		
		{ CoLayoutEditorConfiguration.EDITORIAL_CONFIGURATION, "Editorial" },
		{ CoLayoutEditorConfiguration.AD_CONFIGURATION, "Ad" },

		
		{ CoLayoutEditor.POPUP_SHAPE_PAGE_ITEM_MODIFY, "Modify" },
		{ CoLayoutEditor.POPUP_SHAPE_PAGE_ITEM_SPAWN, "Open" },
		{ CoLayoutEditor.POPUP_SHAPE_PAGE_ITEM_SPAWN_SPAWN_IN_THIS_WINDOW, "in this window" },
		{ CoLayoutEditor.POPUP_SHAPE_PAGE_ITEM_SPAWN_SPAWN_IN_NEW_WINDOW, "in new window" },
		{ CoLayoutEditor.POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_WITH_TO_FIT_HEIGHT_TO_IMAGE, "Adjust image width, then height to image" },
		{ CoLayoutEditor.POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_FIT_KEEP_ASPECT_RATIO, "Adjust image, keep aspect ratio" },
		{ CoLayoutEditor.POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_FIT, "Adjust image" },
		{ CoLayoutEditor.POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_SCALED_IMAGE_SIZE, "Adjust to current image size" },
		{ CoLayoutEditor.POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_IMAGE_SIZE, "Adjust to image size" },
		{ CoLayoutEditor.POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_EMBEDDED_PATH, "Embedded path" },
		{ CoLayoutEditor.POPUP_IMAGE_CONTENT_GET_PICTURE, "Import image ..." },
		{ CoLayoutEditor.POPUP_TEXT_CONTENT_ADJUST_HEIGHT_TO_TEXT, "Adjust height to text" },
		{ CoLayoutEditor.POPUP_TEXT_CONTENT_EDIT, "Edit ..." },
		{ CoLayoutEditor.POPUP_ABSTRACT_LAYOUT_CONTENT_EDIT, "Edit ..." },
		{ CoLayoutEditor.POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_WITH_TO_FIT_HEIGHT_TO_LAYOUT, "Adjust layout width, then height to layout" },
		{ CoLayoutEditor.POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_TO_FIT_KEEP_ASPECT_RATIO, "Adjust layout, keep aspect ratio" },
		{ CoLayoutEditor.POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_TO_FIT, "Adjust layout" },
		{ CoLayoutEditor.POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_TO_SCALED_LAYOUT_SIZE, "Adjust to current layout size" },
		{ CoLayoutEditor.POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_TO_LAYOUT_SIZE, "Adjust to layout size" },
						
						
		{  MENU_CONTENT, menuItem("Contents", 'C') },
		
		{  MENU_FILE, menuItem("File", 'F') },		
			{ MENU_FILE_SPAWN, menuItem("Open", ' ') },		
			{ MENU_FILE_SPAWN_IN_THIS_WINDOW, menuItem("in this window", 't') },		
			{ MENU_FILE_SPAWN_IN_NEW_WINDOW, menuItem("in new window", 'n') },
			{ MENU_FILE_PREVIOUS_MODEL, menuItem("Back", 'B') },
	// Added by jowa for layout editor debugging purposes.
			{ MENU_FILE_EXPORT_XML_FILE, menuItem("Export layout to an XML file...", 'e') },
			{ MENU_FILE_IMPORT_XML_FILE, menuItem("Import an XML layout, replacing the current layout...", 'i') },
	// End jowa
			{ MENU_FILE_PRINT_SETTINGS, menuItem("Print settings...", 's') },
			{ MENU_FILE_PRINT, menuItem("Print...", 'P') },
			{ MENU_FILE_EXPORT, menuItem("Export", 'x') },
			{ MENU_FILE_EXPORT_EPS, menuItem("as EPS...", 'E') },
			{ MENU_FILE_EXPORT_POSTSCRIPT, menuItem("as Postscript...", 'P') },
			{ MENU_FILE_PRINT_MENU, menuItem("Generate PS", 'r') },
			{ MENU_FILE_PRINT_PRINTER, menuItem("to printer", 'p') },
			{ MENU_FILE_PRINT_FILE, menuItem("to file...", 'f') },
			{ MENU_FILE_PRINT_TYPESETTER, menuItem("to RIP", 'R') },
	// Standalone layouteditor
			{ MENU_FILE_NEW_DOCUMENT, menuItem("New", 'N', keyStroke(KeyEvent.VK_N, CTRL)) },
			{ MENU_FILE_OPEN_DOCUMENT, menuItem("Open...", 'O', keyStroke(KeyEvent.VK_O, CTRL)) },
			{ MENU_FILE_SAVE_DOCUMENT, menuItem("Save", 'S', keyStroke(KeyEvent.VK_S, CTRL)) },
			{ MENU_FILE_SAVE_DOCUMENT_AS, menuItem("Save as...", 'a', keyStroke(KeyEvent.VK_S, CTRL | ALT )) },
			{ MENU_FILE_EXIT, menuItem("Exit", 'x', keyStroke(KeyEvent.VK_Q, CTRL)) },

		{ MENU_EDIT, menuItem("Edit", 'E') },
			{ MENU_EDIT_UNDO, menuItem("Undo", 'U', keyStroke(KeyEvent.VK_BACK_SPACE, ALT)) },		
			{ MENU_EDIT_CUT, menuItem("Cut", 'T', keyStroke(KeyEvent.VK_X, CTRL)) },		
			{ MENU_EDIT_COPY, menuItem("Copy", 'C', keyStroke(KeyEvent.VK_C, CTRL)) },
			{ MENU_EDIT_PASTE, menuItem("Paste", 'P', keyStroke(KeyEvent.VK_V, CTRL)) },
			{ MENU_EDIT_DELETE, menuItem("Delete", 'D', keyStroke(KeyEvent.VK_DELETE, 0)) },		
			{ MENU_EDIT_SELECT_ALL, menuItem("Select All", 'A', keyStroke(KeyEvent.VK_A, CTRL)) },		
			{ MENU_EDIT_FIND_REPLACE, menuItem("Find/replace", 'F', keyStroke(KeyEvent.VK_F, CTRL)) },
			{ MENU_EDIT_PREFERENCES, menuItem("Preferences", 'N') },
				{ MENU_EDIT_PREFERENCES_APPLICATION, menuItem("Application...", 'A', keyStroke(KeyEvent.VK_Y, CTRL | ALT | SHIFT )) },
			{ MENU_EDIT_COLORS, menuItem("Colors...", 'R', keyStroke(KeyEvent.VK_F12, SHIFT )) },
			{ MENU_EDIT_HYPHENATIONS, menuItem("Hyphenations...", 'H', keyStroke(KeyEvent.VK_F11, CTRL | SHIFT)) },
			{ MENU_EDIT_DASHES_STRIPES, menuItem("Dashes & Stripes...", 'S') },
					
		{ MENU_STYLE, menuItem("Style", 'S') },
		
		{ MENU_ITEM, menuItem("Item", 'I') },
			{ MENU_ITEM_MODIFY, menuItem("Modify...", 'M') },
			{ MENU_ITEM_TRAPPING, menuItem("Trapping", 'T') },
			{ MENU_ITEM_LAYERS, menuItem("Layers", 'L') },
			{ MENU_ITEM_DUPLICATE, menuItem("Duplicate", 'T', keyStroke(KeyEvent.VK_D, CTRL)) },
			{ MENU_ITEM_STEP_AND_REPEAT, menuItem("Step and Repeat...", 'S', keyStroke(KeyEvent.VK_D, CTRL | SHIFT )) },
			{ MENU_ITEM_DELETE, menuItem("Delete", 'D') },
  			{ MENU_ITEM_SEND_TO_BACK, menuItem("Send to Back", 'N') },
			{ MENU_ITEM_SEND_BACKWARD, menuItem("Send Backward", 'K') },
			{ MENU_ITEM_BRING_TO_FRONT, menuItem("Bring to Front", 'I') },
			{ MENU_ITEM_BRING_FORWARD, menuItem("Bring Forward", 'B') },
			{ MENU_ITEM_SPACE_ALIGN, menuItem("Distribute...", 'u') },
			{ MENU_ITEM_RESHAPE_POLYGONS, menuItem("Reshape polygons", 'R') },
			{ MENU_ITEM_STICKY_CURVE_POINTS, menuItem("Control points sticks to curve points", 's') },
			{ MENU_ITEM_CURVE_CONTINUITY, menuItem("Curve continuity", 'C') },
					{ MENU_ITEM_CURVE_CONTINUITY_0, menuItem("Curve", 'C') },
					{ MENU_ITEM_CURVE_CONTINUITY_1, menuItem("First derivative", 'F') },
					{ MENU_ITEM_CURVE_CONTINUITY_2, menuItem("Second derivative", 'S') },
		
		{ MENU_VIEW, menuItem("View", 'V') },
			{ MENU_VIEW_OPEN_TREE_VIEW, menuItem("Tree view...", 'T') },
			{ MENU_VIEW_FIT_IN_WINDOW, menuItem("Fit in Window", 'F', keyStroke(KeyEvent.VK_0, CTRL)) },
			{ MENU_VIEW_FIT_SELECTION_IN_WINDOW, menuItem("Fit selection in Window", 'i', keyStroke(KeyEvent.VK_0, CTRL | SHIFT )) },
			{ MENU_VIEW_50_PROC, menuItem("50%", '5') },
			{ MENU_VIEW_75_PROC, menuItem("75%", '7') },
			{ MENU_VIEW_ACTUAL_SIZE, menuItem("Actual Size", 'A', keyStroke(KeyEvent.VK_1, CTRL)) },
			{ MENU_VIEW_200_PROC, menuItem("200%", '2') },
			{ MENU_VIEW_SHOW_GUIDES, menuItem("Show Guides", 'G', keyStroke(KeyEvent.VK_F11, SHIFT)) },
			{ MENU_VIEW_SHOW_PAGE_OUTLINES, menuItem("Show Page Outlines", 'P')},
			{ MENU_VIEW_SHOW_OUTLINES, menuItem("Show Outlines", 'O')},
			{ MENU_VIEW_SHOW_BASELINE_GRID, menuItem("Show Baseline Grid", 'B', keyStroke(KeyEvent.VK_F7, CTRL)) },		
			{ MENU_VIEW_SHOW_PAGE_DATA, menuItem("Show Page Information", 'P')},
			{ MENU_VIEW_SNAP_TO_GUIDES, menuItem("Snap to guides", 'S') },
			{ MENU_VIEW_SHOW_INVISIBLES, menuItem("Show invisibles", 'I', keyStroke(KeyEvent.VK_I, CTRL)) },
			{ MENU_VIEW_SHOW_DUMMY_TEXT, menuItem("Show text lines", 'T', keyStroke(KeyEvent.VK_T, CTRL)) },
			{ MENU_VIEW_SHOW_CHILD_LOCK_INDICATOR, menuItem("Child lock", 'l')},
			{ MENU_VIEW_SHOW_IMAGE_CLIP_INDICATOR, menuItem("Show image clipped icon", 'c')},
			{ MENU_VIEW_SHOW_TEXT_OVERFLOW_INDICATOR, menuItem("Show text overflow icon", 't')},
			{ MENU_VIEW_SHOW_TOPICS, menuItem("Show sections", 's')},
			{ MENU_VIEW_PAINT_IMAGES, menuItem("Show images", 'i')},
			{ MENU_VIEW_SHOW_ENTIRE_DESKTOP, menuItem("Show entire desktop", 'e') },
			{ MENU_VIEW_LOOK, menuItem("Look", 'L') },
			{ MENU_VIEW_TEXT_ANTIALIASING, menuItem("Text antialiasing", 'a') },
			{ MENU_VIEW_TEXT_FRACTIONALMETRICS, menuItem("Fractional metrics", 'F') },
			
		{ MENU_UTILITIES, menuItem("Utilities", 'U') },
			{ MENU_UTILITIES_CHECK_SPELLING, menuItem("Check Spelling", 'C') },
			{ MENU_UTILITIES_SPELL_CHECKING_OPTIONS, menuItem("Spell checking options", 'o') },
			{ MENU_UTILITIES_SUGGESTED_HYPHENATION, menuItem("Suggested Hyphenation...", 'H', keyStroke(KeyEvent.VK_H, CTRL))  },
			{ MENU_UTILITIES_INSPECT, menuItem("Inspect selected object", 'I') },

		{ MENU_HELP, menuItem("Help", 'H') },
		{ MENU_HELP_KB_SHORTCUTS, menuItem("Keyboard shortcuts", 'K') },

		
// 	Fönstertitel
		{LAYOUTEDITOR,			"Layout Editor"},

// 	Ledtexter
		{SCALE,					"Scale"},
		
//	Transaktionsbeteckningar
		{POINT_RESHAPE_TRANSACTION,"Reshape a line"},
		{POINT_CREATE_TRANSACTION,"Create a line"},
		{RESHAPE_TRANSACTION,"Reshape"},
		{CREATE_RESHAPE_TRANSACTION,"Create"},
		{POSITION_TRANSACTION,"Reposition"},
		{DELETE_SELECTION_TRANSACTION,"Delete selection"},
		{UPDATE_TEXT_TRANSACTION,"Update text"},
		
		//	Filechooserns OK-knapp
		{MOUNT,"Get it"},

		// 	Modify dialog
		{ CoLayoutEditor.ZOOM, "Zoom" },

		{ CoCheckSpelling.SPELL_CHECK_FAILED_TITLE, "" },
		{ CoCheckSpelling.SPELL_CHECK_FAILED_MESSAGE, "Spell checking can not be performed since the text is locked." },
		{ CoCheckSpelling.SPELL_CHECK_FAILED_RETRY, "Retry" },
		{ CoCheckSpelling.SPELL_CHECK_FAILED_CANCEL, "Skip text" },

		{ CoLayoutEditorPrefsUI.LENGTH_UNIT, "Length unit" },
		
		{ CoLayoutEditor.APPLICATION_PREFS, "Application preferences" },

		{ CoDuplicateDialog.COUNT, "Count" },
		{ CoDuplicateDialog.DX, "X-displacement" },
		{ CoDuplicateDialog.DY, "Y-displacement" },
		{ CoDuplicateDialog.OK, "Ok" },
		{ CoDuplicateDialog.CANCEL, "Cancel" },

		{ CoLayoutEditor.CREATE_TEMPLATE, "Create template" },

		{ CoLayoutEditor.TEMPLATES, "Templates" },

		{ CoLayoutEditor.KEYBOARD_SHORTCUTS_HELP_URL, "kb.html" },

		// com.bluebrim.layoutmanager.impl.server.CoRectangleLayoutManager properties
		{CoDistRectangleIF.RECTANGLE_DIST_CALC,"Rectangle"},
		{CoDistTriangleIF.TRIANGLE_DIST_CALC,"Triangle"},
		{CoDistVerticalIF.VERTICAL_DIST_CALC,"Vertical"},
		{CoDistHorizontalIF.HORIZONTAL_DIST_CALC,"Horizontal"},
		{CoDistConvexIF.CONVEX_DIST_CALC,"Convex"},
		{CoDistConcaveIF.CONCAVE_DIST_CALC,"Concave"},
		{CoRectangleLayoutManagerPanel.TYPE,"Layout as:"},
			
	};

	public Object[][] getContents() {
		return contents;
	}

	public static CoResources getBundle() {
		if (RB == null) {
			RB = getBundle(CoLayouteditorUIStringResources.class);
		}
		return RB;
	}

	public static String getName(String key) {
		try {
			return getBundle().getString(key);
		} catch (MissingResourceException mre) {
			return key;
		}
	}

	public static CoMenuItemResource getMenuItem(Object key) {
		return (CoMenuItemResource) getBundle().get(key);
	}

	public static char getChar(String key) {
		try {
			return getBundle().getString(key).charAt(0);
		} catch (MissingResourceException e) {
			return key.charAt(0);
		}
	}
	
	public static Integer getInteger(String key) {
		try {
			return (Integer) getBundle().getObject(key);
		} catch (MissingResourceException e) {
			return null;
		}
	}

	public static KeyStroke getKeyStroke(String key) {
		try {
			return (KeyStroke) getBundle().get(key);
		} catch (MissingResourceException e) {
			return null;
		}
	}

}