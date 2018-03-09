package com.bluebrim.gui.client;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.base.shared.CoPropertyChangeListener;
import com.bluebrim.base.shared.debug.CoAssertion;
import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.collection.shared.CoCollections;
import com.bluebrim.resource.shared.CoResourceLoader;

/**
	An abstract adaptor class whose <code>subject</code> is the root in a tree structure.
*/
public abstract class CoAbstractTreeAspectAdaptor extends CoAspectAdaptor implements CoTreeValueable, TreeSelectionListener {
	protected 	EventListenerList 	listenerList = new EventListenerList();
	private 	CoAbstractTreeModel m_treeModel ;
	
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
			return getSubject();
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

	public abstract class TreeModelWithCache extends TreeModel {
		private HashMap 		m_cache = new HashMap();
		private ElementWrapper	m_root;
		
			protected class CachePathBuilder extends ListPathBuilder {

				protected Object getTreeElementFor(Object element)
				{
					return getElement(element);
				}
				protected Object makeTreeElement(Object parent,Object element)
				{
					return getWrappedElementFor(element, parent);
				}
				protected List getElementsFor(Object parent)
				{
					return getChildrenFor(parent);
				}

			};
			
		public TreePath createTreePathFor(Object element, TreePath parentPath)
		{
			return super.createTreePathFor(getWrappedElementFor(element, parentPath == null ? getRoot() : parentPath.getLastPathComponent()), parentPath);
		}		
		protected PathBuilder getPathBuilder()
		{
			return new CachePathBuilder();
		}
		public Object getRoot()
		{
			if (m_root == null)
			{
				Object _root = super.getRoot();
				if (CoAssertion.TRACE)
					CoAssertion.trace("Wrapping root"+_root);
				m_root = _root != null ? wrap(_root) : null;
			}
			return m_root;
		}
		public Object getChild(Object parent, int childIndex) {
			List tChildren = getChildrenFor(parent);
			return (tChildren != null) ? tChildren.get(childIndex): null;
		}
		public int getChildCount(Object parent) {
			if (CoAssertion.PRE)
				CoAssertion.preCondition(parent instanceof ElementWrapper, "");
			Object 	key 		= getElement(parent);
			List 	tChildren 	= getCachedElements(key);
			if (tChildren != null)
				return tChildren.size();
			else
			{
				tChildren = getElementsFor(key);
				return (tChildren != null) ? tChildren.size() : 0;
			}
		}
		protected List getChildrenFor(Object parent) {
			Object key 		= getElement(parent);
			List elements 	= getCachedElements(key);
			if (elements == null)
			{
				elements = (List )getElementsFor(key);
				return addCachedElements(key, elements);
			}	
			return elements;
		}
		
		public int getIndexOfChild(Object parent, Object child) 
		{
			Object key 		= getElement(parent);
			List tChildren 	= getCachedElements(key);
			return (tChildren != null)
					?  indexOfIn(getElement(child), tChildren)
					: _indexOfChild(key,child);
		}
		private int indexOfIn(Object child, List elements)
		{
			java.util.ListIterator e = elements.listIterator();
			if (child==null) 
			{
			    while (e.hasNext())
				if (e.next()==null)
				    return e.previousIndex();
			} 
			else 
			{
				Object tChild = getElement(child);
			    while (e.hasNext())
				if (tChild == getElement(e.next()))
				    return e.previousIndex();
			}
			return -1;
		}
		public void reload()
		{
			resetCachedElements();
			super.reload();
		}
		public void reload(TreePath aPath)
		{
			Object tElement = aPath.getLastPathComponent();
			removeCachedElements(getElement(tElement));
			super.reload(aPath);
		}
		private int _indexOfChild(Object parent, Object child)
		{
			List tChildren = getElementsFor(getElement(parent));
			return tChildren != null ? tChildren.indexOf(getElement(child)) : -1;
		}
		private void removeCachedElements(Object key)
		{
			if (CoAssertion.TRACE)
				CoAssertion.trace("Emptying cache for "+key);
			else
				System.out.println("Emptying cache for "+key);
			if (m_root != null &&  key == m_root || key == m_root.getElement())
				resetCachedElements();
			else
				m_cache.remove(key);
		}
		private void resetCachedElements()
		{
			if (CoAssertion.TRACE)
				CoAssertion.trace("Emptying cache");
			m_root	= null;
			m_cache = new HashMap(10);
		}
		private List getCachedElements(Object key)
		{
			return (List )m_cache.get(key);
		}
		private ElementWrapper getWrappedElementFor(Object element, Object parent)
		{
			int tIndex = _indexOfChild(parent,element);
			return (ElementWrapper )getChild(parent, tIndex); 
		}
		private Object getElement(Object element)
		{
			return (element instanceof ElementWrapper ) ? ((ElementWrapper )element).getElement() : element;
		}
		private List addCachedElements(Object key, List elements)
		{
			if (elements != null)
			{
				List tList = (List )CoCollections.collect(elements, new CoCollections.Collector() {
					public Object collect(Object element)
					{
						if (CoAssertion.TRACE)
							CoAssertion.trace("Wrapping "+element);
						return wrap(element);
					}
				});
				
				m_cache.put(key, tList);
				return tList;
			}
			else
				return null;
		}
		protected abstract ElementWrapper wrap(Object element);

	};

