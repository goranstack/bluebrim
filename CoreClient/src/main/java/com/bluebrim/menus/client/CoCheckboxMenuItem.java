package com.bluebrim.menus.client;

import javax.swing.JCheckBoxMenuItem;

/**
 	Subklass till CheckboxMenuItem som anv�nds av CoMenuHandler.<br>
 	Det finns tv� anledningar f�r denna subklassning:
 	<ul>
 	<li> Det m�ste g� att enabla/disabla ett menyitem innan det visas upp.
 	<li> I en inte allt f�r avl�gsen framtid skall CoCheckboxMenuItem anv�nda Swing
 	i st�llet f�r AWT. Denna �ndring �r l�ttare att g�ra om
 	vi alltid anv�nder en egen klass.
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
