package com.pbc.telematics.TCPClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TestSend {
	public static void main(String args[]) throws Exception
    {
        int c;
        Socket s = new Socket("155.135.0.71",8091);
        InputStream in=s.getInputStream();
        OutputStream out=s.getOutputStream();
        BufferedInputStream bin = new BufferedInputStream(s.getInputStream());
        BufferedOutputStream bout = new BufferedOutputStream(s.getOutputStream());
        String myStr="what is google";
        byte buf[] = myStr.getBytes();
        try
        {
            bout.write(buf);
        }
        catch(Exception e)
        {
            System.out.print(e);
        }
        bout.flush();
        //InputStream in = s.getInputStream();
        //OutputStream out =s.getOutputStream();        
        s.shutdownOutput();
        while((c=bin.read()) !=-1)
        {
            //System.out.print("junaid");
            System.out.print((char) c);
        }
        //System.out.println(a);
        s.close();
    }
}
