package com.bluebrim.compositecontent.client;

import com.bluebrim.base.client.datatransfer.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.content.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.image.client.*;
import com.bluebrim.layout.client.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.text.client.*;

/**
 * See CoPageItemUI for details on supplying a layout editor.
 * Creation date: (2000-11-01 10:31:11)
 * @author: Monika Czerska
 */
public class CoWorkPieceUI extends CoAbstractContentUI implements CoContextAcceptingUI {
	public final static String ATOMIC_CONTENT_TAB = "atomic_content_tab";

	private CoContentCollectionUI m_contentCollectionContentUI;

	public class MyTabData extends CoAbstractContentUI.MyTabData {
		public MyTabData(CoDomainUserInterface ui) {
			super(ui);
		}

		public void initializeTabData() {
			addTabData(new CoAbstractTabUI.TabData(ATOMIC_CONTENT_TAB, null, CoContentUIResources.getName(ATOMIC_CONTENT_TAB)) {
				protected CoDomainUserInterface createUserInterface() {
					return m_contentCollectionContentUI;
				}
			});

			super.initializeTabData();
		}
	}

	public CoWorkPieceUI(CoObjectIF workPiece, CoUIContext uiContext) {
		this((CoLayoutEditorDialog) null, uiContext);

		setDomain(workPiece);
	}

	public CoWorkPieceUI(CoLayoutEditorDialog editor, CoUIContext uiContext ) {
		super();

		m_contentCollectionContentUI = CoContentCollectionUI.createForWorkPieceUI(editor, uiContext );
	}

	protected void doAfterCreateUserInterface() {
		super.doAfterCreateUserInterface();

		new java.awt.dnd.DropTarget(
			getTabPane(),
			new CoTabbedPaneDropTargetListener(
				getTabPane(),
				new java.awt.datatransfer.DataFlavor[] { CoTextClientConstants.TEXT_CONTENT_FLAVOR, CoImageClientConstants.IMAGE_CONTENT_FLAVOR, CoLayoutClientConstants.LAYOUT_CONTENT_FLAVOR, CoCompositeContentClientConstants.WORK_PIECE_FLAVOR, CoAbstractContentClientConstants.CONTENT_FLAVOR },
				new String[] { CoContentUIResources.getName(ATOMIC_CONTENT_TAB)}));

	}

	public CoGenericUIContext getCopyOfCurrentRequiredUIContext() {
		return m_contentCollectionContentUI.getCopyOfCurrentRequiredUIContext();
	}

	protected CoServerTabData getTabData() {
		CoServerTabData tabData = new MyTabData(this);
		tabData.initializeTabData();
		return tabData;
	}

	protected Object[] getTabOrder() {
		return new Object[] {  ATOMIC_CONTENT_TAB };

	}

	public void setUIContext(CoUIContext context) {
		m_contentCollectionContentUI.setUIContext(context);
	}
}