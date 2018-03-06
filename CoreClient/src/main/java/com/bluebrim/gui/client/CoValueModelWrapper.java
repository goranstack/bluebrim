package com.bluebrim.gui.client;

import java.beans.*;

/**
 * A <code>CoValueModelWrapper</code> wraps another <code>CoValueModel</code>.
 * All (almost) methods defined in <code>CoValueModel</code> are reimplemented
 * here to dispatch requests down to the wrapped value model.
 * <br>
 * The exceptions are <code>addValueChangeListener(CoValueChangeEvent) and 
 * <code>addEnableDisableListener(CoEnableDisableListener).
 * The reason for this is that in these cases the listeners (the textfield adaptors) need to
 * speak directly with the converter instead of the wrapped value model.
 */
public class CoValueModelWrapper extends CoValueModel implements CoEnableDisableListener, CoValueListener {
	protected CoValueModel m_client;

	public CoValueModelWrapper(CoValueModel valueModel) {
		super();
		setClient(valueModel);
	}

	public void enableDisable(CoEnableDisableEvent e) {
		m_client.setEnabled(e.enable());
	}

	protected CoValueModel getClient() {
		return m_client;
	}
	public Object getValue() {
		return getClient().getValue();
	}

	public CoValueModel getValueModel() {
		return getClient();
	}

	public String getValueName() {
		return getValueModel().getValueName();
	}

	private void setClient(CoValueModel client) {
		if (m_client == client)
			return;

		if (m_client != null) {
			m_client.removeValueListener(this);
			m_client.removeEnableDisableListener(m_clientEnableDisableListener);
		}

		m_client = client;

		if (m_client != null) {
			m_client.addValueListener(this);
			m_client.addEnableDisableListener(m_clientEnableDisableListener);
		}
	}

	public void setValue(Object newValue) {
		getClient().setValue(newValue);
	}

	public void valueChange(CoValueChangeEvent e) {
		fireValueChangeEvent(e.getOldValue(), e.getNewValue());
	}
	private CoEnableDisableListener m_clientEnableDisableListener = new CoEnableDisableListener() {
		public void enableDisable(CoEnableDisableEvent e) {
			Object listeners[] = listenerList.getListenerList();

			CoEnableDisableEvent ev = null;

			for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == CoEnableDisableListener.class) {
					if (ev == null) {
						ev = new CoEnableDisableEvent(CoValueModelWrapper.this, e.enable());
					}

					((CoEnableDisableListener) listeners[i + 1]).enableDisable(ev);
				}
			}
		}
	};
	public void addVetoableChangeListener(VetoableChangeListener l) {
		m_client.addVetoableChangeListener(l);
	}

	public void initValue(Object newValue) {
		m_client.initValue(newValue);
	}

	public boolean isEnabled() {
		return m_client.isEnabled();
	}

	public boolean isReadOnly() {
		return m_client.isReadOnly();
	}

	public void removeVetoableChangeListener(VetoableChangeListener l) {
		m_client.removeVetoableChangeListener(l);
	}

	public void setEnabled(boolean e) {
		m_client.setEnabled(e);
	}

	public Object validate(Object newValue) throws PropertyVetoException {
		return m_client.validate(newValue);
	}

	public void valueHasChanged() {
		m_client.valueHasChanged();
	}
}