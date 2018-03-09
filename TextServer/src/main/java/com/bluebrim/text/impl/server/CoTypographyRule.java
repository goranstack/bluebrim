package com.bluebrim.text.impl.server;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.impl.shared.xtg.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2000-10-31 10:28:40)
 * @author: Dennis
 */

public class CoTypographyRule extends CoObject implements CoTypographyRuleIF {
	public static final String XML_CHARACTER_STYLES = "character-styles";
	public static final String XML_PARAGRAPH_STYLES = "paragraph-styles";
	public static final String XML_TAG = "typography";

	private boolean m_dirty;

	private List m_characterStyles = new ArrayList(); // [ CoCharacterStyleIF ]
	private List m_paragraphStyles = new ArrayList(); // [ CoParagraphStyleIF ]

	private static Key m_key;

	private static class Key extends CoCharacterStyle {
		public String m_name;

		public String getName() {
			return m_name;
		}
	}

	public void clear() {
		m_characterStyles.clear();
		m_paragraphStyles.clear();

		markDirty();
	}

	public CoCharacterStyleIF getCharacterStyle(String name) {
		return find(name, m_characterStyles);
	}
	public List getCharacterStyles() {
		return m_characterStyles;
	}
	public String getFactoryKey() {
		return FACTORY_KEY;
	}
	public CoParagraphStyleIF getParagraphStyle(String name) {
		return (CoParagraphStyleIF) find(name, m_paragraphStyles);
	}
	public List getParagraphStyles() {
		return m_paragraphStyles;
	}
	protected void markDirty() {
		m_dirty = !m_dirty;

		if (com.bluebrim.base.shared.debug.CoAssertion.SIMULATION_SUPPORT)
			com.bluebrim.base.shared.debug.CoAssertion.addChangedObject(this);
	}
	public String readFromXtg(java.io.InputStream s) {
		CoXtgParser p = new CoXtgParser(new java.io.InputStreamReader(s));

		if (p.parse()) {
			p.extract(this);

			markDirty();
		}

		return p.getLog();
	}
	public void removeCharacterStyle(String name) {
		CoCharacterStyle p = find(name, m_characterStyles);
		if (p != null) {
			m_characterStyles.remove(p);

			Iterator i = m_characterStyles.iterator();
			while (i.hasNext()) {
				CoCharacterStyleIF p2 = (CoCharacterStyleIF) i.next();
				if (p2.getBasedOn() == p) {
					p2.setBasedOn(null);
				}
			}

			markDirty();
		}
	}

	public void removeParagraphStyle(String name) {
		CoParagraphStyle p = (CoParagraphStyle) find(name, m_paragraphStyles);
		if (p != null) {
			m_paragraphStyles.remove(p);

			Iterator i = m_paragraphStyles.iterator();
			while (i.hasNext()) {
				CoParagraphStyleIF p2 = (CoParagraphStyleIF) i.next();
				if (p2.getBasedOn() == p) {
					p2.setBasedOn(null);
				}
			}

			markDirty();
		}
	}

	public void removeStyles(List styles) {
		m_characterStyles.removeAll(styles);
		m_paragraphStyles.removeAll(styles);

		Iterator i = m_characterStyles.iterator();
		while (i.hasNext()) {
			CoCharacterStyleIF p2 = (CoCharacterStyleIF) i.next();
			if (styles.contains(p2.getBasedOn())) {
				p2.setBasedOn(null);
			}
		}

		i = m_paragraphStyles.iterator();
		while (i.hasNext()) {
			CoCharacterStyleIF p2 = (CoCharacterStyleIF) i.next();
			if (styles.contains(p2.getBasedOn())) {
				p2.setBasedOn(null);
			}
		}

		markDirty();
	}

	public CoCharacterStyleIF addCharacterStyle(String name) {
		if (find(name, m_characterStyles) != null) {
			return addCharacterStyle(name + "_");
		}

		CoCharacterStyle p = createCharacterStyle(name);

		m_characterStyles.add(p);
		markDirty();

		return p;
	}

	public CoParagraphStyleIF addParagraphStyle(String name) {
		if (find(name, m_paragraphStyles) != null) {
			return addParagraphStyle(name + "_");
		}

		CoParagraphStyle p = createParagraphStyle(name);

		m_paragraphStyles.add(p);
		markDirty();

		return p;
	}

