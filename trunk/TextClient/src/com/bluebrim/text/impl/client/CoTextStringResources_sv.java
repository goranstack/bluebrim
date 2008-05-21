package com.bluebrim.text.impl.client;

import com.bluebrim.font.shared.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 	Resursklass som innehåller lokaliserade typnamn för 
 	de klasser vars instanser behöver använda typnamn när
 	de visar upp sig.
 */
 
public class CoTextStringResources_sv extends CoTextStringResources
{
	static final Object[][] contents =
	{
		{ CoStyledTextMenuImplementation.CHARACTER, "Tecken" },
		
		{ CoAbstractTextEditor.INSERT_LINEFEED, "Radslut" },
		{ CoAbstractTextEditor.INSERT_NON_BREAK_SPACE, "Ej brytande mellanslag" },
		{ CoAbstractTextEditor.INSERT_HYPHENATION_POINT, "Brytpunkt" },
		{ CoAbstractTextEditor.INSERT_ANTI_HYPHENATION_POINT, "Förbjuden brytpunkt" },
		{ CoAbstractTextEditor.INSERT_TEST_TEXT, "Exempeltext" },
		{ "" + CoUnicode.BULLET, "Kula" },
		{ "" + CoUnicode.EN_DASH, "Streck" },
		{ "" + CoUnicode.EM_DASH, "Långt streck" },
		{ "" + CoUnicode.PER_MILLE_SIGN, "Promille" },

		// Ångra-meddelanden
		{ "Undo", "Ångra" },
		{ "Redo", "Gör om" },
		{ "Undo addition", "Ångra infoga" },
		{ "Redo addition", "Gör om infoga" },
		{ "Undo style change", "Ångra stiländring" },
		{ "Redo style change", "Gör om stiländring" },
		{ "Undo deletion", "Ångra ta bort" },
		{ "Redo deletion", "Gör om ta bort" },

		
		
		{ CoCharacterPropertiesUI.KERN_ABOVE_SIZE, "Kerna automatiskt över" },
		{ CoCharacterPropertiesUI.SIMULATE_QXP_JUSTIFICATION_BUG, "QXP-utslutnig" },
		
		{ CoHyphenationUI.NAME, "Namn" },
		{ CoHyphenationUI.LINE_BREAKER, "Brytpunkter" },
		{ CoAnywhereLineBreakerIF.ANYWHERE_LINE_BREAKER, "var som helst" },
		{ CoLiangLineBreakerIF.LIANG_LINE_BREAKER, "enligt Liang" },
		{ CoWordLineBreakerIF.BETWEEN_WORDS_LINE_BREAKER, "mellan ord" },
		{ CoLiangLineBreakerUI.MINIMUM_PREFIX_LENGTH, "Minimum före" },
		{ CoLiangLineBreakerUI.MINIMUM_SUFFIX_LENGTH, "Minimum efter" },
		{ CoLiangLineBreakerUI.MINIMUM_WORD_LENGTH, "Kortaste ord" },
		
	 	{ "CHARACTER_STYLE_DIALOG", "Annan teckenutformning ..." },
  	{ "PARAGRAPH_STYLE_DIALOG", "Annan styckeutformning ..." },
   	{ "PARAGRAPH_TAGS_DIALOG", "Styckestaggar ..."},
   	{ "CHARACTER_TAGS_DIALOG", "Märken ..."},

 		{ CoTextConstants.CHARACTER_STYLE, "Annan teckenutformning" },
  	{ CoTextConstants.PARAGRAPH_STYLE, "Annan styckeutformning" },

  	{ "PARAGRAPH_STYLE_RULE", " Placering mm " },
 		{ "CHARACTER_STYLE_RULE", "Typsnitt & Stil" },
  	{ "PARAGRAPH_RULERS", "Linjer" },
		{ "STYLE", "Stil" },
		{ "PLAIN", "Normal" },
 		{ "CLEAR_CHARACTER_STYLES", "Ta bort teckenutformning" },
 		{ "CLEAR_PARAGRAPH_STYLES", "Ta bort styckesutformning" },
		{ CoTextConstants.PARAGRAPH_TAG, "Styckestagg" },
 		{ CoTextConstants.CHARACTER_TAG, "Märke" },
 		{ CoTextConstants.COMMENT, "Kommentar" },
  	{ "MEASUREMENTS_PREFS_DIALOG","Inställningar, texteditor..." },
   	{ CoAbstractParagraphStyleUI.TOP_RULER,"Ovanför" },
  	{ CoAbstractParagraphStyleUI.BOTTOM_RULER,"Under" },
		{ "REGULAR_TAB_STOP_INTERVAL", "Normalt tabavstånd" },
	
  	{ CoTextConstants.ALIGNMENT, "Justering" },
  	{ CoTextConstants.ALIGN_CENTER.toString(), "Centerad" },
  	{ CoTextConstants.ALIGN_FORCED.toString(), "Tvingad" },
  	{ CoTextConstants.ALIGN_JUSTIFIED.toString(), "Utsluten" },
  	{ CoTextConstants.ALIGN_LEFT.toString(), "Vänster" },
  	{ CoTextConstants.ALIGN_RIGHT.toString(), "Höger" },
		{ CoTextConstants.FIRST_LINE_INDENT, "Indrag första raden" },
		{ CoTextConstants.TRAILING_LINES_INDENT, "Hängande indrag" },
		{ CoTextConstants.LEFT_INDENT, "Indrag vänster" },
		{ CoTextConstants.RIGHT_INDENT, "Indrag höger" },
		{ CoTextConstants.SPACE_ABOVE, "Avstånd ovanför" },
		{ CoTextConstants.SPACE_BELOW, "Avstånd nedanför" },
		{ CoTextConstants.DROP_CAPS, "Anfanger" },
		{ CoTextConstants.DROP_CAPS_COUNT, "Antal tecken" },
		{ CoTextConstants.DROP_CAPS_HEIGHT, "Antal rader" },
		{ CoTextConstants.LINES_TOGETHER, "Håll ihop rader" },
		{ CoTextConstants.LEADING, "Radavstånd" },
		{ CoTextConstants.TOP_OF_COLUMN, "Först i kolumn" },
		{ CoTextConstants.LAST_IN_COLUMN, "Sist i kolumn" },
		{ CoTextConstants.HYPHENATION, "Radbrytning" },
		{ CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR, "Reservförfarande" },
		{ CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR_FIRST_BREAKPOINT.toString(), "Första BP" },
		{ CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR_NO_LINE.toString(), "Ingen BP" },
		{ CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR_NON_BREAKPOINT.toString(), "Otillåten BP" },
// ???		{ CoTextConstants.MINIMUM_SPACE_WIDTH, "Min. space width" },
		/*
 		{ CoTextConstants.ANYWHERE_LINE_BREAKER.toString(),"vart som helst"},
 		{ CoTextConstants.BETWEEN_WORDS_LINE_BREAKER.toString(),"mellan ord"},
 		{ CoTextConstants.LIANG_LINE_BREAKER.toString(),"Liang"},
 		*/
		{ CoTextConstants.REGULAR_TAB_STOP_INTERVAL, "Normalt tabavstånd" },
		{ CoTextConstants.ADJUST_TO_BASE_LINE_GRID, "Följ baslinjeraster" },
		{ CoTextConstants.FOREGROUND_COLOR, "Färg" },
		{ CoTextConstants.FOREGROUND_SHADE, "Tonvärde" },
		{ CoTextConstants.TRACK_AMOUNT, "Knipning" },
		{ CoTextConstants.FONT_SIZE, "Storlek" },
		{ CoTextConstants.STRIKE_THRU, "Genomstruken" },
//		{ CoTextConstants.OUTLINE, "Ihålig" },
		{ CoTextConstants.SHADOW, "Skugga" },
		{ CoTextConstants.ALL_CAPS, "Versaler" },
//		{ CoTextConstants.VARIANT, "Kapitäler" },
		{ CoTextConstants.VERTICAL_POSITION, "Vertikal position" },
		{ CoTextConstants.VERTICAL_POSITION_SUPERSCRIPT.toString(), "Upphöjd" },
		{ CoTextConstants.VERTICAL_POSITION_SUBSCRIPT.toString(), "Nedsänkt" },
		{ CoTextConstants.VERTICAL_POSITION_NONE.toString(), "Normal" },
		{ CoTextConstants.SUPERIOR, "Överkant" },
		{ CoTextConstants.WEIGHT, "Fet" },
		{ CoTextConstants.STYLE, "Kursiv" },
		{ CoTextConstants.UNDERLINE, "Understrykning" },
		{ CoTextConstants.UNDERLINE_NONE.toString(), "Ingen" },
		{ CoTextConstants.UNDERLINE_NORMAL.toString(), "Vanlig" },
		{ CoTextConstants.UNDERLINE_WORD.toString(), "Ord" },
		{ CoTextConstants.FONT_FAMILY, "Typsnitt" },
		{ CoTextConstants.BASELINE_OFFSET, "Baslinje" },
		{ CoTextConstants.HORIZONTAL_SCALE, "X%" },
		{ CoTextConstants.VERTICAL_SCALE, "Y%" },
		{ CoTextConstants.SHADOW_OFFSET, "Avstånd" },
		{ CoTextConstants.SHADOW_ANGLE, "Vinkel" },
		{ CoTextConstants.SHADOW_COLOR, "Färg" },

		{ CoTextConstants.TAG_CHAIN, "Styckesutformningssekvens" },

	
		
		
		
		{ CoHyphenationPatternUI.TITLE, "Avstavningsförslag" },
		
		// tool tips
		{"BOLD_TOOL_TIP", "Fet "},
		{"ITALIC_TOOL_TIP", "Kursiv "},
		{"STRIKE_THRU_TOOL_TIP", "Genomstruken "},
		{"OUTLINE_TOOL_TIP", "Ihålig "},
		{"ALL_CAPS_TOOL_TIP", "Versaler "},
		{"SMALL_CAPS_TOOL_TIP", "Kapitäler "},
		{"SHADOW_TOOL_TIP", "Skuggad "},
		{"SUPERIOR_TOOL_TIP", "Överkant "},
		{"PARAGRAPH_TAG_TOOL_TIP", "Styckesutformning "},
		{"CHARACTER_TAG_TOOL_TIP", "Märken "},
		{"FONT_FAMILY_TOOL_TIP", "Typsnitt "},
		{"FONT_SIZE_TOOL_TIP", "Storlek "},
		{"ALIGN_LEFT_TOOL_TIP", "Vänsterjustering "},
		{"ALIGN_CENTER_TOOL_TIP", "Centerjustering "},
		{"ALIGN_RIGHT_TOOL_TIP", "Högerjustering "},
		{"ALIGN_JUSTIFIED_TOOL_TIP", "Utsluten justering "},
		{"ALIGN_FORCED_TOOL_TIP", "Tvingad justering "},
		{"UNDERLINE_NORMAL_TOOL_TIP", "Vanlig understrykning "},
		{"UNDERLINE_WORD_TOOL_TIP", "Understrykning av ord "},
		{"UNDERLINE_NONE_TOOL_TIP", "Ingen understrykning "},

		{"MENU_STYLE","Stil"},
		{"OTHER","Annan..."},
		{"OTHER_FONT_SIZE_TITLE", "Storlek"},
		{"OTHER_FONT_SIZE_QUESTION", "Ny storlek : "},
		{"OTHER_SHADE_TITLE", "Tonvärde"},
		{"OTHER_SHADE_QUESTION", "Nytt tonvärde : "},

		{"OK","OK"},
		{"APPLY","Verkställ"},
		{"RESET","Återställ"},
		{"CLOSE","Stäng"},
		{"ALL_AS_IS","Alla oförändrade"},

		{"INSERT", "Infoga"},
				
		{"CUT", "Klipp ut"},
		{"COPY", "Kopiera"},
		{"COPY_STYLE", "Kopiera stil"},
		{"PASTE", "Klistra in"},

		// MNEMONIC
		{"MENU_STYLE_MNEMONIC","S"}, 
		{"OTHER_MNEMONIC","A"},
	
		{"FONT_MNEMONIC","T"},
 		{"SIZE_MNEMONIC","S"},
 		{"COLOR_MNEMONIC","F"},
		{"SHADE_MNEMONIC","T"},
 		{"TRACK_AMOUNT_MNEMONIC","K"},
		{"STYLE_MNEMONIC","S"},
		{"PLAIN_MNEMONIC","N"},
 		{"BOLD_MNEMONIC","F"},
 		{"ITALIC_MNEMONIC","K"},
 		{"WORD_UNDERLINE_MNEMONIC","U"},
		{"STRIKE_THRU_MNEMONIC","G"},
 		{"OUTLINE_MNEMONIC","K"},
 		{"SHADOW_MNEMONIC","S"},
		{"ALL_CAPS_MNEMONIC","V"},
 		{"SMALL_CAPS_MNEMONIC","K"},
 		{"SUPER_SCRIPT_MNEMONIC","U"},
		{"SUB_SCRIPT_MNEMONIC","N"},
 		{"SUPERIOR_MNEMONIC","Ö"},
		{"INSERT_MNEMONIC", "I"},
		{"CHARACTER_TAG_MNEMONIC", "E"},
 		{"CLEAR_CHARACTER_STYLES_MNEMONIC","T"},
		
 		{"UNDERLINE_MNEMONIC","U"},
		{"UNDERLINE_NONE_MNEMONIC","I"},
		{"UNDERLINE_NORMAL_MNEMONIC","V"},
		{"UNDERLINE_WORD_MNEMONIC","O"},
 		
 		{"CHARACTER_STYLE_SHEET_MNEMONIC","T"},
 		{"PARAGRAPH_STYLE_SHEET_MNEMONIC","S"},
  		{"CHARACTER_STYLE_DIALOG_MNEMONIC","T"},
  		{"PARAGRAPH_STYLE_DIALOG_MNEMONIC","S"},
		{"HORIZONTAL_VERTICAL_SCALE_MNEMONIC","H"},
 		{"KERN_MNEMONIC","K"},
 		{"BASELINE_SHIFT_MNEMONIC","F"},
 		{"CHARACTER_MNEMONIC","T"},
 		{"LEADING_MNEMONIC","R"},
 		{"FORMATS_MNEMONIC","F"},
 		{"TABS_MNEMONIC","T"},
 		{"RULES_MNEMONIC","L"},
 		
 		{"TEXT_TO_BOX_MNEMONIC","T"},
 		{"SHADE_FOR_TEXT_MNEMONIC","T"},
 		{"FLIP_HORIZONTAL_MNEMONIC","V"},
 		{"FLIP_VERTICAL_MNEMONIC","V"},

		//Stycke
 		{"PARAGRAPH_STYLE_RULE_MNEMONIC","S"},
 		{"LEFT_INDENT_MNEMONIC","I"},
 		{"FIRST_LINE_INDENT_MNEMONIC","I"},
 		{"RIGHT_INDENT_MNEMONIC", "I"},
 		{"AUTO_LEADING_MNEMONIC","A"},
 		{"SPACE_BEFORE_MNEMONIC", "A"},
 		{"SPACE_AFTER_MNEMONIC", "A"},
 		{"ALIGNMENT_MNEMONIC","J"},
 		{"ALIGN_LEFT_MNEMONIC","V"},
 		{"ALIGN_CENTER_MNEMONIC","C"},
 		{"ALIGN_RIGHT_MNEMONIC","H"},
 		{"ALIGN_JUSTIFIED_MNEMONIC","U"},
 		{"ALIGN_FORCED_MNEMONIC", "T"},
 		{"DROP_CAPS_MNEMONIC", "A"},
 		{"DROP_CAPS_COUNT_MNEMONIC", "A"},
 		{"DROP_CAPS_HEIGHT_MNEMONIC", "A"},
 		{"LAST_IN_COLUMN_MNEMONIC","S"},
 		{"UNBREAKABLE_MNEMONIC","F"},
 		{"TOP_OF_COLUMN_MNEMONIC","F"},
 		{"LINES_TOGETHER_MNEMONIC","H"},
		{"PARAGRAPH_TAG_MNEMONIC", "T"},

		
		{"SAMPLE_TEXT", "abcdefghijklmnopqrstuvwxyzåäö\n"+
						"ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ\n"+
						"123456789.:,;(:*!?')"},
		{"CHARACTER_SAMPLE_TEXT","Flygande bäckasiner söka hwila på mjuka tuvor. 1234567890"},
		{"PARAGRAPH_SAMPLE_TEXT","Flygande bäckasiner söka hwila på mjuka tuvor. 1234567890"},





		{"TAB_STOPS", "Tabbar"},

		{CoTabSetPanel.TAB_ALIGNMENT, "Justering"},
		{CoTabSetPanel.TAB_POSITION, "Position"},
		{CoTabSetPanel.TAB_LEADER, "Utfyllnadstecken"},

		{CoTabSetPanel.TAB_ALIGN_LEFT, "Vänster"},
		{CoTabSetPanel.TAB_ALIGN_CENTER, "Centrerat"},
		{CoTabSetPanel.TAB_ALIGN_RIGHT, "Höger"},
		{CoTabSetPanel.TAB_ALIGN_DECIMAL, "Decimal"},
		{CoTabSetPanel.TAB_ALIGN_ON, "Justera mot"},
		{CoTabSetPanel.TAB_LEAD_NONE, "Ingen"},
		{CoTabSetPanel.TAB_LEAD_DOTS, "Punkter"},
		{CoTabSetPanel.TAB_LEAD_HYPHENS, "Bindestreck"},
		{CoTabSetPanel.TAB_LEAD_UNDERLINE, "Understrykning"},
		{CoTabSetPanel.TAB_LEAD_THICKLINE, "Tjock linje"},
		{CoTabSetPanel.TAB_LEAD_EQUALS, "Lika med"},

		{CoTabSetPanel.TAB_ADD_STOP, "Lägg till"},
		{CoTabSetPanel.TAB_DELETE_STOP, "Ta bort"},


		{"CHECK_SPELLING", "Kontrollera stavning" },
		{"SPELL_CHECKING_OPTIONS", "Inställningar, stavningskontroll" },

		{ CoFontFamilyCollectionUI.INSTALLED, "Installerade" },
		{ CoFontFamilyCollectionUI.AVAILABLE, "Tillgängliga" },

		
		{ CoParagraphRulerPanel.POSITION, "Position" },
		{ CoParagraphRulerPanel.THICKNESS, "Tjocklek" },
		{ CoParagraphRulerPanel.SPAN, "Bredd" },

		{ CoTextConstants.RULER_SPAN_FIXED.toString(), "Fast" },
		{ CoTextConstants.RULER_SPAN_TEXT.toString(), "Text" },
		{ CoTextConstants.RULER_SPAN_COLUMN.toString(), "Kolumn" },

		{ CoParagraphRulerPanel.FIXED_SPAN_ALIGNMENT, "Justering" },
		{ CoParagraphRulerPanel.FIXED_SPAN_WIDTH, "Bredd" },

		{ CoParagraphRulerPanel.LEFT_INSET, "Indrag vänster" },
		{ CoParagraphRulerPanel.RIGHT_INSET, "Indrag höger" },

		{ CoTextMeasurementPrefsUI.MEASUREMENT, "Mätning" },
		{ CoTextMeasurementPrefsUI.AVAILABLE_TAGS, "Styckestaggar" },
		{ CoTextMeasurementPrefsUI.MEASURED_TAGS, "Mät på" },
		{ CoTextMeasurementPrefsUI.COLUMN_WIDTH, "Kolumnbredd" },

		{ CoTagChainUI.NAME , "Namn" },
		{ CoTagChainUI.CHAIN , "Sekvens" },

		{ CoTagGroupUI.NAME , "Namn" },
		{ CoTagGroupUI.MEMBERS , "Gruppmedlemmar" },

		{ "GET_TEXT", "Importera från fil ..." },

		{ CoTextStyleUI.NAME, "Namn" },
		{ CoTextStyleUI.SHORTCUT, "Kortkommando" },
		{ CoTextStyleUI.DELETED, "Ta bort" },
		{ CoTextStyleUI.INHERIT, "Ärv" },
		{ CoTextStyleUI.NAME_IS_TAKEN, "Namnet används redan" },
		{ CoTextStyleUI.BASED_ON, "Baserad på" },

		{ CoTypographyRuleUI.ADD, "Ny" },
		{ CoTypographyRuleUI.REMOVE, "Ta bort" },
		{ CoTypographyRuleUI.CHARACTER_STYLE, "Teckenutformning" },
		{ CoTypographyRuleUI.PARAGRAPH_STYLE, "Styckesutformning" },
		{ CoTypographyRuleUI.CHARACTER_STYLE_INITIAL_NAME, "Tecken" },
		{ CoTypographyRuleUI.PARAGRAPH_STYLE_INITIAL_NAME, "Stycke" },
		

	};	
/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
}