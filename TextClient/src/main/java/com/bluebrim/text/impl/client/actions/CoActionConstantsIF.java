package com.bluebrim.text.impl.client.actions;
import javax.swing.*;

import com.bluebrim.text.impl.client.*;
import com.bluebrim.text.shared.*;

/**
 * All text editor actions are defined here.
 * 
 * @author: Dennis Malmström
 */
 
public interface CoActionConstantsIF extends CoTextConstants
{
	// character font attribute action names
	public static final String plainCharacterAction = "Co-character-plain";
	public static final String boldCharacterAction = "Co-character-bold";
	public static final String italicCharacterAction = "Co-character-italic";
	public static final String strikeThruCharacterAction = "Co-character-strikethru";

	public static final String shadowCharacterAction = "Co-character-shadow";
	public static final String allCapsCharacterAction = "Co-character-all-caps";
	public static final String superiorCharacterAction = "Co-character-superior";

	public static final String underlineCharacterAction = "Co-character-underline";
	public static final String verticalPositionCharacterAction = "Co-character-vertical-position";
	public static final String fontFamilyCharacterAction = "Co-character-family";
	public static final String fontSizeCharacterAction = "Co-character-size";
	public static final String foregroundColorCharacterAction = "Co-character-foreground-color";
	public static final String foregroundShadeCharacterAction = "Co-character-foreground-shade";
	public static final String trackAmountCharacterAction = "Co-character-track-amount";
	public static final String baselineOffsetCharacterAction = "Co-character-baseline-offset";
	public static final String horizontalScaleCharacterAction = "Co-character-horizontal-scale";
	public static final String verticalScaleCharacterAction = "Co-character-vertical-scale";
	public static final String shadowAngleCharacterAction = "Co-character-shadow-angle";
	public static final String shadowColorCharacterAction = "Co-character-shadow-color";
	public static final String shadowOffsetCharacterAction = "Co-character-shadow-offset";
	public static final String shadowShadeCharacterAction = "Co-character-shadow-shade";

	
	// paragraph font attribute action names
	public static final String plainParagraphAction = "Co-paragraph-plain";
	public static final String boldParagraphAction = "Co-paragraph-bold";
	public static final String italicParagraphAction = "Co-paragraph-italic";
	public static final String strikeThruParagraphAction = "Co-paragraph-strikethru";

	public static final String shadowParagraphAction = "Co-paragraph-shadow";
	public static final String allCapsParagraphAction = "Co-paragraph-all-caps";
	public static final String superiorParagraphAction = "Co-paragraph-superior";

	public static final String underlineParagraphAction = "Co-paragraph-underline";
	public static final String verticalPositionParagraphAction = "Co-paragraph-vertical-position";
	public static final String fontFamilyParagraphAction = "Co-paragraph-family";
	public static final String fontSizeParagraphAction = "Co-paragraph-size";
	public static final String foregroundColorParagraphAction = "Co-paragraph-foreground-color";
	public static final String foregroundShadeParagraphAction = "Co-paragraph-foreground-shade";
	public static final String trackAmountParagraphAction = "Co-paragraph-track-amount";
	public static final String baselineOffsetParagraphAction = "Co-paragraph-baseline-offset";
	public static final String horizontalScaleParagraphAction = "Co-paragraph-horizontal-scale";
	public static final String verticalScaleParagraphAction = "Co-paragraph-vertical-scale";
	public static final String shadowAngleParagraphAction = "Co-paragraph-shadow-angle";
	public static final String shadowColorParagraphAction = "Co-paragraph-shadow-color";
	public static final String shadowOffsetParagraphAction = "Co-paragraph-shadow-offset";
	public static final String shadowShadeParagraphAction = "Co-paragraph-shadow-shade";
	
	// character/paragraph font attribute action names
	public static final String fontFamilyCharacterOrParagraphAction = "Co-character-or-paragraph-family";
	public static final String fontSizeCharacterOrParagraphAction = "Co-character-or-paragraph-size";
	
	// paragraph attribute action names
	public static final String alignmentParagraphAction = "Co-paragraph-alignment";
	public static final String hyphenationParagraphAction = "Co-paragraph-hyphenation";
	public static final String hyphenationFallbackBehaviorParagraphAction = "Co-paragraph-hyphenation-fallback-behavior";

