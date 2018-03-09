package com.bluebrim.xml.shared;


/**
 * Insert the type's description here.
 * Creation date: (2000-08-16 11:11:35)
 * @author: Michael Klimczak 
 */
public class CoStringWrapper implements com.bluebrim.xml.shared.CoXmlExportEnabledIF{
	String str ="";

/**
 * CoStringWrapper constructor comment.
 */
public CoStringWrapper() {
	super();
}
public CoStringWrapper(String str){
	super();
	this.str = str;
}
/**
 * Returns a String that represents the value of this object.
 * @return a string representation of the receiver
 */
public String toString() {
	
	return str;
}
public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor){
	
	visitor.exportAttribute("string", str);
}

}