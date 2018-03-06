package com.bluebrim.text.impl.shared;

import java.util.*;

import com.bluebrim.text.shared.*;

/**
 * Abstract class for translating text attributes and their values to and from a textual form.
 * The translation of the attribute name is called tag (not to be confused with text character and paragaph tags).
 * Each subclass handles values of a specific type (string, number, boolean, ...)
 *
 * @author: Dennis Malmström
 */
 
public abstract class CoAttributeTranslator implements CoTextConstants
{
	public static final String COMMENT_TAG =      "comment";
	
	private Object m_attribute;
	private String m_tag;


	// all attribute translations
	private static final Hashtable m_attributeToTranslatorMap = new Hashtable();
	private static final Hashtable m_tagToTranslatorMap = new Hashtable();
	
	static
	{
		// PENDING: some are probable missing
		/*
		TAB_SET ???
		*/
		new EnumAttributeTranslator(    HYPHENATION_FALLBACK_BEHAVIOR,  "hfb" );
		new FloatAttributeTranslator(   OPTIMUM_SPACE_WIDTH,            "osw" );
		new FloatAttributeTranslator(   MINIMUM_SPACE_WIDTH,            "msw" );
		new BooleanAttributeTranslator( ADJUST_TO_BASE_LINE_GRID,       "blg" );
		new EnumAttributeTranslator(    ALIGNMENT,                      "align" );
		new BooleanAttributeTranslator( ALL_CAPS,                       "ac" );
		new FloatAttributeTranslator(   BASELINE_OFFSET,                "blo" );
		new FontAttributeTranslator(    STYLE,                          "s" );
		new FontAttributeTranslator(    WEIGHT,                         "w" );
		new EnumAttributeTranslator(    BOTTOM_RULER_ALIGNMENT,         "bra" );
		new FloatAttributeTranslator(   BOTTOM_RULER_FIXED_WIDTH,       "brfw" );
		new FloatAttributeTranslator(   BOTTOM_RULER_LEFT_INSET,        "brli" );
		new FloatAttributeTranslator(   BOTTOM_RULER_POSITION,          "brp" );
		new FloatAttributeTranslator(   BOTTOM_RULER_RIGHT_INSET,       "brri" );
		new FloatAttributeTranslator(   BOTTOM_RULER_SPAN,              "brs" );
		new EnumAttributeTranslator(    BOTTOM_RULER_THICKNESS,         "brt" );
		new BooleanAttributeTranslator( DROP_CAPS,                      "dc" );
		new IntegerAttributeTranslator( DROP_CAPS_COUNT,                "dccc" );
		new IntegerAttributeTranslator( DROP_CAPS_HEIGHT,               "dclc" );
		new FloatAttributeTranslator(   FIRST_LINE_INDENT,              "fli" );
		new StringAttributeTranslator(  FONT_FAMILY,                    "ff" );
		new FloatAttributeTranslator(   FONT_SIZE,                      "fs" );
		new ColorAttributeTranslator(   FOREGROUND_COLOR,               "fgc" );
		new FloatAttributeTranslator(   FOREGROUND_SHADE,               "fgs" );
		new FloatAttributeTranslator(   HORIZONTAL_SCALE,               "hsc" );
		new BooleanAttributeTranslator( LINES_TOGETHER,                 "klt" );
		new BooleanAttributeTranslator( LAST_IN_COLUMN,                 "lic" );
		new LeadingAttributeTranslator(   LEADING,                        "ld" );
		new FloatAttributeTranslator(   LEFT_INDENT,                    "li" );
		new StringAttributeTranslator(  HYPHENATION,                    "h" );
		new FloatAttributeTranslator(   REGULAR_TAB_STOP_INTERVAL,      "rtsi" );
		new FloatAttributeTranslator(   RIGHT_INDENT,                   "ri" );
		new BooleanAttributeTranslator( SHADOW,                         "sh" );
		new FloatAttributeTranslator(   SPACE_ABOVE,                    "sa" );
		new FloatAttributeTranslator(   SPACE_BELOW,                    "sb" );
		new BooleanAttributeTranslator( STRIKE_THRU,                    "st" );
		new BooleanAttributeTranslator( SUPERIOR,                       "su" );
		new BooleanAttributeTranslator( TOP_OF_COLUMN,                  "tc" );
		new EnumAttributeTranslator(    TOP_RULER_ALIGNMENT,            "tra" );
		new FloatAttributeTranslator(   TOP_RULER_FIXED_WIDTH,          "trfw" );
		new FloatAttributeTranslator(   TOP_RULER_LEFT_INSET,           "trli" );
		new FloatAttributeTranslator(   TOP_RULER_POSITION,             "trp" );
		new FloatAttributeTranslator(   TOP_RULER_RIGHT_INSET,          "trri" );
		new FloatAttributeTranslator(   TOP_RULER_SPAN,                 "trs" );
		new EnumAttributeTranslator(    TOP_RULER_THICKNESS,            "trt" );
		new FloatAttributeTranslator(   TRACK_AMOUNT,                   "ta" );
		new FloatAttributeTranslator(   TRAILING_LINES_INDENT,          "tli" );
		new EnumAttributeTranslator(    UNDERLINE,                      "u" );
		new EnumAttributeTranslator(    VERTICAL_POSITION,              "vp" );
		new FloatAttributeTranslator(   VERTICAL_SCALE,                 "vsc" );
		new StringAttributeTranslator(  MACRO,                          "macro" );
		new FloatAttributeTranslator(   SHADOW_OFFSET,                  "sho" );
		new FloatAttributeTranslator(   SHADOW_ANGLE,                   "sha" );
		new ColorAttributeTranslator(   SHADOW_COLOR,                   "shc" );
		new FloatAttributeTranslator(   SHADOW_SHADE,                   "shs" );
		new StringAttributeTranslator(  PARAGRAPH_TAG,					"pt"  );
	}


	
	// Translator for boolan attributes.
	private static class BooleanAttributeTranslator extends CoAttributeTranslator
	{
		private BooleanAttributeTranslator( Object attribute, String tag ) { super( attribute, tag ); }
		
