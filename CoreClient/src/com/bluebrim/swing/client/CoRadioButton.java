package com.bluebrim.swing.client;

import javax.swing.Icon;
import javax.swing.JRadioButton;
/**
 	Subklass till JRadioButton som har en instansvariabel 'buttonGroup' som håller den 
 	grupp som radioknappen tillhör. Normalt så är det gruppen som laddas med sina 
 	knappar men på detta sätt går det att knyta en radioknapp till sin grupp i VCE.
 	@author Lasse Svadängs 971010
 */
public class CoRadioButton extends JRadioButton {
	CoButtonGroup buttonGroup;
/**
 * JRadioButton constructor comment.
 */
public CoRadioButton() {
	super();
}
/**
 * JRadioButton constructor comment.
 * @param arg1 java.lang.String
 */
public CoRadioButton(String arg1) {
	super(arg1);
}
/**
 * JRadioButton constructor comment.
 * @param arg1 java.lang.String
 * @param arg2 javax.swing.Icon
 */
public CoRadioButton(String text, Icon icon) {
	super(text, icon);
}
/**
 * JRadioButton constructor comment.
 * @param text java.lang.String
 * @param Icon javax.swing.Icon
 * @param state boolean
 */
public CoRadioButton(String text, Icon icon, boolean state) {
	super(text, icon, state);
}
/**
 * JRadioButton constructor comment.
 * @param arg1 java.lang.String
 * @param arg2 boolean
 */
public CoRadioButton(String arg1, boolean arg2) {
	super(arg1, arg2);
}
/**
 * JRadioButton constructor comment.
 * @param arg1 javax.swing.Icon
 */
public CoRadioButton(Icon icon) {
	super(icon);
}
/**
 * JRadioButton constructor comment.
 * @param arg1 javax.swing.Icon
 * @param arg2 boolean
 */
public CoRadioButton(Icon icon, boolean state) {
	super(icon, state);
}
/**
 * @param aGroup SE.corren.calvin.userinterface.CoButtonGroup
 */
public CoButtonGroup getButtonGroup () {
	return buttonGroup;
}
/**
 * @param aGroup SE.corren.calvin.userinterface.CoButtonGroup
 */
public void setButtonGroup ( CoButtonGroup aGroup) {
	buttonGroup = aGroup;
	aGroup.add(this);
}
}
