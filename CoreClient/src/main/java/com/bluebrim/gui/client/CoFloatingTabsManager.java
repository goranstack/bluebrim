package com.bluebrim.gui.client;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.bluebrim.menus.client.*;

/**
 * Implements "floating" tabs in CoServerTabUI.
 * PENDING: handle context accepting UIs properly
 * Creation date: (2001-08-16 17:11:35)
 * @author Dennis
 */
public class CoFloatingTabsManager implements ContainerListener, WindowListener {
	private final boolean m_useFrames;

	private Action m_openAction;
	private Action m_restoreAction;

	private final CoAbstractTabUI m_serverTabUI;
	private CoPopupMenu m_popupMenu;

	private CoSubcanvas m_target;
	private List m_windows = new ArrayList();

	private int m_indexOfTab = -1;

	private static class MyDialog extends CoDialog {
		public CoAbstractUserInterfaceData m_uiData;

		public MyDialog(Frame f, String title, CoAbstractUserInterfaceData uiData) {
			super(f, false, null);
			init(title, uiData);
		}

		public MyDialog(Dialog d, String title, CoAbstractUserInterfaceData uiData) {
			super(d, false, null);
			init(title, uiData);
		}

		private void init(String title, CoAbstractUserInterfaceData uiData) {
			setTitle(title);
			m_uiData = uiData;
			setDefaultCloseOperation(HIDE_ON_CLOSE);
			getContentPane().add(uiData.getUserInterface().getPanel());
		}
	}

	/**
	 *  Grr, is a new JFrame subclass really needed here?
	 *  Dennis may have some explaining to do ...  /Markus
	 */
	private static class MyFrame extends JFrame {
		public CoAbstractUserInterfaceData m_uiData;

		public MyFrame(String title, CoAbstractUserInterfaceData uiData) {
			super(title);
			m_uiData = uiData;
			setDefaultCloseOperation(HIDE_ON_CLOSE);
			// Replacing the content pane instead of adding into it. /Markus
			setContentPane(uiData.getUserInterface().getPanel());
			CoGUI.brand(this);
		}
	}

