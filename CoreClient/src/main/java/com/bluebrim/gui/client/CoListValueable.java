package com.bluebrim.gui.client;

import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
/**
 Interfaceklass för värdeobjektsklasser som jobbar med en lista som sitt värde.
 */
public interface CoListValueable extends CoValueable, ListSelectionListener {

	public interface Mutable extends CoListValueable {
		public void addElement(Object element);
		public void addElements(Object elements[]);
		public void removeElement(Object element);
		public void removeElements(Object elements[]);
	};
/**
 * @param l CoListSelectionListener
 */
public void addSelectionListener ( CoSelectionListener l);
/**
 * getSize method comment.
 */
public boolean contains(Object element);
public void elementHasChanged(Object source, Object element);
/**
 * getSize method comment.
 */
public Object getElementAt(int index);
public CoAbstractListModel getListModel();
/**
 * getSize method comment.
 */
public int getSize();
/**
 * getSize method comment.
 */
public int indexOf(Object element);
public boolean isHandlingTransactions();
public void listHasChanged(Object source);
/**
 * @param l CoSelectionListener
 */
public void removeSelectionListener ( CoSelectionListener l);
public void setModelFor(JList list);
}
