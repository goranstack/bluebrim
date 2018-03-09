package com.bluebrim.layout.client;

import java.rmi.*;

import com.bluebrim.layout.shared.*;


/**
 * @author Göran Stäck
 */
public class CoLayoutClient
{
    private static CoLayoutServer LAYOUT_SERVER;

    public static CoLayoutServer getLayoutServer()
    {
        if (LAYOUT_SERVER == null) try
        {
            LAYOUT_SERVER = (CoLayoutServer)Naming.lookup(CoLayoutServer.RMIname);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return LAYOUT_SERVER;
    }
    
    public static void setLayoutServer(CoLayoutServer layoutServer)
    {
        LAYOUT_SERVER = layoutServer; 
    }
   

}
