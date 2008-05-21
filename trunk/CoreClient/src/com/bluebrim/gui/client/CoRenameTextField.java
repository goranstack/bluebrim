package com.bluebrim.gui.client;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import com.bluebrim.base.shared.CoRenameable;
import com.bluebrim.gemstone.client.CoTransactionUtilities;
import com.bluebrim.swing.client.CoTextField;
import com.bluebrim.transact.shared.CoCommand;
/**
 * A text field that have functions for changing the name of a <code>CoRenameable</code>
 * in a JList with a transaction.
 * Creation date: (2001-01-25 14:43:50)
 * @author Arvid Berg
 */
public class CoRenameTextField extends CoTextField implements ActionListener, FocusListener, MouseListener {
	private CoRenameable m_item;

	public CoRenameTextField() {
	}

	public void actionPerformed(ActionEvent ev) {
		renameCommand();
	}

	/**
	 * Creates a rectangle that covers the text in the label
	 * Creation date: (2001-03-06 12:36:56)
	 * @return java.awt.Rectangle
	 * @param tBounds java.awt.Rectangle
	 * @param tLabel javax.swing.JLabel
	 */
	public static Rectangle calculateRectForJLabel(Rectangle tBounds, JLabel tLabel) {

		int offset = getLabelOffset(tLabel);
		Insets ins = tLabel.getInsets();
		//setBounds((int) (r.getX() + ofset + ins.left), (int) (r.getY() + ins.top), (int) (r.getWidth() - (ins.left + ins.right + ofset)), (int) (r.getHeight() - (ins.top + ins.bottom)));
		return new Rectangle((int) (tBounds.getX() + offset + ins.left), (int) (tBounds.getY() + ins.top), (int) (tBounds.getWidth() - (ins.left + ins.right + offset)), (int) (tBounds.getHeight() - (ins.top + ins.bottom)));
	}
	/**
	 * Gets information from the list and calls calculateRectForJLabel.
	 * Creation date: (2001-04-24 15:13:46)
	 * @return java.awt.Rectangle
	 * @param tList javax.swing.JList
	 */
	public static Rectangle calculateRectForJListWidthJLabelRenderer(JList tList) {
		if ((!(tList.getCellRenderer() instanceof JLabel)) || tList.getSelectedIndex() < 0)
			return new Rectangle(10, 10, 20, 20);

		return calculateRectForJLabel(tList.getCellBounds(tList.getSelectedIndex(), tList.getSelectedIndex()), (JLabel) tList.getCellRenderer());
	}
	public void focusGained(FocusEvent r) {
	}
	public void focusLost(FocusEvent e) {
		if (!e.isTemporary())
			renameCommand();
	}
	
	/**
	 * Calculate the ofset from the left side of the label to where the editing should start.
	 * Creation date: (2001-03-06 11:49:49)
	 * @return double
	 * @param tLabel javax.swing.JLabel
	 */
	protected static int getLabelOffset(JLabel tLabel) {
		int ofset = 0;
		Icon icon = tLabel.getIcon();
		if (icon != null) {
			ofset = tLabel.getIconTextGap();
			ofset += icon.getIconWidth();
		}
		return ofset;
	}

	public void mouseClicked(MouseEvent e) {
		CoRenameable item = null;
		Rectangle bounds = null;
		Object o = null;
		if (e.getClickCount() == 2) {
			if (e.getSource() instanceof JList) {
				JList tList = (JList) e.getSource();
				int index = tList.locationToIndex(e.getPoint());
				o = tList.getModel().getElementAt(index);

				bounds = CoRenameTextField.calculateRectForJListWidthJLabelRenderer(tList);

			} else if (e.getSource() instanceof JTree) {
				JTree tTree = (JTree) e.getSource();
				TreePath path = tTree.getPathForLocation(e.getX(), e.getY());
				if (path == null)
					return;
				o = path.getLastPathComponent();

				TreeCellRenderer rend = tTree.getCellRenderer();
				if (rend instanceof JLabel)
					bounds = calculateRectForJLabel(tTree.getPathBounds(path), (JLabel) rend);

				else
					return;
			}

		}
		if (o instanceof CoRenameable)
			item = (CoRenameable) o;
		else
			return;
		rename(item, bounds);

	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
	/** Initialize the text field for editing.
	*/
	public void rename(CoRenameable o, Rectangle r) {
		if (!o.isRenameable())
			return;
		m_item = o;

		setText(m_item.getName());
		selectAll();

		setBorder(new javax.swing.border.EmptyBorder(1, 1, 1, 1));

		setBounds(r);
		addActionListener(this);
		addFocusListener(this);
		setVisible(true);
		requestFocus();
	}
	/** Initialize the text field for editing
	*   @deprecated
	*/
	public void rename(CoRenameable o, JList t) {
		if (!o.isRenameable())
			return;
		m_item = o;

		setText(m_item.getName());
		selectAll();
		Rectangle r = t.getCellBounds(t.getSelectedIndex(), t.getSelectedIndex());
		Point p = t.getLocationOnScreen();
		//t.add(m_nameText);
		setBorder(new javax.swing.border.EmptyBorder(1, 1, 1, 1));
		int ofset = 0;
		Insets ins = new Insets(0, 0, 0, 0);
		if (t.getCellRenderer() instanceof JLabel) {

			Icon icon = ((JLabel) t.getCellRenderer()).getIcon();
			if (icon != null) {
				ofset = ((JLabel) t.getCellRenderer()).getIconTextGap();
				ofset += icon.getIconWidth();
			}
			ins = ((JLabel) t.getCellRenderer()).getInsets();
		}
		setBounds((int) (r.getX() + ofset + ins.left), (int) (r.getY() + ins.top), (int) (r.getWidth() - (ins.left + ins.right + ofset)), (int) (r.getHeight() - (ins.top + ins.bottom)));
		addActionListener(this);
		addFocusListener(this);
		setVisible(true);
		requestFocus();
	}

	/**
	 * Handles the transaction with the domain object.	
	 */
	private void renameCommand() {
		setVisible(false);
		CoCommand c = new CoCommand("RENAME " + m_item.getClass().getName()) {
			public boolean doExecute() {
				m_item.setName(getText());
				return true;
			}
		};
		CoTransactionUtilities.execute(c, m_item);
		removeActionListener(this);
		removeFocusListener(this);
		m_item = null;
	}
}