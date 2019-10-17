package com.zk;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        String data;
        try {
            data = ConfigExecutor.load("/server");
            String[] split = data.split("[:]");
            Socket socket=new Socket(split[0],Integer.parseInt(split[1]));
            PrintWriter printWriter=new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream())
                    ,true);
            printWriter.println("Hello!!");

            printWriter.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
