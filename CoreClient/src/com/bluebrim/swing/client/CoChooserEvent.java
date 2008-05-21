package com.bluebrim.swing.client;

import java.util.EventObject;
/**
 	Ett event som skickas från en CoChooserPanel när ett element har lagts till
 	eller tagits bort.
 */
public class CoChooserEvent extends EventObject {
	boolean addEvent;
	Object	elements[];
	public static boolean ADD_EVENT 	= true;
	public static boolean REMOVE_EVENT 	= false; 
/**
 * This method was created by a SmartGuide.
 */
public CoChooserEvent ( Object source, boolean anAddEvent, Object elements[]) {
	super(source);
	addEvent 		= anAddEvent;
	this.elements	= elements;
}
/**
 */
public Object[] getElements () {
	return elements;
}
/**
 */
public boolean isAddEvent () {
	return addEvent;
}
}
