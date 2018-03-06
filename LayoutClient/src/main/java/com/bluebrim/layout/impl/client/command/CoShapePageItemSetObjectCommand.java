package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * 
 *
 * @author: Dennis Malmström
 */

public abstract class CoShapePageItemSetObjectCommand extends CoShapePageItemCommand {

	protected Object m_object;

	protected CoShapePageItemSetObjectCommand(String name) {
		super(name);
	}

	protected CoUndoCommand createUndoCommand(String name, CoShapePageItemView targetView, CoShapePageItemSetObjectCommand command, Object originalObject, Object newObject) {
		return new CoShapePageItemSetObjectUndoCommand(name, targetView.getShapePageItem(), command, originalObject, newObject);
	}

	public final CoUndoCommand doExecute() {
		CoUndoCommand undo = createUndoCommand(getName(), m_targetView, this, getObject(m_targetView), m_object);

		setObject(m_targetView.getShapePageItem(), m_object);

		return undo;
	}

	public abstract Object getObject(CoShapePageItemView targetView);

	public void prepare(CoShapePageItemView targetView, Object object) {
		prepare(targetView);
		m_object = object;
	}

	public abstract void setObject(CoShapePageItemIF target, Object o);
}