package com.bluebrim.gui.client;

import java.util.Comparator;

import com.bluebrim.base.shared.CoListElementViewIF;

/**
 * List model used by an instance of <code>CoAbstractListAspectAdaptor</code> when the collection implements
 * the <code>List</code> interface.
 */
public abstract class CoAbstractCachedListModel extends CoAbstractListModel implements CoCachedListModelIF {
	private int m_loadingFactor = 10;
	private CoListElementViewIF[] m_cache;

	public abstract static class Default extends CoAbstractCachedListModel {
		private CoValueable m_valueable;

		public Default(CoValueable valueable) {
			m_valueable = valueable;
		}
	}

	public CoAbstractCachedListModel() {
	}

	protected int cachedIndexOf(Object object) {
		Object[] cache = getCache();
		if (cache == null)
			return -1;
		else {
			for (int i = 0; i < cache.length; i++) {
				CoListElementViewIF view = (CoListElementViewIF) cache[i];
				if (view == null)
					continue;
				if (view == object || view.getElement() == object)
					return i;
			}
			return -1;
		}
	}

	protected abstract CoListElementViewIF[] createCache();

	public void elementHasChanged(Object source, Object element) {
		int tIndex = indexOf(element);
		if (tIndex != -1) {
			m_cache[tIndex] = null;
			fireContentsChanged(source, tIndex, tIndex);
		}
	}

	protected abstract void fillCacheFrom(int from);

	private CoListElementViewIF[] getCache() {
		if (m_cache == null)
			rebuildCache();
		return m_cache;
	}

	private CoListElementViewIF getCacheElementAt(int index) {
		CoListElementViewIF cache[] = getCache();
		if (cache == null)
			return null;

		CoListElementViewIF cacheElement = cache[index];
		if (cacheElement == null) {
			fillCacheFrom(index);
			cache = getCache();
			cacheElement = cache[index];
		}

		return cacheElement;
	}

	/**
	 * Answer with the element view at <code>index</code>.
	 */
	public CoListElementViewIF getElementViewAt(int index) {
		return getCacheElementAt(index);
	}

	/**
	 *	Answer with the element at position <code>index</code>
	 */
	public Object getElementAt(int index) {
		CoListElementViewIF cache[] = getCache();
		if (cache == null)
			return null;

		CoListElementViewIF element = cache[index];
		if (element == null) {
			fillCacheFrom(index);
			cache = getCache();
			element = cache[index];
		}
		return element.getElement();
	}

	public int getSize() {
		return m_cache != null ? m_cache.length : 0;
	}

	public int indexOf(Object object) {
		Object[] cache = getCache();
		if (cache == null)
			return -1;
		else {
			for (int i = 0; i < cache.length; i++) {
				CoListElementViewIF view = (CoListElementViewIF) cache[i];
				if (view == null) {
					fillCacheFrom(i);
					view = (CoListElementViewIF) cache[i];
				}
				if (view == object || view.getElement() == object)
					return i;
			}
			return -1;
		}
	}

	public void listHasChanged(Object source) {
		rebuildCache();
		super.listHasChanged(source);
	}

	protected void putView(CoListElementViewIF view, int index) {
		CoListElementViewIF cache[] = getCache();
		cache[index] = view;
	}

	protected void putViews(CoListElementViewIF views[], int start) {
		CoListElementViewIF cache[] = getCache();
		System.arraycopy(views, 0, cache, start, views.length);
	}

	protected void rebuildCache() {
		m_cache = createCache();
		if (m_cache != null && m_cache.length > 0) {
			fillCacheFrom(0);
		}
	}

	public void sort(Comparator c) {
	}

	public void valueChange(CoValueChangeEvent anEvent) {
		rebuildCache();
		fireContentsChanged(anEvent.getSource(), 0, getSize());
	}
}
