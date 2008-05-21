package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.tree.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.browser.client.*;
import com.bluebrim.browser.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.client.transfer.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.observable.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.transact.shared.*;

/**
 * UI for browsing and editing a page item prototype tree.
 * 
 * @author: Dennis
 */

public class CoPageItemPrototypeTreeUI
	extends CoDefaultTreeCatalogUI
	implements CoContextAcceptingUI, CoPageItemDragSource {
	public static String ADD = "CoPageItemPrototypeTreeUI.ADD";
	public static String REMOVE = "CoPageItemPrototypeTreeUI.REMOVE";
	public static String RENAME = "CoPageItemPrototypeTreeUI.RENAME";
	public static String CUT = "CoPageItemPrototypeTreeUI.CUT";
	public static String COPY = "CoPageItemPrototypeTreeUI.COPY";
	public static String PASTE = "CoPageItemPrototypeTreeUI.PASTE";
	public static String ADD_PROTOTYPE = "CoPageItemPrototypeTreeUI.ADD_PROTOTYPE";

	private CoPageItemPrototypeUI m_pageItemPrototypeUI;
	private List m_prototypes;
	private CoUIContext m_uiContext;
	private CoPageItemHolderPaletteWithWorkspaceUI m_prototypeCollectionUI;

	private CoDialog m_renameDialog;
	private CoTextField m_nameText;

	private CoRenameTextField m_renameTextField;

	private static List m_nodeClipBoard = new ArrayList();
	private static List m_leafClipBoard = new ArrayList();

	protected class Editor extends CoAbstractTreeCatalogEditor {
		private JMenuItem m_add;
		private JMenuItem m_remove;
		private JMenuItem m_rename;
		private JMenuItem m_cut;
		private JMenuItem m_copy;
		private JMenuItem m_paste;

		private CoSubMenu m_addPrototype;

		public Editor() {
			super(CoPageItemPrototypeTreeUI.this);
		}

		public void handleAddElementAction(CoAddElementData elementData) {
		}
		protected JComponent createAddItem(CoMenuBuilder menuBuilder, CoPopupMenu menu) {
			return menu;
		}
		protected JComponent createRemoveItem(CoMenuBuilder menuBuilder, CoPopupMenu menu) {
			return menu;
		}
		public AbstractRemoveElementsCommand createRemoveElementsCommand() {
			return null;
		}

		protected void enableDisableMenuItems() {
			CoCatalogElementIF[] e = getSelectedTreeElements();

			if (e == null || e.length == 0) {
				m_add.setEnabled(false);
				m_remove.setEnabled(false);
				m_rename.setEnabled(false);
				m_cut.setEnabled(false);
				m_copy.setEnabled(false);
				m_paste.setEnabled(false);
				m_addPrototype.setEnabled(false);
			} else {

				int I = e.length;

				boolean deletable = (I > 0);
				boolean renameable = (I == 1);
				boolean addable = (I == 1);

				for (int i = 0; i < I; i++) {
					if (e[i] instanceof CoPageItemPrototypeIF) {
						if (!((CoPageItemPrototypeIF) e[i]).isDeleteable())
							deletable = false;
					} else {
						if (!((CoPageItemPrototypeTreeNodeRIF) e[i]).isDeleteable())
							deletable = false;
					}
				}

				if (e[0] instanceof CoPageItemPrototypeIF) {
					if (!((CoPageItemPrototypeIF) e[0]).isRenameable())
						renameable = false;
					addable = false;
				} else {
					if (!((CoPageItemPrototypeTreeNodeRIF) e[0]).isRenameable())
						renameable = false;
					if (!((CoPageItemPrototypeTreeNodeRIF) e[0]).isEditable())
						addable = false;
				}

				m_add.setEnabled(addable);
				m_remove.setEnabled(deletable);
				m_rename.setEnabled(renameable);
				m_cut.setEnabled(deletable);
				m_copy.setEnabled(I > 0);
				m_paste.setEnabled(addable && (!m_nodeClipBoard.isEmpty() || !m_leafClipBoard.isEmpty()));

				addable = (e.length == 1) && (e[0] instanceof CoPageItemPrototypeTreeNodeRIF);
				m_addPrototype.setEnabled(addable);
			}
		}

		protected void createEditMenuItems(CoPopupMenu menu, CoMenuBuilder menuBuilder) {
			super.createEditMenuItems(menu, menuBuilder);
			m_add = menuBuilder.addPopupMenuItem(menu, CoPageItemUIStringResources.getName(ADD), new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addNode();
				}
			});
			m_remove =
				menuBuilder.addPopupMenuItem(menu, CoPageItemUIStringResources.getName(REMOVE), new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					removeNodes();
				}
			});
			m_rename =
				menuBuilder.addPopupMenuItem(menu, CoPageItemUIStringResources.getName(RENAME), new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					renameItem();
				}
			});
			menuBuilder.addSeparator(menu);
			m_cut = menuBuilder.addPopupMenuItem(menu, CoPageItemUIStringResources.getName(CUT), new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cut();
				}
			});
			m_copy =
				menuBuilder.addPopupMenuItem(menu, CoPageItemUIStringResources.getName(COPY), new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					copy();
				}
			});
			m_paste =
				menuBuilder.addPopupMenuItem(menu, CoPageItemUIStringResources.getName(PASTE), new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					paste();
				}
			});

			m_addPrototype = menuBuilder.addPopupSubMenu(menu, CoPageItemUIStringResources.getName(ADD_PROTOTYPE));
			menu.remove(m_addPrototype);
			menu.insert(m_addPrototype, 1);

			Iterator i = m_prototypes.iterator();
			while (i.hasNext()) {
				CoShapePageItemIF prototype = (CoShapePageItemIF) i.next();
				menuBuilder.addMenuItem(m_addPrototype, new Adder(prototype));
			}
		}

	}

	private class Adder extends AbstractAction {
		CoShapePageItemIF m_prototype;

		public Adder(CoShapePageItemIF p) {
			super(p.getType());
			m_prototype = p;
		}

		public void actionPerformed(ActionEvent e) {
			addPrototype(m_prototype);
		}
	}

	public CoPageItemPrototypeTreeUI(List prototypes, CoObjectIF pageItemPrototypeTreeRoot, CoUIContext uiContext) {
		this(prototypes, (CoLayoutEditorDialog) null, uiContext);

		setDomain(pageItemPrototypeTreeRoot);
	}

	public CoPageItemPrototypeTreeUI(List prototypes, CoLayoutEditorDialog editor, CoUIContext uiContext) {
		super();

		m_prototypes = prototypes;

		setUIContext(uiContext);

		m_pageItemPrototypeUI = new CoPageItemPrototypeUI(editor, uiContext);
		userInterfaceCache.put(CoPageItemPrototypeIF.FACTORY_KEY, m_pageItemPrototypeUI);

		m_prototypeCollectionUI =
			new CoPageItemHolderPaletteWithWorkspaceUI(
				null,
				editor,
				uiContext,
				CoPageItemView.DETAILS_EVERYTHING,
				true,
				false);

		CoTransActionCommand[] addCommands =
			(prototypes.isEmpty()) ? null : new CoTransActionCommand[prototypes.size()];

		class AddCommand extends CoTransActionCommand {
			CoShapePageItemIF m_prototype;

			AddCommand(CoShapePageItemIF p) {
				super(p.getName());
				m_prototype = p;
			}

			public boolean doExecute() {
				createNewPrototype(m_prototype);
				return true;
			}
		};

		for (int i = 0; i < addCommands.length; i++) {
			addCommands[i] = new AddCommand((CoShapePageItemIF) prototypes.get(i));
		}

		CoTransActionCommand removeCommand = new CoTransActionCommand("Ta bort") {
			public boolean doExecute() {
				((CoPageItemPrototypeTreeRootIF) getDomain()).remove(m_prototypeCollectionUI.getSelectedHolders());
				return true;
			}
		};

		CoPageItemHolderPaletteUI.Renamer renamer = new CoPageItemHolderPaletteUI.Renamer() {
			protected void rename(CoRenameable holder, String name) {
				((CoPageItemPrototypeTreeRootIF) getDomain()).rename((CoPageItemPrototypeIF) holder, name);
			}
		};

		m_prototypeCollectionUI.setActions(addCommands, removeCommand, renamer);
		userInterfaceCache.put(CoPageItemPrototypeCollectionIF.FACTORY_KEY, m_prototypeCollectionUI);

		//		new CoDefaultPageItemDragSourceListener( this, this, true );

	}

	private void addNode() {
		final CoTreeCatalogElementIF n[] = getSelectedTreeElements();

		if (n.length == 1) {
			CoCommand c = new CoCommand("ADD PAGE ITEM PROTOTYPE TREE NODE") {
				public boolean doExecute() {
					CoPageItemPrototypeTreeNodeRIF tmp =
						((CoPageItemPrototypeTreeRootIF) getDomain()).addTo(
							(CoPageItemPrototypeTreeNodeRIF) n[0],
							CoStringResources.getName(CoConstants.UNTITLED));
					setRecentlyCreated(tmp, getTreeComponent().getSelectionPath());
					return true;
				}
			};
			CoTransactionUtilities.execute(c, getDomain());
		}

	}

	private void addPrototype(final CoShapePageItemIF p) {
		final CoTreeCatalogElementIF n[] = getSelectedTreeElements();

		if (n.length == 1) {
			CoCommand c = new CoCommand("ADD PAGE ITEM PROTOTYPE") {
				public boolean doExecute() {
					((CoPageItemPrototypeTreeRootIF) getDomain()).addTo(
						(CoPageItemPrototypeTreeNodeRIF) n[0],
						p.getType(),
						"",
						(CoShapePageItemIF) p.deepClone());
					return true;
				}
			};
			CoTransactionUtilities.execute(c, getDomain());
		}
	}

	private void copy() {
		final CoFactoryElementIF n[] = getSelectedTreeElements();

		if (n.length > 0) {
			m_nodeClipBoard.clear();
			m_leafClipBoard.clear();
			for (int i = 0; i < n.length; i++) {
				if (n[i] instanceof CoPageItemPrototypeTreeNodeRIF) {
					m_nodeClipBoard.add(((CoPageItemPrototypeTreeNodeRIF) n[i]).deepClone());
				} else {
					m_leafClipBoard.add(((CoPageItemPrototypeIF) n[i]).deepClone());
				}
			}
		}
	}

	protected void createListeners() {
		super.createListeners();

		(new CoDefaultServerObjectListener(this)).initialize();
	}

	protected Object createRootObject() {
		return getDomain();
	}

	protected CoPanel createSubcanvasPanel(CoUserInterfaceBuilder builder) {
		CoPanel p = builder.createPanel(new BorderLayout(), true, new Insets(4, 4, 4, 4));
		p.add(createSelectedElementSubcanvas(), BorderLayout.CENTER);
		return p;
	}

	protected CoAbstractTreeCatalogEditor createTreeCatalogEditor() {
		return new Editor();
	}

	protected CoDomainUserInterface createUserInterfaceFor(CoFactoryElementIF domain) {
		if (domain instanceof CoPageItemPrototypeTreeNodeRIF) {
			return super.createUserInterfaceFor(
				(domain == null) ? null : ((CoPageItemPrototypeTreeNodeRIF) domain).getPrototypes());
		} else {
			return super.createUserInterfaceFor(domain);
		}
	}

	private void cut() {
		final CoFactoryElementIF n[] = getSelectedTreeElements();

		if (n.length > 0) {
			copy();

			CoCommand c = new CoCommand("REMOVE PAGE ITEM PROTOTYPE TREE NODES") {
				public boolean doExecute() {
					((CoPageItemPrototypeTreeRootIF) getDomain()).remove(java.util.Arrays.asList(n));
					return true;
				}
			};
			CoTransactionUtilities.execute(c, getDomain());
		}
	}

	public CoGenericUIContext getCopyOfCurrentRequiredUIContext() {
		return m_pageItemPrototypeUI.getCopyOfCurrentRequiredUIContext();
	}

	private CoDialog getRenameDialog() {
		if (m_renameDialog == null) {
			m_renameDialog = new CoDialog((Frame) getWindow(), "", true);

			m_nameText = getUIBuilder().createTextField(CoTextField.LEFT, 20);

			m_renameDialog.getContentPane().add(m_nameText);
			m_renameDialog.pack();
		}

		return m_renameDialog;
	}

	private CoRenameTextField getRenameTextField() {

		if (m_renameTextField == null) {
			m_renameTextField = new CoRenameTextField();
			getTreeComponent().add(m_renameTextField);
		}

		return m_renameTextField;
	}

	protected void initialize() {
	}
	private void paste() {
		final CoFactoryElementIF n[] = getSelectedTreeElements();

		if (n.length == 1) {
			CoCommand c = new CoCommand("ADD PAGE ITEM PROTOTYPE TREE NODES") {
				public boolean doExecute() {
					Object tmp = null;
					Iterator i = m_nodeClipBoard.iterator();
					while (i.hasNext()) {
						tmp =
							((CoPageItemPrototypeTreeRootIF) getDomain()).addTo(
								(CoPageItemPrototypeTreeNodeRIF) n[0],
								((CoPageItemPrototypeTreeNodeRIF) i.next()).deepClone());
					}

					i = m_leafClipBoard.iterator();
					while (i.hasNext()) {
						((CoPageItemPrototypeTreeRootIF) getDomain()).addTo(
							(CoPageItemPrototypeTreeNodeRIF) n[0],
							((CoPageItemPrototypeIF) i.next()).deepClone());
					}
					if (m_nodeClipBoard.size() == 1 && m_leafClipBoard.size() == 0) {
						setRecentlyCreated(tmp, getTreeComponent().getSelectionPath());
					}
					return true;
				}
			};
			CoTransactionUtilities.execute(c, getDomain());
		}
	}

	private void removeNodes() {
		final CoFactoryElementIF n[] = getSelectedTreeElements();

		if (n.length > 0) {
			CoCommand c = new CoCommand("REMOVE PAGE ITEM PROTOTYPE TREE NODES") {
				public boolean doExecute() {
					((CoPageItemPrototypeTreeRootIF) getDomain()).remove(java.util.Arrays.asList(n));
					return true;
				}
			};
			CoTransactionUtilities.execute(c, getDomain());
		}
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2001-01-23 16:43:07)
	 */
	private void renameItem() {

		//final Object n[]=getSelectedElement();// getSelectedCatalogElements();
		//if ( n.length != 1 )
		//	return;
		final CoRenameable item = (CoRenameable) getSelectedElement();
		TreeCellRenderer c = getTreeComponent().getCellRenderer();
		if (c instanceof JLabel)
			getRenameTextField().rename(
				item,
				CoRenameTextField.calculateRectForJLabel(
					getTreeComponent().getPathBounds(elementToPath(getSelectedElement())),
					(JLabel) c));
		else
			getRenameTextField().rename(item, getTreeComponent().getPathBounds(elementToPath(getSelectedElement())));

	}

	private void renameNode() {
		final CoTreeCatalogElementIF n[] = getSelectedTreeElements();

		if (n.length == 1) {
			if (n[0] instanceof CoPageItemPrototypeTreeNodeRIF) {
				renameNode((CoPageItemPrototypeTreeNodeRIF) n[0]);
			} else {
				renameNode((CoPageItemPrototypeIF) n[0]);
			}
		}
	}

	private void renameNode(final CoPageItemPrototypeIF node) {
		CoDialog d = getRenameDialog();

		m_nameText.setText(node.getName());
		m_nameText.selectAll();

		JTree t = getTreeBox().getTreeView();
		Rectangle r = t.getPathBounds(elementToPath(node));
		Point p = t.getLocationOnScreen();
		m_renameDialog.setLocation((int) (p.getX() + r.getX()), (int) (p.getY() + r.getY()));

		m_nameText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				CoCommand c = new CoCommand("RENAME PAGE ITEM PROTOTYPE") {
					public boolean doExecute() {
						((CoPageItemPrototypeTreeRootIF) getDomain()).rename(node, m_nameText.getText());
						return true;
					}
				};

				CoTransactionUtilities.execute(c, getDomain());

				m_nameText.removeActionListener(this);
				m_renameDialog.setVisible(false);
			}
		});

		m_renameDialog.setVisible(true);
	}

	public void setUIContext(CoUIContext context) {
		m_uiContext = context;
		if (m_pageItemPrototypeUI != null)
			m_pageItemPrototypeUI.setUIContext(context);
	}

	public void valueHasChanged() {
		super.valueHasChanged();

		JTree t = getTreeComponent();
		TreePath[] tps = t.getSelectionPaths();

		List expanded = collectExpandedNodes();

		getTreeCatalogHolder().reload();

		restoreExpandedNodes(expanded);

		TreePath tp = getPathOfRecentlyCreated();
		if (tp != null) {
			t.setSelectionPath(tp);
			clearRecentlyCreated();
		} else {
			t.setSelectionPaths(tps);
		}

	}

	public boolean canStartDrag() {
		CoTreeCatalogElementIF e = getSelectedElement();

		return (e != null) && (e instanceof CoPageItemPrototypeIF);
	}

	private void createNewPrototype(CoShapePageItemIF prototype) {
		((CoPageItemPrototypeTreeRootIF) getDomain()).addTo(
			(CoPageItemPrototypeTreeNodeRIF) getSelectedElement(),
			prototype.getName(),
			"",
			(CoShapePageItemIF) prototype.deepClone());
	}

	protected void doAfterCreateUserInterface() {
		super.doAfterCreateUserInterface();

		new CoDefaultPageItemDragSourceListener(getTreeBox().getTreeView(), this, true);
	}

	public CoShapePageItemIF getSnappingPageItem() {
		return ((CoPageItemPrototypeIF) getSelectedElement()).getPageItem();
	}

	public CoShapePageItemView getSnappingPageItemView() {
		return null;
	}

	public List getTransferablePageItems() {
		return ((CoPageItemPrototypeIF) getSelectedElement()).getPageItems();

	}

	public List getTransferablePageItemViews() {
		return null;
	}

	private void renameNode(final CoPageItemPrototypeTreeNodeRIF node) {
		CoDialog d = getRenameDialog();

		m_nameText.setText(node.getName());
		m_nameText.selectAll();

		JTree t = getTreeBox().getTreeView();
		Rectangle r = t.getPathBounds(elementToPath(node));
		Point p = t.getLocationOnScreen();
		m_renameDialog.setLocation((int) (p.getX() + r.getX()), (int) (p.getY() + r.getY()));

		m_nameText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				CoCommand c = new CoCommand("RENAME PAGE ITEM PROTOTYPE TREE NODE") {
					public boolean doExecute() {
						((CoPageItemPrototypeTreeRootIF) getDomain()).rename(node, m_nameText.getText());
						return true;
					}
				};

				CoTransactionUtilities.execute(c, getDomain());

				m_nameText.removeActionListener(this);
				m_renameDialog.setVisible(false);
			}
		});

		m_renameDialog.setVisible(true);
	}
}