package com.bluebrim.gui.client;




import java.awt.*;

import javax.swing.*;

/**
 * This adaptor works much the same way as CoLabelAdaptor but sets the text of an AbstractButon instead of a CoLabel.
 * @author Dennis Malmström
 *
 */
 
public class CoButtonTextAdaptor extends CoComponentAdaptor implements CoValueListener
{




/**
 * This method was created by a SmartGuide.
 */
public CoButtonTextAdaptor ( ) {
}




protected Component getComponent() {
	return getButton();
}

/**
 */
protected String getStringFromEvent(CoValueChangeEvent anEvent) 
{
	return anEvent.getNewValue() == null ? " " : (String) anEvent.getNewValue() ;		
}
/**
 * @param aValueModel SE.corren.calvin.userinterface.CoValueModel
 */
public CoValueModel getValueModel () {
	return m_valueModel;
}

/**
 * @param aValueModel SE.corren.calvin.userinterface.CoValueModel
 */
public void setValueModel ( CoValueModel aValueModel) {
	m_valueModel = aValueModel;
	m_valueModel.addValueListener(this);
}

/**
 	Uppdatera värdeobjektet med texten i textfältet.
 */
public void updateValueModel() 
{
	getValueModel().setValue(getButton().getText());
}
/**
	Värdeobjektet har fått ett nytt värde och textfältet måste uppdateras.
 */
public void valueChange(CoValueChangeEvent anEvent) 
{
	String tNewValue = getStringFromEvent(anEvent) ;
	if (!tNewValue.equals(getButton().getText()))
	{
		getButton().setText(tNewValue);
	}	
		
}




	AbstractButton m_button;
	CoValueModel m_valueModel;

/**
 * @param aValueModel SE.corren.calvin.userinterface.CoValueModel
 * @param aLabel com.bluebrim.swing.client.CoLabel
 */
public CoButtonTextAdaptor (CoValueModel aValueModel, AbstractButton button) {
	setValueModel(aValueModel);
	setButton(button);
	updateButton();
}

public AbstractButton getButton()
{
	return m_button;
}

public void setButton( AbstractButton button )
{
	m_button = button;
}

/**
 */
public void updateButton() 
{
	Object tValue = getValueModel().getValue();
	getButton().setText((tValue != null) ? tValue.toString(): " ");
	getButton().getParent().validate();
}
}