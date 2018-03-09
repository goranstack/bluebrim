package com.bluebrim.stroke.impl.shared;
import java.awt.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.stroke.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Implementation of a stroke dash definition (see CoStrokeLayer and CoStroke).
 * 
 * @author: Dennis Malmström
 */

public class CoDash extends CoSimpleObject implements CoDashIF {
	public static final String XML_CAP = "cap";
	public static final String XML_CYCLE_LENGTH = "cycle-length";
	public static final String XML_CYCLE_LENGTH_IN_WIDTH = "cycle-length-in-length";
	public static final String XML_DASH = "dash";
	public static final String XML_JOIN = "join";
	public static final String XML_MITER_LIMIT = "miter-limit";
	public static final String XML_TAG = "dash";

	private int m_join;
	private int m_cap;
	private float m_miterLimit;
	private float m_dash[];
	private float m_cycleLength = 1;
	private boolean m_cycleLengthInWidth = true;
	private boolean m_dirty;

	public CoDash() {
		this(CAP_BUTT, JOIN_MITER, 10.0f, null);
	}

	public CoDash(int cap, int join, float miterLimit, float dash[]) {
		if (cap != BasicStroke.CAP_BUTT && cap != BasicStroke.CAP_ROUND && cap != BasicStroke.CAP_SQUARE) {
			throw new IllegalArgumentException("illegal end cap value");
		}

		if (join == BasicStroke.JOIN_MITER) {
			if (miterLimit < 1.0f) {
				throw new IllegalArgumentException("miter limit < 1");
			}
		} else
			if (join != BasicStroke.JOIN_ROUND && join != BasicStroke.JOIN_BEVEL) {
				throw new IllegalArgumentException("illegal line join value");
			}

		if (dash != null) {
			boolean allzero = true;
			for (int i = 0; i < dash.length; i++) {
				float d = dash[i];
				if (d > 0.0) {
					allzero = false;
				} else
					if (d < 0.0) {
						throw new IllegalArgumentException("negative dash length");
					}
			}

			if (allzero) {
				throw new IllegalArgumentException("dash lengths all zero");
			}
		}

		m_cap = cap;
		m_join = join;
		m_miterLimit = miterLimit;
		m_dash = dash;
	}

	public CoDash(CoDashIF ds) {
		this(ds.getCap(), ds.getJoin(), ds.getMiterLimit(), ds.getDash());

		m_cycleLength = ds.getCycleLength();
		m_cycleLengthInWidth = ds.isCycleLengthInWidth();
	}

	public float calculateCycleLength(float width) {
		if ((m_dash == null) || (m_dash.length == 0)) {
			return Float.NaN;
		}

		return m_cycleLength * (m_cycleLengthInWidth ? width : 1);
	}

	public Stroke createStroke(float width, float phase) {
		float[] scaledDash = null;
		if ((m_dash != null) && (m_dash.length != 0)) {
			scaledDash = new float[m_dash.length];
			for (int i = 0; i < scaledDash.length; i++) {
				scaledDash[i] = m_dash[i] * m_cycleLength;
				if (m_cycleLengthInWidth)
					scaledDash[i] *= width;
			}
		}

		return new CoBasicStrokeProxy(width, m_cap, m_join, m_miterLimit, scaledDash, phase);
	}

	public Stroke createUndashedStroke(float width) {
		return new CoBasicStrokeProxy(width, m_cap, m_join, m_miterLimit, null, 0f);
	}

	public CoDashIF deepClone() {
		CoDash c = null;

		try {
			c = (CoDash) clone();
		} catch (CloneNotSupportedException ex) {
			CoAssertion.assertTrue(false, "CoDash.clone() failed in CoDash.deepClone()");
		}

		return c;
	}

	public int getCap() {
		return m_cap;
	}

	public float getCycleLength() {
		return m_cycleLength;
	}

	public float[] getDash() {
		return m_dash;
	}

	public int getJoin() {
		return m_join;
	}

	public float getMiterLimit() {
		return m_miterLimit;
	}

	public int getNumberOfSegments() {
		return m_dash.length;
	}

