package com.bluebrim.base.client.datatransfer;

import java.awt.datatransfer.DataFlavor;
import java.util.HashMap;
import java.util.Map;

/**
 * A data flavor provider class that holds a map with data flavor arrays.
 * The right key will provide you the right set of data flavors.
 * Creation date: (2000-11-24 08:24:14)
 *
 * @author: Peter Jakubicki
 */
public abstract class CoDataFlavorSetProvider {

	protected 	Map 			m_dragFlavorSets;
//	protected 	Map 			m_dropFlavorSets;
	protected 	DataFlavor[] 	m_dropFlavors;

// 	protected abstract void initDropFlavorSet();
/**
 * CoFlavorSet constructor comment.
 */
public CoDataFlavorSetProvider() {
	super();
	m_dragFlavorSets = new HashMap();
//	m_dropFlavorSets = new HashMap();
	
	initFlavorSet();
	
}
/**
 * CoFlavorSet constructor comment.
 */
public CoDataFlavorSetProvider(DataFlavor[] flavors) {
	this();
	m_dropFlavors = flavors;
	
}

public DataFlavor[] getDragFlavorsFor(String name){
		
	return (DataFlavor[])m_dragFlavorSets.get(name);	
}
// NOTE: Very iffy.  /Peter 001130
public DataFlavor[] getDropFlavors(){

	return m_dropFlavors;
}
protected abstract void initDragFlavorSet();
protected void initFlavorSet(){
	initDragFlavorSet();
//	initDropFlavorSet();
}
}
