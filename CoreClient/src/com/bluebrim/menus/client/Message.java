package com.bluebrim.menus.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/** 
	This is a message that specifies the receiver, method
	and arguments. It can be invoked executing the method
	#send.
*/
public class Message {
protected       Method method;
protected       Object receiver;
protected       Object[] arguments;
	/** Create a new message with the specified receiver, method and
	arguments.
	*/
	public Message(Object receiver, Method method, Object args[]) {
		this.receiver = receiver;
		this.method = method;
		this.arguments = args;
	}
/** Answer the arguments of the message.
	*/
	public Object[] getArguments() {	
		return this.arguments;
	}
/** Answer the method of the message.
	*/
	public Method getMethod() {	
		return this.method;
	}
/** Answer the receiver of the message.
	*/
	public Object getReceiver() {
		return this.receiver;
	}
	/** Invoke the message.
		@exception IllegalAccessException, IllegalArgumentException, InvocationTargetException
	 */
	public Object send() throws IllegalAccessException,
		IllegalArgumentException, InvocationTargetException
	{
		return sendToWith(receiver, arguments);		
	}
	/** Invoke the message.
	@exception IllegalAccessException, IllegalArgumentException, InvocationTargetException
	 */
	public Object sendTo(Object rec) throws IllegalAccessException,
		IllegalArgumentException, InvocationTargetException
	{
		return sendToWith(rec, this.arguments);		
	}
	/** Invoke the message.
	@exception IllegalAccessException, IllegalArgumentException, InvocationTargetException
	 */
	public Object sendToWith(Object rec, Object args[]) throws IllegalAccessException,
		IllegalArgumentException, InvocationTargetException
	{
		return this.method.invoke(rec, args);		
	}
	/** Invoke the message.
	@exception IllegalAccessException, IllegalArgumentException, InvocationTargetException
	 */
	public Object sendWith(Object[] args) throws IllegalAccessException,
		IllegalArgumentException, InvocationTargetException
	{
		return sendToWith(receiver, args);		
	}
/** Set the arguments of the receiver.
	 */
	public void setArguments(Object args[]){
		this.arguments = args;
	}
/** Set the method of the message.
	 */
	public void setMethod(Method method) {
		this.method = method;
	}
/** Set the receiver of the message.
	 */
	public void setReceiver(Object o) {
		this.receiver = o;
	}
}
