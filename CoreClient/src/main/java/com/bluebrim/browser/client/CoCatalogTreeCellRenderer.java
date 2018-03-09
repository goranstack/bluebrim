package com.bluebrim.browser.client;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

import com.bluebrim.browser.shared.*;
import com.bluebrim.resource.shared.*;

/**
 * Subklass till DefaultTreeCellRenderer som anv�nds f�r att i en CoTree
 * presentera klasser som implementerar CoTreeCatalogElementIF.
 * @author Lasse Svad�ngs 97-10-10
 * @author Markus Persson 2002-06-12 (changed superclass)
 */
public class CoCatalogTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * Som default visas objekt som implementerar CoTreeCatalogElementIF
	 * upp med sin ikon och sin identitet. Jag s�rbehandlar inte
	 * element som �r expanderade eller l�v.
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean aBoolean) {
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, aBoolean);

		// Kontrollen n�dv�ndig eftersom ett
		// f�rsta anrop g�rs innan min treeview
		// har f�tt sin modell.
		if (value instanceof CoCatalogElementIF) {
			CoCatalogElementIF tElement = (CoCatalogElementIF) value;
			setText(tElement.getIdentity());
			String tIconName = (tElement.getSmallIconName());
			setIcon(tIconName == null || tIconName.length() == 0 ? null : CoResourceLoader.loadIcon(tElement.getIconResourceAnchor(), tIconName));
		}
		return this;
	}
}
