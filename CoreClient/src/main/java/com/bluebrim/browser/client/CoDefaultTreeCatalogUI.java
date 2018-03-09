package com.bluebrim.browser.client;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.tree.TreePath;

import com.bluebrim.base.shared.CoFactoryElementIF;
import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.browser.shared.CoAddElementData;
import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.gui.client.CoAbstractTreeAspectAdaptor;
import com.bluebrim.gui.client.CoAbstractTreeModel;
import com.bluebrim.gui.client.CoDomainUserInterface;
import com.bluebrim.gui.client.CoTreeValueable;
import com.bluebrim.gui.client.CoUserInterfaceBuilder;
import com.bluebrim.menus.client.CoMenuBuilder;
import com.bluebrim.menus.client.CoPopupMenu;
import com.bluebrim.swing.client.CoPanel;

/**
 * 
 *
 * @author Ali Abida
 */
abstract public class CoDefaultTreeCatalogUI extends CoAbstractTreeCatalogUI {
	/**
	 * Editor that does not really do anything, the reason is that
	 * elements in this ui should neither be added nor removed.
	 * It is used in method createTreeCatalogEditor().
	 * @see #createTreeCatalogEditor
	 */
	protected class NullEditor extends CoAbstractTreeCatalogEditor {
		public NullEditor() {
			super(CoDefaultTreeCatalogUI.this);
		}
		protected void enableDisableMenuItems() {
		}
		protected JComponent createAddItem(CoMenuBuilder menuBuilder, CoPopupMenu menu) {
			return menu;
		}
		protected JComponent createRemoveItem (CoMenuBuilder menuBuilder, CoPopupMenu menu) {
			return menu;
		}		
		public void handleAddElementAction(CoAddElementData elementData) {
		}
		protected AbstractRemoveElementsCommand createRemoveElementsCommand() {
			return new AbstractRemoveElementsCommand(CoDefaultTreeCatalogUI.this) {
				protected void doRemoveSelectedElementFrom(CoTreeCatalogElementIF element, CoTreeCatalogElementIF parent) {
				}
			};
		}	
	}


	// support for selection of created nodes
	private Object m_recentlyCreated = null;
	private TreePath m_pathToParentOfRecentlyCreated = null;
public CoDefaultTreeCatalogUI() {
	this(null);
}
public CoDefaultTreeCatalogUI(CoObjectIF domain)
{
	super(domain);
	initialize();

}
public void clearRecentlyCreated()
{
	m_recentlyCreated = null;
	m_pathToParentOfRecentlyCreated = null;
}
/**
 * Overriden from CoAbstractTreeCatalogUI to produce a subcanvas without insets.HMMM...
 * @see CoAbstractTreeCatalogUI#createSubcanvasPanel
 */
protected CoPanel createSubcanvasPanel(CoUserInterfaceBuilder builder) {
	CoPanel panel = builder.createPanel(new BorderLayout());
	panel.add(builder.createScrollPane(createSelectedElementSubcanvas()), BorderLayout.CENTER);
	return panel;
}
protected CoAbstractTreeCatalogEditor createTreeCatalogEditor() {
	return new NullEditor();
}
protected CoTreeValueable createTreeCatalogHolder() {
	CoAbstractTreeAspectAdaptor tValueModel = new CoAbstractTreeAspectAdaptor(getRootHolder(), "TREE") {
		protected CoAbstractTreeModel createTreeModel() {
			return new CoAbstractTreeAspectAdaptor.TreeModel() {
				public List getElementsFor(Object element) {
					debug();
					return ((CoTreeCatalogElementIF )element).getElements();
				}
			};
		}
	};

	getUIBuilder().addTreeAspectAdaptor(tValueModel);
	return tValueModel;
}
/**
 * Returns a CoPanel cotaining the treeview.
 * Overriden from CoAbstractTreeCatalogUI to produce a subcanvas without insets. HMMM
 * @see CoAbstractTreeCatalogUI#createTreePanel
 */
protected CoPanel createTreePanel(CoUserInterfaceBuilder builder ) {
	CoPanel tPanel = builder.createPanel(new BorderLayout());
	tPanel.add(createTreeBox(), BorderLayout.CENTER);
	return tPanel;
}
protected CoDomainUserInterface createUserInterfaceFor(CoFactoryElementIF domain) {
	if (domain != null) {
		CoDomainUserInterface ui = getUserInterfaceFor(domain.getFactoryKey());
		if (ui != null) {
			ui.buildForComponent();
			ui.setDomain(domain);
			return ui;
		}
	} 
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (2000-05-24 13:51:36)
 * @return boolean
 */
private boolean debug() {
	return false;
}
public TreePath getPathOfRecentlyCreated()
{
	return ( m_recentlyCreated == null ) ? null : m_pathToParentOfRecentlyCreated.pathByAddingChild( m_recentlyCreated );
}
private CoDomainUserInterface getUserInterfaceFor(String key) {
	return (CoDomainUserInterface )userInterfaceCache.get(key);
}
//Needs to be implemented by subclasses
protected abstract void initialize();
public void setRecentlyCreated( Object item, TreePath parent )
{
	m_recentlyCreated = item;
	m_pathToParentOfRecentlyCreated = parent;
}
}