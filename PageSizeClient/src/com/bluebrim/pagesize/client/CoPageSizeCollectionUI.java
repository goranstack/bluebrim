package com.bluebrim.pagesize.client;
import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.browser.client.CoAbstractCatalogUI;
import com.bluebrim.browser.client.CoListCatalogEditor;
import com.bluebrim.browser.shared.CoCatalogElementIF;
import com.bluebrim.gemstone.client.CoGsListCatalogEditor;
import com.bluebrim.gui.client.CoAbstractListAspectAdaptor;
import com.bluebrim.gui.client.CoAbstractListModel;
import com.bluebrim.gui.client.CoCollectionListModel;
import com.bluebrim.gui.client.CoListValueable;
import com.bluebrim.gui.client.CoUserInterface;
import com.bluebrim.page.shared.CoPageSizeCollectionIF;
import com.bluebrim.page.shared.CoPageSizeIF;

/**
 *
 * Domain class: CoPageSizeCollectionIF
 */

public class CoPageSizeCollectionUI extends CoAbstractCatalogUI {

	public CoPageSizeCollectionUI() {
		super();
	}

	protected CoListCatalogEditor createCatalogEditor() {
		return new CoGsListCatalogEditor(this);
	}

	public CoUserInterface createCatalogElementUI() {
		return new CoPageSizeUI();
	}

	protected CoListValueable.Mutable createCatalogHolder() {
		return new CoAbstractListAspectAdaptor.Mutable(this, "ELEMENTS") {
			protected Object get(CoObjectIF subject) {
				return ((CoPageSizeCollectionIF) subject).getPageSizes();
			}
			public CoAbstractListModel.Mutable createListModel() {
				return new CoCollectionListModel.Mutable(this) {
					protected boolean doRemoveElement(Object element) {
						return ((CoPageSizeCollectionIF) getDomain()).removePageSize((CoPageSizeIF) element) != null;
					}
					protected void doAddElement(Object element) {
						((CoPageSizeCollectionIF) getDomain()).addPageSize((CoPageSizeIF) element);
					}
				};
			}
		};
	}

	public CoCatalogElementIF newCatalogElement() {
		return ((CoPageSizeCollectionIF) getDomain()).createPageSize();
	}
}