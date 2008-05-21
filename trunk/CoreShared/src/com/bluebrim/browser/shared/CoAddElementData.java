package com.bluebrim.browser.shared;

import java.io.*;

/**
 * An instance of <code>CoAddElementData</code> represents an element 
 * to be added in a catalog ui by a catalog editor.
 * <br>
 * Instance variables:
 * <ul>
 * <li>	<code>m_elementType</code>...(String) a key used to create the right type of element.
 * <li>	<code>m_elementData</code>...(Object) optional, additional data necessary to 
 * create the new element.
 * </ul>
 */
public class CoAddElementData implements Serializable{
	
	private String m_elementType;
	private Object m_elementData;
		
public CoAddElementData ( String elementType) {
	m_elementType = elementType;
}
public CoAddElementData ( String elementType, Object elementData) {
	this(elementType);
	m_elementData = elementData;
}

public final Object getElementData() {
	return m_elementData;
}

public final String getElementType() {
	return m_elementType;
}
public final void setElementData(Object elementData) {
	m_elementData = elementData;
}
}
