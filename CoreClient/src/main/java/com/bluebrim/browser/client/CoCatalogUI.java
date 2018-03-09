package com.bluebrim.browser.client;

import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.browser.shared.CoCatalogElementIF;
import com.bluebrim.gui.client.CoListValueable;
import com.bluebrim.menus.client.CoMenuBuilder;
import com.bluebrim.swing.client.CoListBox;

/**
	Interfaceklass för de UI-klasser som skall hantera en katalog,
	dvs en lista där användaren kan lägga till och ta bort element 
	med hjälp av en popupmeny.
 */
public interface CoCatalogUI {
/**
	Lägger till ett nytt element till katalogen.
 */
public CoCatalogElementIF addElement();
/**
	Lägger till ett nytt element till katalogen.
 */
public CoCatalogElementIF addElement(CoCatalogElementIF element);
void clearSelection();
/**
	Svarar med en sträng för "Lägg till" menyn.
 */
public abstract String getAddItemLabel();
/**
 */
public abstract CoListCatalogEditor getCatalogEditor();
/**
 	Skall svara med CoListValueable som håller listan.
 */
public CoListValueable.Mutable getCatalogHolder();
/**
	Skall svara med den CoListBox som visar upp listan över katalogelement. 
 */
public abstract CoListBox getCatalogListBox();
/**
 	Skall svara med det objekt som äger katalogen
 */
public CoObjectIF getCatalogOwner();
/**
	Svarar med en sträng för "Ta bort" menyn.
 */
public abstract String getRemoveItemLabel();
/**
	Skall svara med array innehållande alla selekterade element.
 */
public abstract Object[] getSelectedCatalogElements();
public abstract CoObjectIF getSingleSelectedCatalogElement();
/**
 */
public abstract CoMenuBuilder getUIMenuBuilder();
/**
 */
public abstract boolean hasSelectedCatalogElements();
/**
	Tar bort alla markerade element från katalogen.
 */
public void postRemoveElements();
/**
	Tar bort alla markerade element från katalogen.
 */
public void removeElements(Object[] elements);
/**
 * Selects the row containing <em>element</em>.
 */
public void selectElement(CoCatalogElementIF element);
}
