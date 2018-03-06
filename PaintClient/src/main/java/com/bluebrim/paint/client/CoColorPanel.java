package com.bluebrim.paint.client;

import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;

/**
 * Component for displaying and editing color and shade
 * Creation date: (2001-08-14 11:11:48)
 * @author: Dennis
 */
public class CoColorPanel extends CoPanel {
	private CoOptionMenu m_colorOptionMenu;
	private CoTextField m_shadeTextField;

	public CoColorPanel(CoUserInterfaceBuilder b) {
		super(new CoRowLayout(1, true));

		b.preparePanel(this);

		m_colorOptionMenu = createColorOptionMenu(b);
		m_shadeTextField = createShadeTextField(b);

		add(m_colorOptionMenu);
		add(m_shadeTextField);
		add(new CoLabel("%"));
	}

	protected CoOptionMenu createColorOptionMenu(CoUserInterfaceBuilder b) {
		CoOptionMenu m = b.createOptionMenu();
		m.setRenderer(new CoOptionMenuColorRenderer());

		return m;
	}

	protected CoTextField createShadeTextField(CoUserInterfaceBuilder b) {
		CoTextField t = b.createSlimTextField();

		t.setColumns(4);
		t.setHorizontalAlignment(CoTextField.RIGHT);

		t.setActivateWhenLosingFocus(true);
		t.setSelectWhenGainingFocus(true);

		return t;
	}

	public CoOptionMenu getColorOptionMenu() {
		return m_colorOptionMenu;
	}

	public CoTextField getShadeTextField() {
		return m_shadeTextField;
	}
}
