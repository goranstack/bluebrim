package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;

public class CoCheckBoxMenuItemAdaptor extends CoComponentAdaptor implements CoValueListener, ActionListener
{
	CoValueable m_valueModel;
	JCheckBoxMenuItem m_button;
public CoCheckBoxMenuItemAdaptor( CoValueable vm, JCheckBoxMenuItem cb) 
{
	setValueModel( vm );
	setCheckBox( cb );
	updateCheckBox();
}
/**
 * This method was created by a SmartGuide.
 * @param e java.awt.event.ActionEvent
 */
public void actionPerformed( ActionEvent e )
{
	getValueModel().setValue( new Boolean( getCheckBox().getState() ) );
}
public JCheckBoxMenuItem getCheckBox()
{
	return m_button;
}
protected Component getComponent()
{
	return getCheckBox();
}
/**
 */
public CoValueable getValueModel()
{
	return m_valueModel;
}
/**
 * This method was created by a SmartGuide.
 * @return javax.swing.ToggleButton
 */
public void setCheckBox(JCheckBoxMenuItem cb)
{
	m_button = cb;
	m_button.setEnabled( m_valueModel.isEnabled() );
	cb.addActionListener(this);
}
/**
 */
public void setValueModel( CoValueable vm )
{
	m_valueModel = vm;
	m_valueModel.addValueListener( this );
}
/**
 * valueModelChange method comment.
 */
public void updateCheckBox()
{
	Object v = getValueModel().getValue();
	Boolean b = (v != null) ? (Boolean) v : Boolean.FALSE;
	getCheckBox().setState( b.booleanValue() );
}
/**
  */
public void valueChange( CoValueChangeEvent e )
{
	Boolean b = (Boolean) e.getNewValue();
	getCheckBox().setState( ( b != null ) ? b.booleanValue() : false );
}
}
