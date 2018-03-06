package com.bluebrim.gui.client;


/**
	Subklass till CoStringResources med motsvarande svenska str�ngar.
 */
public class CoUIStringResources_sv extends CoUIStringResources {
	static final Object[][] contents =
	{

		{DEFAULT_ALPHA_NUMERICAL_MACROS, new String[] {"@","[a-z,�,�,�,A-Z,�,�,�]"}},

		{MESSAGE,"Meddelande"},
		{BROADCAST_MESSAGE,"System meddelande"},
		{FILE_MENU,"Arkiv"},
		{FILE_MENU_MNEMONIC,"A"},
		{HELP_MENU,"Hj�lp"},
		{SAVE_AS,"Spara som"},
		{SAVE_AS_ITEM,"Spara som..."},
		{SAVE_WORKSHEET,"Spara arbetsyta"},

		{EDIT,"�ndra"},
		{ARCHIVE, "Arkivera"},
		{CANCEL,"Avbryt"},
		{OK,"OK"},
		{CONTINUE,"Forts�tt"},
		{READY,"Klar"},
		{SAVE_CHANGES,"Spara �ndringar"},
		{YES,"Ja"},
		{NO,"Nej"},
		{UNTITLED,"Namnl�s"},
		{NOT_EDITABLE, "Inte redigerbar"},
		{PAGES,"sidor"},
		{NAME, "Namn"},
		{SHORT_NAME, "Kortnamn"},
		{ADD_ITEM,"L�gg till"},
		{ADD_ITEM_MNEMONIC, "L"},
		{REMOVE_ITEM,"Ta bort"},
		{ADD_ITEM_LABEL,"L�gg till ny {0}"},
		{REMOVE_ITEM_LABEL,"Ta bort markerad {0}"},
		{DATE, "Datumet"},
		{TIMESTAMP, "Tidpunkten"},
		{NUMBER,"Talet"},
		{CURRENCY,"Beloppet"},
		{CoUIConstants.PERCENT, "Procentsatsen"},
		{CoConverter.BASIC_CONVERTER_PARSE_ERROR, "{0} {1} �r angivet i ett felaktigt format."},
		{CONVERTER_PARSE_ERROR,"{0} {1} �r angivet i ett felaktigt format. Var sn�ll och anv�nd formatet {2} i st�llet."},
		{CLEAR, "Radera"},
		{REMOVE_ELEMENT_MESSAGE, "Vill du ta bort?"},
		{RENAME, "D�p om"},
	
		// MNEMONICS
		{OK_MNEMONIC,"O"},
		{READY_MNEMONIC,"K"},
		{CANCEL_MNEMONIC,"A"},
		{YES_MNEMONIC,"J"},
		{NO_MNEMONIC,"N"},
		{CLEAR_MNEMONIC, "R"},

		//FileChooser
		{ACCEPT_ALL_FILES,			"Alla filer (*.*)"},
		{SAVE,						"Spara"},
		{SAVE_MNEMONIC,				"S"},
		{OPEN,						"�ppna"},
		{UPDATE,					"Uppdatera"},
		{HELP,						"Hj�lp"},
		{ABORT_FILE_CHOOSER_DIALOG,	"St�ng filv�ljaren."},
		{SAVE_SELECTED_FILE,		"Spara den markerade filen."},
		{OPEN_SELECTED_FILE,		"�ppna den markerade filen."},
		{UPDATE_DIRECTORY_LISTINGS,	"Updatera biliotekslistan."},
		{FILECHOOSER_HELP,			"�ppna Filv�ljarens hj�lp"},

		{LOOK_IN,					"Leta i"},
		{FILE_NAME,					"Filnamn"},
		{FILES_OF_TYPE,				"Filer av typen"},
		{UP_ONE_LEVEL,				"Upp en niv�"},
		{NEW_FOLDER,				"Skapa en ny mapp"},
		{LIST,						"Lista"},
		{DETAILED_LIST,				"Detaljerad lista"},
		{HOME,						"Hem"},

		// Misc UI
		{MOVE_UP, "Flytta upp�t"},
		{MOVE_DOWN, "Flytta ned�t"},
		{CoUIConstants.NONE_CHOSEN, "(Ingen vald)"},

		//Worksheet
		{WORKSHEET, "Arbetsyta"},
		{UNTITLED_WORKSHEET, "Namnl�s arbetsyta"},
		{OPEN_COMPONENT_FRAME, "�ppna komponentpalett..."},
		{CLOSE_COMPONENT_FRAME, "St�ng komponentpalett"},
		{OPEN_QUERY_FRAME, "�ppna s�kpalette..."},
		{CLOSE_QUERY_FRAME, "St�ng s�kpalett"},
		{LAYOUT_MENU,	"Layout"},
		{ADJUST_SIZE,	"Anpassa storlek"},
		{FRAME_STYLE,	"Visa som f�nster"},
		{PALETTE_STYLE,	"Visa som paneler"},
		{PACK_FRAMES,	"Packa ihop"},
		{ARRANGE_FRAMES,"Ordna"},

		// CoEnumeration
		{ENUMERATED_VALUES,			"M�jliga v�rden"},

		//CoTimePeriodUI
		{OK_BUTTON,							"OK"},
		{CANCEL_BUTTON,						"Avbryt"},
		{TIME_PERIOD, 						"Period"},
		{TIME_PERIOD_DELIMITER,				"\u2212"},
		{TIME_PERIOD_FROM_DATE, 			"Fr.o.m."},
		{TIME_PERIOD_TO_DATE, 				"t.o.m."},
		
		{CoGUI.QUESTION,		"Fr�ga"},
		{CoGUI.WARNING,			"Varning"},
		{CoGUI.MESSAGE,			"Meddelande"},
		{CoGUI.ERROR,			"Fel"},

		//Bookmarks
		{SAVE_IN_FOLDER,	"Spara i mapp"},
		{SAVE_AS_BOOKMARK,	"Spara som bokm�rke"},
		{OPEN_BOOKMARK,		"�ppna bokm�rke"},
		{SAVE_IN_EXISTING,	"Spara i befintlig"},
		{SAVE_IN_NEW,		"Spara i ny"},
		{BOOKMARK,			"Bokm�rke"},
		{BOOKMARK_NAME,		"Bokm�rkets namn"},
		{FOLDER_NAME,		"Mappens namn"},
		
		// CoFolderViewUI
		{FOLDER_LIST,		"Mappar"},

		{IDENTITY,				"ID"},
		{CREATION_DATE,			"Skapad"},
		{LAST_MODIFIED_DATE,	"Senast �ndrad"},
		{CREATED_BY,			"Skapad av"},
		{LAST_MODIFIED_BY,		"Senast �ndrad av"},
		
		// Menyer
		{PRINT,				"Skriv ut ..."},
		{PREVIEW_PRINT,		"F�rhandsgranska ..."},
		{PRINT_OPTIONS,		"Utskriftsinst�llningar ..."},

		// CoPrintTableOptionUI
		{LANDSCAPE,"Liggande"},
		{TABLE_STYLE,"Utformning"},
		{PREVIEW_IS_MISSING,"Exempel saknas"},
		{COLUMN_PRINT_PREVIEW,"Kolumnvis tabel utskrift, annars Sidvis"},
		{PAPER_PRINT_PREVIEW,"Sidvis tabel utskrift"},
	    {PAPER_MARGIN,        "Utan pappers marginal"},

 
	    {SAVE_AS_XML,  "Spara som XML"},
	    {LOAD_FROM_XML,  "L�s in fr�n XML"},
	    {UNKNOWN,  "Ok�nd"},


	};	
	
	
	
/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
}