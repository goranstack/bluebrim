package com.bluebrim.gui.client;

import java.awt.*;
import java.awt.event.*;

import com.bluebrim.swing.client.*;
/**
*/
public class CoTriStateCheckBoxAdaptor extends CoComponentAdaptor implements CoValueListener, ActionListener
{
	CoValueModel m_valueModel;
	CoTriStateCheckBox m_button;
/**
 * This method was created by a SmartGuide.
 */
public CoTriStateCheckBoxAdaptor()
{
}
/**
 * This method was created by a SmartGuide.
 * @param aValueModel SE.corren.calvin.userinterface.CoValueModel
 * @param aButton JToggleButton
 */
public CoTriStateCheckBoxAdaptor( CoValueModel aValueModel, CoTriStateCheckBox aButton )
{
	setValueModel(aValueModel);
	setButton(aButton);
	updateToggleButton();
}
/**
 * This method was created by a SmartGuide.
 * @param e java.awt.event.ActionEvent
 */
public void actionPerformed(ActionEvent e)
{
	getValueModel().setValue( getButton().getTriState() );
}
/**
 * This method was created by a SmartGuide.
 * @return javax.swing.ToggleButton
 */
public CoTriStateCheckBox getButton()
{
	return m_button;
}
protected Component getComponent()
{
	return getButton();
}
/**
 */
public CoValueModel getValueModel()
{
	return m_valueModel;
}
/**
 * This method was created in VisualAge.
 * @param args java.lang.String[]
 */
public static void main(String args[])
{
	javax.swing.JFrame f = new javax.swing.JFrame();

	java.awt.Container c = f.getContentPane();
	c.setLayout( new java.awt.FlowLayout() );

	CoTriStateCheckBox b1 = new CoTriStateCheckBox( "checkbox 1" );
	CoTriStateCheckBox b2 = new CoTriStateCheckBox( "checkbox 2" );

	c.add( b1 );
	c.add( b2 );

	b2.setEnabled( false );
	
	CoValueModel m = new CoValueHolder( Boolean.TRUE, "" );
	
	new CoTriStateCheckBoxAdaptor( m, b1 );
	new CoTriStateCheckBoxAdaptor( m, b2 );

	f.pack();
	f.show();
}
/**
 * This method was created by a SmartGuide.
 * @return javax.swing.ToggleButton
 */
public void setButton(CoTriStateCheckBox aButton)
{
	m_button = aButton;
	m_button.setEnabled( m_valueModel.isEnabled() );

	aButton.addActionListener(this);
}
/**
 */
public void setValueModel(CoValueModel aValueModel)
{
	m_valueModel = aValueModel;
	aValueModel.addValueListener(this);
}
private void updateToggleButton()
{
	updateToggleButton( (Boolean) getValueModel().getValue() );
}
private void updateToggleButton( Boolean value )
{
	getButton().setTriState( value );
}
public void valueChange(CoValueChangeEvent anEvent)
{
	updateToggleButton( (Boolean) anEvent.getNewValue() );
}
}
