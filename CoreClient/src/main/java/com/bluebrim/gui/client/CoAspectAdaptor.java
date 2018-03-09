package com.bluebrim.gui.client;

import java.beans.*;

import com.bluebrim.base.shared.*;

/**
 * An abstract value model that represents a value taken from an <code>aspect</code> in 
 * a business object, its <code>m_subject</code>. The acces to this aspect is implemented 
 * via two the two abstract methods <code>get(CoObjectIF)</code> and 
 * <code>set(CoObjectIF, Object)</code> that must be implemented in the any concrete subclass.
 * Normally a subclass is implemented as an anonymous, inner class.
 * <br>
 * The most recently read value is cached in the instance variable <code>m_cachedValue</code>.
 * This variable is reset when the adaptor is notified of a change in the domain object 
 * and then updated when the value of the aspect is once again read from domain obejct.
 * From the beginning the value is set to NULL_VALUE, indicating that it has not been set.
 * <br>
 * An aspect adaptor has a component implementing the <code/>CoValueable</code> interface, 
 * the <code>m_context</code>,
 * The contexts value is the <code>m_subject</code> which is accessed via its 
 * <code>getValue</code> method. In most cases <code>m_context</code> is the 
 * userinterface containing the aspect adaptor.
 * <p>
 * Validation of a value can, in present day situation, only be had for those aspect 
 * adaptors that are connected to a textfield. The <code>validate(Object)</code> method is 
 * called from the converter object, chained to the aspect adaptor when it is
 * connected to a textfield, with the new value as an argument. 
 * As default no validation is done in <code>validate</code> but all values are accepted.
 * <br>
 * A real validation can be done by
 * <ol>
 * <li>either reimplement <code>validate(CoObjectIF ,Object)</code> and throw a <code>PropertyVetoException</code> 
 * if the value isn't accepted
 * <li>or from the outside, add a <code>VetoableChangeListener</code> that is responsible for making the 
 * validation and throwing an exception if the validation fails.
 * </ol>
 * In both cases described above the message in the thrown exception will be shown as an error message to the user.
 * <p>
 * A simple example:
 * <pre><code>
 * builder.createTextFieldAdaptor(builder.addAspectAdaptor(new CoAspectAdaptor(this, FIRST_NAME) {
 * 	public void set(CoObjectIF subject, Object value)
 * 	{
 * 		((com.bluebrim.user.shared.CoUserAccountIF) subject).setFirstName(value != null ? value.toString() : null);
 * 	}
 * 	public Object get(CoObjectIF subject)
 * 	{
 * 		return ((com.bluebrim.user.shared.CoUserAccountIF) subject).getFirstName();
 * 	}
 * }), (JTextField) getNamedWidget(FIRST_NAME));
 * </code></pre>
 * <br>
 * Note that as a default the adaptor isn't transaction savvy, eg the changes done to the aspect of the domain object
 * is outside of a transaction and therefore not permanent.
 * <br>
 * @author Lasse Svadängs
 */
