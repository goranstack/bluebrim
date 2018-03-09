package com.bluebrim.font.shared;
import java.io.*;
import java.util.*;

import javax.swing.text.*;

import com.bluebrim.base.shared.debug.*;
import com.bluebrim.collection.shared.*;
import com.bluebrim.font.impl.shared.*;

/**
 * CoFontAttribute is an object representation of com.bluebrim.font.shared.CoFontFaceSpec parameters.
 *
 * PENDING: Check serializability with Dennis. /Markus 2001-09-01
 *
 * NOTE: The axis state could be achived by a subclass per axis instead.
 * There are both upsides and downsides to doing so, why it has been left
 * the way it was for now. /Markus 2001-09-03
 *
 * @author Dennis
 * @author Markus Persson 2001-08-31
 */ 
public final class CoFontAttribute implements Serializable, Comparable {
	private final String m_key;
	private final int m_axis;
	private final int m_value;

	private static final CoIntMap NOT_AN_AXIS_MAP = new CoSimpleIntMap();
	private static final CoIntMap WEIGHT_MAP = new CoSimpleIntMap();
	private static final CoIntMap STYLE_MAP = new CoSimpleIntMap();
	private static final CoIntMap VARIANT_MAP = new CoSimpleIntMap();
	private static final CoIntMap STRETCH_MAP = new CoSimpleIntMap();
	private static final CoIntMap[] AXIS_MAPS = new CoIntMap[] {NOT_AN_AXIS_MAP, WEIGHT_MAP, STYLE_MAP, VARIANT_MAP, STRETCH_MAP};

	private static Map KEY_MAP = new HashMap();

	// Common
//	public static final CoFontAttribute NORMAL = create("NORMAL", com.bluebrim.font.shared.CoFontConstants.NOT_AN_AXIS, com.bluebrim.font.shared.CoFontConstants.NORMAL);
	public static final CoFontAttribute DEFAULT = create("DEFAULT", com.bluebrim.font.shared.CoFontConstants.NOT_AN_AXIS, com.bluebrim.font.shared.CoFontConstants.DEFAULT);
	public static final CoFontAttribute AS_IS = create("AS_IS", com.bluebrim.font.shared.CoFontConstants.NOT_AN_AXIS, com.bluebrim.font.shared.CoFontConstants.AS_IS);

	// Style
	public static final CoFontAttribute ROMAN = create("ROMAN", com.bluebrim.font.shared.CoFontConstants.STYLE_AXIS, com.bluebrim.font.shared.CoFontConstants.ROMAN);
	public static final CoFontAttribute ITALIC = create("ITALIC", com.bluebrim.font.shared.CoFontConstants.STYLE_AXIS, com.bluebrim.font.shared.CoFontConstants.ITALIC);
	public static final CoFontAttribute NORMAL_STYLE = ROMAN;

	// Variant
	public static final CoFontAttribute NORMAL_VARIANT = create("NORMAL_VARIANT", com.bluebrim.font.shared.CoFontConstants.VARIANT_AXIS, com.bluebrim.font.shared.CoFontConstants.NORMAL_VARIANT);
	public static final CoFontAttribute SMALL_CAPS = create("SMALL_CAPS", com.bluebrim.font.shared.CoFontConstants.VARIANT_AXIS, com.bluebrim.font.shared.CoFontConstants.SMALL_CAPS);

	// Weight
	public static final CoFontAttribute W100 = create("W100", com.bluebrim.font.shared.CoFontConstants.WEIGHT_AXIS, com.bluebrim.font.shared.CoFontConstants.W100);
	public static final CoFontAttribute W200 = create("W200", com.bluebrim.font.shared.CoFontConstants.WEIGHT_AXIS, com.bluebrim.font.shared.CoFontConstants.W200);
	public static final CoFontAttribute W300 = create("W300", com.bluebrim.font.shared.CoFontConstants.WEIGHT_AXIS, com.bluebrim.font.shared.CoFontConstants.W300);
	public static final CoFontAttribute W400 = create("W400", com.bluebrim.font.shared.CoFontConstants.WEIGHT_AXIS, com.bluebrim.font.shared.CoFontConstants.W400);
	public static final CoFontAttribute W500 = create("W500", com.bluebrim.font.shared.CoFontConstants.WEIGHT_AXIS, com.bluebrim.font.shared.CoFontConstants.W500);
	public static final CoFontAttribute W600 = create("W600", com.bluebrim.font.shared.CoFontConstants.WEIGHT_AXIS, com.bluebrim.font.shared.CoFontConstants.W600);
	public static final CoFontAttribute W700 = create("W700", com.bluebrim.font.shared.CoFontConstants.WEIGHT_AXIS, com.bluebrim.font.shared.CoFontConstants.W700);
	public static final CoFontAttribute W800 = create("W800", com.bluebrim.font.shared.CoFontConstants.WEIGHT_AXIS, com.bluebrim.font.shared.CoFontConstants.W800);
	public static final CoFontAttribute W900 = create("W900", com.bluebrim.font.shared.CoFontConstants.WEIGHT_AXIS, com.bluebrim.font.shared.CoFontConstants.W900);


