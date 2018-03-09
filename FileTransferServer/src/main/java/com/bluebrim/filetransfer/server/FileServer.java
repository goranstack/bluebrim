package com.bluebrim.filetransfer.server;

import java.rmi.*;

import com.bluebrim.filetransfer.shared.*;

public class FileServer
{

    public static void main(String argv[])
    {
        if (System.getSecurityManager() == null)
        {
            System.setSecurityManager(new RMISecurityManager());
        }
        try
        {
            FileInterface fi = new FileImpl("FileServer");
            Naming.rebind("//127.0.0.1/FileServer", fi);
        } catch (Exception e)
        {
            System.out.println("FileServer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

