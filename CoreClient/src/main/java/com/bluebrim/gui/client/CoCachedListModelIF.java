package com.bluebrim.gui.client;

import javax.swing.Icon;

import com.bluebrim.base.shared.CoListElementViewIF;

/**
 * Interface for list models that wraps the "real" elements and
 * put them in a cache for faster access.
 * Creation date: (1999-10-15 00:12:33)
 * @author Lasse S
 */
public interface CoCachedListModelIF {

	public interface ElementWrapper {
		public abstract Icon getIcon();
		public abstract String getText();
		public abstract Object getElement();
	};
	public CoListElementViewIF getElementViewAt(int index);
}