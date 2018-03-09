package com.bluebrim.gui.client.test;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.bluebrim.gui.client.CoUserInterface;

/**
 * Collects GUI-classes in class path
 *
 * @author Göran Stäck 2002-08-15
 *
 */
public class CoGuiCollector {

	static final int SUFFIX_LENGTH = ".class".length();

	public CoGuiCollector() {
	}

	public Enumeration collectGUIs() {
		String classPath = System.getProperty("java.class.path");
		String separator = System.getProperty("path.separator");
		Hashtable result = new Hashtable(100);
		collectFilesInRoots(splitClassPath(classPath, separator), result);
		return result.elements();
	}

	private void collectFilesInRoots(Vector roots, Hashtable result) {
		Enumeration e = roots.elements();
		while (e.hasMoreElements())
			gatherFiles(new File((String) e.nextElement()), "", result);
	}

	private void gatherFiles(File classRoot, String classFileName, Hashtable result) {
		File thisRoot = new File(classRoot, classFileName);
		if (thisRoot.isFile()) {
			if (isGuiClass(classFileName)) {
				String className = classNameFromFile(classFileName);
				result.put(className, className);
			}
			return;
		}
		String[] contents = thisRoot.list();
		if (contents != null) {
			for (int i = 0; i < contents.length; i++)
				gatherFiles(classRoot, classFileName + File.separatorChar + contents[i], result);
		}
	}

	private Vector splitClassPath(String classPath, String separator) {
		Vector result = new Vector();
		StringTokenizer tokenizer = new StringTokenizer(classPath, separator);
		while (tokenizer.hasMoreTokens())
			result.addElement(tokenizer.nextToken());
		return result;
	}

	private String classNameFromFile(String classFileName) {
		// convert /a/b.class to a.b
		String s = classFileName.substring(0, classFileName.length() - SUFFIX_LENGTH);
		String s2 = s.replace(File.separatorChar, '.');
		if (s2.startsWith("."))
			return s2.substring(1);
		return s2;
	}

	protected boolean isGuiClass(String classFileName) {
		try {
			if (classFileName.endsWith(".class") && 
				classFileName.indexOf('$') < 0 &&
				classFileName.indexOf(File.separatorChar + "client" + File.separatorChar) > 0 &&
				classFileName.indexOf(File.separatorChar + "test" + File.separatorChar) < 0) 
 
				{
				Class testClass = classFromFile(classFileName);
				return (testClass != null) && isGuiClass(testClass);
			}
		} catch (ClassNotFoundException expected) {
		} catch (NoClassDefFoundError notFatal) {
		}
		return false;
	}

	/**
	 * See if the class is GUI class.
	 */
	protected boolean isGuiClass(Class testClass) {

		if (CoUserInterface.class.isAssignableFrom(testClass) && !Modifier.isAbstract(testClass.getModifiers())){
			System.out.println(testClass.getName());
			return true;
		}
		return false;
	}

	private Class classFromFile(String classFileName) throws ClassNotFoundException {
		String className = classNameFromFile(classFileName);
		try {
			return Class.forName(className);
		} catch(ExceptionInInitializerError e) {
			System.out.println("Den statiska initiering falerar i klassen: " + classFileName);
			throw new ClassNotFoundException();
		}
	}

}