package com.bluebrim.swing.client;

import java.awt.AWTEvent;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.Timer;

import com.bluebrim.base.shared.CoStatusShower;

/**
 * Helper that automatically closes a window in a given time
 * if no keyboard or mouse action occurs in the window.
 * A countdown is provided - normally in the window title.
 * 
 * Earlier limitations has been worked out by using an
 * AWTEventListener. Attempts to use the glass pane of Swing
 * windows were never glitch-free and using a custom EventQueue
 * felt unflexible. (We use a WaitCursorEventQueue that this
 * would have to be incorporated into in that case.)
 * 
 * @author Markus Persson 2002-05-27
 */
public class CoAutoCloser implements AWTEventListener, ActionListener {
	private Window m_window;
	private CoStatusShower m_shower;
	private int m_secsLeft;
	private String m_title;
	private String m_prefix;
	private Timer m_timer = new Timer(1000, this);

	public CoAutoCloser(Window window, int seconds) {
		this(window, null, null, seconds);
	}

	public CoAutoCloser(Window window, String title, int seconds) {
		this(window, null, title, seconds);
	}

	/**
	 * The <code>prefix</code> is used as status prefix unless
	 * <code>shower</code> is null. Then it will be used as
	 * title and appended with " - " as status prefix. If it is
	 * null, it's value will be taken from the window title.
	 * (Messy, I know. Clean up somehow ... /Markus)
	 */
	public CoAutoCloser(Window window, CoStatusShower shower, String prefix, int seconds) {
		m_window = window;
		m_secsLeft = seconds;

		if (shower == null) {
			String title = null;
			if (window instanceof Dialog) {
				Dialog dialog = (Dialog) window;
				m_shower = new DialogShower(dialog);
				title = dialog.getTitle();
			} else if (window instanceof Frame) {
				Frame frame = (Frame) window;
				m_shower = new FrameShower(frame);
				title = frame.getTitle();
			} else {
				m_shower = new CoStatusShower.NullShower();
			}
			if (prefix == null) {
				prefix = title;
			}
			m_title = (prefix == null) ? "" : prefix;
			m_prefix = (prefix == null) ? "" : prefix + " - ";
		} else {
			m_prefix = (prefix == null) ? "" : prefix;
			m_shower = shower;
		}
	}

	/**
	 * Starts (or restarts) the timer. If the timer is zero the window will
	 * be closed immediately.
	 */
	public void start() {
		if (!hasTimeRunOut()) {
			m_timer.start();
			Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
		}
	}

	/**
	 * Stops the timer and restores the status (normally the window title).
	 * Normally this need not be invoked from outside this class. The timer
	 * can be restarted from the current time (in whole seconds) by invoking
	 * start() again.
	 */
	public void stop() {
		Toolkit.getDefaultToolkit().removeAWTEventListener(this);
		m_timer.stop();
		m_shower.showStatus(m_title);
	}

	/**
	 * Timer has ticked, meaning a second has passed.
	 */
	public void actionPerformed(ActionEvent e) {
		m_secsLeft--;
		if (hasTimeRunOut()) {
			stop();
		}
	}

	/**
	 * If the time has run out, close the window. Otherwise,
	 * update the status diplay.
	 * 
	 * @return true if time has run out.
	 */
	private boolean hasTimeRunOut() {
		if (m_secsLeft > 0) {
			m_shower.showStatus(m_prefix + "Closing in " + m_secsLeft
				+ ((m_secsLeft == 1) ? " second ..." : " seconds ..."));
			return false;
		} else {
			// Close the window
			m_window.setVisible(false);
			return true;
		}
	}

	/**
	 * Listen to events that have been dispatched and stop
	 * the countdown if a mouse has been pressed or a key <b>typed</b>
	 * within the window. That is when the window has focus.
	 * 
	 * A less efficient but probably equivalent implementation
	 * (needed for pre JDK 1.4) would be to replace the line
	 * <blockquote>
	 *		if (getKFM().getFocusedWindow() == m_window) {
	 * </blockquote>
	 * in the implementation with the line
	 * <blockquote>
	 *		if (SwingUtilities.getRoot((Component) e.getSource()) == m_window) {
	 * </blockquote>
	 * and remove the getKFM() method.
	 */
	public void eventDispatched(AWTEvent e) {
		switch (e.getID()) {
			case MouseEvent.MOUSE_PRESSED:
			case KeyEvent.KEY_TYPED:
//				if (SwingUtilities.getRoot((Component) e.getSource()) == m_window) {
				if (getKFM().getFocusedWindow() == m_window) {
					stop();
				}
				break;
		
			default:
				break;
		}
	}

	private static KeyboardFocusManager getKFM() {
		return KeyboardFocusManager.getCurrentKeyboardFocusManager();
	}

	private static class DialogShower implements CoStatusShower {
		private Dialog m_dialog;
		public DialogShower(Dialog dialog) {
			m_dialog = dialog;
		}
		public void showStatus(String currentStatus) {
			m_dialog.setTitle(currentStatus);
		}
	}

	private static class FrameShower implements CoStatusShower {
		private Frame m_frame;
		public FrameShower(Frame frame) {
			m_frame = frame;
		}
		public void showStatus(String currentStatus) {
			m_frame.setTitle(currentStatus);
		}
	}
}