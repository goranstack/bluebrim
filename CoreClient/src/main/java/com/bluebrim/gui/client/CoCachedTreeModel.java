package com.bluebrim.gui.client;

import javax.swing.tree.TreePath;

import com.bluebrim.base.shared.CoBaseUtilities;
import com.bluebrim.base.shared.debug.CoAssertion;
import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.browser.shared.CoTreeElementView;

/**
 * Abstract tree model whose elements are serializable views of server elements.
 * By implementing the two abstract methods, <code>rebuildViewsFor(CoTreeElementView, int) 
 * and <code>updateViewsFor(CoTreeElementView, int, int), the concrete subclass can implement
 * the caching in the way that best suits its purpose, eg by using an ui servant. 
 * <br>
 * The actual server elements must implement <code>CoTreeCatalogElementIF</code>.
 */
public abstract class CoCachedTreeModel extends CoAbstractTreeModel
{
	private CoTreeElementView 		m_root;
	
	protected class _pathBuilder extends PathBuilder {
		protected TreePath buildPathFor(TreePath parentPath, Object treeElement)
		{
			CoTreeElementView 	parent		= (CoTreeElementView )parentPath.getLastPathComponent();
			int 				childCount	= getChildCount(parent);
			if (childCount == 0)
				return null;
			TreePath tResult = null;
			for (int i = 0; i<childCount; i++)
			{
				CoTreeElementView 		view 	= parent.getChildViews()[i];
				if (view == null)
				{
					updateViewsFor(parent, i,maxChildrenLoadedFor(parent));
					view 	= parent.getChildViews()[i];
				}
				TreePath 	tPath 		= parentPath.pathByAddingChild(view);
				if ((tResult = createPathFor(treeElement, tPath)) != null)
					return tResult;
				
			}
			return null;
		}
		protected TreePath createPathFor(Object treeElement, TreePath parentPath)
		{
			int index;
			if ((index = getIndexOfChild(parentPath.getLastPathComponent(), treeElement)) != -1)
				return parentPath.pathByAddingChild(getChild(parentPath.getLastPathComponent(),index));
			else
				return buildPathFor(parentPath, treeElement);
		}
		
	};
public CoCachedTreeModel() {
	super();
}
public TreePath createTreePathFor(Object element, TreePath parentPath)
{
	if (element != null)
	{
		if (parentPath != null)
		{
			Object 	parent 	= parentPath.getLastPathComponent();
			if (CoAssertion.TRACE)
				CoAssertion.trace("Create path for "+element+ " with parent "+parent);
			int 	index 	= getIndexOfChild(parent, element);
			return parentPath.pathByAddingChild(getChild(parent, index));
		}
		else
			return getPathBuilder().getPathToRootFor(element);
	}
	else
		return parentPath;
}
public Object getChild(Object parent, int childIndex)
{
	CoTreeElementView 	_parent = (CoTreeElementView)parent;
	CoTreeElementView 	child = getChildrenFor(_parent)[childIndex];
	if (child == null)
	{
		updateViewsFor(_parent, childIndex, maxChildrenLoadedFor(_parent));
		child = getChildrenFor(_parent)[childIndex];
	}
	return child;
}
public int getChildCount(Object parent)
{
	return ((CoTreeElementView )parent ).getChildCount();
}
protected final CoTreeElementView[] getChildrenFor(CoTreeElementView parent)
{
	return parent.getChildViews();
}
	private CoTreeCatalogElementIF getElement(Object element)
	{
		return ((CoTreeCatalogElementIF) element).getTreeCatalogElement();
	}
public int getIndexOfChild(Object parent, Object child)
{
	CoTreeElementView 	_parent		= (CoTreeElementView )parent;
	int					childCount	= getChildCount(parent);
	for (int i=0; i<childCount; i++)
	{
		CoTreeElementView view = _parent.getChildViews()[i];
		if (view == null)
		{
			updateViewsFor(_parent, i,maxChildrenLoadedFor(_parent));
			view = _parent.getChildViews()[i];
		}
		if (view.equals(child) || view.getTreeCatalogElement().equals(child))
			return i;
	}
	return -1;
}
protected PathBuilder getPathBuilder()
{
	return new _pathBuilder();
}
public Object getRoot()
{
	if (m_root == null)
	{
		m_root = root();
		if (CoAssertion.TRACE && m_root != null)
			CoAssertion.trace("Caching root" + CoBaseUtilities.debugInfo(m_root.getTreeCatalogElement()));
	}
	return m_root;
}
/**
 * Answer with the maximum number of children that
 * should be loaded when <code>parent</code> is 
 * cached. Integer.MAX_VALUE means all children!
 * <br>
 * NB: This ought to be a smaller figure than the number
 * of children but the design of the ui class for <code>JTree</code>
 * makes it hard to load anything less than that! 
 */
protected int maxChildrenLoadedFor(CoTreeElementView parent)
{
	return Integer.MAX_VALUE;
}
private void rebuild()
{
	if (CoAssertion.TRACE)
		CoAssertion.trace("Reset");
	m_root = null;
	getRoot();
}
protected abstract void rebuildViewsFor(CoTreeElementView parent, int maxChildrenLoaded);
	public void reload()
	{
		rebuild();
		super.reload();
	}
public void reload(TreePath aPath)
{
	CoTreeElementView element = (CoTreeElementView) aPath.getLastPathComponent();
	rebuildViewsFor(element, maxChildrenLoadedFor(element));
	super.reload(aPath);
}
	//
	// These methods should be implemented in 
	// the userinterface context.
	//
	protected abstract CoTreeElementView root();
protected abstract void updateViewsFor(CoTreeElementView parent, int from, int maxChildrenLoaded);
}
