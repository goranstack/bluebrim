package com.bluebrim.swing.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;

import com.bluebrim.menus.client.CoPopupMenu;

public class CoPopupMenuButton extends CoButton implements ActionListener{

	private CoPopupMenu m_menu;
public CoPopupMenuButton() {
	this(null, null);
}
public CoPopupMenuButton(String label) {
	this(label, null);
}
public CoPopupMenuButton(String label, Icon icon) {
	super(label, icon);
	addActionListener(this);
	setBorder(new CoDropShadowBorder());
}
public CoPopupMenuButton(Icon icon) {
	this(null, icon);
}
/**
 * Invoked when an (button) action occurs.
 */
public void actionPerformed(ActionEvent e) {
	if ((e.getSource() == this) && (m_menu != null))
		m_menu.show(this, 0, 0);
}
public final CoPopupMenu getMenu() {
	return m_menu;
}
public void setMenu(CoPopupMenu menu) {
	m_menu = menu;
}
private void showMenu(ActionEvent e) {
	m_menu.show(this, 0, 0);
}
}
