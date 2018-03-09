package com.bluebrim.gui.client;

import java.beans.PropertyVetoException;
import java.text.MessageFormat;
import java.text.ParseException;

import com.bluebrim.base.client.CoConverterParseException;
import com.bluebrim.base.shared.CoSetValueException;

/**
 * Abstract superclass for all conversion classes.
 * <br>
 * A converter is a value model that lies between a
 * text field adaptor and its value model and whose 
 * responsibility is to convert between the string
 * delivered from the text field to the type of object
 * the value model expects.
 * <br>
 * Has concrete subclasses for dates and numbers.
 * <p>
 * By using the factory methods in the userinterface builder 
 * we normally don't have to caer about using the right 
 * conversion class.
 *  
 * <code><pre>
 * 	....
 * 	
 * 	builder.createNumberFieldAdaptor(builder.addAspectAdaptor(new CoTestModel_ShoeSize(this,"ShoeSize")), 
 * 																(CoTextField )getNamedWidget("ShoeSize"),
 * 																CoNumberConverter.INTEGER);
 * 
 * 	builder.createDateFieldAdaptor(builder.addAspectAdaptor(new CoTestModel_DateOfBirth(this,"DateOfBirth")), 
 * 															(CoTextField )getNamedWidget("DateOfBirth"),
 * 															DateFormat.SHORT);
 * 	....
 * </pre></code>
 *
 * There are two different ways to set the value for the value model the converter represents.
 * <ul>
 * <li><code>setValueFromString</code> that first tries to convert the string to an object of the
 * right type before setting the value of the client value model.
 * <li><code>setValue</code> that if the value isn't a string directly calls <code>setValue</code>
 * for the client value model. If the value is a string the setting goes through <code>setValueFromString</code>
 * </ul>
 * <br>
 * <code>CoTextFieldAdaptor</code> uses <code>setValueFromString</code> to be able to display
 * an error message if the value wasn't accepted.
 */
public abstract class CoConverter extends CoValueModelWrapper {

	final public static String BASIC_CONVERTER_PARSE_ERROR = "BASIC_CONVERTER_PARSE_ERROR";

	protected static boolean isString(Object obj) {
		return obj instanceof String;
	}

	public CoConverter(CoValueModel valueModel) {
		super(valueModel);
	}

	protected abstract String doFormat(Object aValue);

	protected String getParseError(Object newValue) {
		Object tArgs[] = { getType(), newValue.toString()};
		return MessageFormat.format(CoUIStringResources.getName(BASIC_CONVERTER_PARSE_ERROR), tArgs);
	}

	public String getStringValue() {
		return valueToString(getValue());
	}

	protected abstract String getType();

	/**
	 * Default, and dummy implementation that just
	 * returns the argument.
	 */
	protected Object parseString(String newValue) throws ParseException {
		return newValue;
	}

	/**
	 * A reimplementation that checks if <code>newValue</code> 
	 * is  a string. If this is the case the setting of 
	 * the new value is done via <code>setValueFromString</code>.
	 * <br>
	 * This fix enables users, i e <code>CoComboBoxAdaptor</code>, of 
	 * this class to use <code>setValue</code> even if the new value
	 * is a string instead of the supported data type.
	 * <br>
	 * PENDING: <code>setValueFromString</code> can throw exceptions.
	 * How do we handle this?
	 * <br>
	 * As a first try a <code>CoSetValueException</code> is thrown.
	 */
	public void setValue(Object newValue) {
		if (isString(newValue)) {
			try {
				setValueFromString((String) newValue);
			} catch (CoConverterParseException cpe) {
				throw new CoSetValueException(true);
			} catch (PropertyVetoException pve) {
				throw new CoSetValueException(true);
			}
		} else
			getClient().setValue(newValue);
	}

	public void setValueFromString(String newValue) throws CoConverterParseException, PropertyVetoException {
		try {
			Object value = parseString(newValue);
			getClient().validate(value);
			getClient().setValue(value);
		} catch (ParseException e) {
			throw new CoConverterParseException(getParseError(newValue));
		}
	}

	/**
		Låter 'formatter' formattera 'aValue' och svara med
		den sträng som blir resultatet. 
	 */
	public String valueToString(Object aValue) {
		return (aValue != null) ? doFormat(aValue) : "";
	}
}