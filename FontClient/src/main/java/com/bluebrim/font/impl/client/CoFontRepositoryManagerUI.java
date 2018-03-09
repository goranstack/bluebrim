package com.bluebrim.font.impl.client;
import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.browser.client.*;
import com.bluebrim.browser.shared.*;
import com.bluebrim.font.impl.client.CoFontRepositoryManager.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;

/**
 * User interface for adding fonts to the system. Certain
 * manipulations are also possible.
 *
 * @author Markus Persson 2001-10-09
 */
public class CoFontRepositoryManagerUI extends CoAbstractCatalogUI {
	private CoFontFamilyManagerUI m_familyUI = new CoFontFamilyManagerUI();

public CoFontRepositoryManagerUI() {
	this(CoFontRepositoryManager.getSingleton());
}


public CoFontRepositoryManagerUI(CoObjectIF domainObject) {
	super(domainObject);
}


protected CoListCatalogEditor createCatalogEditor() {
	// Disable editing and context menu.
	return null;
}


protected CoUserInterface createCatalogElementUI() {
	m_familyUI.setDomain(getSelectedElement());
	return m_familyUI;
}


protected CoListValueable.Mutable createCatalogHolder() {
	return new CoDefaultListAspectAdaptor(this, "FAMILIES") {
		protected Object get(CoObjectIF subject) {
			return (subject != null) ? getManagerFor(subject).getFontFamilyList() : null;
		}
	};
}


protected ListCellRenderer createCatalogListCellRenderer(CoUserInterfaceBuilder builder) {
	return new CoStringRenderer() {
		protected void prepareFor(Object value) {
			setText((value != null) ? ((Family) value).getName() : "");
		}
	};
}


private CoFontRepositoryManager getManagerFor(CoObjectIF subject) {
	return (CoFontRepositoryManager) subject;
}

protected CoCatalogElementIF newCatalogElement() {
	// Creation of new elements does not apply to us.
	return null;
}
}