	public boolean isCycleLengthInWidth() {
		return m_cycleLengthInWidth;
	}

	protected void markDirty() {
		m_dirty = !m_dirty;
	}

	public void setCap(int cap) {
		m_cap = cap;
		markDirty();
	}

	public void setCycleLength(float cycleLength) {
		m_cycleLength = cycleLength;
		markDirty();
	}

	public void setCycleLengthInWidth(boolean cycleLengthInWidth) {
		m_cycleLengthInWidth = cycleLengthInWidth;
		markDirty();
	}

	public void setDash(float[] dash) {
		m_dash = dash;
		markDirty();
	}

	public void setJoin(int join) {
		m_join = join;
		markDirty();
	}

	public void setMiterLimit(float miterLimit) {
		m_miterLimit = miterLimit;
		markDirty();
	}

	public void xmlVisit(CoXmlVisitorIF visitor) {
		visitor.exportAttribute(XML_JOIN, Integer.toString(getJoin()));
		visitor.exportAttribute(XML_CAP, Integer.toString(getCap()));

		visitor.exportAttribute(XML_CYCLE_LENGTH, Float.toString(getCycleLength()));

		visitor.exportAttribute(XML_CYCLE_LENGTH_IN_WIDTH, (isCycleLengthInWidth() ? Boolean.TRUE : Boolean.FALSE).toString());
		visitor.exportAttribute(XML_MITER_LIMIT, Float.toString(getMiterLimit()));

		if (m_dash != null) {
			StringBuffer tmp = new StringBuffer();
			for (int i = 0; i < m_dash.length; i++) {
				if (i > 0)
					tmp.append(" ");
				tmp.append(m_dash[i]);
			}

			visitor.exportAttribute(XML_DASH, tmp.toString());
		}
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!(o instanceof CoDashIF))
			return false;

		CoDashIF d = (CoDashIF) o;

		return (m_join == d.getJoin())
			&& (m_cap == d.getCap())
			&& (m_miterLimit == d.getMiterLimit())
			&& (m_cycleLength == d.getCycleLength())
			&& (m_cycleLengthInWidth == d.isCycleLengthInWidth())
			&& (((m_dash == null) && (d.getDash() == null)) || m_dash.equals(d.getDash()));
	}

	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		CoDashIF dash = ((CoStrokeLayer) superModel).getDash();

		dash.setJoin(CoXmlUtilities.parseInt(node.getAttributes().getNamedItem(XML_JOIN), dash.getJoin()));

		dash.setCap(CoXmlUtilities.parseInt(node.getAttributes().getNamedItem(XML_CAP), dash.getCap()));

		dash.setCycleLength(CoXmlUtilities.parseFloat(node.getAttributes().getNamedItem(XML_CYCLE_LENGTH), dash.getCycleLength()));
		dash.setMiterLimit(CoXmlUtilities.parseFloat(node.getAttributes().getNamedItem(XML_MITER_LIMIT), dash.getMiterLimit()));

		dash.setCycleLengthInWidth(
			CoXmlUtilities
				.parseBoolean(node.getAttributes().getNamedItem(XML_CYCLE_LENGTH_IN_WIDTH), dash.isCycleLengthInWidth())
				.booleanValue());

		String tmp = CoXmlUtilities.parseString(node.getAttributes().getNamedItem(XML_DASH), null);
		if (tmp != null) {
			java.util.StringTokenizer t = new java.util.StringTokenizer(tmp, " ");
			int I = 0;
			while (t.hasMoreTokens()) {
				I++;
				t.nextToken();
			}

			float[] dashArray = new float[I];

			t = new java.util.StringTokenizer(tmp, " ");
			int i = 0;
			while (t.hasMoreTokens()) {
				float f = Float.valueOf(t.nextToken()).floatValue();
				dashArray[i++] = f;
			}

			dash.setDash(dashArray);
		}

		return dash;
	}

	public void xmlAddSubModel(String parameter, Object subModel, CoXmlContext context) throws CoXmlReadException {
	}

	public void xmlImportFinished(Node node, CoXmlContext context) throws CoXmlReadException {
	}
}