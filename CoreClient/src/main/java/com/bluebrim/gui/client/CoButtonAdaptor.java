package com.bluebrim.gui.client;

// JDK imports
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;



/**
 * Abstract subklass till CoComponentAdaptor som används för att koppla ihop en AbstractButton 
 * med ett värdeobjekt. Subklasser till CoButtonAdaptor förväntas implementera den operation
 * som utförs på värdeobjektet när knappen aktiveras. CoButtonAdaptor stöder inte hantering
 * av ändringar i värdemodellen (man kan säga att det är en en-vägs-adaptor).
 */
public class CoButtonAdaptor extends CoComponentAdaptor implements ActionListener
{
	AbstractButton m_button;
	CoValueable   m_valueModel;

/**
 * Default-konstruktor.
 */
public CoButtonAdaptor()
{
}
/**
 * Konstruktor.
 * @param aValueModel värdemodellen
 * @param button knappen
 */
public CoButtonAdaptor( CoValueable aValueModel, AbstractButton button )
{
	this();
	setValueModel( aValueModel );
	setButton( button );
}
/**
 * Hantera knappaktivering.
 * Här förväntas subklasser implementera operationen på värdemodellen.
 */
public void actionPerformed( ActionEvent e )
{
	getValueModel().setValue( e );
}
/**
 * Access-metod till knappen.
 * @return knappen
 */
public AbstractButton getButton()
{
	return m_button;
}
protected Component getComponent()
{
	return getButton();
}
/**
 * Access-metod till värdemodellen.
 * @return värdemodellen
 */
public CoValueable getValueModel()
{
	return m_valueModel;
}
/**
 * Access-metod för att byta knapp.
 * @param b den nya knappen
 */
protected void setButton( AbstractButton b )
{
		// sluta lyssna på ev föregångare
	if ( m_button != null ) m_button.removeActionListener( this );
	
		// byt knapp
	m_button = b;

	m_button.setEnabled( m_valueModel.isEnabled() );
	
		// lyssna på den nya knappen
	if ( m_button != null ) m_button.addActionListener( this );
}
/**
 * Access-metod för att byta värdemodell.
 * @param b den nya värdemodellen
 */
protected void setValueModel( CoValueable valueModel )
{
	m_valueModel = valueModel;
}
}
