package com.bluebrim.text.impl.client;

import java.awt.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.browser.client.*;
import com.bluebrim.browser.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.text.impl.shared.*;

public class CoHyphenationCollectionUI extends CoAbstractCatalogUI {
	public static final String HYPHENATIONS = "CoHyphenationCollectionUI.HYPHENATIONS";
	private int m_startOfMutableHyphenations;

	public CoHyphenationCollectionUI() {
		super();
	}
	public CoHyphenationCollectionUI(CoHyphenationCollectionIF d) {
		super(d);
	}
	protected void basicSetSelectedElement(CoObjectIF element) {
		super.basicSetSelectedElement(element);

		getGuaranteedElementUI().setEnabled(getCatalogList().getSelectedIndex() >= m_startOfMutableHyphenations);
	}
	protected CoListCatalogEditor createCatalogEditor() {
		return new CoGsListCatalogEditor(this) {
			public void enableDisableMenuItems() {
				super.enableDisableMenuItems();

				if (m_removeElementAction != null && m_removeElementAction.isEnabled())
					m_removeElementAction.setEnabled(getCatalogList().getSelectedIndex() >= m_startOfMutableHyphenations);
			}
		};
	}
	public CoUserInterface createCatalogElementUI() {
		return new CoHyphenationUI();
	}
	protected CoListValueable.Mutable createCatalogHolder() {

		return new CoAbstractListAspectAdaptor.Mutable(this, HYPHENATIONS) {

			protected Object get(CoObjectIF subject) {
				return ((CoHyphenationCollectionIF) subject).getHyphenations();
			}

			private CoHyphenationCollectionIF getHyphenationCollection() {
				return (CoHyphenationCollectionIF) getSubject();
			}

			public CoAbstractListModel.Mutable createListModel() {
				return new CoCollectionListModel.Mutable(this) {
					protected boolean doRemoveElement(Object element) {
						getHyphenationCollection().removeHyphenation((com.bluebrim.text.shared.CoHyphenationIF) element);
						return true;
					}
					protected void doAddElement(Object element) {
						getHyphenationCollection().addHyphenation((com.bluebrim.text.shared.CoHyphenationIF) element);
					}
				};
			}

		};

	}
	protected ListCellRenderer createCatalogListCellRenderer(CoUserInterfaceBuilder builder) {
		return new CoImmutableAsItalicCatalogListCellRenderer() {
			protected boolean isMutable(Object value, int index) {
				return index >= m_startOfMutableHyphenations;
			}
		};
	}
	protected Object getCatalogSubcanvasLayoutConstraint() {
		return BorderLayout.CENTER;
	}
	private CoHyphenationCollectionIF getHyphenationCollection() {
		return (CoHyphenationCollectionIF) getDomain();
	}
	public CoCatalogElementIF newCatalogElement() {
		return ((CoHyphenationCollectionIF) getDomain()).createHyphenation();
	}
	public void postDomainChange(CoObjectIF d) {
		super.postDomainChange(d);

		CoHyphenationCollectionIF c = getHyphenationCollection();
		m_startOfMutableHyphenations = (c == null) ? 0 : c.getImmutableHyphenationCount();
	}
	public void valueHasChanged() {
		Object[] tmp = collectSelectedElements();

		super.valueHasChanged();

		restoreSelectedElements(tmp);

		m_startOfMutableHyphenations = getHyphenationCollection().getImmutableHyphenationCount();
	}
}
