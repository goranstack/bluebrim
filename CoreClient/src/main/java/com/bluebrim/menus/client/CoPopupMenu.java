package com.bluebrim.menus.client;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;

import com.bluebrim.resource.shared.CoMenuItemResource;
/**
	Subclass to <code>JPopupMenu</code> that makes sure that
	the whole menu will be onscreen when showing. 
	Unfortunately this only works when the popup menu is the
	root menu, i.e not when it's displayed as a submenu.
	This will hopefully be fixed in a future version of Swing.

	990318, Dennis : lagt till tangentbordsnavigering (up, down, space, enter )
 */
public class CoPopupMenu extends JPopupMenu implements CoMenu {

	public CoPopupMenu() {
		super();
		enableEvents(AWTEvent.KEY_EVENT_MASK);
	}

	public CoPopupMenu(JComponent invoker) {
		this();
		setInvoker(invoker);
	}

	public CoMenuItem addAction(Action action) {
		CoMenuItem mi = new CoMenuItem(action);
		add(mi);
		return mi;
	}

	private Point calculateLocation(Rectangle positions) {
		int x = 0;
		int y = 0;

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension pmSize = getSize();
		// For the first time the menu is popped up, 
		// the size has not yet been initiated
		if (pmSize.width == 0) {
			pmSize = getPreferredSize();
		}

		if (positions.x + pmSize.width < screenSize.width) {
			x = positions.x;
		} else {
			x = Math.min(screenSize.width, (positions.x + positions.width)) - pmSize.width; // Otherwise extend to left
		}

		if (positions.y + pmSize.height < screenSize.height) {
			y = positions.y; // Prefer dropping down
		} else {
			y = Math.max(0, (positions.y + positions.height) - pmSize.height); // Otherwise drop 'up'
		}
		return new Point(x, y);
	}

	public JMenuItem getItem(int index) {
		return (JMenuItem) getComponent(index);
	}

	/**
	 * @return com.bluebrim.menus.client.CoPopupMenuItem
	 * @param index int
	 */
	public CoMenuItem getItemWithLabel(String label) {
		int ncomponents = getComponentCount();
		for (int i = 0; i < ncomponents; i++) {
			Component comp = getComponent(i);
			if (comp != null)
				if (comp instanceof CoMenuItem && label.equals(((CoMenuItem) comp).getText()))
					return (CoMenuItem) comp;
		}

		return null;

	}
	private MenuElement nextEnabledChild(MenuElement e[], int fromIndex) {
		int i, c;
		for (i = fromIndex, c = e.length; i < c; i++) {
			if (e[i] != null) {
				Component comp = e[i].getComponent();
				if (comp != null && comp.isEnabled())
					return e[i];
			}
		}
		return null;
	}
	private MenuElement previousEnabledChild(MenuElement e[], int fromIndex) {
		int i;
		for (i = fromIndex; i >= 0; i--) {
			if (e[i] != null) {
				Component comp = e[i].getComponent();
				if (comp != null && comp.isEnabled())
					return e[i];
			}
		}
		return null;
	}

	private void processKeyDownEvent(KeyEvent e) {
		MenuElement currentSelection[] = MenuSelectionManager.defaultManager().getSelectedPath();
		if (currentSelection.length > 1) {
			MenuElement parent = currentSelection[currentSelection.length - 2];
			if (parent.getComponent() instanceof JMenu) {
				parent = currentSelection[currentSelection.length - 1];
				MenuElement children[] = parent.getSubElements();
				if (children.length > 0) {
					MenuElement newPath[] = new MenuElement[currentSelection.length + 1];
					System.arraycopy(currentSelection, 0, newPath, 0, currentSelection.length);
					newPath[currentSelection.length] = nextEnabledChild(children, 0);
					if (newPath[currentSelection.length] != null)
						MenuSelectionManager.defaultManager().setSelectedPath(newPath);
				}
			} else {
				MenuElement children[] = parent.getSubElements();
				for (int i = 0, I = children.length; i < I; i++) {
					if (children[i] == currentSelection[currentSelection.length - 1]) {
						MenuElement nextChild = nextEnabledChild(children, i + 1);
						if (nextChild == null)
							nextChild = nextEnabledChild(children, 0);
						if (nextChild != null) {
							currentSelection[currentSelection.length - 1] = nextChild;
							MenuSelectionManager.defaultManager().setSelectedPath(currentSelection);
						}
						break;
					}
				}
			}
		}
	}

	private void processKeyEnterEvent(KeyEvent e) {
		MenuElement path[] = MenuSelectionManager.defaultManager().getSelectedPath();
		MenuElement lastElement;
		if (path.length > 0) {
			lastElement = path[path.length - 1];
			if (lastElement instanceof JMenu) {
				MenuElement newPath[] = new MenuElement[path.length + 1];
				System.arraycopy(path, 0, newPath, 0, path.length);
				newPath[path.length] = ((JMenu) lastElement).getPopupMenu();
				MenuSelectionManager.defaultManager().setSelectedPath(newPath);
			} else
				if (lastElement instanceof JMenuItem) {
					MenuSelectionManager.defaultManager().clearSelectedPath();
					((JMenuItem) lastElement).doClick(0);
					((JMenuItem) lastElement).setArmed(false);
				}
		}
	}

