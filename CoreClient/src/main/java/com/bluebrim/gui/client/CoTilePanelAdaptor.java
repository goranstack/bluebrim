package com.bluebrim.gui.client;
import java.awt.*;

/**
 * PENDING: Move to CalvinBase?
 *
 * @author Markus Persson
 */
public class CoTilePanelAdaptor extends CoComponentAdaptor implements CoValueListener {
	private com.bluebrim.gui.client.CoTileMapPanel m_tilePanel;
	private CoListValueable m_listValueable;

public CoTilePanelAdaptor(CoAbstractListAspectAdaptor valueable, com.bluebrim.gui.client.CoTileMapPanel tilePanel) {
	setListValueable(valueable);
	setTilePanel(tilePanel);
	valueable.addValueListener(this);
	valueable.setModelFor(tilePanel);
	tilePanel.getSelectionModel().addListSelectionListener(valueable);
	updateTilePanel();
}


protected Component getComponent() {
	return m_tilePanel;
}


public Object getElementAt(int index) {
	return getListValueable().getElementAt(index);
}


public CoListValueable getListValueable() {
	return m_listValueable;
}


public com.bluebrim.gui.client.CoTileMapPanel getTilePanel() {
	return m_tilePanel;
}


protected void setListValueable(CoListValueable valueable) {
	m_listValueable = valueable;
}


protected void setTilePanel(com.bluebrim.gui.client.CoTileMapPanel tilePanel) {
	m_tilePanel = tilePanel;
	m_tilePanel.setEnabled(m_listValueable.isEnabled());

}


public void updateTilePanel() {
	com.bluebrim.gui.client.CoTileMapPanel tilePanel = getTilePanel();
	try {
		tilePanel.getSelectedValue();
	} catch (ArrayIndexOutOfBoundsException e) {
		tilePanel.getSelectionModel().clearSelection();
	} catch (IndexOutOfBoundsException e) {
		tilePanel.getSelectionModel().clearSelection();
	}
	getListValueable().listHasChanged(this);
}


public void valueChange(CoValueChangeEvent event) {
	updateTilePanel();
}
}