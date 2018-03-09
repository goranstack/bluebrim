package com.bluebrim.gui.client;

import com.bluebrim.base.shared.CoObjectIF;
/**
 * @author Michael Klimczak
 * 
 */
public class CoPluggableReadOnlyAspectAdaptor extends CoReadOnlyAspectAdaptor{

	CoAccessor accessor;
/**
 * CoPluggableReadOnlyAspectAdaptor constructor comment.
 * @param context com.bluebrim.base.client.CoValueable
 * @param name java.lang.String
 */
public CoPluggableReadOnlyAspectAdaptor(CoValueable context, String name) {
	super(context, name);
}
/**
 * CoPluggableReadOnlyAspectAdaptor constructor comment.
 * @param context com.bluebrim.base.client.CoValueable
 * @param name java.lang.String
 * @param subjectFiresChange boolean
 */
public CoPluggableReadOnlyAspectAdaptor(CoValueable context, String name, boolean subjectFiresChange) {
	super(context, name, subjectFiresChange);
}
/**
 * get method comment.
 */
protected Object get(com.bluebrim.base.shared.CoObjectIF subject){
	
	return (accessor != null)? accessor.get(subject) : null;
}
public void setAccessor(CoAccessor accessor) {
	Object tOldValue 		= null;
	this.accessor 			= accessor;
	CoValueable tContext 	= getContext();
	setSubject(tContext!= null ? (CoObjectIF)tContext.getValue() :null);
	Object tNewValue		= getValue();
	if (hasValueChanged(tOldValue, tNewValue))
		fireValueChangeEvent(tOldValue, tNewValue);

}
}
