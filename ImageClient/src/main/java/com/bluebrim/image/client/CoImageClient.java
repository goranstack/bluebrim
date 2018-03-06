package com.bluebrim.image.client;

import java.io.*;
import java.rmi.*;

import com.bluebrim.image.shared.*;


/**
 * @author Göran Stäck
 */
public class CoImageClient implements CoImageContentFactory
{
	private static CoImageContentFactory INSTANCE;
	
    private CoImageServer imageServer;

    private CoImageServer getImageServer()
    {
        if (imageServer == null) try
        {
            imageServer = (CoImageServer)Naming.lookup(CoImageServer.RMIname);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return imageServer;
    }

    
    /**
     * Transfer the file to the server using RMI. The server creates an image content and
     * return a remote reference.
     */
    public CoImageContentIF createImageContent(File file)
    {
        CoImageContentIF imageContent = null;
        byte[] buffer = new byte[(int) file.length()];
        BufferedInputStream input;
        try
        {
            input = new BufferedInputStream(new FileInputStream(file));
            input.read(buffer, 0, buffer.length);
            input.close();
            imageContent = getImageServer().createImageContent(file.getName(), buffer);
        } catch (Exception e)
        {
            throw new RuntimeException("Unable to create image content", e);
         }

        return imageContent;  
    }
    

	public static CoImageContentFactory getInstance() 
	{
		if (INSTANCE == null)
			INSTANCE = new CoImageClient();
		return INSTANCE;
	}
	
	public static void setInstance(CoImageContentFactory instance) {
		INSTANCE = instance;
	}
}
