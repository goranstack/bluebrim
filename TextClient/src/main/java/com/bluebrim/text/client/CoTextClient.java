package com.bluebrim.text.client;

import java.rmi.*;

import com.bluebrim.text.shared.*;


/**
 * @author Göran Stäck
 */
public class CoTextClient
{
    private static CoTextServer TEXT_SERVER;

    public static CoTextServer getTextServer()
    {
        if (TEXT_SERVER == null) try
        {
            TEXT_SERVER = (CoTextServer)Naming.lookup(CoTextServer.RMIname);
        } catch (Exception e) {
            throw new RuntimeException("Failed connection to text server", e);
        }
        return TEXT_SERVER;
    }
    
    public static void setTextServer(CoTextServer textServer)
    {
        TEXT_SERVER = textServer; 
    }

}
