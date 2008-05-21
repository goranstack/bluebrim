package com.bluebrim.text.impl.client;

import java.awt.*;
import java.rmi.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.menus.client.*;
import com.bluebrim.spellchecker.client.*;
import com.bluebrim.spellchecker.shared.*;
import com.bluebrim.text.shared.*;

/*
 * Skapar en h�gerknappsmeny.
 * N�r anv�ndaren klickar med h�gerknappen s� anropas updatePopupMenu
 * d�r menyalternativen uppdateras beroende p� var i texten anv�ndaren
 * befinner sig.
 * F�r att skapa menyn kr�vs att de Actions som ska knytas
 * till de olika menyalternativen skickas in. En editor (CoAbstractTextPane),
 * CoStyleRuleIF, CoLocalCharacterStyleUI, CoLocalParagraphStyleUI och
 * CoUndoHandlerIF m�ste s�ttas f�r att menyn ska fungera fullt ut.
 */

public class CoStyledTextPopupMenu extends CoPopupMenu {

	protected CoSpellCheckPropertiesIF getSpellCheckProperties() {
		try
        {
            return CoSpellCheckerClient.getSpellCheckerServer().getSpellCheckProperties();
        } catch (RemoteException e)
        {
            throw new RuntimeException(e);
        }

	}
	protected void setAllEnabled(boolean b) {
		setAllEnabled(b, this);
	}
	protected static void setAllEnabled(boolean b, JMenu m) {
		Component[] c = m.getMenuComponents();
		for (int i = 0; i < c.length; i++) {
			if (c[i] instanceof JMenu) {
				setAllEnabled(b, (JMenu) c[i]);
			} else if (c[i] instanceof JMenuItem) {
				c[i].setEnabled(b);
			}
		}
	}
	protected static void setAllEnabled(boolean b, JPopupMenu m) {
		Component[] c = m.getComponents();
		for (int i = 0; i < c.length; i++) {
			if (c[i] instanceof JMenu) {
				setAllEnabled(b, (JMenu) c[i]);
			} else if (c[i] instanceof JMenuItem) {
				c[i].setEnabled(b);
			}
		}
	}
	public void setCoCharacterStyleUI(com.bluebrim.text.impl.client.CoCharacterStyleActionUI ui) {
		m_menu.setCoCharacterStyleUI(ui);
	}
	public void setContext(CoTextEditorContextIF context) {
		m_menu.setContext(context);
	}
	public void setCoParagraphStyleUI(com.bluebrim.text.impl.client.CoParagraphStyleActionUI ui) {
		m_menu.setCoParagraphStyleUI(ui);
	}

	public void setEditor(CoAbstractTextEditor editor) {
		m_menu.setEditor(editor);
	}
	public void setTextMeasurementPrefsUI(CoTextMeasurementPrefsUI ui) {
		m_menu.setTextMeasurementPrefsUI(ui);
	}

	public void updatePopupMenu(AttributeSet paraAttr, AttributeSet charAttr, int startSelection, int endSelection) {
		m_menu.update(paraAttr, charAttr, startSelection, endSelection);
	}

	private CoStyledTextMenuImplementation m_menu;

	public CoStyledTextPopupMenu(
		Action[] actions,
		CoMenuBuilder builder,
		CoAbstractTextEditor editor,
		com.bluebrim.text.impl.client.CoCharacterStyleActionUI charStyle,
		com.bluebrim.text.impl.client.CoParagraphStyleActionUI paraStyle,
		CoCharacterTagUI charTag,
		CoParagraphTagUI paraTag,
		CoTextMeasurementPrefsUI measurementPrefs) {
		super();

		m_menu = new CoStyledTextMenuImplementation(builder) {
			protected void add(CoSubMenu menu) {
				CoStyledTextPopupMenu.this.add(menu);
			}
			protected void add(CoMenuItem menuItem) {
				CoStyledTextPopupMenu.this.add(menuItem);
			}
			protected void addSeparator() {
				CoStyledTextPopupMenu.this.addSeparator();
			}
			protected void setAllEnabled(boolean b) {
				CoStyledTextPopupMenu.this.setAllEnabled(b);
			}
		};

		m_menu.create(actions, editor, charStyle, paraStyle, charTag, paraTag, measurementPrefs);

	}

	public void setParagraphTagUI(CoParagraphTagUI ui) {
		m_menu.setParagraphTagUI(ui);
	}
}