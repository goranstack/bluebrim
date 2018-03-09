package com.bluebrim.xml.shared;
import org.w3c.dom.*;

/**
 * This is a wrapper class for reading a {@link java.util.Map.Entry} from XML.
 * <p>
 * Creation date: (2001-06-20 11:05:28)
 * 
 * @author Johan Walles
 */
public class CoJavaMapEntry implements com.bluebrim.xml.shared.CoXmlImportEnabledIF {
	public static final String XML_TAG = "java-map-entry";

	private Object m_key = null;
	private Object m_value = null;
	
	private Boolean m_marker = new Boolean(false);

public String toString() {
	StringBuffer returnMe = new StringBuffer("Map entry: ");

	returnMe.append((m_key == null) ? "null" : m_key.toString());
	returnMe.append("==>");
	returnMe.append((m_value == null) ? "null" : m_value.toString());

	return returnMe.toString();
}

/**
 * Set state for the <code>isComplete</code> method.
 */
public CoJavaMapEntry()
{
	// Mark the key and value as being non-current
	m_key = m_marker;
	m_value = m_marker;
}


public Object getKey() {
	return m_key;
}


public Object getValue() {
	return m_value;
}


/**
 * Creation date: (2001-06-20 11:25:58)
 * 
 * @author Johan Walles
 * 
 * @return <code>true</code> if both the key and the value have been assigned
 */
public boolean isComplete() {
	return (m_key != m_marker) && (m_value != m_marker);
}


public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context)
throws com.bluebrim.xml.shared.CoXmlReadException
{
	if ("key".equals(parameter))
		m_key = subModel;
	else if ("value".equals(parameter))
		m_value = subModel;
	else
	{
		System.out.println("Warning: Map members must be tagged with either \"key\" or \"value\".");
	}
}


public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context)
{
	return new CoJavaMapEntry();
}


public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context)
{
	// This method intentionally left blank
}
}