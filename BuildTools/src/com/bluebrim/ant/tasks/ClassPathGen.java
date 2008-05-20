package com.bluebrim.ant.tasks;

import java.io.*;

import org.apache.tools.ant.*;

import com.bluebrim.ant.genclasspath.*;


public class ClassPathGen extends Task
{
	public void execute() throws BuildException
	{
		AntClassPathGen instance = new AntClassPathGen();
		log("Generating build_classpath.xml for all projects...");
		try
		{
			instance.workSpaceDir = new File(getProject().getBaseDir(), "..");
			instance.collectProjects();
			instance.parseClassPathFiles();
			instance.createAntClassPathFiles();
			log("Done");
		}
		catch (Exception e)
		{
			if (instance.currentEclipseProject != null)
				throw new BuildException("Error in project " + instance.currentEclipseProject, e);
			throw new BuildException("Error", e);
		}
	}
}
