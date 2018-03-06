package com.bluebrim.collection.shared;

import java.util.*;

/**
 * Implementation of the <code>RemoteIteratorIF</code> interface.
 *
 * Creation date: (1999-10-13 11:55:35)
 * @author Lasse S
 */
public class CoRemoteIterator implements CoRemoteIteratorIF {
	private Iterator m_iterator;
public CoRemoteIterator(Iterator iterator) {
	m_iterator = iterator;
}
public boolean hasNext() {
	return m_iterator.hasNext();
}
public Object next() {
	return m_iterator.next();
}
public void remove() {
	m_iterator.remove();
}
/**
 * Static method to be used instead of constructor when one is not sure
 * if the <code>iterator</code> is remote or not, but it needs to be.
 *
 * NOTE: Actually, the test below is not sufficient, as it is not
 * sufficient for a remote iterator to merely implement Remote. A remote
 * iterator must implement an interface which in turn extends both Remote
 * and Iterator. However, testing for that would probably be to costly.
 * 
 * @author Markus Persson 1999-10-13
 */
public static Iterator wrap(Iterator iterator) {
	if (iterator instanceof java.rmi.Remote)
		return iterator;
	else
		return new CoRemoteIterator(iterator);
}
}
