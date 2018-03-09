package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;
/**
 * CoToggleButtonAdaptor knyter ihop en CoValueModel, dvs ett objekt som
 * f�rst�r #getValue och #setValue med en gr�nssnittskomponent (JCheckBox eller JRadioButton)
 * som kan befinna sig i tv� tillst�nd - av och p�.
 * <br>Genom att registrera sig som en ActionListener p� knappen kan den
 * f�nga upp actions och omvandla dessa till #setValue f�r sin ValueModel och genom att
 * registrera sig som en CoValueListener p� sin ValueModel kan den f�nga upp
 * f�r�ndringar i dess v�rde och omvanda dessa till #setSelected-anrop f�r knappen.
 * <p>
 * Instansieras via CoUserInterfaceBuilder.createToggleButtonAdaptor.
 * @see CoUserInterfaceBuilder#createToggleButtonAdaptor
 * @author Lasse Svad�ngs 97-09-30
 *
*/
public class CoToggleButtonAdaptor extends CoComponentAdaptor implements CoValueListener, ActionListener{
	CoValueModel valueModel;
	JToggleButton button;
/**
 * This method was created by a SmartGuide.
 */
public CoToggleButtonAdaptor ( ) {
}
/**
 * This method was created by a SmartGuide.
 * @param aValueModel SE.corren.calvin.userinterface.CoValueModel
 * @param aButton JToggleButton
 */
public CoToggleButtonAdaptor ( CoValueModel aValueModel,JToggleButton aButton) {
	setValueModel(aValueModel);
	setButton(aButton);
	updateToggleButton();
}
/**
 * This method was created by a SmartGuide.
 * @param e java.awt.event.ActionEvent
 */
public void actionPerformed( ActionEvent e) {
	getValueModel().setValue(new Boolean (getButton().isSelected()));
}
/**
 * This method was created by a SmartGuide.
 * @return javax.swing.ToggleButton
 */
public JToggleButton getButton( ) {
	return button;
}
protected Component getComponent( ) {
	return getButton();
}
/**
 */
public CoValueModel getValueModel( ) {
	return valueModel;
}
/**
 * This method was created by a SmartGuide.
 * @return javax.swing.ToggleButton
 */
public void setButton( JToggleButton aButton) {
	button = aButton;
	button.setEnabled( valueModel.isEnabled() );

	aButton.addActionListener(this);
}
/**
 */
public void setValueModel( CoValueModel aValueModel) {
	valueModel = aValueModel;
	aValueModel.addValueListener(this);
}
/**
 * valueModelChange method comment.
 */
public void updateToggleButton() {
	Object tValue 		= getValueModel().getValue();
	Boolean aBoolean 	= (tValue != null) ? (Boolean )tValue : new Boolean(false);
	getButton().setSelected(aBoolean != null ? aBoolean.booleanValue(): false);
}
/**
  */
public void valueChange(CoValueChangeEvent anEvent) {
	Boolean aBoolean = (Boolean )anEvent.getNewValue();
	getButton().setSelected((aBoolean != null) ? aBoolean.booleanValue(): false);
}
}
