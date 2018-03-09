package com.bluebrim.base.shared;

import java.awt.geom.*;

/**
 * Since there was no concrete subclass with double precision.
 *
 * @author Markus Persson 1999-11-25
 */
public class CoDimension2D extends Dimension2D {
	private double m_width;
	private double m_height;

	public CoDimension2D() {
		this(0.0, 0.0);
	}

	public CoDimension2D(double w, double h) {
		m_width = w;
		m_height = h;
	}

	public CoDimension2D(Dimension2D dimension) {
		m_width = dimension.getWidth();
		m_height = dimension.getHeight();
	}
	
	public double getHeight() {
		return m_height;
	}

	public double getWidth() {
		return m_width;
	}

	public void setSize(double w, double h) {
		m_width = w;
		m_height = h;
	}

	public void setWidth(double w) {
		m_width = w;
	}
	
	public void setHeight(double h) {
		m_height = h;
	}
	
	public String toString() {
		return "[ " + m_width + ", " + m_height + " ]";
	}
	

}