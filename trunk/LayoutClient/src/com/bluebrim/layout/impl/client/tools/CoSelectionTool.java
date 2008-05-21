package com.bluebrim.layout.impl.client.tools;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import com.bluebrim.base.client.command.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.client.view.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.transact.shared.*;

/**
 * Implementation of "selection mode" (the default tool)
 * 
 * @author: Dennis Malmström
 */

public class CoSelectionTool extends CoAbstractTool {
	public static boolean DO_TRACE_HIT_TESTING = false;

	Point2D m_mousePosition;

	protected CoShapePageItemView m_activeView;
	public CoSelectionTool(CoLayoutEditor pageItemEditor) {
		super(null, pageItemEditor);
	}
	public void activate(Point2D pos) {
		super.activate(pos);

		m_mousePosition = pos;
		updateCursor();
	}

	private void createXCustomGridLine() {
		CoShapePageItemView v = getCurrentModelView();
		CoImmutableCustomGridIF g = v.getCustomGrid();

		if (g == null)
			return;

		m_viewPanel.untransform(m_mousePosition);
		v.transformFromGlobal(m_mousePosition);

		CoPageItemCommands.MOVE_CUSTOM_GRID_LINE.prepare(
			v.getMutableCustomGrid(),
			Double.NaN,
			m_mousePosition.getX(),
			Double.NaN,
			Double.NaN);

		m_editor.getCommandExecutor().doit(CoPageItemCommands.MOVE_CUSTOM_GRID_LINE, null);

		v.transformToGlobal(m_mousePosition);
		m_viewPanel.transform(m_mousePosition);

		m_viewPanel.repaint();
	}

	private void createYCustomGridLine() {
		CoShapePageItemView v = getCurrentModelView();
		CoImmutableCustomGridIF g = v.getCustomGrid();

		if (g == null)
			return;

		m_viewPanel.untransform(m_mousePosition);
		v.transformFromGlobal(m_mousePosition);

		CoPageItemCommands.MOVE_CUSTOM_GRID_LINE.prepare(
			v.getMutableCustomGrid(),
			Double.NaN,
			Double.NaN,
			Double.NaN,
			m_mousePosition.getY());
		m_editor.getCommandExecutor().doit(CoPageItemCommands.MOVE_CUSTOM_GRID_LINE, null);

		v.transformToGlobal(m_mousePosition);
		m_viewPanel.transform(m_mousePosition);

		m_viewPanel.repaint();
	}

	private boolean deleteCustomGridLine() {
		CoShapePageItemView v = getCurrentModelView();
		CoCustomGridIF g = v.getMutableCustomGrid();

		if (g == null)
			return false;

		double x = getCurrentXCustomGridLine(v);
		double y = getCurrentYCustomGridLine(v);

		if (!Double.isNaN(x)) {
			CoPageItemCommands.MOVE_CUSTOM_GRID_LINE.prepare(g, x, Double.NaN, Double.NaN, Double.NaN);
			m_editor.getCommandExecutor().doit(CoPageItemCommands.MOVE_CUSTOM_GRID_LINE, null);
			return true;
		} else
			if (!Double.isNaN(y)) {
				CoPageItemCommands.MOVE_CUSTOM_GRID_LINE.prepare(g, Double.NaN, Double.NaN, y, Double.NaN);
				m_editor.getCommandExecutor().doit(CoPageItemCommands.MOVE_CUSTOM_GRID_LINE, null);
				return true;
			} else {
				return false;
			}
	}

	private CoReshapeHandleIF getCurrentReshapeHandle() {
		CoReshapeHandleIF h = m_viewPanel.getRootView().getReshapeHandleContaining(m_mousePosition);

		return h;
	}

	protected CoShapePageItemView getCurrentView() {
		return getViewAt(m_mousePosition, false);
	}

