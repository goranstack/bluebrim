package com.bluebrim.gui.client;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;

import jozart.swingutils.*;

/**
 * A singleton of <code>CoWindowList</code> is keeping track of all open instances
 * of <code>CoFrame</code> and <code>CoDialog</code>.
 * <br>
 * Internally the window is kept in a collection as a part of a <code>WindowOwnerPair</code>
 * where the other part of the pair is the object <i>owning</i> the window, e g normally the
 * userinterface displayed in the window.
 * <br>
 * There are several static services implemented beyond <code>addWindow</code> 
 * and </code>removeWindow</code>
 * <ul>
 * <li><code>findWindowAt(Point)</code> which returns the uppermost window 
 * containing the window.
 * <li><code>findWindowFor(Object)</code> which returns the window <i>owned</i>
 * by the argument.
 * <li><code>updateWindows()</code> which is called after a change in L&F
 * </ul>
 * <br>
 * The services provided by this class are mainly used by the d&d classes and
 * when changing L&F.
 */
public class CoWindowList extends WindowAdapter {
	private static CoWindowList INSTANCE = new CoWindowList();
	private static Object DEFAULT_OWNER = new Object();

	private List m_windows = new ArrayList(10);

	private static class WindowOwnerPair {
		Window window;
		Object owner;
		public WindowOwnerPair(Window w, Object o) {
			window = w;
			owner = o;
		}
		public boolean equals(Object aPair) {
			WindowOwnerPair pair = (WindowOwnerPair) aPair;
			return ((pair.window == window) && (pair.owner == owner));
		}
	}

	private synchronized void _addWindow(Window aWindow, Object owner) {
		m_windows.add(new WindowOwnerPair(aWindow, owner));
		aWindow.addWindowListener(this);
	}

	/**
	 * Svara med det fönster som innehåller 'screenPoint' eller null
	 * om det inte finns något.
	 */
	private Window _findWindowAt(Point screenPoint) {
		Iterator windows = m_windows.iterator();
		while (windows.hasNext()) {
			Window iWindow = ((WindowOwnerPair) windows.next()).window;
			if (iWindow.getBounds().contains(screenPoint))
				return iWindow;
		}
		return null;
	}

	private Window _findWindowFor(Object owner) {
		Iterator windows = m_windows.iterator();
		while (windows.hasNext()) {
			WindowOwnerPair iPair = (WindowOwnerPair) windows.next();
			Object iOwner = iPair.owner;
			if (owner == iOwner)
				return iPair.window;
		}
		return null;
	}

	private WindowOwnerPair _findWindowOwnerPairFor(Window aWindow) {
		Iterator windows = m_windows.iterator();
		while (windows.hasNext()) {
			WindowOwnerPair iPair = (WindowOwnerPair) windows.next();
			if (iPair.window == aWindow)
				return iPair;
		}
		return null;
	}

	/**
	 * Svara med det fönster som innehåller 'screenPoint' eller null
	 * om det inte finns något.
	 */
	private WindowOwnerPair _findWindowOwnerPairFor(Window aWindow, Object o) {
		WindowOwnerPair pair = new WindowOwnerPair(aWindow, o);
		Iterator windows = m_windows.iterator();
		while (windows.hasNext()) {
			WindowOwnerPair iPair = (WindowOwnerPair) windows.next();
			if (iPair.equals(pair))
				return iPair;
		}
		return null;
	}

	private synchronized void _removeWindow(Window aWindow, Object owner) {
		WindowOwnerPair pair = _findWindowOwnerPairFor(aWindow, owner);
		if (pair != null)
			m_windows.remove(pair);
		aWindow.removeWindowListener(this);
	}

	private void _setFrontWindow(Window window) {
		WindowOwnerPair pair = _findWindowOwnerPairFor(window);
		if (pair != null) {
			m_windows.remove(pair);
			m_windows.add(0, pair);
		}
	}

	private void _updateWindows() {
		if (m_windows.isEmpty())
			return;

		int maxCount = 5 * m_windows.size() + 5;
		final ProgressMonitor monitor = new ProgressMonitor(null, "Uppdaterar ... ", null, 0, maxCount + 1);
		monitor.setMillisToPopup(0);
		monitor.setMillisToDecideToPopup(0);
		monitor.setProgress(5);

		SwingWorker worker = new SwingWorker() {
			public Object construct() {
				int counter = 0;
				Iterator iter = new ArrayList(m_windows).iterator();
				while (iter.hasNext()) {
					Window iWindow = ((WindowOwnerPair) iter.next()).window;
					SwingUtilities.updateComponentTreeUI(iWindow);
					updateProgress(monitor, counter += 5);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
				}
				return this;
			}
			public void finished() {
				if (getException() != null) {
					System.out.println("Exception occured ");
					getException().printStackTrace();
				}
				monitor.close();
			}
		};
		worker.start();

	}

	public static void addWindow(Window aWindow, Object owner) {
		INSTANCE._addWindow(aWindow, owner);
	}

	/**
	 * Svara med det fönster som innehåller 'screenPoint' eller null
	 * om det inte finns något.
	 */
	public static Window findWindowAt(Point screenPoint) {
		return INSTANCE._findWindowAt(screenPoint);
	}

	public static Window findWindowFor(Object owner) {
		return INSTANCE._findWindowFor(owner);
	}

	public static void removeWindow(Window aWindow, Object owner) {
		INSTANCE._removeWindow(aWindow, owner);
	}

	private void updateProgress(final ProgressMonitor monitor, final int i) {
		Runnable doSetProgressValue = new Runnable() {
			public void run() {
				monitor.setProgress(i);
			}
		};
		SwingUtilities.invokeLater(doSetProgressValue);
	}

	public static void updateWindows() {
		INSTANCE._updateWindows();
	}

	public void windowActivated(WindowEvent e) {
		_setFrontWindow((Window) e.getSource());
	}
}