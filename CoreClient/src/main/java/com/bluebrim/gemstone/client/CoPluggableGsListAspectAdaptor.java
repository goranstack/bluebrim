package com.bluebrim.gemstone.client;

import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.gui.client.CoAbstractListAspectAdaptor;
import com.bluebrim.gui.client.CoAccessor;
import com.bluebrim.gui.client.CoValueable;
/**
 * Subklass till <code>CoAbstractListAspectAdaptor.Default</code> som använder en instans
 * av CoAccessor för att hämta och sätta listan.
 * Genom att byta  accessor så kan värdeobjektet i runtime ändra dett sätt 
 * på vilket den hämtar listan.
 */
public class CoPluggableGsListAspectAdaptor extends CoAbstractListAspectAdaptor.Default {
	CoAccessor accessor;
/**
 * CoPluggableGsListAspectAdaptor constructor comment.
 * @param context com.bluebrim.base.client.CoValueable
 * @param name java.lang.String
 */
public CoPluggableGsListAspectAdaptor(com.bluebrim.gui.client.CoValueable context, String name) {
	super(context, name);
}
/**
 * CoPluggableGsListAspectAdaptor constructor comment.
 * @param context com.bluebrim.base.client.CoValueable
 * @param name java.lang.String
 * @param subjectFiresChange boolean
 */
public CoPluggableGsListAspectAdaptor(com.bluebrim.gui.client.CoValueable context, String name, boolean subjectFiresChange) {
	super(context, name, subjectFiresChange);
}
/**
 * get method comment.
 */
protected Object get(CoObjectIF subject) {
	return (accessor != null)? accessor.get(subject) : null;
}
public void set(CoObjectIF subject, Object value) {}
/**
  */
public void setAccessor(CoAccessor accessor) {
	this.accessor = accessor;
	CoValueable tContext = getContext();
	handleNewSubject(tContext != null ? (CoObjectIF)tContext.getValue() :null);
}
}
