package com.bluebrim.gui.client;
import java.beans.PropertyVetoException;

import com.bluebrim.base.shared.CoBaseUtilities;

/**
 	Konkret subklass till CoValueModel som har en privat instansvariabel för sitt värde 
 	och som implementerar hämta och sättmetoderna.
 	#setValue ser till att ett CoValueChangeEvent skickas till alla som lyssnar 
 	efter ändringar av värdet.
 	@author Lasse Svadängs 1997-10-10
 */
public class CoValueHolder extends CoValueModel {
	private Object m_value;

	public CoValueHolder() {
	}

	public CoValueHolder(Object initValue, Object key) {
		this(key);
		m_value = initValue;
	}

	public CoValueHolder(Object key) {
		super(key);
	}

	public Object getValue() {
		return m_value;
	}

	protected void handleValidationFailure(PropertyVetoException e, Object oldValue, Object errorValue) {
		String message = e.getMessage();
		if (CoBaseUtilities.stringIsNotEmpty(message))
			CoGUI.error(message);

		fireValueChangeEvent(errorValue, oldValue);
	}

	protected boolean hasValueChanged(Object oldValue, Object newValue) {
		return (((newValue != null) && (!newValue.equals(oldValue))) || ((oldValue == null) && (newValue != null)) || ((newValue == null) && (oldValue != null)));
	}

	public void initValue(Object value) {
		Object oldValue = getValue();
		m_value = value;
		fireValueChangeEvent(oldValue, value);
	}

	public void setValue(Object value) {
		Object oldValue = getValue();
		if (hasValueChanged(oldValue, value)) {
			try {
				m_value = validate(value);
				fireValueChangeEvent(oldValue, getValue());
			} catch (PropertyVetoException e) {
				handleValidationFailure(e, oldValue, value);
			}
		}
	}
}