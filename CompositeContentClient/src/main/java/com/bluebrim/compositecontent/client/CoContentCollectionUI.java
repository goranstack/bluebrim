package com.bluebrim.compositecontent.client;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.io.*;
import java.util.*;
import java.util.List;

import com.bluebrim.base.client.datatransfer.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.browser.client.*;
import com.bluebrim.browser.shared.*;
import com.bluebrim.content.client.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.image.client.*;
import com.bluebrim.image.impl.client.*;
import com.bluebrim.image.shared.*;
import com.bluebrim.layout.client.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.client.*;
import com.bluebrim.text.impl.client.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * UI for displaying content collection.
 * See CoPageItemUI for details on supplying a layout editor.
 * Creation date: (2000-11-24 14:38:56)
 * @author Dennis Malmström
 * @author Peter Jakubicki
 * @author Dennis Malmström (2001-08-30, DnD)
 * @author Markus Persson 2001-09-20 (DnD)
 */
public class CoContentCollectionUI extends CoAbstractCatalogUI implements CoContextAcceptingUI, CoDnDDataProvider, CoContentUIConstants {
	protected CoImageContentUI m_imageContentUI;
	protected CoTextContentUI m_textContentUI;
	protected CoContextAcceptingUI m_layoutContentUI;
	protected CoWorkPieceUI m_workPieceUI;
	private static final DataFlavor[] m_allDropFlavors = new DataFlavor[] { CoTextClientConstants.TEXT_CONTENT_FLAVOR, CoImageClientConstants.IMAGE_CONTENT_FLAVOR, CoLayoutClientConstants.LAYOUT_CONTENT_FLAVOR, CoCompositeContentClientConstants.WORK_PIECE_FLAVOR, CoAbstractContentClientConstants.CONTENT_FLAVOR, DataFlavor.javaFileListFlavor, DataFlavor.imageFlavor };
	private static final DataFlavor[] m_atomicDropFlavors = new DataFlavor[] { CoTextClientConstants.TEXT_CONTENT_FLAVOR, CoImageClientConstants.IMAGE_CONTENT_FLAVOR, CoLayoutClientConstants.LAYOUT_CONTENT_FLAVOR };

	protected static class ContentRenderer extends CoCatalogListCellRenderer {
		CoViewIcon m_viewIcon;

		public ContentRenderer(int w, int h) {
			super();
			m_viewIcon = new CoViewIcon(w, h);
		}

		protected void setValue(Object value, int index, boolean isSelected, boolean cellHasFocus) {
			if (value instanceof CoImageContentIF) {
				CoImageContentIF image = (CoImageContentIF) value;
				setText(image.getName());
				m_viewIcon.setView(((CoViewable) image).getView());
				setIcon(m_viewIcon);
			} else {
				super.setValue(value, index, isSelected, cellHasFocus);
			}
		}
	}

	public CoContentCollectionUI(CoObjectIF contentCollection, CoUIContext uiContext) {
		this((CoLayoutEditorDialog) null, uiContext);
	}

	private CoContentCollectionUI(CoLayoutEditorDialog editor, CoUIContext uiContext, CoWorkPieceUI workPieceUI ) {
		super();

		m_textContentUI = new CoTextContentUI(uiContext);
		m_imageContentUI = new CoImageContentUI(uiContext);
		m_layoutContentUI = new CoLayoutContentUI(editor, uiContext);
		m_workPieceUI = workPieceUI;

	}

	public CoContentCollectionUI(CoLayoutEditorDialog editor, CoUIContext uiContext) {
		this(editor, uiContext, new CoWorkPieceUI(editor, uiContext));
	}

	boolean canHandleNonAtomicContens() {
		return m_workPieceUI != null;
	}

	protected CoListCatalogEditor createCatalogEditor() {
		return new CoContentCollectionEditor(this);
	}

	protected CoListValueable.Mutable createCatalogHolder() {
		return new CoAbstractListAspectAdaptor.Mutable(this, "CONTENTS") {
			protected Object get(CoObjectIF subject) {
				return ((CoContentCollectionIF) subject).getContents();
			}

			private CoContentCollectionIF getContentCollection() {
				return (CoContentCollectionIF) getSubject();
			}

			public CoAbstractListModel.Mutable createListModel() {
				return new CoCollectionListModel.Mutable(this) {
					protected boolean doRemoveElement(Object element) {
						return true;
					}
					protected void doAddElement(Object element) {
					}
				};
			}
		};
	}

	protected CoListBox createCatalogListBox(CoUserInterfaceBuilder b) {
		CoListBox lb = super.createCatalogListBox(b);
		lb.getList().setFixedCellHeight(42);
		lb.getList().setCellRenderer(new ContentRenderer(40, 40));
		return lb;

		/*
		CoListBox tListBox = builder.createListBox(new ContentRenderer(40, 40, builder.getDefaults()), "ELEMENTS");
		Dimension tPreferredSize = new Dimension(150, 150);
		tListBox.setPreferredSize(tPreferredSize);
		tListBox.setMinimumSize(tPreferredSize);
		JList tList = tListBox.getList();
		tList.setFixedCellHeight(42);
		tList.setFixedCellWidth(150);
		return tListBox;
		*/
	}
	
