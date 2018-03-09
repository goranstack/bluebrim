package com.bluebrim.base.client.datatransfer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import com.bluebrim.base.shared.CoFactoryElementIF;
import com.bluebrim.browser.client.CoAbstractTreeCatalogUI;
import com.bluebrim.swing.client.CoTreeSelection;
/**
 * Copy/paste/cut manager for Trees
 * Creation date: (2000-12-15 14:21:48)
 * @author: Peter Jakubicki
 */
public class CoTreeCopyCutPasteManager extends CoCopyCutPasteManager{
public CoTreeCopyCutPasteManager(CoAbstractTreeCatalogUI userInterface, CoDataFlavorSetProvider provider)
{
	super(userInterface, provider);
	
}
public CoAbstractTreeCatalogUI getAbstractTreeCatalogUI(){
	return (CoAbstractTreeCatalogUI)getUserInterface();
}
protected CoFactoryElementIF getSelectedItem(){
	return getAbstractTreeCatalogUI().getSingleSelectedTreeElement();
}
protected Object[] getSelectedValues(){
	return getAbstractTreeCatalogUI().getSelectedTreeElements();
}
protected Transferable getTransferableContent(){

	return new CoTreeSelection(getSelectedValues(), getSupportedDataFlavors());	
}
public void enableDisableCopyItem() {

	m_copyAction.setEnabled(getAbstractTreeCatalogUI().hasSelectedTreeElements());

}
protected void enableDisablePasteItem() {
		
		//PENDING: Should modified and moved up....... /Peter 010102
		
		Transferable clipboardContent = CoClipBoard.getClipboard().getContents(this);
		boolean b = clipboardContent != null && clipboardContent.isDataFlavorSupported(getSupportedPasteFlavor());

		m_pasteAction.setEnabled(b);		
}		
protected DataFlavor getSupportedPasteFlavor() {

	// PENDING: Should be moved up....... /Peter 010102
	return new DataFlavor(Object.class, "no perticular flavor");
}		
}