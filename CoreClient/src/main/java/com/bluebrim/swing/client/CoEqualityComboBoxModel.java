package com.bluebrim.swing.client;

import javax.swing.DefaultComboBoxModel;

/**
 * ComboBoxModel trying to select an item in the model if
 * one such which is equals() with the given object exists.
 *
 * Stolen and improved from CoStrokeComboBoxItem.
 *
 * PENDING: Seems to loop forever when no equal object is found in the model. Fix! Important!
 *
 * @author Markus Persson 2000-03-22
 */
public class CoEqualityComboBoxModel extends DefaultComboBoxModel {
public void setSelectedItem(Object obj) {
	if (obj != null) {
		int size = getSize();
		for (int i = 0; i < size; i++) {
			if (obj.equals(getElementAt(i))) {
				super.setSelectedItem(getElementAt(i));
				return;
			}
		}
	}
	super.setSelectedItem(obj);
}
}