	public static final String adjustToBaseLineGridParagraphAction = "Co-paragraph-adjust-to-base-line-grid";
	public static final String lastInColumnParagraphAction = "Co-paragraph-last-in-column";
	public static final String topOfColumnParagraphAction = "Co-paragraph-top-of-column";
	public static final String linesTogetherParagraphAction = "Co-paragraph-lines-together";
	public static final String dropCapsParagraphAction = "Co-paragraph-dropcaps";
	public static final String leftIndentParagraphAction = "Co-paragraph-left-indent";
	public static final String rightIndentParagraphAction = "Co-paragraph-right-indent";
	public static final String firstLineIndentParagraphAction = "Co-paragraph-first-line-indent";
	public static final String trailingLinesIndentParagraphAction = "Co-paragraph-trailing-lines-indent";
	public static final String spaceAboveParagraphAction = "Co-paragraph-space-above";
	public static final String spaceBelowParagraphAction = "Co-paragraph-space-below";
	public static final String leadingParagraphAction = "Co-paragraph-leading";
	public static final String dropCapsWidthParagraphAction = "Co-paragraph-dropcaps-width";
	public static final String dropCapsHeightParagraphAction = "Co-paragraph-dropcaps-height";
	public static final String topRulerPositionParagraphAction = "Co-paragraph-top_ruler-position";
	public static final String topRulerThicknessParagraphAction = "Co-paragraph-top_ruler-thickness";
	public static final String topRulerSpanParagraphAction = "Co-paragraph-top_ruler-span";
	public static final String topRulerAlignmentParagraphAction = "Co-paragraph-top_ruler-alignment";
	public static final String topRulerFixedWidthParagraphAction = "Co-paragraph-top_ruler-fixed-width";
	public static final String topRulerLeftIndentParagraphAction = "Co-paragraph-top_ruler-left-indent";
	public static final String topRulerRightIndentParagraphAction = "Co-paragraph-top_ruler-right-indent";
	public static final String bottomRulerPositionParagraphAction = "Co-paragraph-bottom_ruler-position";
	public static final String bottomRulerThicknessParagraphAction = "Co-paragraph-bottom_ruler-thickness";
	public static final String bottomRulerSpanParagraphAction = "Co-paragraph-bottom_ruler-span";
	public static final String bottomRulerAlignmentParagraphAction = "Co-paragraph-bottom_ruler-alignment";
	public static final String bottomRulerFixedWidthParagraphAction = "Co-paragraph-bottom_ruler-fixed-width";
	public static final String bottomRulerLeftIndentParagraphAction = "Co-paragraph-bottom_ruler-left-indent";
	public static final String bottomRulerRightIndentParagraphAction = "Co-paragraph-bottom_ruler-right-indent";
	public static final String regularTabStopIntervalParagraphAction = "Co-paragraph-regular-tab-stop-interval";
	public static final String minimumSpaceWidthParagraphAction = "Co-paragraph-minimum-space-width";
	public static final String optimumSpaceWidthParagraphAction = "Co-paragraph-optimum-space-width";

	// general attribute action names
	public static final String clearCharacterAttributesAction = "Co-character-clear-attributes";
	public static final String copyCharacterOrParagraphAttributesAction = "Co-copy-character-or-paragraph-attributes";

	// tag action names
	public static final String tagCharacterAction = "Co-character-tag";
	public static final String tagParagraphAction = "Co-paragraph-tag";
	public static final String tagChainAction = "Co-tag-chain";
	
	// comment action names
	public static final String commentAction = "Co-comment";
	
	// macro action names
	public static final String macroAction = "Co-macro";


//	public static final String expandMacrosAction = "Co-expand-macros";

	// dialog action names
	public static final String otherFontSizeCharacterAction = "Co-character-other-font-size";
	public static final String otherFontSizeParagraphAction = "Co-paragraph-other-font-size";
	public static final String otherForegroundShadeCharacterAction = "Co-character-other-foreground-shade";
	public static final String otherForegroundShadeParagraphAction = "Co-paragraph-other-foreground-shade";





	
	// ---------------------------------------------------------------------------------------


