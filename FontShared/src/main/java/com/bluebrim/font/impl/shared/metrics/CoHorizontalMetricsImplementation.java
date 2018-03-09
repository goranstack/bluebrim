package com.bluebrim.font.impl.shared.metrics;
import java.io.*;
import java.util.*;

import org.w3c.dom.*;

/**
 * Generic implementation of the com.bluebrim.font.shared.metrics.CoHorizontalMetrics interface.
 * The lookup is (hopefully) sped up by checking the lower 256 bytes (which is identical to
 * Latin-1, and cover most of the characters in a typical Swedish text) in a float[], but
 * still keeps a small memory footprint by storing the rest of the unicode range in a HashMap.
 * Creation date: (2001-04-03 15:56:53)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public class CoHorizontalMetricsImplementation implements com.bluebrim.font.shared.metrics.CoHorizontalMetrics, Serializable {
	public static final String XML_ADVANCE_MAP = "advance";
	
	private Map m_highMetrics = new HashMap();		// [Character -> Float]
	private float[] m_lowMetrics = new float[256];
public CoHorizontalMetricsImplementation() {
}

public void setAdvance(char ch, float advance) {
	if (ch <= '\u00FF') {
		m_lowMetrics[(int)ch] = advance;
	} else {
		m_highMetrics.put(new Character(ch), new Float(advance));
	}
}

/**
 * getAdvance method comment.
 */
public float getAdvance(char ch) {
	if (ch <= '\u00FF') {
		return m_lowMetrics[(int)ch];
		// FIXME: take care of situations where metrics does not exist
	} else {
		Float f = (Float)m_highMetrics.get(new Character(ch));

		if (f != null) {
			return f.floatValue();
		} else {
			// return the advance for the .nodef character, which is mapped to Unicode 0
			return m_lowMetrics[(int)0];
		}
	}
}





public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
	Map allMetrics = new HashMap(m_highMetrics);
	for (int i = 0; i < 256; i++) {
		Character character = new Character((char) i);
		float advance = m_lowMetrics[i];
		allMetrics.put(character, new Float(advance));
	}

	visitor.export(XML_ADVANCE_MAP, allMetrics);
}


public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	if (subModel instanceof Map) {
		Map importedMap = (Map) subModel;
		Iterator i = importedMap.keySet().iterator();
		while (i.hasNext()) {
			Character ch = (Character) i.next();
			Number advance = (Number) importedMap.get(ch);
			if (ch.charValue() <= '\u00FF') {
				m_lowMetrics[(int) (ch.charValue())] = advance.floatValue();
			} else {
				m_highMetrics.put(ch, new Float(advance.floatValue()));
			}
		}
	}

	// Otherwise, ignore for compatibility
}


public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	// Empty by design
}


public boolean advanceExistsFor(char ch) {
	if (ch <= '\u00FF') {
		return true;
		// FIXME: take care of situations where metrics does not exist
	} else {
		return m_highMetrics.containsKey(new Character(ch));
	}
}


/**
 * Called before XML import. See {@link com.bluebrim.xml.shared.CoXmlImportEnabledIF} for details.
 */
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	return new CoHorizontalMetricsImplementation();
}
}