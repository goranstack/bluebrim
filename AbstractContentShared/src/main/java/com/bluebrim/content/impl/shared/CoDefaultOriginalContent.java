package com.bluebrim.content.impl.shared;
import java.io.*;
import java.util.*;

/**
 * Default implementation of the com.bluebrim.content.shared.CoOriginalContent interface.
 * Creation date: (2001-10-03 08:15:42)
 * @author: Marcus Hirt
 * @see: com.bluebrim.content.shared.CoOriginalContent
 */
public class CoDefaultOriginalContent implements com.bluebrim.content.shared.CoOriginalContent
{
	private String m_originalFileName;
	private byte [] m_data;

	// Byte array allocation increments
	private final static int ARRAY_INCREMENT = 512 * 1024 ; // Half a megabyte at a time

	/**
	 * Returns the original filename of the content.
	 * Should return null if original input was from a stream.
	 */
	public String getOriginalFileName()
	{
		return m_originalFileName;
	}

	/**
	 * Loads binary data from the InputStream until there is nothing left to
	 * read.
	 * Creation date: (2001-10-05 09:44:46)
	 * @param: InputStream is The InputStream to read from. 
	 */
	public void load(InputStream is) throws IOException 
	{
		ArrayList bufferList = new ArrayList();
		BufferedInputStream bis = new BufferedInputStream(is);
		byte [] tmpArray;

		int actuallyRead = 0, totalRead = 0;
		
		while(true)
		{
			tmpArray = new byte[ARRAY_INCREMENT];		
			actuallyRead = bis.read(tmpArray, 0, ARRAY_INCREMENT);
			if(actuallyRead == -1)
				break;
			
			if(actuallyRead != ARRAY_INCREMENT)
			{
				byte [] endArray = new byte[actuallyRead];
				System.arraycopy(tmpArray,0,endArray,0,actuallyRead);
				bufferList.add(endArray);
				totalRead += actuallyRead;
				break;
			}
			totalRead += ARRAY_INCREMENT;
			bufferList.add(tmpArray);
		}

		m_data = new byte[totalRead];
		int offset = 0;
		
		for(int i = 0; i < bufferList.size(); i++)
		{
			byte [] tmp = (byte []) bufferList.get(i);
			System.arraycopy(tmp, 0, m_data, offset, tmp.length);
			offset += ARRAY_INCREMENT;
		}
		bis.close();
		is.close();
	}
	
	/**
	 * Reads all data from the file at location fileName.
	 * Creation date: (2001-10-05 09:48:39)
	 * @param: String fileName The path to the file to read from.
	 */
	public void load(String fileName) throws IOException
	{
	    File f = new File(fileName);
	    m_originalFileName = fileName;
	    if (f.canRead())
		{
	        load(new FileInputStream(f));
	    }
	}
	
	/**
	 * Tests the load/save functionality by loading a file into memory and saving it under
	 * the name "filename".bak.
	 * Creation date: (2001-10-03 09:46:24)
	 * @param args java.lang.String[] 1:st and only argument has to be the path to a file.
	 */
	public static void main(String[] args) 
	{
		com.bluebrim.content.shared.CoOriginalContent oc = new CoDefaultOriginalContent();
		try
		{
			oc.load(args[0]);
			oc.save(oc.getOriginalFileName() + ".bak");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Stores the binary data in this com.bluebrim.content.shared.CoOriginalContent on the
	 * OutputStream.
	 * Creation date: (2001-10-05 09:50:18)
	 * @param: OutputStream os The stream to write to.
	 */
	 public void save(OutputStream os) throws IOException 
	 {
		BufferedOutputStream bos = new BufferedOutputStream(os, ARRAY_INCREMENT);	
		bos.write(m_data, 0, m_data.length);
		bos.flush();
		os.flush();
		bos.close();
		os.close();
	 }
	
	/**
	 * Stores the binary data in this com.bluebrim.content.shared.CoOriginalContent to the
	 * file at the location specified.
	 * @param: String fileName The Path to the file to write to.
	 */
	public void save(java.lang.String fileName) throws IOException 
	{
		File f = new File(fileName);
		save(new FileOutputStream(f));
	}
}
