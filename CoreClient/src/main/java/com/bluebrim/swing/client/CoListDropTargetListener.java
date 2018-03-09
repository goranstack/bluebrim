package com.bluebrim.swing.client;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;

import javax.swing.*;
import javax.swing.border.*;

import com.bluebrim.base.client.datatransfer.*;

/**
 * Abstrakt subklass till CoDropTargetListener för att hantera drop i en CoList.
 */
public abstract class CoListDropTargetListener extends CoDropTargetListener {
	protected int m_dropPosition = -1;
	protected boolean m_allowPositionDrop;
	private boolean m_lineGapMarkerOn = false;

	/**
	 	Implementation of <code>ListCellRenderer</code> that is installed by a
	 	<code>CoListDropTargetListener</code> to handle the feedback when the drag is over it's
	 	list and the list can accept the drag data. It's installed as a "wrapper" of the the original
	 	renderer.
	 */
	private class DropTargetListCellRenderer extends JPanel implements ListCellRenderer {
		public class GapSelectionBorder extends AbstractBorder {

			private Color m_selectionColor;

			public GapSelectionBorder(Color color) {
				super();
				m_selectionColor = color;
			}
			public Insets getBorderInsets(Component c) {
				return new Insets(2, 2, 2, 2);
			}
			public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
				if (m_lineGapMarkerOn) {
					Color oldColor = g.getColor();
					g.setColor(m_selectionColor);
					for (int i = 0; i < 2; i++) {
						if (m_selectAboveRow)
							g.drawLine(x, y + i, width - 1, y + i);
						else
							g.drawLine(x, height - i - 1, width - 1, height - i - 1);
					}
					g.setColor(oldColor);
				}
			}

		}

		private ListCellRenderer m_wrappedRenderer;
		private boolean m_selectAboveRow;
		private Border m_selectionBorder = new GapSelectionBorder(Color.gray);

		public DropTargetListCellRenderer() {
			m_wrappedRenderer = ((CoList) m_component).getCellRenderer();
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JComponent tRendererComponent = (JComponent) m_wrappedRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (m_allowPositionDrop) {
				Border tComponentBorder = tRendererComponent.getBorder();
				tRendererComponent.setBorder((tComponentBorder != null) ? BorderFactory.createCompoundBorder(m_selectionBorder, tComponentBorder) : m_selectionBorder);
				if (getDropPosition() == index) {
					m_lineGapMarkerOn = true;
					m_selectAboveRow = true;
				} else {
					ListModel tModel = (ListModel) list.getModel();
					if (index == tModel.getSize() - 1) {
						if (getDropPosition() > index) {
							m_lineGapMarkerOn = true;
							m_selectAboveRow = false;
						} else
							m_lineGapMarkerOn = false;

					} else {
						m_lineGapMarkerOn = false;
					}
				}
			}
			return tRendererComponent;
		}
	}
	
	/**
	 * @author Piotr Kiernicki (2001-05-29 11:31:32)
	 */
	public CoListDropTargetListener(JComponent component, DataFlavor[] acceptableDropFlavors) {
		super(component, acceptableDropFlavors);
		((CoList) component).setCellRenderer(new DropTargetListCellRenderer());
		m_allowPositionDrop = false; // no selectionBorder (a separator) drawn by the renderer
	}

	protected int calculateDropPosition(DropTargetDragEvent dtde) {
		if (canEndDrag(dtde)) {
			Point tLocation = dtde.getLocation();
			JList tList = (JList)m_component;
			int index = tList.locationToIndex(tLocation);
			Rectangle tRectangle = tList.getCellBounds(index, index);
			if (tRectangle != null) {
				tRectangle.translate(0, tRectangle.height / 2);
				if (tRectangle.contains(tLocation.x, tLocation.y))
					index++;
			} else
				// when pointing below the last element 
				index = tList.getModel().getSize();
			return index;
		} else
			return Integer.MAX_VALUE;
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		super.dragEnter(dtde);
		if (m_targetCanHandleDrop)
			m_allowPositionDrop = true;
	}

	public void dragExit(DropTargetEvent dte) {
		super.dragExit(dte);
		m_allowPositionDrop = false; // selectionBorder is not drawn, see the inner class DropTargetListCellRenderer
	}

	public void dragOver(DropTargetDragEvent dtde) {
		super.dragOver(dtde);
		if (m_allowPositionDrop && canEndDrag(dtde))
			setDropPosition(calculateDropPosition(dtde));
		else
			setDropPosition(-1);
	}
	public void drop(DropTargetDropEvent dtde) {
		super.drop(dtde);
	}

	protected int getDropPosition() {
		return m_dropPosition;
	}

	protected void setDropPosition(int dropPosition) {
		m_dropPosition = dropPosition;
		m_component.repaint();
	}
	
	protected void stopFeedback() {
		m_lineGapMarkerOn = false;
		super.stopFeedback();
	}

}