public abstract class CoAspectAdaptor extends CoValueModel
{
	protected 	CoValueable 		m_context;
	protected 	CoObjectIF  		m_subject;
	protected 	Object 				m_cachedValue;
	private	  	CoValueListener		m_contextValueListener;
	public static Object NULL_VALUE = new Object();

protected CoAspectAdaptor(String name) 
{
	super(name);
	m_cachedValue = NULL_VALUE;
}
public CoAspectAdaptor(CoValueable context, String name) 
{
	super(name);
	setContext(context);
	m_context.addValueListener(new CoValueListener() {
		public void valueChange(CoValueChangeEvent e)
		{
			handleContextChange(e);
		}
	});
}
/**
 * @deprecated as of 000210.
 */
public CoAspectAdaptor(CoValueable context, String name,boolean subjectFiresChange) 
{
	this(context,name);
}
protected PropertyChangeEvent createVetoableChangeEvent(Object oldValue, Object newValue)
{
	return new CoPropertyChangeEvent(this,m_subject,getValueName(),getValueName(), oldValue, newValue);
}
/**
	Needs to be implemented to return the 
	value of the aspect in the business object.
 */
protected abstract Object get(CoObjectIF subject);
/**
 */
protected final CoValueable getContext () 
{
	return m_context;
}
/**
 * Answer with the interval value. This is always the same as <code>getValue()</code> except
 * when there is a null value, when this returns NULL_VALUE and <code>getValue()</code>
 * returns null
 */
protected Object getInternalValue()
{
	return  m_cachedValue != NULL_VALUE ? m_cachedValue : (m_cachedValue = getValueFor(getSubject()));
}
protected final CoObjectIF getSubject( ) 
{
	return m_subject;
}
/**
 * 	Answer with value of the aspect. If the cached value needs refreshing do so
 * by asking the subject.
 */
public Object getValue()
{
	Object value = getInternalValue();
	return value != NULL_VALUE ? value : null;
}
/**
 * Svara med värdet av min aspekt i verksamhetsobjektet 'aSubject'.
 */
protected Object getValueFor(CoObjectIF aSubject)
{
		
	if (aSubject != null)
	{
//		if (CoAssertion.TRACE)
//			CoAssertion.trace("getValueFor() --> "+getValueName());
		return get(aSubject);
	}
	else
		return NULL_VALUE;
}
/**
 * The method is called from <code>valueChange(CoValueChangeEvent)</code>
 * to handle a possible change of subject,e g the domain object whose aspect
 * the adaptor is watching has been exchanged.
 */
protected void handleNewSubject(CoObjectIF newSubject)
{
	
	if (m_subject != newSubject)
	{
		Object oldValue = getValue();
		setSubject(newSubject);
		Object newValue = getValue();
		if (hasValueChanged(oldValue, newValue))
			handleNewValue(oldValue, newValue);
	}
}
/**
 */
protected void handleNewValue(Object oldValue, Object newValue)
{
	m_cachedValue = newValue;
	if (hasValueChanged(oldValue, newValue))
		fireValueChangeEvent(oldValue, newValue);
}
/**
 * Called from <code>valueChange(CoValueChangeEvent)</code> when 
 * the property changed equals <code>PROPERTY_CHANGE</code>.
 * Should probably be declared as deprecated.
 */
protected void handlePropertyChange( CoValueChangeEvent anEvent)
{
	resetCachedValue();
	handleNewValue(null, getValue());
}
/**
 * Called from <code>valueChange(CoValueChangeEvent)</code> when the 
 * property changed equals <code>UPDATE</code>. 
 * This is the case when , for example, domain object in the 
 * userinterface model has been replaced.
 */
protected void handleUpdate( CoValueChangeEvent anEvent)
{
	Object oldSubject 	= getSubject();
	Object newSubject 	= anEvent.getNewValue();
	if (oldSubject != newSubject)
		handleNewSubject((CoObjectIF )newSubject);
	else
	{
		// Made some changes to detect when
		// the value changes to null from not null
		Object cached = m_cachedValue != NULL_VALUE ? m_cachedValue : null;
		resetCachedValue();
		Object newValue = getValue();
		handleNewValue(newValue == null ? cached : null, newValue);
	}
}
/**
 * The window showing the userinterface 
 * model is closing.
 */
protected void handleWindowClosing( CoValueChangeEvent anEvent)
{
	setSubject(null);
}

protected boolean hasValueChanged(Object oldValue, Object newValue)
{
	return  (newValue != null && !newValue.equals(oldValue)) || 
			(oldValue == null) && (newValue != null) || 
			(newValue == null) && (oldValue != null);
}
/**
 * This method is to be called when we just want to set
 * the cached value and not update the domain object.
 * <br>
 * This can for example be the case when we have read all data
 * for a given server object in one go from the server and want
 * to set the value models with these values.
 * <br>
 * It's possible that the subject has changed as well so we have to
 * check that.
 */
public void initValue(Object aValue) 
{
	
	Object oldValue 	= m_cachedValue;

	CoObjectIF subject 	= (CoObjectIF )getContext().getValue();
	if (m_subject != subject)
	{
		setSubject(subject);	// setSubject resets m_cachedValue;
		m_cachedValue = oldValue;
	}

	oldValue = oldValue == NULL_VALUE ? null : oldValue;
	if (hasValueChanged(oldValue, aValue))
	{
		m_cachedValue = aValue;
		handleNewValue(oldValue, aValue);
	}	
}
protected boolean needToChangeValue(Object oldValue,Object newValue) 
{
	return  hasSubject() && hasValueChanged(oldValue, newValue);
}
protected void resetCachedValue() 
{
	m_cachedValue 	= NULL_VALUE;
}
/**
 * Must be implemented to set the value of 
 * the aspect in the domain object.
 * <br>
 * NOTE: Not supposed to be called from outside
 * of this class. Only public so it can be called 
 * from "friends" to this class. 
 */
public abstract void set(CoObjectIF subject, Object value);
/**
 * Set the value of <code> m_context</code>, eg normally
 * the instance of <code>CoDomainUserInterface</code> that
 * this aspect adaptor is working in.
 */
protected final void setContext (CoValueable aContext) 
{
	m_context = aContext;
	setSubject(m_context != null ? (CoObjectIF)m_context.getValue() :null);
}
protected final void setSubject(CoObjectIF aSubject) 
{
	m_subject 		= aSubject;
	resetCachedValue();
}
/**
 * Update, if a new value, the aspect in the domain object 
 * with <code>aValue</code> and notify all objects
 * listening for changes to this aspect.
 */
public void setValue(Object aValue) 
{
	Object tOldValue = getValue();
	if (needToChangeValue(tOldValue, aValue))
	{
		set(m_subject, aValue);
		handleNewValue(tOldValue, aValue);
	}	
}
/**
 * Called to validate the new value <code>value</code> before the domain object 
 * is updated. 
 * The validation is done in two steps
 * <ol>
 * <li>the validation is first done by the receiver by calling <code>validate(Object)</code>.
 * If the validation is successfull, eg no <code>PropertyVetoException</code> was thrown 
 * <li> <code>super.validate(Object)</code> is called a so any registered 
 * <code>VetoableChangeListener</code> get the chance to veto the new value. 
 * </ol>
 */
public Object validate(Object value) throws PropertyVetoException
{
	return (m_subject != null) ? super.validate(validate(m_subject, value)) : value;

}
/**
 * Called from <code>validate</code> and is a NOP. Reimplemented by those subclasses that needs to do a real
 * validation of the new value before the business object <code>aSubject</code> is updated, for example by letting
 * the business object itself validate. 
 * <br>
 * If the validation fails a <code>PropertyVetoException</code> needs to be thrown with a <code>message</code>
 * argument that will be shown to the user as an error message.
 */
protected Object validate(CoObjectIF aSubject,Object newValue) throws PropertyVetoException
{
	return newValue;
}

public void valueHasChanged()
{
	resetCachedValue();
	super.valueHasChanged();
}


/**
 * This method is dispatched to when the listener listening after changes in 
 * the context is notified of a change.
 *
 * There can several reasons for this notification to get triggered:
 * <ul>
 * <li>the window displaying the userinterface is about to be closed. 
 * The subject must then be reset to stop all further listening for changes
 * to the aspect
 * <li>a property has been changed in the domain object. This case
 * should be declared as deprecated.
 * <li>there's has been a general change to the subject. This is dispatched
 * from the context in its <code>valueHasChanged</code> method by a call to
 * <code>fireValueChangeEvent</code> with the property name in the event
 * set to <code>UPDATE</code>.
 * <li>the context, eg the userinterface, gets a new domain.
 * The adaptor must then update its subject, check to see if this means a 
 * new value and if this is the case, notify all listeners.
 * </ul>
 *
 * This method is meant to reimplemented in subclasses that needs another
 * behavior in case of context change.
 */
protected void handleContextChange( CoValueChangeEvent anEvent)
{
	if (anEvent.isWindowClosingEvent())
		handleWindowClosing(anEvent);
	else if (anEvent.isPropertyChangeEvent())	
		handlePropertyChange(anEvent);
	else if (anEvent.isUpdateEvent())	
		handleUpdate(anEvent);
	else
		handleNewSubject((CoObjectIF )anEvent.getNewValue());
}


protected final boolean hasSubject( ) 
{
	return m_subject != null;
}
}