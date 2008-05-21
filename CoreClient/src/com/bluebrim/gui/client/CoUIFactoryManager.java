package com.bluebrim.gui.client;

import java.util.Hashtable;

import com.bluebrim.base.shared.CoFactoryElementIF;
/**
 	En klass som har en Hashtable som inneh�ller instanser
 	av CoUserInterfaceFactory d�r nyckeln �r hela namnet p�
 	dom�nklassen som skall visas upp i motsvarande gr�nssnitt.<br>
 	En privat klassvariabel 'tables' inneh�ller en array av aktiva instanser
 	av subklasser till CoUIFactoryTable. <br>
 	Den senaste anv�nda CoUIfactoryTable cachas i klassvariablen 'lastTable'.

 */
public class CoUIFactoryManager {
	protected Hashtable 	factories					= null;
	private static CoUIFactoryManager instance		= new CoUIFactoryManager();
/**
 * This method was created by a SmartGuide.
 */
protected CoUIFactoryManager ( ) {
	factories				= new Hashtable(100);
}
/**
 */
protected void _add(String key, CoUserInterfaceFactory factory) {
	factories.put(key, factory);
}
/**
 */
protected CoUserInterfaceFactory _getFactory(String key) {
	return (CoUserInterfaceFactory )factories.get(key);
}
/**
 */
protected void _remove(String key) {
	factories.remove(key);
}
/**
 */
public static void add(String key, CoUserInterfaceFactory factory) {
	instance._add(key, factory);
}
/**
 */
public static CoUserInterfaceFactory getFactory(String key) {
	return instance._getFactory(key);
}
/**
 */
public static CoUserInterfaceFactory getFactory(CoFactoryElementIF anObject) {
	return getFactory(anObject.getFactoryKey());
}
/**
 */
public static CoDomainUserInterface openInterfaceOn (CoFactoryElementIF element, String interfaceKey) {
	return getFactory(element).openUserInterfaceOn(element, interfaceKey);
}
/**
 */
public static void remove(String key) {
	instance._remove(key);
}
}
