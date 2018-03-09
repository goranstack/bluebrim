package com.bluebrim.text.impl.client;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2000-11-06 11:28:45)
 * @author: Dennis
 */
public class CoFormattedTextHolderDialog extends JFrame {
	public static final String SCALE = "CoFormattedTextHolderDialog.SCALE";
	private CoAbstractTextEditorPane m_textEditorPane;
	private CoFormattedTextHolderIF m_textHolder;

	protected void doStop() {
		if (m_textHolder != null) {
			CoTextCommands.SetDocumentCommand c =
				new CoTextCommands.SetDocumentCommand(m_textHolder, new CoFormattedText(m_textEditorPane.getDocument()));
			CoTransactionUtilities.execute(c, m_textHolder);
		}

		m_textEditorPane.setDocument(null);

		m_textHolder = null;
	}

	public void stop() {
		setVisible(false);
	}

	public CoFormattedTextHolderDialog(String title, CoAbstractTextEditorPane textEditorPane, CoUserInterfaceBuilder builder) {
		super(title);

		m_textEditorPane = textEditorPane;

		getContentPane().add(m_textEditorPane, BorderLayout.CENTER);

		CoZoomPanel zoomPanel =
			new CoZoomPanel(builder, CoTextClientResources.getName(SCALE), 1, new double[] { 50, 100, 110, 120, 150, 200, 400 }, null);
		getContentPane().add(zoomPanel, BorderLayout.NORTH);
		zoomPanel.setZoomable(new CoZoomPanel.Zoomable() {
			public void setScale(double scale) {
				m_textEditorPane.setScale(scale / 100.0);
			}
			public double getScale() {
				return m_textEditorPane.getScale() * 100;
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				doStop();
			}
		});

	}

	public boolean start(CoFormattedTextHolderIF dh, CoFormattedTextHolderIF.Context context) {
		m_textHolder = dh;

		m_textEditorPane.setDocument(m_textHolder.getMutableFormattedText( context).createMutableStyledDocument());

		setVisible(true);

		return true;
	}
}