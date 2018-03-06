package com.bluebrim.ant.genclasspath;

import java.io.*;
import java.util.*;

import org.jdom.*;

import com.bluebrim.ant.genclasspath.DependentsSorter.*;


/**
 * Used to generate a <code>build_classpath.xml</code> file for each Eclipse project that participate in the Ant build
 * process. You can exclude Eclipse projects by listing them in <code>projects-ignore.config</code> file. The
 * generation is done by creating a <code>EclipseProject</code> instance for each Eclipse project. These instances
 * parse the <code>.classPath</code> file in the Eclipse project folder. That information is then used to create the
 * <code>build_classpath.xml</code> file by using the JDOM api. Previous <code>build_classpath.xml</code> file is
 * owerwritten without any questions.
 * 
 * @author Göran Stäck
 *  
 */
public class AntClassPathGen
{
	private static final String WORK_SPACE_DIR = "../";
	private static final String PROJECTS_IGNORE_FILENAME = "/projects-ignore.config";

	public File workSpaceDir = new File(WORK_SPACE_DIR);
	private Map projects = new HashMap();
	private List ignoredProjects = null;
	public EclipseProject currentEclipseProject;

	public static void main(String[] args)
	{
		AntClassPathGen instance = new AntClassPathGen();
		instance.run();
	}
	
	protected void log(String s)
	{
		System.out.println(s);
	}

	private void run()
	{
		try
		{
			collectProjects();
			parseClassPathFiles();
			createAntClassPathFiles();
			createBuildXml();
			// The following lines are for testing and are not intended for regular use
			//		printOrderedDependents();
			log("Done");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (currentEclipseProject != null)
				System.err.println("Current project: " + currentEclipseProject);
		}
	}

	public void createAntClassPathFiles() throws IOException, CycleException
	{
		Iterator iterator = projects.values().iterator();
		while (iterator.hasNext())
		{
			this.currentEclipseProject = (EclipseProject)iterator.next();
			this.currentEclipseProject.createAntClassPathFile();
			this.currentEclipseProject = null;
		}
	}

	public void parseClassPathFiles() throws JDOMException, IOException
	{
		Iterator iterator = projects.values().iterator();
		while (iterator.hasNext())
		{
			this.currentEclipseProject = (EclipseProject)iterator.next();
			this.currentEclipseProject.parseClassPathFile();
			this.currentEclipseProject = null;
		}
	}

	private void printOrderedDependents() throws CycleException
	{
		Collection referedProjects = getProject("SingleUserDesktopApp").getProjectsInClasspath();
		Collection projects = DependentsSorter.orderDependents(referedProjects.iterator());
		printProjectCollection(referedProjects);
		log("------------------------------");
		printProjectCollection(projects);
	}

	private void printProjectCollection(Collection projects)
	{
		Iterator iterator = projects.iterator();
		while (iterator.hasNext())
			log(((EclipseProject)iterator.next()).getName());
	}

	public void collectProjects()
	{
		File[] projectFolders = workSpaceDir.listFiles();
		for (int i = 0; i < projectFolders.length; i++)
		{
			String projectName = projectFolders[i].getName();
			if (isProjectIgnored(projectName))
				continue;
			File classPathFile = new File(projectFolders[i], ".classpath");
			if (classPathFile.exists())
			{
				EclipseProject eclipseProject = new EclipseProject(this, projectName, classPathFile);
				projects.put(eclipseProject.getName(), eclipseProject);
			}
		}
	}

	/** Is specified project in ignore list? */
	private boolean isProjectIgnored(String projectName)
	{
		// init?
		if (this.ignoredProjects == null)
		{
			this.ignoredProjects = new ArrayList();
			BufferedReader in = null;
			try
			{
				in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(PROJECTS_IGNORE_FILENAME)));
				String line = null;
				while ((line = in.readLine()) != null)
				{
					if (!(line.startsWith("#") || (line.trim().length() == 0)))
						this.ignoredProjects.add(line);
				}
				in.close();
			}
			catch (IOException e)
			{
				System.err.println("WARNING: Can not read ignore-file: " + PROJECTS_IGNORE_FILENAME);
//				e.printStackTrace();
				try
				{
					in.close();
				}
				catch (Throwable ignored)
				{
				}
			}
		}

		// exists project in ignore list?
		for (Iterator iter = this.ignoredProjects.iterator(); iter.hasNext();)
		{
			String ignoredProject = (String)iter.next();
			if (projectName.equalsIgnoreCase(ignoredProject))
				return true;
		}
		return false;
	}

	public EclipseProject getProject(String name)
	{
		return (EclipseProject)projects.get(name);
	}

	/**
	 * This method is not intended for regular use but can be useful for creating a build.xml file for each project in a
	 * workspace. The <code>resources/template_build.xml</code> are copied to each project and the two
	 * <code>@ProjectName@</code> strings in template file are replaced by the Eclipse project name.
	 * @throws IOException
	 */
	public void createBuildXml() throws IOException
	{
		Iterator iterator = projects.values().iterator();
		while (iterator.hasNext())
		{
			this.currentEclipseProject = (EclipseProject)iterator.next();
			this.currentEclipseProject.createBuildFile();
			this.currentEclipseProject = null;
		}
	}

}