	// character font attribute actions
	public static final CoStyledTextAction PLAIN_CHARACTER_ACTION = new CoPlainCharacterAction( plainCharacterAction );
	public static final CoStyledTextAction BOLD_CHARACTER_ACTION = new CoCircularStateCharacterAction( WEIGHT, boldCharacterAction, new Object [] { com.bluebrim.font.shared.CoFontAttribute.BOLD, com.bluebrim.font.shared.CoFontAttribute.NORMAL_WEIGHT, null } );//	CoTriStateBooleanCharacterAction( WEIGHT, boldCharacterAction );
	public static final CoStyledTextAction ITALIC_CHARACTER_ACTION = new CoCircularStateCharacterAction( STYLE, italicCharacterAction, new Object [] { com.bluebrim.font.shared.CoFontAttribute.ITALIC, com.bluebrim.font.shared.CoFontAttribute.NORMAL_STYLE, null } );//CoTriStateBooleanCharacterAction( STYLE, italicCharacterAction );
	public static final CoStyledTextAction STRIKETHRU_CHARACTER_ACTION = new CoTriStateBooleanCharacterAction( STRIKE_THRU, strikeThruCharacterAction );

	public static final CoStyledTextAction SHADOW_CHARACTER_ACTION = new CoTriStateBooleanCharacterAction( SHADOW, shadowCharacterAction );
	public static final CoStyledTextAction ALL_CAPS_CHARACTER_ACTION = new CoTriStateBooleanCharacterAction(ALL_CAPS , allCapsCharacterAction );

	public static final CoStyledTextAction SUPERIOR_CHARACTER_ACTION = new CoTriStateBooleanCharacterAction( SUPERIOR, superiorCharacterAction );
	public static final CoStyledTextAction VERTICAL_POSITION_CHARACTER_ACTION = new CoEnumCharacterAction( VERTICAL_POSITION, verticalPositionCharacterAction );
	public static final CoStyledTextAction UNDERLINE_CHARACTER_ACTION = new CoEnumCharacterAction( UNDERLINE, underlineCharacterAction);
	public static final CoStyledTextAction FONT_FAMILY_CHARACTER_ACTION = new CoStringCharacterAction( FONT_FAMILY, fontFamilyCharacterAction);
	public static final CoStyledTextAction FONT_SIZE_CHARACTER_ACTION = new CoFloatCharacterAction(FONT_SIZE, fontSizeCharacterAction);
	public static final CoStyledTextAction FOREGROUND_COLOR_CHARACTER_ACTION = new CoColorCharacterAction( FOREGROUND_COLOR, foregroundColorCharacterAction );
	public static final CoStyledTextAction FOREGROUND_SHADE_CHARACTER_ACTION = new CoFloatCharacterAction( FOREGROUND_SHADE, foregroundShadeCharacterAction);
	public static final CoStyledTextAction TRACK_AMOUNT_CHARACTER_ACTION = new CoTrackAmountCharacterAction( TRACK_AMOUNT, trackAmountCharacterAction );
	public static final CoStyledTextAction BASELINE_OFFSET_CHARACTER_ACTION = new CoFloatCharacterAction( BASELINE_OFFSET, baselineOffsetCharacterAction);
	public static final CoStyledTextAction HORIZONTAL_SCALE_CHARACTER_ACTION = new CoFloatCharacterAction( HORIZONTAL_SCALE, horizontalScaleCharacterAction);
	public static final CoStyledTextAction VERTICAL_SCALE_CHARACTER_ACTION = new CoFloatCharacterAction( VERTICAL_SCALE, verticalScaleCharacterAction);
	public static final CoStyledTextAction SHADOW_ANGLE_CHARACTER_ACTION = new CoFloatCharacterAction( SHADOW_ANGLE, shadowAngleCharacterAction);
	public static final CoStyledTextAction SHADOW_COLOR_CHARACTER_ACTION = new CoColorCharacterAction( SHADOW_COLOR, shadowColorCharacterAction);
	public static final CoStyledTextAction SHADOW_OFFSET_CHARACTER_ACTION = new CoFloatCharacterAction( SHADOW_OFFSET, shadowOffsetCharacterAction);
	public static final CoStyledTextAction SHADOW_SHADE_CHARACTER_ACTION = new CoFloatCharacterAction( SHADOW_SHADE, shadowShadeCharacterAction);


