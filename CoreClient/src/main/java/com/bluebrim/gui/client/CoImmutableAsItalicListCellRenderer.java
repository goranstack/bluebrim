package com.bluebrim.gui.client;

import java.awt.Font;

import javax.swing.JList;

/**
 * Insert the type's description here.
 * Creation date: (2001-04-05 08:57:00)
 * @author Dennis
 */
public abstract class CoImmutableAsItalicListCellRenderer extends CoListCellRenderer {
	private Font m_font;
	private Font m_immutableFont;

	public CoImmutableAsItalicListCellRenderer() {
		super();
	}

	protected abstract boolean isMutable(Object value, int index);

	protected void setColorAndFont(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.setColorAndFont(list, value, index, isSelected, cellHasFocus);

		if (!isMutable(value, index)) {
			if ((m_font == null) || !list.getFont().equals(m_font)) {
				m_font = list.getFont();
				m_immutableFont = m_font.deriveFont(Font.ITALIC);
			}

			setFont(m_immutableFont);
		}
	}
}