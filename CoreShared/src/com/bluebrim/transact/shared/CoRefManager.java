package com.bluebrim.transact.shared;

/**
 * Manages com.bluebrim.gemstone.shared.CoRef references and Spid:s. Abstraction
 * to be able to use in Gemstone simulation.
 * 
 * @author Markus Persson 2001-01-03
 */
public abstract class CoRefManager {
	public abstract com.bluebrim.transact.shared.CoRef getRefTo(Object object);

//	public Object resolve(CoGemstoneRef ref) {
//		throw new UnsupportedOperationException(
//			"Gemstone reference type not supported by " + this.getClass().getName() + ".");
//	}

	public Object resolve(CoLocalRef ref) {
		throw new UnsupportedOperationException(
			"Local reference type not supported by " + this.getClass().getName() + ".");
	}
}
