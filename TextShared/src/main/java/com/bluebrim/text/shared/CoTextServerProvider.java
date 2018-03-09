package com.bluebrim.text.shared;


/**
 * This class is used to access CoTextServer in classes that is executed both at
 * server and client. The class is initialized with a remote reference in the
 * client and a direct object reference in the server.
 * 
 * @author Göran Stäck
 */
public abstract class CoTextServerProvider
{
    private static CoTextServer TEXT_SERVER;
    
    public static CoTextServer getTextServer()
    {
        return TEXT_SERVER;
    }
    
    public static void setTextServer(CoTextServer textServer)
    {
        TEXT_SERVER = textServer;
    }
}
