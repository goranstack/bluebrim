package com.bluebrim.gui.client;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import com.bluebrim.base.shared.CoStatusShower;

/**
 * Written by Kong Eu Tak for Swing Connection Article
 * Email: konget@cheerful.com
 * Homepage: http://www.singnet.com.sg/~kongeuta/
 * 
 * This class currently extends JDialog. Since JDK 1.4 this
 * (or Dialog) is the best choice, since:<ul>
 * <li>we can be undecorated, that is have no window title.</li>
 * <li>we can stay on top after config popups when running from Eclipse.</li>
 * <li>we can be shown or hidden using Alt-Tab (on MS Windows).</li>
 * <li>we don't show up in the (MS Windows) task bar. (JFrame would.)</li>
 * </ul>
 * 
 * @author Kong Eu Tak
 * @author Markus Persson 2002-04-10 (Corrected badly named variables)
 */
public class CoSplashScreen extends JDialog implements CoStatusShower {
	JLabel m_statusBar;

	private class UpdateStatus implements Runnable {
		private String m_newStatus;
		public UpdateStatus(String status) {
			m_newStatus = status;
		}
		public void run() {
			m_statusBar.setText(m_newStatus);
		}
	}

	private class CloseSplashScreen implements Runnable {
		public void run() {
			setVisible(false);
			dispose();
		}
	}

	public CoSplashScreen(Icon coolPicture) {
		this(coolPicture, null);
	}
	
	public CoSplashScreen(Icon coolPicture, Color bg) {
		// Using Swing's deafult frame, branded by CoApplication.
		super();
		// Attempt to work in JDK 1.3 ... /Markus
		try {
			setUndecorated(true);
		} catch (Throwable bad) {
		}

		// Create a JPanel so we can use a BevelBorder
		JPanel bevelPanel = new JPanel(new BorderLayout());
		if (bg != null) {
			bevelPanel.setBackground(bg);
		}
		bevelPanel.add(new JLabel(coolPicture), BorderLayout.CENTER);
		bevelPanel.add(m_statusBar = new JLabel(" ", SwingConstants.LEFT), BorderLayout.SOUTH);
		m_statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		bevelPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
		setContentPane(bevelPanel);
		pack();

		// Plonk it on center of screen. (Now better way! /Markus)
		CoGUI.centerOnScreen(this);
	}

	public void close() {
		try {
			// Close and dispose Window in AWT thread
			SwingUtilities.invokeLater(new CloseSplashScreen());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showStatus(String currentStatus) {
		try {
			// Update Splash-Screen's status bar in AWT thread
			SwingUtilities.invokeLater(new UpdateStatus(currentStatus));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}