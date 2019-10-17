package com.zk;

import org.apache.zookeeper.CreateMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerSocket ss=null;
        int port=8888;
//
        while (true){
            try {
                ss=new ServerSocket(port);
                break;
            } catch (IOException e) {
                port++;
                System.out.println(port);
                e.printStackTrace();
            }
        }
        System.out.println("服务器启动");
//        向zk注册服务器监听的端口
        try {
            ConfigExecutor.resist("/server","127.0.0.1:"+port, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        while (true){
            try {
                Socket accept = ss.accept();
                new Thread(()->{
                    try {
                        BufferedReader bufferedReader=
                                new BufferedReader(
                                        new InputStreamReader(
                                                accept.getInputStream())
                        );
                        System.out.println("C说："+bufferedReader.readLine());
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
