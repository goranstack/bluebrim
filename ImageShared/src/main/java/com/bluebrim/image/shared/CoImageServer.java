package com.bluebrim.image.shared;

import java.rmi.*;

/**
 * @author Göran Stäck
 */
public interface CoImageServer extends Remote
{
    public static final String RMIname = "ImageServer";
    
	public CoImageContentIF createImageContent( String fileName, byte[] fileContent) throws RemoteException; 

}