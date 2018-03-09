package com.bluebrim.compositecontent.client;

import java.rmi.*;

import com.bluebrim.content.shared.*;


/**
 * @author Göran Stäck
 */
public class CoCompositeContentClient
{     
    private static CoCompositeContentServer COMPOSITE_CONTENT_SERVER;

    public static CoCompositeContentServer getCompositeContentServer()
    {
        if (COMPOSITE_CONTENT_SERVER == null) try
        {
        	COMPOSITE_CONTENT_SERVER = (CoCompositeContentServer)Naming.lookup(CoCompositeContentServer.RMIname);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return COMPOSITE_CONTENT_SERVER;
    }
    
    public static void setCompositeContentServer(CoCompositeContentServer compositeContentServer)
    {
    	COMPOSITE_CONTENT_SERVER = compositeContentServer;
    }

}
