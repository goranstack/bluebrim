package com.bluebrim.font.shared;
import java.awt.font.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.debug.*;
import com.bluebrim.font.impl.shared.metrics.*;

/**
 * Font instance of a font face at a specific size and scale.
 *
 * <p>Note! Normally, a font is specified by its size in points. However, it is also possible to scale 
 * a font by changing the scale value. However, a 12 point font at 2.0x2.0 scale is <b>not</b> 
 * the same as that font at 24 point. Values such as tracking is calculated solely on basis of the font size,
 * and not the scale factors.
 *
 * <p><b>Documentation last updated:</b> 2001-09-26
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se) (Redesigned and completely rewritten, 2001-04-23).
 *
 * @see com.bluebrim.font.shared.CoFontFace
 * @see com.bluebrim.font.shared.CoFontFaceSpec
 */
public class CoFont implements Serializable, com.bluebrim.xml.shared.CoXmlEnabledIF {
	public static final String XML_TAG = "font";
	public static final String XML_FONT_SIZE = "size";
	public static final String XML_X_SCALE = "x-scale";
	public static final String XML_Y_SCALE = "y-scale";

	// The variables that uniquely describes this font
	private com.bluebrim.font.shared.CoFontFace m_face;
	private float m_fontSize;
	private float m_xScale;
	private float m_yScale;

	// Lazy initialized data for this font
	private transient com.bluebrim.font.shared.CoAwtFont m_awtFont;
	private transient com.bluebrim.font.shared.metrics.CoLineMetrics m_lineMetrics;

