package com.bluebrim.base.shared;

/**
	Varje v�rde f�r en nyckel i ett CoPropertySet inneh�ller en array av objekt.
	CoPropertEnumerator �r en implementering av Enumeration som anv�nds f�r att iterera �ver 
	en s�dan array.	
	@see CoPropertySet
*/

public class CoPropertyEnumerator implements java.util.Enumeration {
	Object properties[] = null;
	int currentIndex;
	int maxIndex;
/**
 * This method was created by a SmartGuide.
 * @param properties java.lang.Object[]
 */
public CoPropertyEnumerator (Object properties[] ) {
	this.properties  	= properties;
	currentIndex 		= -1;
	maxIndex = (properties != null) ? properties.length-1 : -1;
}
/**
 * hasMoreElements method comment.
 */
public boolean hasMoreElements() {
	return (maxIndex > currentIndex);
}
/**
 * nextElement method comment.
 */
public Object nextElement() {
	return hasMoreElements() ? properties[++currentIndex] : null;
}
}
