package com.bluebrim.gui.client;

import javax.swing.ButtonModel;
/**
 * Ett event som skickas från en CoButtonGroup när en ny knapp
 * har selekterats.
 * <p>
 * Fångas upp av alla CoSelectedButtonListener.
 * 
 */
public class CoEnableDisableEvent extends java.util.EventObject {
	ButtonModel selectedButton;
	boolean	enable;
/**
 * This method was created by a SmartGuide.
 */
public CoEnableDisableEvent ( Object source) {
	super(source);
	enable(true);
}
/**
 * This method was created by a SmartGuide.
 */
public CoEnableDisableEvent ( Object source, boolean enable) {
	this(source);
	enable(enable);
}
/**
 * This method was created by a SmartGuide.
 */
public boolean enable () {
	return enable;
}
/**
 * This method was created by a SmartGuide.
 */
public void enable (boolean enable) {
	this.enable = enable;
}
}