		public String value2string( Object value )
		{
			return ( (Boolean) value ).toString();
		}
		
		public Object string2value( String tagValue )
		{
			return Boolean.valueOf( tagValue ).booleanValue() ? Boolean.TRUE : Boolean.FALSE;
		}
	}


	
	// Translator for integer attributes.
	private static class IntegerAttributeTranslator extends CoAttributeTranslator
	{
		private IntegerAttributeTranslator( Object attribute, String tag ) { super( attribute, tag ); }
		
		public String value2string( Object value )
		{
			return ( (Integer) value ).toString();
		}
		
		public Object string2value( String tagValue )
		{
			return Integer.valueOf( tagValue );
		}
	}


	
	// Translator for float attributes.
	private static class FloatAttributeTranslator extends CoAttributeTranslator
	{
		private FloatAttributeTranslator( Object attribute, String tag ) { super( attribute, tag ); }
		
		public String value2string( Object value )
		{
			return ( (Float) value ).toString();
		}
		
		public Object string2value( String tagValue )
		{
			return Float.valueOf( tagValue );
		}
	}


	
	// Translator for string attributes.
	private static class StringAttributeTranslator extends CoAttributeTranslator
	{
		private StringAttributeTranslator( Object attribute, String tag ) { super( attribute, tag ); }
		
		public String value2string( Object value )
		{
			return ( String ) value;
		}
		
		public Object string2value( String tagValue )
		{
			return tagValue;
		}
	}


	
	// Translator for color attributes.
	private static class ColorAttributeTranslator extends CoAttributeTranslator
	{
		private ColorAttributeTranslator( Object attribute, String tag ) { super( attribute, tag ); }
		
		public String value2string( Object value )
		{	
			return (String) value;
		}
		
		public Object string2value( String tagValue )
		{
			return tagValue;
		}
	}


	
	// Translator for CoEnumValue attributes.
	private static class EnumAttributeTranslator extends CoAttributeTranslator
	{
		private EnumAttributeTranslator( Object attribute, String tag ) { super( attribute, tag ); }
		
		public String value2string( Object value )
		{
			return ( (CoEnumValue) value ).toString();
		}
		
		public Object string2value( String tagValue )
		{
			return CoEnumValue.getEnumValue( tagValue );
		}
	}


	// Translator for com.bluebrim.font.shared.CoFontAttribute attributes.
	private static class FontAttributeTranslator extends CoAttributeTranslator
	{
		private FontAttributeTranslator( Object attribute, String tag ) { super( attribute, tag ); }
		
		public String value2string( Object value )
		{
			return ( (com.bluebrim.font.shared.CoFontAttribute) value ).getKey();
		}
		
		public Object string2value( String tagValue )
		{
			return com.bluebrim.font.shared.CoFontAttribute.getByKey( tagValue );
		}
	}




protected CoAttributeTranslator( Object attribute, String tag )
{
	// make sure each tag is unique
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( m_tagToTranslatorMap.get( tag ) == null, "Tag \"" + tag + "\" used twice in CoAttributeTranslator" );

	m_attribute = attribute;
	m_tag = tag;

	// register this translation
	m_attributeToTranslatorMap.put( m_attribute, this );
	m_tagToTranslatorMap.put( m_tag, this );
}
public Object getAttribute()
{
	return m_attribute;
}
public String getTag()
{
	return m_tag;
}
public static CoAttributeTranslator getTranslator( Object attribute )
{
	return (CoAttributeTranslator) m_attributeToTranslatorMap.get( attribute );
}
public static CoAttributeTranslator getTranslator( String tagName )
{
	return (CoAttributeTranslator) m_tagToTranslatorMap.get( tagName );
}
// string representaion -> value

public abstract Object string2value( String str );
// value -> string representaion

public abstract String value2string( Object value );

	// Translator for leading attributes.
	private static class LeadingAttributeTranslator extends CoAttributeTranslator
	{
		private LeadingAttributeTranslator( Object attribute, String tag ) { super( attribute, tag ); }
		
		public String value2string( Object value )
		{
			return CoLeading.format( (CoLeading) value );
		}
		
		public Object string2value( String tagValue )
		{
			return CoLeading.parse( tagValue );
		}
	}
}