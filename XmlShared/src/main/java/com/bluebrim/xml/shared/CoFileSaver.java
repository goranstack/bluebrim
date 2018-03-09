package com.bluebrim.xml.shared;

import java.io.*;

/**
 * Utility class that saves a file.
 * The abstract method 'writeToStream' must be implemented by
 * subclasses.
 *
 * First the file is written to a temporary file and then renamed to the target file name
 * If any exception occurs during the execution of writeToStream, the renaming operation
 * is not performed, and thus does not corrupt the target file (if it exists prior to the
 * save)
 * Creation date: (1999-12-09 11:43:22)
 * @author: Mikael Printz
 */
public abstract class CoFileSaver {
	private String m_directory;
	private String m_fileName;

	public CoFileSaver() {
		super();
	}

	public CoFileSaver(File f) {
		this(f.getParent(), f.getName());
	}


	public CoFileSaver(String directory, String fileName) {
		super();
		m_directory = directory + (directory.endsWith(File.separator) ? "" : File.separator);
		m_fileName = fileName;
	}

	protected File createTmpFile() throws IOException {
		return File.createTempFile("" + System.currentTimeMillis(), "tmp");
	}

	/**
	 *	Saves a file to a temporary file and then renames it to the target file
	 *  if the tmp file could be created without exceptions generated
	 *  Returns true if the save operation was successful, false otherwise
	 */
	public boolean save() throws Throwable {
		File tmpFile = createTmpFile();
		FileOutputStream fos = new FileOutputStream(tmpFile);
		try {
			writeToStream(fos);
			fos.close();

			// Ok - write to tmp file successful - rename it to the target file name
			File oldFile = new File(m_directory + m_fileName);
			oldFile.delete();
			if (!tmpFile.renameTo(oldFile)) {
				tmpFile.delete();
				throw new IOException();
			}
			tmpFile.delete();
			return true;
		} finally {
			fos.close();
			tmpFile.delete();
		}
	}

	public abstract void writeToStream(OutputStream os) throws Exception;
}