package com.bluebrim.gui.client;

/**
 @see CoNumberConverter
 * 
 */
public class CoPercentConverter extends CoNumberConverter {
/**
 * CoIntegerConverter constructor comment.
 * @param valueModel com.bluebrim.base.client.CoValueModel
 */
public CoPercentConverter(CoValueModel valueModel) {
	super(valueModel,null);
}
protected Object getDefaultValue()
{
	return new Float(0.50);
}
protected String getType()
{
	return CoUIStringResources.getName("PERCENT");
}
}
