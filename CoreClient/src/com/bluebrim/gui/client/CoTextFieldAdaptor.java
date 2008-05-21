package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.bluebrim.base.client.CoConverterParseException;
import com.bluebrim.base.shared.CoBaseUtilities;
import com.bluebrim.base.shared.CoSetValueException;

/**
A <code>CoTextFieldAdaptor</code> connects a value model that understands <code>getValue</code>
 	och <code>setValue</code> with a textfield that understands <code>getText</code> and
 	<code>setText</code>.
 	<br>
 	As an action listener and focus listener with the textfield it's able to catch
 	changes in the text and transform those to calls to the <code>setValue</code> method 
 	in the value model and as a value listener it can catch changes of the value in the
 	value model and update the text field.
 	<p>
 	The value model is wrapped in an instance of a subclass to <code>CoConverter</code> before it's
 	passed into the <code>CoTextFieldAdaptor</code>. A <code>CoConverter</code> is a value model that 
 	is between the text field and actual value model representing the data and is responsible for 
 	parsing the text value into an object of the class for the value model. In the other direction 
 	it formats the value from the value model into a string for the text field.
 	<br>
 	An example of a converter is <code>CoDateConverter</code> which 
 	handles conversion between <code>String</code> and <code>Date</code> and a number of subclasses to 
 	<code>CoNumberConverter</code> which converts from numeuous numerical objects to string.
 	<p>
 	An instance of <code>CoTextFieldAdaptor</code> is created via <code>CoUserInterfaceBuilder.createTextFieldAdaptor</code>
 	<pre>
		builder.createTextFieldAdaptor(builder.addAspectAdaptor(new CoGsAspectAdaptor(this,"NAME") {
		protected Object get(CoObjectIF subject)
		{
		return ((com.bluebrim.mail.shared.CoGroupMailAddressIF)subject).getName();
		}
		public void set(CoObjectIF subject, Object aValue)
		{
		((com.bluebrim.mail.shared.CoGroupMailAddressIF)subject).setName(aValue != null ? aValue.toString() : null);
		}
		}),(CoTextField )getNamedWidget("NAME"));
 	</pre> 
 	
 */
public class CoTextFieldAdaptor extends CoComponentAdaptor implements CoValueListener
{
	private CoConverter 		m_converter;
	private JTextField 			m_textField;
	private boolean 			m_isActive;
	private ValidationPolicy 	m_validationPolicy;

	private boolean 			m_isDocumentDirty;
	private boolean				m_isLosingFocus;
	
	public static String NEVER_VALIDATE 					= "never_validate";
	public static String VALIDATE_ON_CLOSE 					= "validate_on_close";
	public static String VALIDATE_ON_FOCUS_LOST 			= "validate_on_focus_lost";
	public static String VALIDATE_ON_FOCUS_LOST_NOT_CLOSE 	= "validate_on_focus_lost_not_close";
	public static String VALIDATE_AND_KEEP_FOCUS 			= "validate_and_keep_focus";
	public static String VALIDATE_AND_KEEP_FOCUS_NOT_CLOSE 	= "validate_and_keep_focus_not_close";

	private static Hashtable policies;
	
