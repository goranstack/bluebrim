package com.bluebrim.text.impl.client;

import com.bluebrim.font.shared.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 	Resursklass som inneh�ller lokaliserade typnamn f�r 
 	de klasser vars instanser beh�ver anv�nda typnamn n�r
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
		{ CoAbstractTextEditor.INSERT_ANTI_HYPHENATION_POINT, "F�rbjuden brytpunkt" },
		{ CoAbstractTextEditor.INSERT_TEST_TEXT, "Exempeltext" },
		{ "" + CoUnicode.BULLET, "Kula" },
		{ "" + CoUnicode.EN_DASH, "Streck" },
		{ "" + CoUnicode.EM_DASH, "L�ngt streck" },
		{ "" + CoUnicode.PER_MILLE_SIGN, "Promille" },

		// �ngra-meddelanden
		{ "Undo", "�ngra" },
		{ "Redo", "G�r om" },
		{ "Undo addition", "�ngra infoga" },
		{ "Redo addition", "G�r om infoga" },
		{ "Undo style change", "�ngra stil�ndring" },
		{ "Redo style change", "G�r om stil�ndring" },
		{ "Undo deletion", "�ngra ta bort" },
		{ "Redo deletion", "G�r om ta bort" },

		
		
		{ CoCharacterPropertiesUI.KERN_ABOVE_SIZE, "Kerna automatiskt �ver" },
		{ CoCharacterPropertiesUI.SIMULATE_QXP_JUSTIFICATION_BUG, "QXP-utslutnig" },
		
		{ CoHyphenationUI.NAME, "Namn" },
		{ CoHyphenationUI.LINE_BREAKER, "Brytpunkter" },
		{ CoAnywhereLineBreakerIF.ANYWHERE_LINE_BREAKER, "var som helst" },
		{ CoLiangLineBreakerIF.LIANG_LINE_BREAKER, "enligt Liang" },
		{ CoWordLineBreakerIF.BETWEEN_WORDS_LINE_BREAKER, "mellan ord" },
		{ CoLiangLineBreakerUI.MINIMUM_PREFIX_LENGTH, "Minimum f�re" },
		{ CoLiangLineBreakerUI.MINIMUM_SUFFIX_LENGTH, "Minimum efter" },
		{ CoLiangLineBreakerUI.MINIMUM_WORD_LENGTH, "Kortaste ord" },
		
	 	{ "CHARACTER_STYLE_DIALOG", "Annan teckenutformning ..." },
  	{ "PARAGRAPH_STYLE_DIALOG", "Annan styckeutformning ..." },
   	{ "PARAGRAPH_TAGS_DIALOG", "Styckestaggar ..."},
   	{ "CHARACTER_TAGS_DIALOG", "M�rken ..."},

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
 		{ CoTextConstants.CHARACTER_TAG, "M�rke" },
 		{ CoTextConstants.COMMENT, "Kommentar" },
  	{ "MEASUREMENTS_PREFS_DIALOG","Inst�llningar, texteditor..." },
   	{ CoAbstractParagraphStyleUI.TOP_RULER,"Ovanf�r" },
  	{ CoAbstractParagraphStyleUI.BOTTOM_RULER,"Under" },
		{ "REGULAR_TAB_STOP_INTERVAL", "Normalt tabavst�nd" },
	
  	{ CoTextConstants.ALIGNMENT, "Justering" },
  	{ CoTextConstants.ALIGN_CENTER.toString(), "Centerad" },
  	{ CoTextConstants.ALIGN_FORCED.toString(), "Tvingad" },
  	{ CoTextConstants.ALIGN_JUSTIFIED.toString(), "Utsluten" },
  	{ CoTextConstants.ALIGN_LEFT.toString(), "V�nster" },
  	{ CoTextConstants.ALIGN_RIGHT.toString(), "H�ger" },
		{ CoTextConstants.FIRST_LINE_INDENT, "Indrag f�rsta raden" },
		{ CoTextConstants.TRAILING_LINES_INDENT, "H�ngande indrag" },
		{ CoTextConstants.LEFT_INDENT, "Indrag v�nster" },
		{ CoTextConstants.RIGHT_INDENT, "Indrag h�ger" },
		{ CoTextConstants.SPACE_ABOVE, "Avst�nd ovanf�r" },
		{ CoTextConstants.SPACE_BELOW, "Avst�nd nedanf�r" },
		{ CoTextConstants.DROP_CAPS, "Anfanger" },
		{ CoTextConstants.DROP_CAPS_COUNT, "Antal tecken" },
		{ CoTextConstants.DROP_CAPS_HEIGHT, "Antal rader" },
		{ CoTextConstants.LINES_TOGETHER, "H�ll ihop rader" },
		{ CoTextConstants.LEADING, "Radavst�nd" },
		{ CoTextConstants.TOP_OF_COLUMN, "F�rst i kolumn" },
		{ CoTextConstants.LAST_IN_COLUMN, "Sist i kolumn" },
		{ CoTextConstants.HYPHENATION, "Radbrytning" },
		{ CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR, "Reservf�rfarande" },
		{ CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR_FIRST_BREAKPOINT.toString(), "F�rsta BP" },
		{ CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR_NO_LINE.toString(), "Ingen BP" },
		{ CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR_NON_BREAKPOINT.toString(), "Otill�ten BP" },
