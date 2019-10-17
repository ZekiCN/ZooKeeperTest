package com.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.HashMap;

public class ZKOperate {
//    创建节点create /abc ""
    private ZooKeeper zk;

    public ZKOperate() {
        ZKWatcher zw=new ZKWatcher("slave01:2181,slave02:2181");
        zw.connect();
        this.zk=zw.getZk();

        long id = zk.getSessionId();
        System.out.println("客户端ID"+id);

    }

    public static void main(String[] args) throws KeeperException, InterruptedException {
        ZKOperate zkOperate=new ZKOperate();
//       创建操作
        zkOperate.create("/bd1903","bd1903",true);
        zkOperate.create("/bd1904","bd1904",false);

//        删除操作
        zkOperate.delete("/bd1903",-1,true);
        zkOperate.delete("/bd1903",-1,false);

//      获取数据
        zkOperate.getData("/bd1903",true);
        zkOperate.getData("/bd1904",false);

//        修改数据
        zkOperate.setData("/bd1903","abc",-1,true);
        zkOperate.setData("/bd1904","abc",-1,false);

        Thread.sleep(Long.MAX_VALUE);
    }



//    创建节点
    public void create(String nodePath,String data,boolean sync /*sync 表示是否为异步，true是同步*/) throws KeeperException, InterruptedException {

        if (sync){
//            同步
            String path = this.zk.create(
                    nodePath,
                    data.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
            System.out.println("同步的："+path);
        }else  {
//            异步
            this.zk.create(
                    nodePath,
                    data.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT,
                    new AsyncCallback.Create2Callback() {
                        public void processResult(int i, String s, Object o, String s1, Stat stat) {
                            System.out.println("异步创建执行线程"+Thread.currentThread().getName());
                            KeeperException.Code code = KeeperException.Code.get(i);
                            System.out.println("返回码"+ code);
                            System.out.println("异步创建的节点路径"+s);
                            System.out.println("异步创建的节点名称"+ s1);
                            System.out.println("主线程传递的参数"+o);
                            System.out.println("创建节点的元数据"+stat);
                        }
                    },
                    null);
        }
    }

//     删除节点
    public void delete(String nodePath,int version,boolean sync) throws KeeperException, InterruptedException {
        if (sync){
//            同步
            this.zk.delete(
                    nodePath,
                    version);
        }else {
            this.zk.delete(nodePath,
                    version, new AsyncCallback.VoidCallback() {
                        public void processResult(int i, String s, Object o) {
                            KeeperException.Code code = KeeperException.Code.get(i);
                            if (code==KeeperException.Code.OK){
                                System.out.println("节点"+s+"删除成功");
                            }
                            if (code==KeeperException.Code.NONODE){
                                System.out.println("节点"+s+"不存在，删除失败");
                            }

                            if (code==KeeperException.Code.NOTEMPTY){
                                System.out.println("节点"+s+"非空，操作失败");
                            }
                        }
                    }
                    ,null
            );
        }
    }




//    获取数据
    public void getData(String nodePath,boolean sync) throws KeeperException, InterruptedException {

        if (sync){
//            同步
            Stat stat=new Stat();
            byte[] datas = zk.getData(
                    nodePath,
                    false,
                    stat
            );
            String data=new String(datas);
            System.out.println("同步获取的数据"+data);
            System.out.println("同步获取的元数据"+stat);
            System.out.println("同步获取的版本"+stat.getVersion());
        }else {
//            异步
            zk.getData(nodePath,
                    false,
                    new AsyncCallback.DataCallback() {
                        public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
                            String data=new String(bytes);
                            System.out.println("异步获取数据的执行结果"+KeeperException.Code.get(i));
                            System.out.println("异步获取节点信息"+s);
                            System.out.println("异步获取的数据"+data);
                            System.out.println("异步获取的元数据"+stat);
                        }
                    },null);
        }
    }

//    修改数据
    public void setData(String nodePath,String data,int version,boolean sync) throws KeeperException, InterruptedException {
        if (sync){
            this.getData(nodePath,true);
            zk.setData(nodePath,data.getBytes(),version);
            Stat stat=zk.setData(nodePath,data.getBytes(),version);
            System.out.println("修改之后的版本"+stat);
        }else {
            getData(nodePath,true);
            zk.setData(nodePath, data.getBytes(), version, new AsyncCallback.StatCallback() {
                public void processResult(int i, String s, Object o, Stat stat) {
                    System.out.println("异步修改后的版本"+stat);
                }
            },null);
        }
    }
}
