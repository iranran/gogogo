package hello.programmer.distribute.rpc.optsimple;

import com.google.common.net.HostAndPort;
import hello.programmer.distribute.Constant;
import hello.programmer.distribute.rpc.optsimple.api.EchoDomain;
import hello.programmer.distribute.rpc.optsimple.api.EchoService;
import hello.programmer.distribute.rpc.optsimple.api.HelloService;
import hello.programmer.distribute.rpc.optsimple.loadbalence.LbClient;
import hello.programmer.distribute.rpc.optsimple.provider.EchoServiceImpl;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created on 2017-7-11.
 */
public class RpcTest {
    public static void main(String[] args) throws Exception{
        //client invoke

        CuratorFramework client = CuratorFrameworkFactory.newClient(
                Constant.ZK_HOST,
                new RetryNTimes(10, 5000)
        );
        client.start();

        //Thread.sleep(15000);



        RpcImporter<EchoService> importer = new RpcImporter<>();

        for(int i=0; i<1000; i++){
            //echo.echo("hi " + i);
            try {
                Thread.sleep(1);
                EchoService echo = importer.importer(EchoService.class,client);
                //echo.echo("i am " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(int i =0;i<100; i++){
            RpcImporter<HelloService> importer2 = new RpcImporter<>();
            HelloService helloService = importer2.importer(HelloService.class,client);
            helloService.hello("hihi");
        }

    }
}