	public abstract static class ElementWrapper implements  CoCachedListModelIF.ElementWrapper {
	};
	
	public static class TreeElementWrapper  extends ElementWrapper implements CoTreeCatalogElementIF {
		private Icon				m_icon;
		private CoTreeCatalogElementIF 	m_treeElement;
		private String 					m_iconName;
		private String 					m_name;
		private String 					m_iconResourceAnchor;
		private String					m_type;
		private String 					m_factoryKey;
		
		public TreeElementWrapper(CoTreeCatalogElementIF treeElement)
		{
			m_treeElement 			= treeElement;
			m_iconResourceAnchor	= m_treeElement.getIconResourceAnchor();
			m_iconName				= m_treeElement.getSmallIconName();
			m_name					= m_treeElement.getIdentity();
			m_type					= m_treeElement.getType();
			m_factoryKey			= m_treeElement.getFactoryKey();
			m_icon					= loadIcon();
		}	
		public Icon getIcon()
		{
			return m_icon;
		}
		public String getText()
		{
			return m_name;
		}
		public Object getElement()
		{
			return m_treeElement;
		}
		public String getFactoryKey () {
			return m_factoryKey;
		}
		public String getIconName () {
			return m_iconName;
		}
		public String getIconResourceAnchor()
		{
			return m_iconResourceAnchor;
		}
		public String getIdentity () {
			return m_name;
		}
		public String getSmallIconName () {
			return m_iconName;
		}
		public CoTreeCatalogElementIF getTreeCatalogElement ()
		{
			return m_treeElement.getTreeCatalogElement();
		}
		public String getType () {
			return m_type;
		}
		public boolean equals (Object o) {
			if (o == null)
				return false;
			if (o.getClass() != getClass())
				return false;
			return m_treeElement.equals(((TreeElementWrapper)o).getElement());
		}
		public int hashCode()
		{
			return m_treeElement.hashCode();
		}
		public List getElements()
		{
			return m_treeElement.getElements();
		}
		private Icon loadIcon()
		{
			return (m_iconResourceAnchor != null && m_iconResourceAnchor.length() > 0) && (m_iconName != null && m_iconName.length() > 0)
					? CoResourceLoader.loadIcon(m_iconResourceAnchor, m_iconName)
					: null;
		}
		public void addPropertyChangeListener(CoPropertyChangeListener l) {}
		public void removePropertyChangeListener(CoPropertyChangeListener l) {}
	};

	public static class WrapperRenderer extends DefaultTreeCellRenderer {
		
		public WrapperRenderer() 
		{
			super();
		}
		public Component getTreeCellRendererComponent(	JTree tree,Object value,boolean sel,boolean expanded,boolean leaf, int row,boolean hasFocus) 
		{
			try
			{
				setText(value != null ? ((ElementWrapper )value).getText() : null);
				if(sel)
				    setForeground(getTextSelectionColor());
				else
				    setForeground(getTextNonSelectionColor());
				setIcon(value != null ? ((ElementWrapper )value).getIcon() : null);
				    
				selected 		= sel;
				this.hasFocus	= hasFocus;
			}
			catch(ClassCastException e){}

			return this;
		}	
	};

;

/**
 * CoTreeAspectAdaptor constructor comment.
 * @param context se.corren.calvin.userinterface.CoValueable
 * @param aspect java.lang.String
 * @param aspectType java.lang.Class
 	@param subjectFiresChange boolean
 */
public CoAbstractTreeAspectAdaptor(CoValueable context, String name) {
	this(context, name, false);
}
/**
 * CoTreeAspectAdaptor constructor comment.
 * @param context se.corren.calvin.userinterface.CoValueable
 * @param aspect java.lang.String
 * @param aspectType java.lang.Class
 	@param subjectFiresChange boolean
 */
public CoAbstractTreeAspectAdaptor(CoValueable context, String name, boolean subjectFiresChange) {
	super(context, name, subjectFiresChange);
}
/**
 * addTreeSelectionListener method comment.
 */
public void addTreeSelectionListener(TreeSelectionListener l) {
	listenerList.add(TreeSelectionListener.class, l);

}
protected abstract CoAbstractTreeModel createTreeModel();
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
protected Object get(CoObjectIF subject) 
{ 
	return getTreeModel().getRoot(); 
}
protected Object[] getPathToRootFrom(Object element)
{
	return getTreeModel().getPathToRootFrom(element);
}
public TreePath getPathToRootFrom(JTree tree, Object element)
{
	return getTreeModel().getPathToRootFrom(tree, element);
}
/**
 */
public final CoAbstractTreeModel getTreeModel()
{
	if (m_treeModel == null)
		m_treeModel = createTreeModel();
	return m_treeModel;
}
/**
 */
public void reload()
{
	getTreeModel().reload();
}
/**
 */
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
 * As default it's not possible to change the tree root in 
 * the domain object via the aspect adaptor.
 */
public void set(CoObjectIF subject, Object value) {}
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