package com.bluebrim.font.shared;

import java.rmi.*;

/**
 * @author Göran Stäck
 */
public interface CoFontServer extends Remote
{
    public static final String RMIname = "FontServer";
    
    public CoFontRepositoryIF getFontRepository() throws RemoteException;
}