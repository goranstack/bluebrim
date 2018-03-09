package com.bluebrim.browser.client;

import javax.swing.event.TreeSelectionListener;

import com.bluebrim.gui.client.CoTreeValueable;

/**
	Interface till CoTreeAspectAdaptorklasser som vid sin uppbyggnad av tr�dstrukturen utnyttjar 
	ett CoTreeCatalogFilterIF f�r att avg�ra om ett element skall tas med eller inte. Se ocks�
	CoTreeCatalogFilterIF f�r en mera ing�ende beskrivning �ver hur filtrering �r t�nkt att fungera.<br>
	@see CoTreeCatalogFilterIF.
  */
public interface CoFilteredTreeAspectAdaptorIF extends CoTreeValueable {
public void addTreeSelectionListener(TreeSelectionListener l);
public void removeTreeSelectionListener(TreeSelectionListener l);
}
