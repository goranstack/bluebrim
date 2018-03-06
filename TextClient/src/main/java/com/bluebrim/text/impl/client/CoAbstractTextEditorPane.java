package com.bluebrim.text.impl.client;

import java.awt.*;
import java.rmi.*;

import javax.swing.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.client.*;
import com.bluebrim.text.impl.client.swing.*;
import com.bluebrim.text.shared.*;

//

public abstract class CoAbstractTextEditorPane extends CoPanel {
	protected boolean m_hasLock = false;

	protected CoAbstractTextEditor m_textEditor;
	protected ScaleAndMargins m_scaleAndMargin;
	protected CoTextStyleToolbarSet m_toolbars;
	protected CoStyledTextPopupMenu m_popupMenu;
	protected CoCharacterStyleActionUI m_characterStyleUI;
	protected CoParagraphStyleActionUI m_paragraphStyleUI;
	protected CoTextMeasurementPrefsUI m_measurementPrefs;
	protected CoCharacterTagUI m_characterTagUI;
	protected CoParagraphTagUI m_paragraphTagUI;


	protected CoStyledDocument m_nullDocument = new CoStyledDocument();
	protected interface ScaleAndMargins extends CoHorizontalMarginController.MarginHolder {
		void setScale(double scale);
		void setHorizontalMargin(int m);
		double getScale();
		int getHorizontalMargin();
	}

	public CoAbstractTextEditorPane() {
		this(new CoUserInterfaceBuilder(null), new CoMenuBuilder(null));
	}

	protected abstract ScaleAndMargins createScaleAndMargin();

	protected abstract CoAbstractTextEditor createTextEditor(CoStyledDocumentIF doc);

	public CoStyledDocumentIF getDocument() {
		CoStyledDocumentIF doc = m_textEditor.getCoStyledDocument();

		if (doc == m_nullDocument)
			doc = null;

		return doc;
	}

	public double getMargin() {
		return m_scaleAndMargin.getHorizontalMargin();
	}

	public double getScale() {
		return m_scaleAndMargin.getScale();
	}

	public CoAbstractTextEditor getTextEditor() {
		return m_textEditor;
	}

	public void setContext(CoTextEditorContextIF doc) {
		m_toolbars.m_tagToolBar.setContext(doc);
		m_toolbars.m_fontToolBar.setContext(doc);
		m_popupMenu.setContext(doc);
		m_characterStyleUI.setContext(doc);
		m_paragraphStyleUI.setContext(doc);
		m_characterTagUI.setContext(doc);
		m_paragraphTagUI.setContext(doc);
	}

	public void setDocument(CoStyledDocumentIF doc) {
		if (doc == null)
			doc = m_nullDocument;

		m_textEditor.setCoStyledDocument(doc);

		setContext(doc);
	}

	public void setEnabled(boolean b) {
		super.setEnabled(b);

		m_textEditor.setEnabled(b);
		m_toolbars.m_fontToolBar.setEnabled(b);
		m_toolbars.m_styleToolBar.setEnabled(b);
		m_toolbars.m_tagToolBar.setEnabled(b);
	}

	public void setMargin(double m) {
		m_scaleAndMargin.setHorizontalMargin((int) m);
	}

	public void setScale(double scale) {
		m_scaleAndMargin.setScale(scale);
	}

	public CoAbstractTextEditorPane(CoUserInterfaceBuilder b, CoMenuBuilder mb) {
		super(new BorderLayout());

		m_textEditor = createTextEditor(m_nullDocument);
		m_textEditor.setSize(100, 100);
		JScrollPane sp =
			new JScrollPane(m_textEditor, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(BorderLayout.CENTER, sp);

		JTextField statusText = new JTextField();
		statusText.setEditable(false);
		add(BorderLayout.SOUTH, statusText);

		m_toolbars = new CoTextStyleToolbarSet(m_textEditor.getActions(), m_textEditor);
		m_characterStyleUI = new CoCharacterStyleActionUI(m_textEditor.getActions(), m_textEditor);
		m_paragraphStyleUI = new CoParagraphStyleActionUI(m_textEditor.getActions(), m_textEditor);
		m_paragraphTagUI = new CoParagraphTagUI(m_textEditor);
		m_characterTagUI = new CoCharacterTagUI(m_textEditor);

		m_measurementPrefs = new CoTextMeasurementPrefsUI();
		try
        {
            m_measurementPrefs.setDomain(CoTextClient.getTextServer().getTextMeasurementPrefs());
        } catch (RemoteException e)
        {
            throw new RuntimeException(e);
        }

		m_popupMenu =
			new CoStyledTextPopupMenu(
				m_textEditor.getActions(),
				mb,
				m_textEditor,
				m_characterStyleUI,
				m_paragraphStyleUI,
				m_characterTagUI,
				m_paragraphTagUI,
				m_measurementPrefs);
		m_textEditor.setPopupMenu(m_popupMenu);

		{
			CoHorizontalToolbarDockingBay db = new CoHorizontalToolbarDockingBay();
			db.add(m_toolbars.m_tagToolBar);
			db.addToolbar(m_toolbars.m_fontToolBar, 0);
			db.addToolbar(m_toolbars.m_styleToolBar, 0);
			add(BorderLayout.NORTH, db);
		}

		{
			CoVerticalToolbarDockingBay db = new CoVerticalToolbarDockingBay();
			add(BorderLayout.WEST, db);
		}

		m_scaleAndMargin = createScaleAndMargin();

		setDocument(null);
	}
}