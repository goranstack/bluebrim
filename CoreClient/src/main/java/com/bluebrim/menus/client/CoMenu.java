package com.bluebrim.menus.client;

import java.awt.Component;

import javax.swing.Action;
import javax.swing.JMenuItem;

import com.bluebrim.resource.shared.CoMenuItemResource;

/**
 * @author Markus Persson 2002-10-31
 */
public interface CoMenu {
	public static final Object SEPARATOR = "Separator";
	
	public CoMenuItem add(CoMenuItemResource item);
	public JMenuItem add(Action action);
	public JMenuItem add(JMenuItem item);
	public Component add(Component component);
	public void addSubMenu(CoMenu menu);
	public CoSubMenu addSubMenu(CoMenuItemResource menu);
    public void addSeparator();
	public JMenuItem getItem(int index);
	public void remove(int index);
	public void removeAll();
	public void setEnabled(boolean enabled);
}
