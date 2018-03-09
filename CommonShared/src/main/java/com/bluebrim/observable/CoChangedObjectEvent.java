package com.bluebrim.observable;

import java.util.EventObject;

public class CoChangedObjectEvent extends EventObject {
	Object changedObject;

	public CoChangedObjectEvent(Object source, Object changedObject) {
		super(source);
		this.changedObject = changedObject;
	}

	public Object getChangedObject() {
		return changedObject;
	}
}
