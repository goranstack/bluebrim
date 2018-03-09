package com.bluebrim.browser.client;

import javax.swing.event.TreeSelectionListener;

import com.bluebrim.gui.client.CoTreeValueable;

/**
	Interface till CoTreeAspectAdaptorklasser som vid sin uppbyggnad av trädstrukturen utnyttjar 
	ett CoTreeCatalogFilterIF för att avgöra om ett element skall tas med eller inte. Se också
	CoTreeCatalogFilterIF för en mera ingående beskrivning över hur filtrering är tänkt att fungera.<br>
	@see CoTreeCatalogFilterIF.
  */
public interface CoFilteredTreeAspectAdaptorIF extends CoTreeValueable {
public void addTreeSelectionListener(TreeSelectionListener l);
public void removeTreeSelectionListener(TreeSelectionListener l);
}
