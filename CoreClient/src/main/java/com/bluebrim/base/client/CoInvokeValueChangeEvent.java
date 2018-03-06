package com.bluebrim.base.client;

import com.bluebrim.gui.client.CoDomainUserInterface;
import com.bluebrim.gui.client.CoValueChangeEvent;
/**
 */
public class CoInvokeValueChangeEvent implements java.lang.Runnable {
	CoDomainUserInterface userInterface;
public CoInvokeValueChangeEvent(CoDomainUserInterface userInterface)
{
	this.userInterface	= userInterface;
}
/**
 */
public void run() {
	System.out.println("=>CoInvokeValueChangeEvent.run");
	userInterface.fireValueChangeEvent(new CoValueChangeEvent(userInterface, CoValueChangeEvent.PROPERTY_CHANGE , null, userInterface.getDomain()));
	System.out.println("<=CoInvokeValueChangeEvent.run");}
}
