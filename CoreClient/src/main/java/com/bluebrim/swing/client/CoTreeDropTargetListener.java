package com.bluebrim.swing.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import com.bluebrim.base.client.datatransfer.CoDropTargetListener;

/**
 * Concrete subclass to <code>CoDropTargetListener</code> used to handle drop
 * in a tree view. Uses a special renderer to give feedback whne the drag is over
 * a row that can accept the drag data.
 */
public abstract class CoTreeDropTargetListener extends CoDropTargetListener {
	protected int m_dropRow = -1;

	protected static class DropTargetTreeCellRenderer implements TreeCellRenderer {
		private CoTreeDropTargetListener m_dropTargetListener;
		private DefaultTreeCellRenderer m_wrappedRenderer;
		private boolean m_isRenderingDropNode;
		private Border m_nodeSelectionBorder = new NodeSelectionBorder(Color.gray);

		public class NodeSelectionBorder extends LineBorder {

			public NodeSelectionBorder(Color color) {
				super(color, 1);
			}
			public Insets getBorderInsets(Component c) {
				return new Insets(1, 1, 1, 1);
			}
			public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
				if (isRenderingDropNode()) {
					super.paintBorder(c, g, x, y, width, height);
				}
			}

		}

		public DropTargetTreeCellRenderer(CoTreeDropTargetListener listener) {
			m_dropTargetListener = listener;
			m_wrappedRenderer = (DefaultTreeCellRenderer) ((CoTree) listener.m_component).getCellRenderer();
			Border componentBorder = m_wrappedRenderer.getBorder();
			m_wrappedRenderer.setBorder(
				(componentBorder != null) ? BorderFactory.createCompoundBorder(m_nodeSelectionBorder, componentBorder) : m_nodeSelectionBorder);
		}

		/**
		 * Om 'row' är lika med den rad som draget befinner sig över så läggs en ram 
		 * runt raden.
		 */
		public java.awt.Component getTreeCellRendererComponent(
			JTree tree,
			Object value,
			boolean selected,
			boolean expanded,
			boolean leaf,
			int row,
			boolean hasFocus) {
			JComponent tRendererComponent = (JComponent) m_wrappedRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
			m_isRenderingDropNode = (m_dropTargetListener.getDropRow() == row);
			return tRendererComponent;
		}
		private boolean isRenderingDropNode() {
			return m_isRenderingDropNode;
		}
	}

	public CoTreeDropTargetListener(JComponent component, DataFlavor[] acceptableDropFlavors) {
		super(component, acceptableDropFlavors);
		((CoTree) component).setCellRenderer(new DropTargetTreeCellRenderer(this));

	}

	protected int calculateDropRow(DropTargetDragEvent dtde) {
		Point tPoint = dtde.getLocation();
		int tRow = getTreeView().getRowForLocation(tPoint.x, tPoint.y);
		return tRow;
	}

	/**
	 * sent in dragOver 
	 */
	public boolean canEndDrag(DropTargetDragEvent dtde) {

		if (super.canEndDrag(dtde)) {
			Point p = dtde.getLocation();
			int x = (new Double(p.getX())).intValue();
			int y = (new Double(p.getY())).intValue();

			int tIndex = getTreeView().getRowForLocation(x, y);
			return (tIndex != -1) ? true : false;

		} else
			return false;
	}

	public void dragOver(DropTargetDragEvent dtde) {
		super.dragOver(dtde);
		if (m_targetCanHandleDrop && canEndDrag(dtde)) {
			setDropRow(calculateDropRow(dtde));
		} else {
			setDropRow(-1);
		}

	}

	public void drop(DropTargetDropEvent dtde) {
		super.drop(dtde);
		setDropRow(-1);
	}

	protected int getDropRow() {
		return m_dropRow;
	}

	protected final JTree getTreeView() {
		return (JTree) m_component;
	}

	protected void setDropRow(int row) {
		m_dropRow = row;
		m_component.repaint();
	}
}
