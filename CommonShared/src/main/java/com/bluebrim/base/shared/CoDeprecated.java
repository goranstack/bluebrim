package com.bluebrim.base.shared;

/**
 * Class containing static methods that are a refactoring
 * of bad practices spread throughout the code.
 * 
 * The reason for this is that when heavy rework of parts
 * of the code is done one encounters usage of the existing
 * framework that shouldn´t be supported in the future. When
 * it is hard to decide what to replace that code with, because
 * it would take focus from the work in progress, the messy code
 * or suitable replacement could be moved here, until we have
 * time do decide its ultimate fate.
 * 
 * NOTE: This class is not marked as deprecated for now in order
 * not to distract from the real errors.
 * 
 * @author Markus Persson 2002-09-16
 */
public class CoDeprecated {

	/** De not instantiate */
	private CoDeprecated() {
	}

	/**
	 * Replacement for "Gemstone simulation", "system admin" or
	 * "certain user is running" checks that enable various elements
	 * (often UI components) for developer testing purposes.
	 */
	public static boolean isTesting() {
		return true;
	}
}
