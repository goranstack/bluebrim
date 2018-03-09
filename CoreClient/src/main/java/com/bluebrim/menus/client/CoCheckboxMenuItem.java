package com.bluebrim.menus.client;

import javax.swing.JCheckBoxMenuItem;

/**
 	Subklass till CheckboxMenuItem som används av CoMenuHandler.<br>
 	Det finns två anledningar för denna subklassning:
 	<ul>
 	<li> Det måste gå att enabla/disabla ett menyitem innan det visas upp.
 	<li> I en inte allt för avlägsen framtid skall CoCheckboxMenuItem använda Swing
 	i stället för AWT. Denna ändring är lättare att göra om
 	vi alltid använder en egen klass.
 	</ul>
 */
public class CoCheckboxMenuItem extends JCheckBoxMenuItem {
/**
 * CoCheckboxMenuItem constructor comment.
 */
public CoCheckboxMenuItem() {
	super();
}
/**
 * CoCheckboxMenuItem constructor comment.
 * @param label java.lang.String
 */
public CoCheckboxMenuItem(String label) {
	super(label);
}
/**
 * CoCheckboxMenuItem constructor comment.
 * @param label java.lang.String
 * @param state boolean
 */
public CoCheckboxMenuItem(String label, boolean state) {
	super(label, state);
}
}
