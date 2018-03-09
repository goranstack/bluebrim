package com.bluebrim.observable;

import com.bluebrim.gui.client.*;


public class CoDefaultServerObjectListener extends CoServerObjectListener {

	public CoDefaultServerObjectListener(CoDomainUserInterface ui) {
		super(ui);

		//	ui.addValueListener( this );
	}
	public void changedServerObject(CoChangedObjectEvent e) {
		getUserInterface().valueHasChanged();
	}
}