	private float m_emSpace; // calculated emspace, should be transient?
	private float m_tracking; // calculated tracking, should be transient?
/**
 * Return the advance for the glyph corresponding to the given Unicode char. The advance is the width of the character,
 * or more precisely, the length to the right where the next character should be placed. Zero width advancing characters
 * do exist. The advance is given in points (1/72").
 *
 * @param ch the character for which to get the advance width, in points.
 *
 * @return the advance for that character.
 */
public float getAdvance(char ch) {
	// FIXME: is this really scaled to correct unit?
	
	return m_face.getMetrics().getAdvance(ch) * m_fontSize * m_xScale;
}

/**
 * Returns the AWT Font that is represented by this CoFont. This font, when drawn by the AWT, will <i>hopefully</i>
 * render this CoFont on the screen. <p>
 *
 * Why hopefully? Since it's impossible to exactly specify a specific font file
 * to use with AWT (at least with Type 1 fonts), the font can only be specified by its name. If two or more fonts
 * in AWT's font path share the same name, no guarantee can be given as to which AWT is using. (However, simple
 * testing shows that AWT tend to prefer a TrueType version of "Arial" before a Type 1 "Arial".) Hopefully, this
 * will change in future versions of AWT. <p>
 *
 * The method returns a <code>com.bluebrim.font.impl.shared.CoAwtFont</code>, which is a subclass of java.awt.Font, capable of returning back
 * this <code>CoFont</code>.
 *
 * @return a com.bluebrim.font.impl.shared.CoAwtFont representing this CoFont.
 */
public com.bluebrim.font.shared.CoAwtFont getAwtFont() {
	// Create and cache if not already done
	if (m_awtFont == null) {
		m_awtFont = com.bluebrim.font.shared.CoAwtFont.createAwtFont(this, getAwtAttributes());
	}

	return m_awtFont;
}

/**
 * Returns the em space for this font (i.e. the font face at this size). This is, by definition, the em space of the
 * font face, scaled to the font size this particular font has. <p>
 * <b>Warning!</b> The definition of the em space is vague. For a discussion of the actual meaning of the em space
 * measurement, and how it is determined in Calvin, see {@link com.bluebrim.font.shared.CoFontFace#getEmSpace}.<p>
 *
 * The em space width is returned in points (i.e. 1/72 inch).
 *
 * @return the em space for this font in points.
 *
 * @see com.bluebrim.font.shared.CoFontFace#getEmSpace
 */
public float getEmSpace() {
	return m_emSpace;
}

/**
 * Return the pair kerning delta for the two glyphs corresponding to the given Unicode chars, for this font. 
 * The kerning value is given as points (1/72 inch). For a description of pair kerning, see {@link com.bluebrim.font.shared.metrics.CoPairKerningMetrics}.
 *
 * @param ch1 The unicode character for the first glyph.
 * @param ch2 The unicode character for the second glyph.
 *
 * @return The pair kerning value for these two characters.
 */

public float pairKern(char ch1, char ch2) {
	// FIXME: what kind of units is tracking in? will this really work?

	// emSpace is already scaled with xScale, otherwise it would have to been scaled here
	float value = m_face.getMetrics().getPairKerning(ch1, ch2) * getEmSpace();
//	System.out.println(this + " " + ch1 + "," + ch2 + " " + value);
	return value;
}

public String toString() {
	return m_face + " " + m_fontSize;
}

/**
 * Returns the line metrics for this font. This is the line metrics of this font face, scaled to this specific font size,
 * meaning the <code>com.bluebrim.font.shared.metrics.CoLineMetrics</code> object returns correct values in points for this font size.
 *
 * @return a com.bluebrim.font.shared.metrics.CoLineMetrics scaled to this font size and scale factor.
 */
public com.bluebrim.font.shared.metrics.CoLineMetrics getLineMetrics() {
	// Calculate and cache if not already done...
	if (m_lineMetrics == null) {
		// FIXME: is this the right scaling? what unit is used?
		m_lineMetrics = new CoLineMetricsImplementation(m_face.getMetrics().getLineMetrics(), m_fontSize * m_yScale);
	}

	return m_lineMetrics;
}

/**
 * Preferred CoFont constructor without scaling. Derives a font at a specific size from a font face.
 * Scaling should not be used unless really necessary, so this is the preferred way of instantiating
 * a CoFont.<p>
 *
 * Note! This constructor should not be called explicitely, instead <code>getFont</code> in
 * <code>com.bluebrim.font.shared.CoFontFace</code> should be used.
 *
 * @param face the font face to derive a font from
 * @param fontSize the font size in points
 *
 * @see com.bluebrim.font.shared.CoFontFace#getFont(float)
 */
protected CoFont(com.bluebrim.font.shared.CoFontFace face, float fontSize) {
	this(face, fontSize, 1.0f, 1.0f);
}


/**
 * Returns the track metrics for this font (i.e. this font face at this size). Normally, <code>calculateTracking</code>
 * will be used. The tracking is returned as points (or fractions of points), i.e. 1/72 inch.
 *
 * @return the tracking for this font, in points.
 *
 * @see #calculateTracking(float)
 */
public float getTracking() {
	return m_tracking;
}

/**
 * CoFont constructor with two scale factors for x and y. Derives a font at a specific size from a font face, and
 * with the specified scaling. This should only be used if scaling really is needed.<p>
 *
 * Note! This constructor should not be called explicitely, instead <code>getFont</code> in
 * <code>com.bluebrim.font.shared.CoFontFace</code> should be used.
 *
 * @param face the font face to derive a font from
 * @param fontSize the font size in points
 * @param xScale the width scale of the font
 * @param yScale the height scale of the font
 *
 * @see com.bluebrim.font.shared.CoFontFace#getFont(float,float,float)
 */
protected CoFont(com.bluebrim.font.shared.CoFontFace face, float fontSize, float xScale, float yScale) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(face, "face");
	m_face = face;
	m_fontSize = fontSize;
	m_xScale = xScale;
	m_yScale = yScale;
	updateValues();
}

