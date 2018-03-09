package com.bluebrim.system.shared;

import java.io.*;
import java.net.*;
import java.util.*;

import com.bluebrim.time.shared.*;

/**
 * CoIdentityManager manages all contexts (CoSpecificContext) that are handled by the system.
 * It has a reference to the specific context created for the installation as well as contexts
 * pertaining to objects that originate from other contexts
 * CoIdentityManager also administrates session numbers, which are used for object id generation
 */
public class CoIdentityManager implements Serializable {

	/**
	 *	Name of the algorithm used for creating GOIs
	 */
	public final static String ALGORITHM = "iptime";
	/**
	 *	Session number
	 */
	private long m_sessionNo = 0;

	/**
	 *	Holds specific contexts
	 */
	private HashMap m_contexts = new HashMap();
	/**
	 *	The specific context that represents this system installation
	 */
	private CoSpecificContext m_baseContext;

	public CoIdentityManager() {
		String ip = "unknown";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch(UnknownHostException uhe) {
			System.out.println("Unknown host exception encountered when determining specific context for installation.");
			System.exit(1);
		}

		m_baseContext = new CoSpecificContext(ALGORITHM, ip + "+" + CoTimeStamp.nowMillis(), "");
	}
	public void addSpecificContext(CoSpecificContext context) {
		m_contexts.put(context.toString(), context);
	}
	public CoSpecificContext findSpecificContext(String scStr) {
		if(scStr.equals(m_baseContext.toString())) {
			return m_baseContext;
		}
		return (CoSpecificContext)m_contexts.get(scStr);
	}
	public CoSpecificContext getBaseContext() {
		return m_baseContext;
	}
	/**
	 *	Returns the next session no
	 */
	public synchronized long getNextSessionNo() {
		return m_sessionNo++;
	}
}