package com.bluebrim.compositecontent.client;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.rmi.*;
import java.text.*;

import javax.swing.*;

import com.bluebrim.base.client.datatransfer.*;
import com.bluebrim.browser.client.*;
import com.bluebrim.content.client.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.image.client.*;
import com.bluebrim.image.impl.client.*;
import com.bluebrim.image.shared.*;
import com.bluebrim.layout.client.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.text.client.*;
import com.bluebrim.transact.shared.*;

public class CoContentCollectionEditor extends CoGsListCatalogEditor {

	public final static String REMOVE = "remove";
	public final static String COPY = "copy";
	public final static String CONTENT_PASTE = "content_paste";
	public final static String PASTE = "paste";
	public final static String NEW_TEXT = "new_text";
	public final static String NEW_IMAGE = "new_image";
	public final static String NEW_LAYOUT = "new_layout";
	public final static String NEW_WORKPIECE = "new_workpiece";

	private CoImageFileChooser m_imageFileChooser = new CoImageFileChooser();
	private AbstractAction m_removeAction;
	private boolean m_canHandleNonAtomicContens;

	public CoContentCollectionEditor(CoContentCollectionUI ui) {
		super(ui);

		m_canHandleNonAtomicContens = ui.canHandleNonAtomicContens();

		m_CCPManager = new CoListCopyCutPasteManager(ui, new CoContentDataFlavorSetProvider()) {
			protected void handlePaste(Transferable clipboardContent) {
				handleContentPaste(clipboardContent);
			}
			public void enableDisablePasteItem() {
				//PasteItem
				Transferable clipboardContent = CoClipBoard.getClipboard().getContents(this);
				boolean b = clipboardContent != null && clipboardContent.isDataFlavorSupported(CoAbstractContentClientConstants.CONTENT_FLAVOR);
				m_pasteAction.setEnabled(b);

				if (m_removeAction != null)
					m_removeAction.setEnabled(getCatalogComponent().isEnabled() && getCatalogUI().hasSelectedCatalogElements());
			}

			protected void copyAction() {
				super.copyAction();
			}

		};

	}

	protected void addImages(final File[] files) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				CoWorkerMonitor.Worker worker = new CoWorkerMonitor.Worker() {
					protected Object construct() throws Exception {
						if (files != null) {
							for (int i = 0; i < files.length && !isInterrupted(); i++) {
								System.out.println(files[i].getName());
								createImage(files[i]);
							}
						}
						return null;
					}
				};

