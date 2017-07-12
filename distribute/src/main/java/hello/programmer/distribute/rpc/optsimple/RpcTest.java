package hello.programmer.distribute.rpc.optsimple;

import hello.programmer.distribute.rpc.optsimple.api.EchoDomain;
import hello.programmer.distribute.rpc.optsimple.api.EchoService;
import hello.programmer.distribute.rpc.optsimple.provider.EchoServiceImpl;

import java.net.InetSocketAddress;

/**
 * Created on 2017-7-11.
 */
public class RpcTest {
    public static void main(String[] args) throws Exception{
        //start the service
        new Thread(() -> {
            try{
                Server serviceServer = new RpcExporter("localhost",8082);
                serviceServer.register(EchoService.class, EchoServiceImpl.class);
                serviceServer.start();
            }
            catch (Exception e){

            }
        }).start();

        //client invoke
        RpcImporter<EchoService> importer = new RpcImporter<>();
        EchoService echo = importer.importer(EchoService.class,new InetSocketAddress("localhost",8082));
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
