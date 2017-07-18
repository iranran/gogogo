package hello.programmer.distribute.rpc.optsimple;

import com.google.common.net.HostAndPort;
import hello.programmer.distribute.Constant;
import hello.programmer.distribute.rpc.optsimple.api.EchoDomain;
import hello.programmer.distribute.rpc.optsimple.api.EchoService;
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

        LbClient lbClient = new LbClient(client);
        LbClient.HostAndPort hostAndPort = lbClient.getHostAndPort();


        RpcImporter<EchoService> importer = new RpcImporter<>();
        EchoService echo = importer.importer(EchoService.class,new InetSocketAddress(hostAndPort.getHost(),hostAndPort.getPort()));
        System.out.println(hostAndPort);
        for(int i=0; i<100; i++){
            //echo.echo("hi " + i);
            System.out.println(lbClient.getHostAndPort());
        }

        EchoDomain domain = new EchoDomain();
        domain.setName("ha");
        domain.setPassword("@#$@#$");
        System.out.println(echo.echo(domain));
    }
}
