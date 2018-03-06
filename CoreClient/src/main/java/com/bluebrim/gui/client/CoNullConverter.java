package com.bluebrim.gui.client;

import java.beans.PropertyVetoException;

import com.bluebrim.base.client.CoConverterParseException;

/**
 * Concrete converter class to be used when no conversion is needed, 
 * i e the client value model is expecting a string and returns a
 * string in <code>getValue</code>.
 * <br>
 * Used as default converter in <code>CoTextFieldAdaptor</code>.
 */
public class CoNullConverter extends CoConverter {
	public CoNullConverter(CoValueModel valueModel) {
		super(valueModel);
	}

	protected String doFormat(Object aValue) {
		return aValue.toString();
	}

	protected Object getDefaultValue() {
		return ""; //NOT USED
	}

	protected String getType() {
		return "NULL"; //NOT USED
	}

	public void setValueFromString(String newValue) throws CoConverterParseException, PropertyVetoException {
		getClient().setValue(getClient().validate(newValue));
	}
}