package hello.programmer.distribute.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.nodes.PersistentEphemeralNode;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author liwei
 * @Description
 * @date 2017/7/11 13:07
 */
public class PersistentEphemeralNodeExample {
    private static final String PATH = "/zktest/ephemeralNode";
    private static final String PATH2 = "/zktest/node";
    public static void main(String[] args) throws Exception
    {
        CuratorFramework
                client = CuratorFrameworkFactory.newClient("192.168.200.234:2181", new ExponentialBackoffRetry(1000, 3));
//        client.getConnectionStateListenable().addListener(new ConnectionStateListener()
//        {
//            @Override
//            public void stateChanged(CuratorFramework client, ConnectionState newState)
//            {
//                System.out.println("连接状态:" + newState.name());
//            }
//        });

        client.getConnectionStateListenable().addListener((aclient, newState) -> {
            System.out.println("连接状态:" + newState.name());
        });
        client.start();
        PersistentEphemeralNode node = new PersistentEphemeralNode(client, PersistentEphemeralNode.Mode.EPHEMERAL, PATH, "临时节点".getBytes());
        node.start();
        node.waitForInitialCreate(3, TimeUnit.SECONDS);
        String actualPath = node.getActualPath();
        System.out.println("临时节点路径：" + actualPath + " | 值: " + new String(client.getData().forPath(actualPath)));
        //client.create().forPath(PATH2, "持久化节点".getBytes());
        System.out.println("持久化节点路径： " + PATH2 + " | 值: " + new String(client.getData().forPath(PATH2)));

//        KillSession.kill(client.getZookeeperClient().getZooKeeper(), "127.0.0.1:2181");
//        System.out.println("临时节点路径：" + actualPath + " | 是否存在: " + (client.checkExists().forPath(actualPath) != null));
//        System.out.println("持久化节点路径： " + PATH2 + " | 值: " + new String(client.getData().forPath(PATH2)));

        Thread.sleep(40000);

        CloseableUtils.closeQuietly(node);
        CloseableUtils.closeQuietly(client);
    }
}
