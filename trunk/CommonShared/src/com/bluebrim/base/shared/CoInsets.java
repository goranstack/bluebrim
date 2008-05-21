package com.bluebrim.base.shared;

/*
 * Vår egen variant av awt.Insets med flyttal istället för heltal
 */

public class CoInsets extends CoSimpleObject implements Cloneable, java.io.Serializable {

	public float top;
	public float left;
	public float bottom;
	public float right;

	public CoInsets(float top, float left, float bottom, float right) {
	this.top = top;
	this.left = left;
	this.bottom = bottom;
	this.right = right;
	}
	public Object clone() { 
	try { 
	    return super.clone();
	} catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
	}
	/**
	 * Checks whether two insets objects are equal.
	 */
	public boolean equals(Object obj) {
	if (obj instanceof CoInsets) {
	    CoInsets insets = (CoInsets)obj;
	    return ((top == insets.top) && (left == insets.left) &&
		    (bottom == insets.bottom) && (right == insets.right));
	}
	return false;
	}
	/**
	 * Returns a String object representing this Inset's values.
	 */
	public String toString() {
	return getClass().getName() + "[top="  + top + ",left=" + left + ",bottom=" + bottom + ",right=" + right + "]";
	}
}
