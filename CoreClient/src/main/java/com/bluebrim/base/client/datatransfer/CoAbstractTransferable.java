package com.bluebrim.base.client.datatransfer;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
/**
	Abstract class that implements <code>Transferable</code> and
	acts as superclass for all system specific transferables.
	Implements a couple of common methods.
 */
public abstract class CoAbstractTransferable implements Transferable {
	protected DataFlavor 	m_flavors[];

public CoAbstractTransferable () {
}


public CoAbstractTransferable (DataFlavor flavors[]) {
	m_flavors = flavors;
}


public DataFlavor[] getTransferDataFlavors()
{
	if (m_flavors == null)
		m_flavors = initializeTransferDataFlavors();
	return m_flavors;

}


protected abstract DataFlavor[] initializeTransferDataFlavors();


public boolean isDataFlavorSupported(DataFlavor flavor)
{
	DataFlavor tFlavors[] = getTransferDataFlavors();
	int tSize = tFlavors.length;
	for (int i=0; i<tSize; i++)
	{
		if (flavor.equals(tFlavors[i]))
			return true;
	}
	return false;
}
}