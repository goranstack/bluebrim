package com.bluebrim.base.client.datatransfer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JComponent;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import com.bluebrim.swing.client.CoListDropTargetListener;
import com.bluebrim.swing.client.CoTreeDropTargetListener;

/**
 *	Abstrakt superklass för de objekt som skall hantera visuell feedback för droptargets.
 *	Har konkreta subklasser för olika typer av gränssnittskomponenter, ex JList, 
 *	JTree och JTextField. Dessa hanterar dragOverfeedback och drop på olika sätt.
 *	@see CoListDropTargetListener
 *	@see CoTreeDropTargetListener
 */
public abstract class CoDropTargetListener implements DropTargetListener {
	protected JComponent m_component;
	private boolean m_dragUnderFeedbackOn = false;
	protected boolean m_targetCanHandleDrop;
	public DataFlavor[] m_acceptableDropFlavors;

	private class DropTargetBorder extends AbstractBorder {
		public Insets getBorderInsets(Component c) {
			return new Insets(2, 2, 2, 2);
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			if (m_dragUnderFeedbackOn) {
				Color tmp = g.getColor();
				g.setColor(Color.blue);
				g.drawRect(x, y, width - 1, height - 1);
				g.drawRect(x + 1, y + 1, width - 3, height - 3);
				g.setColor(tmp);
			}
		}
	}

	public CoDropTargetListener(JComponent component, DataFlavor[] acceptableDropFlavors) {
		m_component = component;
		Border componentBorder = component.getBorder();
		if (componentBorder == null)
			component.setBorder(new DropTargetBorder());
		else
			component.setBorder(new CompoundBorder(new DropTargetBorder(), componentBorder));
		m_acceptableDropFlavors = acceptableDropFlavors;
	}

	/**
		Anropas från #dragOver och svarar med true<br>
		Subklasserna specificerar villkor. 
	 */
	protected boolean canEndDrag(DropTargetDragEvent dtde) {
		return true;
	}

	protected boolean canTargetHandleDrop(DropTargetDragEvent dtde) {
		for (int i = 0; i < m_acceptableDropFlavors.length; i++)
			if (dtde.isDataFlavorSupported(m_acceptableDropFlavors[i]))
				return true;
		return false;
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		m_targetCanHandleDrop = canTargetHandleDrop(dtde);

		if (m_targetCanHandleDrop)
			dtde.acceptDrag(dtde.getDropAction());
		else
			dtde.rejectDrag();

		startFeedback();
	}

	public void dragExit(DropTargetEvent dtde) {
		stopFeedback();
	}

	public void dragOver(DropTargetDragEvent dtde) {

		if (m_targetCanHandleDrop && canEndDrag(dtde))
			dtde.acceptDrag(dtde.getDropAction());
		else
			dtde.rejectDrag();
	}

	public void drop(DropTargetDropEvent dtde) {
		if (m_targetCanHandleDrop) {
			dtde.acceptDrop(getDropAction(dtde));
			handleDrop(dtde);
			dtde.dropComplete(true);
		} else {
			dtde.rejectDrop();
		}
		stopFeedback();
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {

	}

	protected void stopFeedback() {
		m_dragUnderFeedbackOn = false;
		m_component.repaint();
	}

	protected int getDropAction(DropTargetDropEvent dtde) {
		return DnDConstants.ACTION_COPY_OR_MOVE;
	}

	protected abstract void handleDrop(DropTargetDropEvent dtde);

	protected void startFeedback() {
		m_dragUnderFeedbackOn = true;
		m_component.repaint();
	}
}