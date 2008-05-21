package com.bluebrim.system.shared;

/**
 * CoSpecificContext globally identifies a unique object identification context. This is accomplished through
 * the m_specificSystem member which uniquely identifies a persistant system session and a context for further
 * qualification.
 */
public class CoSpecificContext implements java.io.Serializable {
	private String m_algorithm;
	private String m_specificSystem;
	private String m_context;

	public CoSpecificContext(String GOIStr) {
		this(parseAlgorithm(GOIStr), parseSpecificSystem(GOIStr), parseContext(GOIStr));
	}
	public CoSpecificContext(String specificSystem, String context) {
		m_context = context;
		m_specificSystem = specificSystem;
	}
	public CoSpecificContext(String algorithm, String specificSystem, String context) {
		m_algorithm = algorithm;
		m_specificSystem = specificSystem;
		m_context = context;
	}
	public boolean equals(Object obj) {
		if(!(obj instanceof CoSpecificContext)) {
			return false;
		}
		return toString().equals(obj.toString());
	}
	public int hashCode() {
		return toString().hashCode();
	}
	public static String parseAlgorithm(String GOIStr) {
		return GOIStr.substring(0, GOIStr.indexOf(com.bluebrim.system.shared.CoGOI.ALGORITHM_DELIMITER));
	}
	public static String parseContext(String GOIStr) {
		int end;
		int idx;
		if((idx = GOIStr.indexOf(com.bluebrim.system.shared.CoGOI.CONTEXT_DELIMITER)) == -1) {
			end = GOIStr.length();
		} else {
			end = idx;
		}

		return GOIStr.substring(GOIStr.indexOf(com.bluebrim.system.shared.CoGOI.SPECIFIC_CONTEXT_DELIMITER) + 1, end);
	}
	public static String parseSpecificSystem(String GOIStr) {
		return GOIStr.substring(GOIStr.indexOf(com.bluebrim.system.shared.CoGOI.ALGORITHM_DELIMITER) + 1, GOIStr.indexOf(com.bluebrim.system.shared.CoGOI.SPECIFIC_CONTEXT_DELIMITER));
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(m_algorithm);
		sb.append(":");
		sb.append(m_specificSystem);
		sb.append("/");
		sb.append(m_context);
		return sb.toString();
	}
}
