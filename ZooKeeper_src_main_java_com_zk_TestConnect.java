package com.zk;

/*
 * @Author: zeki
 * @Description://TODO连接ZOOKEEPER
 * @Date: 2019-10-15 16:57:05
 * @Param:
 * @return:
 **/

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class TestConnect {
    static CountDownLatch countDownLatch=new CountDownLatch(1);


    public static void main(String[] args) throws IOException {
//        zk对象创建异步执行
//        在newZOOKEEPER对象的过程中，
//        主线程会重新开启一个子线程连接ZK服务器，如果连接成功则返回zk对象
        ZooKeeper zk=new ZooKeeper("slave02", 5000, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println("线程名称"+Thread.currentThread().getName());
                System.out.println("事件"+watchedEvent);
                if (watchedEvent.getState()== Event.KeeperState.SyncConnected){
                    countDownLatch.countDown();
                }
            }
        });

        System.out.println(zk);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(zk);

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(zk);
    }
}
