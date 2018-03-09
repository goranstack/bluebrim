package com.bluebrim.font.impl.shared.metrics;

import java.io.*;
import java.util.*;

import org.w3c.dom.*;

/**
 * Default implementation of pair kerning metrics. This implementation is based on a simple hash map, mapping from
 * pairs of Characters to Floats. This solution is simple, but perhaps not extremely efficient. The map is built by
 * adding one pair kerning value at a time, typically when parsing a font file or XML file.<p>
 * 
 * The kerning values are given for the unscaled font face, i.e. the value is given for the font at size 1 point.
 * To be useful at any other font size, it must be scaled to that particular font size.<p>
 *
 * The <code>CoPairKerningMetricsImplementation</code> should only be directly specified when the kerning map is
 * actually build from the font file. Otherwise, the data structure should only be referred to as a 
 * <code>com.bluebrim.font.shared.metrics.CoPairKerningMetrics</code>, which is an immutable interface.
 *
 * <p><b>Creation date:</b> 2001-04-12
 * <br><b>Documentation last updated:</b> 2001-10-04
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoPairKerningMetricsImplementation implements com.bluebrim.font.shared.metrics.CoPairKerningMetrics, Serializable {
	public final static String XML_PAIR_MAP = "pair-map";

	/**
	 * Simple class to bundle to chars together, to use as a key in the pair kerning map.
	 */
	public static class CharPairKey implements Serializable, com.bluebrim.xml.shared.CoXmlEnabledIF {
		public final static String XML_TAG = "char-pair";
		public final static String XML_1 = "1";
		public final static String XML_2 = "2";
		
		private char m_ch1;
		private char m_ch2;

		/** Creates a new CharPairKey from two chars.
		 */
		public CharPairKey(char ch1, char ch2) {
			m_ch1 = ch1;
			m_ch2 = ch2;
		}

		/** Private constructor, only used by XML import.
		 */
		private CharPairKey() {
		}
		
		public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
			return new CharPairKey();
		}

		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof CharPairKey))
				return false;
			CharPairKey other = (CharPairKey) obj;
			if (other.m_ch1 == m_ch1 && other.m_ch2 == m_ch2)
				return true;

			return false;
		}

		public int hashCode() {
			return (m_ch1 << 16 | m_ch2);
		}

		public String toString() {
			return "(" + m_ch1 + "," + m_ch2 + ")";
		}

		public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
			if (subModel instanceof Character) {
				if (XML_1.equals(parameter)) {
					m_ch1 = ((Character) subModel).charValue();
				} else if (XML_2.equals(parameter)) {
					m_ch2 = ((Character) subModel).charValue();
				}
			}
		}

		public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
			// Intentionally left blank
		}

		public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
			visitor.export(XML_1, new Character(m_ch1));
			visitor.export(XML_2, new Character(m_ch2));
		}
	}

	private Map m_map = new HashMap(); // [CoCharPairKey -> Float]
/**
 * Default constructor, constructs an empty pair kerning map.
 */
public CoPairKerningMetricsImplementation() {
}







public float getPairKerning(char ch1, char ch2) {
	Float kerning = (Float) m_map.get(new CharPairKey(ch1, ch2));
	if (kerning == null) {
		return 0.0f;
	} else {
		return kerning.floatValue();
	}
}



/**
 * Sets the pair kerning delta for a pair of characters. This method will update the kerning map with a new kern pair,
 * and the kern value to use between those two characters. The tracking is specified in points (1/72 inch). 
 * Negative values indicates that glyphs should be moved closer together, positive values (unusual) indicates 
 * that the glyphs should be moved further apart.
 *
 * @param ch1 the unicode character for the first glyph.
 * @param ch2 the unicode character for the second glyph.
 * @param tracking the tracking value for this pair at font size 1 point, in points (1/72 inch).
 *
 * @see #getPairKerning(char,char)
 */
public void setPairKerning(char ch1, char ch2, float tracking) {
	m_map.put(new CharPairKey(ch1, ch2), new Float(tracking));
}





public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
	visitor.export(XML_PAIR_MAP, m_map);
}


public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	if (subModel instanceof Map) {
		m_map = new HashMap((Map) subModel);	// Create new to be sure it is a HashMap
	}

	// Otherwise, ignore for compatibility
}


public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	// Empty by design
}


/**
 * Called before XML import. See {@link com.bluebrim.xml.shared.CoXmlImportEnabledIF} for details.
 */
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	return new CoPairKerningMetricsImplementation();
}
}