package com.bluebrim.browser.client;

import javax.swing.ListCellRenderer;

import com.bluebrim.browser.shared.CoCatalogElementIF;
import com.bluebrim.gui.client.CoListCellRenderer;
import com.bluebrim.resource.shared.CoResourceLoader;
/**
 * Subklass till JLabel som används för att i en JList
 * presentera klasser som implementerar CoCatalogElementIF.
 * @author Lasse Svadängs 97-10-10
 * 
 */
public class CoCatalogListCellRenderer extends CoListCellRenderer implements ListCellRenderer {
public CoCatalogListCellRenderer() {
	super();
}
public CoCatalogListCellRenderer(int hTextPosition, int vTextPosition) {
	this();
	setHorizontalTextPosition(hTextPosition);
	setVerticalTextPosition(vTextPosition);
}
public CoCatalogListCellRenderer(int hTextPosition, int vTextPosition, int hAlignment) {
	this(hTextPosition, vTextPosition);
	setHorizontalAlignment(hAlignment);
}
/**
 * Som default visas objekt som implementerar CoCatalogElementIF
 * upp med sin ikon och sin identitet.
 */
protected void setValue(Object value, int index, boolean isSelected, boolean cellHasFocus) {
	if (value != null) {
		CoCatalogElementIF element = (CoCatalogElementIF) value;
		setText(element.getIdentity());
		String iconName = (element.getSmallIconName());
		String anchor = element.getIconResourceAnchor();
		if (anchor != null) {
			setIcon(CoResourceLoader.loadIcon(anchor, iconName));
		}
	} else {
		setText("");
		setIcon(null);
	}
}
}