	protected abstract static class ValidationPolicy
	{
		public abstract boolean onActionPerformed(CoTextFieldAdaptor textFieldAdaptor);
		public abstract boolean onFocusLost(CoTextFieldAdaptor textFieldAdaptor);
		public abstract boolean onClose(CoTextFieldAdaptor textFieldAdaptor);
		protected boolean validateWithoutErrorMessage(CoTextFieldAdaptor textFieldAdaptor)
		{
			try
			{
				validateAndSetValue(textFieldAdaptor.getTextField(), textFieldAdaptor.getConverter());
				return true;
			}
			catch (CoConverterParseException e)
			{
				textFieldAdaptor.updateTextField();
			}
			catch (PropertyVetoException e)
			{
				textFieldAdaptor.updateTextField();
			}
			catch (CoSetValueException e)
			{
				textFieldAdaptor.updateTextField();
			}
			return false;
		}
		protected boolean validateWithErrorMessage(CoTextFieldAdaptor textFieldAdaptor)
		{
			textFieldAdaptor.setIsActive(false);
			try
			{
				validateAndSetValue(textFieldAdaptor.getTextField(), textFieldAdaptor.getConverter());
				textFieldAdaptor.setIsActive(true);
				return true;
			}
			catch (CoConverterParseException e)
			{
				textFieldAdaptor.handleSetValueException(e.getMessage());
			}
			catch (PropertyVetoException e)
			{
				/**
				 * This code handles the correction of values suggested
				 * by the user. An error message is shown that can explain 
				 * that another more adequate value should be set.
				 *
				 * @author Ali Abida 2000-05-19.
				 */
				PropertyChangeEvent event = e.getPropertyChangeEvent();
				if (event != null) {
					textFieldAdaptor.handleCorrectedValue(e.getMessage(), event.getNewValue());
					textFieldAdaptor.setIsActive(true);
					return false;
				}
				else {
					textFieldAdaptor.handleSetValueException(e.getMessage());
				}
			}
			catch (CoSetValueException e)
			{
				if (e.failureHasBeenReported())
				{
					textFieldAdaptor.updateTextField();
				}
				else
					textFieldAdaptor.handleSetValueException(e.getMessage());
			}
			finally
			{
				textFieldAdaptor.setIsActive(true);
			}
			return false;
		}
		protected void validateAndSetValue(JTextField textField, CoConverter converter) throws CoConverterParseException, PropertyVetoException
		{
			Object oldValue = converter.getValue();
			converter.setValueFromString(textField.getText());
			Object newValue = converter.getValue();

			if (!CoGUI.hasValueChanged(oldValue, newValue) && newValue != null) {
				textField.setText(converter.valueToString(newValue));
			}
		}
	}


	
	protected static class NeverValidatePolicy extends ValidationPolicy
	{
		public boolean onActionPerformed(CoTextFieldAdaptor textFieldAdaptor)
		{
			return true;
		}
		public boolean onFocusLost(CoTextFieldAdaptor textFieldAdaptor)
		{
			return true;
		}
		public boolean onClose(CoTextFieldAdaptor textFieldAdaptor)
		{
			return true;
		}
	}


	
	protected static class ValidateOnClosePolicy extends ValidationPolicy
	{
		public boolean onActionPerformed(CoTextFieldAdaptor textFieldAdaptor)
		{
			validateWithoutErrorMessage(textFieldAdaptor);
			return true;
		}
		public boolean onFocusLost(CoTextFieldAdaptor textFieldAdaptor)
		{
			validateWithoutErrorMessage(textFieldAdaptor);
			return true;
		}
		public boolean onClose(CoTextFieldAdaptor textFieldAdaptor)
		{
			return validateWithErrorMessage(textFieldAdaptor);
		}
	}


	
	protected static class ValidateOnFocusLostPolicy extends ValidateOnClosePolicy
	{
		public boolean onActionPerformed(CoTextFieldAdaptor textFieldAdaptor)
		{
			return validateWithErrorMessage(textFieldAdaptor);
//			return true;
		}
		public boolean onFocusLost(CoTextFieldAdaptor textFieldAdaptor)
		{
			return validateWithErrorMessage(textFieldAdaptor);
//			return true;
		}
	}

	
	protected static class ValidateOnFocusLostNotClosePolicy extends ValidateOnFocusLostPolicy
	{
		public boolean onClose(CoTextFieldAdaptor textFieldAdaptor)
		{
			return true;
		}
	}

	
	protected static class ValidateAndKeepFocusPolicy extends ValidateOnFocusLostPolicy
	{
		public boolean onFocusLost(CoTextFieldAdaptor textFieldAdaptor)
		{
			textFieldAdaptor.setIsActive(false);
			JTextField 	tTextField 	= textFieldAdaptor.getTextField();
			CoConverter tConverter	= textFieldAdaptor.getConverter();	
			try
			{
				validateAndSetValue(tTextField, tConverter);
				textFieldAdaptor.setIsActive(true);
				return true;
			}
			catch (CoConverterParseException e)
			{
				textFieldAdaptor.handleSetValueException(e.getMessage());
				resetTextField(tTextField, tConverter.getStringValue());
			}
			catch (PropertyVetoException e)
			{
				textFieldAdaptor.handleSetValueException(e.getMessage());
				resetTextField(tTextField, tConverter.getStringValue());
			}
			catch (CoSetValueException e)
			{
				if (!e.failureHasBeenReported())
					textFieldAdaptor.handleSetValueException(e.getMessage());
				resetTextField(tTextField, tConverter.getStringValue());
			}
			finally
			{
				textFieldAdaptor.setIsActive(true);
			}
			return false;
		}
		private void resetTextField(final JTextField textField, final Object aValue)
		{
			SwingUtilities.invokeLater(new Runnable() {
				public void run()
				{
					textField.setText((aValue != null) ? aValue.toString(): "");
					textField.requestFocus();
					textField.selectAll();
					textField.repaint();
				}
			});
		}
	}

