package com.bluebrim.gui.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
/**
 * CoVisibleComponentAdaptor knyter ihop en CoValueModel, dvs ett objekt som
 * förstår #getValue och #setValue med en gränssnittskomponent (JCheckBox eller JRadioButton)
 * som kan befinna sig i två tillstånd - av och på.
 * <br>Genom att registrera sig som en ActionListener på knappen kan den
 * fånga upp actions och omvandla dessa till #setValue för sin ValueModel och genom att
 * registrera sig som en CoValueListener på sin ValueModel kan den fånga upp
 * förändringar i dess värde och omvanda dessa till #setSelected-anrop för knappen.
 * <p>
 * Instansieras via CoUserInterfaceBuilder.createToggleButtonAdaptor.
 * @see CoUserInterfaceBuilder#createToggleButtonAdaptor
 * @author Lasse Svadängs 97-09-30
 *
*/
public class CoVisibleComponentAdaptor extends CoComponentAdaptor implements CoValueListener, ActionListener{
//	CoValueModel valueModel;
//	JToggleButton button;
	CoValueModel 	m_valueModel;
	JComponent		m_component;
/**
 * This method was created by a SmartGuide.
 */
public CoVisibleComponentAdaptor ( ) {
}
/**
 * This method was created by a SmartGuide.
 * @param aValueModel SE.corren.calvin.userinterface.CoValueModel
 * @param aButton JToggleButton
 */
public CoVisibleComponentAdaptor ( CoValueModel aValueModel,JComponent aComponent) {
	setValueModel(aValueModel);
	setComponent(aComponent);
	updateComponent();
}
/**
 * This method was created by a SmartGuide.
 * @param e java.awt.event.ActionEvent
 */
public void actionPerformed( ActionEvent e) {
//	getValueModel().setValue(new Boolean (getButton().isSelected()));
}
protected Component getComponent( ) {
	return getVisibleComponent();
}
/**
 */
public CoValueModel getValueModel( ) {
	return m_valueModel;
}
/**
 * This method was created by a SmartGuide.
 * @return javax.swing.ToggleButton
 */
public JComponent getVisibleComponent( ) {
	return m_component;
}
/**
 * This method was created by a SmartGuide.
 * @return javax.swing.ToggleButton
 */
public void set( JComponent aComponent) {
	m_component = aComponent;
	m_component.setEnabled( m_valueModel.isEnabled() );

//	aButton.addActionListener(this);
}
/**
 * This method was created by a SmartGuide.
 * @return javax.swing.ToggleButton
 */
public void setComponent( JComponent aComponent) {
	m_component = aComponent;
	m_component.setEnabled( m_valueModel.isEnabled() );

//	m_component.addActionListener(this);
}
/**
 */
public void setValueModel( CoValueModel aValueModel) {
	m_valueModel = aValueModel;
	m_valueModel.addValueListener(this);
}
/**
 * valueModelChange method comment.
 */
public void updateComponent() {
	Object tValue 		= getValueModel().getValue();
	Boolean aBoolean 	= (tValue != null) ? (Boolean )tValue : new Boolean(false);
	getVisibleComponent().setVisible(aBoolean != null ? aBoolean.booleanValue(): false);
}
/**
  */
public void valueChange(CoValueChangeEvent anEvent) {
	Boolean aBoolean = (Boolean )anEvent.getNewValue();
	getVisibleComponent().setVisible((aBoolean != null) ? aBoolean.booleanValue(): false);
}
}
