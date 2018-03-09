package com.bluebrim.swing.client;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import com.bluebrim.base.shared.CoConstants;

/**
 * Default list and table cell renderer for string display.
 *
 * Suitable as superclass for other renderers displaying strings.
 *
 * @author Markus Persson 2000-07-21
 */
public class CoStringRenderer extends JLabel implements TableCellRenderer, ListCellRenderer {
	private static final Object PENDING_VALUE = CoConstants.PENDING_VALUE;

	private static final Color PENDING_BACKGROUND = new Color(0xFF, 0xFF, 0xE0); // Light yellow
	private static final Color PENDING_SELECTED_BACKGROUND = new Color(0xFF, 0xF0, 0xE0); // Light orange
	private static final Color PENDING_FOREGROUND = Color.black;

	private static final Border FOCUS_BORDER;
	private static final Border NO_FOCUS_BORDER;

	static {
		FOCUS_BORDER = UIManager.getBorder("List.focusCellHighlightBorder");
		Insets insets;
		try {
			insets = FOCUS_BORDER.getBorderInsets(null);
		} catch (NullPointerException npe) {
			insets = new Insets(1, 2, 1, 2);
		}
		NO_FOCUS_BORDER = new EmptyBorder(insets);
	}

	public CoStringRenderer() {
		super();
		init();
	}

	protected CoStringRenderer(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		init();
	}

	protected CoStringRenderer(Icon icon) {
		super(icon);
		init();
	}

	public final Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
		if (value != PENDING_VALUE) {
			prepareFor(value);
			setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
			setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
		} else {
			prepareForPending();
			setBackground(isSelected ? PENDING_SELECTED_BACKGROUND : PENDING_BACKGROUND);
			setForeground(PENDING_FOREGROUND);
		}
		setEnabled(list.isEnabled());
		setFont(list.getFont());
		setBorder(hasFocus ? FOCUS_BORDER : NO_FOCUS_BORDER);
		return this;
	}

	public final Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (value != PENDING_VALUE) {
			prepareFor(value);
			setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
			setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
		} else {
			prepareForPending();
			setBackground(isSelected ? PENDING_SELECTED_BACKGROUND : PENDING_BACKGROUND);
			setForeground(PENDING_FOREGROUND);
		}
		setEnabled(table.isEnabled());
		setFont(table.getFont());
		setBorder(hasFocus ? FOCUS_BORDER : NO_FOCUS_BORDER);
		return this;
	}

	private final void init() {
		setOpaque(true);
	}

	protected void prepareFor(Object value) {
		if (value instanceof String) {
			setText((String) value);
		} else {
			setText("");
		}
	}

	/**
	 * Override if you in prepareFor(Object) modify anything other than the text.
	 * (NOTE: The background is handled automatically and cannot be overridden.)
	 */
	protected void prepareForPending() {
		setText("");
	}
}