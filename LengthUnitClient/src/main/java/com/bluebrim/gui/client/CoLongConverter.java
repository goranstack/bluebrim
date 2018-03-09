package com.bluebrim.gui.client;

import java.text.*;

import com.bluebrim.base.shared.*;

/**
 * @see CoNumberConverter
 */
public class CoLongConverter extends CoNumberConverter {

	public CoLongConverter(CoValueModel valueModel) {
		super(valueModel);
	}

	public CoLongConverter(CoValueModel valueModel, CoConvertibleUnitSet units) {
		super(valueModel, units);
	}
	protected Object getDefaultValue() {
		return new Long(9999);
	}

	protected Object parseString(String newValue) throws ParseException {
		Number tValue = (Number) super.parseString(newValue);
		return (tValue != null) ? new Long(tValue.longValue()) : null;
	}
}