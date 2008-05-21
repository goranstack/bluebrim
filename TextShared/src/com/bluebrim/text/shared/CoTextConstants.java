package com.bluebrim.text.shared;
import javax.swing.text.*;

import com.bluebrim.font.shared.*;
import com.bluebrim.text.impl.shared.*;

/**
 * Formated text constants
 *
 * @author: Dennis Malmström
 */

public interface CoTextConstants extends CoFontConstants
{
	// Paragraph formatting
	
		// Attribute names
	public static final String ALIGNMENT			= "alignment";	
	public static final String FIRST_LINE_INDENT	= "first_line_indent";	
	public static final String TRAILING_LINES_INDENT	= "trailing_lines_indent";	
	public static final String LEFT_INDENT			= "left_indent";	
	public static final String RIGHT_INDENT			= "right_indent";	
	public static final String SPACE_ABOVE			= "space_above";	
	public static final String SPACE_BELOW			= "space_below";	
	public static final String DROP_CAPS			= "drop_caps";
	public static final String DROP_CAPS_COUNT		= "drop_caps_count";
	public static final String DROP_CAPS_HEIGHT		= "drop_caps_height";
	public static final String LINES_TOGETHER		= "lines_together";
	public static final String LEADING			= "leading";
	public static final String TOP_OF_COLUMN			= "top_of_column";
	public static final String LAST_IN_COLUMN			= "last_in_column";
	public static final String HYPHENATION			= "hyphenation";
	public static final String HYPHENATION_FALLBACK_BEHAVIOR			= "hyphenation_fallback_behavior";

	public static final String TAB_SET			= "tab_set";
	public static final String REGULAR_TAB_STOP_INTERVAL			= "regular_tab_stop_interval";
	public static final String ADJUST_TO_BASE_LINE_GRID			= "adjust_to_base_line_grid";
	public static final String TOP_RULER_THICKNESS			= "top_ruler_thickness";
	public static final String TOP_RULER_POSITION			= "top_ruler_position";
	public static final String TOP_RULER_SPAN			= "top_ruler_span";
	public static final String TOP_RULER_ALIGNMENT			= "top_ruler_alignment";
	public static final String TOP_RULER_FIXED_WIDTH			= "top_ruler_width";
	public static final String TOP_RULER_LEFT_INSET			= "top_ruler_left_inset";
	public static final String TOP_RULER_RIGHT_INSET			= "top_ruler_right_inset";
	public static final String BOTTOM_RULER_THICKNESS			= "bottom_ruler_thickness";
	public static final String BOTTOM_RULER_POSITION			= "bottom_ruler_position";
	public static final String BOTTOM_RULER_SPAN			= "bottom_ruler_span";
	public static final String BOTTOM_RULER_ALIGNMENT			= "bottom_ruler_alignment";
	public static final String BOTTOM_RULER_FIXED_WIDTH			= "bottom_ruler_width";
	public static final String BOTTOM_RULER_LEFT_INSET			= "bottom_ruler_left_inset";
	public static final String BOTTOM_RULER_RIGHT_INSET			= "bottom_ruler_right_inset";
	public static final String MINIMUM_SPACE_WIDTH			= "minimum_space_width";
	public static final String OPTIMUM_SPACE_WIDTH			= "optimum_space_width";
	
	public static final String[] PARAGRAPH_ATTRIBUTES = new String []
	{
		ALIGNMENT,
		FIRST_LINE_INDENT,
		TRAILING_LINES_INDENT,
		LEFT_INDENT,
		RIGHT_INDENT,
		SPACE_ABOVE,
		SPACE_BELOW,
		DROP_CAPS,
		DROP_CAPS_COUNT,
		DROP_CAPS_HEIGHT,
		LINES_TOGETHER,
		LEADING,
		TOP_OF_COLUMN,
		LAST_IN_COLUMN,
		HYPHENATION,
		HYPHENATION_FALLBACK_BEHAVIOR,
		TAB_SET,
		REGULAR_TAB_STOP_INTERVAL,
		ADJUST_TO_BASE_LINE_GRID,
		TOP_RULER_THICKNESS,
		TOP_RULER_POSITION,
		TOP_RULER_SPAN,
		TOP_RULER_ALIGNMENT,
		TOP_RULER_FIXED_WIDTH,
		TOP_RULER_LEFT_INSET,
		TOP_RULER_RIGHT_INSET,
		BOTTOM_RULER_THICKNESS,
		BOTTOM_RULER_POSITION,
		BOTTOM_RULER_SPAN,
		BOTTOM_RULER_ALIGNMENT,
		BOTTOM_RULER_FIXED_WIDTH,
		BOTTOM_RULER_LEFT_INSET,
		BOTTOM_RULER_RIGHT_INSET,
		OPTIMUM_SPACE_WIDTH,
		MINIMUM_SPACE_WIDTH,
	};
	
