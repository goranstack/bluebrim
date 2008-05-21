package com.bluebrim.stroke.impl.shared;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.stroke.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Implementation of a stroke layer (see CoStroke).
 * 
 * @author: Dennis Malmström
 */

public class CoStrokeLayer extends CoSimpleObject implements CoStrokeLayerIF {
	public static final String XML_TAG = "stroke-layer";
	public static final String XML_WIDTH_PROPORTION = "width-proportion";

	private CoMarkDirtyListener m_owner;
	private CoDashIF m_dash;
	private float m_widthProportion = 0;
	private CoDashColorIF m_color;
	private boolean m_dirty;

	public CoStrokeLayer() {
		this(0);
	}

	public CoStrokeLayer(float width) {
		m_dash = new CoDash() {
			protected void markDirty() {
				super.markDirty();
				CoStrokeLayer.this.markDirty();
			}
		};

		m_widthProportion = width;
		m_color = CoForegroundDashColor.getInstance();
		//	( (CoStrokePropertiesFactoryIF) CoFactoryManager.getFactory( CoStrokePropertiesIF.STROKE_PROPERTIES ) ).getForegroundDashColor();
	}

	protected CoStrokeLayer(Node node, CoXmlContext context) {		
		this(CoXmlUtilities.parseFloat(node.getAttributes().getNamedItem(XML_WIDTH_PROPORTION), 0));
	}

	public CoStrokeLayerIF deepClone() {
		CoStrokeLayer strokeLayer = null;

		try {
			strokeLayer = (CoStrokeLayer) clone();
		} catch (CloneNotSupportedException ex) {
			CoAssertion.assertTrue(false, "CoStrokeLayer.clone() failed in CoStrokeLayer.deepClone()");
		}

		strokeLayer.m_color = m_color.deepClone();

		strokeLayer.m_dash = new CoDash(m_dash) {
			protected void markDirty() {
				super.markDirty();
				CoStrokeLayer.this.markDirty();
			}
		};

		return strokeLayer;
	}

	public CoDashColorIF getColor() {
		return m_color;
	}

	public CoDashIF getDash() {
		return m_dash;
	}

	public String getFactoryKey() {
		return null;
	}

	public float getWidthProportion() {
		return m_widthProportion;
	}

	protected void markDirty() {
		m_dirty = !m_dirty;
		if ( m_owner != null)
			m_owner.markDirty();
	}

	public CoAbsoluteDashColorIF setAbsoluteColor() {
		if (!(m_color instanceof CoAbsoluteDashColorIF)) {
			m_color = new CoAbsoluteDashColor() {
				protected void markDirty() {
					super.markDirty();
					CoStrokeLayer.this.markDirty();
				}
			};
		}

		return (CoAbsoluteDashColorIF) m_color;
	}

	public void setColor(CoDashColorIF color) {
		if (color.equals(CoForegroundDashColor.getInstance()))
			color = CoForegroundDashColor.getInstance();
		else
			if (color.equals(CoBackgroundDashColor.getInstance()))
				color = CoBackgroundDashColor.getInstance();
			else
				if (color.equals(CoNoDashColor.getInstance()))
					color = CoNoDashColor.getInstance();

		m_color = color;
		markDirty();
	}

	public void setWidthProportion(float p) {
		if (m_widthProportion == p)
			return;

		m_widthProportion = p;
		markDirty();
	}

	public void xmlVisit(CoXmlVisitorIF visitor) {
		visitor.exportAttribute(XML_WIDTH_PROPORTION, Float.toString(getWidthProportion()));

		visitor.export(getColor());
		visitor.export(getDash());
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!(o instanceof CoStrokeLayerIF))
			return false;

		CoStrokeLayerIF l = (CoStrokeLayerIF) o;
		return (m_widthProportion == l.getWidthProportion()) && (m_dash.equals(l.getDash())) && (m_color.equals(l.getColor()));
	}

	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		return new CoStrokeLayer(node, context);
	}
	
	public void xmlAddSubModel(String parameter, Object subModel, CoXmlContext context) throws CoXmlReadException {
		if (subModel instanceof CoDashColorIF) {
			setColor((CoDashColorIF) subModel);
		} else
			if (subModel instanceof CoDashIF) {
				m_dash = ((CoDashIF) subModel);
			}

	}

	public void xmlImportFinished(Node node, CoXmlContext context) throws CoXmlReadException {
	}

	public void setOwner(CoMarkDirtyListener owner) {
		m_owner = owner;
	}

}