package com.bluebrim.gui.client;

import java.text.*;

import com.bluebrim.base.shared.*;

/**
 * @see CoNumberConverter
 */
public class CoIntegerConverter extends CoNumberConverter {

	public CoIntegerConverter(CoValueModel valueModel) {
		super(valueModel);
	}

	public CoIntegerConverter(CoValueModel valueModel, CoConvertibleUnitSet units) {
		super(valueModel, units);
	}

	protected Object getDefaultValue() {
		return new Integer(9999);
	}

	protected Object parseString(String newValue) throws ParseException {
		Number tValue = (Number) super.parseString(newValue);
		return (tValue != null) ? new Integer(tValue.intValue()) : null;
	}
}