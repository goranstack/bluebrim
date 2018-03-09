package com.bluebrim.gui.client;
import com.bluebrim.base.shared.*;

/**
 * A modest attempt to fight the subclassing hell of this framework ...
 *
 * @author Markus Persson 2001-10-10
 */
public class CoDefaultListAspectAdaptor extends CoAbstractListAspectAdaptor implements CoListValueable.Mutable {
	public CoDefaultListAspectAdaptor(CoValueable context, String name) {
		super(context, name);
	}


public CoDefaultListAspectAdaptor(CoValueable context, String name, boolean subjectFiresChange) {
	super(context, name, subjectFiresChange);
}

	public void addElement(Object element) {
		throw new UnsupportedOperationException();
	}


	public void addElements(Object elements[]) {
		throw new UnsupportedOperationException();
	}


/**
 * Should return a java.util.List representation of the list.
 * By default returns the subject, as the valueable could
 * hold it directly. Unfortunately, the framework uses the
 * annoying CoObjectIF interface, so this it akward.
 */
protected Object get(CoObjectIF subject) {
	return subject;
}


	public CoAbstractListModel getDefaultListModel() {
		return new CoCollectionListModel.Default(this);
	}


	public void removeElement(Object element) {
		throw new UnsupportedOperationException();
	}


	public void removeElements(Object elements[]) {
		throw new UnsupportedOperationException();
	}
}