	private CoCharacterStyle createCharacterStyle(String name) {
		//	System.err.println( "# CCC #" + name );

		CoCharacterStyle p = new CoCharacterStyle(name) {
			public void setName(String innerName) throws CoCharacterStyleIF.NameNotUniqueException {
				if (find(innerName, m_characterStyles) != null) {
					throw new CoCharacterStyleIF.NameNotUniqueException();
				}
				super.setName(innerName);
			}
		};

		return p;
	}

	private CoParagraphStyle createParagraphStyle(String name) {
		//	System.err.println( "# ppp #" + name );

		CoParagraphStyle p = new CoParagraphStyle(name) {
			public void setName(String innerName) throws CoCharacterStyleIF.NameNotUniqueException {
				if (find(innerName, m_paragraphStyles) != null) {
					throw new CoCharacterStyleIF.NameNotUniqueException();
				}
				super.setName(innerName);
			}
		};

		return p;
	}

	private static CoCharacterStyle find(String name, List l) {
		if (name == null)
			return null;

		if (m_key == null) {
			m_key = new Key();
		}

		m_key.m_name = name;

		int i = l.indexOf(m_key);

		return (i == -1) ? null : (CoCharacterStyle) l.get(i);
	}

	public CoTypographyRule() {
	}

	public boolean add(CoCharacterStyleIF s) {
		if (find(s.getName(), m_characterStyles) != null) {
			return false;
		}

		m_characterStyles.add(s);
		markDirty();

		return true;
	}

	public boolean add(CoParagraphStyleIF s) {
		if (find(s.getName(), m_paragraphStyles) != null) {
			return false;
		}

		m_paragraphStyles.add(s);
		markDirty();

		return true;
	}

	public void copyFrom(CoTypographyRuleIF r) {
		clear();

		Iterator i = r.getCharacterStyles().iterator();
		while (i.hasNext()) {
			CoCharacterStyleIF cs = (CoCharacterStyleIF) i.next();
			addCharacterStyle(cs.getName()).setAttributes(cs.getAttributes());
		}

		i = r.getParagraphStyles().iterator();
		while (i.hasNext()) {
			CoParagraphStyleIF ps = (CoParagraphStyleIF) i.next();
			addParagraphStyle(ps.getName()).setAttributes(ps.getAttributes());
		}

		markDirty();
	}

	public void copyFrom(CoTypographyRule r) {

	}

	public void xmlAddSubModel(String name, Object subModel, CoXmlContext context) {
		context.putValue(CoTypographyRuleIF.class, this);
/* 
 * Since CoCharacterStyle and CoParagraphStyle calls our add-method that both creates
 * and the style this code will add the styles a second time. I'm not sure this use
 * of the XML framwork is good. Leave this code as comment if it will be used again.
 * 
		if (XML_CHARACTER_STYLES.equals(name)) {
			Iterator i = (Iterator) subModel;
			while (i.hasNext()) {
				m_characterStyles.add(i.next());
			}

		} else
			if (XML_PARAGRAPH_STYLES.equals(name)) {
				Iterator i = (Iterator) subModel;
				while (i.hasNext()) {
					m_paragraphStyles.add(i.next());
				}
			}
*/
			
	}

	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		return new CoTypographyRule();
	}

	public void xmlVisit(CoXmlVisitorIF visitor) {
		visitor.export(XML_CHARACTER_STYLES, m_characterStyles);
		visitor.export(XML_PARAGRAPH_STYLES, m_paragraphStyles);
	}

	/**
	 * This method is called after an object and all its sub-objects have
	 * been read from an XML file.
	 * <p>
	 * Creation date: (2001-08-28 16:20:31)
	 */
	public void xmlImportFinished(Node node, CoXmlContext context) throws CoXmlReadException {
	}

	public CoTypographyRuleIF deepClone() {
		CoTypographyRule r = new CoTypographyRule();

		Iterator i = m_characterStyles.iterator();
		while (i.hasNext()) {
			CoCharacterStyle s = (CoCharacterStyle) i.next();

			CoCharacterStyle p = r.createCharacterStyle(s.getName());
			p.setAttributes(s.getAttributes());
			r.m_characterStyles.add(p);
		}

		i = m_paragraphStyles.iterator();
		while (i.hasNext()) {
			CoParagraphStyle s = (CoParagraphStyle) i.next();

			CoParagraphStyle p = r.createParagraphStyle(s.getName());
			p.setAttributes(s.getAttributes());
			r.m_paragraphStyles.add(p);
		}

		return r;
	}
}