package com.bluebrim.ant.tasks;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import org.apache.tools.ant.*;


/**
 * Deletes cvs-dir in the build server for projects whose psf-file have been modified.
 * DeleteCvsDirIfPsfModified would be a more descriptive class name, but the ant task
 * name (which is in lowercase) looks really funny.
 * 
 * Parameters:<ul>
 * <li>psfDir (mandatory): dir for psf-files, e.g. "BuildServer/psf-files".
 * <li>cvsDir (mandatory): cvs dir, e.g. "BuildServer/cvs-home".
 * </ul> 
 * <p>Example:
 * <pre>&lt;psfsync psfdir="${psf-dir}" cvsdir="${cvs-dir}" /&gt;</pre>
 */
public class PsfSync extends Task
{
	private File psfDir;
	private File cvsDir;
	
	public void setPsfDir(File psfDir)
	{
		this.psfDir = psfDir;		
	}
	
	public void setCvsDir(File cvsDir)
	{
		this.cvsDir = cvsDir;		
	}

	public void execute() throws BuildException
	{
		if (psfDir == null)
			throw new BuildException("psfDir attribute must be set.", getLocation());
		if (!psfDir.isDirectory())
			throw new BuildException("psfDir not found or is not a directory: " + psfDir.getAbsolutePath(), getLocation());

		if (cvsDir == null)
			throw new BuildException("cvsDir attribute must be set.", getLocation());
		if (!cvsDir.isDirectory())
			throw new BuildException("cvsDir not found or is not a directory: " + cvsDir.getAbsolutePath(), getLocation());
		
		File[] psfFiles = getPsfFiles();
		List syncedPsfFiles = new ArrayList();
		List syncedWithRemovePsfFiles = new ArrayList();		
		List failedPsfFiles = new ArrayList();
		Throwable lastException = null;
		for (int i = 0; i < psfFiles.length; i++)
		{
			File psfFile = psfFiles[i];
			try
			{
				if (removeCvsDir(psfFile))
					syncedWithRemovePsfFiles.add(psfFile);
				else
					syncedPsfFiles.add(psfFile);
			}
			catch (Throwable e)
			{
				failedPsfFiles.add(psfFile);
				lastException = e;
			}
		}

		List allSyncedPsfFiles = new ArrayList(syncedPsfFiles);
		allSyncedPsfFiles.addAll(syncedWithRemovePsfFiles);
		System.out.println("Synchronized projects: " + getProjectNames(allSyncedPsfFiles));
		if (syncedWithRemovePsfFiles.size() > 0)
			System.out.println("Synchronized projects where cvs-dir have been deleted: " + getProjectNames(syncedWithRemovePsfFiles));

		if (failedPsfFiles.size() > 0)
		{
			String projects = getProjectNames(failedPsfFiles);
			throw new BuildException("Failed to synchronize following projects: " + projects, lastException);
		}
	}
	
	private File[] getPsfFiles()
	{
		FilenameFilter filter = new FilenameFilter()
			{
				public boolean accept(File dir, String name) 
				{
					return name.endsWith(".psf");
				}
			};
			
		return  psfDir.listFiles(filter);
	}
	
	private boolean removeCvsDir(File psfFile) throws BuildException
	{
		debug("removeCvsDir begins; psfFile: " + psfFile.getAbsolutePath());

		boolean result = false;
		File psfOldFile = new File(psfFile.getAbsolutePath() + ".old");
		if (!psfOldFile.exists() || !isContentEquals(psfFile, psfOldFile))
		{
			String projectName = getProjectName(psfFile);
			if ((projectName == null) || (projectName.length() == 0))
				throw new BuildException("Can not extract project name from psf-file: " + psfFile, getLocation());
		
			String dirToRemove = cvsDir + "/" + projectName; 
			try
			{
				debug("Removing dir: " + dirToRemove);
				File file = new File(dirToRemove);
				if (file.exists())
					deleteDir(file);
				result = true;
			}
			catch (IOException e)
			{
				throw new BuildException("Can not delete directory: " + dirToRemove, e);
			}
		}
		return result;
	}
	
	private boolean isContentEquals(File file1, File file2) throws BuildException
	{
		debug("isContentEquals begin; file1: " + file1.getAbsolutePath());
		debug("isContentEquals begin; file2: " + file2.getAbsolutePath());
		
		String filename = null;
		CheckedInputStream cis = null;
		byte[] buf = null;
		try
		{
			// file1
			filename = file1.getAbsolutePath();
			cis = new CheckedInputStream(new FileInputStream(file1), new Adler32());
			buf = new byte[128];
			while (cis.read(buf) >= 0)
			{
				// empty
			}
			long checksum1 = cis.getChecksum().getValue();
			cis.close();
			
			// file2
			filename = file2.getAbsolutePath();
			cis = new CheckedInputStream(new FileInputStream(file2), new Adler32());
			buf = new byte[128];
			while (cis.read(buf) >= 0)
			{
				// empty
			}
			long checksum2 = cis.getChecksum().getValue();

			debug("checksum1=" + checksum1);
			debug("checksum2=" + checksum2);

			return (checksum1 == checksum2);
		}
		catch (IOException e)
		{
			throw new BuildException("Can not calculate checksum for file: " + filename, e);
		}
		finally
		{
			try { cis.close(); } catch (Throwable ignored) { }
		}
	}
	
	private String getProjectNames(List fileList)
	{
		String result = "";
		for (Iterator iter = fileList.iterator(); iter.hasNext(); )
		{
			File file = (File)iter.next();
			if (result.length() > 0)
				result += ", ";
			result += getProjectName(file); 	
		}
		return result;
	}
	
	private String getProjectName(File file)
	{
		String result = null;
		String filename = file.getName();
		int lastIndex = filename.lastIndexOf('.');
		if (lastIndex != -1)
			result = filename.substring(0, lastIndex);

		return result;
	}
	
	/** Deletes specified directory and its sub-direcrories. */
	private void deleteDir(File dir) throws IOException
	{
		if (dir.isDirectory())
		{
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++)
			{
				File file = new File(dir, children[i]);
				if (file.isDirectory())
					deleteDir(file);
				else
				{
					if (!file.delete())
						throw new IOException("Can not delete file: " + file.getAbsolutePath());
				}
			}
		}
		else
			throw new IOException("Specified directory is missing, or not a directory: " + dir.getAbsolutePath());

		if (!dir.delete())
			throw new IOException("Can not delete directory: " + dir.getAbsolutePath());
	}

	private void debug(String msg)
	{
		if (false)
			System.out.println("DEBUG: " + msg);
	}

}
