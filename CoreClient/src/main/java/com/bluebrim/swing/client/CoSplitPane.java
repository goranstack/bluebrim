package com.bluebrim.swing.client;
import java.awt.Insets;

import javax.swing.JSplitPane;

/**
 * Extension to JSplitPane adding the possibility for the split
 * to retain its proportions when resized.
 *
 * @author Markus Persson 1999-11-01
 * @author Markus Persson 2001-04-27
 */
public class CoSplitPane extends JSplitPane {
	private boolean m_proportionalSplit = true;
	private double m_proportionalLocation = 0.5;
	private int m_correspondingPixelLocation;
	private double m_proportionalLastLocation = 0.5;

public CoSplitPane() {
	this(false);
}


public CoSplitPane(boolean proportionalSplit) {
	super(HORIZONTAL_SPLIT, false);
	m_proportionalSplit = proportionalSplit;
	m_correspondingPixelLocation = super.getDividerLocation();
}


public int getLastDividerLocation() {
	return m_proportionalSplit ? toFixed(m_proportionalLastLocation) : super.getLastDividerLocation();
}

public double getProportionalDividerLocation() {
	int pixelLocation = super.getDividerLocation();
	if (pixelLocation == m_correspondingPixelLocation) {
		// No change in pixels. Our proportion is valid.
		return m_proportionalLocation;
	} else {
		// Change in pixels. Update our proportion.
		m_correspondingPixelLocation = pixelLocation;
		return m_proportionalLocation =	toProportional(pixelLocation, m_proportionalLocation);
	}
}


public void setBounds(int x, int y, int width, int height) {
	if (m_proportionalSplit && ((getOrientation() == VERTICAL_SPLIT) ? (height != getHeight()) : (width != getWidth()))) {
		double proportionalLocation = getProportionalDividerLocation();
		super.setBounds(x, y, width, height);
		super.setDividerLocation(m_correspondingPixelLocation = toFixed(proportionalLocation));
	} else {
		super.setBounds(x, y, width, height);
	}
}

public void setDividerLocation(double proportionalLocation) {
	if (proportionalLocation < 0.0 || proportionalLocation > 1.0) {
		throw new IllegalArgumentException("proportional location must be between 0.0 and 1.0.");
	}

	m_proportionalLocation = proportionalLocation;
	super.setDividerLocation(toFixed(proportionalLocation));
	// Need to remember the returned pixel location, since it may not
	// always be the one we sat. This is most notable initially.
	m_correspondingPixelLocation = super.getDividerLocation();
}


public void setDividerLocation(int location) {
	// Overrided simply to avoid Yikes ambigous errors. /Markus 2001-05-03
	super.setDividerLocation(location);
}

public void setLastDividerLocation(int newLastLocation) {
	m_proportionalLastLocation = toProportional(newLastLocation, m_proportionalLastLocation);
	super.setLastDividerLocation(newLastLocation);
}

public void setProportionalSplit(boolean proportionalSplit) {
	m_proportionalSplit = proportionalSplit;
}


private int toFixed(double proportionalLocation) {
	// NOTE: Hardcoded divider border size from Basic L&F. (The 1)
	int pixelMin, pixelSpace;
	Insets insets = getInsets();
	if (getOrientation() == VERTICAL_SPLIT) {
		pixelMin = 1 + insets.top;
		pixelSpace = getHeight() - getDividerSize() - 1 - insets.bottom - pixelMin;
	} else {
		pixelMin = 1 + insets.left;
		pixelSpace = getWidth() - getDividerSize() - 1 - insets.right - pixelMin;
	}
	return pixelMin + (int) (((double)pixelSpace) * proportionalLocation);
}

private double toProportional(int fixedLocation, double defaultProportional) {
	// NOTE: Hardcoded divider border size from Basic L&F. (The 1)
	int pixelMin, pixelSpace;
	Insets insets = getInsets();
	if (getOrientation() == VERTICAL_SPLIT) {
		pixelMin = 1 + insets.top;
		pixelSpace = getHeight() - getDividerSize() - 1 - insets.bottom - pixelMin;
	} else {
		pixelMin = 1 + insets.left;
		pixelSpace = getWidth() - getDividerSize() - 1 - insets.right - pixelMin;
	}
	return (pixelSpace > 0) ? (((double) (fixedLocation - pixelMin)) / pixelSpace) : defaultProportional;
}
}