	// paragraph font attribute actions
	public static final CoStyledTextAction PLAIN_PARAGRAPH_ACTION = new CoPlainParagraphAction( plainParagraphAction );
	public static final CoStyledTextAction BOLD_PARAGRAPH_ACTION = new CoCircularStateParagraphAction( WEIGHT, boldParagraphAction, new Object [] { com.bluebrim.font.shared.CoFontAttribute.BOLD, com.bluebrim.font.shared.CoFontAttribute.NORMAL_WEIGHT, null } );//CoTriStateBooleanParagraphAction( WEIGHT, boldParagraphAction );
	public static final CoStyledTextAction ITALIC_PARAGRAPH_ACTION = new CoCircularStateParagraphAction( STYLE, italicParagraphAction, new Object [] { com.bluebrim.font.shared.CoFontAttribute.ITALIC, com.bluebrim.font.shared.CoFontAttribute.NORMAL_STYLE, null } );//CoTriStateBooleanParagraphAction( STYLE, italicParagraphAction );
	public static final CoStyledTextAction STRIKETHRU_PARAGRAPH_ACTION = new CoTriStateBooleanParagraphAction( STRIKE_THRU, strikeThruParagraphAction );

	public static final CoStyledTextAction SHADOW_PARAGRAPH_ACTION = new CoTriStateBooleanParagraphAction( SHADOW, shadowParagraphAction );
	public static final CoStyledTextAction ALL_CAPS_PARAGRAPH_ACTION = new CoTriStateBooleanParagraphAction(ALL_CAPS , allCapsParagraphAction );

	public static final CoStyledTextAction SUPERIOR_PARAGRAPH_ACTION = new CoTriStateBooleanParagraphAction( SUPERIOR, superiorParagraphAction );
	public static final CoStyledTextAction VERTICAL_POSITION_PARAGRAPH_ACTION = new CoEnumParagraphAction( VERTICAL_POSITION, verticalPositionParagraphAction );
	public static final CoStyledTextAction UNDERLINE_PARAGRAPH_ACTION = new CoEnumParagraphAction( UNDERLINE, underlineParagraphAction);
	public static final CoStyledTextAction FONT_FAMILY_PARAGRAPH_ACTION = new CoStringParagraphAction( FONT_FAMILY, fontFamilyParagraphAction);
	public static final CoStyledTextAction FONT_SIZE_PARAGRAPH_ACTION = new CoFloatParagraphAction(FONT_SIZE, fontSizeParagraphAction);
	public static final CoStyledTextAction FOREGROUND_COLOR_PARAGRAPH_ACTION = new CoColorParagraphAction( FOREGROUND_COLOR, foregroundColorParagraphAction );
	public static final CoStyledTextAction FOREGROUND_SHADE_PARAGRAPH_ACTION = new CoFloatParagraphAction( FOREGROUND_SHADE, foregroundShadeParagraphAction);
	public static final CoStyledTextAction TRACK_AMOUNT_PARAGRAPH_ACTION = new CoFloatParagraphAction( TRACK_AMOUNT, trackAmountParagraphAction);
	public static final CoStyledTextAction BASELINE_OFFSET_PARAGRAPH_ACTION = new CoFloatParagraphAction( BASELINE_OFFSET, baselineOffsetParagraphAction);
	public static final CoStyledTextAction HORIZONTAL_SCALE_PARAGRAPH_ACTION = new CoFloatParagraphAction( HORIZONTAL_SCALE, horizontalScaleParagraphAction);
	public static final CoStyledTextAction VERTICAL_SCALE_PARAGRAPH_ACTION = new CoFloatParagraphAction( VERTICAL_SCALE, verticalScaleParagraphAction);
	public static final CoStyledTextAction SHADOW_SHADE_PARAGRAPH_ACTION = new CoFloatParagraphAction( SHADOW_SHADE, shadowShadeParagraphAction);
	public static final CoStyledTextAction SHADOW_ANGLE_PARAGRAPH_ACTION = new CoFloatParagraphAction( SHADOW_ANGLE, shadowAngleParagraphAction);
	public static final CoStyledTextAction SHADOW_COLOR_PARAGRAPH_ACTION = new CoColorParagraphAction( SHADOW_COLOR, shadowColorParagraphAction);
	public static final CoStyledTextAction SHADOW_OFFSET_PARAGRAPH_ACTION = new CoFloatParagraphAction( SHADOW_OFFSET, shadowOffsetParagraphAction);


	// character/paragraph font attribute actions
	public static final CoStyledTextAction FONT_FAMILY_CHARACTER_OR_PARAGRAPH_ACTION = new CoStringCharacterOrParagraphAction( FONT_FAMILY, fontFamilyCharacterOrParagraphAction);
	public static final CoStyledTextAction FONT_SIZE_CHARACTER_OR_PARAGRAPH_ACTION = new CoFloatCharacterOrParagraphAction( FONT_SIZE, fontSizeCharacterOrParagraphAction);


