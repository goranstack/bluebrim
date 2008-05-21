package com.bluebrim.font.client;

import java.rmi.*;

import com.bluebrim.font.shared.*;


/**
 * @author Göran Stäck
 */
public class CoFontClient
{    
    private static CoFontServer FONT_SERVER;

    private static CoFontServer getFontServer()
    {
        if (FONT_SERVER == null) try
        {
            FONT_SERVER = (CoFontServer)Naming.lookup(CoFontServer.RMIname);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return FONT_SERVER;
    }
        
    public static void setFontServer(CoFontServer fontServer)
    {
        FONT_SERVER = fontServer;  
    }

}
