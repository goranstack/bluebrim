package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.resource.shared.*;

/**
 * @author Göran Stäck 2002-09-14
 *
 */
public interface CoLayoutEditorClientConstants {

	public static class MenuItem extends CoDefMenuItemResource {
		private static int NEXT_ORDINAL = 0;
		private MenuItem() {
			super(NEXT_ORDINAL++);
		}
		protected CoMenuItemResource obtainResource() {
			return CoLayouteditorUIStringResources.getMenuItem(this);
		}
	}

	// menu system decorations
	MenuItem MENU_CONTENT = new MenuItem();

	MenuItem MENU_FILE = new MenuItem();
	MenuItem MENU_FILE_SPAWN = new MenuItem();
	MenuItem MENU_FILE_SPAWN_IN_THIS_WINDOW = new MenuItem();
	MenuItem MENU_FILE_SPAWN_IN_NEW_WINDOW = new MenuItem();
	MenuItem MENU_FILE_PREVIOUS_MODEL = new MenuItem();
	// Added by jowa for layout editor debugging purposes_
	MenuItem MENU_FILE_EXPORT_XML_FILE = new MenuItem();
	MenuItem MENU_FILE_IMPORT_XML_FILE = new MenuItem();
	// End jowa
	MenuItem MENU_FILE_PRINT_SETTINGS = new MenuItem();
	MenuItem MENU_FILE_PRINT = new MenuItem();
	MenuItem MENU_FILE_EXPORT = new MenuItem();
	MenuItem MENU_FILE_EXPORT_EPS = new MenuItem();
	MenuItem MENU_FILE_EXPORT_POSTSCRIPT = new MenuItem();
	MenuItem MENU_FILE_PRINT_MENU = new MenuItem();
	MenuItem MENU_FILE_PRINT_PRINTER = new MenuItem();
	MenuItem MENU_FILE_PRINT_FILE = new MenuItem();
	MenuItem MENU_FILE_PRINT_TYPESETTER = new MenuItem();
	// Standalone layouteditor
	MenuItem MENU_FILE_NEW_DOCUMENT = new MenuItem();
	MenuItem MENU_FILE_OPEN_DOCUMENT = new MenuItem();
	MenuItem MENU_FILE_SAVE_DOCUMENT = new MenuItem();
	MenuItem MENU_FILE_SAVE_DOCUMENT_AS = new MenuItem();
	MenuItem MENU_FILE_EXIT = new MenuItem();

	MenuItem MENU_EDIT = new MenuItem();
	MenuItem MENU_EDIT_UNDO = new MenuItem();
	MenuItem MENU_EDIT_CUT = new MenuItem();
	MenuItem MENU_EDIT_COPY = new MenuItem();
	MenuItem MENU_EDIT_PASTE = new MenuItem();
	MenuItem MENU_EDIT_DELETE = new MenuItem();
	MenuItem MENU_EDIT_SELECT_ALL = new MenuItem();
	MenuItem MENU_EDIT_FIND_REPLACE = new MenuItem();
	MenuItem MENU_EDIT_PREFERENCES = new MenuItem();
	MenuItem MENU_EDIT_PREFERENCES_APPLICATION = new MenuItem();
	MenuItem MENU_EDIT_COLORS = new MenuItem();
	MenuItem MENU_EDIT_HYPHENATIONS = new MenuItem();
	MenuItem MENU_EDIT_DASHES_STRIPES = new MenuItem();

	MenuItem MENU_STYLE = new MenuItem();

	MenuItem MENU_ITEM = new MenuItem();
	MenuItem MENU_ITEM_MODIFY = new MenuItem();
	MenuItem MENU_ITEM_TRAPPING = new MenuItem();
	MenuItem MENU_ITEM_LAYERS = new MenuItem();
	MenuItem MENU_ITEM_DUPLICATE = new MenuItem();
	MenuItem MENU_ITEM_STEP_AND_REPEAT = new MenuItem();
	MenuItem MENU_ITEM_DELETE = new MenuItem();
	MenuItem MENU_ITEM_SEND_TO_BACK = new MenuItem();
	MenuItem MENU_ITEM_SEND_BACKWARD = new MenuItem();
	MenuItem MENU_ITEM_BRING_TO_FRONT = new MenuItem();
	MenuItem MENU_ITEM_BRING_FORWARD = new MenuItem();
	MenuItem MENU_ITEM_SPACE_ALIGN = new MenuItem();
	MenuItem MENU_ITEM_RESHAPE_POLYGONS = new MenuItem();
	MenuItem MENU_ITEM_STICKY_CURVE_POINTS = new MenuItem();
	MenuItem MENU_ITEM_CURVE_CONTINUITY = new MenuItem();
	MenuItem MENU_ITEM_CURVE_CONTINUITY_0 = new MenuItem();
	MenuItem MENU_ITEM_CURVE_CONTINUITY_1 = new MenuItem();
	MenuItem MENU_ITEM_CURVE_CONTINUITY_2 = new MenuItem();