// ???		{ CoTextConstants.MINIMUM_SPACE_WIDTH, "Min. space width" },
		/*
 		{ CoTextConstants.ANYWHERE_LINE_BREAKER.toString(),"vart som helst"},
 		{ CoTextConstants.BETWEEN_WORDS_LINE_BREAKER.toString(),"mellan ord"},
 		{ CoTextConstants.LIANG_LINE_BREAKER.toString(),"Liang"},
 		*/
		{ CoTextConstants.REGULAR_TAB_STOP_INTERVAL, "Normalt tabavst�nd" },
		{ CoTextConstants.ADJUST_TO_BASE_LINE_GRID, "F�lj baslinjeraster" },
		{ CoTextConstants.FOREGROUND_COLOR, "F�rg" },
		{ CoTextConstants.FOREGROUND_SHADE, "Tonv�rde" },
		{ CoTextConstants.TRACK_AMOUNT, "Knipning" },
		{ CoTextConstants.FONT_SIZE, "Storlek" },
		{ CoTextConstants.STRIKE_THRU, "Genomstruken" },
//		{ CoTextConstants.OUTLINE, "Ih�lig" },
		{ CoTextConstants.SHADOW, "Skugga" },
		{ CoTextConstants.ALL_CAPS, "Versaler" },
//		{ CoTextConstants.VARIANT, "Kapit�ler" },
		{ CoTextConstants.VERTICAL_POSITION, "Vertikal position" },
		{ CoTextConstants.VERTICAL_POSITION_SUPERSCRIPT.toString(), "Upph�jd" },
		{ CoTextConstants.VERTICAL_POSITION_SUBSCRIPT.toString(), "Neds�nkt" },
		{ CoTextConstants.VERTICAL_POSITION_NONE.toString(), "Normal" },
		{ CoTextConstants.SUPERIOR, "�verkant" },
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
		{ CoTextConstants.SHADOW_OFFSET, "Avst�nd" },
		{ CoTextConstants.SHADOW_ANGLE, "Vinkel" },
		{ CoTextConstants.SHADOW_COLOR, "F�rg" },

		{ CoTextConstants.TAG_CHAIN, "Styckesutformningssekvens" },

	
		
		
		
		{ CoHyphenationPatternUI.TITLE, "Avstavningsf�rslag" },
		
		// tool tips
		{"BOLD_TOOL_TIP", "Fet "},
		{"ITALIC_TOOL_TIP", "Kursiv "},
		{"STRIKE_THRU_TOOL_TIP", "Genomstruken "},
		{"OUTLINE_TOOL_TIP", "Ih�lig "},
		{"ALL_CAPS_TOOL_TIP", "Versaler "},
		{"SMALL_CAPS_TOOL_TIP", "Kapit�ler "},
		{"SHADOW_TOOL_TIP", "Skuggad "},
		{"SUPERIOR_TOOL_TIP", "�verkant "},
		{"PARAGRAPH_TAG_TOOL_TIP", "Styckesutformning "},
		{"CHARACTER_TAG_TOOL_TIP", "M�rken "},
		{"FONT_FAMILY_TOOL_TIP", "Typsnitt "},
		{"FONT_SIZE_TOOL_TIP", "Storlek "},
		{"ALIGN_LEFT_TOOL_TIP", "V�nsterjustering "},
		{"ALIGN_CENTER_TOOL_TIP", "Centerjustering "},
		{"ALIGN_RIGHT_TOOL_TIP", "H�gerjustering "},
		{"ALIGN_JUSTIFIED_TOOL_TIP", "Utsluten justering "},
		{"ALIGN_FORCED_TOOL_TIP", "Tvingad justering "},
		{"UNDERLINE_NORMAL_TOOL_TIP", "Vanlig understrykning "},
		{"UNDERLINE_WORD_TOOL_TIP", "Understrykning av ord "},
		{"UNDERLINE_NONE_TOOL_TIP", "Ingen understrykning "},

		{"MENU_STYLE","Stil"},
		{"OTHER","Annan..."},
		{"OTHER_FONT_SIZE_TITLE", "Storlek"},
		{"OTHER_FONT_SIZE_QUESTION", "Ny storlek : "},
		{"OTHER_SHADE_TITLE", "Tonv�rde"},
		{"OTHER_SHADE_QUESTION", "Nytt tonv�rde : "},

		{"OK","OK"},
		{"APPLY","Verkst�ll"},
		{"RESET","�terst�ll"},
		{"CLOSE","St�ng"},
		{"ALL_AS_IS","Alla of�r�ndrade"},

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
 		{"SUPERIOR_MNEMONIC","�"},
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

		
		{"SAMPLE_TEXT", "abcdefghijklmnopqrstuvwxyz���\n"+
						"ABCDEFGHIJKLMNOPQRSTUVWXYZ���\n"+
						"123456789.:,;(:*!?')"},
		{"CHARACTER_SAMPLE_TEXT","Flygande b�ckasiner s�ka hwila p� mjuka tuvor. 1234567890"},
		{"PARAGRAPH_SAMPLE_TEXT","Flygande b�ckasiner s�ka hwila p� mjuka tuvor. 1234567890"},





		{"TAB_STOPS", "Tabbar"},

		{CoTabSetPanel.TAB_ALIGNMENT, "Justering"},
		{CoTabSetPanel.TAB_POSITION, "Position"},
		{CoTabSetPanel.TAB_LEADER, "Utfyllnadstecken"},

		{CoTabSetPanel.TAB_ALIGN_LEFT, "V�nster"},
		{CoTabSetPanel.TAB_ALIGN_CENTER, "Centrerat"},
		{CoTabSetPanel.TAB_ALIGN_RIGHT, "H�ger"},
		{CoTabSetPanel.TAB_ALIGN_DECIMAL, "Decimal"},
		{CoTabSetPanel.TAB_ALIGN_ON, "Justera mot"},
		{CoTabSetPanel.TAB_LEAD_NONE, "Ingen"},
		{CoTabSetPanel.TAB_LEAD_DOTS, "Punkter"},
		{CoTabSetPanel.TAB_LEAD_HYPHENS, "Bindestreck"},
		{CoTabSetPanel.TAB_LEAD_UNDERLINE, "Understrykning"},
		{CoTabSetPanel.TAB_LEAD_THICKLINE, "Tjock linje"},
		{CoTabSetPanel.TAB_LEAD_EQUALS, "Lika med"},

		{CoTabSetPanel.TAB_ADD_STOP, "L�gg till"},
		{CoTabSetPanel.TAB_DELETE_STOP, "Ta bort"},


		{"CHECK_SPELLING", "Kontrollera stavning" },
		{"SPELL_CHECKING_OPTIONS", "Inst�llningar, stavningskontroll" },

		{ CoFontFamilyCollectionUI.INSTALLED, "Installerade" },
		{ CoFontFamilyCollectionUI.AVAILABLE, "Tillg�ngliga" },

		
		{ CoParagraphRulerPanel.POSITION, "Position" },
		{ CoParagraphRulerPanel.THICKNESS, "Tjocklek" },
		{ CoParagraphRulerPanel.SPAN, "Bredd" },

		{ CoTextConstants.RULER_SPAN_FIXED.toString(), "Fast" },
		{ CoTextConstants.RULER_SPAN_TEXT.toString(), "Text" },
		{ CoTextConstants.RULER_SPAN_COLUMN.toString(), "Kolumn" },

		{ CoParagraphRulerPanel.FIXED_SPAN_ALIGNMENT, "Justering" },
		{ CoParagraphRulerPanel.FIXED_SPAN_WIDTH, "Bredd" },

		{ CoParagraphRulerPanel.LEFT_INSET, "Indrag v�nster" },
		{ CoParagraphRulerPanel.RIGHT_INSET, "Indrag h�ger" },

		{ CoTextMeasurementPrefsUI.MEASUREMENT, "M�tning" },
		{ CoTextMeasurementPrefsUI.AVAILABLE_TAGS, "Styckestaggar" },
		{ CoTextMeasurementPrefsUI.MEASURED_TAGS, "M�t p�" },
		{ CoTextMeasurementPrefsUI.COLUMN_WIDTH, "Kolumnbredd" },

		{ CoTagChainUI.NAME , "Namn" },
		{ CoTagChainUI.CHAIN , "Sekvens" },

		{ CoTagGroupUI.NAME , "Namn" },
		{ CoTagGroupUI.MEMBERS , "Gruppmedlemmar" },

		{ "GET_TEXT", "Importera fr�n fil ..." },

		{ CoTextStyleUI.NAME, "Namn" },
		{ CoTextStyleUI.SHORTCUT, "Kortkommando" },
		{ CoTextStyleUI.DELETED, "Ta bort" },
		{ CoTextStyleUI.INHERIT, "�rv" },
		{ CoTextStyleUI.NAME_IS_TAKEN, "Namnet anv�nds redan" },
		{ CoTextStyleUI.BASED_ON, "Baserad p�" },

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