		// Attribute values
	public static final CoEnumValue ALIGN_LEFT = new CoEnumValue( "ALIGN_LEFT" );
	public static final CoEnumValue ALIGN_CENTER = new CoEnumValue( "ALIGN_CENTER" );
	public static final CoEnumValue ALIGN_RIGHT = new CoEnumValue( "ALIGN_RIGHT" );
	public static final CoEnumValue ALIGN_JUSTIFIED = new CoEnumValue( "ALIGN_JUSTIFIED" );
	public static final CoEnumValue ALIGN_FORCED = new CoEnumValue( "ALIGN_FORCED" );

	public static final CoEnumValue RULER_SPAN_TEXT = new CoEnumValue( "RULER_SPAN_TEXT" );
	public static final CoEnumValue RULER_SPAN_FIXED = new CoEnumValue( "RULER_SPAN_FIXED" );
	public static final CoEnumValue RULER_SPAN_COLUMN = new CoEnumValue( "RULER_SPAN_COLUMN" );

	public static final CoEnumValue HYPHENATION_FALLBACK_BEHAVIOR_FIRST_BREAKPOINT = new CoEnumValue( "HYPHENATION_FALLBACK_BEHAVIOR_FIRST_BREAKPOINT" );
	public static final CoEnumValue HYPHENATION_FALLBACK_BEHAVIOR_NON_BREAKPOINT = new CoEnumValue( "HYPHENATION_FALLBACK_BEHAVIOR_NON_BREAKPOINT" );
	public static final CoEnumValue HYPHENATION_FALLBACK_BEHAVIOR_NO_LINE = new CoEnumValue( "HYPHENATION_FALLBACK_BEHAVIOR_NO_LINE" );








	
/*
		// leading modulating
	public static final float ABSOLUTE_LEADING_MAX = 10000f;
	public static final float ABSOLUTE_LEADING_MIN = - ABSOLUTE_LEADING_MAX;
	public static final float OFFSET_LEADING_ZERO = 2 * ABSOLUTE_LEADING_MAX + 1;
	public static final float RELATIVE_LEADING_ZERO = - OFFSET_LEADING_ZERO;
	public static final float AUTO_LEADING_VALUE = Float.MAX_VALUE;
*/
	
	// Character formatting
	
		// Attribute names
	public static final String FOREGROUND_COLOR	= "foreground_color";
	public static final String FOREGROUND_SHADE	= "foreground_shade";
	public static final String TRACK_AMOUNT 		= "track_amount";
	public static final String FONT_SIZE		= "font_size";
	public static final String STRIKE_THRU			= "strike_thru";

	public static final String SHADOW				= "shadow";
	public static final String ALL_CAPS				= "all_caps";
	public static final String VERTICAL_POSITION			= "vertical_position";
	public static final String SUPERIOR				= "superior";
	public static final String STYLE					= "style";
	
	public static final String WEIGHT					= "weight";
	
	public static final String UNDERLINE			= "underline";	
	public static final String FONT_FAMILY			= "font_family";
	public static final String BASELINE_OFFSET = "baseline_offset";
	public static final String HORIZONTAL_SCALE			= "horizontal_scale";
	public static final String VERTICAL_SCALE			= "vertical_scale";
	public static final String SHADOW_ANGLE			= "shadow_angle";
	public static final String SHADOW_COLOR			= "shadow_color";
	public static final String SHADOW_OFFSET			= "shadow_offset";
	public static final String SHADOW_SHADE			= "shadow_shade";

