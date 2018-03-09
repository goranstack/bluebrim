package com.bluebrim.font.impl.shared.metrics;

import java.io.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.debug.*;

/**
 * Generic implementation of the com.bluebrim.font.shared.metrics.CoLineMetrics interface.
 *
 * Creation date: (2001-08-28 16:39:51)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */

public class CoLineMetricsImplementation implements com.bluebrim.font.shared.metrics.CoLineMetrics, Serializable {
	private float m_ascent;
	private float m_descent;
	private float m_height;
	private float m_leading;
	private float m_strikethroughOffset;
	private float m_strikethroughThickness;
	private float m_underlineOffset;
	private float m_underlineThickness;
	private float m_xHeight;
	private float m_capHeight;
public float getAscent()
{
	return m_ascent;
}

public float getDescent()
{
	return m_descent;
}

public float getHeight()
{
	return m_height;
}

public float getLeading()
{
	return m_leading;
}

public float getStrikethroughOffset()
{
	return m_strikethroughOffset;
}

public float getStrikethroughThickness()
{
	return m_strikethroughThickness;
}

public float getUnderlineOffset()
{
	return m_underlineOffset;
}

public float getUnderlineThickness()
{
	return m_underlineThickness;
}


public CoLineMetricsImplementation(float ascent, float descent, float height, float leading, float strikethroughOffset, 
	float strikethroughThickness, float underlineOffset, float underlineThickness, float xHeight, float capHeight) {
		
	initialize(ascent, descent, height, leading, strikethroughOffset, strikethroughThickness, underlineOffset, underlineThickness, xHeight, capHeight);
}


public CoLineMetricsImplementation(com.bluebrim.font.shared.metrics.CoLineMetrics m, float scale) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(m, "m");
	
	initialize(m.getAscent() * scale,
			   m.getDescent() * scale,
			   m.getHeight() * scale,
			   m.getLeading() * scale,
			   m.getStrikethroughOffset() * scale,
			   m.getStrikethroughThickness() * scale,
			   m.getUnderlineOffset() * scale,
			   m.getUnderlineThickness() * scale,
			   m.getXHeight() * scale,
			   m.getCapHeight() * scale);
}


/**
 * Returns the "Cap height", i.e. the heigh of a normal <b>upper case</b> capital letter in the font,
 * typically "H".
 * Creation date: (2001-05-10 13:30:16)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */

public float getCapHeight() {
	return m_capHeight;
}


/**
 * Returns the "x height", i.e. the heigh of a normal <b>lower case</b> letter without ascenders in the font,
 * typically "x".
 * Creation date: (2001-05-10 13:30:16)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */

public float getXHeight() {
	return m_xHeight;
}


private void initialize(float ascent, float descent, float height, float leading, float strikethroughOffset, 
	float strikethroughThickness, float underlineOffset, float underlineThickness, float xHeight, float capHeight) {
	m_ascent = ascent;
	m_descent = descent;
	m_height = height;
	m_leading = leading;
	m_strikethroughOffset = strikethroughOffset;
	m_strikethroughThickness = strikethroughThickness;
	m_underlineOffset = underlineOffset;
	m_underlineThickness = underlineThickness;
	m_xHeight = xHeight;
	m_capHeight = capHeight;
}





public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
	visitor.export(XML_ASCENT, new Float(m_ascent));
	visitor.export(XML_DESCENT, new Float(m_descent));
	visitor.export(XML_HEIGHT, new Float(m_height));
	visitor.export(XML_LEADING, new Float(m_leading));
	visitor.export(XML_STRIKETHROUGH_OFFSET, new Float(m_strikethroughOffset));
	visitor.export(XML_STRIKETHROUGH_THICKNESS, new Float(m_strikethroughThickness));
	visitor.export(XML_UNDERLINE_OFFSET, new Float(m_underlineOffset));
	visitor.export(XML_UNDERLINE_THICKNESS, new Float(m_underlineThickness));
	visitor.export(XML_X_HEIGHT, new Float(m_xHeight));
	visitor.export(XML_CAP_HEIGHT, new Float(m_capHeight));
}


public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	if (subModel instanceof Number) {
		if (XML_ASCENT.equals(parameter)) {
			m_ascent = ((Number) subModel).floatValue();
		} else if (XML_DESCENT.equals(parameter)) {
			m_descent = ((Number) subModel).floatValue();
		} else if (XML_HEIGHT.equals(parameter)) {
			m_height = ((Number) subModel).floatValue();
		} else if (XML_LEADING.equals(parameter)) {
			m_leading = ((Number) subModel).floatValue();
		} else if (XML_STRIKETHROUGH_OFFSET.equals(parameter)) {
			m_strikethroughOffset = ((Number) subModel).floatValue();
		} else if (XML_STRIKETHROUGH_THICKNESS.equals(parameter)) {
			m_strikethroughThickness = ((Number) subModel).floatValue();
		} else if (XML_UNDERLINE_OFFSET.equals(parameter)) {
			m_underlineOffset = ((Number) subModel).floatValue();
		} else if (XML_UNDERLINE_THICKNESS.equals(parameter)) {
			m_underlineThickness = ((Number) subModel).floatValue();
		} else if (XML_X_HEIGHT.equals(parameter)) {
			m_xHeight = ((Number) subModel).floatValue();
		} else if (XML_CAP_HEIGHT.equals(parameter)) {
			m_capHeight = ((Number) subModel).floatValue();
		} 
	} 

	// Otherwise, ignore for compatibility
}


public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	// Empty by design
}


/**
 * This constructor must only be called from XML import.
 */
 
private CoLineMetricsImplementation() {
}


/**
 * Called before XML import. See {@link com.bluebrim.xml.shared.CoXmlImportEnabledIF} for details.
 */
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	return new CoLineMetricsImplementation();
}
}