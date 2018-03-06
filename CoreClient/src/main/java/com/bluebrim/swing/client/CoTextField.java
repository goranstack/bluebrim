package com.bluebrim.swing.client;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
/**
 Subklass till JTextField som implementerar CoDnDTextComponentIF så att fältet kan 
 vara en källa för en drag & drop operation.
 */
public class CoTextField extends JTextField implements CoDnDTextComponentIF, CoAsIsCapable, DocumentListener {
	private int m_maxColumns;
	private boolean m_asIs = false;
	private boolean m_isDirty = false; // edited since last setText-call

	private boolean m_activateWhenLosingFocus;
	private boolean m_selectWhenGainingFocus;

	private static final FocusListener m_lostFocusListener = new FocusListener() {
		public void focusGained(FocusEvent e) {
		}

		public void focusLost(FocusEvent e) {
			CoTextField tf = (CoTextField) e.getSource();
			tf.focusLost();
		}
	};

	private static final FocusListener m_gainedFocusListener = new FocusListener() {
		public void focusGained(FocusEvent e) {
			((CoTextField) e.getSource()).selectAll();
		}

		public void focusLost(FocusEvent e) {
		}
	};

	private DocumentListener m_documentListener;
	private CoTextDragSourceListener m_dragSourceListener;
	protected boolean m_isDragging = false;
	protected boolean m_isSelecting = false;
	public final CoTextDragSourceListener getDragSourceListener() {
		return m_dragSourceListener;
	}
	public final void setDragSourceListener(CoTextDragSourceListener listener) {
		m_dragSourceListener = listener;
		setCaret(new DefaultCaret() {

			public void mousePressed(MouseEvent e) {
				if (m_isDragging)
					return;
				super.mousePressed(e);
			}
			public void mouseDragged(MouseEvent e) {
				if (m_isDragging)
					return;
				super.mouseDragged(e);
			}
			public void mouseClicked(MouseEvent e) {
				if (m_isDragging) {
					m_isDragging = false;
					int pos = viewToModel(e.getPoint());
					select(pos, pos);
				}
				super.mouseClicked(e);
			}
		});
	}

	public CoTextField() {
		this(null, 0, 0);
	}

	public CoTextField(int columns) {
		this(null, columns, 0);
	}

	public CoTextField(String text) {
		this(text, 0, 0);
	}

	public CoTextField(String text, int columns) {
		this(text, columns, 0);
	}

	public CoTextField(String text, int columns, int maxColumns) {
		this(null, text, columns, maxColumns);
	}

	public CoTextField(Document doc, String text, int columns, int maxColumns) {
		super(doc, text, columns);
		m_maxColumns = maxColumns;

		init();
	}

	public CoTextField(Document doc, String text, int columns) {
		this(doc, text, columns, 0);
	}

	public Color getBackground() {
		return isAsIs() ? m_asIsColor : super.getBackground();
	}

	private DocumentListener getDocumentListener() {
		if (m_documentListener == null) {
			m_documentListener = new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					doit(e);
				}
				public void removeUpdate(DocumentEvent e) {
					doit(e);
				}
				public void changedUpdate(DocumentEvent e) {
					doit(e);
				}

				private void doit(DocumentEvent e) {
					if (isAsIs()) {
						unsetAsIs();
						repaint();
					}

					m_isDirty = true;
				}
			};
		}

		return m_documentListener;
	}

	/**
	 * @see JTextField#getColumns()
	 * @author Markus Persson 1999-10-14
	 */
	public int getMaxColumns() {
		return m_maxColumns;
	}

	/**
	 * Prevents the height of this textfield to become
	 * greater than the preferred height, which in turn
	 * normally is derived from the font height.
	 *
	 * Useful in conjunction with glue in BoxPanels.
	 *
	 * Also, if a non-zero number of max columns has been
	 * set, the width is set to the columns multiplied by
	 * the column width.
	 *
	 * @see JTextField#getPreferredSize()
	 * @author Markus Persson 1999-06-07
	 */
	public Dimension getMaximumSize() {
		Dimension max = super.getMaximumSize();
		if (m_maxColumns != 0) {
			max.width = m_maxColumns * getColumnWidth();
		}
		max.height = getPreferredSize().height;
		return max;
	}

	public boolean isAsIs() {
		return m_asIs;
	}

	public boolean isDirty() {
		return m_isDirty;
	}