	// Convenience names. Note that this is more of a suggestion of reasonable values for e.g. a
	// semibold font, than a normative regulation. /Ihse
	// (Actually NORMAL and BOLD are CSS2 alternative names for weight, which may or may not
	// coincide with the name assigned by the font maker/vendor. /Markus)
	public static final CoFontAttribute THIN = W100;
	public static final CoFontAttribute EXTRALIGHT = W200;
	public static final CoFontAttribute ULTRALIGHT = W200;
	public static final CoFontAttribute LIGHT = W300;
	public static final CoFontAttribute NORMAL_WEIGHT = W400;
	public static final CoFontAttribute MEDIUM = W500;
	public static final CoFontAttribute BOOK = W500;
	public static final CoFontAttribute SEMIBOLD = W600;
	public static final CoFontAttribute DEMIBOLD = W600;
	public static final CoFontAttribute BOLD = W700;
	public static final CoFontAttribute ULTRABOLD = W800;
	public static final CoFontAttribute EXTRABOLD = W800;
	public static final CoFontAttribute BLACK = W900;
	public static final CoFontAttribute HEAVY = W900;

	// Stretch
	public static final CoFontAttribute S100 = create("ULTRA_CONDENSED", com.bluebrim.font.shared.CoFontConstants.STRETCH_AXIS, com.bluebrim.font.shared.CoFontConstants.S100);
	public static final CoFontAttribute S200 = create("EXTRA_CONDENSED", com.bluebrim.font.shared.CoFontConstants.STRETCH_AXIS, com.bluebrim.font.shared.CoFontConstants.S200);
	public static final CoFontAttribute S300 = create("CONDENSED", com.bluebrim.font.shared.CoFontConstants.STRETCH_AXIS, com.bluebrim.font.shared.CoFontConstants.S300);
	public static final CoFontAttribute S400 = create("SEMI_CONDENSED", com.bluebrim.font.shared.CoFontConstants.STRETCH_AXIS, com.bluebrim.font.shared.CoFontConstants.S400);
	public static final CoFontAttribute S500 = create("NORMAL_STRETCH", com.bluebrim.font.shared.CoFontConstants.STRETCH_AXIS, com.bluebrim.font.shared.CoFontConstants.S500);
	public static final CoFontAttribute S600 = create("SEMI_EXPANDED", com.bluebrim.font.shared.CoFontConstants.STRETCH_AXIS, com.bluebrim.font.shared.CoFontConstants.S600);
	public static final CoFontAttribute S700 = create("EXPANDED", com.bluebrim.font.shared.CoFontConstants.STRETCH_AXIS, com.bluebrim.font.shared.CoFontConstants.S700);
	public static final CoFontAttribute S800 = create("EXTRA_EXPANDED", com.bluebrim.font.shared.CoFontConstants.STRETCH_AXIS, com.bluebrim.font.shared.CoFontConstants.S800);
	public static final CoFontAttribute S900 = create("ULTRA_EXPANDED", com.bluebrim.font.shared.CoFontConstants.STRETCH_AXIS, com.bluebrim.font.shared.CoFontConstants.S900);

	// Convenience names. Note that this is more of a suggestion of reasonable values
	// than a normative regulation. /Ihse
	// (Actually these are the CSS2 assigned names for stretch, which may or may not
	// coincide with the name assigned by the font maker/vendor. /Markus)
	public static final CoFontAttribute ULTRA_CONDENSED = S100;
	public static final CoFontAttribute EXTRA_CONDENSED = S200;
	public static final CoFontAttribute CONDENSED = S300;
	public static final CoFontAttribute SEMI_CONDENSED = S400;
	public static final CoFontAttribute NORMAL_STRETCH = S500;
	public static final CoFontAttribute SEMI_EXPANDED = S600;
	public static final CoFontAttribute EXPANDED = S700;
	public static final CoFontAttribute EXTRA_EXPANDED = S800;
	public static final CoFontAttribute ULTRA_EXPANDED = S900;
	
	// List of possible values for each axis.
	public static final CoFontAttribute[] STYLE_OPTIONS = new CoFontAttribute[] {
		NORMAL_STYLE, ITALIC };

	public static final CoFontAttribute[] VARIANT_OPTIONS = new CoFontAttribute[] {
		NORMAL_VARIANT, SMALL_CAPS };

	public static final CoFontAttribute[] WEIGHT_OPTIONS = new CoFontAttribute[] {
		W100, W200, W300, W400, W500, W600, W700, W800, W900 };

	public static final CoFontAttribute[] STRETCH_OPTIONS = new CoFontAttribute[] {
		ULTRA_CONDENSED, EXTRA_CONDENSED, CONDENSED, SEMI_CONDENSED, NORMAL_STRETCH,
		SEMI_EXPANDED, EXPANDED, EXTRA_EXPANDED, ULTRA_EXPANDED };

