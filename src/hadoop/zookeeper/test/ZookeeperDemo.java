package hadoop.zookeeper.test;

import java.io.IOException;

import org.apache.hadoop.ha.ClientBaseWithFixes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperDemo {
	public static void main(String[] args) throws Exception {
		// 创建一个与服务器的连接
		ZooKeeper zk = new ZooKeeper("hadoop0:2181", ClientBaseWithFixes.CONNECTION_TIMEOUT, new Watcher() {
			// 监控所有触发的事情
			@Override
			public void process(WatchedEvent event) {
				System.out.println("已经触发了"+event.getType()+"事件!");
			}
		});
		// 创建一个目录节点
		zk.create("/testRootPath", "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		// 创建一个子目录节点
		zk.create("/testRootPath/testChildPathOne", "testChildOneData".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println(new String(zk.getData("/testRootPath", false, null)));
		// 取出子目录节点列表
		System.out.println(zk.getChildren("/testRootPath", true));
		// 修改子目录节点数据
		zk.setData("/testRootPath/testChildPathOne", "modifyChildOneData".getBytes(), -1);
		System.out.println("目录节点状态：["+zk.exists("/testRootPath", true)+"]");
		// 创建另外一个子目录节点
		zk.create("/testRootPath/testChildPathTwo", "testChildTwoData".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo", true, null)));
		// 删除子目录节点
		zk.delete("/testRootPath/testChildPathTwo", -1);
		zk.delete("/testRootPath/testChildPathOne", -1);
		// 删除父目录节点
		zk.delete("/testRootPath", -1);
		// 关闭连接
		zk.close();
		
	}
}
