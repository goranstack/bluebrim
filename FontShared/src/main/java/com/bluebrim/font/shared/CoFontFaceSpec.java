package com.bluebrim.font.shared;
import java.io.*;

import org.w3c.dom.*;

/**
 * Immutable font face specification class.
 *
 * This class is intended to serve as complete feature set for a font face.
 * In a way it is similar to the AttributeSet class. The difference is that
 * for this class the feature space is well defined and closed (except for
 * the family name).
 *
 * A CoFontFaceSpec can be used to find a com.bluebrim.font.shared.CoFontFace, either by an exact match,
 * if available, or a closest match. Also a com.bluebrim.font.shared.CoFontFace can be queried about
 * what CoFontFaceSpec it represents.
 *
 * NOTE: Removed set method added by someone not understanding the meaning
 * of immutable!
 *
 * @author Markus Persson 2000-08-30
 * @author Markus Persson 2001-08-31
 */
public class CoFontFaceSpec implements Serializable, Comparable {
	// State
	private final String m_familyName;
	private final int m_weight;
	private final int m_style;
	private final int m_variant;
	private final int m_stretch;

	// Transient cache
	private transient String m_string;
	private transient int m_hash;

	/** Simple wrapper class that implements com.bluebrim.xml.shared.CoXmlEnabledIF, and which can construct a CoFontFaceSpec. Created only
	 * due to limitations in the XML framework.
	 */
	public static class XmlWrapper implements com.bluebrim.xml.shared.CoXmlEnabledIF {
		public final static String XML_TAG = "font-face-spec";
		public final static String XML_WEIGHT = "weight";
		public final static String XML_STYLE = "style";
		public final static String XML_VARIANT = "variant";
		public final static String XML_STRETCH = "stretch";
		public final static String XML_NAME = "name";

		// State
		private String m_familyName;
		private int m_weight;
		private int m_style;
		private int m_variant;
		private int m_stretch;

		public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
			visitor.export(XML_WEIGHT, new Integer(m_weight));
			visitor.export(XML_STYLE, new Integer(m_style));
			visitor.export(XML_VARIANT, new Integer(m_variant));
			visitor.export(XML_STRETCH, new Integer(m_stretch));
			visitor.exportString(XML_NAME, m_familyName);
		}

		public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
			if (subModel instanceof Number) {
				if (XML_WEIGHT.equals(parameter)) {
					m_weight = ((Number) subModel).intValue();
				} else if (XML_STYLE.equals(parameter)) {
					m_style = ((Number) subModel).intValue();
				} else if (XML_VARIANT.equals(parameter)) {
					m_variant = ((Number) subModel).intValue();
				} else if (XML_STRETCH.equals(parameter)) {
					m_stretch = ((Number) subModel).intValue();
				}
			} else if (subModel instanceof String) {
				if (XML_NAME.equals(parameter)) {
					m_familyName = (String) subModel;
				}
			}

			// Otherwise, ignore for compatibility
		}

		public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
			// Empty by design
		}

		private XmlWrapper() {
		}

		public XmlWrapper(CoFontFaceSpec spec) {
			m_familyName = spec.m_familyName;
			m_weight = spec.m_weight;
			m_style = spec.m_style;
			m_variant = spec.m_variant;
			m_stretch = spec.m_stretch;
		}

		/**
		 * Creates and returns a new CoFontFaceSpec based on what has been read by the XML import.
		 *
		 * @return a new, immutable CoFontFaceSpec.
		 */
		public CoFontFaceSpec getFontFaceSpec() {
			return new CoFontFaceSpec(m_familyName, m_weight, m_style, m_variant, m_stretch);
		}

		public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
			return new XmlWrapper();
		}
	}


	// Dangerous stuff ... /Markus
//	private static CoFontFaceSpec m_key = new CoFontFaceSpec();



