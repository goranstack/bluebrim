package com.bluebrim.gui.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.bluebrim.swing.client.CoButton;
import com.bluebrim.swing.client.CoPanel;

/**
 * Broken out from CoTransactionUtilities.
 * 
 * PENDING: Include in CoGUI or what? Remove obsolete classes!
 * 
 * @author Markus Persson 2002-05-22
 */
public class CoErrorDialog extends CoDialog {

	private CoButton m_okButton;
	private CoButton m_additionalInfoButton;
	private String m_message;
	private String m_details;

	public static boolean resume(String message, final String details) {
		JButton button = new JButton("Mera info ...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DetailsDialog(null, details, false).open();
			}
		});
		Object[] infos = new Object[] {
			button,
			"Fortsätt",
			"Avsluta",
		};
		int selected = JOptionPane.showOptionDialog(
			null,
			message,
			"Fel vid initiering",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.ERROR_MESSAGE,
			null,
			infos,
			infos[1]);
		return (selected == 1);
	}
	
	public static void open(String message, String details) {
		new CoErrorDialog(null, message, details).open();
	}
	public static void open(String message, Throwable nestedException) {
		open(message, detailsFrom(nestedException));
	}
	public CoErrorDialog(Frame frame, String message, String details) {
		super(frame, null, true);
		m_message = toHTML(message);
		m_details = details;
	}

	protected CoPanel createMainPanel() {
		CoPanel tPanel = new CoPanel();
		tPanel.setLayout(new BorderLayout());
		tPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tPanel.add(new JLabel(m_message, UIManager.getIcon("OptionPane.errorIcon"), SwingConstants.CENTER), BorderLayout.WEST);
		return tPanel;
	}

	public CoPanel createButtonPanel() {
		m_okButton = new CoButton(CoUIStringResources.getName("OK"));
		m_okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		m_okButton.setDefaultCapable(true);
		m_okButton.setFocusPainted(true);
		CoPanel tPanel = new CoPanel(null, false, new Insets(5, 5, 5, 5));
		tPanel.setLayout(new BoxLayout(tPanel, BoxLayout.X_AXIS));
		tPanel.add(Box.createHorizontalGlue());

		if (m_details != null) {
			m_additionalInfoButton = new CoButton("Mera info");
			m_additionalInfoButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showAdditionalInfo();
				}
			});
			m_additionalInfoButton.setFocusPainted(true);
			tPanel.add(m_additionalInfoButton);
			tPanel.add(Box.createHorizontalStrut(5));
		}
		tPanel.add(m_okButton);
		return tPanel;
	}

	protected void init() {
		JRootPane pane = new JRootPane();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.add(createMainPanel());
		pane.add(createButtonPanel());
		pane.setDefaultButton(m_okButton);
		getContentPane().add(pane);
		pack();
	}

	public void open() {
		init();
		CoGUI.centerOnScreen(this);
		show();
		dispose();
	}

	private void showAdditionalInfo() {
		new DetailsDialog((Frame) getOwner(), m_details, true).open();
	}
	private String toHTML(String message) {
		StringBuffer buffer = new StringBuffer("<html>");
		StringTokenizer t = new StringTokenizer(message, "\n");
		while (t.hasMoreTokens()) {
			buffer.append("<br>");
			buffer.append(t.nextToken());
		}
		buffer.append("</html>");
		return buffer.toString();
	}

	private static String detailsFrom(Throwable exception) {
		StringWriter buf = new StringWriter();
		PrintWriter printer = new PrintWriter(buf);

		exception.printStackTrace(printer);
		printer.close();
		return buf.toString();
	}
	
	public static class DetailsDialog extends CoDialog {
		private CoButton m_button;
		private JTextArea m_textArea;

		public DetailsDialog(Frame owner, String details, boolean big) {
			super(owner, null, true);
			m_textArea = new JTextArea(details);
			if (big) {
				setSize(800, 500);
			} else {
				setSize(600, 400);
			}
		}
		
		protected Component createSrollPane() {
			// set to disable horizontal scrolling
//			m_textArea.setLineWrap(true);
			m_textArea.setEditable(false);
			JScrollPane scroll = new JScrollPane(m_textArea);
			scroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			return scroll;
		}
		
		public Component createButtonPanel() {
			m_button = new CoButton(CoUIStringResources.getName("OK"));
			m_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
			m_button.setDefaultCapable(true);
			m_button.setFocusPainted(true);

			Box box = new Box(BoxLayout.X_AXIS);
			box.add(Box.createHorizontalGlue());
			box.add(m_button);
			return box;
		}

		protected void init() {
			Container panel = getContentPane();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.add(createSrollPane());
			panel.add(createButtonPanel());
			getRootPane().setDefaultButton(m_button);
			// pack destroys the size
//			pack();
		}

		public void open() {
			init();
			CoGUI.centerOnScreen(this);
			show();
			dispose();
		}
	}

}