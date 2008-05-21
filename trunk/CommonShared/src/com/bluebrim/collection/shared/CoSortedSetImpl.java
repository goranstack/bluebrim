package com.bluebrim.collection.shared;

import java.util.*;

/**
 * @author Markus Persson 2002-08-21
 */
public class CoSortedSetImpl extends AbstractSet implements CoSortedSet {
	private CoKeyedComparator m_comparator;
	private SortedMap m_entryMap;

	public CoSortedSetImpl(CoKeyedComparator comparator) {
		this(comparator, new TreeMap());
	}

	public CoSortedSetImpl(Collection elements, CoKeyedComparator comparator) {
		this(comparator);
		addAll(elements);
	}

	private CoSortedSetImpl(CoKeyedComparator comparator, SortedMap entryMap) {
		m_comparator = comparator;
		m_entryMap = entryMap;
	}

	public final boolean add(Object item) {
		return addUsingKey(item, m_comparator.sortKey(item));
	}

	public final boolean addAll(Collection elements) {
		boolean modified = false;
		Iterator iter = elements.iterator();
		while (iter.hasNext()) {
			modified |= add(iter.next());
		}
		return modified;
	}

	private final boolean addUsingKey(Object item, Comparable key) {
		return (m_entryMap.put(key, item) == null);
	}

	public Comparator comparator() {
		return m_comparator;
	}

	/**
	 * Should be called after the element has changed in such a way that the
	 * key on which the set is sorted has changed. In order for the old mapping
	 * to be successfully removed, the old key has to be sent along.
	 *
	 * PENDING: Return boolean indicating if removal was successful?
	 * PENDING: Only put in new position if found in old position?
	 *
	 * @author Markus Persson 2000-10-05
	 */
	public void elementChangedKeyFrom(Object element, Comparable oldKey) {
		// Remove element from old place, if possible.
		removeUsingKey(element, oldKey);

		// Add the element again.
		add(element);
	}

	public void reIndex() {
		CoKeyedComparator comparator = m_comparator;
		SortedMap map = m_entryMap;

		// Will only contain each key once, so it could be a set, but this is probably faster ...
		Collection keysToRemove = new ArrayList();
		// Is a set to prevent the same item from being added several times ...
		Collection itemsToAdd = new HashSet();

		Iterator mapEntries = map.entrySet().iterator();
		while (mapEntries.hasNext()) {
			Map.Entry mapEntry = (Map.Entry) mapEntries.next();
			Object mapItem = mapEntry.getValue();
			Comparable key = (Comparable) mapEntry.getKey();
			if (!key.equals(comparator.sortKey(mapItem))) {
				keysToRemove.add(key);
				itemsToAdd.add(mapItem);
			}
		}

		// Remove stray keys.
		Iterator strayKeys = keysToRemove.iterator();
		while (strayKeys.hasNext()) {
			map.remove(strayKeys.next());
		}

		// Add the "lost" items.
		addAll(itemsToAdd);
	}

	public final boolean remove(Object item) {
		return removeUsingKey(item, m_comparator.sortKey(item));
	}

	/**
	 * PENDING: Could possibly be more efficient ...
	 */
	public final boolean removeAll(Collection items) {
		boolean modified = false;
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			modified |= remove(iter.next());
		}
		return modified;
	}

	private final boolean removeUsingKey(Object item, Comparable key) {
		SortedMap map = m_entryMap;

		Object mapItem = map.get(key);
		if ((mapItem != null) && mapItem.equals(item)) {
			map.remove(key);
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return "CoSortedSetImpl sorted by: " + m_comparator;
	}

	public Iterator iterator() {
		return m_entryMap.values().iterator();
	}

	public Iterator iterator(Object fromInclusive) {
		return iteratorByKey(m_comparator.sortKey(fromInclusive));
	}

	public Iterator iteratorByKey(Comparable fromInclusiveKey) {
		return m_entryMap.tailMap(fromInclusiveKey).values().iterator();
	}

	public Iterator iteratorByKey(Comparable fromInclusiveKey, Comparable toExclusiveKey) {
		return m_entryMap.subMap(fromInclusiveKey, toExclusiveKey).values().iterator();
	}

	public Object first() {
		return m_entryMap.get(m_entryMap.firstKey());
	}

	public Object last() {
		return m_entryMap.get(m_entryMap.lastKey());
	}

	public SortedSet subSet(Object fromInclusiveElement, Object toExclusiveElement) {
		return child(m_entryMap.subMap(m_comparator.sortKey(fromInclusiveElement), m_comparator.sortKey(toExclusiveElement)));
	}

	public SortedSet headSet(Object fromInclusiveElement) {
		return child(m_entryMap.headMap(m_comparator.sortKey(fromInclusiveElement)));
	}

	public SortedSet tailSet(Object toExclusiveElement) {
		return child(m_entryMap.tailMap(m_comparator.sortKey(toExclusiveElement)));
	}

	public CoSortedSet subSetByKey(Comparable fromInclusiveKey, Comparable toExclusiveKey) {
		return child(m_entryMap.subMap(fromInclusiveKey, toExclusiveKey));
	}

	/**
	 * Used internally to create a new subset.
	 */
	private CoSortedSet child(SortedMap subMap) {
		return new CoSortedSetImpl(m_comparator, subMap);
	}

	public int size() {
		return m_entryMap.size();
	}

}