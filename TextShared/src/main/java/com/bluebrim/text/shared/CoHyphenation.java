package com.bluebrim.text.shared;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.browser.shared.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.transact.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * A hyphenation object is a named object collecting the state needed to
 * configure hyphenation behavior.
 * Currently this state consists of a linebreaker and a hint on what to do when the linebreaker fails.
 * 
 * @author: Dennis Malmström
 */

public class CoHyphenation extends CoObject implements CoCatalogElementIF, CoHyphenationIF {
	//	public static final String XML_LINE_BREAKER = "line-breaker";
	public static final String XML_FALLBACK_BEHAVIOR = "fallback-behavior";
	public static final String XML_NAME = "name";
	public static final String XML_TAG = "hyphenation";

	private String m_name;
	private CoLineBreakerIF m_lineBreaker;
	private CoEnumValue m_fallbackBehavior = CoStyleConstants.HYPHENATION_FALLBACK_BEHAVIOR_NON_BREAKPOINT;
	private boolean m_dirty;
	private Map m_lineBreakers = null; // [ String -> CoLineBreakerIF ], possible values for m_lineBreaker

	public CoHyphenation(String name) {
		m_name = name;
		setLineBreaker(CoLiangLineBreaker.LIANG_LINE_BREAKER);
	}

	private CoHyphenation(Node node, CoXmlContext context) {
		this(CoStringResources.getName(CoConstants.UNTITLED));
		NamedNodeMap attributes = node.getAttributes();
		m_name = CoXmlUtilities.parseString(attributes.getNamedItem(XML_NAME), m_name);
		String key = CoXmlUtilities.parseString(attributes.getNamedItem(XML_FALLBACK_BEHAVIOR), null);
		if (key != null)
			m_fallbackBehavior = CoEnumValue.getEnumValue(key);
		
	}

	private CoPropertyChangeListener m_propertyListener = new CoPropertyChangeListener() {
		public void propertyChange(CoPropertyChangeEvent ev) {
			markDirty();
		}
	};

	public boolean equals(Object o) {
		if (o instanceof CoHyphenationIF) {
			CoHyphenationIF h = (CoHyphenationIF) o;
			return m_name.equals(h.getName())
				&& m_fallbackBehavior.equals(h.getFallbackBehavior())
				&& m_lineBreaker.equals(h.getLineBreaker());

		} else {
			return super.equals(o);
		}
	}

	public String getFactoryKey() {
		return FACTORY_KEY;
	}

	public CoEnumValue getFallbackBehavior() {
		return m_fallbackBehavior;
	}

	public String getIconResourceAnchor() {
		return null;
	}

	public String getIdentity() {
		return getName();
	}

	public CoLineBreakerIF getLineBreaker() {
		return m_lineBreaker;
	}

	private CoLineBreakerIF getLineBreaker(String key) {
		if (m_lineBreakers == null) {
			m_lineBreakers = new HashMap();

			CoAbstractLineBreaker lb;

			lb = CoLiangLineBreaker.create();
			lb.addPropertyChangeListener(m_propertyListener);
			m_lineBreakers.put(CoLiangLineBreaker.LIANG_LINE_BREAKER, lb);

			lb = CoWordLineBreaker.create();
			lb.addPropertyChangeListener(m_propertyListener);
			m_lineBreakers.put(CoWordLineBreaker.BETWEEN_WORDS_LINE_BREAKER, lb);

			lb = CoAnywhereLineBreaker.create();
			lb.addPropertyChangeListener(m_propertyListener);
			m_lineBreakers.put(CoAnywhereLineBreaker.ANYWHERE_LINE_BREAKER, lb);
		}

		return (CoLineBreakerIF) m_lineBreakers.get(key);
	}

	public String getLineBreakerKey() {
		return m_lineBreaker.getFactoryKey();
	}

	public CoMutableLineBreakerIF getMutableLineBreaker() {
		return m_lineBreaker.getMutableProxy();
	}

	public String getName() {
		return m_name;
	}

	public String getType() {
		return getFactoryKey();
	}

	private void markDirty() {
		m_dirty = !m_dirty;

		if (CoAssertion.SIMULATION_SUPPORT)
			CoAssertion.addChangedObject(this);
	}

	public void setFallbackBehavior(CoEnumValue v) {
		if (!m_fallbackBehavior.equals(v)) {
			m_fallbackBehavior = v;
			markDirty();
		}
	}

	public final void setLineBreaker(String lineBreakerKey) {
		setLineBreaker(getLineBreaker(lineBreakerKey));
	}

	public void setLineBreaker(CoLineBreakerIF lb) {
		CoAssertion.assertTrue(lb != null, "Attempt to set LineBreaker = null");
		m_lineBreaker = lb;
		markDirty();
	}

	public void setName(String name) {
		m_name = name;
		markDirty();
	}

	public void xmlVisit(CoXmlVisitorIF visitor) {
		visitor.exportAttribute(XML_NAME, m_name);
		visitor.exportAttribute(XML_FALLBACK_BEHAVIOR, m_fallbackBehavior.toString());
		visitor.export(m_lineBreaker);
	}

	public void xmlAddSubModel(String name, Object subModel, CoXmlContext context) {
		if (subModel instanceof CoLineBreakerIF) {
			setLineBreaker((CoLineBreakerIF) subModel);
		}

	}

	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		return new CoHyphenation(node, context);
	}

	/**
	 * This method is called after an object and all its sub-objects have
	 * been read from an XML file.
	 * <p>
	 * Creation date: (2001-08-28 16:04:13)
	 */
	public void xmlImportFinished(Node node, CoXmlContext context) throws CoXmlReadException {
	}

	// Return identity

	public final CoRef getId() {
		return CoRef.to(this);
	}
}