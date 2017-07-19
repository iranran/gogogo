package hello.programmer.distribute.rpc.optsimple;

import hello.programmer.distribute.Constant;
import hello.programmer.distribute.rpc.optsimple.api.EchoService;
import hello.programmer.distribute.rpc.optsimple.provider.EchoServiceImpl;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Created by lenovo on 2017-7-15.
 */
public class RpcServerStarter {
    public static void main(String[] args) {
        try{
            String host = "localhost";
            int port = 9000 + new java.util.Random().nextInt(100);
            System.out.println(port);
            Server serviceServer = new RpcExporter(host,port);

            serviceServer.register(EchoService.class, EchoServiceImpl.class);


            CuratorFramework client = CuratorFrameworkFactory.newClient(
                    Constant.ZK_HOST,
                    new RetryNTimes(10, 5000)
            );
            client.start();
            System.out.println("zk client start successfully!");

            String servicePath = Constant.ZK_PATH + "/" + EchoService.class.getName();
            Stat stat = client.checkExists().forPath(servicePath);
            if(stat == null){
                client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).
                        forPath(servicePath, "".getBytes());
            }
            else {
                System.out.println(stat);
            }



            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).
                    forPath(servicePath +"/address-", (host + ":" + port).getBytes());


//            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).
//                    forPath(servicePath +"/instance", EchoServiceImpl.class.getName().getBytes());

//            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).
//                    forPath(servicePath +"/interface", EchoService.class.getName().getBytes());

            // 2.Client API test
            // 2.1 Create node
//            String data1 = "hello";
//            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).
//                    forPath(Constant.ZK_PATH + "/" + host + ":" + port, data1.getBytes());

            serviceServer.start();
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }
}
