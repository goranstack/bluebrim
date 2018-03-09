package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import com.bluebrim.base.shared.CoListElementViewIF;
import com.bluebrim.swing.client.CoList;

/**
 * List to be used together with a list model that implements
 * the <code>CoCachedListModelIF</code> interface, i e a list model
 * that wraps the "real" element and puts them in a cache for faster access.
 * If we don't want all elements in the list to be wrapped from the beginning we
 * must have a fixed cell height and width. Otherwise will <code>ListUI.
 * updateLayoutState</code> iterate over all elements in the list and thereby
 * create wrappers for each and every element! Use <code>JList.
 * setPrototypeCellValue </code> or <code>setFixedCellWidth</code> and
 * </code>setFixedCellHeight</code>.
 *
 * Creation date: (1999-10-15 00:40:20)
 * @author: Lasse S
 */
public class CoCachedList extends CoList {

	public static class Renderer extends CoListCellRenderer {
		public Renderer() {
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			CoListElementViewIF element = (CoListElementViewIF) value;
			super.getListCellRendererComponent(list, element != null ? element.getIcon() : null, index, isSelected, cellHasFocus);
			setText((element != null) ? element.getText() : new String());
			return this;
		}
	}

	public class CachedListUI extends CoList.ListUI {

		protected void paintCell(
			Graphics g,
			int row,
			Rectangle rowBounds,
			ListCellRenderer cellRenderer,
			ListModel dataModel,
			ListSelectionModel selModel,
			int leadIndex) {
			Object value = ((CoCachedListModelIF) dataModel).getElementViewAt(row);
			boolean cellHasFocus = list.hasFocus() && (row == leadIndex);
			boolean isSelected = selModel.isSelectedIndex(row);

			Component rendererComponent = cellRenderer.getListCellRendererComponent(list, value, row, isSelected, cellHasFocus);

			int cx = rowBounds.x;
			int cy = rowBounds.y;
			int cw = rowBounds.width;
			int ch = rowBounds.height;
			rendererPane.paintComponent(g, rendererComponent, list, cx, cy, cw, ch, true);
		}
		protected void updateLayoutState() {
			int fixedCellHeight = list.getFixedCellHeight();
			int fixedCellWidth = list.getFixedCellWidth();

			cellWidth = (fixedCellWidth != -1) ? fixedCellWidth : -1;

			if (fixedCellHeight != -1) {
				cellHeight = fixedCellHeight;
				cellHeights = null;
			} else {
				cellHeight = -1;
				cellHeights = new int[list.getModel().getSize()];
			}

			if ((fixedCellWidth == -1) || (fixedCellHeight == -1)) {

				ListModel dataModel = list.getModel();
				int dataModelSize = dataModel.getSize();
				ListCellRenderer renderer = list.getCellRenderer();

				if (renderer != null) {
					for (int index = 0; index < dataModelSize; index++) {
						Object value = ((CoCachedListModelIF) dataModel).getElementViewAt(index);
						Component c = renderer.getListCellRendererComponent(list, value, index, false, false);
						rendererPane.add(c);
						Dimension cellSize = c.getPreferredSize();
						if (fixedCellWidth == -1) {
							cellWidth = Math.max(cellSize.width, cellWidth);
						}
						if (fixedCellHeight == -1) {
							cellHeights[index] = cellSize.height;
						}
					}
				} else {
					if (cellWidth == -1) {
						cellWidth = 0;
					}
					for (int index = 0; index < dataModelSize; index++) {
						cellHeights[index] = 0;
					}
				}
			}

			list.invalidate();
		}
	}

	public CoCachedList() {
	}

	public void updateUI() {
		setUI(new CachedListUI());
	}

	/**
	 * Temporary copy from JList, slightly modified. This is needed
	 * since someone stupidly decided to not deliver the cached views
	 * from the model in the normal way. Right now and I do not have
	 * the time to correct all the places it may have affected.
	 * 
	 * PENDING: Fix this when doing client/server rework, if not sooner.
	 * Implementing ref usage comes to mind here ...
	 *
	 * @see JList#getToolTipText
	 * @author Markus Persson 2002-11-25
	 */
	public String getToolTipText(MouseEvent event) {
		if (event != null) {
			Point p = event.getPoint();
			int index = locationToIndex(p);
			ListCellRenderer r = getCellRenderer();
			Rectangle cellBounds;

			if (index != -1 && r != null && (cellBounds = getCellBounds(index, index)) != null && cellBounds.contains(p.x, p.y)) {
				ListSelectionModel lsm = getSelectionModel();
				Object stupidWayToGetAView = ((CoCachedListModelIF) getModel()).getElementViewAt(index);

				Component rComponent =
					r.getListCellRendererComponent(
						this,
						stupidWayToGetAView,
						index,
						lsm.isSelectedIndex(index),
						(hasFocus() && (lsm.getLeadSelectionIndex() == index)));

				if (rComponent instanceof JComponent) {
					MouseEvent newEvent;

					p.translate(-cellBounds.x, -cellBounds.y);
					newEvent =
						new MouseEvent(
							rComponent,
							event.getID(),
							event.getWhen(),
							event.getModifiers(),
							p.x,
							p.y,
							event.getClickCount(),
							event.isPopupTrigger());

					String tip = ((JComponent) rComponent).getToolTipText(newEvent);

					if (tip != null) {
						return tip;
					}
				}
			}
		}
		return super.getToolTipText();
	}
}