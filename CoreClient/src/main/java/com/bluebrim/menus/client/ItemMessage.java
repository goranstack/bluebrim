package com.bluebrim.menus.client;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
/** 
	This class is used as listener of change item events. When
	a change item event occurs, it invokes the message associated.
*/  
public class ItemMessage implements ItemListener {
protected  Message message;
	/** Create a item message with the specified receiver, method
	and arguments.
	*/
	public ItemMessage(Object receiver, java.lang.reflect.Method method, Object args[]) {
		this.message = new Message(receiver, method, args);
	}
	/** Create a item message with the specified receiver, method
	and arguments.
	*/
	public ItemMessage(Object receiver, String methodName) {
		this(receiver, methodName, (new Object[] {}));
	}
	/** Create a item message with the specified receiver, method
	and arguments.
	*/
	public ItemMessage(Object receiver, String methodName, Object args[]) {
		createMessage(receiver, methodName, args);
	}
	/** Create a item message with the specified receiver, method
	and arguments.
	*/
	public ItemMessage(Object receiver, String methodName, Object arg) {
		createMessage(receiver, methodName, (new Object[] {arg}));
	}
/** Create a item message with the specified message.
	*/
	public ItemMessage(Message message) {
		this.message = message;
	}
	/** Create a action message with the specified receiver, method
	and arguments.
	*/
	public void createMessage(Object receiver, String methodName, Object args[]) {
		Class arg[] = {};
		if (args != null)
		{
			arg = new Class[args.length];
			for (int i = arg.length-1; i>=0; i--)
				arg[i] = args[i].getClass();
		}		
		try
		{
			message = new Message(receiver, receiver.getClass().getMethod(methodName, arg), args);
		}	
		catch(Exception e)
		{
			message = null;
		}		
	}
	/**
	 * Invoked when an item's state has been changed.
	 */
	public void itemStateChanged(ItemEvent e)	{
		try {		
			if (this.message.getArguments() == null) {
				Object args[] = new Object[1];
				args[0] = e;
				this.message.sendWith(args);
			}
			this.message.send();
		} catch (Exception ex) {
			System.out.println("Error executing the message:");
			System.out.println("    "  + this.message.getMethod().toString());
			ex.printStackTrace();
		}
	}
}
