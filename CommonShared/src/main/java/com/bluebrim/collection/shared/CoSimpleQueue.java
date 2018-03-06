package com.bluebrim.collection.shared;

import java.util.*;

/**
 * Simple, naive implementation. Only slightly
 * improved from old scheduler code. Feel free
 * to replace with better implementation.
 * 
 * @author Markus Persson 2002-09-18
 */
public class CoSimpleQueue implements CoQueue {
	private List m_list = new ArrayList();

	public CoSimpleQueue() {
	}

	public void add(Object obj) {
		m_list.add(0, obj);
	}

	public Object remove() {
		if (m_list.isEmpty()) {
			return null;
		} else {
			return m_list.remove(m_list.size() - 1);
		}
	}

	public boolean isEmpty() {
		return m_list.isEmpty();
	}

	public int size() {
		return m_list.size();
	}

	public void clear() {
		m_list.clear();
	}

	public void addAll(Collection collection) {
		m_list.addAll(0, collection);
	}

	public Object peek() {
		if (m_list.isEmpty()) {
			return null;
		} else {
			return m_list.get(m_list.size() - 1);
		}
	}

	public List removeAll() {
		// PENDING: Implement! 
		return null;
	}

	public List removeCount(int n) {
		// PENDING: Implement! 
		return null;
	}

	public Collection toCollection(Collection collection) {
		// PENDING: Implement! 
		return null;
	}

}
