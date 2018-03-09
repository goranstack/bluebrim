package com.bluebrim.layout.impl.client.tools;

import java.awt.event.*;
import java.awt.geom.*;

/**
 * Tools are objects that handle input events in the layout editor.
 * A tool dispatcher (CoToolDispatcher) dispatches the events to the active tool.
 * Every call to an event handling method must return the next tool to become active.
 *
 * The event handling behavior of this class is to do nothing and return itself as the next active tool.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoTool {

	/**
	 * Hook called when tool becomes active
	 */
	public abstract void activate(Point2D pos);

	/**
	 * Hook called when tool becomes inactive
	 */
	public abstract void deactivate(Point2D pos);

	public final static Point2D getLocation(MouseEvent e) {
		return new Point2D.Double(e.getX(), e.getY());
	}

	public String getName() {
		return null;
	}

	public String getToolTipText() {
		return getName();
	}

	public CoTool keyPressed(KeyEvent e) {
		return this;
	}
	public CoTool keyReleased(KeyEvent e) {
		return this;
	}

	public CoTool keyTyped(KeyEvent e) {
		return this;
	}

	public CoTool mouseClicked(MouseEvent e) {
		return this;
	}

	public CoTool mouseDragged(java.awt.event.MouseEvent e) {
		return this;
	}

	public CoTool mouseEntered(java.awt.event.MouseEvent e) {
		return this;
	}

	public CoTool mouseExited(java.awt.event.MouseEvent e) {
		return this;
	}

	public CoTool mouseMoved(MouseEvent e) {
		return this;
	}

	public CoTool mousePressed(java.awt.event.MouseEvent e) {
		return this;
	}

	public CoTool mouseReleased(java.awt.event.MouseEvent e) {
		return this;
	}
}
