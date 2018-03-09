package com.bluebrim.gui.client;

import java.awt.Component;

import com.bluebrim.swing.client.CoLabel;

/**
 * En CoLabelAdaptor knyter ihop en CoValueModel som förstår #getValue
 * och #setValue med ett CoLAbel som förstår #setText och #getText.
 * <br>Genom att registrera sig som CoValueListener hos sin CoValueModel kan den
 * på samma sätt fånga upp ändringar i dess värde och omvandla dessa till anrop
 * av #setText hos labelfältet.
 * <p>
 * Instansieras via CoUserInterfaceBuilder.createLabelAdaptor.
 * @see CoUserInterfaceBuilder#createLabelAdaptor
 * @author Helena Rankegård 98-06-16
 *
 */
public class CoLabelAdaptor extends CoComponentAdaptor implements CoValueListener {
	CoValueModel	valueModel;
	CoLabel label;


/**
 * This method was created by a SmartGuide.
 */
public CoLabelAdaptor ( ) {
}
/**
 * @param aValueModel SE.corren.calvin.userinterface.CoValueModel
 * @param aLabel com.bluebrim.swing.client.CoLabel
 */
public CoLabelAdaptor (CoValueModel aValueModel, CoLabel aLabel) {
	setValueModel(aValueModel);
	setLabel(aLabel);
	updateLabel();
}



protected Component getComponent() {
	return getLabel();
}
public CoLabel getLabel() {
	return label;
}
/**
 */
protected String getStringFromEvent(CoValueChangeEvent anEvent) 
{
	return anEvent.getNewValue() == null ? "" : (String) anEvent.getNewValue() ;		
}
/**
 * @param aValueModel SE.corren.calvin.userinterface.CoValueModel
 */
public CoValueModel getValueModel () {
	return valueModel;
}
public void setLabel (CoLabel aLabel) {
	label = aLabel;
	//label.addActionListener(this);
//	label.addFocusListener(this);
}
/**
 * @param aValueModel SE.corren.calvin.userinterface.CoValueModel
 */
public void setValueModel ( CoValueModel aValueModel) {
	valueModel = aValueModel;
	valueModel.addValueListener(this);
}
/**
 */
public void updateLabel() 
{
	Object tValue = getValueModel().getValue();
	getLabel().setText((tValue != null) ? tValue.toString(): "");
	getLabel().getParent().validate();
}
/**
 	Uppdatera värdeobjektet med texten i textfältet.
 */
public void updateValueModel() 
{
	getValueModel().setValue(getLabel().getText());
}
/**
	Värdeobjektet har fått ett nytt värde och textfältet måste uppdateras.
 */
public void valueChange(CoValueChangeEvent anEvent) 
{
	String tNewValue = getStringFromEvent(anEvent) ;
	if (!tNewValue.equals(getLabel().getText()))
	{
		getLabel().setText(tNewValue);
	}	
		
}



}