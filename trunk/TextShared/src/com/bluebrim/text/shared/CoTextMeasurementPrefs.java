package com.bluebrim.text.shared;
import java.util.*;

import com.bluebrim.base.shared.*;

/**
 * Implementation of text measurement preferences.
 * 
 * @author: Dennis Malmström
 */

public class CoTextMeasurementPrefs extends CoObject implements CoTextMeasurementPrefsIF {
	private boolean m_dirty;

	private List m_measurementTags = new ArrayList();
	private double m_measurementColumnWidth = 500;

	public String getFactoryKey() {
		return null;
	}

	public double getMeasurementColumnWidth() {
		return m_measurementColumnWidth;
	}

	public List getMeasurementTags() {
		return m_measurementTags;
	}

	private void markDirty() {
		m_dirty = !m_dirty;
	}

	public void setMeasurementColumnWidth(double w) {
		m_measurementColumnWidth = w;
		markDirty();
	}

	public void setMeasurementTags(List tags) {
		m_measurementTags = tags;
		markDirty();
	}
}
