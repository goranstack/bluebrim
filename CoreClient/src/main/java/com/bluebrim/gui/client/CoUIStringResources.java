package com.bluebrim.gui.client;

import java.util.MissingResourceException;

import com.bluebrim.base.shared.CoConstants;
import com.bluebrim.resource.shared.CoOldResourceBundle;

/**
 	Resursklass som innehåller lokaliserade strängar som bl a används 
 	av de klasser vars instanser behöver använda typnamn när
 	de visar upp sig.<br>
 	<blockquote><pre>
 		CoStringResources.getName(PUBLICATION);
 	</pre>
 	</blockquote>
	
 */
public class CoUIStringResources extends CoOldResourceBundle implements CoConstants, CoUIConstants {
	private static CoOldResourceBundle rb	= null;
	static final Object[][] contents =
	{
		{DEFAULT_NUMERICAL_MACROS, new String[] {"#","[0-9]"}},
		{DEFAULT_ALPHA_NUMERICAL_MACROS, new String[] {"@","[a-zA-Z]"}},
		
		{MESSAGE,"Message"},
		{BROADCAST_MESSAGE, "Broadcast system message"},
		{FILE_MENU,"File"},
		{FILE_MENU_MNEMONIC,"F"},
		{SAVE_AS,"Save as"},
		{SAVE_AS_ITEM,"Save as..."},
		{SAVE_WORKSHEET,"Save worksheet"},

		{EDIT,"Edit"},
		{ARCHIVE, "Archive"},
		{CANCEL,"Cancel"},
		{OK,"OK"},
		{READY,"OK"},
		{SAVE_CHANGES,"Save changes"},
		{YES,"Yes"},
		{NO,"No"},
		{UNTITLED,"Untitled"},
		{NOT_EDITABLE, "Not editable"},
		{PAGES,"pages"},
		{NAME, "Name"},
		{SHORT_NAME, "Shortname"},
		{ADD_ITEM,"Add"},
		{ADD_ITEM_MNEMONIC, "A"},
		{REMOVE_ITEM,"Remove"},
		{ADD_ITEM_LABEL,"Add new {0}"},
		{REMOVE_ITEM_LABEL,"Remove selected {0}"},
		{DATE, "date"},
		{TIMESTAMP, "time"},
		{NUMBER,"number"},
		{CURRENCY,"amount"},
		{CoUIConstants.PERCENT, "percentage"},
		{CoConverter.BASIC_CONVERTER_PARSE_ERROR, "The {0} {1} is in a wrong format."},
		{CONVERTER_PARSE_ERROR,"The {0} {1} is in a wrong format. Please use the format {2} instead."},
		{CLEAR, "Erase"},
		{REMOVE_ELEMENT_MESSAGE, "Do you want to remove this?"},
		{RENAME, "Rename"},

		// MNEMONICS
		{OK_MNEMONIC,"O"},
		{READY_MNEMONIC,"R"},
		{CANCEL_MNEMONIC,"C"},
		{YES_MNEMONIC,"Y"},
		{NO_MNEMONIC,"N"},
		{CLEAR_MNEMONIC, "E"},
		
		// Misc UI
		{MOVE_UP, "Move up"},
		{MOVE_DOWN, "Move down"},
		{CoUIConstants.NONE_CHOSEN, "(None)"},
		
		//Worksheet
		{WORKSHEET, "Worksheet"},
		{UNTITLED_WORKSHEET, "Untitled worksheet"},
		{OPEN_COMPONENT_FRAME, "Open component palette..."},
		{CLOSE_COMPONENT_FRAME, "Close component palette"},
		{OPEN_QUERY_FRAME, "Open query palette..."},
		{CLOSE_QUERY_FRAME, "Close query palette"},
		{LAYOUT_MENU,	"Layout"},
		{ADJUST_SIZE,	"Adjust size"},
		{FRAME_STYLE,	"Show as windows"},
		{PALETTE_STYLE,	"Show as panels"},
		{PACK_FRAMES,	"Pack"},
		{ARRANGE_FRAMES,"Arrange"},

		// CoEnumeration
		{ENUMERATED_VALUES,			"Possible values"},
	
		//FileChooser
		{ACCEPT_ALL_FILES,			"All Files (*.*)"},
		{SAVE,						"Save"},
		{SAVE_MNEMONIC,				"S"},
		{OPEN,						"Open"},
		{UPDATE,					"Update"},
		{HELP,						"Help"},
		{ABORT_FILE_CHOOSER_DIALOG,	"Abort file chooser dialog."},
		{SAVE_SELECTED_FILE,		"Save selected file."},
		{OPEN_SELECTED_FILE,		"Open selected file."},
		{UPDATE_DIRECTORY_LISTINGS,	"Update directory listing."},
		{FILECHOOSER_HELP,			"FileChooser help."},

		{LOOK_IN,					"Look in"},
		{FILE_NAME,					"File name"},
		{FILES_OF_TYPE,				"Files of type"},
		{UP_ONE_LEVEL,				"Up one level"},
		{NEW_FOLDER,				"Create New Folder"},
		{LIST,						"List"},
		{DETAILED_LIST,				"Detailed list"},
		{HOME,						"Home"},

		 //CoTimePeriodUI
		{OK_BUTTON,							"Ok"},
		{CANCEL_BUTTON,						"Cancel"},
		{TIME_PERIOD, 						"Time period"},
		{TIME_PERIOD_DELIMITER,				"\u2212"},		
		{TIME_PERIOD_FROM_DATE, 			"From."},
		{TIME_PERIOD_TO_DATE, 				"to"},
		
		{CoGUI.QUESTION,		"Question"},
		{CoGUI.WARNING,			"Warning"},
		{CoGUI.MESSAGE,			"Message"},
		{CoGUI.ERROR,			"Error"},

		//Bookmarks
		{SAVE_IN_FOLDER,	"Save in folder"},
		{SAVE_AS_BOOKMARK,	"Save as bookmark"},
		{OPEN_BOOKMARK,		"Open bookmark"},
		{SAVE_IN_EXISTING,	"Save in existing"},
		{SAVE_IN_NEW,		"Save in new"},		
		{BOOKMARK,			"Bookmark"},
		{BOOKMARK_NAME,		"Bookmark name"},
		{FOLDER_NAME,		"Folder name"},
		

		// CoFolderViewUI
		{FOLDER_LIST,		"Folders"},

		{IDENTITY,				"ID"},
		{CREATION_DATE,			"Created"},
		{LAST_MODIFIED_DATE,	"Last Modified"},
		{CREATED_BY,			"Created by"},
		{LAST_MODIFIED_BY,		"Last modified by"},
		
		// Menus
		{PRINT,				"Print ..."},
		{PREVIEW_PRINT,		"Preview print ..."},
		{PRINT_OPTIONS,		"Print options ..."},
		
		// CoPrintTableOptionUI
		{LANDSCAPE,           "Landscape"},
		{TABLE_STYLE,         "Table Style"},
		{PREVIEW_IS_MISSING,  "Preview is missing"},
		{COLUMN_PRINT_PREVIEW,"Column print preview"},
		{PAPER_PRINT_PREVIEW, "Paper print preview"},
	    {PAPER_MARGIN,        "Paper margin"},

	    
	    {SAVE_AS_XML,  "Save as XML"},
	    {LOAD_FROM_XML,  "Load from XML"},
	    {UNKNOWN,  "Unknown"},
	};



/**
  Sätter om rb när Locale har ändrats. 
 */
protected static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)getBundle(CoUIStringResources.class.getName());
	return rb;
}
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
	rb = null;
	rb = getBundle();
	rb.resetLookup();
}
}