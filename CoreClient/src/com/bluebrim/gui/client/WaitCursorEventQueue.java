package com.bluebrim.gui.client;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.awt.MenuContainer;

import javax.swing.SwingUtilities;

/**
 * An EventQueue subclass that automatically displays
 * a mouse wait cursor if any event takes too long to
 * dispatch.
 * 
 * See <a href="http://www.javaworld.com/javaworld/javatips/jw-javatip87.html">
 * Java Tip 87</a>.
 * 
 * @author Kyle Davis
 * @author Markus Persson 2002-05-28
 */
public class WaitCursorEventQueue extends EventQueue {
	private int delay;
	private WaitCursorTimer waitTimer;
	
	public WaitCursorEventQueue(int delay) {
		this.delay = delay;
		waitTimer = new WaitCursorTimer();
		waitTimer.setDaemon(true);
		waitTimer.start();
	}
	
	protected void dispatchEvent(AWTEvent event) {
		waitTimer.startTimer(event.getSource());
		try {
			super.dispatchEvent(event);
		} finally {
			waitTimer.stopTimer();
		}
	}

	private class WaitCursorTimer extends Thread {
		private Object source;
		private Component parent;
		
		synchronized void startTimer(Object source) {
			this.source = source;
			notify();
		}
		
		synchronized void stopTimer() {
			if (parent == null)
				interrupt();
			else {
				parent.setCursor(null);
				parent = null;
			}
		}
		
		public synchronized void run() {
			while (true) {
				try {
					//wait for notification from startTimer()
					wait();

					//wait for event processing to reach the threshold, or
					//interruption from stopTimer()
					wait(delay);
					if (source instanceof Component)
						parent = SwingUtilities.getRoot((Component) source);
					else if (source instanceof MenuComponent) {
						MenuContainer mParent = ((MenuComponent) source).getParent();
						if (mParent instanceof Component)
							parent = SwingUtilities.getRoot((Component) mParent);
					}
					if ((parent != null) && parent.isShowing())
						parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				} catch (InterruptedException ie) {
				}
			}
		}
	}
}