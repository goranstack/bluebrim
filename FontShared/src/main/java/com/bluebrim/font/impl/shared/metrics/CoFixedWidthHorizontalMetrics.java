package com.bluebrim.font.impl.shared.metrics;
import java.io.*;

import org.w3c.dom.*;

/**
 * Implementation of the com.bluebrim.font.shared.metrics.CoHorizontalMetrics interface for fixed-width fonts (a.k.a monospaced fonts). For fixed-width
 * fonts, such as Courier, this is much faster and less resource-hungry. However, as of now, fixed-width fonts are not
 * detected in neither Type 1 nor TrueType font parsers, so this class is not used by them.
 *
 * <p><b>Creation date:</b> 2001-10-01
 * <br><b>Documentation last updated:</b> 2001-10-01
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoHorizontalMetricsImplementation
 */
public class CoFixedWidthHorizontalMetrics implements com.bluebrim.font.shared.metrics.CoHorizontalMetrics, Serializable {
	public final static String XML_TAG = "fixed-width-horizontal-metrics";
	
	private float m_allAdvance;
public float getAdvance(char ch) {
	return m_allAdvance;
}


public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
	visitor.export(null, new Float(m_allAdvance));
}


public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	if (subModel instanceof Number) {
		m_allAdvance = ((Number) subModel).floatValue();
	}

	// Otherwise, ignore for compatibility
}


public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	// Empty by design
}


public boolean advanceExistsFor(char ch) {
	return true;
}


/**
 * Called before XML import. See {@link com.bluebrim.xml.shared.CoXmlImportEnabledIF} for details.
 */
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	return new CoFixedWidthHorizontalMetrics();
}


/**
 * Default constructor. If possible, use the constructor which specifies a advance directly.
 *
 * @see #CoFixedWidthHorizontalMetrics(float)
 */
public CoFixedWidthHorizontalMetrics() {
}


/**
 * Sets the advance for all characters in this fixed-width font. Since the font is monospaced, all fonts have
 * the same single advance. The advance is given in points (1/72 inch) for a font size of 1 point for the
 * font face. This value is preferrably specified in the constructor instead.
 *
 * @param advance the advance in points.
 *
 * @see #CoFixedWidthHorizontalMetrics(float)
 */
public void setAllAdvance(float advance) {
	m_allAdvance = advance;
}


/**
 * Constructor with the advance value of the monospaced font. This is the preferred constructor.
 *
 * @param allAdvance the advance of all characters in this font.
 *
 * @see #setAllAdvance(float)
 */
public CoFixedWidthHorizontalMetrics(float allAdvance) {
	m_allAdvance = allAdvance;
}
}