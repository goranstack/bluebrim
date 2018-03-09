package com.bluebrim.gui.client;

import java.text.*;

import com.bluebrim.base.shared.*;
/**
 @see CoNumberConverter
 * 
 */
public class CoDoubleConverter extends CoNumberConverter {
/**
 * CoIntegerConverter constructor comment.
 * @param valueModel com.bluebrim.base.client.CoValueModel
 */
public CoDoubleConverter(CoValueModel valueModel ) {
	super(valueModel );
}
/**
 * CoIntegerConverter constructor comment.
 * @param valueModel com.bluebrim.base.client.CoValueModel
 */
public CoDoubleConverter(CoValueModel valueModel, CoConvertibleUnitSet units ) {
	super(valueModel, units );
}
protected Object getDefaultValue()
{
	return new Double(9999.99);
}
/**
 * setValue method comment.
 */
protected Object parseString(String newValue) throws ParseException
{
	Number tValue = (Number )super.parseString(newValue);
	return (tValue != null) ? new Double(tValue.doubleValue()) : null;
}
}