/**
 * Calculate the resulting tracking, that is the sum of the track kerning metrics and a user specified addition.
 * The tracking is calculated as the sum of the actual track kerning metrics for this font face at this font's
 * font size, and the extra tracking as specified by the parameter. This allows the user to condense or extract
 * a font spacing by a specified value. Use {@link #getTracking} to get track kerning only.<p>
 *
 * <b>Note!</b> The tracking value is specified as 1/200 points (i.e. 1/(72*200) inch), both in the extraTracking 
 * parameter, and in the return value.
 *
 * @param extraTracking The additional tracking specified by the user, in 1/200 points.
 *
 * @return the resulting tracking, in 1/200 points.
 *
 * @see #getTracking
 */
public float calculateTracking(float extraTracking) {
	// FIXME: what kind of units is tracking in? will this really work?
	// Why the calculation with emSpace? Shouldn't that been done already?

	// emSpace is already scaled with xScale, otherwise it would have to been scaled here
	return (float) (extraTracking + getTracking()) * getEmSpace() / 200.0f;
}

/**
 * Create attributes for the AWT font creation. This is needed to pass on to AWT to create an AWT font instance
 * representing this <code>CoFont</code>. <p>
 *
 * In future versions of AWT, hopefully it will become possible to specify a font to AWT by giving an InputStream
 * containing the font. (Today it's possible, but only for TrueType, not Type 1.) If/when that is the case, all
 * this messing around with Attributes will hopefully become obsolete.
 *
 * @return a AWT attribute map
 */
protected Map getAwtAttributes() {
	// Start with attributes provided by our font face
	Map m = new HashMap(m_face.getAwtData().getAwtAttributes());

	// Add our size ...
	m.put(TextAttribute.SIZE, new Float(m_fontSize));

	// ... and our scaling, if any
	if (m_xScale != 1.0f || m_yScale != 1.0f) {
		AffineTransform transform = AffineTransform.getScaleInstance(m_xScale, m_yScale);
		m.put(TextAttribute.TRANSFORM, transform);
	}

	return m;
}

/**
 * Returns the font size of this font.
 *
 * @return the font size of this font.
 */
public float getFontSize() {
	return m_fontSize;
}


/**
 * Returns the font face that this font is derived from.
 *
 * @return the font face that this font is derived from.
 */
public com.bluebrim.font.shared.CoFontFace getFace() {
	return m_face;
}


public boolean equals(Object o) {
	if (!(o instanceof CoFont)) return false;
	if (o == this) return true;

	CoFont other = (CoFont)o;

	return (other.m_fontSize==m_fontSize &&
		other.m_face.equals(m_face) &&
		other.m_xScale==m_xScale &&
		other.m_yScale==m_yScale);
}


/**
 * Update calculated values after non-transient data has been changed.
 */
private void updateValues() {
	// FIXME: what unit is this value in? should it be scaled?
	m_tracking = m_face.getMetrics().getTracking(m_fontSize) * m_xScale;

	// FIXME: what unit is this value in? should it be scaled?
	m_emSpace = m_face.getEmSpace() * m_fontSize * m_xScale;
}





public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	if (subModel instanceof Number) {
		if (XML_FONT_SIZE.equals(parameter)) {
			m_fontSize = ((Number) subModel).floatValue();
		} else if (XML_X_SCALE.equals(parameter)) {
			m_xScale = ((Number) subModel).floatValue();
		} else if (XML_Y_SCALE.equals(parameter)) {
			m_yScale = ((Number) subModel).floatValue();
		}
	} else if (subModel instanceof com.bluebrim.font.shared.CoFontFace) {
		m_face = (com.bluebrim.font.shared.CoFontFace) subModel;
	}

	// Otherwise, ignore for compatibility
}


public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	updateValues();
}


/**
 * Private, empty constructor. This constructor should only be used while reading in XML.
 */
private CoFont() {
}


/**
 * Called before XML import. See {@link com.bluebrim.xml.shared.CoXmlImportEnabledIF} for details.
 */
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	return new CoFont();
}


public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
	visitor.export(XML_FONT_SIZE, new Float(m_fontSize));
	visitor.export(XML_X_SCALE, new Float(m_xScale));
	visitor.export(XML_Y_SCALE, new Float(m_yScale));
	visitor.export(m_face);
}
}