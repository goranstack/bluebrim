package com.bluebrim.menus.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.TooManyListenersException;

import javax.swing.Action;
import javax.swing.Icon;

/**
	Abstrakt superklass för actionobjekt som skall kopplas till exempelvis ett menyval.
	Inhåller text, icon och mekanismer för att se till att menyvalet enablas/disablas 
	utifrån enable/disable för actionobjektet.
	För att det skall vara möjligt för en meny att plocka bort det menyval som mostsvaras 
	av ett actionobjekt så kan en action bara ha en enda PropertyChangeListener i taget.
 */
public abstract class CoAbstractMenuAction implements Action, Cloneable, Serializable 
{
	String name 			= "";
	boolean enabled 	= true;
	Icon icon 			= null;
	protected PropertyChangeListener listener = null;

	/**
	 * Defines an Action object with a default description string
	 * and default icon.
	 */
	public CoAbstractMenuAction() {}
	/**
	 * Defines an Action object with the specified description string
	 * and a default icon.
	 */
	public CoAbstractMenuAction(String name) {
	this.name = name;
	}
	/**
	 * Defines an Action object with the specified description string
	 * and a the specified icon.
	 */
	public CoAbstractMenuAction(String name, Icon icon) {
	this(name);
	this.icon = icon;
	}
/**
*/
public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
	try
	{	
		doAddPropertyChangeListener(listener);
	}
	catch (TooManyListenersException e)
	{
		e.printStackTrace();
	}			
}
/**
*/
protected void doAddPropertyChangeListener(PropertyChangeListener listener) throws TooManyListenersException{
	if (this.listener != null)
	    throw new TooManyListenersException();
	else
	    this.listener = listener;

}
protected void doRemovePropertyChangeListener(PropertyChangeListener listener) {
	if (listener == this.listener)
		this.listener = null;
	else
		throw new IllegalArgumentException("CoAbstractMenuAction.doRemovePropertyChangeListener "+listener);
}
/**
*/
protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
	if (listener != null)
		listener.propertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
}
	/** @see Action#getIcon */
	// Get Icon associated with key
	public Icon getIcon(String key) { return icon; }
	/** @see Action#getText */
	// Gets the String associated with key
	public String getText(String key) {
	return this.name;
	}
	/** @see Action#isEnabled */
	public boolean isEnabled() {
	return enabled;
	}
public void removePropertyChangeListener() {
		removePropertyChangeListener(listener);
}
public void removePropertyChangeListener(PropertyChangeListener listener) {
	try
	{
		doRemovePropertyChangeListener(listener);
	}
	catch (IllegalArgumentException e)
	{
		e.printStackTrace();
	}		
}
	/** @see Action#setEnabled */
	public void setEnabled(boolean newValue) {
	boolean oldValue = this.enabled;
	this.enabled = newValue;
	firePropertyChange("enabled", 
			   new Boolean(oldValue), new Boolean(newValue));
	}
	/** @see Action#setIcon */
	// Set the Icon associated with key
	public void setIcon(String key, Icon newValue) {
	Icon oldValue = this.icon;
	this.icon = newValue;
	firePropertyChange(key, oldValue, newValue);	
	}
	/** @see Action#setText */
	// Sets the String associated with key
	public void setText(String key, String newValue) {
	String oldValue = this.name;
	this.name = newValue;
	firePropertyChange(key, oldValue, newValue);
	}
}