				MessageFormat msg = new MessageFormat(CoContentUIResources.getName(NEW_IMAGE));
				new CoWorkerMonitor(worker, msg.format(new String[] { "dsfjldksfj" }), true).start();
			}
		});
	}

	protected void buildAddMenu(CoMenuBuilder menuBuilder, CoMenu addMenu) {

		menuBuilder.addMenuItem(addMenu, new AbstractAction(CoContentUIResources.getName(NEW_TEXT)) {
			public void actionPerformed(ActionEvent e) {
				createTextContent();
			}
		});
		/*
		menuBuilder.addMenuItem(addMenu, new AbstractAction(CoContentUIResources.getName("NEW_TEXTS")){
			public void actionPerformed(ActionEvent e){
				Component canvas = getAbstractCatalogUI().getNamedWidget("SELECTED_ELEMENT");
				JFileChooser chooser=new JFileChooser();
				chooser.setMultiSelectionEnabled(true);
				File[] imageFiles = null;	
				if (chooser.showOpenDialog(canvas) == JFileChooser.APPROVE_OPTION){
					
					imageFiles = chooser.getSelectedFiles();
					
				}
				addTexts(imageFiles);	
			}
		});
		*/
		menuBuilder.addMenuItem(addMenu, new AbstractAction(CoContentUIResources.getName(NEW_IMAGE)) {
			public void actionPerformed(ActionEvent e) {
				Component canvas = getAbstractCatalogUI().getNamedWidget("SELECTED_ELEMENT");
				m_imageFileChooser.setMultiSelectionEnabled(true);
				File[] imageFiles = null;
				if (m_imageFileChooser.showDialog(canvas) == JFileChooser.APPROVE_OPTION) {

					imageFiles = m_imageFileChooser.getSelectedFiles();

				}

				class TempDialog extends JDialog implements ActionListener {

					boolean stop = false;
					public TempDialog(Frame f) {
						super(f, false);
						JButton b = new JButton("Avbryt");
						b.addActionListener(this);
						getContentPane().add(b);
						pack();
					}
					public void actionPerformed(ActionEvent e) {
						stop = true;
					}
					public void createImages(File[] files) {
						show();
						System.out.println(files);
						if (files != null) {
							for (int i = 0; i < files.length && !stop; i++) {
								System.out.println(files[i].getName());
								CoContentCollectionEditor.this.createImage(files[i]);
							}
						}
						dispose();
					}

				};

				if (imageFiles != null) {
					addImages(imageFiles);
				}
			}
		});

		menuBuilder.addMenuItem(addMenu, new AbstractAction(CoContentUIResources.getName(NEW_LAYOUT)) {
			public void actionPerformed(ActionEvent e) {
				createLayoutContent();
			}
		});

		if (m_canHandleNonAtomicContens) {
			menuBuilder.addMenuItem(addMenu, new AbstractAction(CoContentUIResources.getName(NEW_WORKPIECE)) {
				public void actionPerformed(ActionEvent e) {
					createWorkPiece();
				}
			});
		}

	}

	protected JComponent createAddItem(CoMenuBuilder menuBuilder, CoPopupMenu menu) {
		CoSubMenu addMenu = menuBuilder.addPopupSubMenu(menu, getCatalogUI().getAddItemLabel());
		buildAddMenu(menuBuilder, addMenu);
		return addMenu;
	}

	protected void createEditMenuItems(CoPopupMenu menu, CoMenuBuilder menuBuilder) {
		createAddItem(menuBuilder, menu);

		if (m_CCPManager != null)
			m_CCPManager.createCopyPasteCutItems(menu, menuBuilder);

		createRemoveItem(menuBuilder, menu);

/* 
 * This rather specific action should be installed created and installed by
 * the class that use this UI. That way we can maintain a more strict modularity
 * Comment out until we have a new design for UI that also includes the possibility
 * to install actions with from using class. Göran Stäck 2002-11-11
 

		menuBuilder.addPopupMenuItem(menu, new AbstractAction(CoUIStringResources.getName(CoUIConstants.SAVE_AS_XML)) {
			public void actionPerformed(ActionEvent e) {
				CoContentCollectionIF cc = getContentCollection();
				if (cc != null)
					saveXml(cc);
			}
		});

		menuBuilder.addPopupMenuItem(menu, new AbstractAction(CoUIStringResources.getName(CoUIConstants.LOAD_FROM_XML)) {
			public void actionPerformed(ActionEvent e) {
				loadXml();
			}
		});
*/
	}

	private void createImage(final File imageFile) {

		CoTransactionUtilities.execute(new CoCommand(NEW_IMAGE) {
			public boolean doExecute() {
				CoImageContentIF imageContent = null;
				try {
					imageContent = CoImageClient.getInstance().createImageContent(imageFile);
					getContentCollection().addContent(imageContent);
					imageContent.setName(imageFile.getName());
					return imageContent != null ? getAbstractCatalogUI().addElement(imageContent) != null : false;
				} catch (Exception e) {
					return false;
				}
			}
			public void finish() {
				super.finish();
			}
		}, null);
	}
	
	private void createLayoutContent() {

		CoTransactionUtilities.execute(new CoCommand(NEW_LAYOUT) {
			public boolean doExecute() {
				CoContentCollectionIF contentCollection = getContentCollection();
				CoContentIF layoutContent;
                try
                {
                    layoutContent = CoLayoutClient.getLayoutServer().createLayoutContent( contentCollection.getLayoutParameters());
                } catch (RemoteException e)
                {
                    return false;
                }
                contentCollection.addContent(layoutContent);
				getAbstractCatalogUI().addElement(layoutContent);
				return true;
			}
			public void finish() {
				super.finish();
			}
		}, null);

	}
	
	protected JComponent createRemoveItem(CoMenuBuilder menuBuilder, CoPopupMenu menu) {
		JComponent cmp = menuBuilder.addPopupMenuItem(menu, m_removeAction = new AbstractAction(CoContentUIResources.getName(REMOVE)) {
			public void actionPerformed(ActionEvent e) {
				remove();
			}
		});

		menuBuilder.addSeparator(menu);

		return cmp;

	}
	
	private void createTextContent() {

		CoTransactionUtilities.execute(new CoCommand(NEW_TEXT) {
			public boolean doExecute() {
				CoContentCollectionIF contentCollection = getContentCollection();
				CoContentIF textContent;
                try
                {
                    textContent = CoTextClient.getTextServer().createTextContent( null);
                } catch (RemoteException e)
                {
                    return false;
                }
                contentCollection.addContent(textContent);
				getAbstractCatalogUI().addElement(textContent);
				return true;
			}
			public void finish() {
				super.finish();
			}
		}, null);

	}
	
	private void createWorkPiece() {

		CoTransactionUtilities.execute(new CoCommand(NEW_WORKPIECE) {
			public boolean doExecute() {
				CoContentCollectionIF contentCollection = getContentCollection();
				CoContentIF workPiece;
                try
                {
                    workPiece = CoCompositeContentClient.getCompositeContentServer().createWorkPiece( contentCollection.getLayoutParameters());
                } catch (RemoteException e)
                {
                    return false;
                }
                contentCollection.addContent(workPiece);
				getAbstractCatalogUI().addElement(workPiece);
				return true;
			}
			public void finish() {
				super.finish();
			}
		}, null);

	}
	
	public void enableDisableMenuItems() {

		super.enableDisableMenuItems();
		boolean hasSelectedItems = getCatalogUI().hasSelectedCatalogElements();

		if (m_removeAction != null)
			m_removeAction.setEnabled(getCatalogComponent().isEnabled() && hasSelectedItems);
	}
	
	private CoAbstractCatalogUI getAbstractCatalogUI() {
		return (CoAbstractCatalogUI) m_catalogUI;
	}
	
	private CoContentCollectionIF getContentCollection() {
		return (CoContentCollectionIF) getAbstractCatalogUI().getDomain();
	}
	
	private CoContentCollectionUI getContentCollectionUI() {

		return (CoContentCollectionUI) getCatalogUI();
	}
	
	private CoFrame getCurrentFrame() {
		return getAbstractCatalogUI().getCurrentFrame();
	}

	private void handleContentPaste(final Transferable content) {

		CoTransactionUtilities.execute(new CoCommand(CONTENT_PASTE) {
			Object[] m_contents;

			public void prepare() {
				try {
					m_contents = (Object[]) content.getTransferData(CoAbstractContentClientConstants.CONTENT_FLAVOR);

				} catch (UnsupportedFlavorException e) {
				} catch (IOException e1) {
				}
				super.prepare();
			}
			private boolean hasContent() {
				return m_contents != null;
			}
			public boolean doExecute() {
				if (hasContent()) {
					for (int i = 0; i < m_contents.length; i++) {
						CoContentIF c = (CoContentIF) m_contents[i];
						((CoContentCollectionIF) getContentCollectionUI().getDomain()).addContent(c.copy());
					}
				}
				return hasContent();
			}
		}, getContentCollectionUI().getDomain());

	}
	
	public void handleRemoveElementAction() {
	}