public CoFontFaceSpec(String family, com.bluebrim.font.shared.CoFontAttribute weight, com.bluebrim.font.shared.CoFontAttribute style, com.bluebrim.font.shared.CoFontAttribute variant, com.bluebrim.font.shared.CoFontAttribute stretch) {
	// NOTE: Type check of the attributes could easily be done by adding and using a method
	// in com.bluebrim.font.shared.CoFontAttribute called getValueForOrFail taking an axis as argument. Alternatively,
	// which also would provide compile time checking, com.bluebrim.font.shared.CoFontAttribute could be subclassed
	// per axis. /Markus 2001-09-03
	this(family, weight.getValue(), style.getValue(), variant.getValue(), stretch.getValue());
}

/**
 * NOTE: As all other Comparable implementations, this throws
 * a NullPointerException if the argument is null and
 * a ClassCastException if it is of the wrong type. Do NOT
 * change this! Change where this is wrongly used instead.
 * @author Markus Persson 2001-08-31
 */
public int compareTo(Object other) {
	return compareTo((CoFontFaceSpec) other);
}

public boolean equals(Object other) {
	return (other instanceof CoFontFaceSpec) && equals((CoFontFaceSpec) other);
}

public String getFamilyName() {
	return m_familyName;
}

public static CoFontFaceSpec getKey(String family, com.bluebrim.font.shared.CoFontAttribute weight, com.bluebrim.font.shared.CoFontAttribute style, com.bluebrim.font.shared.CoFontAttribute variant, com.bluebrim.font.shared.CoFontAttribute stretch) {
	// NOTE: Instance reuse can be accomplished in a couple of ways,
	// none of which has been implemented. (The old way isn't one of
	// them.) For now, this will have to do. Also, HotSpot VMs handle
	// short lived objects pretty well. /Markus 2001-09-03
	return new CoFontFaceSpec(family, weight, style, variant, stretch);
}


public int hashCode() {
	if (m_hash == 0) {
		// Let's make a hash value composed of two int's added together. The first int is composed of the
		// fixed space parameters, scaled to fit in a byte each, with the bytes shifted to fit in an int.
		// Note that this calculation's efficiency as a hash code is heavily dependent on the values in
		// com.bluebrim.font.shared.CoFontConstants. If these are ever changed, the hashCode can lose dramatically in efficiency.
		// The second int is the hashCode of the family name. /Ihse
		int weightVal = (m_weight + 300) / 50;
		int stretchVal = (m_stretch + 400) / 50;
		int styleVal = m_style;
		int variantVal = m_variant;
		int familyVal = m_familyName.hashCode();

		m_hash = ((weightVal << 24) | (stretchVal << 16) | (styleVal << 8) | (variantVal)) + familyVal;
		// NOTE: If m_hash becomes zero it will be recalculated every time. This could be avoided
		// by detecting this case and change it to some other fixed value. The downside of this
		// is that every object has to pay for the few (if any) object having hashcode zero. /Markus
		// With the chosen method of calculating the hash code, this is however very unlikely, requiring
		// a familyName hash code of zero, and a strange weightVal. /Magnus Ihse
	}

	return m_hash;
}


public String toString() {
	if (m_string == null) {
		StringBuffer b = new StringBuffer(m_familyName);

		if (m_weight != com.bluebrim.font.shared.CoFontConstants.NORMAL_WEIGHT) {
			b.append(" ");
			b.append(com.bluebrim.font.shared.CoFontAttribute.getWeight(m_weight).getName());
		}

		if (m_style != com.bluebrim.font.shared.CoFontConstants.NORMAL_STYLE) {
			b.append(" ");
			b.append(com.bluebrim.font.shared.CoFontAttribute.getStyle(m_style).getName());
		}

		if (m_stretch != com.bluebrim.font.shared.CoFontConstants.NORMAL_STRETCH) {
			b.append(" ");
			b.append(com.bluebrim.font.shared.CoFontAttribute.getStretch(m_weight).getName());
		}

		if (m_variant != com.bluebrim.font.shared.CoFontConstants.NORMAL_VARIANT) {
			b.append(" ");
			b.append(com.bluebrim.font.shared.CoFontAttribute.getVariant(m_variant).getName());
		}

		m_string = b.toString();
	}

	return m_string;
}


