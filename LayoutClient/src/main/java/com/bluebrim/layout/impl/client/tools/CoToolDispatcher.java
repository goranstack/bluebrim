package com.bluebrim.layout.impl.client.tools;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

/**
 * CoToolDispatcher is responsible for maintaining a set of tools (see CoTool)
 * and dispatching input events to the active one.
 * 
 * @author: Dennis Malmström
 */

public class CoToolDispatcher implements MouseListener, MouseMotionListener, KeyListener {

	private static final int m_buttonMask = MouseEvent.BUTTON1_MASK;
	private List m_tools = new ArrayList(); // [ CoTool ], set of tools
	private CoTool m_activeTool; // the active tool
	private CoTool m_defaultTool; // the deafult tool, if any
	Point2D m_mousePosition; // from latest mouse event
	EventListenerList m_listeners = new EventListenerList();

	public interface Listener extends EventListener {
		void activeToolChanged(CoTool oldTool, CoTool newTool);
	}

	/**
	 * Make a tool the active one
	 */
	private void changeTool(CoTool tool) {
		if (tool == null)
			tool = m_defaultTool;

		if (tool == m_activeTool)
			return;

		// deactive previous active tool
		if (m_activeTool != null)
			m_activeTool.deactivate(m_mousePosition);
		CoTool old = m_activeTool;

		// activate new active tool
		m_activeTool = tool;
		m_activeTool.activate(m_mousePosition);

		// notify listeners
		fireActiveToolChanged(old, m_activeTool);
	}

	public CoTool getActiveTool() {
		return m_activeTool;
	}

	public Point2D getMousePosition() {
		return m_mousePosition;
	}

	/**
	 * Dispatch input event to active tool
	 */
	public void keyPressed(KeyEvent e) {
		if (!((Component) e.getSource()).isEnabled())
			return;

		if (m_activeTool != null)
			changeTool(m_activeTool.keyPressed(e));
	}

	/**
	 * Dispatch input event to active tool
	 */
	public void keyReleased(KeyEvent e) {
		if (!((Component) e.getSource()).isEnabled())
			return;

		if (m_activeTool != null)
			changeTool(m_activeTool.keyReleased(e));
	}

	/**
	 * Dispatch input event to active tool
	 */
	public void keyTyped(KeyEvent e) {
		if (!((Component) e.getSource()).isEnabled())
			return;

		if (m_activeTool != null)
			changeTool(m_activeTool.keyTyped(e));
	}

	/**
	 * Dispatch input event to active tool
	 */
	public void mouseClicked(MouseEvent e) {
		if (!((Component) e.getSource()).isEnabled())
			return;

		e = transform(e);

		m_mousePosition = CoAbstractTool.getLocation(e);

		if (SwingUtilities.isLeftMouseButton(e)) {
			if (m_activeTool != null)
				changeTool(m_activeTool.mouseClicked(e));
		}
	}

	/**
	 * Dispatch input event to active tool
	 */
	public void mouseDragged(java.awt.event.MouseEvent e) {
		if (!((Component) e.getSource()).isEnabled())
			return;

		e = transform(e);

		m_mousePosition = CoAbstractTool.getLocation(e);
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (m_activeTool != null)
				changeTool(m_activeTool.mouseDragged(e));
		}
	}

	/**
	 * Dispatch input event to active tool
	 */
	public void mouseEntered(java.awt.event.MouseEvent e) {
		if (!((Component) e.getSource()).isEnabled())
			return;

		e = transform(e);

		m_mousePosition = CoAbstractTool.getLocation(e);

		if (m_activeTool != null)
			changeTool(m_activeTool.mouseEntered(e));
	}

	/**
	 * Dispatch input event to active tool
	 */
	public void mouseExited(java.awt.event.MouseEvent e) {
		if (!((Component) e.getSource()).isEnabled())
			return;

		e = transform(e);

		m_mousePosition = CoAbstractTool.getLocation(e);

		if (m_activeTool != null)
			changeTool(m_activeTool.mouseExited(e));
	}

	/**
	 * Dispatch input event to active tool
	 */
	public void mouseMoved(MouseEvent e) {
		if (!((Component) e.getSource()).isEnabled())
			return;

		e = transform(e);

		m_mousePosition = CoAbstractTool.getLocation(e);

		if (m_activeTool != null)
			changeTool(m_activeTool.mouseMoved(e));
	}

	/**
	 * Dispatch input event to active tool
	 */
	public void mousePressed(MouseEvent e) {
		if (!((Component) e.getSource()).isEnabled())
			return;

		e = transform(e);

		m_mousePosition = CoAbstractTool.getLocation(e);

		if (SwingUtilities.isLeftMouseButton(e)) {
			if (m_activeTool != null)
				changeTool(m_activeTool.mousePressed(e));
		}
	}

	/**
	 * Dispatch input event to active tool
	 */
	public void mouseReleased(MouseEvent e) {
		if (!((Component) e.getSource()).isEnabled())
			return;

		e = transform(e);

		m_mousePosition = CoAbstractTool.getLocation(e);

		if (SwingUtilities.isLeftMouseButton(e)) {
			if (m_activeTool != null)
				changeTool(m_activeTool.mouseReleased(e));
		}
	}

	public void nextTool() {
		int i = m_tools.indexOf(m_activeTool);
		if (i + 1 < m_tools.size()) {
			setActiveTool(i + 1);
		} else {
			setActiveTool(0);
		}
	}

	public void previousTool() {
		int i = m_tools.indexOf(m_activeTool);
		if (i - 1 >= 0) {
			setActiveTool(i - 1);
		} else {
			setActiveTool(m_tools.size() - 1);
		}
	}

	public void setActiveTool(int i) {
		setActiveTool((CoTool) m_tools.get(i));
	}

	/**
	 * The tool needn't be managed by the dispatcher
	 */
	public void setActiveTool(CoTool tool) {
		changeTool(tool);
	}

	public void setDefaultTool(CoTool tool) {
		m_defaultTool = tool;
	}

	private MouseEvent transform(MouseEvent e) {
		return e;
	}

	public void addListener(CoToolDispatcher.Listener l) {
		m_listeners.add(CoToolDispatcher.Listener.class, l);
	}

	protected void fireActiveToolChanged(CoTool oldTool, CoTool newTool) {
		Object listeners[] = m_listeners.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CoToolDispatcher.Listener.class) {
				((CoToolDispatcher.Listener) listeners[i + 1]).activeToolChanged(oldTool, newTool);
			}
		}

	}

	/**
	 * Add a tool to set of managed tools
	 */
	public void manageTool(CoTool tool) {
		m_tools.add(tool);
		//	m_nameToToolMap.put( name, tool );
		//	m_toolToNameMap.put( tool, name );
	}

	public void removeListener(CoToolDispatcher.Listener l) {
		m_listeners.remove(CoToolDispatcher.Listener.class, l);
	}

	/**
	 * Remove a tool from set of managed tools
	 */
	public void unmanageTool(CoTool tool) {
		if (tool == null)
			return;

		if (m_activeTool == tool) {
			setActiveTool(null);
		}

		m_tools.remove(tool);
		//	m_nameToToolMap.remove( m_toolToNameMap.get( tool ) );
		//	m_toolToNameMap.remove( tool );
	}
}