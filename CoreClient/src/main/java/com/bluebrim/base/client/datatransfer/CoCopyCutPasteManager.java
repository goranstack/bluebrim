package com.bluebrim.base.client.datatransfer;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import com.bluebrim.base.shared.CoFactoryElementIF;
import com.bluebrim.gui.client.CoUserInterface;
import com.bluebrim.menus.client.CoMenuBuilder;
import com.bluebrim.menus.client.CoPopupMenu;

/**
 * Manager for copy/paste/cut.  
 * Creation date: (2000-12-12 14:18:48)
 * @author: Peter Jakubicki
 */
public abstract class CoCopyCutPasteManager implements ClipboardOwner{

	private  	CoUserInterface				m_userInterface;
	protected 	CoDataFlavorSetProvider		m_flavorProvider;

	protected AbstractAction m_copyAction;
	protected AbstractAction m_pasteAction;

	protected AbstractAction m_cutAction; //PENDING: not implemented yet.

public CoCopyCutPasteManager(CoUserInterface userInterface, CoDataFlavorSetProvider provider)
{
	m_userInterface = userInterface;
	m_flavorProvider = provider;
	m_copyAction = new AbstractAction("Kopiera"){
						public void actionPerformed(java.awt.event.ActionEvent e){
								copyAction();
								}
						};
	m_pasteAction = new AbstractAction("Klistra in"){
						public void actionPerformed(java.awt.event.ActionEvent e){
							pasteAction();
						}
					};
	m_cutAction = new AbstractAction("Klipp ut"){
						public void actionPerformed(java.awt.event.ActionEvent e){
							
						}
					};

	m_pasteAction.setEnabled(false);
	m_copyAction.setEnabled(false);
	m_cutAction.setEnabled(false);

}
protected void copyAction(){

	Transferable transferableContent = getTransferableContent();
	CoClipBoard.getClipboard().setContents(transferableContent, this);
}
private void createCopyItem(CoMenuBuilder menuBuilder, CoPopupMenu menu){
	menuBuilder.addPopupMenuItem(menu, m_copyAction);
}
public void createCopyPasteCutItems(CoPopupMenu menu, CoMenuBuilder menuBuilder) {
	createCopyItem(menuBuilder, menu);
	createCutItem(menuBuilder, menu);
	createPasteItem(menuBuilder, menu);
}
private void  createCutItem(CoMenuBuilder menuBuilder, CoPopupMenu menu){
//	if(m_cutAction != null)
//		menuBuilder.addPopupMenuItem(menu, m_cutAction);
}
private void createPasteItem(CoMenuBuilder menuBuilder, CoPopupMenu menu){
	menuBuilder.addPopupMenuItem(menu, m_pasteAction);
}
protected void cutAction(){
 //Not yet implemented
}
public final void enableDisableCopyCutPasteMenuItems() {
	enableDisableCopyItem();
	enableDisablePasteItem();
	enableDisableCutItem();
}		

protected final Transferable getClipboardContent(){

	return CoClipBoard.getClipboard().getContents(this);

}
protected abstract CoFactoryElementIF getSelectedItem();
protected abstract Object[] getSelectedValues();
public DataFlavor[] getSupportedDataFlavors()
{

	//Ugly Temp solution...
	//Peter 001215
	Object[] selections = getSelectedValues();
	List flavorList = new ArrayList();
	DataFlavor[] tFlavors;
	
	if(selections != null){
		for(int i = 0 ; i < selections.length ; i++){
			tFlavors = m_flavorProvider.getDragFlavorsFor(((CoFactoryElementIF)selections[i]).getFactoryKey());
			for(int j = 0 ; j < tFlavors.length ; j++){
				if(!flavorList.contains(tFlavors[j]))
					flavorList.add(tFlavors[j]);
			}
		}

		Object[] tf = flavorList.toArray();
		DataFlavor[] pf = new DataFlavor[tf.length];
		for(int i = 0 ; i<tf.length ; i++){
			pf[i] = (DataFlavor)tf[i];
		}
		return pf;
			
	}
	
	return	m_flavorProvider.getDropFlavors();
}
protected abstract Transferable getTransferableContent();
protected final CoUserInterface getUserInterface(){
	return m_userInterface;
}
protected void handlePaste(final Transferable clipboardContent){

}
public void lostOwnership(Clipboard clipboard, Transferable contents){

	//NOP
}
protected void pasteAction(){

	final Transferable clipboardContent = getClipboardContent();
	if(clipboardContent != null )	
		handlePaste(clipboardContent);

}
protected abstract void enableDisableCopyItem();
protected void enableDisableCutItem() {
	m_cutAction.setEnabled(false);
}		
protected void enableDisablePasteItem() {
	m_pasteAction.setEnabled(false);
}
}