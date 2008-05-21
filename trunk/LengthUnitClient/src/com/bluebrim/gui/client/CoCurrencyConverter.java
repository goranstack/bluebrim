package com.bluebrim.gui.client;

import com.bluebrim.gui.client.CoUIStringResources;
import com.bluebrim.gui.client.CoValueModel;
/**
 @see CoNumberConverter
 * 
 */
public class CoCurrencyConverter extends CoNumberConverter {
/**
 * CoIntegerConverter constructor comment.
 * @param valueModel com.bluebrim.base.client.CoValueModel
 */
public CoCurrencyConverter(CoValueModel valueModel) {
	super(valueModel,null);
}
protected Object getDefaultValue()
{
	return new Double(9999.99);
}
protected String getType()
{
	return CoUIStringResources.getName("CURRENCY");
}
}
