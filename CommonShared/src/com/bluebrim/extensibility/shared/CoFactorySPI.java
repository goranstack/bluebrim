package com.bluebrim.extensibility.shared;

/**
 * SPI for factory service providers.
 * @author Markus Persson 2002-03-11
 */
public interface CoFactorySPI {

	/**
	 * Populate the given factory with subcontractors
	 * to handle all supported interfaces.
	 */
	public void populate(CoFactory factory);

}
