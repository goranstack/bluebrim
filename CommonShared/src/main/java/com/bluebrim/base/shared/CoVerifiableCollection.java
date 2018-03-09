package com.bluebrim.base.shared;

import java.util.*;

import com.bluebrim.collection.shared.*;

/**
 * A Collection which holds CoFreezableIF instances. Will not
 * allow removal of CoFreezableIF instances if they are frozen
 * Delegates all calls to a Collection instance
 * Creation date: (2000-06-09 15:49:07)
 * @author: Mikael Printz
 */
public class CoVerifiableCollection implements CoVerifiableCollectionIF {
	protected List		 m_collection = new ArrayList();
	protected Collection m_verifiers  = new ArrayList();

	/**
	 * Note that implementations of the methods in this interface should
	 * throw a CoVerifierException if the operation is not verified.
	 * The CoVerifierException shold be constructed so
	 */
	public static abstract class Verifier {
		public void verifyAdd(Object element, Collection elements) { return; }
		public void verifyRemove(Object element, Collection elements) { return; };
		public void verifyAddAll(Collection addElements, Collection elements) { return; };
		public void verifyRemoveAll(Collection removeElements, Collection elements) { return; };
		public void verifyClear(Collection elements) { return; };
		public void verifyAgainstElements(Object obj, Collection elements) { return; };
	}
public CoVerifiableCollection() {
	super();
}
public void add(int index, Object el) {
	throw new UnsupportedOperationException("add(int, Object) not implemented for CoVerifiableCollection");
}
public boolean add(Object o) {
	verifyAdd(o);
	// Need to listen to be able to verify changes
	((CoObjectIF)o).addPropertyChangeListener(this);
	return  m_collection.add(o);
}
public boolean addAll(int idx, Collection coll) {
	throw new UnsupportedOperationException("addAll is not implemented for CoVerifiableCollection");
}
/**
 * If not all objects are CoFreezableIF, none are added
 */
public boolean addAll(Collection c) {
	Iterator iter = c.iterator();
	while(iter.hasNext()) {
		add(iter.next());
	}
	return true;
}
public void clear() {
	Iterator iter = m_collection.iterator();
	while(iter.hasNext()) {
		remove(iter.next());
	}
}
public boolean contains(Object o) {
	return m_collection.contains(o);
}
public boolean containsAll(Collection c) {
	return m_collection.containsAll(c);
}
public void deregisterVerifier(Verifier verifier) {
	m_verifiers.remove(verifier);
}
public void each(CoCollections.EachDo doer) {
	CoCollections.each(m_collection, doer);
}
public Object get(int idx) {
	return m_collection.get(idx);
}
public int indexOf(Object element) {
	return m_collection.indexOf(element);
}
public boolean isEmpty() {
	return m_collection.isEmpty();
}
public Iterator iterator() {
	return new CoRemoteIterator(m_collection.iterator());
}
public int lastIndexOf(Object obj) {
	return m_collection.lastIndexOf(obj);
}
public ListIterator listIterator() {
	return m_collection.listIterator();
}
public ListIterator listIterator(int idx) {
	return m_collection.listIterator(idx);
}
public void propertyChange(CoPropertyChangeEvent evt) {
	verifyAgainstElements(evt.getSource());
}
public void registerVerifier(Verifier verifier) {
	m_verifiers.add(verifier);
}
public Object remove(int idx) {
	throw new UnsupportedOperationException("remove(idx) not implemented for CoVerifiableCollection");
}
public boolean remove(Object o) {
	verifyRemove(o);
	((CoObjectIF)o).removePropertyChangeListener(this);
	return m_collection.remove(o);
}
/**
 *	If any of the objects are frozen, none are removed
 */
public boolean removeAll(Collection c) {
	Iterator iter = c.iterator();
	while(iter.hasNext()) {
		remove(iter.next());
	}
	return true;
}
public boolean retainAll(Collection c) {
	throw new UnsupportedOperationException("Verifiable collection does not support retainAll");
}
public Object set(int idx, Object obj) {
	throw new UnsupportedOperationException("Set not implemented for CoVerifiableCollection");
}
public int size() {
	return m_collection.size();
}
public List subList(int start, int end) {
	return m_collection.subList(start, end);
}
public java.lang.Object[] toArray() {
	return m_collection.toArray();
}
public java.lang.Object[] toArray(java.lang.Object[] a) {
	return m_collection.toArray(a);
}
/**
 * Note that this method throws a CoVerifierException which is
 * a runtime exception if not verified.
 */
public void verifyAdd(final Object el) {
	CoCollections.each(m_verifiers, new CoCollections.EachDo() {
		public void doTo(Object element) {
			((Verifier)element).verifyAdd(el, m_collection);
		}
	});
}
/**
 * Note that this method throws a CoVerifierException which is
 * a runtime exception if not verified.
 */
public void verifyAgainstElements(final Object el) {
	CoCollections.each(m_verifiers, new CoCollections.EachDo() {
		public void doTo(Object element) {
			((Verifier)element).verifyAgainstElements(el, m_collection);
		}
	});
}
/**
 * Note that this method throws a CoVerifierException which is
 * a runtime exception if not verified.
 */
public void verifyAgainstElements(final Object el, Verifier verifier) {
	verifier.verifyAgainstElements(el, m_collection);
}
/**
 * Note that this method throws a CoVerifierException which is
 * a runtime exception if not verified.
 */
public void verifyRemove(final Object obj) {
	CoCollections.each(m_verifiers, new CoCollections.EachDo() {
		public void doTo(Object element) {
			((Verifier)element).verifyRemove(obj, m_collection);
		}
	});
}
}