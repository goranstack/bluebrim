package com.bluebrim.image.impl.server;

import java.io.*;
import java.rmi.*;

import com.bluebrim.image.shared.*;

/**
 * @author Göran Stäck 2002-04-25
 */
public class CoRemoteImageServer implements CoImageServer 
{
		
	public CoImageContentIF createImageContent( String fileName, byte[] fileContent) throws RemoteException 
	{
	    CoImageContentIF imageContent = null;
        File file = new File(fileName);
        BufferedOutputStream output = null;
        try
        {
            output = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try
        {
        output.write(fileContent, 0, fileContent.length);
        output.flush();
        output.close();
        imageContent = new CoImageContent(file);
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return imageContent;
	}

}
