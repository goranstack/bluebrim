package com.bluebrim.base.client.command;

import java.awt.ActiveEvent;

import com.bluebrim.base.client.CoCloseWindowCommand;
/**
 En subklass till AWTEvent som implementerar ActiveEvent och som anv�nds tillsammans med CoPostEventCommand 
 och CoDispatchEventCommand. 
 Detta event kan postas i min SystemEventQueue fr�n ett CoPostEventCommand eller
 dispatchas fr�n ett CoDispatchEventCommand.
 @see CoCloseWindowCommand 
*/
public abstract class CoEvent extends java.awt.AWTEvent implements ActiveEvent {
/**
 * CoCloseWindowEvent constructor comment.
 * @param source java.lang.Object
 * @param id int
 */
public CoEvent(Object source, int id) {
	super(source, id);
}
}
