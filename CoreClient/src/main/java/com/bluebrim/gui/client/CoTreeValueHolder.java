package com.bluebrim.gui.client;

import java.util.List;

import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
/**
 	En abstrakt subklass till CoValueHolder vars värde är det objekt som
 	utgör roten i en trädstruktur. <br>
	Implementerar CoTreeValueable och därigenom också TreeModel.
	Ett annat objekt, t ex en gränssnittsmodell som är intresserad av 
	ändringar i en trädvy lyssnar på denna klass i stället för på trädvyn.<br>
	Subklasser måste implementera #getChildrenFor som skall svara med alla grenar för
	ett visst objekt som ingår i strukturen. Observera att denna lösning förutsätter
	att alla objekt i trädstrukturen implementerar ett gemensamt protokoll för att komma 
	åt sina grenar.
 */
public abstract class CoTreeValueHolder extends CoValueHolder implements CoTreeValueable, TreeSelectionListener{
	protected 	EventListenerList 	listenerList = new EventListenerList();
	private 	CoAbstractTreeModel m_treeModel;
	
	public abstract class TreeModel extends CoAbstractTreeModel {
		
		protected class ListPathBuilder extends PathBuilder {
			protected TreePath buildPathFor(TreePath parentPath, Object treeElement)
			{
				List elements = getElementsFor(parentPath.getLastPathComponent());
				if (elements == null)
					return null;
				TreePath tResult = null;
				for (int i = 0; i<elements.size(); i++)
				{
					Object 		tElement 	= elements.get(i);
					TreePath 	tPath 		= parentPath.pathByAddingChild(makeTreeElement(parentPath.getLastPathComponent(),tElement));
					if ((tResult = createPathFor(treeElement, tPath)) != null)
						return tResult;
					
				}
				return null;
			}
					
		};
		public Object getRoot()
		{
			return CoTreeValueHolder.this.getValue();
		}
		public int getIndexOfChild(Object parent, Object child) {
			List tElements = getElementsFor(parent );
			return (tElements != null) ? tElements.indexOf(child) : -1;
		}
		public int getChildCount(Object parent) {
			List tElements = getElementsFor(parent );
			return (tElements != null) ? tElements.size(): 0;
		}
		public Object getChild(Object parent, int childIndex)
		{
			List tElements = getElementsFor(parent);
			return (tElements != null) ? tElements.get(childIndex) : null;
		}
		protected PathBuilder getPathBuilder()
		{
			return new ListPathBuilder();
		}
		public abstract List getElementsFor(Object parent);
	};
/**
 * ValueHolder constructor comment.
 */
protected CoTreeValueHolder()
 {
	super();
}
/**
 * This method was created by a SmartGuide.
 * @param initValue java.lang.Object
 */
public CoTreeValueHolder ( Object initValue, String name) 
{
	super(initValue,name);
}
/**
 * ValueHolder constructor comment.
 */
public CoTreeValueHolder(String name)
 {
	super(name);
}
/**
 * addTreeSelectionListener method comment.
 */
public void addTreeSelectionListener(TreeSelectionListener l) {
	listenerList.add(TreeSelectionListener.class, l);

}
public abstract CoAbstractTreeModel createTreeModel();
public void eachChildDo (Object startElement,CoTreeElementVisitor iterator)
{
	eachChildDo(startElement, iterator, true);
}
public void eachChildDo (Object startElement,CoTreeElementVisitor iterator, boolean parentBeforeChildren)
{
	int tChildCount	= getTreeModel().getChildCount(startElement);
	for (int i=0; i<tChildCount; i++)
	{
		Object tChild = getTreeModel().getChild(startElement, i);
		if (tChild != null)
		{
			if (parentBeforeChildren)
				iterator.doToElement(tChild);
			eachChildDo(tChild, iterator);
			if (!parentBeforeChildren)
				iterator.doToElement(tChild);
		}
	}
}
protected Object[] getPathToRootFrom(Object element)
{
	return getTreeModel().getPathToRootFrom(element);
}
public TreePath getPathToRootFrom(JTree tree,Object element)
{
	return getTreeModel().getPathToRootFrom(tree, element);
}
/**
 */
public CoAbstractTreeModel getTreeModel()
{
	if (m_treeModel == null)
		m_treeModel = createTreeModel();
	return m_treeModel;
}
public void reload()
{
	getTreeModel().reload();
}
public void reload(TreePath aPath) 
{
	getTreeModel().reload(aPath);
}
/**
 * removeTreeSelectionListener method comment.
 */
public void removeTreeSelectionListener(TreeSelectionListener l) {
	listenerList.remove(TreeSelectionListener.class, l);
}
/**
 	Selekteringen har ändrats i en trädvy jag visas upp i.
 	Andra värdeobjekt som är intresserade av detta
 	Meddela alla de som lyssnar efter förändringar och gör
 */
public void valueChanged(TreeSelectionEvent e) {
	Object tListeners[] 		= listenerList.getListenerList();
	Class tListenerClass		= TreeSelectionListener.class;
	TreeSelectionEvent tEvent = null;
	for (int i = tListeners.length - 2; i >= 0; i -= 2)
	{
		if (tListeners[i] == tListenerClass)
		{
			if (tEvent == null)
				tEvent = (TreeSelectionEvent ) e.cloneWithSource(this);
			((TreeSelectionListener)tListeners[i + 1]).valueChanged(tEvent);
		}
	}
}
}
