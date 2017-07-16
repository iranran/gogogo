package hello.programmer.distribute.rpc.optsimple;

import hello.programmer.distribute.Constant;
import hello.programmer.distribute.rpc.optsimple.api.EchoDomain;
import hello.programmer.distribute.rpc.optsimple.api.EchoService;
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

        List<String> list = client.getChildren().forPath(Constant.ZK_PATH);
        System.out.println(list);
        String host = list.get(0).split(":")[0];
        int port = Integer.parseInt(list.get(0).split(":")[1]);

        RpcImporter<EchoService> importer = new RpcImporter<>();
        EchoService echo = importer.importer(EchoService.class,new InetSocketAddress(host,port));
        System.out.println(echo);
        for(int i=0; i<100; i++){
            System.out.println(echo.echo("hi " + i));
        }

        EchoDomain domain = new EchoDomain();
        domain.setName("ha");
        domain.setPassword("@#$@#$");
        System.out.println(echo.echo(domain));
    }
}
