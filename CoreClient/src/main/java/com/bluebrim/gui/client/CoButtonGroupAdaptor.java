package com.bluebrim.gui.client;

import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;

import com.bluebrim.swing.client.CoButtonGroup;
import com.bluebrim.swing.client.CoSelectedButtonEvent;
import com.bluebrim.swing.client.CoSelectedButtonListener;
/**
 	Connects a <code>CoButtonGroup</code>, i.e a a collection of radiobuttons where only one button
 	at a time cna be selected, with a value model whose value is the action command for the selected button.
 	<p>
 	As a listener for selection events from the button group the adaptor can catch a selection and
 	transform this to a <code>setValue</code> call to the value model and as a listener for value events from 
 	its value model it can catch value changes andtransform these to selections of a button in the group.
 	@see CoButtonGroup
 	@see CoSelectedButtonListener
	@author Lasse Svadängs 97-09-30
 */
public class CoButtonGroupAdaptor extends CoComponentAdaptor implements CoSelectedButtonListener, CoValueListener {
	CoValueModel valueModel;
	CoButtonGroup buttonGroup;
/**
 */
public CoButtonGroupAdaptor ( ) {
}
/**
 * @param aValueModel SE.corren.calvin.userinterface.CoValueModel
 * @param aButtonGroup SE.corren.calvin.userinterface.CoButtonGroup
 */
public CoButtonGroupAdaptor ( CoValueModel aValueModel,CoButtonGroup aButtonGroup) {
	setValueModel(aValueModel);
	setButtonGroup(aButtonGroup);
	updateButtonGroup();
}
/**
 * Skicka vidare till knappgruppen så att den kan 
 * enabla/disabla sina knappar.
 */
public void enableDisable(CoEnableDisableEvent e) {
	getButtonGroup().enableDisableButtons(e.enable());
}
/**
 * @return CoButtonGroup
 */
public CoButtonGroup getButtonGroup( ) {
	return buttonGroup;
}
protected Component getComponent( ) {
	return null;
}
/**
 * @return CoValueModel
 */
public CoValueModel getValueModel( ) {
	return valueModel;
}
/**
 * @param e CoSelectedButtonEvent
 */
public void selectedButtonChanged( CoSelectedButtonEvent e) {
	ButtonModel tButtonModel	= e.getSelectedButton();
	getValueModel().setValue(tButtonModel != null ? tButtonModel.getActionCommand() : null);
}
/**
 * @return CoButtonGroup
 */
public void setButtonGroup( CoButtonGroup aButtonGroup) {
	buttonGroup = aButtonGroup;
	buttonGroup.enableDisableButtons( valueModel.isEnabled());

	aButtonGroup.addSelectedButtonListener(this);
}
/**
 */
public void setValueModel( CoValueModel aValueModel) {
	valueModel = aValueModel;
	aValueModel.addValueListener(this);
}
/**
 * Uppdatera knappgruppen genom att selektera den knappen
 * som har sitt actionCommand lika med min ValueModels värde.
 */
public void updateButtonGroup() {
	Object tValue = getValueModel().getValue();
	String tString = (tValue != null) ? tValue.toString(): "";
	getButtonGroup().setSelected(tString);
}
/**
 */
public void valueChange(CoValueChangeEvent anEvent) {
	Object 			tNewValue 	= anEvent.getNewValue();
	String 			aString 	= tNewValue != null ? tNewValue.toString(): "";
	AbstractButton 	tButton		= getButtonGroup().commandToButton(aString);
	getButtonGroup().setSelected((tButton != null) ? tButton.getModel() : null, true);
	if ( tButton != null ) tButton.repaint();
}
}
