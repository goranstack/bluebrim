package com.bluebrim.layout.impl.client.command;

import java.awt.geom.*;

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

public class CoShapePageItemReparentCommand extends CoUndoableCommand {
	protected CoShapePageItemIF m_child;

	protected CoCompositePageItemIF m_oldParent;
	protected CoCompositePageItemIF m_newParent;
	protected CoPoint2DDouble m_position;

	protected CoRef m_newParentId;
	protected CoRef m_oldParentId;

	public CoShapePageItemReparentCommand() {
		super("REPARENT PAGE ITEM");
	}

	public final CoUndoCommand doExecute() {
		if ((m_oldParent != null) && (m_newParent != null)) {
			if ((m_oldParent != null) && (m_oldParentId == null)) {
				m_oldParentId = m_oldParent.getId();
			}

			if ((m_newParent != null) && (m_newParentId == null)) {
				m_newParentId = m_newParent.getId();
			}

			if (m_newParentId.equals(m_oldParentId)) {
				// no operation
				return null;
			}
		}

		if (m_child.isSlave()) {
			// slaves can't be reparented
			return null;
		}

		CoUndoCommand undo = new CoShapePageItemReparentUndoCommand(getName(), m_child, m_oldParent, m_newParent, m_position);

		if (m_newParent == null) {
			// no new parent -> remove
			m_oldParent.remove(m_child);
		} else {
			m_newParent.add(m_child, m_position);
		}

		return undo;
	}

	public void prepare(String name, CoShapePageItemIF child, CoCompositePageItemIF oldParent, com.bluebrim.transact.shared.CoRef oldParentId, CoCompositePageItemIF newParent, com.bluebrim.transact.shared.CoRef newParentId) {
		prepare(name, child, oldParent, oldParentId, newParent, newParentId, null);
	}

	public void prepare(String name, CoShapePageItemIF child, CoCompositePageItemIF oldParent, com.bluebrim.transact.shared.CoRef oldParentId, CoCompositePageItemIF newParent, com.bluebrim.transact.shared.CoRef newParentId, Point2D pos) {
		prepare(name, child, oldParent, oldParentId, newParent, newParentId, (pos == null) ? null : new CoPoint2DDouble(pos));
	}

	public void prepare(String name, CoShapePageItemIF child, CoCompositePageItemIF oldParent, com.bluebrim.transact.shared.CoRef oldParentId, CoCompositePageItemIF newParent, com.bluebrim.transact.shared.CoRef newParentId, CoPoint2DDouble pos) {
		setName(name);

		m_child = child;

		m_oldParentId = oldParentId;
		m_newParentId = newParentId;

		m_oldParent = oldParent;
		m_newParent = newParent;
		m_position = pos;
	}

}