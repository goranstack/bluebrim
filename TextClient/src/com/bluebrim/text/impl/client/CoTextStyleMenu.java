package com.bluebrim.text.impl.client;

import java.awt.*;

import javax.swing.*;

import com.bluebrim.menus.client.*;
import com.bluebrim.text.shared.*;

/*
 * Skapar en stil-meny med alla dess undermenyer.
 * Klassen implementerar CoAttributeListenerIF så att de olika
 * menyalternativen kan uppdateras allt eftersom användaren förflyttar
 * sig i texten. För att skapa menyn krävs att de Actions som ska knytas
 * till de olika menyalternativen skickas in och likaså en menybyggare
 * (CoMenuBuilder). När en editor (CoAbstractTextPane) sätts så
 * kan attributlyssnaren börja jobba och menyalternativ blir valbara.
 * Även CoStyleRuleIF, CoLocalCharacterStyleUI och CoLocalParagraphStyleUI
 * måste sättas för att menyn ska fungera fullt ut.
 */

public class CoTextStyleMenu extends CoSubMenu implements CoAttributeListenerIF {

	public void attributesChanged(CoAttributeEvent event) {
		m_menu.update(event.getParagraphAttributes(), event.getCharacterAttributes(), event.getP0(), event.getP1());
	}

	protected void setAllEnabled(boolean b) {
		setAllEnabled(b, this);
	}
	protected static void setAllEnabled(boolean b, JMenu m) {
		Component[] c = m.getMenuComponents();
		for (int i = 0; i < c.length; i++) {
			if (c[i] instanceof JMenu) {
				setAllEnabled(b, (JMenu) c[i]);
			} else
				if (c[i] instanceof JMenuItem) {
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

	private CoStyledTextMenuImplementation m_menu;

	public CoTextStyleMenu(
		Action[] actions,
		CoMenuBuilder builder,
		CoAbstractTextEditor editor,
		com.bluebrim.text.impl.client.CoCharacterStyleActionUI charStyle,
		com.bluebrim.text.impl.client.CoParagraphStyleActionUI paraStyle,
		CoCharacterTagUI charTag,
		CoParagraphTagUI paraTag) {
		super("Stilmeny");

		if (builder.getMenuBar() == null)
			builder.createMenuBar();

		builder.prepareMenu(this);

		m_menu = new CoStyledTextMenuImplementation(builder) {
			protected void add(CoSubMenu menu) {
				CoTextStyleMenu.this.add(menu);
			}
			protected void add(CoMenuItem menuItem) {
				CoTextStyleMenu.this.add(menuItem);
			}
			protected void addSeparator() {
				CoTextStyleMenu.this.addSeparator();
			}
			protected void setAllEnabled(boolean b) {
				CoTextStyleMenu.this.setAllEnabled(b);
			}
		};

		m_menu.create(actions, editor, charStyle, paraStyle, charTag, paraTag, null);

		setText(m_menu.getResourceString("MENU_STYLE"));
		setMnemonic(m_menu.getResourceChar("MENU_STYLE"));
	}

	public void setParagraphTagUI(CoParagraphTagUI ui) {
		m_menu.setParagraphTagUI(ui);
	}
}