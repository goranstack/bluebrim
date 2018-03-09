package com.bluebrim.gui.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * A very simple dialog for displaying Readme-texts and such.
 * Use it by creating it with a title and then calling either
 * showText(String text) or showFile(String filePath, boolean useDoNotShowAgainBox).
 * if useDoNotShowAgainBox is true a checkbox will be shown letting the user mark
 * the file as read.
 *
 * The user can only do one thing and that is to close the dialog.
 *
 * See/run main() for two examples.
 *
 * PENDING: Extend the "do not show me again" usage in Java Prefs to
 * handle file versions (time stamp?). Also make a UI for clearing
 * the setting? /Markus 2002-09-02
 *
 * PENDING: Make this a real Dialog? /Markus 2002-06-07
 * 
 * @author Göran Hultgren
 * @author Markus Persson 2002-05-22
 */
public class CoTextDialog extends JFrame {
	final static int PADDING = 5;
	private String m_readKey;
	private Box mainPanel, buttonPanel;
	private JTextArea area;
	private JCheckBox m_doNotShowAgainBox;
	private JButton closeButton;

	private class WindowExitAdapter extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			close();
		}
	}
	
	private class CloseAction extends Object implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			close();
		}
	}
	
	/**
	 * @param readKey Key (typically filename) used to mark file as
	 * being read. Pass null to not show a checkbox.
	 */
	public CoTextDialog(String title, String readKey) {
		setTitle(title);
		m_readKey = readKey;
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)); 
		((JPanel)getContentPane()).setBorder(createPaddedBorder()); // Tomt utrymme runt om dialogen
		mainPanel = Box.createVerticalBox();
		area = new JTextArea("");
		area.setEditable(false);
		JScrollPane scroller = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainPanel.add(scroller);
		buttonPanel = Box.createHorizontalBox();
		if (readKey != null) {
			m_doNotShowAgainBox = new JCheckBox("Visa inte igen", isReadInPrefs());
			buttonPanel.add(m_doNotShowAgainBox);
		}
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(closeButton = new JButton("Stäng"));
		closeButton.addActionListener(new CloseAction());
		mainPanel.add(buttonPanel);
		getContentPane().add(mainPanel);
		pack();
		setBounds(CoGUI.centerOnScreen(600, 400));
		CoGUI.brand(this);
	}

	/**
	 * If we have shown a file - mark it as read if it should not be shown
	 * again next time.
	 */
	public void close() {
		if (m_readKey != null) {
			boolean newState = isNowMarkedAsRead();
			if (newState != isReadInPrefs()) {
				setReadInPrefs(newState);
			}
		}
		dispose();
	}
	
	/**
	 * Set the text to be shown and open the frame.
	 */
	public void showText(String text) {
		area.setText(text);
		show();
	}
	
	/**
	 * Shows file if file exists and has not been read.
	 * Use readKey passed in constructor as filename.
	 * If forceRead is true we do not care
	 * if file has been marked as already read.
	 */
	public void showTextFile(boolean forceRead) {
		if (forceRead || !isReadInPrefs()) {
			showText(readFile(m_readKey));
		}
	}
	
	/**
	 * Return the file called fileName as a String.
	 */
	public String readFile(String fileName) {
		StringBuffer fileContent = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				fileContent.append(line);
				fileContent.append('\n');
			}
		} catch(Exception e) {
			return "Something went wrong when opening textfile "+fileName+" for reading. Ignoring.";
		}
		return fileContent.toString();
	}

	private boolean isReadInPrefs() {
		return getPrefsNode().getBoolean(m_readKey, false);
	}

	private void setReadInPrefs(boolean isRead) {
		getPrefsNode().putBoolean(m_readKey, isRead);
	}
	
	private Preferences getPrefsNode() {
		return Preferences.userNodeForPackage(this.getClass()).node("read files");
	}
	
	public boolean isNowMarkedAsRead() {
		return (m_doNotShowAgainBox != null) && m_doNotShowAgainBox.isSelected();
	}
	
	private Border createPaddedBorder() {
		return new EmptyBorder(PADDING, PADDING, PADDING, PADDING);
	}
	
	static public void main(String[] args) {
		CoTextDialog dlg = new CoTextDialog("En titel", null); // No checkbox
		dlg.showText("Text\nMore text...\nEven more text...");
		CoTextDialog dlg2 = new CoTextDialog("En fildialog", "Readme.txt"); // Open a dialog with a checkbox.
		dlg2.showTextFile(false); // Only show if it is not read
	}
}
