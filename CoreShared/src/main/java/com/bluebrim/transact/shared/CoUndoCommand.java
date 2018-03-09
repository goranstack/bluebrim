package com.bluebrim.transact.shared;

public abstract class CoUndoCommand extends CoAbstractUndoCommand {
	private String m_name;

	public CoUndoCommand(String name) {
		m_name = name;
	}

	public void abortRedo() {
	}

	public void abortUndo() {
	}

	public abstract boolean doRedo();

	public abstract boolean doUndo();

	public void finishRedo() {
	}

	public void finishUndo() {
	}

	public String getName() {
		return m_name;
	}

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

	public final void redo() {
		super.redo();

		prepareRedo();

		if (!doRedo()) {
			finishRedo();
		} else {
			abortRedo();
		}
	}

	public void setName(String name) {
		m_name = name;
	}

	public final void undo() {
		super.undo();

		prepareUndo();

		if (!doUndo()) {
			finishUndo();
		} else {
			abortUndo();
		}
	}
}
