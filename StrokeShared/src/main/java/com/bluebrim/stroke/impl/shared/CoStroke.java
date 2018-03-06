package com.bluebrim.stroke.impl.shared;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.stroke.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.transact.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Implementation of a named, multi-layered stroke.
 * 
 * @author: Dennis Malmström
 */

public class CoStroke extends CoSimpleObject implements CoStrokeIF, CoMarkDirtyListener {
	public static final String XML_GOI = "goi";
	public static final String XML_LAYERS = "layers";
	public static final String XML_NAME = "name";
	public static final String XML_TAG = "stroke";

	private CoMarkDirtyListener m_owner;
	private List m_strokeLayers = new ArrayList(); // [ CoStrokeLayerIF ]
	private String m_name = CoStringResources.getName(CoConstants.UNTITLED);
	private boolean m_dirty;
	private CoGOI m_goi;

	public CoStroke() {
		this(CoRefService.createGOI());
	}
	
	private CoStroke(CoGOI goi) {
		m_goi = goi;
		m_strokeLayers.add(createStrokeLayer(1));
	}

	protected CoStroke(Node node, CoXmlContext context) {
		this();
		NamedNodeMap attributes = node.getAttributes();
		
		m_name = CoXmlUtilities.parseString(attributes.getNamedItem(XML_NAME), m_name);
		String tmp = CoXmlUtilities.parseString(attributes.getNamedItem(XML_GOI), null);
		if (tmp != null)
			m_goi = new CoGOI(tmp);
	}


	public CoStrokeLayerIF add() {
		CoStrokeLayerIF strokeLayer = createStrokeLayer(0);
		m_strokeLayers.add(strokeLayer);
		markDirty();
		return strokeLayer;
	}

	public void clear() {
		m_strokeLayers.clear();
		markDirty();
	}

	private CoStrokeLayer createStrokeLayer(float width) {
		CoStrokeLayer strokeLayer = new CoStrokeLayer(width);
		strokeLayer.setOwner(this);
		return strokeLayer;
	}

	public CoStrokeIF deepClone() {
		CoStroke c = null;

		try {
			c = (CoStroke) clone();
		} catch (CloneNotSupportedException ex) {
			CoAssertion.assertTrue(false, "CoStroke.clone() failed in CoStroke.deepClone()");
		}

		c.m_strokeLayers = new ArrayList();

		Iterator e = m_strokeLayers.iterator();
		while (e.hasNext()) {
			c.m_strokeLayers.add(((CoStrokeLayerIF) e.next()).deepClone()); // PENDING: bug bug bug: must use c.createStrokeLayer
		}

		return c;
	}

	public CoStrokeLayerIF get(int i) {
		return (CoStrokeLayerIF) m_strokeLayers.get(i);
	}

	public int getCount() {
		return m_strokeLayers.size();
	}

	public String getFactoryKey() {
		return FACTORY_KEY;
	}

	public String getName() {
		return m_name;
	}

	public int indexOf(CoStrokeLayerIF ss) {
		return m_strokeLayers.indexOf(ss);
	}

	public CoStrokeLayerIF insert(int i) {
		CoStrokeLayerIF ss = createStrokeLayer(0);
		m_strokeLayers.add(i, ss);
		markDirty();
		return ss;
	}

	public boolean isRenameable() {
		return true;
	}

	public void markDirty() {
		m_dirty = !m_dirty;
		if (m_owner != null)
			m_owner.markDirty();
	}

	public void normalize() {
		float W = 0;
		int c = 0;

		int I = m_strokeLayers.size();
		for (int i = 0; i < I; i++) {
			CoStrokeLayerIF ss = (CoStrokeLayerIF) m_strokeLayers.get(i);
			float w = ss.getWidthProportion();
			W += w;
			if (w == 0)
				c++;
		}

		if (c == 0) {
			for (int i = 0; i < I; i++) {
				CoStrokeLayerIF ss = (CoStrokeLayerIF) m_strokeLayers.get(i);
				float w = ss.getWidthProportion();
				ss.setWidthProportion(w / W);
			}
		} else {
			float dw = 0;
			W = ((int) (0.5 + W * 100)) / 100f;
			if (W < 1) {
				dw = (1 - W) / c;
			} else {
				dw = 1 / (float) I;
			}
			float s = (1 - c * dw) / W;
			for (int i = 0; i < I; i++) {
				CoStrokeLayerIF ss = (CoStrokeLayerIF) m_strokeLayers.get(i);
				float w = ss.getWidthProportion();
				if (w == 0) {
					w = dw;
				} else {
					w = w * s;
				}
				ss.setWidthProportion(w);
			}
		}
	}

	public void remove(int i) {
		m_strokeLayers.remove(i);
		markDirty();
	}

	public void remove(CoStrokeLayerIF ss) {
		m_strokeLayers.remove(ss);
		markDirty();
	}

	public void setName(String name) {
		if (m_name.equals(name))
			return;

		m_name = name;
		markDirty();
	}

	public String toString() {
		return getName();
	}

	public void xmlVisit(CoXmlVisitorIF visitor) {
		visitor.exportAttribute(XML_NAME, getName());
		visitor.exportGOIAttribute(XML_GOI, m_goi);
		visitor.export(XML_LAYERS, m_strokeLayers);
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!(o instanceof CoStrokeIF))
			return false;

		CoStrokeIF s = (CoStrokeIF) o;

		if (!m_name.equals(s.getName()))
			return false;

		int I = m_strokeLayers.size();

		if (I != s.getCount())
			return false;

		for (int i = 0; i < I; i++) {
			if (!get(i).equals(s.get(i))) {
				return false;
			}
		}

		return true;
	}

	public CoStroke(String singletonId) {
		this(new CoGOI(new CoSpecificContext("strokeSingleton", "anySystem", singletonId), 0));
	}

	public long getCOI() {
		return m_goi.getCoi();
	}

	public CoGOI getGOI() {
		return m_goi;
	}

	public boolean isMutable() {
		return true;
	}

	public void xmlAddSubModel(String parameter, Object subModel, CoXmlContext context) throws CoXmlReadException {
		if (parameter.equals(XML_LAYERS)) {
			m_strokeLayers.clear();
			Iterator i = (Iterator) subModel;
			while (i.hasNext()) {
				m_strokeLayers.add(i.next());
			}

		}
	}

	/**
	 * Called at XML-import by reflexion from the XML framework
	 */
	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) throws CoXmlReadException {
		return new CoStroke(node, context);
	}
	
	/**
	 * This method is called after an object and all its sub-objects have
	 * been read from an XML file.
	 * <p>
	 * Creation date: (2001-08-28 14:06:50)
	 */
	public void xmlImportFinished(Node node, CoXmlContext context) throws CoXmlReadException {
	}

	public void setOwner(CoMarkDirtyListener owner) {
		m_owner = owner;
	}

}