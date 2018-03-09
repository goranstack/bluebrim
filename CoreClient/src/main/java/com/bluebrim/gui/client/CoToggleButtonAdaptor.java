package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;
/**
 * CoToggleButtonAdaptor knyter ihop en CoValueModel, dvs ett objekt som
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
