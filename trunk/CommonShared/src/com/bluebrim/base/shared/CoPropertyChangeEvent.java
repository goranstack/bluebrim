package com.bluebrim.base.shared;

import java.beans.*;
/**
 	Subklass till PropertyChangeEvent som har lagt till instansvariabeln 'propertyOwner', 
 	dvs det objekt som �ger det property som har �ndrats. Detta beh�ver n�mligen inte alltid 
 	vara lika med eventets 'source'. Skall anv�ndas av CoObject om �ndringar av v�rdet 
 	f�r en av dess egenskaper skall propageras ut till ev lyssnare. 
 	@see CoObject
 * 
 */
public class CoPropertyChangeEvent extends PropertyChangeEvent  {
	Object	persistentSource;
	Object propertyOwner 			= null;
	String changeId 							= null;
	public static String NO_ID			= "";
	/**
		Anv�nds som 'changeId' n�r element har lagts till en lista
	*/
	public static String ADD_ID			= "Add";
	/**
		Anv�nds som 'changeId' n�r element har tagits bort fr�n en lista
	*/
	public static String REMOVE_ID 	= "Remove";
/**
 * CoPropertyChangeEvent constructor comment.
 * @param source CoObjectIF
 * @param propertyName java.lang.String
 * @param oldValue java.lang.Object
 * @param newValue java.lang.Object
 */
public CoPropertyChangeEvent( Object source, Object propertyOwner, String propertyName, String changeId, Object oldValue, Object newValue) {
	super(source, propertyName, oldValue, newValue);
	setChangeId(changeId);
	setPropertyOwner(propertyOwner);
	persistentSource	= source;
}
/**
 */
public String getChangeId () {
	return changeId;
}
/**
	Svara med det CoObjectIF som �ger den egenskap som �ndrats.
 */
public Object getPropertyOwner () {
	return propertyOwner;
}
/**
	Svara med 'persistentSource'.
 */
public Object getSource () {
	return persistentSource;
}
/**
 */
public void setChangeId (String changeId ) {
	this.changeId = changeId;
}
/**
 */
public void setPropertyOwner (Object propertyOwner ) {
	this.propertyOwner = propertyOwner;
}
}