	// paragraph attribute actions
	public static final CoStyledTextAction ALIGNMENT_PARAGRAPH_ACTION = new CoEnumParagraphAction( ALIGNMENT, alignmentParagraphAction );
	public static final CoStyledTextAction HYPHENATION_PARAGRAPH_ACTION = new CoStringParagraphAction( HYPHENATION, hyphenationParagraphAction );
	public static final CoStyledTextAction HYPHENATION_FALLBACK_BEHAVIOR_PARAGRAPH_ACTION = new CoEnumParagraphAction( HYPHENATION_FALLBACK_BEHAVIOR, hyphenationFallbackBehaviorParagraphAction );

	public static final CoStyledTextAction ADJUST_TO_BASE_LINE_GRID_PARAGRAPH_ACTION = new CoTriStateBooleanParagraphAction( ADJUST_TO_BASE_LINE_GRID, adjustToBaseLineGridParagraphAction );
	public static final CoStyledTextAction LAST_IN_COLUMN_PARAGRAPH_ACTION = new CoTriStateBooleanParagraphAction( LAST_IN_COLUMN, lastInColumnParagraphAction);
	public static final CoStyledTextAction TOP_OF_COLUMN_PARAGRAPH_ACTION = new CoTriStateBooleanParagraphAction( TOP_OF_COLUMN, topOfColumnParagraphAction);
	public static final CoStyledTextAction LINES_TOGETHER_PARAGRAPH_ACTION = new CoTriStateBooleanParagraphAction( LINES_TOGETHER, linesTogetherParagraphAction);
	public static final CoStyledTextAction DROP_CAPS_PARAGRAPH_ACTION = new CoTriStateBooleanParagraphAction( DROP_CAPS, dropCapsParagraphAction);
	public static final CoStyledTextAction LEFT_INDENT_PARAGRAPH_ACTION = new CoFloatParagraphAction( LEFT_INDENT, leftIndentParagraphAction);
	public static final CoStyledTextAction RIGHT_INDENT_PARAGRAPH_ACTION = new CoFloatParagraphAction( RIGHT_INDENT, rightIndentParagraphAction);
	public static final CoStyledTextAction FIRST_LINE_PARAGRAPH_ACTION = new CoFloatParagraphAction( FIRST_LINE_INDENT, firstLineIndentParagraphAction);
	public static final CoStyledTextAction TRAILING_LINES_INDENT_PARAGRAPH_ACTION = new CoFloatParagraphAction( TRAILING_LINES_INDENT, trailingLinesIndentParagraphAction);
	public static final CoStyledTextAction SPACE_ABOVE_PARAGRAPH_ACTION = new CoFloatParagraphAction( SPACE_ABOVE, spaceAboveParagraphAction);
	public static final CoStyledTextAction SPACE_BELOW_PARAGRAPH_ACTION = new CoFloatParagraphAction( SPACE_BELOW, spaceBelowParagraphAction);
	public static final CoStyledTextAction LEADING_PARAGRAPH_ACTION = new CoLeadingParagraphAction( leadingParagraphAction );
	public static final CoStyledTextAction DROP_CAPS_WIDTH_PARAGRAPH_ACTION = new CoIntegerParagraphAction( DROP_CAPS_COUNT, dropCapsWidthParagraphAction );
	public static final CoStyledTextAction DROP_CAPS_HEIGHT_PARAGRAPH_ACTION = new CoIntegerParagraphAction( DROP_CAPS_HEIGHT, dropCapsHeightParagraphAction);