	public static final String[] CHARACTER_FONT_ATTRIBUTES = new String []
	{
		FONT_SIZE,
		FONT_FAMILY,
		FOREGROUND_COLOR,
		FOREGROUND_SHADE,
		TRACK_AMOUNT,
		BASELINE_OFFSET,
		HORIZONTAL_SCALE,
		VERTICAL_SCALE,
	};

	public static final String[] CHARACTER_STYLE_ATTRIBUTES = new String []
	{
		STRIKE_THRU,
		SHADOW,
		ALL_CAPS,
		VERTICAL_POSITION,
		SUPERIOR,
		STYLE,
		WEIGHT,
		UNDERLINE,
		SHADOW_OFFSET,
		SHADOW_ANGLE,
		SHADOW_COLOR,
		SHADOW_SHADE,
	};

	public static final String[] CHARACTER_ATTRIBUTES = 
		new String []
		{
			FONT_FAMILY,
			FONT_SIZE,
			FOREGROUND_COLOR,
			FOREGROUND_SHADE,
			TRACK_AMOUNT,
			BASELINE_OFFSET,
			HORIZONTAL_SCALE,
			VERTICAL_SCALE,
			STRIKE_THRU,
			SHADOW,
			ALL_CAPS,
			VERTICAL_POSITION,
			SUPERIOR,
			STYLE,
			WEIGHT,
			UNDERLINE,
			SHADOW_OFFSET,
			SHADOW_ANGLE,
			SHADOW_COLOR,
			SHADOW_SHADE,
		};
	
		// Attribute values
	public static final CoEnumValue UNDERLINE_NONE = new CoEnumValue( "UNDERLINE_NONE" );
	public static final CoEnumValue UNDERLINE_NORMAL = new CoEnumValue( "UNDERLINE_NORMAL" );
	public static final CoEnumValue UNDERLINE_WORD = new CoEnumValue( "UNDERLINE_WORD" );

	public static final CoEnumValue VERTICAL_POSITION_NONE = new CoEnumValue( "VERTICAL_POSITION_NONE" );
	public static final CoEnumValue VERTICAL_POSITION_SUPERSCRIPT = new CoEnumValue( "VERTICAL_POSITION_SUPERSCRIPT" );
	public static final CoEnumValue VERTICAL_POSITION_SUBSCRIPT = new CoEnumValue( "VERTICAL_POSITION_SUBSCRIPT" );


	
	// Paragraph and character tags

	// Tag names
	public static final String DEFAULT_TAG_NAME = "-";
	
		// Attribute names
  public static final String PARAGRAPH_TAG = "paragraph_tag";
	public static final String CHARACTER_TAG = "character_tag";
	public static final String TAG_CHAIN = "tag_chain";
	public static final String IS_DELETED = "is_deleted";
	public static final String KEY_STROKE = "KEY_STROKE";


	
	// Macros
	
		// Attribute names
	public static final String MACRO	= "macro";


	
	// Comments
	
		// Attribute names
	public static final String COMMENT = "comment";
	
		// element names
	public static final String CommentElementName	= "comment";

	
	
	// Factory keys
	public static final String CHARACTER_STYLE	= "character_style";
	public static final String PARAGRAPH_STYLE	= "paragraph_style";
	
	
	// Misc.

		// Attribute values
	public static final String NO_VALUE = " ";


	// Attribute categories
	public static final AttributeSet PARAGRAPH_STYLE_ATTRIBUTE_SET = new CoSimpleAttributeSet(PARAGRAPH_ATTRIBUTES);
	public static final AttributeSet CHARACTER_FONT_ATTRIBUTE_SET = new CoSimpleAttributeSet(CHARACTER_FONT_ATTRIBUTES);
	public static final AttributeSet CHARACTER_STYLE_ATTRIBUTE_SET = new CoSimpleAttributeSet(CHARACTER_STYLE_ATTRIBUTES);
		
	// special characters
//	public static final char NON_BREAK_SPACE_CHARACTER = '\u00A0';
	public static final String NO_BREAK_SPACE_STRING = "" + CoUnicode.NO_BREAK_SPACE;
	
}