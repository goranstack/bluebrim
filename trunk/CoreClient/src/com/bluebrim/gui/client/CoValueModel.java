package com.bluebrim.gui.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import javax.swing.event.EventListenerList;

/**
 	Abstrakt implementering av CoValueable. Har stöd för att registrera och meddela objekt
	som är intresserade av ändringar av dess värde. Metoderna för att hämta och sätta 
	värdet är dock fortfarande abstrakta.<br>
	Instansvariabler
	<ul>
	<li>	listenerList (EventListenerList) för de objekt som lyssnar efter ändringar av värdet
	<li>	name (String)för värdeobjektets namn
	</ul>
	@author Lasse Svadängs 971010
 */
public abstract class CoValueModel implements CoValueable {
	protected EventListenerList listenerList = new EventListenerList();
	private Object m_key;

	private boolean m_enabled = true;

	protected CoValueModel() {
	}

	public CoValueModel(Object key) {
		m_key = key;
	}

	public void addEnableDisableListener(CoEnableDisableListener l) {
		listenerList.add(CoEnableDisableListener.class, l);
	}

	public void addValueListener(CoValueListener l) {
		listenerList.add(CoValueListener.class, l);
	}

	public void addVetoableChangeListener(VetoableChangeListener l) {
		listenerList.add(VetoableChangeListener.class, l);
	}
	protected PropertyChangeEvent createVetoableChangeEvent(Object oldValue, Object newValue) {
		return new PropertyChangeEvent(this, getValueName(), oldValue, newValue);
	}
	protected void fireEnableDisableEvent() {
		Object listeners[] = listenerList.getListenerList();

		CoEnableDisableEvent e = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CoEnableDisableListener.class) {
				if (e == null) {
					e = new CoEnableDisableEvent(this, m_enabled);
				}

				((CoEnableDisableListener) listeners[i + 1]).enableDisable(e);
			}
		}
	}
	/**
	 	Värdet har ändrats och alla lyssnare skall meddelas om detta.
	 */
	protected void fireValueChangeEvent(Object oldValue, Object newValue) {
		fireValueChangeEvent(getValueName(), oldValue, newValue);
	}
	/**
	 	Värdet har ändrats och alla lyssnare skall meddelas om detta.
	 */
	protected void fireValueChangeEvent(String propertyName, Object oldValue, Object newValue) {
		Object tListeners[] = listenerList.getListenerList();
		CoValueChangeEvent tEvent = null;
		Class tListenerClass = CoValueListener.class;
		for (int i = tListeners.length - 2; i >= 0; i -= 2) {
			if (tListeners[i] == tListenerClass) {
				if (tEvent == null)
					tEvent = new CoValueChangeEvent(this, propertyName, oldValue, newValue);
				((CoValueListener) tListeners[i + 1]).valueChange(tEvent);
			}
		}

	}
	/**
	 	Värdet har ändrats och alla lyssnare skall meddelas om detta.
	 */
	protected void fireValueChangeEvent(CoValueChangeEvent e) {
		Object tListeners[] = listenerList.getListenerList();
		CoValueChangeEvent tEvent = null;
		Class tListenerClass = CoValueListener.class;
		for (int i = tListeners.length - 2; i >= 0; i -= 2) {
			if (tListeners[i] == tListenerClass) {
				((CoValueListener) tListeners[i + 1]).valueChange(e);
			}
		}

	}
	/**
	 	Värdet skall ändras och alla som validera det nya värdet skall meddelas om detta.
	 */
	protected void fireVetoableChangeEvent(Object oldValue, Object newValue) throws PropertyVetoException {
		Object tListeners[] = listenerList.getListenerList();
		PropertyChangeEvent tEvent = null;
		Class tListenerClass = VetoableChangeListener.class;
		for (int i = tListeners.length - 2; i >= 0; i -= 2) {
			if (tListeners[i] == tListenerClass) {
				if (tEvent == null)
					tEvent = createVetoableChangeEvent(oldValue, newValue);
				((VetoableChangeListener) tListeners[i + 1]).vetoableChange(tEvent);
			}
		}

	}
	
	/**
	 	Svarar med det aktuella värdet, konverterat till Object.
	 */
	public abstract Object getValue();
	
	// PENDING: Keys can be non-strings. Problem with events ...
	public String getValueName() {
		return (m_key instanceof String) ? (String) m_key : null;
	}

	public Object getKey() {
		return m_key;
	}

	/**
	 * As default just calls <code>setValue</code>.
	 */
	public void initValue(Object newValue) {
		setValue(newValue);
	}

	public boolean isEnabled() {
		return m_enabled;
	}

	public boolean isReadOnly() {
		return false;
	}

	public void removeEnableDisableListener(CoEnableDisableListener l) {
		listenerList.remove(CoEnableDisableListener.class, l);
	}
	public void removeValueListener(CoValueListener l) {
		listenerList.remove(CoValueListener.class, l);
	}

	public void removeVetoableChangeListener(VetoableChangeListener l) {
		listenerList.remove(VetoableChangeListener.class, l);
	}
	public void setEnabled(boolean e) {
		if (e == m_enabled)
			return;
		m_enabled = e;
		fireEnableDisableEvent();
	}

	/**
		Sätter värdet till 'newValue'.
	 */
	public abstract void setValue(Object newValue);

	/**
	 	The value is about to be changed to <code>newValue</code>. Give all
	 	that have shown interest a chance to veto the change.
	 	Return the new value if no one vetoes.
	*/
	public Object validate(Object newValue) throws PropertyVetoException {
		fireVetoableChangeEvent(getValue(), newValue);
		return newValue;
	}

	/**
		En villkorslös triggning av fireValueChanged.
	 */
	public void valueHasChanged() {
		fireValueChangeEvent(null, getValue());
	}
}