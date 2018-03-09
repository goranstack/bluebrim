package com.bluebrim.transact.shared;

import com.bluebrim.extensibility.shared.*;
import com.bluebrim.system.shared.*;

/**
 * Service for various kinds of references.
 * 
 * May be moved to own domain ...
 * 
 * @author Markus Persson 2002-09-18
 */
public class CoRefService {
	private static CoRefSPI PROVIDER = (CoRefSPI) CoServices.getPreferredProvider(CoRefSPI.class);
	private static boolean AVAILABLE = CoServices.isReal(PROVIDER);

	/**
	 * Prevent instantiation of this class.
	 * (Possibly, in the future, it will create one instance of itself
	 * and hand it to some maintainer for the ability to reinitialize
	 * its provider. This requires implementing a trivial interface.)
	 */
	private CoRefService() {
	}

	/**
	 * Whether this service is available or not. If it isn't, all other
	 * public static methods of this class will throw a
	 * <code>CoServiceNotAvailableException</code>.
	 */
	public static boolean isAvailable() {
		return AVAILABLE;
	}

	// Temp below ...

	public static CoGOI createGOI() {
		// Create a new GOI with the specific context and a generated COI
		long coi = COI_BASE++;
//		long coi = (getSessionId() << 32) + COI_BASE++;
		return new CoGOI(getSpecificContext(), coi);
	}
	
	private static CoIdentityManager ID_MAN = new CoIdentityManager();
	private static long COI_BASE = 0L;

	public static CoSpecificContext getSpecificContext() {
		return ID_MAN.getBaseContext();
	}

	public static CoSpecificContext lookupSpecificContext(String scStr) {
		CoSpecificContext sContext = ID_MAN.findSpecificContext(scStr);
		if (sContext == null) {
			sContext = new CoSpecificContext(scStr);
			ID_MAN.addSpecificContext(sContext);
		}
		return sContext;
	}

}
