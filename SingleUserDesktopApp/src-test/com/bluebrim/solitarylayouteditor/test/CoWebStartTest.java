package com.bluebrim.solitarylayouteditor.test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Starts a small web server that can be used to test the 
 * Java Web Start distribution. After starting this class
 * double click on the internet short cut file
 * 
 * @author GStack
 *
 */
public class CoWebStartTest

{

	public static void main(String[] args) throws IOException
	{
		final File webDocumentRoot = new File("dist", "web-document-root"); 
		if (!webDocumentRoot.exists())
			throw new RuntimeException("Unable to find the \"web-document-root\" directory");
		NanoHTTPD nanoHTTPD = null;
		int port = 4714;
		try
		{
			nanoHTTPD = new NanoHTTPD(port)
			{
				@Override
				public Response serve(String uri, String method, Properties header, Properties parms)
				{
					return serveFile(uri, header, webDocumentRoot, false);
				}
			};
	
		} catch (IOException e)
		{
			throw new RuntimeException("Unable to start internal web server" , e);
		}
		System.out.println("Starting web server on port: " + port + " document root: " + webDocumentRoot.getAbsolutePath());
		System.out.println( "Hit Enter to stop.\n" );
		try { System.in.read(); } catch( Throwable t ) {};

	}

}
