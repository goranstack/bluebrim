package com.bluebrim.swing.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;

/**
 * Subklass till JScrollPane som innehåller en CoTable.
 */
public class CoTableBox extends JScrollPane {

	private CoTable m_table;
	private FocusListener m_focusListener;

	public CoTableBox() {
		super();
		m_focusListener = new FocusListener() {
			public void focusGained(FocusEvent e) {
				repaint();
			}
			public void focusLost(FocusEvent e) {
				repaint();
			}
		};
		setTable(new CoTable());
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if ((e.getClickCount() == 1) && ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0))
					unselect();
			}});
	}

	public CoTable getTable() {
		return m_table;
	}

	/**
	 * If you have a component in a scroll pane and the scrollpane is larger 
	 * than the component you have in it, there is a gray area.
	 * <br>
	 * This code from Aprille Davis fixes this problem.
	 */
	public void paint(Graphics g) {
		Color oldColor = g.getColor();

		// Get the bounds of the viewable rectangle.
		Rectangle rect = getViewport().getBounds();
		// Only paint the non-table portion of the rectangle.
		int heightOffset = 0; // height of table
		if (getViewport().getView() != null) {
			heightOffset = getViewport().getView().getBounds().height;
			// set the color we're going to paint to be the same as the
			//component that is in the scrollpane
			g.setColor(getViewport().getView().getBackground());
		}

		g.fillRect(rect.x, rect.y + heightOffset, rect.width, rect.height - heightOffset);

		g.setColor(oldColor);
		super.paint(g);

	}

	public final void setTable(CoTable table) {
		if (m_table != table) {
			if (m_table != null)
				m_table.removeFocusListener(m_focusListener);
			m_table = table;
			setViewportView(m_table);
			if (m_table != null)
				m_table.addFocusListener(m_focusListener);
		}
	}
	
	private void unselect() {
		m_table.editingStopped(new ChangeEvent(this));
		m_table.clearSelection();
	}

}
