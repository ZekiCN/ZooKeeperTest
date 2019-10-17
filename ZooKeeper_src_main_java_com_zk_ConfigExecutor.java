package com.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/*
 * @Author: Zeki
 * @Description://TODO向zookeeper注册提取数据
 * @Date: 2019-10-17 15:14:13
 * @Param:
 * @return:
 **/
public class ConfigExecutor {
    private  static ZooKeeper zk;

    static {
        ZKWatcher zkWatcher=new ZKWatcher("slave01");
        zkWatcher.connect();
        zk = zkWatcher.getZk();
    }

//    创建指定节点
    public static void resist(String nodePath, String data, CreateMode cm) throws Exception {
        Stat stat = zk.exists(nodePath, false);
        if (stat!=null)
            throw new Exception("指定节点已存在！");

        zk.create(nodePath,data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,cm);
    }


//    加载指定节点的数据
    public static String load(String nodePath) throws Exception {
        Stat stat = zk.exists(nodePath, false);
        if (stat==null)
            throw new Exception("指定节点不存在！！");

        byte[] bytes = zk.getData(nodePath, false, new Stat());
        return new String(bytes);
    }
}
