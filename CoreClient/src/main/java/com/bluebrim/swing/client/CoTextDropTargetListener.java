package com.bluebrim.swing.client;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetEvent;

import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import com.bluebrim.base.client.datatransfer.CoDropTargetListener;
/**
 * Konkret subklass till CoDropTargetListener för att hantera 
 * drag och eventuellt drop i en textvy, exempelvis en CoTextField.
 * Instansvariabler
 * <ul>
 * <li>	savedSelStart	(int) startposition för den ursprungliga selekteringen
 * <li>	savedSelEnd		(int) slutposition för den ursprungliga selekteringen	</ul>
 */
public abstract class CoTextDropTargetListener extends CoDropTargetListener {
	int savedSelStart = -1;
	int savedSelEnd = -1;
	protected int m_dropPosition = -1;

	public CoTextDropTargetListener(JComponent component, DataFlavor[] acceptableDropFlavors) {
		super(component, acceptableDropFlavors);
	}

	protected int calculateDropPosition(DropTargetDragEvent dtde) {
		return getTextComponent().viewToModel(dtde.getLocation());
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		super.dragEnter(dtde);
		getTextComponent().setCaretColor(Color.blue);

		if (m_targetCanHandleDrop) {
			getTextComponent().requestFocus();
			setDropPosition(calculateDropPosition(dtde));
		}
	}

	public void dragExit(DropTargetEvent dtde) {
		getTextComponent().setCaretColor(Color.black);
		super.dragExit(dtde);
	}

	public void dragOver(DropTargetDragEvent dtde) {
		super.dragOver(dtde);
		if (m_targetCanHandleDrop)
			setDropPosition(calculateDropPosition(dtde));
	}


	private JTextComponent getTextComponent() {
		return (JTextComponent) m_component;
	}

	protected void setDropPosition(int dropPosition) {
		getTextComponent().setCaretPosition(dropPosition);
		m_component.repaint();
		m_dropPosition = dropPosition;
	}
}