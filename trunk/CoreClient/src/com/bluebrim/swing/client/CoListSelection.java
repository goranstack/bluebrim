package com.bluebrim.swing.client;
import java.awt.datatransfer.DataFlavor;

import com.bluebrim.base.client.datatransfer.CoDataTransferKit;
import com.bluebrim.base.client.datatransfer.CoNonLazyTransferable;

/**
	Klass som representerar drag & drop data från en CoList. 
	Data från en lista består alltid av en array av objekt.
 */
public class CoListSelection extends CoNonLazyTransferable {
	public static DataFlavor LIST_FLAVOR = CoDataTransferKit.domainFlavor(java.util.List.class, "Object List");

public CoListSelection(Object data[]) {
	super(data);
}


public CoListSelection(Object data[], DataFlavor flavors[]) {
	super(data, flavors);
}


protected DataFlavor[] initializeTransferDataFlavors() {
	return new DataFlavor[] {LIST_FLAVOR, CoDataTransferKit.DUMMY_FLAVOR};
}
}