	MenuItem MENU_VIEW = new MenuItem();
	MenuItem MENU_VIEW_OPEN_TREE_VIEW = new MenuItem();
	MenuItem MENU_VIEW_FIT_IN_WINDOW = new MenuItem();
	MenuItem MENU_VIEW_FIT_SELECTION_IN_WINDOW = new MenuItem();
	MenuItem MENU_VIEW_50_PROC = new MenuItem();
	MenuItem MENU_VIEW_75_PROC = new MenuItem();
	MenuItem MENU_VIEW_ACTUAL_SIZE = new MenuItem();
	MenuItem MENU_VIEW_200_PROC = new MenuItem();
	MenuItem MENU_VIEW_SHOW_GUIDES = new MenuItem();
	MenuItem MENU_VIEW_SHOW_PAGE_OUTLINES = new MenuItem();
	MenuItem MENU_VIEW_SHOW_OUTLINES = new MenuItem();
	MenuItem MENU_VIEW_SHOW_BASELINE_GRID = new MenuItem();
	MenuItem MENU_VIEW_SHOW_PAGE_DATA = new MenuItem();
	MenuItem MENU_VIEW_SNAP_TO_GUIDES = new MenuItem();
	MenuItem MENU_VIEW_SHOW_INVISIBLES = new MenuItem();
	MenuItem MENU_VIEW_SHOW_DUMMY_TEXT = new MenuItem();
	MenuItem MENU_VIEW_SHOW_CHILD_LOCK_INDICATOR = new MenuItem();
	MenuItem MENU_VIEW_SHOW_IMAGE_CLIP_INDICATOR = new MenuItem();
	MenuItem MENU_VIEW_SHOW_TEXT_OVERFLOW_INDICATOR = new MenuItem();
	MenuItem MENU_VIEW_SHOW_TOPICS = new MenuItem();
	MenuItem MENU_VIEW_PAINT_IMAGES = new MenuItem();
	MenuItem MENU_VIEW_SHOW_ENTIRE_DESKTOP = new MenuItem();
	MenuItem MENU_VIEW_LOOK = new MenuItem();
	MenuItem MENU_VIEW_TEXT_ANTIALIASING = new MenuItem();
	MenuItem MENU_VIEW_TEXT_FRACTIONALMETRICS = new MenuItem();

	MenuItem MENU_UTILITIES = new MenuItem();
	MenuItem MENU_UTILITIES_CHECK_SPELLING = new MenuItem();
	MenuItem MENU_UTILITIES_SPELL_CHECKING_OPTIONS = new MenuItem();
	MenuItem MENU_UTILITIES_SUGGESTED_HYPHENATION = new MenuItem();
	MenuItem MENU_UTILITIES_INSPECT = new MenuItem();

	MenuItem MENU_HELP = new MenuItem();
	MenuItem MENU_HELP_KB_SHORTCUTS = new MenuItem();

	// 	Fönstertitel
	String LAYOUTEDITOR = "LAYOUTEDITOR";

	// 	Ledtexter
	String SCALE = "SCALE";

	//	Transaktionsbeteckningar
	String POINT_RESHAPE_TRANSACTION = "POINT_RESHAPE_TRANSACTION";
	String POINT_CREATE_TRANSACTION = "POINT_CREATE_TRANSACTION";
	String RESHAPE_TRANSACTION = "RESHAPE_TRANSACTION";
	String CREATE_RESHAPE_TRANSACTION = "CREATE_RESHAPE_TRANSACTION";
	String POSITION_TRANSACTION = "POSITION_TRANSACTION";
	String DELETE_SELECTION_TRANSACTION = "DELETE_SELECTION_TRANSACTION";
	String UPDATE_TEXT_TRANSACTION = "UPDATE_TEXT_TRANSACTION";

	//	Filechooserns OK-knapp
	String MOUNT = "MOUNT";

}
