package com.bluebrim.text.impl.shared;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.text.shared.*;

/**
 * Utility class for access to text attributes in an attribute set
 *
 * Note: The get/is methods of this class do not traverse the resolver reference of an attrivute set.
 *
 * Also see CoViewStyleConstants.
 *
 * @author: Dennis Malmström
 */

public class CoStyleConstants implements CoTextConstants
{
  public static final Object AS_IS_VALUE = new Object();

  public static final Boolean AS_IS_BOOLEAN_VALUE = new Boolean( false );
  public static final Integer AS_IS_INTEGER_VALUE = new Integer( 0 );
  public static final Float AS_IS_FLOAT_VALUE = new Float( 0 );
  public static final CoEnumValue AS_IS_ENUM_VALUE = new CoEnumValue( "as is" );
  public static final String AS_IS_STRING_VALUE = "as is";
  public static final com.bluebrim.font.shared.CoFontAttribute AS_IS_FONT_ATTRIBUTE_VALUE = com.bluebrim.font.shared.CoFontAttribute.AS_IS;
  public static final CoTabSetIF AS_IS_TAB_SET_VALUE =
  	new CoTabSetIF()
  	{
	  	public CoTabStopIF addTabStop() { return null; }
			public CoTabSetIF copy() { return null; }
			public int getIndexOfTabStop( CoTabStopIF t ) { return -1; }
			public CoTabStopIF getTabAfter( float location ) { return null; }
	  	public CoTabStopIF getTabStop( int i ) { return null; }
			public int getTabStopCount() { return 0; }
			public void removeTabStop( CoTabStopIF tabStop ) {}	
  	};
  


  

private CoStyleConstants()
{
}



public static Boolean enum2Boolean( com.bluebrim.font.shared.CoFontAttribute value, com.bluebrim.font.shared.CoFontAttribute trueValue )
{
	if ( value == null ) return null;
	return ( value.getValue() == trueValue.getValue() ) ? Boolean.TRUE : Boolean.FALSE;
}
public static Object get( AttributeSet a, Object attribute, Object isAsValue )
{
	if ( ! a.isDefined( attribute ) ) return null;

	Object value = a.getAttribute( attribute );

	if ( value == AS_IS_VALUE ) return isAsValue;//throw m_asIsValueException;
		
	return value;
}
public static Boolean getAdjustToBaseLineGrid(AttributeSet a)
{
	return (Boolean) get( a, ADJUST_TO_BASE_LINE_GRID, AS_IS_BOOLEAN_VALUE );
}
	/**
	 * Gets the alignment setting.
	 *
	 * @param a the attribute set
	 * @returns the value
	 */
public static CoEnumValue getAlignment(AttributeSet a)
{
	return (CoEnumValue) get( a, ALIGNMENT, AS_IS_ENUM_VALUE );
}
public static Boolean getAllCaps(AttributeSet a)
{
	return (Boolean) get(a, ALL_CAPS, AS_IS_BOOLEAN_VALUE);
}
public static Float getBaselineOffset( AttributeSet a )
{
	return (Float) get( a, BASELINE_OFFSET, AS_IS_FLOAT_VALUE );
}
public static CoEnumValue getBottomRulerAlignment( AttributeSet a )
{
	return (CoEnumValue) get( a, BOTTOM_RULER_ALIGNMENT, AS_IS_ENUM_VALUE );
}
public static Float getBottomRulerFixedWidth( AttributeSet a )
{
	return (Float) get( a, BOTTOM_RULER_FIXED_WIDTH, AS_IS_FLOAT_VALUE );
}
public static Float getBottomRulerLeftInset( AttributeSet a )
{
	return (Float) get( a, BOTTOM_RULER_LEFT_INSET, AS_IS_FLOAT_VALUE );
}
public static Float getBottomRulerPosition( AttributeSet a )
{
	return (Float) get( a, BOTTOM_RULER_POSITION, AS_IS_FLOAT_VALUE );
}
public static Float getBottomRulerRightInset( AttributeSet a )
{
	return (Float) get( a, BOTTOM_RULER_RIGHT_INSET, AS_IS_FLOAT_VALUE );
}
public static CoEnumValue getBottomRulerSpan( AttributeSet a )
{
	return (CoEnumValue) get( a, BOTTOM_RULER_SPAN, AS_IS_ENUM_VALUE );
}
public static Float getBottomRulerThickness( AttributeSet a )
{
	return (Float) get( a, BOTTOM_RULER_THICKNESS, AS_IS_FLOAT_VALUE );
}
public static String getCharacterTag( AttributeSet a )
{
	return (String) get( a, CHARACTER_TAG, AS_IS_STRING_VALUE );
}
public static Boolean getDropCaps( AttributeSet a )
{
	return (Boolean) get( a, DROP_CAPS, AS_IS_BOOLEAN_VALUE );
}
public static Integer getDropCapsCharacterCount( AttributeSet a )
{
	return (Integer) get( a, DROP_CAPS_COUNT, AS_IS_INTEGER_VALUE );
}
public static Integer getDropCapsLineCount( AttributeSet a )
{
	return (Integer) get( a, DROP_CAPS_HEIGHT, AS_IS_INTEGER_VALUE );
}
public static Float getFirstLineIndent( AttributeSet a )
{
	return (Float) get( a, FIRST_LINE_INDENT, AS_IS_FLOAT_VALUE );
}
public static String getFontFamily( AttributeSet a )
{
	return (String) get( a, FONT_FAMILY, AS_IS_STRING_VALUE );
}
public static Float getFontSize( AttributeSet a )
{
	return (Float) get( a, FONT_SIZE, AS_IS_FLOAT_VALUE );
}
public static String getForegroundColor(AttributeSet a)
{
	return (String) get( a, FOREGROUND_COLOR, AS_IS_STRING_VALUE );
}
public static Float getForegroundShade(AttributeSet a)
{
	return (Float) get( a, FOREGROUND_SHADE, AS_IS_FLOAT_VALUE );
}
public static Float getHorizontalScale(AttributeSet a)
{
	return (Float) get( a, HORIZONTAL_SCALE, AS_IS_FLOAT_VALUE );
}
public static String getHyphenation( AttributeSet a )
{
	return (String) get( a, HYPHENATION, AS_IS_STRING_VALUE );
}
public static CoEnumValue getHyphenationFallbackBehavior( AttributeSet a )
{
	return (CoEnumValue) get( a, HYPHENATION_FALLBACK_BEHAVIOR, AS_IS_ENUM_VALUE );
}
public static Boolean getKeepLinesTogether( AttributeSet a )
{
	return (Boolean) get( a, LINES_TOGETHER, AS_IS_BOOLEAN_VALUE );
}
public static KeyStroke getKeyStroke(AttributeSet a)
{
	return (KeyStroke) get(a, KEY_STROKE, null );
}
public static Boolean getLastInColumn(AttributeSet a)
{
	return (Boolean) get(a, LAST_IN_COLUMN, AS_IS_BOOLEAN_VALUE);
}

public static Float getLeftIndent(AttributeSet a)
{
	return (Float) get( a, LEFT_INDENT, AS_IS_FLOAT_VALUE );
}

public static String getParagraphTag( AttributeSet a )
{
	return (String) get( a, PARAGRAPH_TAG, AS_IS_STRING_VALUE );
}
public static Float getRegularTabStopInterval(AttributeSet a)
{
	return (Float) get( a, REGULAR_TAB_STOP_INTERVAL, AS_IS_FLOAT_VALUE );
}
public static Float getRightIndent(AttributeSet a)
{
	return (Float) get( a, RIGHT_INDENT, AS_IS_FLOAT_VALUE );
}
public static Boolean getShadow(AttributeSet a)
{
	return (Boolean) get(a, SHADOW, AS_IS_BOOLEAN_VALUE);
}
public static Float getSpaceAbove(AttributeSet a)
{
	return (Float) get( a, SPACE_ABOVE, AS_IS_FLOAT_VALUE );
}
public static Float getSpaceBelow(AttributeSet a)
{
	return (Float) get( a, SPACE_BELOW, AS_IS_FLOAT_VALUE );
}

public static Boolean getStrikeThru(AttributeSet a)
{
	return (Boolean) get(a, STRIKE_THRU, AS_IS_BOOLEAN_VALUE);
}
public static com.bluebrim.font.shared.CoFontAttribute getStyle(AttributeSet a)
{
	return (com.bluebrim.font.shared.CoFontAttribute) get(a, STYLE, AS_IS_FONT_ATTRIBUTE_VALUE);
}
public static Boolean getSuperior(AttributeSet a)
{
	return (Boolean) get(a, SUPERIOR, AS_IS_BOOLEAN_VALUE);
}
public static CoTabSetIF getTabSet( AttributeSet a )
{
	CoTabSetIF s = (CoTabSetIF) get( a, TAB_SET, AS_IS_TAB_SET_VALUE );

	return s;
}
public static Boolean getTopOfColumn(AttributeSet a)
{
	return (Boolean) get(a, TOP_OF_COLUMN, AS_IS_BOOLEAN_VALUE);
}
public static CoEnumValue getTopRulerAlignment( AttributeSet a )
{
	return (CoEnumValue) get( a, TOP_RULER_ALIGNMENT, AS_IS_ENUM_VALUE );
}
public static Float getTopRulerFixedWidth( AttributeSet a )
{
	return (Float) get( a, TOP_RULER_FIXED_WIDTH, AS_IS_FLOAT_VALUE );
}
public static Float getTopRulerLeftInset( AttributeSet a )
{
	return (Float) get( a, TOP_RULER_LEFT_INSET, AS_IS_FLOAT_VALUE );
}
public static Float getTopRulerPosition( AttributeSet a )
{
	return (Float) get( a, TOP_RULER_POSITION, AS_IS_FLOAT_VALUE );
}
public static Float getTopRulerRightInset( AttributeSet a )
{
	return (Float) get( a, TOP_RULER_RIGHT_INSET, AS_IS_FLOAT_VALUE );
}
public static CoEnumValue getTopRulerSpan( AttributeSet a )
{
	return (CoEnumValue) get( a, TOP_RULER_SPAN, AS_IS_ENUM_VALUE );
}
public static Float getTopRulerThickness( AttributeSet a )
{
	return (Float) get( a, TOP_RULER_THICKNESS, AS_IS_FLOAT_VALUE );
}
public static Float getTrackAmount( AttributeSet a )
{
	return (Float) get( a, TRACK_AMOUNT, AS_IS_FLOAT_VALUE );
}
public static Float getTrailingLinesIndent( AttributeSet a )
{
	return (Float) get( a, TRAILING_LINES_INDENT, AS_IS_FLOAT_VALUE );
}

public static CoEnumValue getUnderline( AttributeSet a )
{
	return (CoEnumValue) get( a, UNDERLINE, AS_IS_ENUM_VALUE );
}

public static com.bluebrim.font.shared.CoFontAttribute getWeight(AttributeSet a)
{
	return (com.bluebrim.font.shared.CoFontAttribute) get(a, WEIGHT, AS_IS_FONT_ATTRIBUTE_VALUE);
}
public static CoEnumValue getVerticalPosition( AttributeSet a )
{
	return (CoEnumValue) get( a, VERTICAL_POSITION, AS_IS_ENUM_VALUE );
}
public static Float getVerticalScale(AttributeSet a)
{
	return (Float) get( a, VERTICAL_SCALE, AS_IS_FLOAT_VALUE );
}
public static void set( MutableAttributeSet a, Object attribute, Object value )
{
	if
		( value != null )
	{
		a.addAttribute( attribute, value );
	} else {
		a.removeAttribute( attribute );
	}
}
public static void setAdjustToBaseLineGrid(MutableAttributeSet a, Boolean b)
{
	set( a, ADJUST_TO_BASE_LINE_GRID, b );
}
public static void setAlignment(MutableAttributeSet a, CoEnumValue align)
{
	set( a, ALIGNMENT, align );
}
public static void setAllCaps( MutableAttributeSet a, Boolean state )
{
	set( a, ALL_CAPS, state );
}

public static void setBaselineOffset( MutableAttributeSet a, Float blof )
{
	set( a, BASELINE_OFFSET, blof );
}
public static void setBottomRulerAlignment( MutableAttributeSet a, CoEnumValue v )
{
	set( a, BOTTOM_RULER_ALIGNMENT, v );
}
public static void setBottomRulerFixedWidth( MutableAttributeSet a, Float v )
{
	set( a, BOTTOM_RULER_FIXED_WIDTH, v );
}
public static void setBottomRulerLeftIndent( MutableAttributeSet a, Float v )
{
	set( a, BOTTOM_RULER_LEFT_INSET, v );
}
public static void setBottomRulerPosition( MutableAttributeSet a, Float v )
{
	set( a, BOTTOM_RULER_POSITION, v );
}
public static void setBottomRulerRightIndent( MutableAttributeSet a, Float v )
{
	set( a, BOTTOM_RULER_RIGHT_INSET, v );
}
public static void setBottomRulerSpan( MutableAttributeSet a, CoEnumValue v )
{
	set( a, BOTTOM_RULER_SPAN, v );
}
public static void setBottomRulerThickness( MutableAttributeSet a, Float v )
{
	set( a, BOTTOM_RULER_THICKNESS, v );
}
/**
 * Sets the icon attribute.
 *
 * @param a the attribute set
 * @param c the icon
 */
public static void setComment( MutableAttributeSet a, String c )
{
	a.addAttribute( AbstractDocument.ElementNameAttribute, CommentElementName );
	a.addAttribute( COMMENT, c );
}
public static void setDropCaps(MutableAttributeSet a, Boolean b)
{
	set( a, DROP_CAPS, b );
}
public static void setDropCapsCharacterCount(MutableAttributeSet a, Integer count)
{
	set( a, DROP_CAPS_COUNT, count );
}
public static void setDropCapsLineCount(MutableAttributeSet a, Integer count)
{
	set( a, DROP_CAPS_HEIGHT, count );
}
public static void setFirstLineIndent(MutableAttributeSet a, Float i)
{
	set( a, FIRST_LINE_INDENT, i );
}
public static void setFontFamily(MutableAttributeSet a, String fam)
{
	set( a, FONT_FAMILY, fam );
}
public static void setFontSize(MutableAttributeSet a, Float fontSize)
{
	set( a, FONT_SIZE, fontSize );
}
public static void setForegroundColor(MutableAttributeSet a, String c)
{
	set( a, FOREGROUND_COLOR, c );
}
public static void setForegroundShade(MutableAttributeSet a, Float s)
{
	set( a, FOREGROUND_SHADE, s );
}
public static void setHorizontalScale(MutableAttributeSet a, Float s)
{
	set( a, HORIZONTAL_SCALE, s );
}
public static void setHyphenation( MutableAttributeSet a, String name )
{
	set( a, HYPHENATION, name );
}
public static void setHyphenationFallbackBehavior(MutableAttributeSet a, CoEnumValue align)
{
	set( a, HYPHENATION_FALLBACK_BEHAVIOR, align );
}
public static void setKeepLinesTogether(MutableAttributeSet a, Boolean state)
{
	set( a, LINES_TOGETHER, state );
}
public static void setKeyStroke( MutableAttributeSet a, KeyStroke ks )
{
	set( a, KEY_STROKE, ks );
}
public static void setLastInColumn(MutableAttributeSet a, Boolean b)
{
	set( a, LAST_IN_COLUMN, b );
}

public static void setLeftIndent(MutableAttributeSet a, Float i)
{
	set( a, LEFT_INDENT, i );
}


public static void setRegularTabStopInterval(MutableAttributeSet a, Float i)
{
	set( a, REGULAR_TAB_STOP_INTERVAL, i );
}

public static void setRightIndent(MutableAttributeSet a, Float i)
{
	set( a, RIGHT_INDENT, i );
}
public static void setShadow(MutableAttributeSet a, Boolean state)
{
	set( a, SHADOW, state );
}
public static void setSpaceAbove(MutableAttributeSet a, Float i)
{
	set( a, SPACE_ABOVE, i );
}
public static void setSpaceBelow(MutableAttributeSet a, Float i)
{
	set( a, SPACE_BELOW, i );
}

public static void setStrikeThru(MutableAttributeSet a, Boolean state)
{
	set( a, STRIKE_THRU, state );
}
public static void setStyle(MutableAttributeSet a, com.bluebrim.font.shared.CoFontAttribute b)
{
	set( a, STYLE, b );
}
public static void setSuperior(MutableAttributeSet a, Boolean state)
{
	set( a, SUPERIOR, state );
}
public static void setTabSet( MutableAttributeSet a, CoTabSetIF s )
{
	set( a, TAB_SET, s );
}
public static void setTopOfColumn(MutableAttributeSet a, Boolean b)
{
	set( a, TOP_OF_COLUMN, b );
}
public static void setTopRulerAlignment( MutableAttributeSet a, CoEnumValue v )
{
	set( a, TOP_RULER_ALIGNMENT, v );
}
public static void setTopRulerFixedWidth( MutableAttributeSet a, Float v )
{
	set( a, TOP_RULER_FIXED_WIDTH, v );
}
public static void setTopRulerLeftIndent( MutableAttributeSet a, Float v )
{
	set( a, TOP_RULER_LEFT_INSET, v );
}
public static void setTopRulerPosition( MutableAttributeSet a, Float v )
{
	set( a, TOP_RULER_POSITION, v );
}
public static void setTopRulerRightIndent( MutableAttributeSet a, Float v )
{
	set( a, TOP_RULER_RIGHT_INSET, v );
}
public static void setTopRulerSpan( MutableAttributeSet a, CoEnumValue v )
{
	set( a, TOP_RULER_SPAN, v );
}
public static void setTopRulerThickness( MutableAttributeSet a, Float v )
{
	set( a, TOP_RULER_THICKNESS, v );
}
public static void setTrackAmount(MutableAttributeSet a, Float trackAmount)
{
	set( a, TRACK_AMOUNT, trackAmount );
}
public static void setTrailingLinesIndent(MutableAttributeSet a, Float i)
{
	set( a, TRAILING_LINES_INDENT, i );
}

public static void setUnderline( MutableAttributeSet a, CoEnumValue u )
{
	set( a, UNDERLINE, u );
}

public static void setWeight(MutableAttributeSet a, com.bluebrim.font.shared.CoFontAttribute b)
{
	set( a, WEIGHT, b );
}
public static void setVerticalPosition( MutableAttributeSet a, CoEnumValue u )
{
	set( a, VERTICAL_POSITION, u );
}
	public static void setVerticalScale(MutableAttributeSet a, Float s)
{
	set( a, VERTICAL_SCALE, s );
}

public static Float getMinimumSpaceWidth( AttributeSet a )
{
	return (Float) get( a, MINIMUM_SPACE_WIDTH, AS_IS_FLOAT_VALUE );
}

public static Float getOptimumSpaceWidth( AttributeSet a )
{
	return (Float) get( a, OPTIMUM_SPACE_WIDTH, AS_IS_FLOAT_VALUE );
}

public static Float getShadowAngle( AttributeSet a )
{
	return (Float) get( a, SHADOW_ANGLE, AS_IS_FLOAT_VALUE );
}

public static String getShadowColor(AttributeSet a)
{
	return (String) get( a, SHADOW_COLOR, AS_IS_STRING_VALUE );
}

public static Float getShadowOffset( AttributeSet a )
{
	return (Float) get( a, SHADOW_OFFSET, AS_IS_FLOAT_VALUE );
}

public static Float getShadowShade(AttributeSet a)
{
	return (Float) get( a, SHADOW_SHADE, AS_IS_FLOAT_VALUE );
}

public static void setMinimumSpaceWidth( MutableAttributeSet a, Float c )
{
	set( a, MINIMUM_SPACE_WIDTH, c );
}

public static void setOptimumSpaceWidth( MutableAttributeSet a, Float c )
{
	set( a, OPTIMUM_SPACE_WIDTH, c );
}

public static void setShadowAngle( MutableAttributeSet a, Float blof )
{
	set( a, SHADOW_ANGLE, blof );
}

public static void setShadowColor(MutableAttributeSet a, String c)
{
	set( a, SHADOW_COLOR, c );
}

public static void setShadowOffset( MutableAttributeSet a, Float o )
{
	set( a, SHADOW_OFFSET, o );
}

public static void setShadowShade(MutableAttributeSet a, Float s)
{
	set( a, SHADOW_SHADE, s );
}


/**
 * @author Markus Persson 2001-08-31
 */
public static void setStyle(MutableAttributeSet set, int style) {
	setStyle(set, com.bluebrim.font.shared.CoFontAttribute.getStyle(style));
}


/**
 * @author Markus Persson 2001-08-31
 */
public static void setWeight(MutableAttributeSet set, int weight) {
	setWeight(set, com.bluebrim.font.shared.CoFontAttribute.getWeight(weight));
}

  public static final CoLeading AS_IS_LEADING_VALUE = new CoLeading();

public static CoLeading getLeading(AttributeSet a)
{
	return (CoLeading) get( a, LEADING, AS_IS_LEADING_VALUE );
}

public static void setLeading(MutableAttributeSet a, CoLeading l)
{
	set( a, LEADING, l );
}
}