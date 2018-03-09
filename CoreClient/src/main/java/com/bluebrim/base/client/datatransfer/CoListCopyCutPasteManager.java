package com.bluebrim.base.client.datatransfer;

import java.awt.datatransfer.Transferable;

import com.bluebrim.base.shared.CoFactoryElementIF;
import com.bluebrim.browser.client.CoAbstractCatalogUI;
import com.bluebrim.swing.client.CoListSelection;
/**
 * Copy/paste/cut manager for Lists
 * Creation date: (2000-12-12 14:18:48)
 * @author: Peter Jakubicki
 */
public class CoListCopyCutPasteManager extends CoCopyCutPasteManager {

	public CoListCopyCutPasteManager(CoAbstractCatalogUI userInterface, CoDataFlavorSetProvider provider) {
		super(userInterface, provider);

	}

	public void enableDisableCopyItem() {

		m_copyAction.setEnabled(getAbstractCatalogUI().hasSelectedCatalogElements());

	}

	private CoAbstractCatalogUI getAbstractCatalogUI() {
		return (CoAbstractCatalogUI) getUserInterface();
	}

	protected CoFactoryElementIF getSelectedItem() {
		// NOTE: Wild cast, for two reasons: I don't like this class
		// and believe it should be removed so I want to change as little
		// as possible. The elements almost all of the time implement
		// CoCatalogElementIF. (I changed the return type of the catalog
		// UI because its specificity was inflexible and never used.)
		// /Markus 2001-10-22
		return (CoFactoryElementIF) getAbstractCatalogUI().getSelectedElement();
	}

	protected Object[] getSelectedValues() {
		return getAbstractCatalogUI().getCatalogList().getSelectedValues();
	}

	protected Transferable getTransferableContent() {

		return new CoListSelection(getSelectedValues(), getSupportedDataFlavors());
	}
}
