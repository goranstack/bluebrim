package com.bluebrim.layout.impl.client.editor;

import java.awt.Frame;
import java.awt.event.ActionEvent;

/**
 * Layout editor operation: Open the ui for editing spell checker preferences
 * 
 * @author: Dennis
 */

public class CoEditSpellingPrefs extends CoLayoutEditorAction {
	public static final String SPELL_CHECK_FAILED_MESSAGE = "CoLayoutEditor.SPELL_CHECK_FAILED_MESSAGE";
	public static final String SPELL_CHECK_FAILED_TITLE = "CoLayoutEditor.SPELL_CHECK_FAILED_TITLE";
	public static final String SPELL_CHECK_FAILED_RETRY = "CoLayoutEditor.SPELL_CHECK_FAILED_RETRY";
	public static final String SPELL_CHECK_FAILED_CANCEL = "CoLayoutEditor.SPELL_CHECK_FAILED_CANCEL";

	public CoEditSpellingPrefs(String name, CoLayoutEditor e) {
		super(name, e);
	}

	public CoEditSpellingPrefs(CoLayoutEditor e) {
		super(e);
	}

	public void actionPerformed(ActionEvent arg1) {
		m_editor.m_styledTextEditor.editSpellCheckOptions((Frame) m_editor.getWindow(), getSpellCheckProperties());
	}

	protected Object getSpellCheckProperties() {
//		try
//        {
//            return CoSpellCheckerClient.getSpellCheckerServer().getSpellCheckProperties();
//        } catch (RemoteException e)
//        {
//            throw new RuntimeException(e);
//        }
		throw new UnsupportedOperationException("Until Wintertree spell checker is replaced by a GPL spell checker");
	}
}