package com.bluebrim.browser.client;

import java.util.List;

import javax.swing.tree.TreePath;

import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.browser.shared.CoFilteredTreeCatalogElementIF;
import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.browser.shared.CoTreeCatalogFilterIF;
import com.bluebrim.gui.client.CoAbstractTreeAspectAdaptor;
import com.bluebrim.gui.client.CoValueable;

public abstract class CoFilteredTreeAspectAdaptor extends CoAbstractTreeAspectAdaptor {
	private CoTreeCatalogFilterIF m_filter = null;

	public abstract class FilteredCachedTreeModel extends TreeModelWithCache {
		public FilteredCachedTreeModel() {
		}
		public void reload(TreePath aPath) {
			if (m_filter != null) {
				Object tElement = aPath.getLastPathComponent();
				m_filter.resetElementsFrom((CoTreeCatalogElementIF) tElement);
			}
			super.reload(aPath);

		}
		public void reload() {
			if (m_filter != null) {
				m_filter.resetElements();
			}
			super.reload();

		}
		protected abstract List getElementsFor(CoFilteredTreeCatalogElementIF subject, CoTreeCatalogFilterIF filter);
	}

	public CoFilteredTreeAspectAdaptor(CoValueable context, String name, CoTreeCatalogFilterIF filter) {
		this(context, name, false, filter);
	}
	/**
	 * CoFilteredTreeAspectAdaptor constructor comment.
	 */
	public CoFilteredTreeAspectAdaptor(CoValueable context, String name, boolean subjectFiresChange, CoTreeCatalogFilterIF filter) {
		super(context, name, subjectFiresChange);
		m_filter = filter;
	}
	private FilteredCachedTreeModel _treeModel() {
		return (FilteredCachedTreeModel) getTreeModel();
	}
	public CoTreeCatalogFilterIF getFilter() {
		return m_filter;
	}
	protected Object getValueFor(CoObjectIF aSubject) {
		CoTreeCatalogFilterIF tFilter = getFilter();
		if (tFilter == null)
			return super.getValueFor(aSubject);
		else {
			return (aSubject != null) ? _treeModel().getElementsFor((CoFilteredTreeCatalogElementIF) aSubject, tFilter) : null;
		}
	}
	public void setFilter(CoTreeCatalogFilterIF filter) {
		if (filter != m_filter) {
			m_filter = filter;
			_treeModel().reload();
		}
	}
}
