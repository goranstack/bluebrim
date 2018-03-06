package com.bluebrim.layout.impl.server.geom;

import java.awt.geom.*;

import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.shared.*;

/**
 * Abstract superclass for all grid line implementations.
 * A grid line is a line defined by a point and a length.
 * They are mostly used for snapping against.
 * They can also be intersected with rectangles for column grid derivation purposes.
 * 
 * @author: Dennis Malmström
 */
public abstract class CoGridLine implements CoGridLineIF, CoGeometryConstants {
	// grid line geometry
	protected double m_x;
	protected double m_y;
	protected double m_length;

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	public double getLength() {
		return m_length;
	}
	public double getX() {
		return m_x;
	}
	public double getY() {
		return m_y;
	}
	public void setLength(double l) {
		m_length = l;
	}
	public void setX(double x) {
		m_x = x;
	}
	public void setY(double y) {
		m_y = y;
	}
	public boolean snap(double x, double y, double range, int edgeMask, Point2D p, Point2D d) {
		return snap(x, y, range, edgeMask, ALL_DIRECTIONS_MASK, p, d);
	}

	public String toString() {
		return "( " + m_x + ", " + m_y + " ) " + m_length;
	}
	public void translateBy(double dx, double dy) {
		m_x += dx;
		m_y += dy;
	}

	protected int m_type;

	public CoGridLine(double x, double y, double length, int type) {
		m_type = type;

		setX(x);
		setY(y);
		setLength(length);
	}

	public int getType() {
		return m_type;
	}
}