	public static final CoStyledTextAction TOP_RULER_POSITION_PARAGRAPH_ACTION = new CoFloatParagraphAction( TOP_RULER_POSITION, topRulerPositionParagraphAction );
	public static final CoStyledTextAction TOP_RULER_THICKNESS_PARAGRAPH_ACTION = new CoFloatParagraphAction( TOP_RULER_THICKNESS, topRulerThicknessParagraphAction );
	public static final CoStyledTextAction TOP_RULER_SPAN_PARAGRAPH_ACTION = new CoEnumParagraphAction( TOP_RULER_SPAN, topRulerSpanParagraphAction );
	public static final CoStyledTextAction TOP_RULER_ALIGNMENT_PARAGRAPH_ACTION = new CoEnumParagraphAction( TOP_RULER_ALIGNMENT, topRulerAlignmentParagraphAction );
	public static final CoStyledTextAction TOP_RULER_FIXED_WIDTH_PARAGRAPH_ACTION = new CoFloatParagraphAction( TOP_RULER_FIXED_WIDTH, topRulerFixedWidthParagraphAction );
	public static final CoStyledTextAction TOP_RULER_LEFT_INDENT_PARAGRAPH_ACTION = new CoFloatParagraphAction( TOP_RULER_LEFT_INSET, topRulerLeftIndentParagraphAction );
	public static final CoStyledTextAction TOP_RULER_RIGHT_INDENT_PARAGRAPH_ACTION = new CoFloatParagraphAction( TOP_RULER_RIGHT_INSET, topRulerRightIndentParagraphAction );
	public static final CoStyledTextAction BOTTOM_RULER_POSITION_PARAGRAPH_ACTION = new CoFloatParagraphAction( BOTTOM_RULER_POSITION, bottomRulerPositionParagraphAction );
	public static final CoStyledTextAction BOTTOM_RULER_THICKNESS_PARAGRAPH_ACTION = new CoFloatParagraphAction( BOTTOM_RULER_THICKNESS, bottomRulerThicknessParagraphAction );
	public static final CoStyledTextAction BOTTOM_RULER_SPAN_PARAGRAPH_ACTION = new CoEnumParagraphAction( BOTTOM_RULER_SPAN, bottomRulerSpanParagraphAction );
	public static final CoStyledTextAction BOTTOM_RULER_ALIGNMENT_PARAGRAPH_ACTION = new CoEnumParagraphAction( BOTTOM_RULER_ALIGNMENT, bottomRulerAlignmentParagraphAction );
	public static final CoStyledTextAction BOTTOM_RULER_FIXED_WIDTH_PARAGRAPH_ACTION = new CoFloatParagraphAction( BOTTOM_RULER_FIXED_WIDTH, bottomRulerFixedWidthParagraphAction );
	public static final CoStyledTextAction BOTTOM_RULER_LEFT_INDENT_PARAGRAPH_ACTION = new CoFloatParagraphAction( BOTTOM_RULER_LEFT_INSET, bottomRulerLeftIndentParagraphAction );
	public static final CoStyledTextAction BOTTOM_RULER_RIGHT_INDENT_PARAGRAPH_ACTION = new CoFloatParagraphAction( BOTTOM_RULER_RIGHT_INSET, bottomRulerRightIndentParagraphAction );

	public static final CoStyledTextAction REGULAR_TAB_STOP_INTERVAL_PARAGRAPH_ACTION = new CoFloatParagraphAction( REGULAR_TAB_STOP_INTERVAL, regularTabStopIntervalParagraphAction );
	public static final CoStyledTextAction MINIMUM_SPACE_WIDTH_PARAGRAPH_ACTION = new CoFloatParagraphAction( MINIMUM_SPACE_WIDTH, minimumSpaceWidthParagraphAction );
	public static final CoStyledTextAction OPTIMUM_SPACE_WIDTH_PARAGRAPH_ACTION = new CoFloatParagraphAction( OPTIMUM_SPACE_WIDTH, optimumSpaceWidthParagraphAction );

	// general attribute actions
	public static final CoStyledTextAction CLEAR_ATTRIBUTES_CHARACTER_ACTION = new CoClearCharacterAttributesAction( clearCharacterAttributesAction );
	public static final CoStyledTextAction COPY_ATTRIBUTES_CHARACTER_OR_PARAGRAPH_ACTION = new CoCopyCharacterOrParagraphAttributesAction( copyCharacterOrParagraphAttributesAction );


	// tag actions
	public static final CoStyledTextAction TAG_CHARACTER_ACTION = new CoTagCharacterAction( tagCharacterAction );
	public static final CoStyledTextAction TAG_PARAGRAPH_ACTION = new CoTagParagraphAction( tagParagraphAction );
	public static final CoStyledTextAction TAG_CHAIN_ACTION = new CoTagChainAction( tagChainAction );

	
	// comment actions
	public static final CoStyledTextAction COMMENT_ACTION = new CoCommentAction( commentAction );

	
	// macro actions
	public static final CoStyledTextAction MACRO_ACTION = new CoMacroAction( macroAction );



//	public static final CoStyledTextAction EXPAND_MACROS_ACTION = new CoExpandMacrosAction( expandMacrosAction );


