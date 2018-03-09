package com.bluebrim.menus.client;

import java.awt.Component;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.bluebrim.resource.shared.CoMenuItemResource;

/**
 */
public class CoSubMenu extends JMenu implements CoMenu {
	
	private CoMenuItemResource m_resource;

	public CoSubMenu() {
		super();
	}

	public CoSubMenu(String name) {
		super(name);
	}

	public CoSubMenu(String name, boolean tearable) {
		super(name, tearable);
	}

	public CoSubMenu(CoMenuItemResource item) {
		super(item.text());
		setMnemonic(item.mnemonic());
		m_resource = item;
	}

	public CoMenuItem addAction(Action action) {
		CoMenuItem mi = new CoMenuItem(action);
		add(mi);
		return mi;
	}

	public int getItemCount() {
		return getMenuComponentCount();
	}
	
	public CoMenuItemResource getResource() {
		return m_resource;
	}

	public void remove(int pos) {
		Component tComponent = getMenuComponent(pos);
		if (tComponent instanceof CoMenuItem)
			 ((CoMenuItem) tComponent).setAction(null);
		super.remove(pos);
	}

	public void remove(Action a) {
		Component tMenuComponents[] = getMenuComponents();
		for (int i = tMenuComponents.length - 1; i >= 0; i--) {
			try {
				CoMenuItem tItem = (CoMenuItem) tMenuComponents[i];
				if (a == tItem.getAction()) {
					remove(tItem);
					break;
				}
			} catch (Exception e) {
			}
		}
	}

	public void remove(JMenuItem item) {
		try {
			((CoMenuItem) item).setAction(null);
		} catch (Exception e) {
		}
		super.remove(item);
	}

	/**
		BUG FIX:
		JMenu.removeAll assumes that menu is not empty. 
	*/
	public void removeAll() {
		if (getMenuComponentCount() > 0)
			super.removeAll();
	}

	public CoMenuItem add(CoMenuItemResource item) {
		CoMenuItem menuItem = new CoMenuItem(item);
		super.add(menuItem);
		return menuItem;
	}

	public void addSubMenu(CoMenu menu) {
		super.add((JMenu) menu);
	}

	public CoSubMenu addSubMenu(CoMenuItemResource menu) {
		CoSubMenu subMenu = new CoSubMenu(menu);
		super.add(subMenu);
		return subMenu;
	}
	
	public void insertSeparatorBefore(CoMenuItemResource item) {
		int insertPos = indexOf(item);
		if (insertPos < 0)
            throw new IllegalArgumentException("The specified item does not exist");
		add(new JSeparator(), insertPos);
	}

	public void insertBefore(CoMenuItemResource item, JMenuItem insertItem) {
		int insertPos = indexOf(item);
		if (insertPos < 0)
            throw new IllegalArgumentException("The specified item does not exist");
		insert(insertItem, insertPos);
	}


	public CoMenuItem insertBefore(CoMenuItemResource menu, CoMenuItemResource resource) {
		CoMenuItem item = new CoMenuItem(resource);
		insertBefore(menu, item);
		return item;
	}
	
	private int indexOf (CoMenuItemResource menu) {
		for (int i = 0; i < getMenuComponentCount(); i++) {
			Component component = getMenuComponent(i);
			if (component instanceof CoMenuItem)
				if (((CoMenuItem)component).getResource() == menu)
					return i;
		}
		return -1;
	}
	

}