	protected void createWidgets(CoPanel aPanel, CoUserInterfaceBuilder builder) {
		super.createWidgets(aPanel, builder);
		CoSplitPane splitPane = (CoSplitPane)builder.getNamedWidget("SPLIT_PANE");
		splitPane.setProportionalSplit(true);
		splitPane.setDividerLocation(0.25);
		splitPane.setContinuousLayout(false); // Continous rescale of images is to slow		
	}

	public String getDefaultWindowTitle() {
		return CoContentUIResources.getName(CONTENT_COLLECTION);
	}

	public Rectangle getDefaultBounds() {
		return CoGUI.centerOnScreen(800, 500);
	}


	protected Map createElementUIMap() {
		Map uiMap = new HashMap();

		// NOTE: Needed for now since not all state for these
		// are carried by CoUIContext. (Must also context enable
		// CoAbstractCatalogUI to remove these.) /Markus 2001-10-15
		uiMap.put(CoImageContentIF.FACTORY_KEY, m_imageContentUI);
		uiMap.put(CoTextContentIF.FACTORY_KEY, m_textContentUI);
		uiMap.put(CoLayoutContentIF.FACTORY_KEY, m_layoutContentUI);
		uiMap.put(CoWorkPieceIF.FACTORY_KEY, m_workPieceUI);
		return uiMap;
	}

	// Should be called by CoWorkPieceUI only

	public static CoContentCollectionUI createForWorkPieceUI(CoLayoutEditorDialog editor, CoUIContext uiContext ) {
		return new CoContentCollectionUI(editor, uiContext, null );
	}

	protected CoContentCollectionIF getContentCollection() {
		return (CoContentCollectionIF) getDomain();
	}

	public CoGenericUIContext getCopyOfCurrentRequiredUIContext() {
		return m_layoutContentUI.getCopyOfCurrentRequiredUIContext();
	}

	private DataFlavor[] getDropDataFlavors() {
		return canHandleNonAtomicContens() ? m_allDropFlavors : m_atomicDropFlavors;
	}

	public Transferable getTransferableFor(Object[] rawSelection) {
		// NOTE: This method (and the interface it implements) wouldn't
		// be neccessary if the limitation to only drag one item was
		// removed. An extension to the DnD framework handling this
		// common case could easily be done, but it can be confusing
		// for the user. If multiple selections are allowed, why can
		// they not be dragged? (Dropped somewhere specific is a
		// different matter!) One way, if desirable, is to only allow
		// single selection in the list. Then this method would not be
		// needed. /Markus 2001-09-20
		if (rawSelection.length == 1) {
			return CoDataTransferKit.getTransferableFor(rawSelection);
		} else {
			// Prevent drag
			return null;
		}
	}

	protected void handleDrop(final DropTargetDropEvent ev, final int pos) {
		CoCommand c = new CoCommand("DROP CONTENTS") {
			public boolean doExecute() {
				handleDrop((CoContentCollectionIF) getDomain(), ev.getTransferable(), pos);
				return true;
			}

			public void finish() {
				super.finish();
				getCatalogList().clearSelection();
			}
		};

		CoTransactionUtilities.execute(c, getDomain());
	}
	
	private void handleDrop(CoContentCollectionIF contentCollection, Transferable transferable, int pos)
    {
        try
        {
            Object content = transferable.getTransferData(CoAbstractContentClientConstants.CONTENT_FLAVOR);
            if (content instanceof CoContentIF) 
                contentCollection.addContent((CoContentIF) content);
        } catch (UnsupportedFlavorException e)
        {
        } catch (IOException e)
        {
        }

        try
        {
            Object fileList = transferable.getTransferData(DataFlavor.javaFileListFlavor);
            if (fileList instanceof List) 
                addContent(contentCollection, (List) fileList);
        } catch (UnsupportedFlavorException e)
        {
        } catch (IOException e)
        {
        }

    }
	
	/**
	 * Assume image files but should be generalized to handle all kinds if file based
	 * content i.e. text files.
	 * 
	 * @param fileList
	 */
	private void addContent(CoContentCollectionIF contentCollection, List fileList)
	{
	    Iterator iterator = fileList.iterator();
	    while (iterator.hasNext())
        {
            File file = (File) iterator.next();
            CoImageContentIF imageContent = CoImageClient.getInstance().createImageContent(file);
            contentCollection.addContent(imageContent);          
        }
	}


	protected CoCatalogElementIF newCatalogElement() {
		return null;
	}

	protected void prepareDrag() {
		getCatalogList().prepareDrag(DnDConstants.ACTION_COPY_OR_MOVE, null);
	}

	public void prepareDragAndDrop() {
		prepareDrag();
		prepareDrop();
	}

	protected void prepareDrop() {
		// drop
		CoList component = getCatalogList();
		CoDropTargetListener dtl = new CoListDropTargetListener(component, getDropDataFlavors()) {
		
			public boolean canEndDrag(DropTargetDragEvent ev) {
				return true;
			}

			protected void handleDrop(DropTargetDropEvent ev) {
				CoContentCollectionUI.this.handleDrop(ev, getDropPosition());
			}
		};

		new DropTarget(component, dtl);
	}

	protected void prepareTransferData(Object[] transferData) {
	}

	public void setUIContext(CoUIContext context) {
		m_layoutContentUI.setUIContext(context);
	}

}