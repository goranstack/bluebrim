package com.bluebrim.resource.shared;

import java.awt.event.*;
import java.util.*;

import javax.swing.*;

/**
 * A "better" resource bundle supporting the use of other
 * keys than strings, typically <b>typesafe enums</b>.
 * Suitably adapted typesafe enums provide good encapsulation
 * of resource management. Unfortunately, classes such as
 * ResourceBundle assume all keys are strings ...
 * 
 * For info on typesafe enums, see <a href="http://developer.java.sun.com/developer/Books/shiftintojava/page1.html">
 * Substitutes for Missing C Constructs</a> from the book <em>Shift Into Java</em>
 * by Joshua Bloch and <a href="http://www.javaworld.com/javaworld/javatips/jw-javatip122.html">
 * Java Tip 122</a>.
 * 
 * Implementation-wise this is for now basically a copy of
 * ListResourceBundle. Although we do not use string keys,
 * we take advantage of some special code in ResourceBundle.
 * It could be imitated here with slight modifications and
 * perhaps it will be in the future.
 * 
 * @author Markus Persson 2002-05-31
 */
public abstract class CoResources extends ResourceBundle {
 	/** Convenience constants for subclasses defining key strokes. */
	protected static final int SHIFT = InputEvent.SHIFT_DOWN_MASK;
	protected static final int CTRL = InputEvent.CTRL_DOWN_MASK;
	protected static final int META = InputEvent.META_DOWN_MASK;
	protected static final int ALT = InputEvent.ALT_DOWN_MASK;
	protected static final int BUTTON1 = InputEvent.BUTTON1_DOWN_MASK;
	protected static final int BUTTON2 = InputEvent.BUTTON2_DOWN_MASK;
	protected static final int BUTTON3 = InputEvent.BUTTON3_DOWN_MASK;
	protected static final int ALT_GRAPH = InputEvent.ALT_GRAPH_DOWN_MASK;

	private Map m_lookup = null;

	/**
	 * Sole constructor. (For invocation by subclass constructors,
	 * typically implicit.)
	 */
	protected CoResources() {
	}

	protected static final CoResources getBundle(Class baseClass) {
		return (CoResources) getBundle(baseClass.getName());
	}

	/**
	 * Convenience method for subclasses defining key strokes.
	 * @author Markus Persson 2002-06-11
	 */
	protected static final KeyStroke keyStroke(int keyCode, int modifiers) {
		return KeyStroke.getKeyStroke(keyCode, modifiers);
	}

	/**
	 * Convenience method for subclasses defining menu items.
	 * @author Markus Persson 2002-10-31
	 */
	protected static final CoUnlocalizedMenuItemResource menuItem(String text, char mnemonic, KeyStroke accelerator) {
		return new CoUnlocalizedMenuItemResource(text, mnemonic, accelerator);
	}

	/**
	 * Convenience method for subclasses defining menu items.
	 * @author Markus Persson 2002-10-31
	 */
	protected static final CoUnlocalizedMenuItemResource menuItem(String text, char mnemonic) {
		return new CoUnlocalizedMenuItemResource(text, mnemonic, null);
	}

	/**
	 * Gets an object for the given key from this resource bundle or one of its parents.
	 * This method first tries to obtain the object from this resource bundle using
	 * {@link #handleGetObject(java.lang.String) handleGetObject}.
	 * If not successful, and the parent resource bundle is not null,
	 * it calls the parent's <code>getObject</code> method.
	 * If still not successful, it throws a MissingResourceException.
	 *
	 * @param key the key for the desired object
	 * @exception NullPointerException if <code>key</code> is <code>null</code>
	 * @exception MissingResourceException if no object for the given key can be found
	 * @return the object for the given key
	 */
	public final Object get(Object key) {
		// lazily load the lookup hashtable.
		if (m_lookup == null) {
			loadLookup();
		}
		Object obj = m_lookup.get(key);
		if (obj == null) {
			if (parent != null) {
				obj = ((CoResources) parent).get(key);
			}
			if (obj == null) {
				if (key == null) {
					throw new NullPointerException();
				} else {
					throw new MissingResourceException("No resource found.", this.getClass().getName(), key.toString());
				}
			}
		}
		return obj;
	}

	public final String getStr(Object key) {
		return (String) get(key);
	}

	public char getChar(Object key) {
		return getStr(key).charAt(0);
	}

	/**
	 * Disallow this string method?
	 */
	public final Object handleGetObject(String key) {
		// lazily load the lookup hashtable.
		if (m_lookup == null) {
			loadLookup();
		}
		if (key == null) {
			throw new NullPointerException();
		}
		return m_lookup.get(key); // this class ignores locales
	}

	/**
	 * Implementation of ResourceBundle.getKeys.
	 * 
	 * NOTE: Actually we don't need this method, so we could throw
	 * UnsupportedMethodException and remove the nested class used
	 * here. Remove if not inheriting ResourceBundle.
	 */
	public Enumeration getKeys() {
		// lazily load the lookup hashtable.
		if (m_lookup == null) {
			loadLookup();
		}

		ResourceBundle parent = this.parent;
		return new ResourceBundleEnumeration(m_lookup.keySet(), (parent != null) ? parent.getKeys() : null);
	}

	/**
	 * See class description.
	 */
	abstract protected Object[][] getContents();

	/**
	 * We lazily load the lookup hashtable.  This function does the
	 * loading.
	 */
	private synchronized void loadLookup() {
		if (m_lookup != null)
			return;

		Object[][] contents = getContents();
		HashMap temp = new HashMap(contents.length);
		for (int i = 0; i < contents.length; ++i) {
			// key and value must be non-null.
			Object key = contents[i][0];
			Object value = contents[i][1];
			if (key == null || value == null) {
				throw new NullPointerException();
			}
			Object existing = temp.put(key, value);
			if (existing != null) {
				System.out.println(
					"WARNING("
						+ getClass().getName()
						+ "): Duplicate entries for key '"
						+ key
						+ "'. Old entry: '"
						+ existing
						+ "'. New entry: '"
						+ value
						+ "'");
			}
		}
		m_lookup = temp;
	}

	/**
	 * Straight copy from package private class in java.util from JDK 1.4.
	 */
	private static class ResourceBundleEnumeration implements Enumeration {

		Set set;
		Iterator iterator;
		Enumeration enumeration; // may remain null

		/**
		 * Constructs a resource bundle enumeration.
		 * @param set an set providing some elements of the enumeration
		 * @param enumeration an enumeration providing more elements of the enumeration.
		 *        enumeration may be null.
		 */
		ResourceBundleEnumeration(Set set, Enumeration enumeration) {
			this.set = set;
			this.iterator = set.iterator();
			this.enumeration = enumeration;
		}

		Object next = null;

		public boolean hasMoreElements() {
			if (next == null) {
				if (iterator.hasNext()) {
					next = iterator.next();
				} else if (enumeration != null) {
					while (next == null && enumeration.hasMoreElements()) {
						next = enumeration.nextElement();
						if (set.contains(next)) {
							next = null;
						}
					}
				}
			}
			return next != null;
		}

		public Object nextElement() {
			if (hasMoreElements()) {
				Object result = next;
				next = null;
				return result;
			} else {
				throw new NoSuchElementException();
			}
		}
	}
}