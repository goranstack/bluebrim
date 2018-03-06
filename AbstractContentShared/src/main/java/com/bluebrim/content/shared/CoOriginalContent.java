package com.bluebrim.content.shared;
import java.io.*;

/**
 * Interface for classes that store and retreive 
 * original content (binary data).
 * Creation date: (2001-10-03 08:10:03)
 * @author: Marcus Hirt
 */
public interface CoOriginalContent 
{
	/**
	 * Returns the original filename of the content.
	 * Should return null if original input was from a stream.
	 */
	public String getOriginalFileName();
	
	/**
	 * Loads binary data from the InputStream until there is nothing left to
	 * read.
	 * Creation date: (2001-10-05 09:44:46)
	 * @param: InputStream is The InputStream to read from. 
	 */
	public void load(InputStream is) throws IOException;
	
	/**
	 * Reads all data from the file at location fileName.
	 * Creation date: (2001-10-05 09:48:39)
	 * @param: String fileName The path to the file to read from.
	 */	
	public void	load(String fileName) throws IOException;
	
	/**
	 * Stores the binary data in this CoOriginalContent on the
	 * OutputStream.
	 * Creation date: (2001-10-05 09:50:18)
	 * @param: OutputStream os The stream to write to.
	 */
	public void save(OutputStream os) throws IOException;
	
	/**
	 * Stores the binary data in this CoOriginalContent to the
	 * file at the location specified.
	 * @param: String fileName The Path to the file to write to.
	 */
	 public void save(String fileName) throws IOException;
}
