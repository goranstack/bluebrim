package com.bluebrim.extensibility.shared;

/**
 * Exception indicating that (server) initialization
 * had failures.
 * 
 * @author Markus Persson 2002-05-22
 */
public class CoInitFailedException extends RuntimeException {
	private String m_persister;
	private int m_failed;
	private String m_failLog;

	public CoInitFailedException(String persister, int failed, String failLog) {
		super(persister + " init had " + failed + "failures.");
		m_persister = persister;
		m_failed = failed;
		m_failLog = failLog;
	}
	
	public String getPersisterName() {
		return m_persister;
	}
	
	public int getFailCount() {
		return m_failed;
	}
	
	public String getFailLog() {
		return m_failLog;
	}
}
