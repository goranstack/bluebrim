package com.bluebrim.postscript.impl.server.color;

/**
 * Representation of a colorant (ink) on the typesetter or printer. A colorant is a basic ink that the
 * output device can represent without halftones. Typically this is black for greyscale pages, or
 * cyan, magenta, yellow and black for CMYK pages. A page can however also have any other combination of
 * colorants, e.g. black and magenta. Non-standard colorants are also possible, such as "gold" or
 * hexachrome colors.<p>
 *
 * PENDING: The color model in Calvin is in desperate need of reshaping. This class is probably useless
 * at the moment.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoColorant {
	// Define the four basic CMYK colorants.
	// The neutral density values is of unknown origin, probably "inspired" from Quark Xpress.
	// FIXME: these values must be adjusted for each specific printer
	public static CoColorant BLACK = new CoColorant("Black", "K_Ink", 1.7);
	public static CoColorant CYAN = new CoColorant("Cyan", "C_Ink", 0.61);
	public static CoColorant MAGENTA = new CoColorant("Magenta", "M_Ink", 0.76);
	public static CoColorant YELLOW = new CoColorant("Yellow", "Y_Ink", 0.16);

	// Define a value for undefined measures for step limit and color scaling
	public static double UNDEFINED = Double.NaN;

	private String m_name;
	private String m_postscriptName;
	
	private TrappingType m_trappingType;
	private double m_neutralDensity;
	private double m_trappingStepLimit;
	private double m_trappingColorScaling;
	
	/**
	 * Enumerator class for trapping type parameters.
	 */
	public static class TrappingType {
		public static TrappingType NORMAL = new TrappingType("Normal");
		public static TrappingType TRANSPARENT = new TrappingType("Transparent");
		public static TrappingType OPAQUE = new TrappingType("Opaque");
		public static TrappingType OPAQUEIGNORE = new TrappingType("OpaqueIgnore");
		
		private String m_postscriptName;
		
		private TrappingType(String postscriptName) {
			m_postscriptName = postscriptName;
		}

		public String getPostscriptName() {
			return m_postscriptName;
		}

		public String toString() {
			return getPostscriptName();
		}
	}
public double getNeutralDensity() {
	return m_neutralDensity;
}


public String getPostscriptName() {
	return m_postscriptName;
}


public double getTrappingColorScaling() {
	return m_trappingColorScaling;
}


public double getTrappingStepLimit() {
	return m_trappingStepLimit;
}


public void setNeutralDensity(double neutralDensity) {
	m_neutralDensity = neutralDensity;
}


public void setPostscriptName(String postscriptName) {
	m_postscriptName = postscriptName;
}


public void setTrappingColorScaling(double trappingColorScaling) {
	m_trappingColorScaling = trappingColorScaling;
}


public void setTrappingStepLimit(double trappingStepLimit) {
	m_trappingStepLimit = trappingStepLimit;
}


public CoColorant(String postscriptName, double neutralDensity) {
	this(postscriptName, postscriptName, neutralDensity);
}


public CoColorant(String postscriptName, String name, double neutralDensity) {
	this(postscriptName, name, neutralDensity, TrappingType.NORMAL);
}


public CoColorant(String postscriptName, String name, double neutralDensity, TrappingType trappingType) {
	this(postscriptName, name, neutralDensity, trappingType, UNDEFINED, UNDEFINED);
}


public CoColorant(String postscriptName, String name, double neutralDensity, TrappingType trappingType, 
	double trappingColorScaling, double trappingStepLimit) {
		
	m_postscriptName = postscriptName;
	m_name = name;
	m_trappingType = trappingType;
	m_neutralDensity = neutralDensity;
	m_trappingColorScaling = trappingColorScaling;
	m_trappingStepLimit = trappingStepLimit;
}


public String getName() {
	return m_name;
}


public TrappingType getTrappingType() {
	return m_trappingType;
}


public void setName(String name) {
	m_name = name;
}


public void setTrappingType(TrappingType trappingType) {
	m_trappingType = trappingType;
}


public String toString() {
	return getName() + "[" + getPostscriptName() + "]";
}
}