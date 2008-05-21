package com.bluebrim.text.impl.shared;

import java.io.*;
import java.util.*;

import javax.swing.text.*;

import org.w3c.dom.*;

import com.bluebrim.observable.*;
import com.bluebrim.resource.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Implemenation of a linebreaker that allows breaking according to the Liang algorithm.
 * 
 * @author Dennis Malmström
 */
public class CoLiangLineBreaker extends CoAbstractLineBreaker implements CoLiangLineBreakerIF {
	public static final String XML_MINIMUM_PREFIX = "minimum-prefix";
	public static final String XML_MINIMUM_SUFFIX = "maximum-suffix";
	public static final String XML_MINIMUM_WORD = "minimum-word";
	public static final String XML_TAG = "liang-line-breaker";

	// property names
	public static final String MINIMUM_PREFIX_PROPERTY = "CoLiangLineBreaker.MINIMUM_PREFIX_PROPERTY";
	public static final String MINIMUM_SUFFIX_PROPERTY = "CoLiangLineBreaker.MINIMUM_SUFFIX_PROPERTY";
	public static final String MINIMUM_WORD_PROPERTY = "CoLiangLineBreaker.MINIMUM_WORD_PROPERTY";

	// algorithm parameters (Same as QXP's Standard H&J)
	private int m_minimumPrefixLength = 3;
	private int m_minimumSuffixLength = 2;
	private int m_minimumWordLength = 6;

	private CoHyphenationPatternCollectionIF m_customPatterns = null; // custom breakpoint patterns

	private transient CoLiangTree m_tree; // structure containing the liang patterns

	private transient CoLiangBreakPointIterator m_iterator;

	// mutable proxy
	protected class MutableProxy extends CoAbstractLineBreaker.MutableProxy implements CoRemoteLiangLineBreakerIF {
		public int getMinimumPrefixLength() {
			return CoLiangLineBreaker.this.getMinimumPrefixLength();
		}
		public int getMinimumSuffixLength() {
			return CoLiangLineBreaker.this.getMinimumSuffixLength();
		}
		public int getMinimumWordLength() {
			return CoLiangLineBreaker.this.getMinimumWordLength();
		}

		public void setMinimumPrefixLength(int l) {
			CoLiangLineBreaker.this.setMinimumPrefixLength(l);
		}
		public void setMinimumSuffixLength(int l) {
			CoLiangLineBreaker.this.setMinimumSuffixLength(l);
		}
		public void setMinimumWordLength(int l) {
			CoLiangLineBreaker.this.setMinimumWordLength(l);
		}
	};

	public static CoLiangLineBreaker create() {
		return new CoLiangLineBreaker();
	}
	protected CoMutableLineBreakerIF createMutableProxy() {
		return new MutableProxy();
	}
	public boolean equals(Object o) {
		if (o instanceof CoLiangLineBreakerIF) {
			CoLiangLineBreakerIF b = (CoLiangLineBreakerIF) o;
			return m_minimumPrefixLength == b.getMinimumPrefixLength()
				&& m_minimumSuffixLength == b.getMinimumSuffixLength()
				&& m_minimumWordLength == b.getMinimumWordLength();
		} else {
			return super.equals(o);
		}
	}
	public CoLineBreakerIF.BreakPointIteratorIF getBreakPoints(Segment text) {
		// Note: The lazy initialization below is not a problem transaction-wise.
		//       This method can't be called on a server object because the return value isn't rmi-capable.
		if (m_tree == null)
			refreshTree();
		if (m_iterator == null)
			m_iterator = new CoLiangBreakPointIterator();

		m_iterator.set(m_tree, text, m_minimumPrefixLength, m_minimumSuffixLength, m_minimumWordLength);

		return m_iterator;
	}
	public java.lang.String getFactoryKey() {
		return LIANG_LINE_BREAKER;
	}
	public int getMinimumPrefixLength() {
		return m_minimumPrefixLength;
	}
	public int getMinimumSuffixLength() {
		return m_minimumSuffixLength;
	}
	public int getMinimumWordLength() {
		return m_minimumWordLength;
	}
	public String getUIname() {
		return "com.bluebrim.text.impl.client.CoLiangLineBreakerUI";
	}
	private void refreshTree() {
		if (m_customPatterns == null) {
			// get custom patterns
			try {
				m_customPatterns = CoTextServerProvider.getTextServer().getHyphenationPatterns();
			} catch (Exception npex) {
				m_tree = null;
				return;
			}

			CoChangedObjectListener l = new CoChangedObjectListener() {
				public void serverObjectChanged(CoChangedObjectEvent e) {
					m_tree = null; // invalidate tree when custom pattens change
				}
			};

			CoObservable.addChangedObjectListener(l, m_customPatterns);
		}

		Collection c = m_customPatterns.getPatterns();

		try {
			// get installed patterns
			InputStream s = CoResourceLoader.getStream(getClass(), "sehyph.tex");
			if (s == null)
				throw new IOException("Can't find resource sehyph.tex with resource anchor: " + getClass().getName());

			// create tree
			m_tree = new CoLiangTree(s, c);
		} catch (IOException ex) {
			System.err.println("Hyphenation pattern file could not be read :" + ex.getMessage());
		}

	}
	public void setMinimumPrefixLength(int m) {
		m_minimumPrefixLength = m;

		firePropertyChange(MINIMUM_PREFIX_PROPERTY, Boolean.TRUE, Boolean.FALSE);
	}
	public void setMinimumSuffixLength(int m) {
		m_minimumSuffixLength = m;

		firePropertyChange(MINIMUM_SUFFIX_PROPERTY, Boolean.TRUE, Boolean.FALSE);
	}
	public void setMinimumWordLength(int m) {
		m_minimumWordLength = m;

		firePropertyChange(MINIMUM_WORD_PROPERTY, Boolean.TRUE, Boolean.FALSE);
	}

	private CoLiangLineBreaker() {
	}

	public void xmlVisit(CoXmlVisitorIF visitor) {
		visitor.exportAttribute(XML_MINIMUM_PREFIX, Integer.toString(m_minimumPrefixLength));
		visitor.exportAttribute(XML_MINIMUM_SUFFIX, Integer.toString(m_minimumSuffixLength));
		visitor.exportAttribute(XML_MINIMUM_WORD, Integer.toString(m_minimumWordLength));
	}

	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		CoLiangLineBreaker lb = create();

		lb.m_minimumPrefixLength =
			CoXmlUtilities.parseInt(node.getAttributes().getNamedItem(XML_MINIMUM_PREFIX).getNodeValue(), lb.m_minimumPrefixLength);
		lb.m_minimumSuffixLength =
			CoXmlUtilities.parseInt(node.getAttributes().getNamedItem(XML_MINIMUM_SUFFIX).getNodeValue(), lb.m_minimumSuffixLength);
		lb.m_minimumWordLength =
			CoXmlUtilities.parseInt(node.getAttributes().getNamedItem(XML_MINIMUM_WORD).getNodeValue(), lb.m_minimumWordLength);

		return lb;
	}
}