package hello.programmer.distribute.rpc.optsimple;

import hello.programmer.distribute.Constant;
import hello.programmer.distribute.rpc.optsimple.api.EchoService;
import hello.programmer.distribute.rpc.optsimple.api.HelloService;
import hello.programmer.distribute.rpc.optsimple.provider.EchoServiceImpl;
import hello.programmer.distribute.rpc.optsimple.provider.HelloServiceImpl;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

/**
 * Created by lenovo on 2017-7-15.
 */
public class RpcServerStarter {
    public static void main(String[] args) {
        try{
            String host = "localhost";
            int port = 9000 + new java.util.Random().nextInt(100);
            System.out.println(port);


            CuratorFramework client = CuratorFrameworkFactory.newClient(
                    Constant.ZK_HOST,
                    new RetryNTimes(10, 5000)
            );
            client.start();
            System.out.println("zk client start successfully!");

            Server serviceServer = new RpcExporter(host,port);

            serviceServer.register(EchoService.class, EchoServiceImpl.class,client);
            serviceServer.register(HelloService.class, HelloServiceImpl.class,client);

            serviceServer.start();
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }
}
