package com.bluebrim.gui.client;

/**
 * En eventklass som representerar en f�r�ndring av v�rdet i ett gr�nssnittsmodells v�rdeobjekt.
 * Genom att lyssna efter dessa event (genom att implementera CoValueModelListener) kan ett 
 * gr�nssnitt lyssna efter f�r�ndringar i ett annat gr�nssnitt som ligger i en subcanvas.
 */
public class CoValueModelChangeEvent extends CoValueChangeEvent {
	CoValueChangeEvent valueChangeEvent;

	public CoValueModelChangeEvent(CoUserInterface source, String propertyName, CoValueChangeEvent event) {
		super(source, propertyName, event.getOldValue(), event.getNewValue());
		valueChangeEvent = event;
	}

	public CoValueModelChangeEvent(CoUserInterface source, CoValueChangeEvent event) {
		super(source, event.getPropertyName(), event.getOldValue(), event.getNewValue());
		valueChangeEvent = event;
	}

	public CoValueModelChangeEvent(CoUserInterface source, CoValueModelChangeEvent event) {
		super(source, event.getPropertyName(), event.getOldValue(), event.getNewValue());
		valueChangeEvent = event.getValueChangeEvent();
	}

	public CoUserInterface getUserInterface() {
		return (CoUserInterface) getSource();
	}

	public CoValueChangeEvent getValueChangeEvent() {
		return valueChangeEvent;
	}

	public CoValueable getValueModel() {
		return getUserInterface().getNamedValueModel(getPropertyName());
	}
}
