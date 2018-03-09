package com.bluebrim.browser.client;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

import com.bluebrim.browser.shared.*;
import com.bluebrim.resource.shared.*;

/**
 * Subklass till DefaultTreeCellRenderer som används för att i en CoTree
 * presentera klasser som implementerar CoTreeCatalogElementIF.
 * @author Lasse Svadängs 97-10-10
 * @author Markus Persson 2002-06-12 (changed superclass)
 */
public class CoCatalogTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * Som default visas objekt som implementerar CoTreeCatalogElementIF
	 * upp med sin ikon och sin identitet. Jag särbehandlar inte
	 * element som är expanderade eller löv.
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean aBoolean) {
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, aBoolean);

		// Kontrollen nödvändig eftersom ett
		// första anrop görs innan min treeview
		// har fått sin modell.
		if (value instanceof CoCatalogElementIF) {
			CoCatalogElementIF tElement = (CoCatalogElementIF) value;
			setText(tElement.getIdentity());
			String tIconName = (tElement.getSmallIconName());
			setIcon(tIconName == null || tIconName.length() == 0 ? null : CoResourceLoader.loadIcon(tElement.getIconResourceAnchor(), tIconName));
		}
		return this;
	}
}
