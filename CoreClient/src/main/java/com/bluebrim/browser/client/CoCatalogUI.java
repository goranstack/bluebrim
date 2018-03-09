package com.bluebrim.browser.client;

import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.browser.shared.CoCatalogElementIF;
import com.bluebrim.gui.client.CoListValueable;
import com.bluebrim.menus.client.CoMenuBuilder;
import com.bluebrim.swing.client.CoListBox;

/**
	Interfaceklass f�r de UI-klasser som skall hantera en katalog,
	dvs en lista d�r anv�ndaren kan l�gga till och ta bort element 
	med hj�lp av en popupmeny.
 */
public interface CoCatalogUI {
/**
	L�gger till ett nytt element till katalogen.
 */
public CoCatalogElementIF addElement();
/**
	L�gger till ett nytt element till katalogen.
 */
public CoCatalogElementIF addElement(CoCatalogElementIF element);
void clearSelection();
/**
	Svarar med en str�ng f�r "L�gg till" menyn.
 */
public abstract String getAddItemLabel();
/**
 */
public abstract CoListCatalogEditor getCatalogEditor();
/**
 	Skall svara med CoListValueable som h�ller listan.
 */
public CoListValueable.Mutable getCatalogHolder();
/**
	Skall svara med den CoListBox som visar upp listan �ver katalogelement. 
 */
public abstract CoListBox getCatalogListBox();
/**
 	Skall svara med det objekt som �ger katalogen
 */
public CoObjectIF getCatalogOwner();
/**
	Svarar med en str�ng f�r "Ta bort" menyn.
 */
public abstract String getRemoveItemLabel();
/**
	Skall svara med array inneh�llande alla selekterade element.
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
	Tar bort alla markerade element fr�n katalogen.
 */
public void postRemoveElements();
/**
	Tar bort alla markerade element fr�n katalogen.
 */
public void removeElements(Object[] elements);
/**
 * Selects the row containing <em>element</em>.
 */
public void selectElement(CoCatalogElementIF element);
}
