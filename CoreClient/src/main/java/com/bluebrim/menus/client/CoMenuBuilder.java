package com.bluebrim.menus.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.MissingResourceException;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import com.bluebrim.base.shared.CoNamed;
import com.bluebrim.gui.client.CoFrame;
import com.bluebrim.gui.client.CoUserInterface;
import com.bluebrim.resource.shared.CoOldResourceBundle;

/**
 	Ett CoUserInterface har en instans av CoMenuBuilder som ansvarar för att 
 	bygga dess menyer. En CoMenuBuilder har en instans av CoUIDefaults som håller 
 	fonter och färger för menyer och menyval.
 */
public class CoMenuBuilder {
	private CoMenuBar m_menuBar;
	protected transient CoUserInterface m_userInterface;
	public static char NO_SHORTCUT = '\n';

	private static class NamedMenuItem extends CoMenuItem {
		private CoNamed m_namedObject;
		public NamedMenuItem(CoNamed named, Action action) {
			super(named.getName(), (Icon) action.getValue(Action.SMALL_ICON));
			m_namedObject = named;
		}
		public String getText() {
			return m_namedObject != null ? m_namedObject.getName() : super.getText();
		}
	};
	private class MenuItemActionChangedListener implements PropertyChangeListener {
		JMenuItem menuItem;
		public MenuItemActionChangedListener(JMenuItem mi) {
			super();
			menuItem = mi;
		}
		public void propertyChange(PropertyChangeEvent e) {
			String propertyName = e.getPropertyName();
			if (propertyName.equals(Action.NAME)) {
				String text = (String) e.getNewValue();
				menuItem.setText(text);
			} else if (propertyName.equals("enabled")) {
				Boolean enabledState = (Boolean) e.getNewValue();
				menuItem.setEnabled(enabledState.booleanValue());
			} else if (propertyName.equals(Action.SMALL_ICON)) {
				Icon icon = (Icon) e.getNewValue();
				menuItem.setIcon(icon);
				menuItem.invalidate();
				menuItem.repaint();
			}
		}
	};

	private class CheckBoxMenuItemActionChangedListener extends MenuItemActionChangedListener {
		public CheckBoxMenuItemActionChangedListener(JCheckBoxMenuItem mi) {
			super(mi);
		}
		public void propertyChange(PropertyChangeEvent e) {
			String propertyName = e.getPropertyName();
			if (e.getPropertyName().equals("state")) {
				Boolean tState = (Boolean) e.getNewValue();
				((JCheckBoxMenuItem) menuItem).setState(tState.booleanValue());
			} else
				super.propertyChange(e);
		}
	}

