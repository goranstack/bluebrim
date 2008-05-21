package com.bluebrim.xml.shared;

/**
 * This is an <code>Object</code> with a name.  It is used when reading
 * native Java types (like <code>Collection</code>s and <code>Number</code>s)
 * from XML.
 * <p>
 * Creation date: (2001-05-30 16:57:18)
 * 
 * @author Johan Walles
 */
public class CoNamedObject {
	private Object m_parentObject;
	private String m_name;
	private Object m_object;

	public CoNamedObject(Object parentObject, String name, Object object) {
		m_parentObject = parentObject;
		m_name = name;
		m_object = object;
	}

	public CoNamedObject(Object parentObject, String name) {
		this(parentObject, name, null);
	}

	public String getName() {
		return m_name;
	}

	public Object getObject() {
		return m_object;
	}

	public void setObject(Object object) {
		m_object = object;
	}

	public String toString() {
		StringBuffer returnMe = new StringBuffer();

		returnMe.append((m_name == null) ? "No name" : m_name);
		returnMe.append(": ");
		returnMe.append((m_object == null) ? "null" : m_object.toString());

		return returnMe.toString();
	}

	public Object getParentObject() {
		return m_parentObject;
	}

}