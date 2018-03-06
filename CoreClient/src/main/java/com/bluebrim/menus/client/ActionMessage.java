package com.bluebrim.menus.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
/** 
	En klass som lyssnar efter ActionEvents, t ex vid ett menyval.
	När ett event fångas upp så skickas ett meddelande,'message',
	i väg som hanterar menyvalet.<br>
	Detta sätt att hantera ex menyval kommer troligen att ersättas 
	av en anonym ActionListener när VAJ stödjer inre klasser 
	.
*/  
public class ActionMessage implements ActionListener {
protected   Message message;
	/** Create a action message with the specified receiver, method
	and arguments.
	*/
	public ActionMessage(Object receiver, Method method, Object args[]) {
		this.message = new Message(receiver, method, args);
	}
	/** Create a action message with the specified receiver, method
	and arguments.
	*/
	public ActionMessage(Object receiver, Method method, Object arg) {
		this.message = new Message(receiver, method, (new Object[] {arg}));
	}
	/** Create a action message with the specified receiver, method
	and arguments.
	*/
	public ActionMessage(Object receiver, String methodName) {
		this(receiver,methodName, null);
	}
	/** Create a action message with the specified receiver, method
	and arguments.
	*/
	public ActionMessage(Object receiver, String methodName, Object args[]) {
		createMessage(receiver,methodName, args);
	}
	/** Create a action message with the specified receiver, method
	and arguments.
	*/
	public ActionMessage(Object receiver, String methodName, Object arg) {
		createMessage(receiver,methodName, (new Object[] {arg}));
	}
	/** Create a action message with the specified message.
	*/
	public ActionMessage(Message message) {
		this.message = message;
	}
	/**
	 	Anropas när ett event vi lyssnar efter har postats.
	*/
	public void actionPerformed(ActionEvent e)	{
		try {		
			this.message.send();
		} catch (Exception ex) {
			System.out.println("Error executing the message:");
			System.out.println("    "  + this.message.getMethod().toString());
			ex.printStackTrace();
		}
	}
	/** 
		Skapa ett actionmeddelande med den angivna mottagaren, metoden och argumenten.
		'args' kan vara null.
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
}
