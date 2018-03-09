package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import com.bluebrim.base.shared.CoSetValueException;
/**
  	A component adaptor used to connect a <code>CoComboBox</code> with a value model holding the
 	selected element. It's listening for <code>ItemEvents</code> from the combobox and updates the value model when the
 	user makes a selection in the combobox.
 	<br>
 	An instance is created by <code>CoUserInterfaceBuilder.createComboBoxAdaptor()</code>.
 	
 */
public class CoComboBoxAdaptor extends CoComponentAdaptor implements ItemListener, CoValueListener {
	private JComboBox 		m_comboBox;
	private CoValueModel 	m_valueModel;
/**
 * This method was created by a SmartGuide.
 */
public CoComboBoxAdaptor ( ) {
}
/**
 */
public CoComboBoxAdaptor ( CoValueModel aValueModel,JComboBox aComboBox ) {
	this();
	setValueModel(aValueModel);
	setComboBox(aComboBox);
	updateComboBox();
}
public final JComboBox getComboBox() {
	return m_comboBox;
}
/**
 * getComponent method comment.
 */
protected Component getComponent() {
	return getComboBox();
}
public final CoValueModel getValueModel() {
	return m_valueModel;
}
/**
 	Användaren har selekterat ett nytt element i comboboxen.
 	Sätt mitt värdeobjekts värde till den nya selekteringen.
 */
public void itemStateChanged(ItemEvent e) {
	if (e.getStateChange() == ItemEvent.SELECTED) {
		try {
			getValueModel().setValue(e.getItem());
		} catch (CoSetValueException ex) {
			updateComboBox();
		}
	}
}
public final void setComboBox(JComboBox comboBox) {
	m_comboBox = comboBox;
	m_comboBox.setEnabled( m_valueModel.isEnabled() );
	m_comboBox.addItemListener(this);
}
public final void setValueModel(CoValueModel valueModel) {
	m_valueModel = valueModel;
	m_valueModel.addValueListener(this);
}
public void updateComboBox() 
{
	getComboBox().setSelectedItem(getValueModel().getValue());
	getComboBox().repaint();
}
/**
 */
public void valueChange(CoValueChangeEvent e) {
	Object tNewValue 	= e.getNewValue();
	Object tOldValue	= e.getOldValue();
	if ((tNewValue != tOldValue) && ((tNewValue == null) || !e.getNewValue().equals(e.getOldValue())))
		updateComboBox();
}
}
