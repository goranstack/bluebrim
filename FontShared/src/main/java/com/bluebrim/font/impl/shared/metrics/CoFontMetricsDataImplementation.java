package com.bluebrim.font.impl.shared.metrics;

import java.io.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.debug.*;

/**
 * Font face data.
 *
 * This class contains the large data for a font face,
 * namely metrics, kerning and tracking information.
 *
 * @author Markus Persson 2000-09-04
 * @author Magnus Ihse <magnus.ihse@appeal.se> (2001-04-27 18:59:32)
 */
public class CoFontMetricsDataImplementation implements com.bluebrim.font.shared.metrics.CoFontMetricsData, Serializable {
	// metrics data
	private com.bluebrim.font.shared.metrics.CoHorizontalMetrics m_horizontalMetrics;
	private com.bluebrim.font.shared.metrics.CoLineMetrics m_lineMetrics;
	private com.bluebrim.font.shared.metrics.CoPairKerningMetrics m_pairKerningMetrics;
	private com.bluebrim.font.shared.metrics.CoTrackingMetrics m_trackingMetrics;
	private float m_italicAngle;
/**
 * Return the advance for the glyph corresponding to the given Unicode char.
 * Creation date: (2001-04-20 10:38:29)
 * @param ch The unicode character for which to get the advance width.
 * @return The advance for that character.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public float getAdvance(char ch) {
	return m_horizontalMetrics.getAdvance(ch);
}

public com.bluebrim.font.shared.metrics.CoHorizontalMetrics getHorizontalMetrics() {
	return m_horizontalMetrics;
}

public com.bluebrim.font.shared.metrics.CoLineMetrics getLineMetrics() {
	return m_lineMetrics;
}

/**
 * Return the pair kerning delta for the two glyphs corresponding to the given Unicode chars.
 * Creation date: (2001-04-20 10:38:29)
 * @param ch1 The unicode character for the first glyph.
 * @param ch2 The unicode character for the second glyph.
 * @return The pair kerning value for these two characters.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public float getPairKerning(char ch1, char ch2) {
	if (m_pairKerningMetrics != null) {
		return m_pairKerningMetrics.getPairKerning(ch1, ch2);
	} else {
		return 0;
	}
}

public com.bluebrim.font.shared.metrics.CoPairKerningMetrics getPairKerningMetrics() {
	return m_pairKerningMetrics;
}

/**
 * Return the tracking for the specified font size.
 * Creation date: (2001-04-20 10:38:29)
 * @param fontSize The font size.
 * @return The track kerning value for the font face at this font size.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public float getTracking(float fontSize) {
	if (m_trackingMetrics != null) {
		return m_trackingMetrics.getTracking(fontSize);
	} else {
		return 0;
	}
}

public com.bluebrim.font.shared.metrics.CoTrackingMetrics getTrackingMetrics() {
	return m_trackingMetrics;
}


public float getItalicAngle() {
	return m_italicAngle;
}


public CoFontMetricsDataImplementation(com.bluebrim.font.shared.metrics.CoHorizontalMetrics horizontalMetrics, com.bluebrim.font.shared.metrics.CoLineMetrics lineMetrics,
	com.bluebrim.font.shared.metrics.CoPairKerningMetrics pairKerningMetrics, com.bluebrim.font.shared.metrics.CoTrackingMetrics trackingMetrics) {

	this(horizontalMetrics, lineMetrics, pairKerningMetrics, trackingMetrics, 0.0f);
}



public CoFontMetricsDataImplementation(com.bluebrim.font.shared.metrics.CoHorizontalMetrics horizontalMetrics, com.bluebrim.font.shared.metrics.CoLineMetrics lineMetrics,
	com.bluebrim.font.shared.metrics.CoPairKerningMetrics pairKerningMetrics, com.bluebrim.font.shared.metrics.CoTrackingMetrics trackingMetrics, float italicAngle) {

	if (CoAssertion.ASSERT) CoAssertion.notNull(horizontalMetrics, "horizontalMetrics");
	if (CoAssertion.ASSERT) CoAssertion.notNull(lineMetrics, "lineMetrics");
	if (CoAssertion.ASSERT) CoAssertion.notNull(pairKerningMetrics, "pairKerningMetrics");
	if (CoAssertion.ASSERT) CoAssertion.notNull(trackingMetrics, "trackingMetrics");
		
	m_horizontalMetrics = horizontalMetrics;
	m_lineMetrics = lineMetrics;
	m_pairKerningMetrics = pairKerningMetrics;
	m_trackingMetrics = trackingMetrics;
	m_italicAngle = italicAngle;
}





public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
	visitor.export(m_horizontalMetrics);
	visitor.export(m_lineMetrics);
	visitor.export(m_pairKerningMetrics);
	visitor.export(m_trackingMetrics);
	visitor.export(XML_ITALIC_ANGLE, new Float(m_italicAngle));
}


public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	if (subModel instanceof com.bluebrim.font.shared.metrics.CoHorizontalMetrics) {
		m_horizontalMetrics = (com.bluebrim.font.shared.metrics.CoHorizontalMetrics) subModel;
	} else if (subModel instanceof com.bluebrim.font.shared.metrics.CoLineMetrics) {
		m_lineMetrics = (com.bluebrim.font.shared.metrics.CoLineMetrics) subModel;
	} else if (subModel instanceof com.bluebrim.font.shared.metrics.CoPairKerningMetrics) {
		m_pairKerningMetrics = (com.bluebrim.font.shared.metrics.CoPairKerningMetrics) subModel;
	} else if (subModel instanceof com.bluebrim.font.shared.metrics.CoTrackingMetrics) {
		m_trackingMetrics = (com.bluebrim.font.shared.metrics.CoTrackingMetrics) subModel;
	} else if (subModel instanceof Number) {
		if (XML_ITALIC_ANGLE.equals(parameter)) {
			m_italicAngle = ((Number) subModel).floatValue();
		}
	}
	
	// Otherwise, ignore for compatibility
}


public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	// Empty by design
}


/**
 * This constructor must only be called from the XML import.
 */
 
private CoFontMetricsDataImplementation() {
}


public boolean advanceExistsFor(char ch) {
	return m_horizontalMetrics.advanceExistsFor(ch);
}


/**
 * Called before XML import. See {@link com.bluebrim.xml.shared.CoXmlImportEnabledIF} for details.
 */
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	return new CoFontMetricsDataImplementation();
}
}