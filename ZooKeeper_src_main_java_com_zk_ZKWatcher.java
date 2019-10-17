package com.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKWatcher implements Watcher {
    private String connStr;
    private CountDownLatch cdl;
    private ZooKeeper zk;

    public ZKWatcher(String connStr) {
        this.connStr = connStr;
        this.cdl=new CountDownLatch(1);
    }

    public  void connect(){
        try {
            this.zk=new ZooKeeper(this.connStr, 50000, this);
//             暂停主线程
            this.cdl.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState()== Event.KeeperState.SyncConnected){
            this.cdl.countDown();
        }
    }

    public  ZooKeeper getZk() {
        return this.zk;
    }
}
