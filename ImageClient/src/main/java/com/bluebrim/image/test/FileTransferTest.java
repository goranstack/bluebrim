package com.bluebrim.image.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTransferTest
{

    public static final int BUFFER_SIZE = 1024 * 50;

    private byte[] buffer;

    public FileTransferTest()
    {
        buffer = new byte[BUFFER_SIZE];
    }

    public void startServer() throws Exception
    {
        ServerSocket socket = new ServerSocket(9000);
        Socket client = socket.accept();
        BufferedInputStream in = new BufferedInputStream(new FileInputStream("hugefile.jar"));
        BufferedOutputStream out = new BufferedOutputStream(client.getOutputStream());
        int len = 0;
        while ((len = in.read(buffer)) > 0)
        {
            out.write(buffer, 0, len);
            System.out.print("#");
        }
        in.close();
        out.flush();
        out.close();
        client.close();
        socket.close();
        System.out.println("\nDone!");
    }

    public void startClient() throws Exception
    {
        Socket socket = new Socket("localhost", 9000);
        BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("outfile.jar"));
        int len = 0;
        while ((len = in.read(buffer)) > 0)
        {
            out.write(buffer, 0, len);
            System.out.print("#");
        }
        in.close();
        out.flush();
        out.close();
        socket.close();
        System.out.println("\nDone!");
    }

    public static void main(String[] args) throws Exception
    {
        FileTransferTest test = new FileTransferTest();
        if (args.length == 0)
        {
            test.startServer();
        } else
        {
            test.startClient();
        }
    }
}