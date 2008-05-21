package com.bluebrim.base.client.datatransfer;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Subclass to <code>CoAbstractTransferable</code> that gets its data
 * and flavors in the constructor, i e it's not lazy initialized when asked for.
 * Creation date: (1999-11-01 11:45:13)
 * @author: Lasse S
 */
public class CoNonLazyTransferable extends CoAbstractTransferable {
	protected Object m_data[];
/**
 * CoNonLazyTransferable constructor comment.
 */
public CoNonLazyTransferable() {
	super();
}
public CoNonLazyTransferable(Object[] data) {
	m_data = data;
}
public CoNonLazyTransferable(Object[] data, DataFlavor[] flavors) {
	super(flavors);
	m_data = data;
}
public Object getTransferData(DataFlavor flavor) throws IOException, UnsupportedFlavorException {
	if (isDataFlavorSupported(flavor)) {
		// Workaround for bug. See CoDataTransferKit. /Markus
		if (flavor.equals(CoDataTransferKit.DUMMY_FLAVOR)) {
			return CoDataTransferKit.DUMMY_FLAVOR_RETURN_VALUE;
		} else {
			return m_data;
		}
	} else {
		// PENDING: Supposed to throw exception here. Try it soon! /Markus 2001-09-20
		return null;
	}
}
protected DataFlavor[] initializeTransferDataFlavors() {
	return m_flavors;
}
public void lostOwnership(Clipboard clipboard, Transferable contents){
	
}
}
