package com.bluebrim.collection.shared;

import java.util.*;

public class EnumerationToIterator implements Iterator {
	Enumeration e;
public EnumerationToIterator (Enumeration e)
		{this.e = e;}
	public boolean hasNext() {
	return e.hasMoreElements();
	}
	public Object next() {
	return e.nextElement();
	}
public void remove () {
}
}