	protected static class ValidateAndKeepFocusNotClosePolicy extends ValidateAndKeepFocusPolicy
	{
		public boolean onClose(CoTextFieldAdaptor textFieldAdaptor)
		{
			return true;
		}
	}

	static {
		policies = new Hashtable(3);
		policies.put(NEVER_VALIDATE, 						new NeverValidatePolicy());
		policies.put(VALIDATE_ON_FOCUS_LOST, 				new ValidateOnFocusLostPolicy());
		policies.put(VALIDATE_ON_FOCUS_LOST_NOT_CLOSE, 		new ValidateOnFocusLostNotClosePolicy());
		policies.put(VALIDATE_ON_CLOSE, 					new ValidateOnClosePolicy());
		policies.put(VALIDATE_AND_KEEP_FOCUS, 				new ValidateAndKeepFocusPolicy());
		policies.put(VALIDATE_AND_KEEP_FOCUS_NOT_CLOSE, 	new ValidateAndKeepFocusNotClosePolicy());
	}

/**
 * @param aValueModel SE.corren.calvin.userinterface.CoValueModel
 * @param aTextField com.sun.ajav.swing.JTextField
 */
public CoTextFieldAdaptor ( CoConverter aValueModel,JTextField aTextField) {
	this(aValueModel, aTextField, VALIDATE_ON_CLOSE);
}
/**
  */
public CoTextFieldAdaptor ( CoConverter converter,JTextField aTextField, String validationPolicy) {
	m_isActive = true;
	setConverter(converter);
	setTextField(aTextField);
	updateTextField();
	setValidationPolicy(validationPolicy);
}
/**
 * This makes textfields with read only adaptors disabled.
 */
public void enableDisable(CoEnableDisableEvent e) {
	boolean enable = e.enable && !getConverter().isReadOnly();
	getTextField().setEnabled(enable);
	getTextField().repaint();
}
protected void focusGained()
{
	markDocumentDirty(isDirty());
	
	m_textField.selectAll();
	m_textField.repaint();
}
protected void focusLost()
{
	if (m_isLosingFocus)
		return;
	if ( ! m_isDocumentDirty ) 
	{
		m_textField.select(0, 0);
		m_textField.repaint();
		return;
	}

	m_isLosingFocus = true;

	try
	{
		if (m_textField.isEditable())
		{
			if (m_validationPolicy.onFocusLost(this))
			{
				m_textField.select(0, 0);
				m_textField.repaint();
				// If the validation grabs focus, i e by showing a dialog,
				// the text field gets focus again when this dialog is closed.
				// The code below fixes this !
				if (m_textField.hasFocus())
					m_textField.dispatchEvent(new FocusEvent(m_textField, FocusEvent.FOCUS_LOST, false));
			}
			else
			{
				invokeRequestFocus();
			}
		}
		else
		{
			m_textField.select(0, 0);
			m_textField.repaint();
		}
	}
	finally
	{
		m_isLosingFocus = false;
	}
}
protected Component getComponent() {
	return getTextField();
}
/**
 */
protected String getStringFromEvent(CoValueChangeEvent anEvent) 
{
	return anEvent.getNewValue() == null ? "" : (String) anEvent.getNewValue() ;		
}
private JTextField getTextField() {
	return m_textField;
}

/**
 * Handles the correction of values suggested by the user.
 * For example if the user tries to set first delivery date
 * for a delivery order to a day that is not publishing day
 * the system may want to suggest another day.
 * 
 * @author Ali Abida 2000-05-19.
 */
protected void handleCorrectedValue(final String errorMessage, final Object correctedValue) {
	Toolkit.getDefaultToolkit().beep();
	
	if (!CoBaseUtilities.stringIsEmpty(errorMessage)) {
		JOptionPane.showMessageDialog(getTextField(), errorMessage ,"", JOptionPane.ERROR_MESSAGE);
		updateTextField(correctedValue);
	}
	else
		updateTextField(correctedValue);
}
/**
 	En exception har inträffat när ett nytt värde skulle
 	sättas via textfältet. En exception kan antingen bero på att
 	inmatat värde var av ett felaktigt format, exempelvis ett
 	icke-numeriskt värde i ett numeriskt fält, eller att värdet
 	inte ingår i den tillåtna värdemängden.
 	
 */
protected void handleSetValueException(final String errorMessage) 
{
	java.awt.Toolkit.getDefaultToolkit().beep();
	if (!CoBaseUtilities.stringIsEmpty(errorMessage))
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				JOptionPane.showMessageDialog(getTextField(),errorMessage ,"", JOptionPane.ERROR_MESSAGE);
				updateTextField();
			}
		});
	}
	else
		updateTextField();
}
/**
 */
