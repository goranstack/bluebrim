package com.bluebrim.browser.client;
import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.base.shared.CoStringResources;
import com.bluebrim.browser.shared.CoAddElementData;
import com.bluebrim.browser.shared.CoTreeCatalogElementIF;
import com.bluebrim.gui.client.CoAbstractTreeAspectAdaptor;
import com.bluebrim.gui.client.CoDomainUserInterface;
import com.bluebrim.gui.client.CoTreeValueable;
import com.bluebrim.gui.client.CoUserInterfaceBuilder;
import com.bluebrim.menus.client.CoMenuBuilder;
import com.bluebrim.menus.client.CoPopupMenu;
import com.bluebrim.swing.client.CoPanel;
import com.bluebrim.swing.client.CoTreeBox;

/**
 * Abstract superclass for ui's showing a
 * tree structure.
 */
public abstract class CoBasicTreeUI extends CoDomainUserInterface implements CoTreeCatalogUI {
	protected HashMap m_editors;
	private BasicEditor m_editor;

	protected final static String ROOT = "root";
	protected final static String TREE = "tree";
	protected final static String NO_SELECTION = "no_selection";
	protected final static String MULTI_SELECTION = "multi_selection";

	public class BasicEditor extends CoAbstractTreeCatalogEditor {
		public BasicEditor(CoBasicTreeUI ui) {
			super(ui);
		}
		protected JComponent createAddItem(CoMenuBuilder menuBuilder, CoPopupMenu menu) {
			return null;
		}
		public void handleAddElementAction(CoAddElementData elementData) {
		}
		protected JComponent createRemoveItem(CoMenuBuilder menuBuilder, CoPopupMenu menu) {
			return null;
		}
		public AbstractRemoveElementsCommand createRemoveElementsCommand() {
			return null;
		}
		protected void enableDisableMenuItems() {
		}
	}

	public CoBasicTreeUI() {
		super();
	}

	public CoBasicTreeUI(CoObjectIF object) {
		super(object);
	}

	protected void buildMainPanel(CoPanel outerPanel, CoUserInterfaceBuilder builder) {
		outerPanel.add(createTreeBox(builder), getTreeBoxConstraints());
	}

	protected void createListeners() {
		super.createListeners();
		getTreeCatalogHolder().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				treeSelectionChanged(e);
			}
		});
	}

	protected abstract CoTreeBox createTreeBox(CoUserInterfaceBuilder aBuilder);

	protected abstract CoAbstractTreeAspectAdaptor createTreeCatalogHolder(CoUserInterfaceBuilder aBuilder);

	protected void createValueModels(CoUserInterfaceBuilder aBuilder) {
		super.createValueModels(aBuilder);
		aBuilder.createTreeBoxAdaptor(aBuilder.addTreeAspectAdaptor(createTreeCatalogHolder(aBuilder)), (CoTreeBox) getNamedWidget(TREE));
	}

	protected void createWidgets(CoPanel aPanel, CoUserInterfaceBuilder aBuilder) {
		super.createWidgets(aPanel, aBuilder);
		buildMainPanel(aPanel, aBuilder);
	}

	protected void doAfterCreateUserInterface() {
		super.doAfterCreateUserInterface();
		initializeEditors();
		handleSelectedElement(null);
	}

	/**
	 * Answer with a TreePath for the row displaying <code>element</code>.
	 * Return null if not found, i e if either the element isn't in the tree
	 * or else if the node isn't visible.
	 */
	public TreePath elementToPath(Object element) {
		return getTreeCatalogHolder().getPathToRootFrom(getTreeComponent(), element);
	}

	public String getAddElementItemLabel() {
		return CoStringResources.getName("ADD_ITEM");
	}

	protected BasicEditor getEditor() {
		return m_editor;
	}

	protected BasicEditor getEditorFor(String editorKey) {
		return (BasicEditor) m_editors.get(editorKey);
	}

	public String getRemoveElementItemLabel() {
		return CoStringResources.getName("REMOVE_ITEM");
	}

	/**
	 	Svarar med en array innehållande alla selekterade element.
	 */
	public CoTreeCatalogElementIF[] getSelectedTreeElements() {
		CoTreeCatalogElementIF tSelectedElements[] = null;
		TreePath tSelectionPaths[] = getTreeComponent().getSelectionPaths();
		if (tSelectionPaths != null) {
			tSelectedElements = new CoTreeCatalogElementIF[tSelectionPaths.length];
			for (int i = tSelectionPaths.length - 1; i >= 0; i--) {
				Object tPath[] = tSelectionPaths[i].getPath();
				tSelectedElements[i] = ((CoTreeCatalogElementIF) tPath[tPath.length - 1]).getTreeCatalogElement();
			}
		}
		return tSelectedElements;
	}

	public CoTreeCatalogElementIF getSingleSelectedTreeElement() {
		CoTreeCatalogElementIF tSelectedElements[] = getSelectedTreeElements();
		return ((tSelectedElements != null) && (tSelectedElements.length == 1)) ? tSelectedElements[0] : null;
	}

	protected TreePath getSingleSelectionPath() {
		TreePath tSelectionPaths[] = getTreeComponent().getSelectionPaths();
		return ((tSelectionPaths != null) && (tSelectionPaths.length == 1)) ? tSelectionPaths[0] : null;
	}

	protected String getTreeBoxConstraints() {
		return BorderLayout.CENTER;
	}

	public CoTreeValueable getTreeCatalogHolder() {
		return (CoTreeValueable) getNamedValueModel(TREE);
	}

	public final JTree getTreeComponent() {
		return ((CoTreeBox) getNamedWidget(TREE)).getTreeView();
	}

	public CoMenuBuilder getUIMenuBuilder() {
		return getMenuBuilder();
	}

	protected void handleMultiSelection() {
		installEditor(MULTI_SELECTION);
	}

	protected void handleNoSelection() {
		installEditor(NO_SELECTION);
	}

	protected void handleOneSelectedElement(CoTreeCatalogElementIF element) {
		installEditor(element.getFactoryKey());
	}

	protected void handleSelectedElement(CoTreeCatalogElementIF element) {
		if (element != null)
			handleOneSelectedElement(element);
		else if (getSelectedTreeElements() != null)
			handleMultiSelection();
		else
			handleNoSelection();
	}

	public boolean hasSelectedTreeElements() {
		CoTreeCatalogElementIF tSelectedTreeElements[] = getSelectedTreeElements();
		return ((tSelectedTreeElements != null) && (tSelectedTreeElements.length > 0));
	}

	protected void initializeEditors() {
		m_editors = new HashMap();
	}

	private void installEditor(String editorKey) {
		if (m_editor != null)
			m_editor.remove();
		m_editor = getEditorFor(editorKey);
		if (m_editor != null)
			m_editor.install();
	}

	public void reloadSingleSelectionPath() {
		getTreeCatalogHolder().reload(getSingleSelectionPath());
	}

	private void treeSelectionChanged(TreeSelectionEvent e) {
		handleSelectedElement(getSingleSelectedTreeElement());
	}
}