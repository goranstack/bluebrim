package com.bluebrim.text.impl.client;

import java.util.*;

import com.bluebrim.font.shared.*;
import com.bluebrim.resource.shared.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 	Resursklass som innehåller lokaliserade typnamn för 
 	de klasser vars instanser behöver använda typnamn när
 	de visar upp sig.
 */
 
public class CoTextStringResources extends CoOldResourceBundle
{
	public static CoOldResourceBundle rb = null;


	static final Object[][] contents =
	{
		{ CoStyledTextMenuImplementation.CHARACTER, "Character" },
		
		{ CoAbstractTextEditor.INSERT_LINEFEED, "Linefeed" },
		{ CoAbstractTextEditor.INSERT_NON_BREAK_SPACE, "Non breaking space" },
		{ CoAbstractTextEditor.INSERT_HYPHENATION_POINT, "Breakpoint" },
		{ CoAbstractTextEditor.INSERT_ANTI_HYPHENATION_POINT, "Non breakpoint" },
		{ "" + CoUnicode.BULLET, "Bullet" },
		{ "" + CoUnicode.EN_DASH, "En dash" },
		{ "" + CoUnicode.EM_DASH, "Em dash" },
		{ "" + CoUnicode.PER_MILLE_SIGN, "Per mille" },

		{ CoAbstractTextEditor.INSERT_TEST_TEXT, "Sample text" },
		
		// Ångra-meddelanden
		{"Undo", "Undo"},
		{"Redo", "Redo"},
		{"Undo addition", "Undo addition"},
		{"Redo addition", "Redo addition"},
		{"Undo style change", "Undo style change"},
		{"Redo style change", "Redo style change"},
		{"Undo deletion", "Undo deletion"},
		{"Redo deletion", "Redo deletion"},

		
		{ CoCharacterPropertiesUI.KERN_ABOVE_SIZE, "Auto Kern Above" },
		{ CoCharacterPropertiesUI.SIMULATE_QXP_JUSTIFICATION_BUG, "QXP justification" },
		
		{ CoHyphenationUI.NAME, "Name" },
		{ CoHyphenationUI.LINE_BREAKER, "Breakpoints" },
		{ CoAnywhereLineBreakerIF.ANYWHERE_LINE_BREAKER, "anywhere" },
		{ CoLiangLineBreakerIF.LIANG_LINE_BREAKER, "Liang" },
		{ CoWordLineBreakerIF.BETWEEN_WORDS_LINE_BREAKER, "between words" },
		{ CoLiangLineBreakerUI.MINIMUM_PREFIX_LENGTH, "Minimum before" },
		{ CoLiangLineBreakerUI.MINIMUM_SUFFIX_LENGTH, "Minimum after" },
		{ CoLiangLineBreakerUI.MINIMUM_WORD_LENGTH, "Shortest word" },
		
		{ CoHyphenationPatternUI.TITLE, "Suggested hyphenation" },
		
		{"FONT_SIZE_OPTIONS", new String[] { "7","9","10","12","14","18","24","36","48","60","72"} },
		{"FOREGROUND_SHADE_OPTIONS", new String[] { "0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100" } },

		// paths to images
		{"BOLD_IMAGE", "bold.gif"},
		{"BOLD_TRUE_IMAGE", "bold_green.gif"},
		{"BOLD_FALSE_IMAGE", "bold_red.gif"},
		
		{"ITALIC_IMAGE", "italic.gif"},
		{"ITALIC_TRUE_IMAGE", "italic_green.gif"},
		{"ITALIC_FALSE_IMAGE", "italic_red.gif"},
		
		{"STRIKE_THRU_IMAGE", "strikethru.gif"},
		{"STRIKE_THRU_TRUE_IMAGE", "strikethru_green.gif"},
		{"STRIKE_THRU_FALSE_IMAGE", "strikethru_red.gif"},
		
		{"OUTLINE_IMAGE", "outline.gif"},
		{"OUTLINE_TRUE_IMAGE", "outline_green.gif"},
		{"OUTLINE_FALSE_IMAGE", "outline_red.gif"},
		
		{"ALL_CAPS_IMAGE", "allcaps.gif"},
		{"ALL_CAPS_TRUE_IMAGE", "allcaps_green.gif"},
		{"ALL_CAPS_FALSE_IMAGE", "allcaps_red.gif"},
		
		{"SMALL_CAPS_IMAGE", "smallcaps.gif"},
		{"SMALL_CAPS_TRUE_IMAGE", "smallcaps_green.gif"},
		{"SMALL_CAPS_FALSE_IMAGE", "smallcaps_red.gif"},
		
		{"SHADOW_IMAGE", "shadow.gif"},
		{"SHADOW_TRUE_IMAGE", "shadow_green.gif"},
		{"SHADOW_FALSE_IMAGE", "shadow_red.gif"},
		
		{"SUPERIOR_IMAGE", "superior.gif"},
		{"SUPERIOR_TRUE_IMAGE", "superior_green.gif"},
		{"SUPERIOR_FALSE_IMAGE", "superior_red.gif"},
		
		{"LEFT_IMAGE", "left.gif"},
		{"CENTER_IMAGE", "center.gif"},
		{"RIGHT_IMAGE", "right.gif"},
		{"JUSTIFIED_IMAGE", "justified.gif"},
		{"FORCED_IMAGE", "forced.gif"},
		
		{"NO_UNDERLINE_IMAGE", "nounderline.gif"},
		{"NORMAL_UNDERLINE_IMAGE", "normalunderline.gif"},
		{"WORD_UNDERLINE_IMAGE", "wordunderline.gif"},

		// tool tips
		{"BOLD_TOOL_TIP", "Bold "},
		{"ITALIC_TOOL_TIP", "Italic "},
		{"STRIKE_THRU_TOOL_TIP", "Strike thru "},
		{"OUTLINE_TOOL_TIP", "Outline "},
		{"ALL_CAPS_TOOL_TIP", "All caps "},
		{"SMALL_CAPS_TOOL_TIP", "Small caps "},
		{"SHADOW_TOOL_TIP", "Shadow "},
		{"SUPERIOR_TOOL_TIP", "Superior "},
		{"PARAGRAPH_TAG_TOOL_TIP", "Paragraph tags "},
		{"CHARACTER_TAG_TOOL_TIP", "Character tags "},
		{"FONT_FAMILY_TOOL_TIP", "Font "},
		{"FONT_SIZE_TOOL_TIP", "Size "},
		{"ALIGN_LEFT_TOOL_TIP", "Left alignment "},
		{"ALIGN_CENTER_TOOL_TIP", "Center alignment "},
		{"ALIGN_RIGHT_TOOL_TIP", "Right alignment "},
		{"ALIGN_JUSTIFIED_TOOL_TIP", "Justified alignment "},
		{"ALIGN_FORCED_TOOL_TIP", "Forced alignment "},
		{"UNDERLINE_NORMAL_TOOL_TIP", "Normal underline "},
		{"UNDERLINE_WORD_TOOL_TIP", "Word underline "},
		{"UNDERLINE_NONE_TOOL_TIP", "No underline "},

		{"MENU_STYLE","Style"},
		{"OTHER","Other..."},
		{"OTHER_FONT_SIZE_TITLE", "Font size"},
		{"OTHER_FONT_SIZE_QUESTION", "New font size : "},
		{"OTHER_SHADE_TITLE", "Shade"},
		{"OTHER_SHADE_QUESTION", "New shade : "},

		{"OK","Ok"},
		{"APPLY","Apply"},
		{"RESET","Reset"},
		{"CLOSE","Close"},
		{"ALL_AS_IS","All as is"},






		{ CoTextConstants.ALIGNMENT, "Alignment" },
		{ CoTextConstants.FIRST_LINE_INDENT, "First line indent" },
		{ CoTextConstants.TRAILING_LINES_INDENT, "Trailing lines indent" },
		{ CoTextConstants.LEFT_INDENT, "Left indent" },
		{ CoTextConstants.RIGHT_INDENT, "Right indent" },
		{ CoTextConstants.SPACE_ABOVE, "Space above" },
		{ CoTextConstants.SPACE_BELOW, "Space below" },
		{ CoTextConstants.DROP_CAPS, "Drop caps" },
		{ CoTextConstants.DROP_CAPS_COUNT, "Number of characters" },
		{ CoTextConstants.DROP_CAPS_HEIGHT, "Number of lines" },
		{ CoTextConstants.LINES_TOGETHER, "Keep lines together" },
		{ CoTextConstants.LEADING, "Leading" },
		{ CoTextConstants.TOP_OF_COLUMN, "Top of column" },
		{ CoTextConstants.LAST_IN_COLUMN, "Last in column" },
		{ CoTextConstants.HYPHENATION, "Hyphenation" },
		{ CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR, "HFB" },
		{ CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR_FIRST_BREAKPOINT, "First BP" },
		{ CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR_NO_LINE, "No BP" },
		{ CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR_NON_BREAKPOINT, "Illegal BP" },
//		{ CoTextConstants.TAB_SET, "" },
		{ CoTextConstants.REGULAR_TAB_STOP_INTERVAL, "Default tab interval" },
		{ CoTextConstants.ADJUST_TO_BASE_LINE_GRID, "Adjust to base line grid" },
		{ CoTextConstants.OPTIMUM_SPACE_WIDTH, "Opt. space width" },
		{ CoTextConstants.MINIMUM_SPACE_WIDTH, "Min. space width" },
		/*
		{ CoTextConstants.TOP_RULER_THICKNESS, "" },
		{ CoTextConstants.TOP_RULER_POSITION, "" },
		{ CoTextConstants.TOP_RULER_SPAN, "" },
		{ CoTextConstants.TOP_RULER_ALIGNMENT, "" },
		{ CoTextConstants.TOP_RULER_FIXED_WIDTH, "" },
		{ CoTextConstants.TOP_RULER_LEFT_INSET, "" },
		{ CoTextConstants.TOP_RULER_RIGHT_INSET, "" },
		{ CoTextConstants.BOTTOM_RULER_THICKNESS, "" },
		{ CoTextConstants.BOTTOM_RULER_POSITION, "" },
		{ CoTextConstants.BOTTOM_RULER_SPAN, "" },
		{ CoTextConstants.BOTTOM_RULER_ALIGNMENT, "" },
		{ CoTextConstants.BOTTOM_RULER_FIXED_WIDTH, "" },
		{ CoTextConstants.BOTTOM_RULER_LEFT_INSET, "" },
		{ CoTextConstants.BOTTOM_RULER_RIGHT_INSET, "" },
		*/
//		{ CoTextConstants.FOREGROUND_COLOR, "" },
//		{ CoTextConstants.FOREGROUND_SHADE, "" },
		{ CoTextConstants.TRACK_AMOUNT, "Track amount" },
		{ CoTextConstants.FONT_SIZE, "Size" },
		{ CoTextConstants.STRIKE_THRU, "Strike thru" },
//		{ CoTextConstants.OUTLINE, "Outline" },
		{ CoTextConstants.SHADOW, "Shadow" },
		{ CoTextConstants.ALL_CAPS, "All caps" },
//		{ CoTextConstants.VARIANT, "Small caps" },
		{ CoTextConstants.VERTICAL_POSITION, "Vertical position" },
		{ CoTextConstants.SUPERIOR, "Superior" },
		{ CoTextConstants.WEIGHT, "Bold" },
		{ CoTextConstants.STYLE, "Italic" },
		{ CoTextConstants.UNDERLINE, "Underline" },
		{ CoTextConstants.FONT_FAMILY, "Font" },
		{ CoTextConstants.BASELINE_OFFSET, "Baseline offset" },
		{ CoTextConstants.HORIZONTAL_SCALE, "X%" },
		{ CoTextConstants.VERTICAL_SCALE, "Y%" },
		{ CoTextConstants.SHADOW_OFFSET, "Offset" },
		{ CoTextConstants.SHADOW_ANGLE, "Angle" },
		{ CoTextConstants.SHADOW_COLOR, "Color" },

		{ CoTextConstants.TAG_CHAIN, "Paragraph tag chain" },
		
		{"FONT","Font"},
 		{"SIZE","Size"},
 		{"COLOR","Color"},
		{"SHADE","Shade"},
		{"CLEAR_SHADE","Clear shade"},
 		{"TRACK_AMOUNT","Track amount"},
 		{"BASELINE_OFFSET","Baseline offset"},
		{"STYLE","Type style"},
		{"PLAIN","Plain"},
 		{"BOLD","Bold"},
 		{"ITALIC","Italic"},

 		{"UNDERLINE","Underline"},
		{"UNDERLINE_NONE","None"},
		{"UNDERLINE_NORMAL","Normal"},
		{"UNDERLINE_WORD","Word"},
		
		{"UNKNOWN"," "},
		{"UNKNOWN_MNEMONIC"," "},
		
		{ "VERTICAL_POSITION", "Vertical position" },
		{ "VERTICAL_POSITION_SUPERSCRIPT", "Superscript" },
		{ "VERTICAL_POSITION_SUBSCRIPT", "Subscript" },
		{ "VERTICAL_POSITION_NONE", "Baseline" },

		{"STRIKE_THRU","Strike thru"},
 		{"OUTLINE","Outline"},
 		{"SHADOW","Shadow"},
		{"ALL_CAPS","All caps"},
 		{"SMALL_CAPS","Small caps"},
 		{"SUPERIOR","Superior"},
  	{"CLEAR_CHARACTER_STYLES","Clear character styles"},
  	{"CLEAR_PARAGRAPH_STYLES","Clear paragraph styles"},

  	{"CHARACTER_STYLE_DIALOG","Character style ..."},
  	{"PARAGRAPH_STYLE_DIALOG","Paragraph style ..."},
  	{"PARAGRAPH_TAGS_DIALOG","Paragraph tags ..."},
  	{"MEASUREMENTS_PREFS_DIALOG","Text editor Preferences ..."},
		/*
 		{"CHARACTER_STYLE","Character style"},
 		{"CHARACTER_STYLE_SHEET","Character style..."},
  	{"PARAGRAPH_STYLE","Paragraph style"},
 		{"PARAGRAPH_STYLE_SHEET","Paragraph style..."},
		{"HORIZONTAL_VERTICAL_SCALE","Horizontal/Vertical scale..."},
 		{"KERN","Kern..."},
 		{"BASELINE_SHIFT","Shift baseline..."},
 		{"CHARACTER","Character..."},
 		{"LEADING","Leading..."},
 		{"FORMATS","Formats..."},
 		{"TABS","Tabs..."},
 		{"RULES","Rules..."},
  		
 		{"TEXT_TO_BOX","Text to block"},
 		{"SHADE_FOR_TEXT","Shade for text"},
 		{"FLIP_HORIZONTAL","Flip horizontal"},
 		{"FLIP_VERTICAL","Flip vertical"},
		*/
 		// macro
		{"INSERT", "Insert"},
		{"COMMENT", "Comment"},

		// ?
		{"CHARACTER_TAG", "Character tag"},
		
 		//Stycke
 		{"PARAGRAPH_STYLE_RULE","Paragraph style"},
 		{"CHARACTER_STYLE_RULE","Character style"},
		{"LEFT_INDENT","Left indent"},
 		{"FIRST_LINE_INDENT","First line indent"},
 		{"TRAILING_LINES_INDENT","Trailing lines indent"},
 		{"RIGHT_INDENT", "Right indent"},
 		{"AUTO_LEADING","Auto"},
 		{"LEADING", "Leading"},
 		{"SPACE_BEFORE", "Space before"},
 		{"SPACE_AFTER", "Space after"},
 		{"ALIGNMENT","Alignment"},
 //		{"ALIGN_UNKNOWN"," "},
 		{"ALIGN_NONE","None"},
 		{"ALIGN_LEFT","Left"},
 		{"ALIGN_CENTER","Center"},
 		{"ALIGN_RIGHT","Right"},
 		{"ALIGN_JUSTIFIED","Justified"},
 		{"ALIGN_FORCED", "Forced"},
 		{"LINE_BREAKER","Break lines"},
 		{"ANYWHERE_LINE_BREAKER","anywhere"},
 		{"BEFORE_CONSONANT_LINE_BREAKER","before consonants"},
 		{"BETWEEN_WORDS_LINE_BREAKER","between words"},
 		{"LIANG_LINE_BREAKER","Liang"},
// 		{"H&J", "H&J"},
 		{"DROP_CAPS", "Drop caps"},
 		{"DROP_CAPS_COUNT", "Number of characters"},
 		{"DROP_CAPS_HEIGHT", "Number of lines"},
 		{"TOP_OF_COLUMN","Top of column"},
 		{"LAST_IN_COLUMN","Last in column"},
 		{"LINES_TOGETHER","Keep lines together"},
 		{"UNBREAKABLE","Don't flow between columns"},
 		{"PARAGRAPH_TAG", "Paragraph tag"},

  	{"PARAGRAPH_RULERS","Rulers"},
  	{"TOP_RULER","Top ruler"},
  	{"BOTTOM_RULER","Bottom ruler"},


 		
		{"CUT", "Cut"},
		{"COPY", "Copy"},
		{"COPY_STYLE", "Copy style"},
		{"PASTE", "Paste"},

		// MNEMONIC
		{"MENU_STYLE_MNEMONIC","S"}, 
		{"OTHER_MNEMONIC","O"},
	
		{"FONT_MNEMONIC","F"},
 		{"SIZE_MNEMONIC","S"},
 		{"COLOR_MNEMONIC","C"},
		{"SHADE_MNEMONIC","S"},
 		{"TRACK_AMOUNT_MNEMONIC","T"},
		{"STYLE_MNEMONIC","S"},
		{"PLAIN_MNEMONIC","P"},
 		{"BOLD_MNEMONIC","B"},
 		{"ITALIC_MNEMONIC","I"},
 		{"WORD_UNDERLINE_MNEMONIC","U"},
		{"STRIKE_THRU_MNEMONIC","S"},
 		{"OUTLINE_MNEMONIC","O"},
 		{"SHADOW_MNEMONIC","S"},
		{"ALL_CAPS_MNEMONIC","A"},
 		{"SMALL_CAPS_MNEMONIC","S"},
 		{"SUPER_SCRIPT_MNEMONIC","S"},
		{"SUB_SCRIPT_MNEMONIC","S"},
 		{"SUPERIOR_MNEMONIC","S"},
		{"INSERT_MNEMONIC", "I"},
		{"CHARACTER_TAG_MNEMONIC", "C"},
 		{"CLEAR_CHARACTER_STYLES_MNEMONIC","C"},
		
 		{"UNDERLINE_MNEMONIC","U"},
		{"UNDERLINE_NONE_MNEMONIC","N"},
		{"UNDERLINE_NORMAL_MNEMONIC","N"},
		{"UNDERLINE_WORD_MNEMONIC","W"},
		
 		{"CHARACTER_STYLE_SHEET_MNEMONIC","C"},
 		{"PARAGRAPH_STYLE_SHEET_MNEMONIC","P"},
   	{"CHARACTER_STYLE_DIALOG_MNEMONIC","C"},
  	{"PARAGRAPH_STYLE_DIALOG_MNEMONIC","P"},
 		{"HORIZONTAL_VERTICAL_SCALE_MNEMONIC","H"},
 		{"KERN_MNEMONIC","K"},
 		{"BASELINE_SHIFT_MNEMONIC","S"},
 		{"CHARACTER_MNEMONIC","C"},
 		{"LEADING_MNEMONIC","L"},
 		{"FORMATS_MNEMONIC","F"},
 		{"TABS_MNEMONIC","T"},
 		{"RULES_MNEMONIC","R"},
 		
 		{"TEXT_TO_BOX_MNEMONIC","T"},
 		{"SHADE_FOR_TEXT_MNEMONIC","S"},
 		{"FLIP_HORIZONTAL_MNEMONIC","F"},
 		{"FLIP_VERTICAL_MNEMONIC","F"},
		
 		//Stycke
 		{"PARAGRAPH_STYLE_RULE_MNEMONIC","P"},
 		{"LEFT_INDENT_MNEMONIC","L"},
 		{"FIRST_LINE_INDENT_MNEMONIC","F"},
 		{"RIGHT_INDENT_MNEMONIC", "R"},
 		{"AUTO_LEADING_MNEMONIC","A"},
// 		{"LEADING_MNEMONIC", "L"},
 		{"SPACE_BEFORE_MNEMONIC", "S"},
 		{"SPACE_AFTER_MNEMONIC", "S"},
 		{"ALIGNMENT_MNEMONIC","A"},
 		{"ALIGN_LEFT_MNEMONIC","L"},
 		{"ALIGN_CENTER_MNEMONIC","C"},
 		{"ALIGN_RIGHT_MNEMONIC","R"},
 		{"ALIGN_JUSTIFIED_MNEMONIC","J"},
 		{"ALIGN_FORCED_MNEMONIC", "F"},
// 		{"H&J_MNEMONIC", "H"},
 		{"DROP_CAPS_MNEMONIC", "D"},
 		{"DROP_CAPS_COUNT_MNEMONIC", "N"},
 		{"DROP_CAPS_HEIGHT_MNEMONIC", "N"},
 		{"LAST_IN_COLUMN_MNEMONIC","L"},
 		{"UNBREAKABLE_MNEMONIC","D"},
 		{"TOP_OF_COLUMN_MNEMONIC","T"},
 		{"LINES_TOGETHER_MNEMONIC","K"},
		{"PARAGRAPH_TAG_MNEMONIC", "P"},

	
		{"SAMPLE_TEXT", "abcdefghijklmnopqrstuvwxyz\n"+
						"ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"+
						"123456789.:,;(:*!?')"},	
		{"CHARACTER_SAMPLE_TEXT","The quick brown fox jumped over the lazy dog. 1234567890"},
		{"PARAGRAPH_SAMPLE_TEXT","The quick brown fox jumped over the lazy dog. 1234567890"},

		
		

		{"TAB_STOPS", "Tab stops"},

		{CoTabSetPanel.TAB_ALIGNMENT, "Alignment"},
		{CoTabSetPanel.TAB_POSITION, "Position"},
		{CoTabSetPanel.TAB_LEADER, "Leader"},

		{CoTabSetPanel.TAB_ALIGN_LEFT, "Left"},
		{CoTabSetPanel.TAB_ALIGN_CENTER, "Center"},
		{CoTabSetPanel.TAB_ALIGN_RIGHT, "Right"},
		{CoTabSetPanel.TAB_ALIGN_DECIMAL, "Decimal"},
		{CoTabSetPanel.TAB_ALIGN_ON, "Align on"},
		{CoTabSetPanel.TAB_LEAD_NONE, "None"},
		{CoTabSetPanel.TAB_LEAD_DOTS, "Dots"},
		{CoTabSetPanel.TAB_LEAD_HYPHENS, "Hyphens"},
		{CoTabSetPanel.TAB_LEAD_UNDERLINE, "Underline"},
		{CoTabSetPanel.TAB_LEAD_THICKLINE, "Thickline"},
		{CoTabSetPanel.TAB_LEAD_EQUALS, "Equals"},

		{CoTabSetPanel.TAB_ADD_STOP, "Add"},
		{CoTabSetPanel.TAB_DELETE_STOP, "Remove"},




		{"ADJUST_TO_BASE_LINE_GRID", "Adjust to base line grid"},



		{"CHECK_SPELLING", "Check spelling" },
		{"SPELL_CHECKING_OPTIONS", "Spell checking options" },

		{ CoFontFamilyCollectionUI.INSTALLED, "Installed" },
		{ CoFontFamilyCollectionUI.AVAILABLE, "Available" },


		{ CoParagraphRulerPanel.POSITION, "Position" },
		{ CoParagraphRulerPanel.THICKNESS, "Thickness" },
		{ CoParagraphRulerPanel.SPAN, "Width" },

		{ CoTextConstants.RULER_SPAN_FIXED.toString(), "Fixed" },
		{ CoTextConstants.RULER_SPAN_TEXT.toString(), "Text" },
		{ CoTextConstants.RULER_SPAN_COLUMN.toString(), "Column" },

		{ CoParagraphRulerPanel.FIXED_SPAN_ALIGNMENT, "Alignment" },
		{ CoParagraphRulerPanel.FIXED_SPAN_WIDTH, "Width" },

		{ CoParagraphRulerPanel.LEFT_INSET, "Left inset" },
		{ CoParagraphRulerPanel.RIGHT_INSET, "Right inset" },

		{ CoTextMeasurementPrefsUI.MEASUREMENT, "Measurement" },
		{ CoTextMeasurementPrefsUI.AVAILABLE_TAGS, "Available tags" },
		{ CoTextMeasurementPrefsUI.MEASURED_TAGS, "Measured tags" },
		{ CoTextMeasurementPrefsUI.COLUMN_WIDTH, "Column width" },

		{ "REGULAR_TAB_STOP_INTERVAL", "Default tab interval" },

		{ CoTagChainUI.NAME , "Name" },
		{ CoTagChainUI.CHAIN , "Chain" },

		{ CoTagGroupUI.NAME , "Name" },
		{ CoTagGroupUI.MEMBERS , "Members" },

		{ "GET_TEXT", "Import from file ..." },

		{ CoTextStyleUI.NAME, "Name" },
		{ CoTextStyleUI.SHORTCUT, "Shortcut" },
		{ CoTextStyleUI.DELETED, "Deleted" },
		{ CoTextStyleUI.INHERIT, "Inherit" },
		{ CoTextStyleUI.NAME_IS_TAKEN, "That name is already used" },
		{ CoTextStyleUI.BASED_ON, "Based on" },

		{ CoTypographyRuleUI.ADD, "Add" },
		{ CoTypographyRuleUI.REMOVE, "Remove" },
		{ CoTypographyRuleUI.CHARACTER_STYLE, "Character style" },
		{ CoTypographyRuleUI.PARAGRAPH_STYLE, "Paragraph style" },
		{ CoTypographyRuleUI.CHARACTER_STYLE_INITIAL_NAME, "C" },
		{ CoTypographyRuleUI.PARAGRAPH_STYLE_INITIAL_NAME, "P" },
	
	};








	
/**
  Sätter om rb när Locale har ändrats. 
 */
public static CoOldResourceBundle getBundle ( ) {
	if (rb == null)
		rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoTextStringResources.class.getName());
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
		return aKey;
	}			
}
/**
  Svara med det namn som hör till nyckeln aKey.
  Saknas en resurs för aKey så svara med denna.
 */
public static String[] getNames( String aKey )
{
	try
	{
		return getBundle().getStringArray(aKey);
	}
	catch (MissingResourceException e)
	{
		return null;
	}
}
/**
  Svara med det namn som hör till nyckeln aKey.
  Saknas en resurs för aKey så svara med denna.
 */
public static Object getValue( String aKey )
{
	try
	{
		return getBundle().getObject(aKey);
	}
	catch (MissingResourceException e)
	{
		return null;
	}
}
/**
  Sätter om rb när Locale har ändrats. 
 */
public static void resetBundle ( ) {
	rb = (CoOldResourceBundle)ResourceBundle.getBundle(CoTextStringResources.class.getName());
	rb.resetLookup();
}
}