	public CoFloatingTabsManager(CoAbstractTabUI stui, CoMenuBuilder mb, boolean useFrames) {
		super();

		m_useFrames = useFrames;

		m_serverTabUI = stui;

		m_openAction = new AbstractAction("Öppna i eget fönster") {
			public void actionPerformed(ActionEvent ev) {
				doit(ev);
			}
		};

		m_restoreAction = new AbstractAction("Återställ alla") {
			public void actionPerformed(ActionEvent ev) {
				restore(ev);
			}
		};

		m_popupMenu = mb.createPopupMenu();
		mb.addPopupMenuItem(m_popupMenu, m_openAction);
		mb.addPopupMenuItem(m_popupMenu, m_restoreAction);

		m_serverTabUI.getTabPane().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ev) {
				if (ev.isPopupTrigger())
					postPopupMenu(ev);
			}
			public void mousePressed(MouseEvent ev) {
				if (ev.isPopupTrigger())
					postPopupMenu(ev);
			}
			public void mouseReleased(MouseEvent ev) {
				if (ev.isPopupTrigger())
					postPopupMenu(ev);
			}
		});

		m_openAction.setEnabled(m_serverTabUI.getTabPane().getTabCount() > 1);
		m_restoreAction.setEnabled(!m_windows.isEmpty());

		m_serverTabUI.getTabPane().addContainerListener(this);
	}
	public void componentAdded(java.awt.event.ContainerEvent e) {
		m_openAction.setEnabled(m_serverTabUI.getTabPane().getTabCount() > 1);
	}
	public void componentRemoved(java.awt.event.ContainerEvent e) {
		m_openAction.setEnabled(m_serverTabUI.getTabPane().getTabCount() > 1);
	}
	private void doit(ActionEvent ev) {
		if (m_indexOfTab == -1)
			return;

		Container c = m_target.getTopLevelAncestor();
		String title = m_serverTabUI.getTabPane().getTitleAt(m_indexOfTab);

		if (m_useFrames) {
			MyFrame f = new MyFrame(title, removeTab());

			m_windows.add(f);
			m_restoreAction.setEnabled(!m_windows.isEmpty());
			m_openAction.setEnabled(m_serverTabUI.getTabPane().getTabCount() > 1);
			f.addWindowListener(this);
			f.pack();
			f.show();
		} else {

			MyDialog d = null;
			if (c instanceof Frame) {
				d = new MyDialog((Frame) c, title, removeTab());
			} else if (c instanceof Dialog) {
				d = new MyDialog((Dialog) c, title, removeTab());
			}

			if (d != null) {
				m_windows.add(d);
				m_restoreAction.setEnabled(!m_windows.isEmpty());
				m_openAction.setEnabled(m_serverTabUI.getTabPane().getTabCount() > 1);
				d.addWindowListener(this);
				d.pack();
				d.show();
			}
		}
	}
	private void postPopupMenu(MouseEvent e) {
		m_indexOfTab = -1;

		int I = m_serverTabUI.getTabPane().getComponentCount();
		for (int i = 0; i < I; i++) {
			Rectangle r = m_serverTabUI.getTabPane().getBoundsAt(i);
			if (r.contains(e.getX(), e.getY())) {
				Component c = m_serverTabUI.getTabPane().getComponentAt(i);
				if (c instanceof CoSubcanvas) {
					m_indexOfTab = i;
					m_target = (CoSubcanvas) c;
					m_popupMenu.show(m_serverTabUI.getTabPane(), e.getX(), e.getY());
					e.consume();
				}

				break;
			}
		}

	}
	private CoAbstractUserInterfaceData removeTab() {
		CoAbstractUserInterfaceData uiData = m_serverTabUI.getTabDataAt(m_indexOfTab);
		CoDomainUserInterface ui = uiData.getUserInterface();
		com.bluebrim.base.shared.CoObjectIF domain = ui.getDomain();

		m_serverTabUI.hideTab(uiData);

		ui.setDomain(domain);

		return uiData;
	}
	private void restore(ActionEvent ev) {
		Iterator i = m_windows.iterator();

		if (m_useFrames) {
			while (i.hasNext()) {
				MyFrame f = (MyFrame) i.next();
				f.hide();
				m_serverTabUI.showTab(f.m_uiData);
				f.dispose();
			}
		} else {
			while (i.hasNext()) {
				MyDialog d = (MyDialog) i.next();
				d.hide();
				m_serverTabUI.showTab(d.m_uiData);
				d.dispose();
			}
		}

		m_restoreAction.setEnabled((m_windows != null) && !m_windows.isEmpty());

		m_windows.clear();
	}
	/**
	 * Invoked when the window is set to be the user's
	 * active window, which means the window (or one of its
	 * subcomponents) will receive keyboard events.
	 */
	public void windowActivated(WindowEvent e) {
	}
	/**
	 * Invoked when a window has been closed as the result
	 * of calling dispose on the window.
	 */
	public void windowClosed(WindowEvent e) {
	}
	public void windowClosing(WindowEvent e) {
		if (m_useFrames) {
			MyFrame f = (MyFrame) e.getSource();
			m_windows.remove(f);
			m_serverTabUI.showTab(f.m_uiData);
		} else {
			MyDialog d = (MyDialog) e.getSource();
			m_windows.remove(d);
			m_serverTabUI.showTab(d.m_uiData);
		}

		m_restoreAction.setEnabled(m_windows.isEmpty());
	}
	/**
	 * Invoked when a window is no longer the user's active
	 * window, which means that keyboard events will no longer
	 * be delivered to the window or its subcomponents.
	 */
	public void windowDeactivated(WindowEvent e) {
	}
	/**
	 * Invoked when a window is changed from a minimized
	 * to a normal state.
	 */
	public void windowDeiconified(WindowEvent e) {
	}
	/**
	 * Invoked when a window is changed from a normal to a
	 * minimized state. For many platforms, a minimized window 
	 * is displayed as the icon specified in the window's 
	 * iconImage property.
	 * @see Frame#setIconImage
	 */
	public void windowIconified(WindowEvent e) {
	}
	/**
	 * Invoked the first time a window is made visible.
	 */
	public void windowOpened(WindowEvent e) {
	}
}
