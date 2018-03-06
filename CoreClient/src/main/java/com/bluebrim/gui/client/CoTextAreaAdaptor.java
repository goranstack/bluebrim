package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;

import javax.swing.JTextArea;
/**
 * En CoTextAreaAdaptor knyter ihop en v�rdeobjekt som f�rst�r #getValue
 * och #setValue med ett textarea som f�rst�r #setText och #getText.
 * Genom att registrera sig som ActionListener och FocusListener hos textarea
 * kan den f�nga  �ndringar i detta och omvandla dessa till anrop av 
 * #setValue till sitt v�rdeobjekt genom att registrera sig som CoValueListener 
 * hos sitt v�rdeobjekt kan den p� samma s�tt f�nga upp �ndringar i dess v�rde och 
 * omvandla dessa till anrop av #setText hos textf�ltet.<br>
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
 	N�r textarean f�r fokus s� selekteras all text.
 */
public void focusGained(FocusEvent e) {
	getTextArea().selectAll();
}
/**
 	N�r textarean tappar fokus s� uppdateras v�rdeobjektet
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
	S�tt 'valueModel' till v�rdeobjektet i argumenten
	och l�t mottagaren lyssna efter f�r�ndringar i dess v�rde.
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
	Uppdatera textarean med v�rdeobkektets v�rde.
 */
protected void updateTextArea() 
{
	Object tValue 			= getValueModel().getValue();
	JTextArea tTextArea		= getTextArea();
	tTextArea.setText((tValue != null) ? tValue.toString(): "");
}
/**
 	Uppdatera v�rdeobjektet med texten i textarean.
 */
protected void updateValueModel() 
{
	getValueModel().setValue(getTextArea().getText());
}
/**
	V�rdeobjektet har f�tt ett nytt v�rde och textf�ltet m�ste uppdateras.
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
	Uppdatera v�rdeobjektet innan f�nstret st�ngs.
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
