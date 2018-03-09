package com.bluebrim.browser.client;

import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.tree.TreePath;

import com.bluebrim.base.shared.CoStringResources;
import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.gui.client.CoTreeValueable;
import com.bluebrim.menus.client.CoMenuBuilder;
import com.bluebrim.menus.client.CoPopupMenu;
import com.bluebrim.transact.shared.CoCommand;

/**
 * A subclass to <code>CoAbstractCatalogEditor</code> that is responsible
 * for editing the contents of a tree catalog, i e a tree where the user can
 * add or remove elements via a popup menu.
 * <br>
 * The actions used are held in a <code>Map</code> so they can be easily accessed when 
 * the menu items are to be enabled or disabled. The command classes used are 
 * defined as inner classes.
 */
public abstract class CoAbstractTreeCatalogEditor extends CoAbstractCatalogEditor
{
	private CoTreeCatalogUI m_treeCatalogUI = null;
	private Map 			m_menuActions 	= new HashMap();
	private MouseListener	m_mouseListener;
	
	public final static String	REMOVE_ELEMENTS_ACTION	= "remove_elements_action";
	
	/**	
	 * Defines the algorithm used when adding an elements to a <code>CoTreeCatalogUI</code>.
	 * The methods <code>doExecute</code>,<code>prepare</code> and <code>finish</code> are made
	 * public in this class.
	 */
	public static abstract class AbstractAddElementCommand extends AbstractTreeCommand
	{
		protected CoTreeCatalogElementIF 	m_addedElement;
		protected TreePath 					m_selection;
		
		public AbstractAddElementCommand(CoTreeCatalogUI treeCatalogUI)
		{
			super(treeCatalogUI, CoStringResources.getName("ADD_ITEM"));
			TreePath selectionPaths[] 	= treeCatalogUI.getTreeComponent().getSelectionPaths();
			m_selection 				= selectionPaths != null ? selectionPaths[0] : null;
		}
		public boolean doExecute()
		{
			m_addedElement = addElement();
			return m_addedElement != null;
		}
		public void finish()
		{
			CoTreeValueable treeHolder = this.m_treeCatalogUI.getTreeCatalogHolder();
			if (m_selection != null) {
				treeHolder.reload(m_selection);
			} else {
				treeHolder.reload();
			}

			TreePath selectionPath = treeHolder.getTreeModel().createTreePathFor(m_addedElement, m_selection);
			this.m_treeCatalogUI.getTreeComponent().setSelectionPath(selectionPath);
			this.m_treeCatalogUI.getTreeComponent().scrollPathToVisible(selectionPath);
		}
		protected abstract CoTreeCatalogElementIF addElement();
	}
	/**
	 * Defines the algorithm used when removing elements from a <code>CoTreeCatalogUI</code>.
	 * The methods <code>doExecute</code>,<code>prepare</code> and <code>finish</code> are made
	 * public in this class. 
	 */
	public static abstract class AbstractRemoveElementsCommand extends AbstractTreeCommand {

		protected TreePath 			m_selection[];
		protected TreePath			m_parents[];
		
		public AbstractRemoveElementsCommand(CoTreeCatalogUI treeCatalogUI)
		{
			super(treeCatalogUI, CoStringResources.getName("REMOVE_ITEM"));
			m_selection 	= treeCatalogUI.getTreeComponent().getSelectionPaths();
			m_parents 		= new TreePath[m_selection.length];
		}	
		public boolean doExecute()
		{
			for (int i = m_selection.length - 1; i >= 0; i--)
			{
				Object iPath[] 					= m_selection[i].getPath();
				CoTreeCatalogElementIF iElement = (CoTreeCatalogElementIF )iPath[iPath.length-1];
				CoTreeCatalogElementIF iParent	= (CoTreeCatalogElementIF )iPath[iPath.length-2];
				doRemoveSelectedElementFrom(iElement.getTreeCatalogElement(), iParent.getTreeCatalogElement());
			}
			return true;
		}			
		public void finish()
		{
			for (int j = m_parents.length-1; j>=0; j--)
			{
				this.m_treeCatalogUI.getTreeCatalogHolder().reload(m_parents[j]);
			}		
		}			
		public void prepare()
		{
			for (int i = m_selection.length - 1; i >= 0; i--)
			{
				Object iPath[] 			= m_selection[i].getPath();
				Object iParentPath[]	= new Object[iPath.length-1];
				System.arraycopy(iPath,0,iParentPath,0,iPath.length-1);
				m_parents[i] 			= new TreePath(iParentPath);
			}
			this.m_treeCatalogUI.getTreeComponent().clearSelection();
			
		}	
		protected abstract void doRemoveSelectedElementFrom(CoTreeCatalogElementIF element, CoTreeCatalogElementIF parent);
	}
	/**	
	 * Abstract superclass for commands working 
	 * on a selection in a tree.
	 */
	public static abstract class AbstractTreeCommand extends CoCommand {
		protected CoTreeCatalogUI 			m_treeCatalogUI;

		public AbstractTreeCommand(CoTreeCatalogUI treeCatalogUI, String name)
		{
			super(name);
			m_treeCatalogUI 			= treeCatalogUI;
		}
	}
/**
 */
public CoAbstractTreeCatalogEditor (CoTreeCatalogUI treeCatalogUI) {
	m_treeCatalogUI = treeCatalogUI;
	m_editMenu 		= createEditMenu(m_treeCatalogUI.getTreeComponent());
}
/**
 */
protected abstract AbstractRemoveElementsCommand createRemoveElementsCommand();
/**
 */
protected JComponent createRemoveItem (CoMenuBuilder menuBuilder, CoPopupMenu menu) {
	return menuBuilder.addPopupMenuItem(menu,putMenuAction(REMOVE_ELEMENTS_ACTION,new CoRemoveElementAction(getTreeCatalogUI().getRemoveElementItemLabel(),this)));
}
/**
 */
protected JComponent getCatalogComponent() {
	return getTreeCatalogUI().getTreeComponent();
}
/**
 */
public Action getMenuAction(String key) {
	return (Action )m_menuActions.get(key);
}
/**
 */
public CoMenuBuilder getMenuBuilder () {
	return getTreeCatalogUI().getUIMenuBuilder();
}
/**
 */
public CoTreeCatalogUI getTreeCatalogUI() {
	return m_treeCatalogUI;
}
/**
 */
public void handleRemoveElementAction() {
	createRemoveElementsCommand().execute();
}
public CoAbstractCatalogEditor initialize () {
	super.initialize();
	m_mouseListener = createMouseListener();
	return this;
}
/**
 */
public void install()
{
	getCatalogComponent().addMouseListener(m_mouseListener);
}
/**
 */
public void installIn(CoTreeCatalogUI catalogUI)
{
	catalogUI.getTreeComponent().addMouseListener(m_mouseListener);
}
/**
 */
protected Action putMenuAction(String key, Action menuAction) {
	m_menuActions.put(key, menuAction);
	return menuAction;
}
/**
 */
public void remove()
{
	getCatalogComponent().removeMouseListener(m_mouseListener);
}
/**
 */
public void removeFrom(CoTreeCatalogUI catalogUI)
{
	catalogUI.getTreeComponent().removeMouseListener(m_mouseListener);
}
}