package com.bluebrim.swing.client;
import java.awt.datatransfer.DataFlavor;

import com.bluebrim.base.client.datatransfer.CoDataTransferKit;
import com.bluebrim.base.client.datatransfer.CoNonLazyTransferable;

/**
	Klass som representerar drag & drop data från en CoTree. 
	Data från ett träd består alltid av en array av objekt.
 */
public class CoTreeSelection extends CoNonLazyTransferable {
	public static DataFlavor treeFlavor;
	static 
	{
		try 
		{
	   		treeFlavor = CoDataTransferKit.domainFlavor(Class.forName("com.bluebrim.swing.client.CoTree"), "Object array");
		} 
		catch (ClassNotFoundException e) 
		{
		}
	} 

public CoTreeSelection ( Object data[]) {
	super(data);
}


public CoTreeSelection ( Object data[], DataFlavor flavors[]) {
	super(data, flavors);
}


protected DataFlavor[] initializeTransferDataFlavors() {
	return new DataFlavor[] {treeFlavor};
}
}