package com.bluebrim.menus.client;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.bluebrim.resource.shared.CoMenuItemResource;

/**
 */
public class CoMenuBar extends JMenuBar implements CoMenu {

	public CoMenuBar() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS) {
			public Dimension minimumLayoutSize(Container target) {
				return preferredLayoutSize(target);
			}
		});
		setAlignmentX(LEFT_ALIGNMENT);
	}

	public void removeAllMenus() {

		int menuCount = getMenuCount();
		int menuCount2 = menuCount - 1;
		for (int i = menuCount2; i > -1; i--) {
			if (getComponent(i) instanceof JMenu)
				remove(i);
		}
	}

	public void setEnabled(boolean enabled) {
		int I = getMenuCount();
		for (int i = 0; i < I; i++) {
			((JMenu) getMenu(i)).setEnabled(enabled);
		}

		super.setEnabled(enabled);
	}

	public JMenuItem add(Action action) {
		return add(new CoMenuItem(action));
	}

	public CoMenuItem add(CoMenuItemResource item) {
		throw new UnsupportedOperationException("Not possible to add CoMenuItem to a CoMenuBar");
	}

	public JMenuItem add(JMenuItem item) {
		return super.add((JMenu) item);
	}

	public void addSeparator() {
		add(new JSeparator());
	}

	public void addSubMenu(CoMenu menu) {
		super.add((JMenu) menu);
	}

	public CoSubMenu addSubMenu(CoMenuItemResource menu) {
		CoSubMenu subMenu = new CoSubMenu(menu);
		super.add(subMenu);
		return subMenu;
	}


	public JMenuItem getItem(int index) {
		return (JMenuItem) getComponent(index);
	}


	public void insertBefore(CoMenuItemResource menu, JMenu insertMenu) {
		int insertPos = indexOf(menu);
		if (insertPos < 0)
            throw new IllegalArgumentException("The specified menu does not exist");
		super.add(insertMenu, insertPos);
	}


	public CoSubMenu insertBefore(CoMenuItemResource menu, CoMenuItemResource resource) {
		CoSubMenu subMenu = new CoSubMenu(resource);
		insertBefore(menu, subMenu);
		return subMenu;
	}
	
	private int indexOf (CoMenuItemResource menu) {
		for (int i = 0; i < getComponentCount(); i++) {
			Component component = getComponent(i);
			if (component instanceof CoSubMenu)
				if (((CoSubMenu)component).getResource() == menu)
					return i;
		}
		return -1;
	}

}