	// Index to the default, normal, attribute of each *_OPTION array.
	public static final int STYLE_OPTIONS_DEFAULT = 0;
	public static final int VARIANT_OPTIONS_DEFAULT = 0;
	public static final int WEIGHT_OPTIONS_DEFAULT = 3;
	public static final int STRETCH_OPTIONS_DEFAULT = 4;


/**
 * NOTE: As all other Comparable implementations, this throws
 * a NullPointerException if the argument is null and
 * a ClassCastException if it is of the wrong type. Do NOT
 * change this! Change where this is wrongly used instead.
 * @author Markus Persson 2001-08-31
 */
public int compareTo(Object other) {
	return compare(m_value, ((CoFontAttribute) other).m_value);
}
public boolean equals(Object other) {
	return (other instanceof CoFontAttribute) && equals((CoFontAttribute) other);
}
public String getKey() {
	return m_key;
}
public String getName() {
	return CoFontResources.getName(m_key);
}

public int getValue() {
	return m_value;
}
public int hashCode() {
	return m_value;
}
public String toString() {
	return getName();
}


private CoFontAttribute(String key, int axis, int value) {
	m_key = key;
	m_axis = axis;
	m_value = value;
}


private static final int compare(int first, int second) {
	return (first < second ? -1 : (first == second ? 0 : 1));
}


/**
 * NOTE: As all other Comparable implementations, this throws
 * a NullPointerException if the argument is null. Do NOT
 * change this! Change where this is wrongly used instead.
 * @author Markus Persson 2001-08-31
 */
public int compareTo(CoFontAttribute other) {
	return compare(m_value, other.m_value);
}


private static CoFontAttribute create(String key, int axis, int value) {
	CoAssertion.notNull(key, "key");

	CoFontAttribute attr = new CoFontAttribute(key, axis, value);
	StyleContext.registerStaticAttributeKey(attr);
	Object oldAttr = AXIS_MAPS[axis].put(value, attr);
	CoAssertion.assertTrue(oldAttr == null, "Conflicting CoFontAttributes defined: " + attr + " and " + oldAttr);
	
	oldAttr = KEY_MAP.put(key, attr);
	CoAssertion.assertTrue(oldAttr == null, "CoFontAttributes with conflicting keys defined: " + attr + " and " + oldAttr);

	return attr;
}

public boolean equals(CoFontAttribute other) {
	// NOTE: Ignore legacy key here! /Markus
	return (m_axis == other.m_axis) && (m_value == other.m_value);
}


public static CoFontAttribute get(int axis, int value) {
	return (CoFontAttribute) AXIS_MAPS[axis].get(value);
}


public static CoFontAttribute getByKey(String key) {
	CoAssertion.notNull(key, "key");	
	return (CoFontAttribute) KEY_MAP.get(key);
}


public static CoFontAttribute getStretch(int value) {
	return (CoFontAttribute) STRETCH_MAP.get(value);
}


public static CoFontAttribute getStyle(int value) {
	return (CoFontAttribute) STYLE_MAP.get(value);
}


public static CoFontAttribute getVariant(int value) {
	return (CoFontAttribute) VARIANT_MAP.get(value);
}


public static CoFontAttribute getWeight(int value) {
	return (CoFontAttribute) WEIGHT_MAP.get(value);
}


/**
 * Canonicalize on deserialization.
 *
 * This is not neccessary but mimics earlier implementation,
 * albeit more complete now. Reduces instances used but does
 * not guarantee true canonicalization of Java objects, due
 * to GemStone session-differentiated object views, transient
 * lifecycle limitations and threading issues. If not for the
 * former, resolving the two latter would achive Java object
 * and not only persistent object canonicalization as it now
 * would.
 *
 * @see #get(int, int)
 * @author Markus Persson 2001-08-31
 */
private Object readResolve() throws ObjectStreamException {
	try {
		CoFontAttribute cached = get(m_axis, m_value);
		if (cached != null) {
			return cached;
		} else {
			System.err.println("Unexpected cache miss deserializing CoFontAttribute with axis=" + m_axis + ", value=" + m_value + ".");
			AXIS_MAPS[m_axis].put(m_value, this);
			return this;
		}
	} catch (Exception e) {
		// Something failed, probably due to axis out of range.
		// This should not happen unless there is some version
		// mismatch between the serializing and deserializing ends.
		// One could argue that the exception should be narrowed
		// down to an ArrayIndexOutOfBoundsException, but since
		// this canonicalization isn't neccesary, any failure can
		// be ignored by simply returning this. However, for
		// completeness an error message will also be displayed.
		System.err.println("Unexpected canonicalization failure for CoFontAttribute with axis=" + m_axis + ", value=" + m_value + ".");
		return this;
	}
}
}