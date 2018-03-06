package com.bluebrim.base.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 	Representerar ett anrop av metoden 'method' i klassen 'targetClass'.<br>
	Exempel:
	<code><pre>
		Class argTypes[] 		= {String.class};
		CoMessage message		= new CoMessage(Customer.class, "setName",argTypes);
		Object arguments[]	= {"Albert"};
		message.sendTo(new Customer(), arguments).
	</pre></code>
	Detta är det bästa jag kan åstadkomma för att härma VisualWorks #perform:with:!<br>
	Instansvariabler
	<ul>
	<li>	methodName (String) metodnamn
	<li>	targetClass (Class) klassen som implementerar metoden med namnet 'methodName'
	<li>	method (Method) metoden i 'targetClass' med namnet 'methodName'
	<li>	argumentTypes (Class[]) typer/klasser för metodens argument
	</ul>
	@author Lasse Svadängs 971006
 */
public class CoMessage {
	Class		lastTargetClass;
	Class		targetClass;
	String 		methodName;
	Method 		method;
	Class		argumentTypes[]= {};
/**
 * This method was created by a SmartGuide.
 */
public CoMessage ( ) {
}
/**
 * This method was created by a SmartGuide.
 */
public CoMessage ( Class targetClass,String methodName, Class argumentTypes[] ) {
	this();
	setTargetClass(targetClass);
	setLastTargetClass(targetClass);
	setMethodName(methodName);
	setArgumentTypes(argumentTypes);
}
/**
 * This method was created by a SmartGuide.
 @exception NoSuchMethodException
 */
private Method findMethod ()throws NoSuchMethodException {
	return findMethodIn(getTargetClass());
}
/**
 * This method was created by a SmartGuide.
  @exception NoSuchMethodException
 */
private Method findMethodIn (Class aClass)throws NoSuchMethodException {
	method = aClass.getMethod(getMethodName(),getArgumentTypes());
	return method;
}
/**
 * This method was created by a SmartGuide.
 */
protected Class[] getArgumentTypes()
{
	return argumentTypes;
}
/**
 */
private Class getLastTargetClass () {
	return lastTargetClass;
}
/**
 * This method was created by a SmartGuide.
  @exception NoSuchMethodException
 */
private Method getMethodIn (Class aClass)throws NoSuchMethodException {
	Class tClass = aClass;
	Method m = null;
	if (tClass != getLastTargetClass())
		method = null;
	try
	{
		m = (method != null) ? method : findMethodIn(tClass);
	}
	catch (NoSuchMethodException e)
	{
		tClass = tClass.getSuperclass();
		if (tClass != null)
			m =getMethodIn(tClass);
		else
			throw new NoSuchMethodException(e.getMessage());
	}		
	setLastTargetClass(tClass);
	return m;	
}
/**
 * This method was created by a SmartGuide.
 */
protected String getMethodName () {
	return  methodName;
}
/**
 * This method was created by a SmartGuide.
 */
public Class getTargetClass()
{
	return targetClass;
}
/**
 * This method was created by a SmartGuide.
  @exception CoMessageException
 */
public Object sendTo(Object aTarget)throws CoMessageException
{
	Object args[] = {};
	return sendTo(aTarget,args);
}
/**
 * This method was created by a SmartGuide.
 @exception CoMessageException
 */
public Object sendTo(Object aTarget, Object arguments[])throws CoMessageException
{
	Object args[] = {};
	if (arguments != null) 
		args = arguments;
	try
	{
		return aTarget != null ? getMethodIn(aTarget.getClass()).invoke(aTarget,args) : null;
	}
	catch(NoSuchMethodException e)
	{
		throw new CoMessageException(e.getLocalizedMessage());
	}
	catch(IllegalAccessException e)
	{
		throw new CoMessageException(e.getLocalizedMessage());
	}
	catch(InvocationTargetException e)
	{
		throw new CoMessageException(e.getLocalizedMessage());
	}					
}
/**
 * This method was created by a SmartGuide.
 */
public void setArgumentTypes(Class argumentTypes[])
{
	if (argumentTypes == null)
	{
		Class argTypes[] = {};
		this.argumentTypes = argTypes;
	}	
	else
		this.argumentTypes = argumentTypes;
}
/**
 */
private void setLastTargetClass (Class aClass) {
	lastTargetClass = aClass;
}
/**
 * This method was created by a SmartGuide.
 */
public void setMethodName ( String methodName) {
	this.methodName = methodName;
}
/**
 * This method was created by a SmartGuide.
 */
public void setTargetClass(Class targetClass)
{
	this.targetClass = targetClass;
}
}
