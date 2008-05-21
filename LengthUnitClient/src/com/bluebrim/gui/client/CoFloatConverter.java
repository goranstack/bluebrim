package com.bluebrim.gui.client;

import java.text.*;

import com.bluebrim.base.shared.*;
/**
 @see CoNumberConverter
 * 
 */
public class CoFloatConverter extends CoNumberConverter {
/**
 * CoIntegerConverter constructor comment.
 * @param valueModel com.bluebrim.base.client.CoValueModel
 */
public CoFloatConverter(CoValueModel valueModel) {
	super(valueModel);
}
/**
 * CoIntegerConverter constructor comment.
 * @param valueModel com.bluebrim.base.client.CoValueModel
 */
public CoFloatConverter(CoValueModel valueModel, CoConvertibleUnitSet units) {
	super(valueModel, units);
}
protected Object getDefaultValue()
{
	return new Float(9999.99);
}
/**
 * setValue method comment.
 */
protected Object parseString(String newValue) throws ParseException
{
	Number tValue = (Number )super.parseString(newValue);
	return (tValue != null) ? new Float(tValue.floatValue()) : null;
}
}
