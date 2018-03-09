package com.bluebrim.xml.shared;

import java.io.*;
import java.rmi.*;

import com.bluebrim.base.shared.*;

/**
 * Interface for the output generators
 * Creation date: (1999-09-10 13:19:25)
 * @author: Mikael Printz
 */
public interface CoOutputGeneratorIF extends Remote, CoNamed, Cloneable, CoFactoryElementIF, CoOutputGeneratorConstants {
	public interface OutputParameterIF extends Remote, CoFactoryElementIF {
	}

	public boolean supportsOutputAgain();

	public Object clone() throws CloneNotSupportedException;

	public OutputParameterIF createOutputParameter();

	public void dropOutput();

	public OutputParameterIF getOutputParameter();

	public boolean isPropertyBased();

	/**
	 *	Generates output on the stream
	 *	
	 */
	public void output(OutputStream os) throws CoOutputGenerationException;

	/**
	 *	Generates output on the stream
	 *	
	 */
	public void output(OutputStream os, int options) throws CoOutputGenerationException;

	/**
	 *	Generates output on the writer
	 *	
	 */
	public void output(Writer writer) throws CoOutputGenerationException;

	/**
	 *	Generates output on the writer
	 *	
	 */
	public void output(Writer writer, int options) throws CoOutputGenerationException;

	public boolean producesOutput();

	public void setOutputParameter(OutputParameterIF outputParameter);
}