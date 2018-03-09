package com.bluebrim.gui.client;

import java.awt.Component;

import com.bluebrim.swing.client.CoLabel;

/**
 * En CoLabelAdaptor knyter ihop en CoValueModel som f�rst�r #getValue
 * och #setValue med ett CoLAbel som f�rst�r #setText och #getText.
 * <br>Genom att registrera sig som CoValueListener hos sin CoValueModel kan den
 * p� samma s�tt f�nga upp �ndringar i dess v�rde och omvandla dessa till anrop
 * av #setText hos labelf�ltet.
 * <p>
 * Instansieras via CoUserInterfaceBuilder.createLabelAdaptor.
 * @see CoUserInterfaceBuilder#createLabelAdaptor
 * @author Helena Rankeg�rd 98-06-16
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
 	Uppdatera v�rdeobjektet med texten i textf�ltet.
 */
public void updateValueModel() 
{
	getValueModel().setValue(getLabel().getText());
}
/**
	V�rdeobjektet har f�tt ett nytt v�rde och textf�ltet m�ste uppdateras.
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