	/**
	 * Lägger till en menyrad i menyn menu. Callback med objektet answer som argument.
	 */
	public CoCheckboxMenuItem addCheckBoxMenuItem(CoMenu menu, String label) {
		CoCheckboxMenuItem item = null;
		try {
			item = createCheckboxMenuItem(label);
			menu.add(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	/**
		Lägger till en menyrad i menyn menu. 
	 	'mnemonic' är ett tecken i menyvalets namn som tillsammans 
	 	med Alt kan användas som snabbkommando.
	 */
	public CoCheckboxMenuItem addCheckBoxMenuItem(CoMenu menu, String label, char mnemonic, ItemListener action) {
		CoCheckboxMenuItem item = null;
		try {
			item = addCheckBoxMenuItem(menu, label, action);
			if ((item != null) && (mnemonic != NO_SHORTCUT))
				item.setMnemonic(mnemonic);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	/**
		Lägger till en menyrad i menyn menu
		'keyCode' är ett snabbkommando som tillsammans med 'modifiers' möjliggör en
		aktivering av menyvalet utan att traversera ner i menyn.<br>
		Exempel på användning:
		<pre>
		menuBuilder.addCheckBoxMenuItem(menu, 'Visa allt', KeyEvent.VK_V, ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK, new ShowAllAction());
		</pre>
		Ovanstående kod ger ett menyval där Ctl+Alt+v kan användas för att aktivera menyvalet.
	 */
	public CoCheckboxMenuItem addCheckBoxMenuItem(CoMenu menu, String label, int keyCode, int modifiers, ItemListener action) {
		CoCheckboxMenuItem item = addCheckBoxMenuItem(menu, label, action);
		item.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
		return item;
	}
	/**
	 * Lägger till en menyrad i menyn menu. Callback med objektet answer som argument.
	 */
	public CoCheckboxMenuItem addCheckBoxMenuItem(CoMenu menu, String label, ItemListener action) {
		CoCheckboxMenuItem item = null;
		try {
			item = createCheckboxMenuItem(label, action);
			menu.add(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	/**
	 * Lägger till en menyrad i menyn menu. Callback med objektet answer som argument.
	 */
	public CoCheckboxMenuItem addCheckBoxMenuItem(CoMenu menu, String label, Action action) {
		CoCheckboxMenuItem item = null;
		try {
			item = createCheckboxMenuItem(label, action);
			menu.add(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	/**
	 * Lägger till en menyrad i menyn menu. Callback med objektet answer som argument.
	 */
	public CoCheckboxMenuItem addCheckBoxMenuItem(CoMenu menu, Action action) {
		CoCheckboxMenuItem item = null;
		try {
			item = createCheckboxMenuItem(action);
			menu.add(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	/**
	 * Lägger till en menyrad i menyn menu. Callback med objektet answer som argument.
	 */
	public CoCheckboxMenuItem addCheckBoxMenuItem(CoPopupMenu menu, String label) {
		CoCheckboxMenuItem item = null;
		try {
			item = createCheckboxMenuItem(label);
			menu.add(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	/**
	 * Lägger till en menyrad i menyn menu. Callback med objektet answer som argument.
	 */
	public CoCheckboxMenuItem addCheckBoxMenuItem(CoPopupMenu menu, String label, Action action) {
		CoCheckboxMenuItem item = null;
		try {
			item = createCheckboxMenuItem(label, action);
			menu.add(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	/**
	 * Lägger till en hierarkisk meny i menyn menu
	 */
	public CoSubMenu addSubMenu(CoMenu menu, String label) {

		CoSubMenu subMenu = newMenu(label);
		menu.add(subMenu);
		return subMenu;

	}
	/**
	 * Lägger till en hierarkisk meny i menyn menu
	 */
	public CoMenu addSubMenu(CoMenu menu, String label, char mnemonic) {

		CoSubMenu subMenu = addSubMenu(menu, label);
		subMenu.setMnemonic(mnemonic);
		return subMenu;

	}
	/**
	 * Lägger till en submeny i menyn menu
	 */
	public CoSubMenu addPopupSubMenu(CoPopupMenu menu, String label) {

		CoSubMenu subMenu = newMenu(label);
		menu.add(subMenu);
		return subMenu;

	}
	/**
	 * Lägger till en submeny i menyn menu
	 */
	public CoSubMenu addPopupSubMenu(CoPopupMenu menu, String label, Color bkgnd) {

		CoSubMenu subMenu = newMenu(label);
		subMenu.setBackground(bkgnd);
		menu.add(subMenu);
		return subMenu;

	}
	/**
	 * Lägger till en meny i menyraden
	 */
	public CoSubMenu addMenu(String label) {

		return addMenu(newMenu(label));

	}
	/**
	 * Lägger till en meny i menyraden.
	 * 'mnemonic' är ett tecken i menynamnet som tillsammans 
	 * med Alt kan användas som snabbkommando.
	 */
	public CoSubMenu addMenu(String label, char mnemonic) {

		CoSubMenu menu = addMenu(label);
		menu.setMnemonic(mnemonic);
		return menu;

	}
	public CoSubMenu addMenu(CoSubMenu menu) {
		m_menuBar.addSubMenu(menu);
		return menu;
	}

	public CoMenu addMenu(CoMenu menu) {
		m_menuBar.addSubMenu(menu);
		return menu;
	}
	
	
	
	/**
		Lägger till ett menyval till menyn 'menu'.
	 	'mnemonic' är ett tecken i menyvalets namn som tillsammans 
	 	med Alt kan användas som snabbkommando.
	 */
	public CoMenuItem addMenuItem(CoMenu menu, char mnemonic, Action action) {
		CoMenuItem mi = addMenuItem(menu, action);
		mi.setMnemonic(mnemonic);
		return mi;
	}
	/**
		'keyCode' är ett snabbkommando som tillsammans med 'modifiers' möjliggör en
		aktivering av menyvalet utan att traversera ner i menyn.<br>
		Exempel på användning:
		<pre>
		menuBuilder.addCheckBoxMenuItem(menu, KeyEvent.VK_V, ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK, new ShowAllAction("Visa allt"));
		</pre>
		Ovanstående kod ger ett menyval där Ctl+Alt+v kan användas för att aktivera menyvalet.
	 */
	public CoMenuItem addMenuItem(CoMenu menu, int keyCode, int modifiers, Action action) {
		CoMenuItem mi = addMenuItem(menu, action);
		mi.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
		return mi;
	}
	/**
	
	 */
	public CoMenuItem addMenuItem(CoMenu menu, String label) {
		CoMenuItem mi = createMenuItem(label);
		menu.add(mi);
		return mi;
	}
	/**
	 	Lägger till en menyrad i menyn menu. .
	 	'mnemonic' är ett tecken i menyvalets namn som tillsammans 
	 	med Alt kan användas som snabbkommando.
	 */
	public CoMenuItem addMenuItem(CoMenu menu, String label, char mnemonic, ActionListener action) {
		return addMenuItem(menu, label, null, mnemonic, action);
	}
	/**
		'keyCode' är ett snabbkommando som tillsammans med 'modifiers' möjliggör en
		aktivering av menyvalet utan att traversera ner i menyn.<br>
		Exempel på användning:
		<pre>
		menuBuilder.addCheckBoxMenuItem(menu, KeyEvent.VK_V, ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK, new ShowAllAction("Visa allt"));
		</pre>
		Ovanstående kod ger ett menyval där Ctl+Alt+v kan användas för att aktivera menyvalet.
	 */
	public CoMenuItem addMenuItem(CoMenu menu, String label, int keyCode, int modifiers, ActionListener action) {
		CoMenuItem mi = addMenuItem(menu, label, action);
		mi.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
		return mi;
	}
	/**
		'keyCode' är ett snabbkommando som tillsammans med 'modifiers' möjliggör en
		aktivering av menyvalet utan att traversera ner i menyn.<br>
		Exempel på användning:
		<pre>
		menuBuilder.addCheckBoxMenuItem(menu, KeyEvent.VK_V, ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK, new ShowAllAction("Visa allt"));
		</pre>
		Ovanstående kod ger ett menyval där Ctl+Alt+v kan användas för att aktivera menyvalet.
	 */
	public CoMenuItem addMenuItem(CoMenu menu, String label, int keyCode, int modifiers, Action action) {
		CoMenuItem mi = addMenuItem(menu, label, action);
		mi.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
		return mi;
	}
	/**
	
	 */
	public CoMenuItem addMenuItem(CoMenu menu, String label, ActionListener action) {
		return addMenuItem(menu, label, null, action);
	}
	/**
	
	 */
	public CoMenuItem addMenuItem(CoMenu menu, String label, ActionListener action, Color bkgnd) {
		CoMenuItem tItem = addMenuItem(menu, label, null, action);
		tItem.setBackground(bkgnd);
		return tItem;
	}
	/**
	
	 */
	public CoMenuItem addMenuItem(CoMenu menu, String label, Action action) {
		CoMenuItem mi = createMenuItem(label, (Icon) action.getValue(Action.SMALL_ICON), action);
		menu.add(mi);
		return mi;
	}
	/**
	 	Lägger till en menyrad i menyn menu. 
	 	'mnemonic' är ett tecken i menyvalets namn som tillsammans 
	 	med Alt kan användas som snabbkommando.
	 */
	public CoMenuItem addMenuItem(CoMenu menu, String label, Icon icon, char mnemonic, ActionListener action) {
		CoMenuItem item = null;
		try {
			item = addMenuItem(menu, label, icon, action);
			if ((item != null) && (mnemonic != NO_SHORTCUT))
				item.setMnemonic(mnemonic);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	/**
	
	 */
	public CoMenuItem addMenuItem(CoMenu menu, String label, Icon icon, ActionListener action) {
		CoMenuItem item = null;
		try {
			item = (icon != null) ? createMenuItem(label, icon, action) : createMenuItem(label, action);
			menu.add(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	/**
	 */
	public CoMenuItem addMenuItem(CoMenu menu, Action action) {
		CoMenuItem mi = createMenuItem((String) action.getValue(Action.NAME), (Icon) action.getValue(Action.SMALL_ICON), action);
		menu.add(mi);
		return mi;
	}
	/**
		Creates a menu item and adds it to <code>menu</code>. 
		This item uses lasy valuation of its label by using
		<code>namedObject</code>. In this way the item will always
		use the current name.
	 */
	public CoMenuItem addMenuItem(CoMenu menu, CoNamed namedObject, Action action) {
		CoMenuItem item = new NamedMenuItem(namedObject, action);
		item.setBorder(UIManager.getBorder("MenuItem.border"));
		item.setForeground(UIManager.getColor("MenuItem.foreground"));
		item.setBackground(UIManager.getColor("MenuItem.background"));
		item.setFont(UIManager.getFont("MenuItem.font"));
		registerMenuItemForAction(item, action);
		if (action != null)
			item.addActionListener(action);
		menu.add(item);
		return item;
	}
	/**
	 * Lägger till en menyrad i menyn menu
	 */
	public CoMenuItem addPopupMenuItem(CoPopupMenu menu, String label) {
		return addPopupMenuItem(menu, label, (Icon) null, null);
	}
	/**
	 * Lägger till en menyrad i menyn menu
	 */
	public CoMenuItem addPopupMenuItem(CoPopupMenu menu, String label, ActionListener action) {
		return addPopupMenuItem(menu, label, null, action);
	}
	/**
	 * Lägger till en menyrad i menyn menu
	 */
	public CoMenuItem addPopupMenuItem(CoPopupMenu menu, String label, ActionListener action, Color bkgnd) {
		CoMenuItem tItem = addPopupMenuItem(menu, label, null, action);
		tItem.setBackground(bkgnd);
		return tItem;
	}
	/**
	 * Lägger till en menyrad i menyn menu
	 */
	public CoMenuItem addPopupMenuItem(CoPopupMenu menu, String label, Icon anIcon, ActionListener action) {

		try {
			CoMenuItem item = this.createPopupMenuItem(label, anIcon, action);
			menu.add(item);
			return item;

		} catch (Exception e) {
			return null;
		}
	}
	/**
	 */
	public CoMenuItem addPopupMenuItem(CoPopupMenu menu, Action action) {
		CoMenuItem mi = createPopupMenuItem((String) action.getValue(Action.NAME), (Icon) action.getValue(Action.SMALL_ICON), action);
		menu.add(mi);
		return mi;
	}
	/**
	 * Lägger till en menyrad i menyn menu. Callback med objektet answer som argument.
	 */
	public JRadioButtonMenuItem addRadioButtonMenuItem(CoMenu menu, String label, Action action, ButtonGroup group) {
		JRadioButtonMenuItem item = null;
		try {
			item = createRadioButtonMenuItem(label, action);
			group.add(item);
			menu.add(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	/**
	 * Lägger till en linje i toMenu
	 */
	public void addSeparator(CoMenu toMenu) {

		toMenu.addSeparator();

	}
	/**
	 * Lägger till en linje i toMenu
	 */
	public void addSeparator(CoPopupMenu toPopupMenu) {

		toPopupMenu.addSeparator();

	}
	/**
	 */
	public CoTriStateCheckBoxMenuItem addTriStateCheckBoxMenuItem(CoMenu menu, String label, char mnemonic, ActionListener action) {
		CoTriStateCheckBoxMenuItem item = null;
		try {
			item = addTriStateCheckBoxMenuItem(menu, label, action);
			if ((item != null) && (mnemonic != NO_SHORTCUT))
				item.setMnemonic(mnemonic);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	/**
	 */
	public CoTriStateCheckBoxMenuItem addTriStateCheckBoxMenuItem(CoMenu menu, String label, int keyCode, int modifiers, ActionListener action) {
		CoTriStateCheckBoxMenuItem item = addTriStateCheckBoxMenuItem(menu, label, action);
		item.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
		return item;
	}
	/**
	 */
	public CoTriStateCheckBoxMenuItem addTriStateCheckBoxMenuItem(CoMenu menu, String label, ActionListener action) {
		CoTriStateCheckBoxMenuItem item = null;
		try {
			item = createTriStateCheckboxMenuItem(label, action);
			menu.add(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	private CoCheckboxMenuItem createCheckboxMenuItem(String label) {
		CoCheckboxMenuItem item = newCheckboxMenuItem(label);
		return item;
	}
	/** Create a checkbox menu item and add its listener.
	The method will be sent to the receiver when the item state changes.
	*/
	private CoCheckboxMenuItem createCheckboxMenuItem(String label, ItemListener action) {
		CoCheckboxMenuItem item = newCheckboxMenuItem(label);
		if (action != null)
			item.addItemListener(action);
		return item;
	}
	private CoCheckboxMenuItem createCheckboxMenuItem(String label, Action action) {
		CoCheckboxMenuItem item = newCheckboxMenuItem(label);
		registerCheckBoxMenuItemForAction(item, action);
		if (action != null)
			item.addActionListener(action);
		return item;
	}
	/** Create a checkbox menu item and add its listener.
	The method will be sent to the receiver when the item state changes.
	*/
	private CoCheckboxMenuItem createCheckboxMenuItem(Action action) {
		return createCheckboxMenuItem((String) action.getValue(Action.NAME), action);
	}
	/**
	 * Svara med en fontmeny.
	 */
	public CoMenu createFontMenu(String label, String methodName, Object caller) {

		try {
			Class receiverClass = caller.getClass();
			Class noneType[] = {
			};

			Method method = receiverClass.getMethod(methodName, noneType);

			return this.doCreateFontMenu(label, 100, caller, method);

		} catch (NoSuchMethodException e) {
		}

		return null;

	}
	/**
	 * This method was created by a SmartGuide.
	 */
	public CoMenuBar createMenuBar() {

		this.m_menuBar = new CoMenuBar();
		m_menuBar.setBorder(UIManager.getBorder("MenuBar.border"));
		m_menuBar.setForeground(UIManager.getColor("MenuBar.foreground"));
		m_menuBar.setBackground(UIManager.getColor("MenuBar.background"));
		m_menuBar.setFont(UIManager.getFont("MenuBar.font"));
		return m_menuBar;
	}
	private CoMenuItem createMenuItem(String label) {
		CoMenuItem item = newMenuItem(label);
		return item;
	}
	/** Create a menu item and add its listener.
	The method argument will be sent to the receiver when 
	the item is selected.
	*/
	private CoMenuItem createMenuItem(String label, ActionListener action) {
		return createMenuItem(label, null, action);
	}
	/** Create a menu item and add its listener.
	The method argument will be sent to the receiver when 
	the item is selected.
	*/
	private CoMenuItem createMenuItem(String label, Icon icon, ActionListener action) {
		CoMenuItem item = newMenuItem(label, icon);
		if (action != null)
			item.addActionListener(action);
		return item;
	}
	/** Create a menu item and add its listener.
	The method argument will be sent to the receiver when 
	the item is selected.
	*/
	private CoMenuItem createMenuItem(String label, Icon icon, Action action) {
		CoMenuItem item = newMenuItem(label, icon);
		registerMenuItemForAction(item, action);
		if (action != null)
			item.addActionListener(action);
		return item;
	}
	public CoPopupMenu createPopupMenu() {
		CoPopupMenu m = newPopupMenu();
		return m;
	}
	/**
	 * This method was created by a SmartGuide.
	 */
	public CoPopupMenu createPopupMenu(JComponent invoker) {
		CoPopupMenu tMenu = newPopupMenu(invoker);
		tMenu.addPopupMenuListener(new CoClosePopupSubmenu());
		return tMenu;
	}
	/** 
	
	*/
	private CoMenuItem createPopupMenuItem(String label, Icon anIcon, ActionListener action) {
		CoMenuItem item = newPopupMenuItem(label, anIcon);
		if (action != null)
			item.addActionListener(action);
		return item;
	}
	/** 
	
	*/
	private CoMenuItem createPopupMenuItem(String label, Icon anIcon, Action action) {
		CoMenuItem item = newPopupMenuItem(label, anIcon);
		registerMenuItemForAction(item, action);
		if (action != null)
			item.addActionListener(action);
		return item;
	}
	private JRadioButtonMenuItem createRadioButtonMenuItem(String label, Action action) {
		JRadioButtonMenuItem item = newRadioButtonMenuItem(label);
		registerMenuItemForAction(item, action);
		if (action != null)
			item.addActionListener(action);
		return item;
	}
	/** Create a checkbox menu item and add its listener.
	The method will be sent to the receiver when the item state changes.
	*/
	private CoTriStateCheckBoxMenuItem createTriStateCheckboxMenuItem(String label, ActionListener action) {
		CoTriStateCheckBoxMenuItem item = newTriStateCheckboxMenuItem(label);
		if (action != null)
			item.addActionListener(action);
		return item;
	}
	public void deleteAllMenus() {

		m_menuBar.removeAllMenus();
	}
	/** Create a font menu. The argument method is sent to
	the receiver when	a item in this menu is selected. 
	
	The method should	have three parameters:
		String name -> name of the font selected;
		int style ->  style of the font selected;
		int size -> size of the font selected.
	*/
	private CoMenu doCreateFontMenu(String label, int maxEntries, Object receiver, Method method) {
		String fontNames[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		CoSubMenu menu = newMenu(label);

		int styles[] = this.fontStyles();
		int styleIndex = 0;

		for (int index = 0; index < fontNames.length; index++) {
			for (int size = 12; size < 20; size += 2) {

				String itemLabel = fontNames[index];
				int style = styles[styleIndex];
				styleIndex = (styleIndex + 1) % styles.length;

				if (style == Font.PLAIN) {
					itemLabel += "-PLAIN-";
				} else if (style == Font.BOLD) {
					itemLabel += "-BOLD-";
				} else if (style == Font.BOLD + Font.ITALIC) {
					itemLabel += "-BOLDITALIC-";
				} else if (style == Font.ITALIC) {
					itemLabel += "-ITALIC-";
				}

				itemLabel += Integer.toString(size);

				Object args[] = { fontNames[index], new Integer(style), new Integer(size)};

				menu.add(this.createMenuItem(itemLabel, new ActionMessage(receiver, method, args)));

				if (menu.getItemCount() == maxEntries)
					return menu;
			}
		}
		return menu;
	}
	/** Answer an array with fonts styles.
	*/
	private int[] fontStyles() {
		int styles[] = new int[4];
		styles[0] = Font.PLAIN;
		styles[1] = Font.ITALIC;
		styles[2] = Font.BOLD + Font.ITALIC;
		styles[3] = Font.BOLD;

		return styles;
	}
	/**
	 */
	public CoMenuBar getMenuBar() {
		return m_menuBar;
	}
	private static String getName(CoOldResourceBundle resources, String key) {
		try {
			return resources.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}
	private static Object getObject(CoOldResourceBundle resources, String key) {
		try {
			return resources.getObject(key);
		} catch (MissingResourceException e) {
			return null;
		}
	}
	private static String getString(CoOldResourceBundle resources, String key) {
		try {
			return resources.getString(key);
		} catch (MissingResourceException e) {
			return null;
		}
	}
	private static String[] getStringArray(CoOldResourceBundle resources, String key) {
		try {
			return resources.getStringArray(key);
		} catch (MissingResourceException e) {
			return null;
		}
	}
	public CoUserInterface getUserInterface() {
		return m_userInterface;
	}
	/**
	 * Skjuter in en menyrad i 'menu'
	 */
	public CoMenuItem insertPopupMenuItem(CoPopupMenu menu, int position, String label, Icon anIcon, ActionListener action) {

		try {
			CoMenuItem item = this.createPopupMenuItem(label, anIcon, action);
			menu.insert(item, position);
			return item;

		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * This method was created by a SmartGuide.
	 */
	public void installMenuBarIn(CoFrame window) {
		window.setJMenuBar(createMenuBar());
	}
	/** 
	*/
	private CoCheckboxMenuItem newCheckboxMenuItem(String label) {
		CoCheckboxMenuItem item = new CoCheckboxMenuItem(label);
		item.setBorder(UIManager.getBorder("MenuItem.border"));
		item.setForeground(UIManager.getColor("MenuItem.foreground"));
		item.setBackground(UIManager.getColor("MenuItem.background"));
		item.setFont(UIManager.getFont("MenuItem.font"));
		return item;
	}
	/**
	 * Made public by Markus Persson 1999-08-31.
	 * @see CoDaybookListUI
	 */
	public CoSubMenu newMenu(String label) {
		CoSubMenu menu = new CoSubMenu(label);
		prepareMenu(menu);
		return menu;
	}
	/**
	*/
	private CoMenuItem newMenuItem(String label) {
		return newMenuItem(label, null);
	}
	/**
	*/
	private CoMenuItem newMenuItem(String label, Icon icon) {
		CoMenuItem item = new CoMenuItem(label, icon);
		prepareMenuItem(item);
		return item;
	}
	/**
	*/
	private CoPopupMenu newPopupMenu() {
		CoPopupMenu menu = new CoPopupMenu();
		preparePopupMenu(menu);
		return menu;
	}
	/**
	*/
	private CoPopupMenu newPopupMenu(JComponent invoker) {
		CoPopupMenu menu = new CoPopupMenu(invoker);
		preparePopupMenu(menu);
		return menu;
	}
	/**
	*/
	private CoMenuItem newPopupMenuItem(String label, Icon icon) {
		CoMenuItem item = new CoMenuItem(label, icon);
		item.setBorder(UIManager.getBorder("MenuItem.border"));
		item.setForeground(UIManager.getColor("MenuItem.foreground"));
		item.setBackground(UIManager.getColor("MenuItem.background"));
		item.setFont(UIManager.getFont("MenuItem.font"));
		return item;
	}
	/** 
	*/
	private JRadioButtonMenuItem newRadioButtonMenuItem(String label) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(label);
		item.setBorder(UIManager.getBorder("MenuItem.border"));
		item.setForeground(UIManager.getColor("MenuItem.foreground"));
		item.setBackground(UIManager.getColor("MenuItem.background"));
		item.setFont(UIManager.getFont("MenuItem.font"));
		return item;
	}
	/** 
	*/
	private CoTriStateCheckBoxMenuItem newTriStateCheckboxMenuItem(String label) {
		CoTriStateCheckBoxMenuItem item = new CoTriStateCheckBoxMenuItem(label);
		item.setBorder(UIManager.getBorder("MenuItem.border"));
		item.setForeground(UIManager.getColor("MenuItem.foreground"));
		item.setBackground(UIManager.getColor("MenuItem.background"));
		item.setFont(UIManager.getFont("MenuItem.font"));
		return item;
	}
	public void prepareMenu(JMenu menu) {
	}
	public void prepareMenuItem(JMenuItem item) {
	}
	/**
	*/
	public void preparePopupMenu(CoPopupMenu menu) {
		menu.setLightWeightPopupEnabled((m_userInterface == null) ? true : m_userInterface.isLightWeightPopupEnabled());
	}
	/**
	 */
	private void registerCheckBoxMenuItemForAction(JCheckBoxMenuItem item, Action action) {
		action.addPropertyChangeListener(new CheckBoxMenuItemActionChangedListener(item));
		item.setEnabled(action.isEnabled());
	}
	/**
	 */
	private void registerMenuItemForAction(JMenuItem item, Action action) {
		action.addPropertyChangeListener(new MenuItemActionChangedListener(item));
		item.setEnabled(action.isEnabled());
	}
	private void setUserInterface(CoUserInterface userInterface) {
		m_userInterface = userInterface;
	}
	public CoMenuBuilder(CoUserInterface userInterface) {
		setUserInterface(userInterface);
	}

}