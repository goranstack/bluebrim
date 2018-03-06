package com.bluebrim.font.impl.shared.metrics;
import java.io.*;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.debug.*;

/**
 * Implementation of tracking curve
 * 
 * @author: Dennis Malmström
 * @author Magnus Ihse <magnus.ihse@appeal.se> (2001-05-03 15:11:05)
 */

public class CoTrackingMetricsImplementation implements com.bluebrim.font.shared.metrics.CoTrackingMetrics, Serializable {
	public final static String XML_TAG = "tracking-metrics";
	public final static String XML_FONT_SIZES = "font-sizes";
	public final static String XML_TRACKING_VALUES = "tracking-values";

	private float[] m_fontSizeIndex;
	private float[] m_trackingValues;
/**
 * Initialize tracking curve with two arrays. Note: The fontSize array MUST be sorted in
 * ascending order, or getTracking will not work!
 * Creation date: (2001-04-18 10:46:01)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public CoTrackingMetricsImplementation(float[] fontSizeIndex, float[] trackingValues) {
	setTrackingArrays(fontSizeIndex, trackingValues);
}



/**
 * <no description given yet>
 * Creation date: (2001-04-27 21:43:45)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 * 
 * @return float[]
 */
public float[] getFontSizeIndex() {
	return m_fontSizeIndex;
}



public float getTracking(float fontSize) {
	if (m_fontSizeIndex.length < 1) return 0;	// No tracking found

	if (fontSize <= m_fontSizeIndex[0]) {	// Smaller than smallest? Then use smallest.
		return m_trackingValues[0];
	}

	if (fontSize >= m_fontSizeIndex[m_fontSizeIndex.length - 1]) { // Larger than largest?
		return m_trackingValues[m_fontSizeIndex.length - 1];
	}

	// Otherwise, find the largest font size in the index array 
	// that is smaller than the given font size
	int i = 0;
	while (i < m_fontSizeIndex.length - 2) {
		if (fontSize < m_fontSizeIndex[i + 1]) break;
		i++;
	}

	// Now do a linear interpolation between this tracking value and the next higher

	return (
		(m_fontSizeIndex[i] - fontSize) * m_trackingValues[i + 1]
			+ (fontSize - m_fontSizeIndex[i + 1]) * m_trackingValues[i])
		/ (m_fontSizeIndex[i] - m_fontSizeIndex[i + 1]);
}



/**
 * <no description given yet>
 * Creation date: (2001-04-27 21:43:45)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 * 
 * @return float[]
 */
public float[] getTrackingValues() {
	return m_trackingValues;
}



/**
 * <no description given yet>
 * Creation date: (2001-04-27 21:43:45)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 * 
 * @return float[]
 */
public final void setTrackingArrays(float[] fontSizeIndex, float[] trackingValues) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(fontSizeIndex, "fontSizeIndex");
	if (CoAssertion.ASSERT) CoAssertion.notNull(trackingValues, "trackingValues");
	
	if (fontSizeIndex.length != trackingValues.length) {
		throw new ArrayIndexOutOfBoundsException("fontSizeIndex and trackingValues not of equal length");
	}
	
	m_trackingValues = trackingValues;
	m_fontSizeIndex = fontSizeIndex;
}





public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
	List fontSizes = new ArrayList(m_fontSizeIndex.length);
	List trackingValues = new ArrayList(m_trackingValues.length);
	for (int i = 0; i < m_fontSizeIndex.length; i++) {
		fontSizes.add(new Float(m_fontSizeIndex[i]));
		trackingValues.add(new Float(m_trackingValues[i]));
	}
		
	visitor.export(XML_FONT_SIZES, fontSizes);
	visitor.export(XML_TRACKING_VALUES, trackingValues);
}


public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	if (subModel instanceof Iterator) {
		Iterator i = (Iterator) subModel;
		if (XML_FONT_SIZES.equals(parameter)) {
			// First read as linked list to determine length
			List fontSizeList = new LinkedList();
			while (i.hasNext()) {
				fontSizeList.add(i.next());
			}
			// Then convert to float array
			m_fontSizeIndex = new float[fontSizeList.size()];
			for (int j = 0; j < m_fontSizeIndex.length; j++) {
				m_fontSizeIndex[j] = ((Number) (fontSizeList.get(j))).floatValue();
			}
		} else if (XML_TRACKING_VALUES.equals(parameter)) {
			// First read as linked list to determine length
			List trackingList = new LinkedList();
			while (i.hasNext()) {
				trackingList.add(i.next());
			}
			// Then convert to float array
			m_trackingValues = new float[trackingList.size()];
			for (int j = 0; j < m_trackingValues.length; j++) {
				m_trackingValues[j] = ((Number) (trackingList.get(j))).floatValue();
			}
		} 
	} 

	// Otherwise, ignore for compatibility
}


public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	// Empty by design
}


/**
 * This constructor should only be called from XML import.
 */
private CoTrackingMetricsImplementation() {
}


/**
 * Called before XML import. See {@link com.bluebrim.xml.shared.CoXmlImportEnabledIF} for details.
 */
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	return new CoTrackingMetricsImplementation();
}
}