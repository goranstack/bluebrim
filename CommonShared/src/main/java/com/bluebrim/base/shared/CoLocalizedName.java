package com.bluebrim.base.shared;

import com.bluebrim.resource.shared.*;

/**
 * Class that can deliver a localized name to be displayed to the user.
 *
 * @author Markus Persson 1999-05-17
 */
public class CoLocalizedName extends CoAbstractLocalizedName {
	private Class m_resourceClass;	
public CoLocalizedName(Class resourceClass, String nameKey) {
	super(nameKey);
	m_resourceClass = resourceClass;
}
public String getName() {
	return CoOldResourceBundle.getString(m_resourceClass, getKey());
}
}
