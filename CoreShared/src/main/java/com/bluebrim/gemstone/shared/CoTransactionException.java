package com.bluebrim.gemstone.shared;

import java.io.*;

/**
 * Exception thrown by <code>CoSession.doInTransaction</code> when 
 * the transaction fails.
 * Creation date: (1999-12-28 12:44:01)
 * @author: Lasse Svadängs
 */
public class CoTransactionException extends Exception {
	public Throwable m_detail;

	public CoTransactionException() {
	}

	public CoTransactionException(String s) {
		super(s);
	}

	public CoTransactionException(String s, Throwable ex) {
		super(s);
		m_detail = ex;
	}

	public CoTransactionException(Throwable ex) {
		super();
		m_detail = ex;
	}

	public String getErrorMessage() {
		return super.getMessage();
	}

	public String getMessage() {
		if (m_detail == null)
			return super.getMessage();
		else
			return super.getMessage() + "; nested exception is: \n\t" + m_detail.toString();
	}

	public void printStackTrace() {
		printStackTrace(System.err);
	}

	public void printStackTrace(PrintStream ps) {
		if (m_detail == null) {
			super.printStackTrace(ps);
		} else {
			synchronized (ps) {
				ps.println(this);
				m_detail.printStackTrace(ps);
			}
		}
	}

	public void printStackTrace(PrintWriter pw) {
		if (m_detail == null) {
			super.printStackTrace(pw);
		} else {
			synchronized (pw) {
				pw.println(this);
				m_detail.printStackTrace(pw);
			}
		}
	}
}
