package com.bluebrim.filetransfer.server;

import java.io.*;
import java.rmi.*;
import java.rmi.server.*;

import com.bluebrim.filetransfer.shared.*;

public class FileImpl extends UnicastRemoteObject implements FileInterface
{

    private String name;

    public FileImpl(String s) throws RemoteException
    {
        super();
        name = s;
    }

    public byte[] downloadFile(String fileName)
    {
        try
        {
            File file = new File(fileName);
            byte buffer[] = new byte[(int) file.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileName));
            input.read(buffer, 0, buffer.length);
            input.close();
            return (buffer);
        } catch (Exception e)
        {
            System.out.println("FileImpl: " + e.getMessage());
            e.printStackTrace();
            return (null);
        }
    }
}

