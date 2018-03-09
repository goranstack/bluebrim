package com.bluebrim.gui.client;

import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.bluebrim.base.shared.debug.CoAssertion;

/**
 * An abstract treemodel class.
 */
public abstract class CoAbstractTreeModel implements TreeModel {
	private 	EventListenerList 	m_listenerList = new EventListenerList();

	/**
	 *	A <code>PathBuilder</code> is used by the tree model to,
	 * for a giving element, create a path uptp and including the root.
	 *
	 * Tiis class is subclassed for tree models that in some way 
	 * cache the elements.
	 */
	protected abstract class PathBuilder {
		public PathBuilder(){}

		public TreePath getPathToRootFor(JTree tree, Object element)
		{
			TreePath tPath = getPathToRootInTreeFor(tree, element);
			return (tPath != null) ? tPath : getPathToRootFor(element);
		}
		private TreePath getPathToRootInTreeFor(JTree tree, Object element)
		{
			int  tRowCount	= tree.getRowCount();
			for (int i=0; i<tRowCount; i++)
			{
				TreePath iPath = tree.getPathForRow(i);
				if (getTreeElementFor(iPath.getLastPathComponent()) == getTreeElementFor(element))
					return iPath;
			}
			return null;
		}
		public TreePath getPathToRootFor(Object element)
		{
			Object 		tRoot 	= getRoot();
			TreePath 	tPath	= new TreePath(tRoot);
			if (element == getTreeElementFor(tRoot))
				return tPath;
			else
				return createPathFor(element, tPath);
		}
		protected TreePath createPathFor(Object treeElement, TreePath parentPath)
		{
			if (getIndexOfChild(parentPath.getLastPathComponent(), treeElement) != -1)
				return parentPath.pathByAddingChild(makeTreeElement(parentPath.getLastPathComponent(),treeElement));
			else
				return buildPathFor(parentPath, treeElement);
		}
		protected abstract TreePath buildPathFor(TreePath parentPath, Object treeElement);
		
		protected Object getTreeElementFor(Object element)
		{
			return element;
		}
		protected Object makeTreeElement(Object parent,Object element)
		{
			return element;
		}
		
	};
public CoAbstractTreeModel() {
	super();
}
public void addTreeModelListener(TreeModelListener l) {
	m_listenerList.add(TreeModelListener.class,l);
}
public TreePath createTreePathFor(Object element, TreePath parentPath)
{
	return parentPath != null 
				? parentPath.pathByAddingChild(element)
				: new TreePath(new Object[] {getRoot(),element});
}
public void elementHasChanged(Object source,Object element)
{
	TreePath 	tPath 	= getPathBuilder().getPathToRootFor(element);
	if (CoAssertion.ASSERT)
		CoAssertion.assertTrue(tPath != null, "TreePath null in elementHasChanged for "+element);
		
	Object		tParent	= tPath.getParentPath().getLastPathComponent();
	fireTreeNodesChanged(this, tPath.getPath(), (new int[] {getIndexOfChild(tParent,element)}),  (new Object[] {element}));
}
protected void fireTreeNodesChanged(Object source, Object path[], int childIndices[], Object children[])
{
	Object tListeners[] 				= m_listenerList.getListenerList();
	TreeModelEvent treeModelEvent 	= null;
	Class tListenerClass				= TreeModelListener.class;
	for (int i = tListeners.length - 2; i >= 0; i -= 2)
	{
		if (tListeners[i] == tListenerClass)
		{
			if (treeModelEvent == null)
				treeModelEvent = new TreeModelEvent(source, path, childIndices, children);
			((TreeModelListener)tListeners[i + 1]).treeNodesChanged(treeModelEvent);
		}
	}
}
protected void fireTreeNodesInserted(Object source, Object path[], int childIndices[], Object children[])
{
	Object tListeners[] 				= m_listenerList.getListenerList();
	TreeModelEvent treeModelEvent 	= null;
	Class tListenerClass				= TreeModelListener.class;
	for (int i = tListeners.length - 2; i >= 0; i -= 2)
	{
		if (tListeners[i] == tListenerClass)
		{
			if (treeModelEvent == null)
				treeModelEvent = new TreeModelEvent(source, path, childIndices, children);
			((TreeModelListener)tListeners[i + 1]).treeNodesInserted(treeModelEvent);
		}
	}
}
protected void fireTreeNodesRemoved(Object source, Object path[], int childIndices[], Object children[])
{
	Object tListeners[] 				= m_listenerList.getListenerList();
	TreeModelEvent treeModelEvent 	= null;
	Class tListenerClass				= TreeModelListener.class;
	for (int i = tListeners.length - 2; i >= 0; i -= 2)
	{
		if (tListeners[i] == tListenerClass)
		{
			if (treeModelEvent == null)
				treeModelEvent = new TreeModelEvent(source, path, childIndices, children);
			((TreeModelListener)tListeners[i + 1]).treeNodesRemoved(treeModelEvent);
		}
	}
}
protected void fireTreeStructureChanged(Object source, Object path[], int childIndices[], Object children[])
{
	Object tListeners[] 				= m_listenerList.getListenerList(); 
	TreeModelEvent treeModelEvent 	= null;
	Class tListenerClass				= TreeModelListener.class;
	for (int i = tListeners.length - 2; i >= 0; i -= 2)
	{
		if (tListeners[i] == tListenerClass)
		{
			if (treeModelEvent == null)
				treeModelEvent = new TreeModelEvent(source, path, childIndices, children);
			((TreeModelListener)tListeners[i + 1]).treeStructureChanged(treeModelEvent);
		}
	}
}
protected abstract PathBuilder getPathBuilder();
public Object[] getPathToRootFrom(Object element)
{
	return getPathBuilder().getPathToRootFor(element).getPath();
}
public TreePath getPathToRootFrom(JTree tree, Object element)
{
	return getPathBuilder().getPathToRootFor(element);
}
public boolean isLeaf(Object element) {
	return getChildCount(element) == 0;
}
/**
 */
public void reload()
{
	fireTreeStructureChanged(this,(new Object[] {getRoot()}), null, null);
}
/**
 */
public void reload(TreePath aPath)
{
	fireTreeStructureChanged(this, aPath.getPath(), null, null);
}
public void removeTreeModelListener(TreeModelListener l) {
	m_listenerList.remove(TreeModelListener.class,l);
}
public void valueForPathChanged(TreePath aPath, Object anElement) {
	Object 	tChangedObject	= aPath.getLastPathComponent();
	Object	tParent			= aPath.getParentPath().getLastPathComponent();
	fireTreeNodesChanged(this, aPath.getParentPath().getPath(), (new int[] {getIndexOfChild(tParent,tChangedObject)}),  (new Object[] {tChangedObject}));
}
}
