package hello.programmer.distribute.rpc.simple;

import hello.programmer.distribute.rpc.simple.api.EchoService;
import hello.programmer.distribute.rpc.simple.provider.EchoServiceImpl;

import java.net.InetSocketAddress;

/**
 * Created on 2017-7-11.
 */
public class RpcTest {
    public static void main(String[] args) throws Exception{
        new Thread(() -> {
            try{
                RpcExporter.exporter("localhost",8080);
            }
            catch (Exception e){

            }
        }).start();

        RpcImporter<EchoService> importer = new RpcImporter<>();
        EchoService echo = importer.importer(EchoServiceImpl.class,new InetSocketAddress("localhost",8080));
        for(int i=0; i<100; i++){
            System.out.println(echo.echo("hi " + i));
        }

    }
}
