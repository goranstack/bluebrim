package com.bluebrim.browser.client;

import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.bluebrim.base.client.datatransfer.CoCopyCutPasteManager;
import com.bluebrim.browser.shared.CoAddElementData;
import com.bluebrim.gui.client.CoGUI;
import com.bluebrim.gui.client.CoPopupGestureListener;
import com.bluebrim.gui.client.CoUIConstants;
import com.bluebrim.gui.client.CoUIStringResources;
import com.bluebrim.menus.client.CoMenuBuilder;
import com.bluebrim.menus.client.CoPopupMenu;

public abstract class CoAbstractCatalogEditor {
	protected CoPopupMenu m_editMenu;

	public CoAbstractCatalogEditor() {
	}

	/**
		Subklasserna instansierar och svarar med det menyval 
		som skall användas för 'Lägg till'.<br> Observera att svaret kan
		vara såväl en CoMenuItem som en CoMenu, därav JComponent som
		returklass.
	 */
	protected abstract JComponent createAddItem(CoMenuBuilder menuBuilder, CoPopupMenu menu);

	/**
		Anropas från gränssnittklassens #createPopupMenus()
		för att instansiera en popupmeny för att
		lägga till och ta bort element ur katalogen. 
	 */
	protected final CoPopupMenu createEditMenu(JComponent invoker) {
		return getMenuBuilder().createPopupMenu(invoker);
	}

	protected void createEditMenuItems(CoPopupMenu editMenu, CoMenuBuilder menuBuilder) {
		createAddItem(menuBuilder, editMenu);
		if (m_CCPManager != null)
			m_CCPManager.createCopyPasteCutItems(editMenu, menuBuilder);
		createRemoveItem(menuBuilder, editMenu);
	}

	protected void createListeners() {
		getEditMenu().addPopupMenuListener(createPopupMenuListener());
	}

	protected MouseListener createMouseListener() {
		return new CoPopupGestureListener(m_editMenu);
	}

	protected PopupMenuListener createPopupMenuListener() {
		return new PopupMenuListener() {
			public void popupMenuCanceled(PopupMenuEvent event) {
			}
			public void popupMenuWillBecomeInvisible(PopupMenuEvent event) {
			}
			public void popupMenuWillBecomeVisible(PopupMenuEvent event) {
				enableDisableMenuItems();
			}
		};
	}

	/**
		Subklasserna instansierar och svarar med det menyval 
		som skall användas för 'Ta bort'.
	 	Observera att svaret kan vara såväl en CoMenuItem som en CoMenu, 
	 	därav JComponent som returklass.
	*/
	protected abstract JComponent createRemoveItem(CoMenuBuilder menuBuilder, CoPopupMenu menu);

	/**
		Anropas från #popupMenuWillbecomeVisible så att 
		menyvalen kan enablas/disablas.
	 */
	protected void enableDisableMenuItems() {
		if (m_CCPManager != null)
			m_CCPManager.enableDisableCopyCutPasteMenuItems();
	}

	protected abstract JComponent getCatalogComponent();

	public final CoPopupMenu getEditMenu() {
		return m_editMenu;
	}

	public abstract CoMenuBuilder getMenuBuilder();

	public abstract void handleAddElementAction(CoAddElementData elementData);

	public abstract void handleRemoveElementAction();

	/**
		Must be called after the editor is created to build menus and listeners.
		CAN NOT be called from the constructor because subclasses may be using
		varibles that aren't properly initialized if called from the constructor.
	 */
	public CoAbstractCatalogEditor initialize() {
		createEditMenuItems(m_editMenu, getMenuBuilder());
		createListeners();
		return this;
	}

	/**
	 * PENDING: Method added to have some sort of option to cancel the removal
	 * 			Might need to be changed later - the message might not always be neccessary
	 * 			and you might want the option to bypass it.
	 *
	 *			/Peter 2000-10-18
	 */
	public void preHandleRemoveElementAction() {
		if (CoGUI.question(CoUIStringResources.getName((CoUIConstants.REMOVE_ELEMENT_MESSAGE)), null))
			handleRemoveElementAction();
	}

	protected final void setEditMenu(CoPopupMenu editMenu) {
		m_editMenu = editMenu;
	}

	protected void showEditMenu(int x, int y) {
		JComponent c = getCatalogComponent();
		if (c.isEnabled())
			getEditMenu().show(c, x, y);
	}
	protected CoCopyCutPasteManager m_CCPManager;
}