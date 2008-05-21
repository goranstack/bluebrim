package com.bluebrim.content.impl.client;

import java.io.*;

import com.bluebrim.content.shared.*;
/**
 * This class is providing a platform independant way of 
 * opening a file using the (by the os) associated program.
 * 
 * Creation date: (2001-10-01 15:05:04)
 * @author: Marcus Hirt
 */
public abstract class CoFileOpener 
{
	private final static String OS_PROPERTY = "os.name";
	
	// Different os environment variables.
	private final static String WIN_ID  = "windows";
	private final static String UNIX_ID = "unix"; // Fixme: I'm not sure this is correct.
	private final static String MAC_ID  = "mac";  // Fixme: I'm not sure this is correct.
	
	// Concrete implementation holder
	private static CoFileOpener m_fileOpener;
	
	// Valid os identifiers
	public class OsEnumeration
	{
		public final static int UNKNOWN = -1;
		public final static int WINDOWS =  1;
		public final static int UNIX    =  2;
		public final static int MAC     =  3;
	}

	public CoFileOpener() 
	{
		super();
	}
	
	/**
	 * Returns an int designating the operating system our
	 * virtual machine is currently executing on.
	 * Creation date: (2001-10-01 15:40:55)
	 */
	public static int getCurrentOperatingSystem()
	{
		String os = System.getProperty(OS_PROPERTY);
		if (os == null)
			return OsEnumeration.UNKNOWN;
		os = os.toLowerCase();
		if(os.startsWith(WIN_ID))
			return OsEnumeration.WINDOWS;
		else if(os.startsWith(MAC_ID))
			return OsEnumeration.MAC;
		else if(os.startsWith(UNIX_ID))
			return OsEnumeration.UNIX;
		return OsEnumeration.UNKNOWN;
	}
		
	/**
	 * Returns a file opener valid for the operating system
	 * the calling Virtual Machine is executing on. 
	 * Creation date: (2001-10-01 16:52:49)
	 */
	public static CoFileOpener getFileOpener()
	{
		return getFileOpenerFor(getCurrentOperatingSystem());	
	}

	/**
	 * Returns a file opener for the specified operating system.
	 * Creation date: (2001-10-01 16:57:28)
	 */	
	private static CoFileOpener getFileOpenerFor(int os) throws IllegalArgumentException
	{
		switch (os)
		{
			case OsEnumeration.WINDOWS:
				return new CoFileOpenerWinImpl();
			case OsEnumeration.MAC:
				//return new CoFileOpenerMacImpl();
			case OsEnumeration.UNIX:
				//return new CoFileOpenerUnixImpl();
		}
		throw new IllegalArgumentException("No FileOpener for the specified operating system exists!");
	}

	/**
	 * Returns the identifier of the OS that this instance handles.
	 */
	public abstract int getOpenerType();

	/**
	 * Simple test. Supply the file you wish to open as the first and only argument.
	 * Creation date: (2001-10-01 16:22:33)
	 * @param args java.lang.String[]
	 */
	public static void main(String[] args) 
	{
		if(args.length != 1)
			System.out.println("The first and only argument has to be the file location (or url),");
		try
		{
			CoFileOpener.open(args[0]);
		}
		catch(IOException ioe)
		{
			System.out.println(ioe.toString());
		}
	}

	/**
	 * Convenience method to open a file. The method of choice to open a
	 * file without having to bother with the creation of a CoFileOpenerImpl object.
	 * 
	 * Creation date: (2001-10-01 16:25:29)
	 * @param: String location The location of the file to open.
	 */
	 public static void open(String location) throws IOException
	 {
		 if(m_fileOpener == null)
		 	m_fileOpener = getFileOpener();
		 if(m_fileOpener != null)
		 {
		 	if(m_fileOpener.getOpenerType() != getCurrentOperatingSystem())
			 	m_fileOpener = getFileOpener();
		 	m_fileOpener.openFile(location);
		 }
	 }
	
	/**
	 * The actual implementation of the "open a file" functionality for
	 * a specific target platform. Fixme: Maybe use an URL or File in the future?
	 * Creation date: (2001-10-01 16:49:16)
	 * @param: String location The path to the file. 
	 */
	public abstract void openFile(String location) throws IOException;

	/**
	 * Looks for a temporary directory (os dependant), stores the content 
	 * in a file using a temporary name and opens the newly stored file.
	 * @parameter: CoOriginalContent content The content to export data from.
	 * @return: returns the filename under which the content was saved.
	 */
	public abstract String saveAndOpenTemporaryFile(CoOriginalContent content) throws IOException;
}
