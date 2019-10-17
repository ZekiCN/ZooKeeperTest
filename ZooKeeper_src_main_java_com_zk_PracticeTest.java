package com.zk;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class PracticeTest {
    private ZooKeeper zooKeeper;

    public PracticeTest() {
        ZKWatcher zkWatcher=new ZKWatcher("slave01,slave02");
        zkWatcher.connect();
        zooKeeper=zkWatcher.getZk();
    }

    public static void main(String[] args) throws KeeperException, InterruptedException {
        PracticeTest practiceTest=new PracticeTest();

        practiceTest.getChildren("/test",true);
        practiceTest.getChildren("/test",false);

        Thread.sleep(Long.MAX_VALUE);
    }


    public void getChildren(String nodePath,boolean sync) throws KeeperException, InterruptedException {

        byte[] datas = this.zooKeeper.getData(nodePath, false, new Stat());
        System.out.println("获取的数据"+new String(datas));
        if (sync){
//            同步
            List<String> children = zooKeeper.getChildren(nodePath, false);
            System.out.println("原始节点"+nodePath);
            children.forEach(child->{
                String newNodePath=nodePath+(nodePath.equals("/")?"":"/")+child;
                try {
                    getChildren(newNodePath,true);
                    System.out.println("子节点路径"+newNodePath);

                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } else {
//            异步
            zooKeeper.getChildren(nodePath, false,
                    new AsyncCallback.Children2Callback() {
                        @Override
                        public void processResult(int i, String s, Object o, List<String> list, Stat stat) {
//                            stat 指的是当前path元数据

                            list.forEach(System.out::println);

                        }
                    },null);
        }
        this.zooKeeper.delete(nodePath,-1);
    }
}
