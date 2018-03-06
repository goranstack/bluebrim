package com.bluebrim.gui.client;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;

import com.bluebrim.swing.client.CoTreeBox;

/**
	En CoTreeBoxAdaptor knyter ihop en CoTreeValueable, dvs en CoTreeValueHolder
  	eller CoTreeAspectAdaptor, som innehåller trädets rot med ett CoTreeBox och CoTree
  	som skall visa upp trädet.<br>
	Genom att implementera TreeModel och göra sig själv till vyns model får en CoTreeBoxAdaptor
	kontroll över hur vyn hittar sina noder.<br>
	'treeHolder' lyssnar på TreeSelectionEvent från trädvyn och på TreeModelEvent från
	adaptorn.
	@author Lasse Svadängs 97-09-30
	
*/
public class CoTreeBoxAdaptor extends CoComponentAdaptor implements CoValueListener, TreeExpansionListener {
	private CoTreeValueable 	m_treeHolder;
	private CoTreeBox 			m_treeBox;
/**
 */
public CoTreeBoxAdaptor () {
	super();

}
/**
 * @param treeHolder SE.corren.calvin.userinterface.CoTreeValueable
 * @param treeBox SE.corren.calvin.userinterface.CoTreeBox
 * @param nodeAspect java.lang.String
 */
public CoTreeBoxAdaptor ( CoTreeValueable treeHolder, CoTreeBox treeBox) {
	this();
	setModelAndTree(treeHolder,treeBox);
}
public void enableDisable(CoEnableDisableEvent e)
{
	getTreeView().setEnabled(e.enable);
	super.enableDisable( e );
}
/**
 */
public Component getComponent() {
	return getTreeView();
}
private TreeModel getTreeModel() {
	return m_treeHolder.getTreeModel();
}
/**
 * getRoot method comment.
 */
private JTree getTreeView() {
	return m_treeBox.getTreeView();
}
/**
 * @param rootHolder SE.corren.calvin.userinterface.CoTreeValueable
 * @param treeBox SE.corren.calvin.userinterface.CoTreeBox
 */
private void setModelAndTree( CoTreeValueable treeHolder, CoTreeBox treeBox) {
	setTreeHolder(treeHolder);
	setTreeBox(treeBox);
	// Nedanstående cast fungerar eftersom treeHolder 
	// antingen är en CoTreeValueHolder eller en CoTreeAspectAdaptor
	getTreeView().addTreeSelectionListener((TreeSelectionListener )treeHolder);
}
/**
 * getRoot method comment.
 */
private void setTreeBox(CoTreeBox aTreeBox) {
	m_treeBox = aTreeBox;
	m_treeBox.setEnabled( m_treeHolder.isEnabled() );

	setTreeView(aTreeBox.getTreeView());
}
/**
  */
private void setTreeHolder(CoTreeValueable treeHolder) {
	m_treeHolder = treeHolder;
	((CoValueable )treeHolder).addValueListener(this);
}
private void setTreeView(JTree aTreeView) {
	updateTreeModel();
	m_treeHolder.reload();
}
/**
 */
public void treeCollapsed ( TreeExpansionEvent e) {
	getTreeView().getParent().repaint();

}
/**
 */
public void treeExpanded ( TreeExpansionEvent e) {
	return;
}
/**
  */
private void updateTreeModel() {
	TreeModel tTreeModel	= getTreeModel();
	getTreeView().setModel(tTreeModel.getRoot() != null ? tTreeModel: null);
}
/**
 */
public void valueChange(CoValueChangeEvent anEvent) {
	if (anEvent.getNewValue() == null)
	{
		getTreeView().setModel(null);
	}
	else
	{
		updateTreeModel();
		m_treeHolder.reload();
	}	
}
}