/**
 * Return the squared distance in the font spec space between this and another spec. Note that variation
 * is not considered at all.
 * Creation date: (2001-05-15 12:07:45)
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public int distanceTo(CoFontFaceSpec other) {
	// Normalize all distances to a common scale. This is of course not perfectly scientific. The scaling
	// values has been chosen as to get as reasonable values as possible, according to what the user would like
	// to get if the specified font is not available, in my opinion.
	// This means that the distance between roman and italic is estimated to 230 (something just a bit larger than
	// 200, but smaller than 250). It also means that condensed/expanded (which has a value of 200) is estimated
	// to a weight of 250 (between 200 and 300, and larger than italic). These are the basic premises that lies
	// behind this scaling figures.
	// Magnus Ihse <magnus.ihse@appeal.se> (2001-05-11 14:28:02)
	double weightDistance = m_weight - other.m_weight;
	double styleDistance = (m_style - other.m_style) * 230; 		// normalize to weight scale
	double stretchDistance = (m_stretch - other.m_stretch) * 1.25;	// normalize to weight scale

	// now return the square sum
	return (int) (weightDistance * weightDistance +
				  styleDistance * styleDistance +
			      stretchDistance * stretchDistance);
}


public CoFontFaceSpec(String family, int weight, int style, int variant, int stretch) {
	m_familyName = family;
	m_style = style;
	m_variant = variant;
	m_weight = weight;
	m_stretch = stretch;
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
public int compareTo(CoFontFaceSpec other) {
	if (other == this) {
		return 0;
	}

	int i = m_familyName.compareTo(other.m_familyName);
	if (i != 0) {
		return i;
	}

	i = compare(m_weight, other.m_weight);
	if (i != 0) {
		return i;
	}

	i = compare(m_style, other.m_style);
	if (i != 0) {
		return i;
	}

	i = compare(m_variant, other.m_variant);
	if (i != 0) {
		return i;
	}

	return compare(m_stretch, other.m_stretch);
}


public boolean equals(CoFontFaceSpec other) {
	return (other == this) ||
		((m_weight == other.m_weight) &&
		(m_style == other.m_style) &&
		(m_stretch == other.m_stretch) &&
		(m_variant == other.m_variant) &&
		m_familyName.equals(other.m_familyName));
}


public static CoFontFaceSpec getKey(String family, int weight, int style, int variant, int stretch) {
	// NOTE: Instance reuse can be accomplished in a couple of ways,
	// none of which has been implemented. (The old way isn't one of
	// them.) For now, this will have to do. Also, HotSpot VMs handle
	// short lived objects pretty well. /Markus 2001-09-03
	return new CoFontFaceSpec(family, weight, style, variant, stretch);
}


public int getStretch() {
	return m_stretch;
}


public com.bluebrim.font.shared.CoFontAttribute getStretchAttribute() {
	return com.bluebrim.font.shared.CoFontAttribute.getStretch(m_stretch);
}


public int getStyle() {
	return m_style;
}


public com.bluebrim.font.shared.CoFontAttribute getStyleAttribute() {
	return com.bluebrim.font.shared.CoFontAttribute.getStyle(m_style);
}


public int getVariant() {
	return m_variant;
}


public com.bluebrim.font.shared.CoFontAttribute getVariantAttribute() {
	return com.bluebrim.font.shared.CoFontAttribute.getVariant(m_variant);
}


public int getWeight() {
	return m_weight;
}


public com.bluebrim.font.shared.CoFontAttribute getWeightAttribute() {
	return com.bluebrim.font.shared.CoFontAttribute.getWeight(m_weight);
}
}