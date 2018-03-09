package com.bluebrim.gui.client;
import java.text.MessageFormat;
import java.text.ParseException;

import javax.swing.JOptionPane;

/**
 * Abstract superclass for <code>converters</code> where the 
 * conversion is done by either
 * <ul>
 * <li>using methods in the class representing 
 * the type, i e <code>com.bluebrim.base.shared.CoDate</code> or 
 * <li> using one of the format classes from the 
 * JDK.
 * </ul>
 */
public abstract class CoFormatConverter extends CoConverter {
/**
 * CoDateConverter constructor comment.
 */
public CoFormatConverter(CoValueModel valueModel) {
	super(valueModel);
}


/**
 	L�t formatteringsobjektet parsa 'newValue'. Svarar med null om 
 	parsningen misslyckas.
 */
protected abstract Object doParse(String stringValue) throws ParseException;


/**
	Subklassen svarar med ett objekt som representerar 
	ett defaultv�rde f�r felmeddelanden mm.
 */
protected abstract Object getDefaultValue();


protected String getParseError(Object newValue) 
{
	Object tArgs[] = 
	{
		getType(),
		newValue.toString(),
		valueToString(getDefaultValue())};
	return MessageFormat.format(CoUIStringResources.getName("CONVERTER_PARSE_ERROR"), tArgs);
}


/**
 * Returns the parsed value of <code>newValue</code> by
 * dispatching to <code>doParse</code>.
 * If <code>newValue</code> is null, null is returned.
 */
protected Object parseString(String newValue) throws ParseException
{
	return newValue != null ? doParse(newValue) : null; 
}


/**
 	L�t formatteringsobjektet parsa 'newValue'. Svarar med null om 
 	parsningen misslyckas.
 */
protected void showParseError(Object newValue) 
{
	JOptionPane.showMessageDialog(null, "Felaktigt format. Anv�nd formen "+valueToString(getDefaultValue()),
										"Felaktigt format", JOptionPane.ERROR_MESSAGE);
}
}