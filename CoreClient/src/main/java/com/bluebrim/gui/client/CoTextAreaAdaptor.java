package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;

import javax.swing.JTextArea;
/**
 * En CoTextAreaAdaptor knyter ihop en värdeobjekt som förstår #getValue
 * och #setValue med ett textarea som förstår #setText och #getText.
 * Genom att registrera sig som ActionListener och FocusListener hos textarea
 * kan den fånga  ändringar i detta och omvandla dessa till anrop av 
 * #setValue till sitt värdeobjekt genom att registrera sig som CoValueListener 
 * hos sitt värdeobjekt kan den på samma sätt fånga upp ändringar i dess värde och 
 * omvandla dessa till anrop av #setText hos textfältet.<br>
 *
 *
 * Instansieras via CoUserInterfaceBuilder.createTextAreaAdaptor.
 * @see CoUserInterfaceBuilder#createAreaFieldAdaptor
 *
 */
public class CoTextAreaAdaptor extends CoComponentAdaptor implements FocusListener, CoValueListener {
	CoValueable 	valueModel;
	JTextArea  		textArea;
	boolean isActive 	= true;
/**
 * This method was created by a SmartGuide.
 */
public CoTextAreaAdaptor ( ) {
}
/**
 */
public CoTextAreaAdaptor ( CoValueable aValueModel,JTextArea aTextArea) {
	setValueModel(aValueModel);
	setTextArea(aTextArea);
	updateTextArea();
}
/**
 	När textarean får fokus så selekteras all text.
 */
public void focusGained(FocusEvent e) {
	getTextArea().selectAll();
}
/**
 	När textarean tappar fokus så uppdateras värdeobjektet
 	och texten avselekteras.
 */
public void focusLost(FocusEvent e) 
{
	if (isActive)
	{
		updateValueModel();
		getTextArea().select(0,0);
	}	
}
protected Component getComponent() {
	return getTextArea();
}
/**
 */
protected String getStringFromEvent(CoValueChangeEvent anEvent) 
{
	return anEvent.getNewValue() == null ? "" : (String) anEvent.getNewValue() ;		
}
public JTextArea getTextArea() {
	return textArea;
}
/**
 */
public CoValueable getValueModel () {
	return valueModel;
}
public void setTextArea( JTextArea aTextArea) {
	textArea = aTextArea;
	textArea.setEnabled( valueModel.isEnabled() );

	textArea.addFocusListener(this);
}
/**
	Sätt 'valueModel' till värdeobjektet i argumenten
	och låt mottagaren lyssna efter förändringar i dess värde.
 */
public void setValueModel ( CoValueable aValueModel) {
	if (valueModel != aValueModel)
	{
		if (valueModel != null)
			valueModel.removeValueListener(this);
		valueModel = aValueModel;
		if (valueModel != null)
			valueModel.addValueListener(this);
	}
}
/**
	Uppdatera textarean med värdeobkektets värde.
 */
protected void updateTextArea() 
{
	Object tValue 			= getValueModel().getValue();
	JTextArea tTextArea		= getTextArea();
	tTextArea.setText((tValue != null) ? tValue.toString(): "");
}
/**
 	Uppdatera värdeobjektet med texten i textarean.
 */
protected void updateValueModel() 
{
	getValueModel().setValue(getTextArea().getText());
}
/**
	Värdeobjektet har fått ett nytt värde och textfältet måste uppdateras.
 */
public void valueChange(CoValueChangeEvent anEvent) 
{
	String tNewValue = getStringFromEvent(anEvent) ;
	if (!tNewValue.equals(getTextArea().getText()))
	{
		getTextArea().setText(tNewValue);
	}	
		
}
public void windowActivated(WindowEvent e)
{
	isActive = true;
}
/**
	Uppdatera värdeobjektet innan fönstret stängs.
*/
public void windowClosing(WindowEvent e) 
{
	if (getTextArea().isEnabled())
		updateValueModel();
}
public void windowDeactivated(WindowEvent e)
{
	isActive = false;
}
}
