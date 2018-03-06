package com.bluebrim.gui.client;

import com.bluebrim.base.shared.CoObjectIF;
/**
 * Subclass to <code>CoAbstractListAspectAdaptor</code> using an instance 
 * of <code>CoAccessor</code> to get the list.
 * <br>
 * By changing the accessor it's possible at runtime to change the way 
 * the adaptor accesses its list.
 */
public class CoPluggableListAspectAdaptor extends CoAbstractListAspectAdaptor.Default {
	private CoAccessor m_accessor;
/**
 * CoPluggableGsListAspectAdaptor constructor comment.
 * @param context com.bluebrim.base.client.CoValueable
 * @param name java.lang.String
 */
public CoPluggableListAspectAdaptor(com.bluebrim.gui.client.CoValueable context, String name) {
	super(context, name);
}
public CoPluggableListAspectAdaptor(CoValueable context, String name, CoAccessor accessor) {
	super(context, name);
	m_accessor = accessor;
}
/**
 * get method comment.
 */
protected Object get(CoObjectIF subject) {
	return (m_accessor != null)? m_accessor.get(subject) : null;
}
public void set(CoObjectIF subject, Object value) {}

public final void setAccessor(CoAccessor accessor) {
	m_accessor 			= accessor;
	CoValueable context = getContext();
	handleNewSubject(context != null ? (CoObjectIF)context.getValue() :null);
}
}