//	public boolean isEditable() {
//		return super.isEditable() && isEnabled();
//	}
//
//	public boolean isFocusTraversable() {
//		return super.isFocusTraversable() && isEditable();
//	}
//
//	public boolean isRequestFocusEnabled() {
//		return super.isRequestFocusEnabled() && isEditable();
//	}
//
	public boolean isSelecting() {
		return m_isSelecting;
	}

	protected void paintComponent(Graphics g) {
		boolean wasOpaque = isOpaque();
		Color origColor = g.getColor();
		Color parentBkg;

		if (wasOpaque && ((parentBkg = getParent().getBackground()) != null)) {
			g.setColor(parentBkg);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(origColor);
		}

		Border border = getBorder();
		if (border != null) {
			try {
				g.setColor(getBackground());
				Rectangle rectangle = AbstractBorder.getInteriorRectangle(this, border, 0, 0, getWidth(), getHeight());
				g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
				g.setColor(origColor);
				setOpaque(false);
				super.paintComponent(g);
			} finally {
				setOpaque(wasOpaque);
			}
		} else
			super.paintComponent(g);

	}

	protected void processMouseEvent(MouseEvent e) {
		//m_isDragging = false;

		if (m_dragSourceListener == null)
			super.processMouseEvent(e);
		else {
			if (m_dragSourceListener.canStartDrag(e))
				m_isDragging = true;
			super.processMouseEvent(e);
		}

		if (e.getID() == MouseEvent.MOUSE_RELEASED || e.getID() == MouseEvent.MOUSE_CLICKED)
			m_isSelecting = false;
	}

	protected void processMouseMotionEvent(MouseEvent e) {
		//m_isDragging = m_isDragging && e.getID() == MouseEvent.MOUSE_DRAGGED;

		if (!m_isDragging && e.getID() == MouseEvent.MOUSE_DRAGGED) {
			m_isSelecting = true;
		}
		super.processMouseMotionEvent(e);
	}

	public void setActivateWhenLosingFocus(boolean v) {
		if (v && !m_activateWhenLosingFocus) {
			addFocusListener(m_lostFocusListener);
		} else if (!v && m_activateWhenLosingFocus) {
			removeFocusListener(m_lostFocusListener);
		}
		m_activateWhenLosingFocus = v;
	}

	public void setAsIs() {
		setText("");
		m_asIs = true;
	}

	public void setDocument(Document doc) {
		Document old = getDocument();

		if ((doc != old) && (old != null)) {
			old.removeDocumentListener(this);
		}

		super.setDocument(doc);

		if (doc != old) {
			doc.addDocumentListener(this);
		}
	}

	/**
	 * Sets the maximum number of columns in this CoTextField, and then
	 * invalidate the layout. Similar to setColumns in JTextField.
	 *
	 * @exception IllegalArgumentException if columns is less than 0
	 * @see JTextField#setColumns(int)
	 *
	 * @author Markus Persson 1999-10-14
	 */
	public void setMaxColumns(int columns) {
		if (columns < 0) {
			throw new IllegalArgumentException("max columns less than zero.");
		}
		if (columns != m_maxColumns) {
			m_maxColumns = columns;
			invalidate();
		}
	}

	public void setSelectWhenGainingFocus(boolean v) {
		if (v && !m_selectWhenGainingFocus) {
			addFocusListener(m_gainedFocusListener);
		} else if (!v && m_selectWhenGainingFocus) {
			removeFocusListener(m_gainedFocusListener);
		}
		m_selectWhenGainingFocus = v;
	}

	public void setText(String s) {
		m_asIs = false;

		if ((s != null) && s.equals(getText()))
			return;

		super.setText(s);

		m_isDirty = false;
	}

	protected void unsetAsIs() {
		m_asIs = false;
	}

	public void changedUpdate(DocumentEvent e) {
		handleDocumentChange(e);
	}

	private void focusLost() {
		if (m_isDirty) {
			m_isDirty = false;
			postActionEvent();
		}
	}

	private void handleDocumentChange(DocumentEvent e) {
		if (isAsIs()) {
			unsetAsIs();
			repaint();
		}

		markDirty();
	}

	private void init() {
		getDocument().addDocumentListener(this);
	}

	public void insertUpdate(DocumentEvent e) {
		handleDocumentChange(e);
	}

	private void markDirty() {
		m_isDirty = true;
	}

	public void removeUpdate(DocumentEvent e) {
		handleDocumentChange(e);
	}

}