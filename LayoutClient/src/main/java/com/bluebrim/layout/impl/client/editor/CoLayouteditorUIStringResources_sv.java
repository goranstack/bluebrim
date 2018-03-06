package com.bluebrim.layout.impl.client.editor;

import java.awt.event.*;

import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;

/**
 * @author Dennis
 */
public class CoLayouteditorUIStringResources_sv extends CoLayouteditorUIStringResources
{
	static final Object[][] contents =
	{
		{ CoEditText.SCALE, "Skala" },
		
		{ CoLayoutEditorConfiguration.EDITORIAL_CONFIGURATION, "Redaktionell" },
		{ CoLayoutEditorConfiguration.AD_CONFIGURATION, "Annons" },

		{ CoLayoutEditor.POPUP_SHAPE_PAGE_ITEM_MODIFY, "Modifiera" },
		{ CoLayoutEditor.POPUP_SHAPE_PAGE_ITEM_SPAWN, "Öppna" },
		{ CoLayoutEditor.POPUP_SHAPE_PAGE_ITEM_SPAWN_SPAWN_IN_THIS_WINDOW, "i detta fönster" },
		{ CoLayoutEditor.POPUP_SHAPE_PAGE_ITEM_SPAWN_SPAWN_IN_NEW_WINDOW, "i nytt fönster" },
		{ CoLayoutEditor.POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_WITH_TO_FIT_HEIGHT_TO_IMAGE, "Anpassa bildbredden, sedan höjden till bilden" },
		{ CoLayoutEditor.POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_FIT_KEEP_ASPECT_RATIO, "Anpassa bilden utan att förvränga den" },
		{ CoLayoutEditor.POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_FIT, "Anpassa bilden" },
		{ CoLayoutEditor.POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_SCALED_IMAGE_SIZE, "Anpassa till bildens nuvarande storlek" },
		{ CoLayoutEditor.POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_IMAGE_SIZE, "Anpassa till bildens verkliga storlek" },
		{ CoLayoutEditor.POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_EMBEDDED_PATH, "Urklippsbana" },
		{ CoLayoutEditor.POPUP_IMAGE_CONTENT_GET_PICTURE, "Montera bild..." },
		{ CoLayoutEditor.POPUP_TEXT_CONTENT_ADJUST_HEIGHT_TO_TEXT, "Anpassa höjden till texten" },
		{ CoLayoutEditor.POPUP_TEXT_CONTENT_EDIT, "Editera ..." },
		{ CoLayoutEditor.POPUP_ABSTRACT_LAYOUT_CONTENT_EDIT, "Editera ..." },
		{ CoLayoutEditor.POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_WITH_TO_FIT_HEIGHT_TO_LAYOUT, "Anpassa bildbredden, sedan höjden till layouten" },
		{ CoLayoutEditor.POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_TO_FIT_KEEP_ASPECT_RATIO, "Anpassa layouten utan att förvränga den" },
		{ CoLayoutEditor.POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_TO_FIT, "Anpassa layouten" },
		{ CoLayoutEditor.POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_TO_SCALED_LAYOUT_SIZE, "Anpassa till layoutens nuvarande storlek" },
		{ CoLayoutEditor.POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_TO_LAYOUT_SIZE, "Anpassa till layoutens verkliga storlek" },
		
		// menu system decorations
		{ MENU_CONTENT, menuItem("Material", 'I') },		

		{ MENU_FILE, menuItem("Arkiv", 'A') },			
			{ MENU_FILE_SPAWN, menuItem("Öppna", ' ') },		
			{ MENU_FILE_SPAWN_IN_THIS_WINDOW, menuItem("i detta fönster", 'd') },		
			{ MENU_FILE_SPAWN_IN_NEW_WINDOW, menuItem("i eget fönster", 'e') },
			{ MENU_FILE_PREVIOUS_MODEL, menuItem("Föregående", 'T') },
			{ MENU_FILE_PRINT_SETTINGS, menuItem("Utskriftsformat...", 'o') },
			{ MENU_FILE_PRINT, menuItem("Skriv ut...", 'u') },
			{ MENU_FILE_EXPORT, menuItem("Exportera", 'x') },
			{ MENU_FILE_EXPORT_EPS, menuItem("som EPS...", 'E') },
			{ MENU_FILE_EXPORT_POSTSCRIPT, menuItem("som Postscript...", 'P') },
			{ MENU_FILE_PRINT_MENU, menuItem("Generera PS", 'r') },
			{ MENU_FILE_PRINT_PRINTER, menuItem("på skrivare", 'p') },
			{ MENU_FILE_PRINT_FILE, menuItem("till fil...", 'f') },
			{ MENU_FILE_PRINT_TYPESETTER, menuItem("till RIP", 'R') },
	// Standalone layouteditor
			{ MENU_FILE_NEW_DOCUMENT, menuItem("Ny(tt)", 'N', keyStroke(KeyEvent.VK_N, CTRL)) },
			{ MENU_FILE_OPEN_DOCUMENT, menuItem("Öppna...", 'Ö', keyStroke(KeyEvent.VK_O, CTRL)) },
			{ MENU_FILE_SAVE_DOCUMENT, menuItem("Spara", 'p', keyStroke(KeyEvent.VK_S, CTRL)) },
			{ MENU_FILE_SAVE_DOCUMENT_AS, menuItem("Spara som...", 'a', keyStroke(KeyEvent.VK_S, CTRL | ALT )) },
			{ MENU_FILE_EXIT, menuItem("Avsluta", 'v', keyStroke(KeyEvent.VK_Q, CTRL)) },

		{ MENU_EDIT, menuItem("Redigera", 'R') },
			{ MENU_EDIT_UNDO, menuItem("Ångra/Återställ", 'Å', keyStroke(KeyEvent.VK_BACK_SPACE, ALT)) },
			{ MENU_EDIT_CUT, menuItem("Klipp ut", 'T', keyStroke(KeyEvent.VK_X, CTRL)) },
			{ MENU_EDIT_COPY, menuItem("Kopiera", 'K', keyStroke(KeyEvent.VK_C, CTRL)) },
			{ MENU_EDIT_PASTE, menuItem("Klistra in", 'L', keyStroke(KeyEvent.VK_V, CTRL)) },
			{ MENU_EDIT_DELETE, menuItem("Ta bort", 'B', keyStroke(KeyEvent.VK_DELETE, 0)) },
			{ MENU_EDIT_SELECT_ALL, menuItem("Markera allt", 'M', keyStroke(KeyEvent.VK_A, CTRL)) },
			{ MENU_EDIT_FIND_REPLACE, menuItem("Sök/ersätt", 'ö', keyStroke(KeyEvent.VK_F, CTRL)) },
			{ MENU_EDIT_PREFERENCES, menuItem("Inställningar", 'G') },
				{ MENU_EDIT_PREFERENCES_APPLICATION, menuItem("Tillämpning...", 'T', keyStroke(KeyEvent.VK_Y, CTRL | ALT | SHIFT )) },
			{ MENU_EDIT_COLORS, menuItem("Färger...", 'F', keyStroke(KeyEvent.VK_F12, SHIFT )) },
			{ MENU_EDIT_HYPHENATIONS, menuItem("Avstavning...", 'A', keyStroke(KeyEvent.VK_F11, CTRL | SHIFT)) },
			{ MENU_EDIT_DASHES_STRIPES, menuItem("Streck och Linjer...", 'C') },
			
		{ MENU_STYLE, menuItem("Stil", 'S') },
		
		{ MENU_ITEM, menuItem("Objekt", 'O') },
			{ MENU_ITEM_MODIFY, menuItem("Modifiera...", 'M') },
			{ MENU_ITEM_TRAPPING, menuItem("Svällning", 'S') },
			{ MENU_ITEM_LAYERS, menuItem("Lager", 'L') },
			{ MENU_ITEM_DUPLICATE, menuItem("Duplicera", 'u', keyStroke(KeyEvent.VK_D, CTRL)) },
			{ MENU_ITEM_STEP_AND_REPEAT, menuItem("Duplicera och repetera...", 'C', keyStroke(KeyEvent.VK_D, CTRL | SHIFT )) },
			{ MENU_ITEM_DELETE, menuItem("Ta bort", 'T') },
			{ MENU_ITEM_SEND_TO_BACK, menuItem("Lägg bakom", 'L') },
			{ MENU_ITEM_SEND_BACKWARD, menuItem("Flytta bakåt", 'F') },
			{ MENU_ITEM_BRING_TO_FRONT, menuItem("Lägg framför", 'ä') },
			{ MENU_ITEM_BRING_FORWARD, menuItem("Flytta framåt", 'y') },
			{ MENU_ITEM_SPACE_ALIGN, menuItem("Sprid ut/Justera...", 'e') },
			{ MENU_ITEM_RESHAPE_POLYGONS, menuItem("Omforma polygon", 'O') },
			{ MENU_ITEM_STICKY_CURVE_POINTS, menuItem("Kontrollpunkter följer kurvpunkter", 'f') },
			{ MENU_ITEM_CURVE_CONTINUITY, menuItem("Kurvors kontinuitet", 'K') },
					{ MENU_ITEM_CURVE_CONTINUITY_0, menuItem("Kurvan", 'K') },
					{ MENU_ITEM_CURVE_CONTINUITY_1, menuItem("Förstaderivatan", 'F') },
					{ MENU_ITEM_CURVE_CONTINUITY_2, menuItem("Andraderivatan", 'A') },

		{ MENU_VIEW, menuItem("Visa", 'V') },
			{ MENU_VIEW_OPEN_TREE_VIEW, menuItem("Träd...", 'T') },
			{ MENU_VIEW_FIT_IN_WINDOW, menuItem("Anpassad storlek", 'A', keyStroke(KeyEvent.VK_0, CTRL)) },
			{ MENU_VIEW_FIT_SELECTION_IN_WINDOW, menuItem("Selektionsanpassad storlek", 'S', keyStroke(KeyEvent.VK_0, CTRL | SHIFT )) },
			{ MENU_VIEW_50_PROC, menuItem("50%", '5') },
			{ MENU_VIEW_75_PROC, menuItem("75%", '7') },
			{ MENU_VIEW_ACTUAL_SIZE, menuItem("Normal storlek", 'N', keyStroke(KeyEvent.VK_1, CTRL))},
			{ MENU_VIEW_200_PROC, menuItem("200%", '2') },
			{ MENU_VIEW_SHOW_GUIDES, menuItem("Visa stödlinjer", 'd', keyStroke(KeyEvent.VK_F11, SHIFT))},
			{ MENU_VIEW_SHOW_PAGE_OUTLINES, menuItem("Visa sidors konturer", 's')},
			{ MENU_VIEW_SHOW_OUTLINES, menuItem("Visa konturer", 'k')},
			{ MENU_VIEW_SHOW_BASELINE_GRID, menuItem("Visa baslinjeraster", 'b', keyStroke(KeyEvent.VK_F7, CTRL))},
			{ MENU_VIEW_SNAP_TO_GUIDES, menuItem("Fäst vid stödlinjer", 'F')},
			{ MENU_VIEW_SHOW_INVISIBLES, menuItem("Visa osynliga tecken", 'i', keyStroke(KeyEvent.VK_I, CTRL))},
			{ MENU_VIEW_SHOW_DUMMY_TEXT, menuItem("Visa textrader", 't'), keyStroke(KeyEvent.VK_T, CTRL)},
			{ MENU_VIEW_SHOW_CHILD_LOCK_INDICATOR, menuItem("Barnlås", 'l')},
			{ MENU_VIEW_SHOW_IMAGE_CLIP_INDICATOR, menuItem("Markera beskurna bilder", 'b')},
			{ MENU_VIEW_SHOW_TEXT_OVERFLOW_INDICATOR, menuItem("Markera överflödande text", 'ö')},
			{ MENU_VIEW_PAINT_IMAGES, menuItem("Visa bilder", 'b')},
			{ MENU_VIEW_SHOW_TOPICS, menuItem("Visa avdelningar", 'a')},
			{ MENU_VIEW_SHOW_PAGE_DATA, menuItem("Visa sidinformation", 's')},
			{ MENU_VIEW_SHOW_ENTIRE_DESKTOP, menuItem("Visa hela skrivbordet", 'h') },
			{ MENU_VIEW_LOOK, menuItem("Utseende", 'U') },
			{ MENU_VIEW_TEXT_ANTIALIASING, menuItem("Textutjämning", 'u') },
			{ MENU_VIEW_TEXT_FRACTIONALMETRICS, menuItem("Bråktalsplacerad text", 'B') },
			
				
		{ MENU_UTILITIES, menuItem("Övrigt", 'Ö') },
			{ MENU_UTILITIES_CHECK_SPELLING, menuItem("Kontrollera stavning", 'K') },
			{ MENU_UTILITIES_SPELL_CHECKING_OPTIONS, menuItem("Inställningar, stavningskontroll", 'I') },
			{ MENU_UTILITIES_SUGGESTED_HYPHENATION, menuItem("Avstavningsförslag...", 'v', keyStroke(KeyEvent.VK_H, CTRL)) },
			{ MENU_UTILITIES_INSPECT, menuItem("Inspektera markerat objekt", 'I') },

		{ MENU_HELP, menuItem("Hjälp", 'H') },
		
			{ MENU_HELP_KB_SHORTCUTS, menuItem("Tangentbordskommandon", 'T') },

		
// 	Fönstertitel
		{ LAYOUTEDITOR,			"Layouteditor"},

// 	Ledtexter
		{ SCALE,					"Skala"},
		
//	Filechooserns OK-knapp
		{ MOUNT,"Montera"},


// 	Modify dialog
		
		{ CoLayoutEditor.ZOOM, "Skala" },

		{ CoCheckSpelling.SPELL_CHECK_FAILED_TITLE, "" },
		{ CoCheckSpelling.SPELL_CHECK_FAILED_MESSAGE, "Stavningskontrollen kan ej genomföras eftersom texten är låst." },
		{ CoCheckSpelling.SPELL_CHECK_FAILED_RETRY, "Försök igen" },
		{ CoCheckSpelling.SPELL_CHECK_FAILED_CANCEL, "Hoppa över" },


		{ CoLayoutEditorPrefsUI.LENGTH_UNIT, "Längdenhet" },
		
		{ CoLayoutEditor.APPLICATION_PREFS, "Inställningar tillämpning" },

		{ CoDuplicateDialog.COUNT, "Antal" },
		{ CoDuplicateDialog.DX, "X-förskjutning" },
		{ CoDuplicateDialog.DY, "Y-förskjutning" },
		{ CoDuplicateDialog.OK, "Ok" },
		{ CoDuplicateDialog.CANCEL, "Avbryt" },

		{ CoLayoutEditor.CREATE_TEMPLATE, "Skapa mall" },

		{ CoLayoutEditor.TEMPLATES, "Mallar" },

		{ CoLayoutEditor.KEYBOARD_SHORTCUTS_HELP_URL, "kb_sv.html" },

		// CoRectangleLayoutManager properties
		{CoDistRectangleIF.RECTANGLE_DIST_CALC,"Rectangle"},
		{CoDistTriangleIF.TRIANGLE_DIST_CALC,"Triangle"},
		{CoDistVerticalIF.VERTICAL_DIST_CALC,"Vertical"},
		{CoDistHorizontalIF.HORIZONTAL_DIST_CALC,"Horizontal"},
		{CoDistConvexIF.CONVEX_DIST_CALC,"Convex"},
		{CoDistConcaveIF.CONCAVE_DIST_CALC,"Concave"},
		{CoRectangleLayoutManagerPanel.TYPE,"Placera som:"},
	};	

	public Object[][] getContents ( ) {
		return contents;
	}
}