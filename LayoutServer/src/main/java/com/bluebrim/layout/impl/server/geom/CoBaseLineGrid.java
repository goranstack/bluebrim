package com.bluebrim.layout.impl.server.geom;
import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Abstract superclass to baseline grids.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoBaseLineGrid extends CoObject implements CoImmutableBaseLineGridIF, CoXmlEnabledIF {
	// xml tag constants
	public static final String XML_TYPE = "type";

	protected double m_y;
	protected boolean m_dirty;

	public CoBaseLineGrid() {
		super();
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public CoImmutableBaseLineGridIF deepClone() {
		try {
			CoImmutableBaseLineGridIF grid = (CoImmutableBaseLineGridIF) clone();

			return grid;
		} catch (CloneNotSupportedException ex) {
			return null;
		}
	}

	public CoImmutableBaseLineGridIF derive(double y) {
		CoDerivedBaseLineGrid grid = new CoDerivedBaseLineGrid(this);
		grid.setY(y);
		return grid;
	}

	public abstract CoBaseLineGeometryIF getBaseLineGeometry(double y);

	public String getFactoryKey() {
		return BASE_LINE_GRID;
	}

	double getY0Position(double y) {
		double m = getY0Position();

		if (m < y) {
			int n = 1 + (int) ((y - m) / getDeltaY());
			m += getDeltaY() * n;
		}

		return m - y;
	}

	public boolean isDerived() {
		return false;
	}

	protected void markDirty() {
		m_dirty = !m_dirty;

		if (CoAssertion.SIMULATION_SUPPORT)
			CoAssertion.addChangedObject(this);
	}

	public void setY(double y) {
		if (m_y == y)
			return;

		m_y = y;
		markDirty();
	}

	/*
	 * Used at XML import
	 * Helena Rankegård 2001-10-29
	 */
	public void xmlAddSubModel(String name, Object subModel, CoXmlContext context) {
	}

	/*
	 * Used at XML import
	 * Helena Rankegård 2001-10-29
	 */
	public void xmlImportFinished(Node node, CoXmlContext context) {
	}

	/*
	 * Used at XML export
	 * Helena Rankegård 2001-10-29
	 */
	public void xmlVisit(CoXmlVisitorIF visitor) {
	}
}