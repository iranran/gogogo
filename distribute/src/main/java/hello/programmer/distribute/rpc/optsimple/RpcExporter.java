package hello.programmer.distribute.rpc.optsimple;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created on 2017-7-11.
 */
public class RpcExporter implements Server{

    static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final Map<String, Class> serviceRegistry = new HashMap<>();

    private static boolean isRunning = false;

    private int port;
    private String host;

    public RpcExporter(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public void stop() {
        isRunning = false;

    }

    @Override
    public void start() throws IOException {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(host , port));

        try{
            while (true){
                executor.execute(new ExporterTask(server.accept()));
            }
        }
        finally {
            server.close();
        }
    }

    @Override
    public void register(Class serviceInterface, Class impl) {
        serviceRegistry.put(serviceInterface.getName(), impl);
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPort() {
        return 0;
    }

    private static class ExporterTask implements Runnable{

        Socket client = null;

        public ExporterTask(Socket client){
            this.client = client;
        }

        @Override
        public void run() {
            ObjectInputStream input = null;
            ObjectOutputStream output = null;

            try {
                input = new ObjectInputStream(client.getInputStream());
                String interfaceName = input.readUTF();

                Class<?> service = serviceRegistry.get(interfaceName);
                System.out.println(service);
                String methodName = input.readUTF();
                Class<?>[] parameterTypes = (Class<?>[])input.readObject();
                Object[] arguments = (Object[])input.readObject();
                Method method = service.getMethod(methodName,parameterTypes);
                Object result = method.invoke(service.newInstance(),arguments);
                output = new ObjectOutputStream(client.getOutputStream());
                output.writeObject(result);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (client != null) {
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
