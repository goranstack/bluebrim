package com.bluebrim.swing.client;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;

/**
  	Subklass till JComboBox. Subklassad för att få en egen typ och inte behöva exponera Swing i klientklasserna. 

  	Följande funktionallitet har adderats:

  	1. "as is"-hantering.
  	   Metoder: setAsIs, isAsIs
  	   
  	2. "Tyst" (genererar inga events) manipulering av comboboxen.
  	   Om sista argumentet i nedanstående metoder är true så genereras inga events.
  	   Metoder: addItem( Object, boolean )
  	            addNullItem( Object, boolean )
  	            removeAllItems( boolean )
  	            removeItem( boolean )
  	            setSelectedIndex( int, boolean )
  	            setSelectedItem( Object, boolean )
  	   Med metoden setQuiet( boolean ) kan events stängs av och på för flera anrop.
  	   Exempel:
  	     cb.setQuiet( true );
  	     cb.addItem( "1" );
  	     cb.addItem( "2" );
  	     cb.addItem( "3" );
 	       cb.setQuiet( false );
 	     är ekvivalent med:
  	     cb.addItem( "1", true );
  	     cb.addItem( "2", true );
  	     cb.addItem( "3", true );

  	3. ActionEvent-objekt som levereras av comboboxen har det valda objektets strängrepresentation som
  	   actionCommand i stället för den konstanta sträng som JComboBox använder.

  	4. Hantering av null. Metoden addNullItem kan användas för att definiera ett null-värde.
  	   Första argumentet i addNullItem presenteras som vanligt i comboboxen på skärmen men
  	   comboboxens api (getSelectedItem, ItemEvent.getItem() samt ActionEvent.getActionCommand() )
  	   kommer att leverera null istället.

 *
 * CoNamed handling added by Markus Persson 1999-10-29.
 *
 * PENDING: Will this collide with anyone using icons in the combo box?
 */

public class CoComboBox extends JComboBox implements CoAsIsCapable {
	private boolean m_keepQuiet = false;
	private boolean m_asIs = false;

	private Object m_nullItem = null;
	private int m_nullIndex = -1;

	private static final Object m_asIsValue = new Object();

	private static final NumberFormat m_numberFormat = NumberFormat.getInstance(Locale.getDefault());

	private class Renderer extends JComponent implements ListCellRenderer {
		private ListCellRenderer m_delegate;
		private Dimension m_size;

		public Renderer(ListCellRenderer renderer) {
			m_delegate = renderer;
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			if (value instanceof CoNamed) {
				value = ((CoNamed) value).getName();
			} else if (value instanceof Number) {
				value = m_numberFormat.format(value);
			}

			if (m_asIs && (index == -1)) {
				m_size = m_delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus).getPreferredSize();
				return this;
			} else {
				return m_delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}
		}

		public Dimension getPreferredSize() {
			return m_size;
		}