	// dialog actions
	public static final CoStyledTextAction OTHER_FONT_SIZE_CHARACTER_ACTION = 
		new CoOtherFloatCharacterAction( FONT_SIZE,
																		 otherFontSizeCharacterAction,
																		 CoTextStringResources.getName( "OTHER_FONT_SIZE_TITLE" ),
																		 CoTextStringResources.getName( "OTHER_FONT_SIZE_QUESTION" ) );

	public static final CoStyledTextAction OTHER_FOREGROUND_SHADE_CHARACTER_ACTION =
		new CoOtherFloatCharacterAction( FOREGROUND_SHADE,
																		 otherForegroundShadeCharacterAction,
																		 CoTextStringResources.getName( "OTHER_SHADE_TITLE" ),
																		 CoTextStringResources.getName( "OTHER_SHADE_QUESTION" ) );

	public static final CoStyledTextAction OTHER_FONT_SIZE_PARAGRAPH_ACTION = 
		new CoOtherFloatParagraphAction( FONT_SIZE,
																		 otherFontSizeParagraphAction,
																		 CoTextStringResources.getName( "OTHER_FONT_SIZE_TITLE" ),
																		 CoTextStringResources.getName( "OTHER_FONT_SIZE_QUESTION" ) );

	public static final CoStyledTextAction OTHER_FOREGROUND_SHADE_PARAGRAPH_ACTION =
		new CoOtherFloatParagraphAction( FOREGROUND_SHADE,
																		 otherForegroundShadeParagraphAction,
																		 CoTextStringResources.getName( "OTHER_SHADE_TITLE" ),
																		 CoTextStringResources.getName( "OTHER_SHADE_QUESTION" ) );



	public static final String clearParagraphAttributesAction = "Co-paragraph-clear-attributes";
	public static final CoStyledTextAction CLEAR_ATTRIBUTES_PARAGRAPH_ACTION = new CoClearParagraphAttributesAction( clearParagraphAttributesAction );
	