	protected void processKeyEvent(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_DOWN :
				{
					if ((e.getID() == KeyEvent.KEY_PRESSED) && (e.getModifiers() == 0)) {
						processKeyDownEvent(e);
						e.consume();
					}
					break;
				}

			case KeyEvent.VK_UP :
				{
					if ((e.getID() == KeyEvent.KEY_PRESSED) && (e.getModifiers() == 0)) {
						processKeyUpEvent(e);
						e.consume();
					}
					break;
				}

			case KeyEvent.VK_ENTER :
			case KeyEvent.VK_SPACE :
				{
					if ((e.getID() == KeyEvent.KEY_RELEASED) && (e.getModifiers() == 0)) {
						processKeyEnterEvent(e);
						e.consume();
					}
					break;
				}

			default :
				{
				}
		}

		super.processKeyEvent(e);
	}

	private void processKeyUpEvent(KeyEvent e) {
		MenuElement currentSelection[] = MenuSelectionManager.defaultManager().getSelectedPath();
		if (currentSelection.length > 1) {
			MenuElement parent = currentSelection[currentSelection.length - 2];
			if (parent.getComponent() instanceof JMenu) {
				parent = currentSelection[currentSelection.length - 1];
				MenuElement children[] = parent.getSubElements();
				if (children.length > 0) {
					MenuElement newPath[] = new MenuElement[currentSelection.length + 1];
					System.arraycopy(currentSelection, 0, newPath, 0, currentSelection.length);
					newPath[currentSelection.length] = previousEnabledChild(children, children.length - 1);
					if (newPath[currentSelection.length] != null)
						MenuSelectionManager.defaultManager().setSelectedPath(newPath);
				}
			} else {
				MenuElement children[] = parent.getSubElements();
				for (int i = 0, I = children.length; i < I; i++) {
					if (children[i] == currentSelection[currentSelection.length - 1]) {
						MenuElement nextChild = previousEnabledChild(children, i - 1);
						if (nextChild == null)
							nextChild = previousEnabledChild(children, children.length - 1);
						if (nextChild != null) {
							currentSelection[currentSelection.length - 1] = nextChild;
							MenuSelectionManager.defaultManager().setSelectedPath(currentSelection);
						}
						break;
					}
				}
			}
		}
	}

	private Point recalculateLocation(int left, int top) {
		int x = 0;
		int y = 0;

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension pmSize = getSize();
		// For the first time the menu is popped up, 
		// the size has not yet been initiated
		if (pmSize.width == 0) {
			pmSize = getPreferredSize();
		}
		Point position = new Point(left, top);
		if (position.x + pmSize.width < screenSize.width) {
			x = position.x;
		} else {
			x = Math.min(screenSize.width, position.x) - pmSize.width; // Otherwise extend to left
		}

		if (position.y + pmSize.height < screenSize.height) {
			y = position.y; // Prefer dropping down
		} else {
			y = Math.max(0, position.y - pmSize.height); // Otherwise drop 'up'
		}
		return new Point(x, y);
	}


	public void setVisible(boolean v) {
		super.setVisible(v);

		if (v) {
			requestFocus();
		} else {
			getInvoker().requestFocus();
		}
	}

	/**
		Overrides the original method in <code>JPopupMenu</code> to make sure that the
		whole menu is onscreen, i.e it takes the screen size into consideration and calculates
		another location if the menu will be partly outside the screen using the original one. 
	*/
	public void show(Component invoker, int x, int y) {
		Point tInvokerOrigin = invoker.getLocationOnScreen();
		Point tNewScreenLocation = recalculateLocation(tInvokerOrigin.x + x, tInvokerOrigin.y + y);
		super.show(invoker, tNewScreenLocation.x - tInvokerOrigin.x, tNewScreenLocation.y - tInvokerOrigin.y);
	}

	/**
		Shows the menu in the best possible location in <code>invoker</code>, the component
		underlying the menu. This method is to be used when the menu is of the
		"popup menu button" kind, i.e the result of a mouse down event in a button. 
		<br>
		Normally such a menu has its origin in the upper left corner of its component. 
		This method takes the screen size into consideration and chooses one of the other corners
		of the component if the menu will not fit onscreen.
	*/
	public void showIn(Component invoker) {
		Point tInvokerOrigin = invoker.getLocationOnScreen();
		Point tScreenLocation = calculateLocation(new Rectangle(tInvokerOrigin, invoker.getSize()));
		super.show(invoker, tScreenLocation.x - tInvokerOrigin.x, tScreenLocation.y - tInvokerOrigin.y);
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


}
