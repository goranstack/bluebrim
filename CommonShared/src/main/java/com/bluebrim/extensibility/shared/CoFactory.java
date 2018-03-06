package com.bluebrim.extensibility.shared;

import java.util.*;

/**
 * Single point of entry for generic factory functionality.
 *
 * To be implemented ...
 * 
 * @author Markus Persson 2002-03-07
 */
public class CoFactory {
	/** When the creation location doesn't matter. */
	public static final int ANYWHERE = 0;
	/** Create in local VM. */
	public static final int LOCAL = 1;
	/** Create in client VM. Could be problematic sometimes (Exception). */
	public static final int CLIENT = 2;
	/** Create in server VM. */
	public static final int SERVER = 3;

	/** Mainly for internal use */
	public static final Class[] PARAM_VOID = new Class[0];
	public static final Class[] PARAM_STRING = new Class[] {String.class};
	/** Mainly for internal use */
	public static final Object[] ARGS_VOID = new Object[0];


	// PENDING: Use SPI and location specific implementations.
	private static CoFactory INSTANCE = new CoFactory();
	
	private Map m_contractors = new HashMap();
	
	
	public static Object createImpl(int where, Class toImplement, Class[] argTypes, Object[] args) {
		return INSTANCE.create(where, toImplement, argTypes, args);
	};

	public static Object createImpl(int where, Class toImplement) {
		return INSTANCE.create(where, toImplement, PARAM_VOID, ARGS_VOID);
	};

	public static Object createImpl(int where, Class toImplement, String constructorArg) {
		return INSTANCE.create(where, toImplement, PARAM_STRING, new Object[] {constructorArg});
	};
	
	public static boolean hasImpl(int where, Class toImplement) {
		return INSTANCE.hasContractor(where, toImplement);
	};

	
	// Instance methods

	public void add(String toImplement, CoSubcontractor contractor) {
		Object old = m_contractors.put(toImplement, contractor);
		if (old != null) {
			System.out.println("Multiple implementations of " + toImplement
			+ " (" + contractor + " and " + old + ") which isn't supported yet.");
		}
	}

	public Object create(int where, Class toImplement, Class[] argTypes, Object[] args) {
		CoSubcontractor contractor = (CoSubcontractor) m_contractors.get(toImplement.getName());
		if (contractor == null) {
			throw new CoFactoryException("No implementation known.");
		}
		return contractor.create(toImplement, args);
	};
	
	public boolean hasContractor(int where, Class toImplement) {
		return m_contractors.containsKey(toImplement.getName());
	};

}
