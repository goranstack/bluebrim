package com.bluebrim.font.shared;


/**
 * This class is used to access CoFontServer in classes that is executed both at
 * server and client. The class is initialized with a remote reference in the
 * client and a direct object reference in the server.
 * 
 * @author Göran Stäck
 */
public abstract class CoFontServerProvider
{
    private static CoFontServer FONT_SERVER;
    
    public static void setFontServer(CoFontServer fontServer)
    {
        FONT_SERVER = fontServer; 
    }
    
    public static CoFontServer getFontServer()
    {
        return FONT_SERVER;
    }
    
}
