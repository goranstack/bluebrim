package com.bluebrim.transact.shared;

import javax.swing.undo.*;

public abstract class CoAbstractUndoCommand extends AbstractUndoableEdit {

	public void abortRedo() {
	}

	public void abortUndo() {
	}

	public void finishRedo() {
	}

	public void finishUndo() {
	}

	public abstract String getName();

	public String getPresentationName() {
		return getName();
	}

	public String getRedoPresentationName() {
		return getPresentationName();
	}

	public String getUndoPresentationName() {
		return getPresentationName();
	}

	public void prepareRedo() {
	}

	public void prepareUndo() {
	}

	public abstract void setName(String name);

}