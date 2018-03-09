package com.bluebrim.gui.client;

/**
 * Ett event som postas av ett UserInterface innan
 * det öppnas i ett fönster.
 * 
 */
public class CoPostBuildEvent extends java.util.EventObject {
/**
 * PostBuildEvent constructor comment.
 * @param source java.lang.Object
 */
public CoPostBuildEvent(Object source) {
	super(source);
}
}
