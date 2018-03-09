package com.bluebrim.gui.client;

import java.awt.Component;

import javax.swing.KeyStroke;

import com.bluebrim.swing.client.CoKeyStrokePane;

/**
 * @author Dennis
*/

public class CoKeyStrokePaneAdaptor extends CoComponentAdaptor implements CoValueListener, CoKeyStrokePane.KeyStrokeListener
{
	CoKeyStrokePane m_keyStrokePane;
	CoValueModel   m_valueModel;
/**
 * This method was created by a SmartGuide.
 */
public CoKeyStrokePaneAdaptor ( ) {
}
public CoKeyStrokePaneAdaptor( CoValueModel vm, CoKeyStrokePane p )
{
	setValueModel( vm );
	setKeyStrokePane( p );
	updateKeyStrokePane();
}
protected Component getComponent()
{
	return getKeyStrokePane();
}
public CoKeyStrokePane getKeyStrokePane()
{
	return m_keyStrokePane;
}
public CoValueModel getValueModel()
{
	return m_valueModel;
}
public void keyStrokeChanged( KeyStroke ks )
{
	getValueModel().setValue( ks );
}
/**
 * Access-metod för att byta knapp.
 * @param b den nya knappen
 */
protected void setKeyStrokePane( CoKeyStrokePane p )
{
		// sluta lyssna på ev föregångare
	if ( m_keyStrokePane != null ) m_keyStrokePane.removeKeyStrokeListener( this );
	
		// byt knapp
	m_keyStrokePane = p;

	m_keyStrokePane.setEnabled( m_valueModel.isEnabled() );
	
		// lyssna på den nya knappen
	if ( m_keyStrokePane != null ) m_keyStrokePane.addKeyStrokeListener( this );
}
public void setValueModel( CoValueModel vm )
{
	m_valueModel = vm;
	m_valueModel.addValueListener( this );
}
public void updateKeyStrokePane()
{
	KeyStroke ks = (KeyStroke) getValueModel().getValue();
	m_keyStrokePane.setKeyStroke( ks );
}
public void valueChange( CoValueChangeEvent ev )
{
	m_keyStrokePane.setKeyStroke( (KeyStroke) ev.getNewValue() );
}
}
