package hello.programmer.distribute.rpc.optsimple.loadbalence;

import hello.programmer.distribute.Constant;
import org.apache.curator.framework.CuratorFramework;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017-7-17.
 */
public class LbClient{

    CuratorFramework client;

    private List<HostAndPort> list;

    public LbClient(CuratorFramework client){
        this.client = client;

        try{
            List<String> infos = client.getChildren().forPath(Constant.ZK_PATH);

            System.out.println(infos);


            for (String info : infos){
                list = new ArrayList<>();
                list.add(new HostAndPort(info.split(":")[0],Integer.parseInt(info.split(":")[1])));
            }

        }
        catch (Exception e){

            e.printStackTrace();
        }
    }

    public HostAndPort getHostAndPort(){
        int size = list.size();
        return list.get(new java.util.Random().nextInt(size));
    }

    public class HostAndPort{
        String host;
        int port;

        public HostAndPort(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        @Override
        public String toString() {
            return "HostAndPort{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    '}';
        }
    }




}
