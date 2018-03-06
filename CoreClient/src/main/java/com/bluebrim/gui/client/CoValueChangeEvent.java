package com.bluebrim.gui.client;

import java.beans.PropertyChangeEvent;

/**
 * En eventklass som representerar en förändring
 * av värdet i en ValueModel.
 * <p>
 * @see CoValueModel#fireValueChangeEvent
 * @author Lasse Svadängs 971010.
 */
public class CoValueChangeEvent extends PropertyChangeEvent {
	public static String WINDOW_CLOSING = "window_closing";
	public static String UPDATE = "update";
	public static String PROPERTY_CHANGE = "property_change";

	/**
	 * @param source java.lang.Object
	 * @param propertyName java.lang.String
	 * @param oldValue java.lang.Object
	 * @param newValue java.lang.Object
	 */
	public CoValueChangeEvent(Object source, String propertyName, Object oldValue, Object newValue) {
		super(source, propertyName, oldValue, newValue);
	}

	public boolean isPropertyChangeEvent() {
		return PROPERTY_CHANGE.equals(getPropertyName());
	}

	public boolean isUpdateEvent() {
		return UPDATE.equals(getPropertyName());
	}

	public boolean isWindowClosingEvent() {
		return WINDOW_CLOSING.equals(getPropertyName());
	}
}
