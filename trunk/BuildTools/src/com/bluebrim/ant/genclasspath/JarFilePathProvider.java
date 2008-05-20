package com.bluebrim.ant.genclasspath;

/**
 * Implemented by objects that can provide a jar file path to be used in a include element in a file set in an Ant build
 * file
 * 
 * @author Göran Stäck
 *  
 */
public interface JarFilePathProvider
{
	public String getJarFilePath();
}
