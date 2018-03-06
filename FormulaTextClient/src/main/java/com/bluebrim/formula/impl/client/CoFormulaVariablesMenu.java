package com.bluebrim.formula.impl.client;

import com.bluebrim.menus.client.*;
import com.bluebrim.text.impl.client.actions.*;


/*
 * A menu to use in a menu bar to list variables and insert them into a Co´FormulaTextField
 */

public class CoFormulaVariablesMenu extends CoSubMenu {
	// the editor to write the variable names into
	protected CoFormulaVariableInsertAction m_action;

	public CoFormulaVariablesMenu(CoMenuBuilder builder, CoFormulaTextField editor, String[] menuItemNames) {
		super(CoFormulaUIResources.getUIString("VARIABLES_MENU"));

		setEditor(editor);
		createMenuItems(builder, menuItemNames);
	}
	protected void createMenuItems(CoMenuBuilder builder, String[] menuItemNames) {
		if (menuItemNames == null || menuItemNames.length == 0 || m_action == null) {
			setEnabled(false);
			return;
		}

		setEnabled(true);

		// create menu items
		for (int i = 0; i < menuItemNames.length; i++) {
			String name = menuItemNames[i];
			CoMenuItem mi = builder.addMenuItem(this, name, m_action);
			mi.setActionCommand(name);
		}
	}
	public void setEditor(CoFormulaTextField editor) {
		if (editor == null)
			m_action = null;
		else
			m_action = new CoFormulaVariableInsertAction(editor);
	}
}