		public void paint(Graphics g) {
			Color oldColor = g.getColor();
			g.setColor(m_asIsColor);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(oldColor);
			paintBorder(g);
		}
	}

	//	private static NumberFormat m_formatter = NumberFormat.getInstance(Locale.getDefault());

	public class Editor extends CoTextField implements ComboBoxEditor, FocusListener {
		private KeyListener m_keyListener;
		private boolean m_alreadyLostFocus; // prevents infinite recursion in focusLost

		public Editor(int columns) {
			super("", columns);
			addFocusListener(this);
		}

		public Component getEditorComponent() {
			return this;
		}

		public void focusLost(FocusEvent e) {
			if (!m_alreadyLostFocus && !e.isTemporary()) {
				m_alreadyLostFocus = true;
				postActionEvent();
				m_alreadyLostFocus = false;
			}
		}

		public void focusGained(FocusEvent e) {
			selectAll();
		}

		public Object getItem() {
			return getText();
		}

		public void setItem(Object i) {
			if (i != null) {
				if (i instanceof Number)
					i = m_numberFormat.format(i);
				setText(i.toString());
			} else {
				setText("");
			}
		}

		public void selectAll() {
			super.selectAll();
			requestFocus();
		}

		public boolean isAsIs() {
			return (CoComboBox.this == null) ? false : CoComboBox.this.m_asIs; // Yes, the null-check must be there
		}

		protected void unsetAsIs() {
			CoComboBox.this.m_asIs = false;
		}
	}

	public CoComboBox() {
		this(9);
	}

    public CoComboBox(Object[] items) {
        super(items);
    }

	public CoComboBox(int columns) {
		super();
		setRenderer(new Renderer(getRenderer()));
		setEditor(new Editor(columns));
	}

	public CoComboBox(ComboBoxModel model) {
		this(model, 9);
	}

	public CoComboBox(ComboBoxModel model, int columns) {
		super(model);
		setRenderer(new Renderer(getRenderer()));
		setEditor(new Editor(columns));
	}
	public void addItem(Object anObject, boolean quiet) {
		m_keepQuiet = quiet;
		addItem(anObject);
		m_keepQuiet = false;
	}
	public void addNullItem(Object nullItem) {
		m_nullItem = nullItem;
		m_nullIndex = getItemCount();
		addItem(nullItem);
		setSelectedItem(m_nullItem);
	}
	public void addNullItem(Object anObject, boolean quiet) {
		m_keepQuiet = quiet;
		addNullItem(anObject);
		m_keepQuiet = false;
	}
	protected void fireActionEvent() {
		if (!m_keepQuiet) {
			super.fireActionEvent();
		}
	}
	protected void fireItemStateChanged(ItemEvent e) {
		if (!m_keepQuiet) {
			super.fireItemStateChanged(e);
		}
	}
	public String getActionCommand() {
		Object i = getSelectedItem();

		if (i == null)
			return null;
		else if (i == m_nullItem)
			return null;
		else
			return i.toString();
	}
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	public void insertItemAt(Object anObject, int index, boolean quiet) {
		m_keepQuiet = quiet;
		insertItemAt(anObject, index);
		m_keepQuiet = false;
	}
	public void insertNullItemAt(Object nullItem, int index) {
		m_nullItem = nullItem;
		m_nullIndex = index;
		insertItemAt(nullItem, index);
		setSelectedIndex(index);
	}
	public void insertNullItemAt(Object nullItem, int index, boolean quiet) {
		m_keepQuiet = quiet;
		insertNullItemAt(nullItem, index);
		m_keepQuiet = false;
	}
	public boolean isAsIs() {
		return m_asIs;
	}
	public boolean isQuiet() {
		return m_keepQuiet;
	}
	public void removeAllItems() {
		MutableComboBoxModel model = (MutableComboBoxModel) dataModel;
		if (model.getSize() > 0) {
			super.removeAllItems();
		}
	}
	public void removeAllItems(boolean quiet) {
		MutableComboBoxModel model = (MutableComboBoxModel) dataModel;
		if (model.getSize() > 0) {
			m_keepQuiet = quiet;
			removeAllItems();
			m_keepQuiet = false;
		}
	}
	public void removeItem(Object anObject, boolean quiet) {
		m_keepQuiet = quiet;
		removeItem(anObject);
		m_keepQuiet = false;
	}
	protected void selectedItemChanged() {
		if (m_nullItem == null) {
			super.selectedItemChanged();
		} else {
			fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, (selectedItemReminder == m_nullItem) ? null : selectedItemReminder, ItemEvent.DESELECTED));

			selectedItemReminder = getModel().getSelectedItem();

			fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, (selectedItemReminder == m_nullItem) ? null : selectedItemReminder, ItemEvent.SELECTED));
			fireActionEvent();
		}
	}
	public void setAsIs() {
		setSelectedIndex(0, true);
		m_asIs = true;
	}
	public void setNullItem(Object nullItem) {
		m_nullItem = nullItem;
		int size = getModel().getSize();
		for (int i = 0; i < size; i++) {
			if (m_nullItem.equals(getModel().getElementAt(i))) {
				m_nullIndex = i;
				break;
			}
		}
	}
	public void setQuiet(boolean q) {
		m_keepQuiet = q;
	}
	public void setSelectedIndex(int i) {
		if (i == m_nullIndex) {
			setSelectedItem(null);
		} else {
			super.setSelectedIndex(i);
		}
	}
	public void setSelectedIndex(int i, boolean quiet) {
		m_keepQuiet = quiet;

		setSelectedIndex(i);

		m_keepQuiet = false;
	}
	public void setSelectedItem(Object item) {
		if (m_asIs) {
			m_asIs = false;
			selectedItemReminder = m_asIsValue;
		}

		if ((m_nullItem != null) && (m_nullItem.equals(item))) {
			super.setSelectedItem(null);
		} else {
			super.setSelectedItem(item);
		}
	}
	public void setSelectedItem(Object anObject, boolean quiet) {
		m_keepQuiet = quiet;

		setSelectedItem(anObject);

		m_keepQuiet = false;
	}
}