	public CoTool keyPressed(KeyEvent e) {
		if (e.getModifiers() == 0) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_BACK_SPACE :
				case KeyEvent.VK_DELETE :
					if (deleteCurvePoint())
						e.consume();
					else
						if (deleteCustomGridLine())
							e.consume();
					updateCursor();
					break;

				case KeyEvent.VK_A :
				case KeyEvent.VK_INSERT :
					if (addCurvePoint(e))
						e.consume();
					updateCursor();
					break;

				case KeyEvent.VK_X :
					createXCustomGridLine();
					updateCursor();
					break;

				case KeyEvent.VK_Y :
					createYCustomGridLine();
					updateCursor();
					break;
					/*
					case KeyEvent.VK_F7 :
					m_editor.getCommandManager().undoit();
					break;
					*/
				case KeyEvent.VK_SUBTRACT :
					selectParent();
					updateCursor(); // JDK/JFC bug? Cursor isn't redrawn
					break;

				case KeyEvent.VK_ADD :
					selectChildren();
					updateCursor(); // JDK/JFC bug? Cursor isn't redrawn
					break;

				case KeyEvent.VK_DIVIDE :
					selectPreviousChild();
					updateCursor(); // JDK/JFC bug? Cursor isn't redrawn
					break;

				case KeyEvent.VK_MULTIPLY :
					selectNextChild();
					updateCursor(); // JDK/JFC bug? Cursor isn't redrawn
					break;

				default :
					break;
			}
		}

		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP :
				twitch(0, ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) ? -1 : -10);
				break;

			case KeyEvent.VK_DOWN :
				twitch(0, ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) ? 1 : 10);
				break;

			case KeyEvent.VK_LEFT :
				twitch(((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) ? -1 : -10, 0);
				break;

			case KeyEvent.VK_RIGHT :
				twitch(((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) ? 1 : 10, 0);
				break;

			default :
				break;
		}

		return this;
	}

	public CoTool mouseClicked(MouseEvent e) {
		switch (e.getClickCount()) {
			case 1 :
				return mouseClickedOnce(e);
			case 2 :
				return mouseClickedTwice(e);
			default :
				return this;
		}
	}

	private CoTool mouseClickedOnce(MouseEvent e) {
		return this;
	}

	private CoTool mouseClickedTwice(MouseEvent e) {
		double x = 0;
		double y = 0;

		m_editor.doubleClicked();

		return this;
	}

	public CoTool mouseMoved(MouseEvent e) {
		long t0 = 0;
		if (DO_TRACE_HIT_TESTING) {
			t0 = System.currentTimeMillis();
		}

		m_mousePosition = getLocation(e);
		updateCursor();

		if (DO_TRACE_HIT_TESTING) {
			long t = System.currentTimeMillis();
			System.err.print(t0 + ": mouse moved " + e.getX() + ", " + e.getY() + " : ");
			System.err.println(t + " (" + (t - t0) + " ms)");
		}

		return this;
	}

	public CoTool mousePressed(MouseEvent e) {
		m_activeView = null;

		m_mousePosition = getLocation(e);

		CoReshapeHandleIF handle = getCurrentReshapeHandle();
		if (handle != null) {
			// mouse pressed on reshape handle, pass control to reshape tracker
			if (isReshapeable(m_viewPanel.getRootView().getViewOwningReshapeHandle(handle))) {
				return new CoReshapeTool(
					this,
					m_editor,
					(e.getModifiers() & MouseEvent.ALT_MASK) != 0,
					(e.getModifiers() & MouseEvent.CTRL_MASK) != 0,
					m_viewPanel.getSelectionManager().getSelectedView(handle),
					handle);
			} else {
				return this;
			}
		}

		CoShapePageItemView view = getCurrentModelView();

		if (view.getCustomGrid() != null) {
			double x = getCurrentXCustomGridLine(view);
			if (!Double.isNaN(x)) {
				// mouse pressed on custom gridline
				return new CoMoveXCustomGridLineTool(this, m_editor, view, x, view.getHeight());
			}

			double y = getCurrentYCustomGridLine(view);
			if (!Double.isNaN(y)) {
				// mouse pressed on custom gridline
				return new CoMoveYCustomGridLineTool(this, m_editor, view, y, view.getWidth());
			}
		}

		view = getCurrentView();

		if // mouse pressed on selectable view
		 ((view != null) && (isSelectable(view))) {
			// search ancestor path for selected view
			CoShapePageItemView v = view;
			while (v != null) {
				if (m_viewPanel.getSelectionManager().isSelected(v) && isMoveable(v)) {
					view = v;
					break;
				}
				v = v.getParent();
			}

			if (!isMoveable(view)) {
				return new CoRubberbandSelectTool(m_editor, this);
			}

			// mouse pressed on movable view, pass control to appropriate move tracker
			boolean isSelected = m_viewPanel.getSelectionManager().isSelected(view);

			if (isSelected) {
				// mouse pressed on selected view, pass control to move-all-selected tracker
				return new CoMoveTool(
					this,
					m_editor,
					(e.getModifiers() & MouseEvent.ALT_MASK) != 0,
					view,
					m_viewPanel.getSelectionManager().getSelectedViews());
			} else {
				// mouse pressed on unselected view, pass control to move-a-view tracker
				return new CoMoveTool(this, m_editor, (e.getModifiers() & MouseEvent.ALT_MASK) != 0, view);
			}

		}

		// mouse pressed on non-movable view, pass control to rubberband-select tracker
		return new CoRubberbandSelectTool(m_editor, this);
	}

	protected void selectChildren() {
		CoShapePageItemView view = getCurrentView();

		if (view != null) {
			CoPageItemView top = m_viewPanel.getRootView();
			CoShapePageItemView v = view;
			while ((v != top)) //&& ( ! m_viewPanel.getRootView().isModelView( v ) ) )
				{
				if (m_viewPanel.getSelectionManager().isSelected(v)) {
					if (v instanceof CoCompositePageItemView) {
						CoCompositePageItemView V = (CoCompositePageItemView) v;

						m_viewPanel.getSelectionManager().beginSelectionTransaction();
						m_viewPanel.getSelectionManager().unselect(V);

						Iterator children = V.getChildren().iterator();
						while (children.hasNext()) {
							CoShapePageItemView c = (CoShapePageItemView) children.next();
							if (isSelectable(c))
								m_viewPanel.getSelectionManager().select(c);
						}
						m_viewPanel.getSelectionManager().endSelectionTransaction();
						m_viewPanel.repaint();

					}
					break;
				}
				v = v.getParent();
			}
		}
	}

	protected void selectNextChild() {
		CoShapePageItemView view = m_viewPanel.getSelectionManager().getSelectedView();
		if (view == null) {
			view = getCurrentView();
			if (!m_viewPanel.getSelectionManager().isSelected(view))
				view = null;
		}
		if (view == null) {
			view = m_activeView;
		}

		if (view != null) {
			CoCompositePageItemView p = view.getParent();

			int i = p.getIndexOfChild(view);
			if (i + 1 >= p.getChildCount())
				i = -1;

			m_activeView = p.getChildAt(i + 1);
			m_viewPanel.getSelectionManager().select(m_activeView, true);
		}
	}

	protected void selectParent() {
		CoShapePageItemView view = getCurrentView();

		if (view != null) {
			CoShapePageItemView top = m_viewPanel.getRootView();
			CoShapePageItemView v = view;
			while ((v != top) && (!m_viewPanel.getRootView().isModelView(v))) {
				if (m_viewPanel.getSelectionManager().isSelected(v)) {
					v = v.getParent();

					if (isSelectable(v)) {
						m_viewPanel.getSelectionManager().select(v);
						m_viewPanel.repaint();
					}

					break;
				}
				v = v.getParent();
			}
		}
	}

	protected void selectPreviousChild() {
		CoShapePageItemView view = m_viewPanel.getSelectionManager().getSelectedView();
		if (view == null) {
			view = getCurrentView();
			if (!m_viewPanel.getSelectionManager().isSelected(view))
				view = null;
		}
		if (view == null) {
			view = m_activeView;
		}

		if (view != null) {
			CoCompositePageItemView p = view.getParent();

			int i = p.getIndexOfChild(view);
			if (i - 1 < 0)
				i = p.getChildCount();

			m_activeView = p.getChildAt(i - 1);
			m_viewPanel.getSelectionManager().select(m_activeView, true);
		}
	}

	private void twitch(final double dx, final double dy) {
		CoUndoableCommand c = null;

		if (m_viewPanel.getSelectionManager().getSelectedViewCount() > 0) {
			CoCompoundUndoableCommand.INSTANCE.reset("TWITCH_PAGE_ITEMS");

			Iterator views = m_viewPanel.getSelectionManager().getSelectedViews();
			while (views.hasNext()) {
				CoShapePageItemView view = (CoShapePageItemView) views.next();
				CoCompoundUndoableCommand.INSTANCE.add(
					CoShapePageItemSetPositionCommand.create("", view, view.getX() + dx, view.getY() + dy));
			}
			c = CoCompoundUndoableCommand.INSTANCE;
		} else {
			final CoShapePageItemView view = getCurrentView();

			if (view != null) {
				CoPageItemCommands.SET_POSITION.prepare(view, view.getX() + dx, view.getY() + dy);
				c = CoPageItemCommands.SET_POSITION;
			}
		}

		if (c != null) {
			m_editor.getCommandExecutor().doit(c, null);
		}
	}

	private void updateCursor() {
		if (m_viewPanel.hasRootView()) {
			Cursor c;
			String statusText = "";
			CoRootView rootView = m_viewPanel.getRootView();

			if (m_mousePosition == null) {
				c = m_viewPanel.getRootView().getSelectionCursor();

			} else {
				// check if over handle
				CoReshapeHandleIF h = getCurrentReshapeHandle();
				if (h != null) {
					CoShapePageItemView v = rootView.getViewOwningReshapeHandle(h);
					if (v != rootView)
						statusText = v.getName();
					if (isReshapeable(v)) {
						c = rootView.getReshapeCursor();
					} else {
						c = rootView.getSelectionCursor();
					}

				} else {
					// check if over custom grid line
					CoShapePageItemView v = getCurrentModelView();

					double d = 0;
					if ((v.getCustomGrid() != null) && !Double.isNaN(d = getCurrentXCustomGridLine(v))) {
						c = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
					} else
						if ((v.getCustomGrid() != null) && !Double.isNaN(d = getCurrentYCustomGridLine(v))) {
							c = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
						} else {
							v = getCurrentView();
							if (v != rootView)
								statusText = v.getName();

							// check if over movable view
							if (isMoveable(v)) {
								c = v.getMoveCursor();
							} else {
								c = rootView.getSelectionCursor();
							}
						}
				}

				m_editor.setStatusText(statusText);

			}

			m_viewPanel.setCursor(c);
		}
	}

	private boolean addCurvePoint(KeyEvent e) {
		final CoShapePageItemView v = getCurrentView();

		if (v == null)
			return false;
		if (!(v.getCoShape() instanceof CoImmutableCurveShapeIF))
			return false;

		CoCurveShapeIF s = (CoCurveShapeIF) v.getCoShape().deepClone();

		CoReshapeHandleIF h = getCurrentReshapeHandle();
		int i = (h == null) ? -1 : s.getIndexOfPoint(h.getX(), h.getY());
		s.insertPoint(i);

		CoPageItemCommands.RESHAPE.prepare(v, s, false);
		m_editor.getCommandExecutor().doit(CoPageItemCommands.RESHAPE, null);

		return true;
	}

	private boolean deleteCurvePoint() {
		final CoReshapeHandleIF h = getCurrentReshapeHandle();

		if (h == null)
			return false;

		final CoShapePageItemView v = m_viewPanel.getRootView().getViewOwningReshapeHandle(h);

		if (!(v.getCoShape() instanceof CoImmutableCurveShapeIF))
			return false;

		CoCurveShapeIF s = (CoCurveShapeIF) v.getCoShape().deepClone();
		s.removePoint(h.getX(), h.getY());

		CoPageItemCommands.RESHAPE.prepare(v, s, false);
		m_editor.getCommandExecutor().doit(CoPageItemCommands.RESHAPE, null);

		return true;
	}

	protected CoShapePageItemView getCurrentModelView() {
		CoShapePageItemView v = m_viewPanel.getRootView().findModelViewContaining(m_mousePosition);
		return (v == null) ? m_viewPanel.getRootView() : v;
	}

	protected double getCurrentXCustomGridLine(CoShapePageItemView v) {
		return m_viewPanel.getRootView().findClosestXCustomGridLine(v, m_mousePosition, CoSelectedView.HANDLE_RANGE);
	}

	protected double getCurrentYCustomGridLine(CoShapePageItemView v) {
		return m_viewPanel.getRootView().findClosestYCustomGridLine(v, m_mousePosition, CoSelectedView.HANDLE_RANGE);
	}

	public String getName() {
		return CoPageItemUIStringResources.getName("SELECTION_TOOL");
	}

}