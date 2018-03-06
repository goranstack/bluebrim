package com.bluebrim.layout.impl.client.command;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Command for reparenting page items.
 *
 * Important: This class is more than just a convenience class, it is THE way to reparent page items.
 *            All reparenting should be performed by this class or the class CoPageItemsReparentCommand.
 *
 * Also see CoPageItemsReparentCommand
 *
 * @author: Dennis Malmström
 */

public class CoShapePageItemReparentUndoCommand extends CoUndoCommand {
	protected CoShapePageItemIF m_child;

	protected CoCompositePageItemIF m_oldParent;
	protected CoCompositePageItemIF m_newParent;
	protected CoPoint2DDouble m_position;

	// undo state
	protected double m_originalX;
	protected double m_originalY;

	public CoShapePageItemReparentUndoCommand(String name, CoShapePageItemIF child, CoCompositePageItemIF oldParent, CoCompositePageItemIF newParent, CoPoint2DDouble pos) {
		super(name);

		m_child = child;
		m_oldParent = oldParent;
		m_newParent = newParent;
		m_position = pos;

		m_originalX = m_child.getX();
		m_originalY = m_child.getY();

		/*
		if
			( oldParent != null )
		{
			m_originalZIndex = oldParent.getIndexOfChild( child );
			m_originalLayoutIndex = oldParent.getLayoutIndexOfChild( child );
		} else {
			m_originalZIndex = -1;
			m_originalLayoutIndex = -1;
		}
		*/
	}

	public boolean doRedo() {
		if (m_child.isSlave()) {
			// slaves can't be reparented
			return false;
		}

		if (m_newParent == null) {
			// no new parent -> remove
			m_oldParent.remove(m_child);
		} else {

			if (m_position != null) {
				// save position for undo
				m_originalX = m_child.getX();
				m_originalY = m_child.getY();
			}

			m_newParent.add(m_child);
			if (m_position != null)
				m_child.setPosition(m_position.getX(), m_position.getY());
		}

		return true;
	}

	public boolean doUndo() {
		if (m_child.isSlave()) {
			// slaves can't be reparented
			return false;
		}

		if (m_oldParent == null) {
			// no old parent -> it was an pure add
			m_newParent.remove(m_child);
		} else {
			m_oldParent.add(m_child);

			if (m_position != null) {
				// restore position
				m_child.setPosition(m_originalX, m_originalY);
			}
		}

		return true;
	}
}