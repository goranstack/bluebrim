package com.bluebrim.system.shared;

import java.io.*;

import com.bluebrim.transact.shared.*;

/**
 * CoGOI (Global Object Identifier) represents a way to globally identify
 * objects.
 */
public class CoGOI implements Serializable {
	/**
	 *	The delimiters below denote the chars that delimit
	 *  the different parts of a GOI in string form.
	 */
	public final static char CONTEXT_DELIMITER = '#';
	public final static char SPECIFIC_CONTEXT_DELIMITER = '/';
	public final static char IP_DELIMITER = '+';
	public final static char ALGORITHM_DELIMITER = ':';
	private CoSpecificContext m_context;
	private long m_coi;

	public CoGOI(String goiStr) {
//		this(CoRefService.lookupSpecificContext(goiStr.substring(0, goiStr.indexOf(CONTEXT_DELIMITER))), Long.parseLong(goiStr.substring(goiStr.indexOf(CONTEXT_DELIMITER) + 1)));
		this(CoRefService.lookupSpecificContext(goiStr.substring(0, goiStr.indexOf(CONTEXT_DELIMITER))), Long.parseLong(goiStr.substring(goiStr.indexOf(CONTEXT_DELIMITER) + 1)));
	}

	public CoGOI(CoSpecificContext context, long coi) {
		m_context = context;
		m_coi = coi;
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof CoGOI)) {
			return false;
		}
		CoGOI aGoi = (CoGOI)obj;
		return m_coi == aGoi.getCoi() && m_context.equals(aGoi.getContext());
	}
	public long getCoi() {
		return m_coi;
	}
	public CoSpecificContext getContext() {
		return m_context;
	}
	public String toString() {
		return m_context.toString() + CONTEXT_DELIMITER + m_coi;
	}
}