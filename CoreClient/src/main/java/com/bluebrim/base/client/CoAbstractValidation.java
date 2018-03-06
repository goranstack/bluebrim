package com.bluebrim.base.client;

import java.beans.*;
import java.text.*;

import com.bluebrim.base.shared.*;
/**
	Abstract implementation of <code>CoValidationIF</code> which in its 
	implementation of <code>vetoableChange</code> calls <code>validate</code>
	and throws a <code>PropertyVetoException</code> if the validation fails.
	<br>
	Example of usage:
	<pre><code>
		...
		...
		
		getNamedValueModel(PUBLISHING_DATE).addVetoableChangeListener(new CoAbstractValidation(com.bluebrim.mediaproduction.impl.client.CoMediaProductionUIStringResources.getName(PUBLISHING_DATE)) {
			public boolean validate(Object newValue)
			{
				JSDate tDate = (JSDate )newValue;
				return isPublishedInEdition(tDate);
			}
			protected String valueToString(Object newValue)
			{
				return ((JSDate)newValue).format(DateFormat.SHORT);
			}
		});
		...
		...
	</pre></code>
	eg by installing it as a <code>VetoableChangeListener</code> in the value model it will try to validate the new value
	before the change is commited. If the validation fails the transaction is aborted.
 */
public abstract class CoAbstractValidation implements CoValidationIF {
	protected String propertyName;
public CoAbstractValidation()
{
}
public CoAbstractValidation(String propertyName)
{
	this();
	this.propertyName = propertyName;
}
/**
 */
protected String createErrorMessage(PropertyChangeEvent e) {
	MessageFormat tMessage	= new MessageFormat(CoStringResources.getName("VALIDATION_ERROR"));
	return tMessage.format(new Object[] {valueToString(e.getNewValue()), (propertyName != null) ? propertyName : e.getPropertyName()}) ;
}
/**
 */
public abstract boolean validate(Object value);
/**
 */
protected  String valueToString(Object value)
{
	return value != null ? value.toString() : new String();
}
/**
	Som default accepteras alla värden.
 */
public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
	if (!validate(e.getNewValue()))
		throw new PropertyVetoException(createErrorMessage(e),e);
}
}
