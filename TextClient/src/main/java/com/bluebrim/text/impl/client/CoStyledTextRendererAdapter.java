package com.bluebrim.text.impl.client;

import java.awt.*;

import javax.swing.text.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.text.impl.client.swing.*;

/**
 */
public class CoStyledTextRendererAdapter extends CoComponentAdaptor implements CoValueListener
{
	private CoStyledTextRendererComponent 	m_rendererComponent;
	private CoValueable   					m_valueModel;

/**
 * Default-konstruktor.
 */
public CoStyledTextRendererAdapter()
{
}
public CoStyledTextRendererAdapter( CoValueable aValueModel, CoStyledTextRendererComponent rendererComponent )
{
	this();
	setValueModel( aValueModel );
	setRendererComponent( rendererComponent );
	updateRendererComponent();
}
protected Component getComponent()
{
	return getRendererComponent();
}
public CoStyledTextRendererComponent getRendererComponent()
{
	return m_rendererComponent;
}
/**
 * Access-metod till värdemodellen.
 * @return värdemodellen
 */
public CoValueable getValueModel()
{
	return m_valueModel;
}
protected void setRendererComponent( CoStyledTextRendererComponent c )
{
	m_rendererComponent = c;
	m_rendererComponent.setEnabled( m_valueModel.isEnabled() );

}
protected void setValueModel( CoValueable valueModel )
{
	if (m_valueModel != valueModel)
	{
		if (m_valueModel != null)
			m_valueModel.removeValueListener(this);
		m_valueModel = valueModel;
		if (m_valueModel != null)
			m_valueModel.addValueListener(this);
	}
}
private void updateRendererComponent() {
	m_rendererComponent.setDocument((Document )getValueModel().getValue());
	m_rendererComponent.repaint();
}
public void valueChange(CoValueChangeEvent anEvent) {
	updateRendererComponent();
}
}