/* 
 * This rather specific action should be installed created and installed by
 * the class that use this UI. That way we can maintain a more strict modularity
 * Comment out until we have a new design for UI that also includes the possibility
 * to install actions with from using class. Göran Stäck 2002-11-11
 
	private void loadXml() {
		CoXmlContext context = new CoXmlContext();
		final Object model = CoSystemUIUtilities.selectAndLoadXml(getCurrentFrame(), context, false);
		if (model != null) {
			try {
				CoTransactionUtilities.execute(new CoCommand("CONTENT COLLECTION UPDATE") {
					public boolean doExecute() {
						Iterator contents = ((CoContentCollectionIF) model).getContents().iterator();
						while (contents.hasNext())
							getContentCollection().addContent((CoContentIF) contents.next());
						return true;
					}
					public void finish() {
						super.finish();
					}
				}, null);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	private void saveXml(CoXmlExportEnabledIF topObject) {
		CoXmlContext context = new CoXmlContext();
		CoSystemUIUtilities.selectAndSaveXml(getCurrentFrame(), context, topObject);
	}

*/
	private void remove() {

		CoTransactionUtilities.execute(new CoCommand(REMOVE) {
			public boolean doExecute() {
				getContentCollection().removeContents(getAbstractCatalogUI().getSelectedCatalogElements());
				return true;
			}
			public void finish() {
				super.finish();
				getAbstractCatalogUI().getCatalogListBox().getList().clearSelection();
			}
		}, null);

	}

}