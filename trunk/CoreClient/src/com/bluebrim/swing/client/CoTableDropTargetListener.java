package com.bluebrim.swing.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.bluebrim.base.client.datatransfer.CoDropTargetListener;
/**
	Subclass to <code>CoDropTargetListener</code> to handle drag & drop 
	over a <code>CoTable</code>.
 */
public abstract class CoTableDropTargetListener extends CoDropTargetListener {
	protected int m_dropPosition = -1;
	protected DropTargetTableCellRenderer m_targetRenderer;

	/**
	 	Implementation of <code>ListCellRenderer</code> that is installed by a
	 	<code>CoListDropTargetListener</code> to handle the feedback when the drag is over it's
	 	list and the list can accept the drag data. It's installed as a "wrapper" of the the original
	 	renderer.
	 */
	private static class DropTargetTableCellRenderer extends JPanel implements TableCellRenderer {
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
				if (isRenderingDropRow()) {
					Color oldColor = g.getColor();
					g.setColor(m_selectionColor);
					for (int i = 0; i < 2; i++) {
						if (drawSelectionAboveRow())
							g.drawLine(x, y + i, width - 1, y + i);
						else
							g.drawLine(x, height - i - 1, width - 1, height - i - 1);
					}
					g.setColor(oldColor);
				}
			}

		}

		private CoTableDropTargetListener m_dropTargetListener;
		private TableCellRenderer m_wrappedRenderers[];
		private boolean m_isRenderingDropRow;
		private boolean m_selectAboveRow;
		private Border m_selectionBorder = new GapSelectionBorder(Color.blue);

		public DropTargetTableCellRenderer(CoTableDropTargetListener listener) {
			m_dropTargetListener = listener;
			CoTable tTable = (CoTable) listener.m_component;
			TableColumnModel tColumnModel = tTable.getColumnModel();
			int tColumnCount = tTable.getColumnCount();
			m_wrappedRenderers = new TableCellRenderer[tColumnCount];
			for (int i = 0; i < tColumnCount; i++) {
				TableColumn tColumn = tColumnModel.getColumn(i);
				m_wrappedRenderers[i] = tColumn.getCellRenderer();
				tColumn.setCellRenderer(this);
			}
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JComponent tRendererComponent = (JComponent) m_wrappedRenderers[column].getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			Border tComponentBorder = tRendererComponent.getBorder();
			tRendererComponent.setBorder((tComponentBorder != null) ? BorderFactory.createCompoundBorder(m_selectionBorder, tComponentBorder) : m_selectionBorder);
			if (m_dropTargetListener.getDropPosition() == row) {
				m_isRenderingDropRow = true;
				m_selectAboveRow = true;
			} else {
				TableModel tModel = (TableModel) table.getModel();
				if (row == tModel.getRowCount() - 1) {
					if (m_dropTargetListener.getDropPosition() > row) {
						m_isRenderingDropRow = true;
						m_selectAboveRow = false;
					} else
						m_isRenderingDropRow = false;

				} else {
					m_isRenderingDropRow = false;
				}
			}
			return tRendererComponent;
		}
		private boolean isRenderingDropRow() {
			return m_isRenderingDropRow;
		}
		private boolean drawSelectionAboveRow() {
			return m_selectAboveRow;
		}
	}

	public CoTableDropTargetListener(JComponent component, DataFlavor[] acceptableDropFlavors) {
		super(component, acceptableDropFlavors);
		m_targetRenderer = new DropTargetTableCellRenderer(this);
	}

	protected int calculateDropPosition(DropTargetDragEvent dtde) {
		Point tLocation = dtde.getLocation();
		int index = rowAtPoint(tLocation);
		Rectangle tRectangle = ((JTable) m_component).getCellRect(index, 0, false);
		if (tRectangle != null) {
			tRectangle.translate(0, tRectangle.height / 2);
			if (tRectangle.contains(tLocation.x, tLocation.y))
				index++;
		} else
			index = -1;
		return index;
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		super.dragEnter(dtde);
		setDropPosition(calculateDropPosition(dtde));
	}

	public void dragExit(DropTargetDragEvent dtde) {
		super.dragExit(dtde);
		//setDropPosition(-1);
	}

	public void dragOver(DropTargetDragEvent dtde) {
		super.dragOver(dtde);
		setDropPosition(calculateDropPosition(dtde));
	}

	public void drop(DropTargetDropEvent dtde) {
		super.drop(dtde);
		setDropPosition(-1);
	}

	protected int getDropPosition() {
		return m_dropPosition;
	}

	protected int rowAtPoint(Point point) {
		JTable tTable = (JTable) m_component;
		int rowHeight = tTable.getRowHeight();
		int rowSpacing = tTable.getIntercellSpacing().height;
		int totalRowHeight = rowHeight + rowSpacing;
		int result = point.y / totalRowHeight;
		if (result < 0) {
			return -1;
		} else if (result >= tTable.getRowCount()) {
			return tTable.getRowCount();
		} else {
			return result;
		}
	}

	protected void setDropPosition(int dropPosition) {
		m_dropPosition = dropPosition;
		m_component.repaint();
	}

	protected boolean targetCanHandleDrop(DropTargetEvent dte) {
		return getDropPosition() != -1;
	}
}