package com.bluebrim.gui.client;


/**
	Subklass till CoStringResources med motsvarande svenska strängar.
 */
public class CoUIStringResources_sv extends CoUIStringResources {
	static final Object[][] contents =
	{

		{DEFAULT_ALPHA_NUMERICAL_MACROS, new String[] {"@","[a-z,å,ä,ö,A-Z,Å,Ä,Ö]"}},

		{MESSAGE,"Meddelande"},
		{BROADCAST_MESSAGE,"System meddelande"},
		{FILE_MENU,"Arkiv"},
		{FILE_MENU_MNEMONIC,"A"},
		{HELP_MENU,"Hjälp"},
		{SAVE_AS,"Spara som"},
		{SAVE_AS_ITEM,"Spara som..."},
		{SAVE_WORKSHEET,"Spara arbetsyta"},

		{EDIT,"Ändra"},
		{ARCHIVE, "Arkivera"},
		{CANCEL,"Avbryt"},
		{OK,"OK"},
		{CONTINUE,"Fortsätt"},
		{READY,"Klar"},
		{SAVE_CHANGES,"Spara ändringar"},
		{YES,"Ja"},
		{NO,"Nej"},
		{UNTITLED,"Namnlös"},
		{NOT_EDITABLE, "Inte redigerbar"},
		{PAGES,"sidor"},
		{NAME, "Namn"},
		{SHORT_NAME, "Kortnamn"},
		{ADD_ITEM,"Lägg till"},
		{ADD_ITEM_MNEMONIC, "L"},
		{REMOVE_ITEM,"Ta bort"},
		{ADD_ITEM_LABEL,"Lägg till ny {0}"},
		{REMOVE_ITEM_LABEL,"Ta bort markerad {0}"},
		{DATE, "Datumet"},
		{TIMESTAMP, "Tidpunkten"},
		{NUMBER,"Talet"},
		{CURRENCY,"Beloppet"},
		{CoUIConstants.PERCENT, "Procentsatsen"},
		{CoConverter.BASIC_CONVERTER_PARSE_ERROR, "{0} {1} är angivet i ett felaktigt format."},
		{CONVERTER_PARSE_ERROR,"{0} {1} är angivet i ett felaktigt format. Var snäll och använd formatet {2} i stället."},
		{CLEAR, "Radera"},
		{REMOVE_ELEMENT_MESSAGE, "Vill du ta bort?"},
		{RENAME, "Döp om"},
	
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
		{OPEN,						"Öppna"},
		{UPDATE,					"Uppdatera"},
		{HELP,						"Hjälp"},
		{ABORT_FILE_CHOOSER_DIALOG,	"Stäng filväljaren."},
		{SAVE_SELECTED_FILE,		"Spara den markerade filen."},
		{OPEN_SELECTED_FILE,		"Öppna den markerade filen."},
		{UPDATE_DIRECTORY_LISTINGS,	"Updatera biliotekslistan."},
		{FILECHOOSER_HELP,			"Öppna Filväljarens hjälp"},

		{LOOK_IN,					"Leta i"},
		{FILE_NAME,					"Filnamn"},
		{FILES_OF_TYPE,				"Filer av typen"},
		{UP_ONE_LEVEL,				"Upp en nivå"},
		{NEW_FOLDER,				"Skapa en ny mapp"},
		{LIST,						"Lista"},
		{DETAILED_LIST,				"Detaljerad lista"},
		{HOME,						"Hem"},

		// Misc UI
		{MOVE_UP, "Flytta uppåt"},
		{MOVE_DOWN, "Flytta nedåt"},
		{CoUIConstants.NONE_CHOSEN, "(Ingen vald)"},

		//Worksheet
		{WORKSHEET, "Arbetsyta"},
		{UNTITLED_WORKSHEET, "Namnlös arbetsyta"},
		{OPEN_COMPONENT_FRAME, "Öppna komponentpalett..."},
		{CLOSE_COMPONENT_FRAME, "Stäng komponentpalett"},
		{OPEN_QUERY_FRAME, "Öppna sökpalette..."},
		{CLOSE_QUERY_FRAME, "Stäng sökpalett"},
		{LAYOUT_MENU,	"Layout"},
		{ADJUST_SIZE,	"Anpassa storlek"},
		{FRAME_STYLE,	"Visa som fönster"},
		{PALETTE_STYLE,	"Visa som paneler"},
		{PACK_FRAMES,	"Packa ihop"},
		{ARRANGE_FRAMES,"Ordna"},

		// CoEnumeration
		{ENUMERATED_VALUES,			"Möjliga värden"},

		//CoTimePeriodUI
		{OK_BUTTON,							"OK"},
		{CANCEL_BUTTON,						"Avbryt"},
		{TIME_PERIOD, 						"Period"},
		{TIME_PERIOD_DELIMITER,				"\u2212"},
		{TIME_PERIOD_FROM_DATE, 			"Fr.o.m."},
		{TIME_PERIOD_TO_DATE, 				"t.o.m."},
		
		{CoGUI.QUESTION,		"Fråga"},
		{CoGUI.WARNING,			"Varning"},
		{CoGUI.MESSAGE,			"Meddelande"},
		{CoGUI.ERROR,			"Fel"},

		//Bookmarks
		{SAVE_IN_FOLDER,	"Spara i mapp"},
		{SAVE_AS_BOOKMARK,	"Spara som bokmärke"},
		{OPEN_BOOKMARK,		"öppna bokmärke"},
		{SAVE_IN_EXISTING,	"Spara i befintlig"},
		{SAVE_IN_NEW,		"Spara i ny"},
		{BOOKMARK,			"Bokmärke"},
		{BOOKMARK_NAME,		"Bokmärkets namn"},
		{FOLDER_NAME,		"Mappens namn"},
		
		// CoFolderViewUI
		{FOLDER_LIST,		"Mappar"},

		{IDENTITY,				"ID"},
		{CREATION_DATE,			"Skapad"},
		{LAST_MODIFIED_DATE,	"Senast ändrad"},
		{CREATED_BY,			"Skapad av"},
		{LAST_MODIFIED_BY,		"Senast ändrad av"},
		
		// Menyer
		{PRINT,				"Skriv ut ..."},
		{PREVIEW_PRINT,		"Förhandsgranska ..."},
		{PRINT_OPTIONS,		"Utskriftsinställningar ..."},

		// CoPrintTableOptionUI
		{LANDSCAPE,"Liggande"},
		{TABLE_STYLE,"Utformning"},
		{PREVIEW_IS_MISSING,"Exempel saknas"},
		{COLUMN_PRINT_PREVIEW,"Kolumnvis tabel utskrift, annars Sidvis"},
		{PAPER_PRINT_PREVIEW,"Sidvis tabel utskrift"},
	    {PAPER_MARGIN,        "Utan pappers marginal"},

 
	    {SAVE_AS_XML,  "Spara som XML"},
	    {LOAD_FROM_XML,  "Läs in från XML"},
	    {UNKNOWN,  "Okänd"},


	};	
	
	
	
/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
}