package com.androes.zoo;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Client {

	public static void main(String...v) throws Exception {
		System.out.println("[[start Client()]]");
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper("123.123.123.123:2181",3000,new Watcher(){
				@Override
				public void process(WatchedEvent event) {
					System.out.println("----------"+event.getPath()+","+event.getType()+","+event.getState());
				}
			});
			
			// ls
			List<String> rootList = zk.getChildren("/", false);
			for (String str:rootList) {
				System.out.println(str);
			}
			
			// create
			String node0 = "/aaa";
			Stat stat0 = zk.exists(node0, false);
			if (stat0 == null) {
				System.out.println("create node : " + node0);
				zk.create(node0, "xxxx".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			
			// data
			stat0 = zk.exists(node0, false);
			System.out.println("stat "+node0+":"+stat0.toString());
			byte [] node0Data = zk.getData(node0, false, null);
			System.out.println("get data "+node0+": "+new String(node0Data));
			
			// update
			System.out.println("set data "+node0);
			zk.setData(node0, "bbb".getBytes(), -1);
			stat0 = zk.exists(node0, false);
			System.out.println("stat "+node0+":"+stat0.toString());
			node0Data = zk.getData(node0, false, null);
			System.out.println("get data "+node0+": "+new String(node0Data));
			
			// delete
			System.out.println("delete node : "+node0);
			zk.delete(node0, -1);
			
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (zk != null) zk.close();
		}
	}
}
