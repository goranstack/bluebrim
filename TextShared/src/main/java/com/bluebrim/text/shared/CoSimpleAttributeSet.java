package com.bluebrim.text.shared;
import java.io.*;
import java.util.*;

import javax.swing.text.*;
/**
 * A copy of javax.swing.text.SimpleAttributeSet.
 *
 * Added just to make <code>table</code> non-transient so instances
 * of this class can be made persistent in GemStone/J.
 *
 * Moved, renamed and added constructor to avoid trivial subclasses.
 * /Markus
 *
 * @author Lasse S 1999-10-17
 * @author Markus Persson 2001-09-03
 */
public class CoSimpleAttributeSet implements MutableAttributeSet, Serializable, Cloneable {
	private Hashtable table = new Hashtable(3);

	public static final AttributeSet EMPTY = new EmptyAttributeSet();
	static public final Enumeration EMPTY_ENUMERATION = new Enumeration() {
		public boolean hasMoreElements() {
			return false;
		}
		public Object nextElement() {
			throw new NoSuchElementException("No more elements");
		}
	};

	static class EmptyAttributeSet implements AttributeSet, Serializable {
		public int getAttributeCount() {
			return 0;
		}
		public boolean isDefined(Object attrName) {
			return false;
		}
		public boolean isEqual(AttributeSet attr) {
			return (attr.getAttributeCount() == 0);
		}
		public AttributeSet copyAttributes() {
			return this;
		}
		public Object getAttribute(Object key) {
			return null;
		}
		public Enumeration getAttributeNames() {
			return EMPTY_ENUMERATION;
		}
		public boolean containsAttribute(Object name, Object value) {
			return false;
		}
		public boolean containsAttributes(AttributeSet attributes) {
			return (attributes.getAttributeCount() == 0);
		}
		public AttributeSet getResolveParent() {
			return null;
		}
	};

;

	public CoSimpleAttributeSet() {
	}

	/**
	 * Convienience constructor for use from CoTextConstants ...
	 *
	 * @author Markus Persson 2001-09-03
	 */
	public CoSimpleAttributeSet(String[] names) {
		for (int i = 0; i < names.length; i++) {
			addAttribute(names[i], names[i]);
		}
	}

	private CoSimpleAttributeSet(Hashtable table) {
		this.table = table;
	}

	public CoSimpleAttributeSet(AttributeSet source) {
		addAttributes(source);
	}

	public void addAttribute(Object name, Object value) {
		table.put(name, value);
	}

	public void addAttributes(AttributeSet attributes) {
		Enumeration names = attributes.getAttributeNames();
		while (names.hasMoreElements()) {
			Object name = names.nextElement();
			addAttribute(name, attributes.getAttribute(name));
		}
	}

	public Object clone() {
		CoSimpleAttributeSet copy;
		try {
			copy = (CoSimpleAttributeSet) super.clone();
			copy.table = (Hashtable) table.clone();
		} catch (CloneNotSupportedException cnse) {
			copy = null;
		}
		return copy;
	}

	public boolean containsAttribute(Object name, Object value) {
		return value.equals(getAttribute(name));
	}

	public boolean containsAttributes(AttributeSet attributes) {
		boolean result = true;
		Enumeration names = attributes.getAttributeNames();
		while (result && names.hasMoreElements()) {
			Object name = names.nextElement();
			result = attributes.getAttribute(name).equals(getAttribute(name));
		}
		return result;
	}

	public AttributeSet copyAttributes() {
		return (AttributeSet) clone();
	}

	public boolean equals(Object obj) {
		if (obj instanceof AttributeSet) {
			AttributeSet attrs = (AttributeSet) obj;
			return isEqual(attrs);
		}
		return false;
	}

	public Object getAttribute(Object name) {
		Object value = table.get(name);
		if (value == null) {
			AttributeSet parent = getResolveParent();
			if (parent != null) {
				value = parent.getAttribute(name);
			}
		}
		return value;
	}

	public int getAttributeCount() {
		return table.size();
	}

	public Enumeration getAttributeNames() {
		return table.keys();
	}

	public AttributeSet getResolveParent() {
		return (AttributeSet) table.get(StyleConstants.ResolveAttribute);
	}

	public int hashCode() {
		return table.hashCode();
	}

	public boolean isDefined(Object attrName) {
		return table.containsKey(attrName);
	}

	public boolean isEmpty() {
		return table.isEmpty();
	}

	public boolean isEqual(AttributeSet attr) {
		return ((getAttributeCount() == attr.getAttributeCount()) && containsAttributes(attr));
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
		s.defaultReadObject();
		table = new Hashtable(3);
		StyleContext.readAttributeSet(s, this);
	}

	public void removeAttribute(Object name) {
		table.remove(name);
	}

	public void removeAttributes(Enumeration names) {
		while (names.hasMoreElements())
			removeAttribute(names.nextElement());
	}

	public void removeAttributes(AttributeSet attributes) {
		Enumeration names = attributes.getAttributeNames();
		while (names.hasMoreElements()) {
			Object name = names.nextElement();
			Object value = attributes.getAttribute(name);
			if (value.equals(getAttribute(name)))
				removeAttribute(name);
		}
	}

	public void setResolveParent(AttributeSet parent) {
		addAttribute(StyleConstants.ResolveAttribute, parent);
	}

	public String toString() {
		String s = "";
		Enumeration names = getAttributeNames();
		while (names.hasMoreElements()) {
			Object key = names.nextElement();
			Object value = getAttribute(key);
			if (value instanceof AttributeSet) {
				// don't go recursive
				s = s + key + "=**AttributeSet** ";
			} else {
				s = s + key + "=" + value + " ";
			}
		}
		return s;
	}

	private void writeObject(java.io.ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
		StyleContext.writeAttributeSet(s, this);
	}
}