package com.bluebrim.xml.shared;

import java.io.*;

import com.bluebrim.base.shared.*;

/**
 * Abstract output generator. 
 * Creation date: (1999-09-10 13:19:25)
 * @author: Mikael Printz
 */
public abstract class CoAbstractOutputGenerator implements CoOutputGeneratorIF {
	protected CoOutputGeneratorIF.OutputParameterIF m_outputParameter;

	public boolean supportsOutputAgain() {
		return false;
	}

	public CoAbstractOutputGenerator() {
		m_outputParameter = createOutputParameter();
	}

	public void addPropertyChangeListener(CoPropertyChangeListener l) {
	}

	public Object clone() throws CloneNotSupportedException {
		CoAbstractOutputGenerator clone = (CoAbstractOutputGenerator) super.clone();
		clone.m_outputParameter = clone.createOutputParameter();
		return clone;
	}

	/**
	 *	Override in subclasses that need output parameters
	 *
	 */
	public CoOutputGeneratorIF.OutputParameterIF createOutputParameter() {
		return null;
	}

	/**
	 *	To be implemented in subclasses where the output is saved
	 *  after execution. Can be used to purge the database of any
	 *  old saved output from output generators
	 */
	public abstract void dropOutput();

	public CoOutputGeneratorIF.OutputParameterIF getOutputParameter() {
		return m_outputParameter;
	}

	/**
	 * output method comment.
	 */
	public void output(OutputStream os) throws CoOutputGenerationException {
		output(os, CoReportConstants.NONE);
	}

	/**
	 * output method comment.
	 */
	public void output(Writer writer) throws CoOutputGenerationException {
		output(writer, CoReportConstants.NONE);
	}

	/**
	 *	Returns true by default; most output generators do produce output
	 */
	public boolean producesOutput() {
		return true;
	}

	public void removePropertyChangeListener(CoPropertyChangeListener l) {
	}

	public void setOutputParameter(CoOutputGeneratorIF.OutputParameterIF outputParameter) {
		m_outputParameter = outputParameter;
	}
}