package com.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class WatcherTest {
    public static void main(String[] args) throws KeeperException, InterruptedException {
        ZKWatcher zkWatcher=new ZKWatcher("slave01");
        zkWatcher.connect();
        ZooKeeper zk = zkWatcher.getZk();

        zk.getData("/test1", watchedEvent -> {
            Watcher.Event.EventType type = watchedEvent.getType();
            if (type==Watcher.Event.EventType.NodeDeleted){
                System.out.println("节点被删除");
            }
        },new Stat());
        Thread.sleep(Long.MAX_VALUE);
    }
}
