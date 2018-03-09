package com.bluebrim.postscript.impl.server.color;

/**
 * A trap style is a combination of trapping properties that is specific to a single trap zone. It is therefore possible
 * to have several trap styles in a single Postscript document. <p>
 *
 * PENDING: This class is not really needed, since trap zones is not to be used as of the current model. 
 *
 * <p><b>Creation date:</b> 2001-08-03
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoTrappingSettings
 */
public class CoTrapStyle {
	// Generic trapping parameters
	private boolean m_trappingEnabled;
	private double m_slidingTrapLimit;		// range: 0.0 - 1.0

	// Image trapping parameters
	private boolean m_imageInternalTrapping;
	private boolean m_imageToObjectTrapping;
	private TrapPlacement m_imageTrapPlacement;

	/**
	 * Enumerator class for image trap placement parameter.
	 */
	public static class TrapPlacement {
		public static TrapPlacement NORMAL = new TrapPlacement("Normal");
		public static TrapPlacement CENTER = new TrapPlacement("Center"); // This is normally the most useful placement
		public static TrapPlacement SPREAD = new TrapPlacement("Spread");
		public static TrapPlacement CHOKE  = new TrapPlacement("Choke");
		
		private String m_placement;
		
		private TrapPlacement(String placement) {
			m_placement = placement;
		}

		public String getPlacement() {
			return m_placement;
		}

		public String toString() {
			return getPlacement();
		}
	}
public boolean equals(Object obj) {
	if (this == obj) return true;
	if (obj == null) return false;
	if (!(obj instanceof CoTrapStyle)) return false;

	CoTrapStyle other = (CoTrapStyle) obj;
	if (this.m_trappingEnabled != other.m_trappingEnabled) return false;
	if (this.m_slidingTrapLimit != other.m_slidingTrapLimit) return false;
	if (this.m_imageInternalTrapping != other.m_imageInternalTrapping) return false;
	if (this.m_imageToObjectTrapping != other.m_imageToObjectTrapping) return false;
	if (this.m_imageTrapPlacement != other.m_imageTrapPlacement) return false; // This is OK, since it's a enumerator class

	return true;
}


public CoTrapStyle() {
}


public CoTrapStyle(boolean trappingEnabled, double slidingTrapLimit, boolean imageInternalTrapping,
	boolean imageToObjectTrapping, TrapPlacement imageTrapPlacement) {
	m_trappingEnabled = trappingEnabled;
	m_slidingTrapLimit = slidingTrapLimit;
	m_imageInternalTrapping = imageInternalTrapping;
	m_imageToObjectTrapping = imageToObjectTrapping;
	m_imageTrapPlacement = imageTrapPlacement;
}


public TrapPlacement getImageTrapPlacement() {
	return m_imageTrapPlacement;
}


public double getSlidingTrapLimit() {
	return m_slidingTrapLimit;
}


public boolean isImageInternalTrapping() {
	return m_imageInternalTrapping;
}


public boolean isImageToObjectTrapping() {
	return m_imageToObjectTrapping;
}


public boolean isTrappingEnabled() {
	return m_trappingEnabled;
}


public void setImageInternalTrapping(boolean imageInternalTrapping) {
	m_imageInternalTrapping = imageInternalTrapping;
}


public void setImageToObjectTrapping(boolean imageToObjectTrapping) {
	m_imageToObjectTrapping = imageToObjectTrapping;
}


public void setImageTrapPlacement(TrapPlacement imageTrapPlacement) {
	m_imageTrapPlacement = imageTrapPlacement;
}


public void setSlidingTrapLimit(double slidingTrapLimit) {
	com.bluebrim.base.shared.debug.CoAssertion.preCondition((slidingTrapLimit >= 0.0) && (slidingTrapLimit <= 1.0), "Value out of range");
	m_slidingTrapLimit = slidingTrapLimit;
}


public void setTrappingEnabled(boolean trappingEnabled) {
	m_trappingEnabled = trappingEnabled;
}
}