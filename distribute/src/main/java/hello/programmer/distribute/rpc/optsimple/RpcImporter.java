package hello.programmer.distribute.rpc.optsimple;

import hello.programmer.distribute.rpc.optsimple.api.EchoService;
import hello.programmer.distribute.rpc.optsimple.loadbalence.LbClient;
import org.apache.curator.framework.CuratorFramework;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created on 2017-7-11.
 */
public class RpcImporter<T> {

    public T importer(final Class<?> serviceInterface, final CuratorFramework client){
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(),new Class<?>[]{
                        serviceInterface},
                new InvocationHandler(){
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket = null;
                        ObjectOutputStream output = null;
                        ObjectInputStream input = null;

                        try {
                            socket = new Socket();

                            LbClient lbClient = new LbClient(client);
                            LbClient.HostAndPort hostAndPort = lbClient.getHostAndPort(serviceInterface);
                            InetSocketAddress addr = new InetSocketAddress(hostAndPort.getHost(),hostAndPort.getPort());

                            socket.connect(addr);
                            output = new ObjectOutputStream(socket.getOutputStream());
                            output.writeUTF(serviceInterface.getName());
                            output.writeUTF(method.getName());
                            output.writeObject(method.getParameterTypes());
                            output.writeObject(args);
                            input = new ObjectInputStream(socket.getInputStream());
                            return input.readObject();
                        }
                        catch (Exception e){

                        }
                        finally {
                            if(socket != null) socket.close();
                            if(output != null) output.close();
                            if(input != null) input.close();
                        }
                        return null;
                    }

                }
        );
    }
}