private void setIsActive(boolean state) 
{
	m_isActive = state;
}
private void setTextField(JTextField aTextField)
{
	m_textField = aTextField;
	m_textField.setEnabled( m_converter.isEnabled() );

	m_textField.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			m_validationPolicy.onActionPerformed(CoTextFieldAdaptor.this);
		}
	});
	m_textField.addFocusListener(new FocusListener()
	{
		public void focusGained(FocusEvent e)
		{
			if (!e.isTemporary())
				CoTextFieldAdaptor.this.focusGained();
		}
		public void focusLost(FocusEvent e)
		{
			if (!e.isTemporary())
				CoTextFieldAdaptor.this.focusLost();
		}
	});


	m_textField.getDocument().addDocumentListener(
		new DocumentListener()
		{
			public void changedUpdate( DocumentEvent e ) { markDirty(); }
			public void insertUpdate( DocumentEvent e ) { markDirty(); }
			public void removeUpdate( DocumentEvent e ) { markDirty(); }
			private void markDirty()
			{
				markDocumentDirty(true);
			}
		}
	);
}
/**
 */
public final void setValidationPolicy(String aValidationPolicy) 
{
	m_validationPolicy = (ValidationPolicy)policies.get(aValidationPolicy);
}

/**
 * Uppdatera textfältet med värdeobjektets värde.
 */
protected void updateTextField() 
{
	String stringValue = m_converter.getStringValue();
	m_textField.setText(stringValue != null ? stringValue: "");
	markDocumentDirty(false);

}
/**
 * Updates the textfield with a new corrected value.
 *
 * PENDING: The Method CoConverter#valueToString should perhaps be public. 
 *			Note that this method uses the fact that protected access in 
 *			java mean that objects in the same package can call eachothers 
 *			protected methods.
 *
 * @author Ali Abida 2000-05-19.
 */
protected void updateTextField(Object correctedValue) {
	String corrected = m_converter.valueToString(correctedValue);
	m_textField.setText((corrected != null) ? corrected : "");
	m_textField.selectAll();
	m_textField.repaint();
	markDocumentDirty(true);
}
public void userInterfaceActivated(CoUserInterfaceEvent e)
{
	setIsActive(true);
}
/**
	Uppdatera värdeobjektet innan fönstret stängs.
*/
public void userInterfaceClosing(CoUserInterfaceEvent e) 
{
	if ( ! m_isDocumentDirty ) return;
	
	if (m_textField.isEnabled())
	{
		if (!m_validationPolicy.onClose(this))
			e.consume();
	}
}
public void userInterfaceDeactivated(CoUserInterfaceEvent e)
{
	setIsActive(false);
}
public void userInterfaceValidated(CoUserInterfaceEvent e)
{
	if ( ! m_isDocumentDirty ) return;
	
	if (!m_validationPolicy.onClose(this))
		e.consume();
}
/**
	Värdeobjektet har fått ett nytt värde och textfältet måste uppdateras.
 */
public void valueChange(CoValueChangeEvent anEvent) 
{
	Object source = anEvent.getSource();
	if (source != m_converter)
		return;
	if (isDirty())
	{
		m_textField.setText( m_converter.getStringValue());
		markDocumentDirty(false);

	}	
		
}


/**
 */
public final CoConverter getConverter () {
	return m_converter;
}


private void invokeRequestFocus() {
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
			m_textField.requestFocus();
		}
	});
}


private boolean isDirty() 
{
	String stringValue = m_converter.getStringValue() ;
	return !stringValue.equals(m_textField.getText());
}


private void markDocumentDirty (boolean dirty) {
	m_isDocumentDirty = dirty;
}


/**
	Sätt 'valueModel' till värdeobjektet i argumenten
	och låt mottagaren lyssna efter förändringar i dess värde.
 */
private void setConverter ( CoConverter converter) {
	if (m_converter != converter)
	{
		if (converter != null)
			converter.removeValueListener(this);
		m_converter = converter;
		if (m_converter != null)
			m_converter.addValueListener(this);
	}
}

/**
 * Added this as a way to detect what triggered
 * a setValue() in a valuable. Not very pretty.
 *
 * @author Markus Persson 2001-04-05
 */
public boolean isLosingFocus() {
	return m_isLosingFocus;
}
}