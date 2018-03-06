package com.bluebrim.layout.impl.client;

import java.awt.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;

/**
 * Component adaptor for CoViewPanel.
 *
 * @author: Dennis
 */

public class CoViewPanelAdaptor extends CoComponentAdaptor implements CoValueListener
{
	private CoValueModel m_valueModel;
	private CoAbstractViewPanel m_viewPanel;
	
public CoViewPanelAdaptor() {
}
public CoViewPanelAdaptor(CoValueModel valueModel, CoAbstractViewPanel viewPanel) {
	setValueModel(valueModel);
	setViewPanel(viewPanel);
	updateViewPanel();
}
protected Component getComponent( ) {
	return getViewPanel();
}
public CoValueModel getValueModel() {
	return m_valueModel;
}
public CoAbstractViewPanel getViewPanel() {
	return m_viewPanel;
}
public final void setValueModel(CoValueModel valueModel) {
	m_valueModel = valueModel;
	valueModel.addValueListener(this);
}
public final void setViewPanel(CoAbstractViewPanel viewPanel) {
	m_viewPanel = viewPanel;
	m_viewPanel.setEnabled( m_valueModel.isEnabled() );

}
public void updateViewPanel() {
	getViewPanel().setView((CoView) getValueModel().getValue());
}
public void valueChange(CoValueChangeEvent event) {
	CoView view = (CoView)event.getNewValue();
	getViewPanel().setView(view);
}
}
