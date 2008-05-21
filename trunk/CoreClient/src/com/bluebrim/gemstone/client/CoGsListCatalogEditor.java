package com.bluebrim.gemstone.client;

import com.bluebrim.browser.client.CoCatalogUI;
import com.bluebrim.browser.client.CoListCatalogEditor;
import com.bluebrim.browser.shared.CoAddElementData;

/**
 * Subclass to <code>CoListCatalogEditor</code> that has 
 * "add" and "remove" as transactions. updating the database. 
 * <br>
 * All catalog ui's that want to have transactions for free 
 * must reimplement <code>createCatalogEditor()</code> to
 * create an instance of <code>CoListCatalogEditor</code>.
 */
public class CoGsListCatalogEditor extends CoListCatalogEditor
{


/**
 * This method was created by a SmartGuide.
 */
public CoGsListCatalogEditor (CoCatalogUI catalogUI) {
	super(catalogUI);
}


/**
 * This is the default behavior when adding an element to 
 * the catalog ui, eg only one type of element is supported
 * and the <code>elementData</code> argument isn't used.
 * <br>
 * In a subclass where it's possible to add several different types
 * of elements this method can be overriden and different behavior
 * exercised dependent on the value of <code>elementData</code>
 */
public void handleAddElementAction (CoAddElementData elementData) {

	CoTransactionUtilities.execute(getAddElementCommand(elementData), getCatalogUI().getCatalogOwner());
}
/**
 * This is the default behavior when remove is
 * triggered via the object menu.
 */
public void handleRemoveElementAction () {
	CoTransactionUtilities.execute(getRemoveElementsCommand(), getCatalogUI().getCatalogOwner());


}
}