	public static Action[] ACTIONS =
	{
		PLAIN_CHARACTER_ACTION,
		BOLD_CHARACTER_ACTION,
		ITALIC_CHARACTER_ACTION,
		STRIKETHRU_CHARACTER_ACTION,
//		OUTLINE_CHARACTER_ACTION,
		SHADOW_CHARACTER_ACTION,
		ALL_CAPS_CHARACTER_ACTION,
//		SMALL_CAPS_CHARACTER_ACTION,
		SUPERIOR_CHARACTER_ACTION,
		VERTICAL_POSITION_CHARACTER_ACTION,
		UNDERLINE_CHARACTER_ACTION,
		FONT_FAMILY_CHARACTER_ACTION,
		FONT_SIZE_CHARACTER_ACTION,
		FOREGROUND_COLOR_CHARACTER_ACTION,
		FOREGROUND_SHADE_CHARACTER_ACTION,
		TRACK_AMOUNT_CHARACTER_ACTION,
		BASELINE_OFFSET_CHARACTER_ACTION,
		PLAIN_PARAGRAPH_ACTION,
		BOLD_PARAGRAPH_ACTION,
		ITALIC_PARAGRAPH_ACTION,
		STRIKETHRU_PARAGRAPH_ACTION,
//		OUTLINE_PARAGRAPH_ACTION,
		SHADOW_PARAGRAPH_ACTION,
		ALL_CAPS_PARAGRAPH_ACTION,
//		SMALL_CAPS_PARAGRAPH_ACTION,
		SUPERIOR_PARAGRAPH_ACTION,
		VERTICAL_POSITION_PARAGRAPH_ACTION,
		UNDERLINE_PARAGRAPH_ACTION,
		FONT_FAMILY_PARAGRAPH_ACTION,
		FONT_SIZE_PARAGRAPH_ACTION,
		FOREGROUND_COLOR_PARAGRAPH_ACTION,
		FOREGROUND_SHADE_PARAGRAPH_ACTION,
		TRACK_AMOUNT_PARAGRAPH_ACTION,
		FONT_FAMILY_CHARACTER_OR_PARAGRAPH_ACTION,
		FONT_SIZE_CHARACTER_OR_PARAGRAPH_ACTION,
		ALIGNMENT_PARAGRAPH_ACTION,
		ADJUST_TO_BASE_LINE_GRID_PARAGRAPH_ACTION,
		LAST_IN_COLUMN_PARAGRAPH_ACTION,
		TOP_OF_COLUMN_PARAGRAPH_ACTION,
		HYPHENATION_PARAGRAPH_ACTION,
		HYPHENATION_FALLBACK_BEHAVIOR_PARAGRAPH_ACTION,
		LINES_TOGETHER_PARAGRAPH_ACTION,
		DROP_CAPS_PARAGRAPH_ACTION,
		LEFT_INDENT_PARAGRAPH_ACTION,
		RIGHT_INDENT_PARAGRAPH_ACTION,
		FIRST_LINE_PARAGRAPH_ACTION,
		TRAILING_LINES_INDENT_PARAGRAPH_ACTION,
		SPACE_ABOVE_PARAGRAPH_ACTION,
		SPACE_BELOW_PARAGRAPH_ACTION,
		LEADING_PARAGRAPH_ACTION,
		OPTIMUM_SPACE_WIDTH_PARAGRAPH_ACTION,
		MINIMUM_SPACE_WIDTH_PARAGRAPH_ACTION,
		DROP_CAPS_WIDTH_PARAGRAPH_ACTION,
		DROP_CAPS_HEIGHT_PARAGRAPH_ACTION,
		CLEAR_ATTRIBUTES_CHARACTER_ACTION,
		CLEAR_ATTRIBUTES_PARAGRAPH_ACTION,
		COPY_ATTRIBUTES_CHARACTER_OR_PARAGRAPH_ACTION,
		TAG_CHARACTER_ACTION,
		TAG_PARAGRAPH_ACTION,
		TAG_CHAIN_ACTION,
		COMMENT_ACTION,
		MACRO_ACTION,
//		EXPAND_MACROS_ACTION,
		OTHER_FONT_SIZE_CHARACTER_ACTION,
		OTHER_FOREGROUND_SHADE_CHARACTER_ACTION,
		OTHER_FONT_SIZE_PARAGRAPH_ACTION,
		OTHER_FOREGROUND_SHADE_PARAGRAPH_ACTION,
		TOP_RULER_POSITION_PARAGRAPH_ACTION,
		TOP_RULER_THICKNESS_PARAGRAPH_ACTION,
		TOP_RULER_SPAN_PARAGRAPH_ACTION,
		TOP_RULER_ALIGNMENT_PARAGRAPH_ACTION,
		TOP_RULER_FIXED_WIDTH_PARAGRAPH_ACTION,
		TOP_RULER_LEFT_INDENT_PARAGRAPH_ACTION,
		TOP_RULER_RIGHT_INDENT_PARAGRAPH_ACTION,
		BOTTOM_RULER_POSITION_PARAGRAPH_ACTION,
		BOTTOM_RULER_THICKNESS_PARAGRAPH_ACTION,
		BOTTOM_RULER_SPAN_PARAGRAPH_ACTION,
		BOTTOM_RULER_ALIGNMENT_PARAGRAPH_ACTION,
		BOTTOM_RULER_FIXED_WIDTH_PARAGRAPH_ACTION,
		BOTTOM_RULER_LEFT_INDENT_PARAGRAPH_ACTION,
		BOTTOM_RULER_RIGHT_INDENT_PARAGRAPH_ACTION,
		REGULAR_TAB_STOP_INTERVAL_PARAGRAPH_ACTION,
		HORIZONTAL_SCALE_CHARACTER_ACTION,
		VERTICAL_SCALE_CHARACTER_ACTION,
		BASELINE_OFFSET_PARAGRAPH_ACTION,
		HORIZONTAL_SCALE_PARAGRAPH_ACTION,
		VERTICAL_SCALE_PARAGRAPH_ACTION,
		SHADOW_OFFSET_CHARACTER_ACTION,
		SHADOW_ANGLE_CHARACTER_ACTION,
		SHADOW_COLOR_CHARACTER_ACTION,
		SHADOW_SHADE_CHARACTER_ACTION,
		SHADOW_OFFSET_PARAGRAPH_ACTION,
		SHADOW_ANGLE_PARAGRAPH_ACTION,
		SHADOW_COLOR_PARAGRAPH_ACTION,
		SHADOW_SHADE_PARAGRAPH_ACTION,

	};
}