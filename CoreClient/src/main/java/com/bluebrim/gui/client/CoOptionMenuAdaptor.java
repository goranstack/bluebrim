package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.bluebrim.base.shared.CoSetValueException;
import com.bluebrim.swing.client.CoOptionMenu;

/**
  	A component adaptor used to connect a <code>CoOptionMenu</code> with a value model holding the
 	selected element. It's listening for <code>ItemEvents</code> from the combobox and updates the value model when the
 	user makes a selection in the option menu.
 	<br>
 	An instance is created by <code>CoUserInterfaceBuilder.createOptionMenuAdaptor()</code>.
 	
 */
public class CoOptionMenuAdaptor extends CoComponentAdaptor implements ItemListener, CoValueListener {
	private CoOptionMenu m_optionMenu;
	private CoValueModel m_valueModel;

	public CoOptionMenuAdaptor() {
	}

	public CoOptionMenuAdaptor(CoValueModel vm, CoOptionMenu om) {
		this();

		setValueModel(vm);
		setOptionMenu(om);
		updateOptionMenu();
	}

	protected Component getComponent() {
		return getOptionMenu();
	}

	public final CoOptionMenu getOptionMenu() {
		return m_optionMenu;
	}

	public final CoValueModel getValueModel() {
		return m_valueModel;
	}

	/**
	 	Användaren har selekterat ett nytt element i comboboxen.
	 	Sätt mitt värdeobjekts värde till den nya selekteringen.
	 */
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			try {
				getValueModel().setValue(e.getItem());
			} catch (CoSetValueException ex) {
				updateOptionMenu();
			}
		}
	}

	public final void setOptionMenu(CoOptionMenu om) {
		m_optionMenu = om;
		m_optionMenu.setEnabled(m_valueModel.isEnabled());
		m_optionMenu.addItemListener(this);
	}

	public final void setValueModel(CoValueModel valueModel) {
		m_valueModel = valueModel;
		m_valueModel.addValueListener(this);
	}

	public final void updateOptionMenu() {
		getOptionMenu().setSelectedItem(getValueModel().getValue());
		getOptionMenu().repaint();
	}

	public void valueChange(CoValueChangeEvent e) {
		Object tNewValue = e.getNewValue();
		Object tOldValue = e.getOldValue();
		if ((tNewValue != tOldValue) && ((tNewValue == null) || !e.getNewValue().equals(e.getOldValue())))
			updateOptionMenu();
	}
}