package com.bluebrim.gui.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

public class CoMetalTheme extends DefaultMetalTheme {

	//	Grey look
	private final ColorUIResource m_primary1 = new ColorUIResource(103, 103, 133);
	private final ColorUIResource m_primary2 = new ColorUIResource(160, 160, 200);
	private final ColorUIResource m_primary3 = new ColorUIResource(220, 220, 250);

	private final ColorUIResource m_secondary1 = new ColorUIResource(120, 120, 120);
	private final ColorUIResource m_secondary2 = new ColorUIResource(200, 200, 200);
	private final ColorUIResource m_secondary3 = new ColorUIResource(240, 240, 240);
/*
	private final ColorUIResource m_primary1 = new ColorUIResource(50, 50, 50);
	private final ColorUIResource m_primary2 = new ColorUIResource(120, 120, 120);
	private final ColorUIResource m_primary3 = new ColorUIResource(200, 200, 200);

	private final ColorUIResource m_secondary1 = new ColorUIResource(255, 230, 120);
	private final ColorUIResource m_secondary2 = new ColorUIResource(255, 230, 170);
	private final ColorUIResource m_secondary3 = new ColorUIResource(255, 230, 190);
*/
	private FontUIResource m_controlFont;
	private FontUIResource m_menuFont;

	public void addCustomEntriesToTable(UIDefaults defs) {
		super.addCustomEntriesToTable(defs);
		Font textFieldFont = UIManager.getFont(CoUIConstants.TEXTFIELD_FONT);
		defs.put(CoUIConstants.TEXTFIELD_MARGIN, new Insets(2, 2, 2, 2));
		defs.put(CoUIConstants.LABELED_TEXT_FIELD_FONT, textFieldFont.deriveFont(textFieldFont.getSize() - 2.0f));
		defs.put(CoUIConstants.LABELED_TEXT_FIELD_FOREGROUND, UIManager.getColor(CoUIConstants.TEXTFIELD_FOREGROUND));
		defs.put(CoUIConstants.LABELED_TEXT_FIELD_BACKGROUND, new ColorUIResource(198, 224, 244)); // Light blue. /Markus
		defs.put(CoUIConstants.LABELED_TEXT_FIELD_BORDER, BorderFactory.createLineBorder(getPrimary1()));

		defs.put(CoUIConstants.TREE_FONT, getControlTextFont());
		// Make tree cells use their tree's background (more or less). /Markus
		defs.remove(CoUIConstants.TREE_TEXT_BACKGROUND);

		defs.put(CoUIConstants.FOCUS_COLOR, getPrimary1());
		defs.put(CoUIConstants.TEXTFIELD_INACTIVE_FOREGROUND, Color.black);

		Color focusColor = defs.getColor(CoUIConstants.FOCUS_COLOR);
		defs.put(CoUIConstants.BUTTON_FOCUS, focusColor);
		defs.put(CoUIConstants.TOGGLEBUTTON_FOCUS, focusColor);
		defs.put(CoUIConstants.RADIOBUTTON_FOCUS, focusColor);
		defs.put(CoUIConstants.CHECKBOX_FOCUS, focusColor);

		defs.put(CoUIConstants.HEADLINE_COLOR, Color.blue);

		
		// Quest to make the UI builder obsolete. Perhaps this
		// should be elsewhere ... /Markus 2002-05-24
		InsetsUIResource buttonMargin = new InsetsUIResource(0, 2, 0, 2);
		defs.put("Button.margin", buttonMargin);
		defs.put("ToggleButton.margin", buttonMargin);
		defs.put("RadioButton.margin", buttonMargin);
		defs.put("CheckBox.margin", buttonMargin);
		
	}

	public CoMetalTheme() {
		m_controlFont = new FontUIResource(Font.getFont("swing.plaf.metal.controlFont", CoUIConstants.GARAMOND_12_LIGHT));
		m_menuFont = new FontUIResource(Font.getFont("swing.plaf.metal.systemFont", CoUIConstants.GARAMOND_12_LIGHT));
	}

	public FontUIResource getControlTextFont() {
		return m_controlFont;
	}

	public FontUIResource getMenuTextFont() {
		return m_menuFont;
	}

	protected ColorUIResource getPrimary1() {
		return m_primary1;
	}

	protected ColorUIResource getPrimary2() {
		return m_primary3;
	}

	protected ColorUIResource getPrimary3() {
		return m_primary3;
	}

	protected ColorUIResource getSecondary1() {
		return m_secondary1;
	}

	protected ColorUIResource getSecondary2() {
		return m_secondary2;
	}

	protected ColorUIResource getSecondary3() {
		return m_secondary3;
	}
}