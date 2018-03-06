package com.bluebrim.base.shared;


/**
	An abstract class that implements <code>CoFactoryElementIF</code>. 
	<code>CoObject</code>, or <code>CoSimpleObject</code>, should be used as superclass for
	all business object that are not taken from a 3rd party library. 
	Classes from such libraries can instead be subclassed, implementing <code>CoFactoryElementIF</code>.
 */
public abstract class CoObject implements CoFactoryElementIF {
	CoEventListenerList listenerList = null;

	/**
	 	Registrerat objekt som lyssnar efter CoPropertyChangeEvents, dvs event som skickas 
	 	när en egenskap hos ett verksamhetsobjekt ändras.
	 */
	public void addPropertyChangeListener(CoPropertyChangeListener l) {
		if (listenerList == null)
			listenerList = new CoEventListenerList();
		listenerList.add(CoPropertyChangeListener.class, l);
	}

	/**
	 	Skickar ett CoPropertyChangeEvent när element har lagts till en lista (Vector). 
	 	Eventet kommer att ha null som 'oldValue' och som 'newValue', en array som innehåller index för första 
	 	och sista elementet för de element som har lagts till. 
	 */
	public void fireAddToListPropertyChange(Object source, String propertyName, int firstIndex, int lastIndex) {
		int tArray[] = { firstIndex, lastIndex };
		firePropertyChange(source, propertyName, CoPropertyChangeEvent.ADD_ID, null, tArray);
	}

	public void firePropertyChange(
		Object source,
		String propertyName,
		String changeID,
		Object oldValue,
		Object newValue) {

		/*	if(CoAssertion.TRACE) {
				if(oldValue == newValue || oldValue != null && oldValue.equals(newValue)) {
					CoAssertion.trace("Ignoring property change event for equivalent objects (" + oldValue + ", " + newValue + ") triggered by " + this.getClass().getName());
				}
			}*/
		if (oldValue == newValue || oldValue != null && oldValue.equals(newValue)) {
			return;
		}
		Object targets[];
		synchronized (this) {
			if (listenerList == null) {
				return;
			}
			targets = (Object[]) listenerList.getListenerList().clone();
		}
		CoPropertyChangeEvent evt = new CoPropertyChangeEvent(source, this, propertyName, changeID, oldValue, newValue);

		for (int i = 0; i < targets.length; i += 2) {
			if ((Class) targets[i] == CoPropertyChangeListener.class) {
				CoPropertyChangeListener target = (CoPropertyChangeListener) targets[i + 1];
				target.propertyChange(evt);
			}
		}
	}

	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		firePropertyChange(this, propertyName, CoPropertyChangeEvent.NO_ID, oldValue, newValue);
	}

	/**
	 	Skickar ett CoPropertyChangeEvent när element har tagits bort från en lista (Vector). 
	 	Eventet kommer att ha null som 'oldValue' och som 'newValue', en array som innehåller index för första 
	 	och sista elementet för de element som har tagits bort. 
	 */
	public void fireRemoveFromListPropertyChange(Object source, String propertyName, int firstIndex, int lastIndex) {
		int tArray[] = { firstIndex, lastIndex };
		firePropertyChange(source, propertyName, CoPropertyChangeEvent.REMOVE_ID, null, tArray);
	}

	public String getIconName() {
		return "DefaultIcon.gif";
	}

	public String getSmallIconName() {
		return getIconName();
	}

	public void removePropertyChangeListener(CoPropertyChangeListener l) {
		if (listenerList != null)
			listenerList.remove(CoPropertyChangeListener.class, l);
	}

	public String toString() {
		String tmp = super.toString();
		tmp = tmp.substring(tmp.lastIndexOf('.') + 1);
		return tmp;
	}

	/**
	 * While waiting for refactorization...
	 */
	public String getFactoryKey() {
		return null;
	}

}
