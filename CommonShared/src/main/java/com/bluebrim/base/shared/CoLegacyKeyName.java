package com.bluebrim.base.shared;

/**
 * Highly temoprary class for legacy code that intermixes displayable names
 * for properties with keys used to find the properties.
 *
 * @author Markus Persson 1999-05-17
 */
public class CoLegacyKeyName extends CoPlainName {
public CoLegacyKeyName(String name) {
	super(name);
}
public String getKey() {
// Highly temporary! This is for legacy code. /Markus
	return getName();
}
}
