package com.zk;

import java.io.IOException;
import java.net.ServerSocket;

public class PortInUse {
    public static void main(String[] args) throws IOException {
        ServerSocket ss=new ServerSocket(8888);
        ServerSocket ss1=new ServerSocket(8889);
        ServerSocket ss2=new ServerSocket(8890);
        ServerSocket ss3=new ServerSocket(8891);
        while (true){}
    }
}
