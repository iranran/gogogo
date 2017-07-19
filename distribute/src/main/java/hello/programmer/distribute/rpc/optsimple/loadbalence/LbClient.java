package hello.programmer.distribute.rpc.optsimple.loadbalence;

import hello.programmer.distribute.Constant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.zookeeper.data.Stat;

import java.util.*;

/**
 * Created by lenovo on 2017-7-17.
 */
public class LbClient{

    CuratorFramework client;

    private Map<String,RpcServiceInfo> rpcMap;

    private void init(){
        try {
            List<String> infos = client.getChildren().forPath(Constant.ZK_PATH);
            System.out.println(infos);
            rpcMap = new HashMap<>();
            for (String serviceName : infos) {
                RpcServiceInfo info = new RpcServiceInfo();
                info.setServiceName(serviceName);
                List<String> serviceInfos = client.getChildren().forPath(Constant.ZK_PATH + "/" + serviceName);

                for (String serviceInfo : serviceInfos) {
                    byte[] value = client.getData().forPath(Constant.ZK_PATH + "/" + serviceName + "/" + serviceInfo);
                    if(serviceInfo.startsWith("address-")){
                        info.addHost(new String(value));
                    }
                    if(serviceInfo.equals("instance")){
                        info.setServceClass(new String(value));
                    }
                    if(serviceInfo.equals("interface")){
                        info.setInterfaceName(new String(value));
                    }
                }
                rpcMap.put(info.getServiceName(),info);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public LbClient(CuratorFramework client){
        this.client = client;

        try{
            init();

            // 2.Register watcher
            PathChildrenCache watcher = new PathChildrenCache(
                    client,
                    Constant.ZK_PATH,
                    true    // if cache data
            );
            watcher.getListenable().addListener((client1, event) -> {
                ChildData data = event.getData();
                if (data == null) {
                    System.out.println("No data in event[" + event + "]");
                }
                else {
                    PathChildrenCacheEvent.Type type = event.getType();
                    //vCHILD_ADDED

//                    if(type == PathChildrenCacheEvent.Type.CHILD_REMOVED){
//                        ListIterator<HostAndPort> iter = list.listIterator();
//                        while (iter.hasNext()){
//                            HostAndPort hap = iter.next();
//                            if(data.getPath().endsWith(hap.getHost() + ":" + hap.getPort())){
//                                iter.remove();
//                            }
//                            System.out.println(iter.next());
//                        }
//                    }

                    System.out.println("Receive event: "
                            + "type=[" + event.getType() + "]"
                            + ", path=[" + data.getPath() + "]"
                            + ", data=[" + new String(data.getData()) + "]"
                            + ", stat=[" + data.getStat() + "]");
                    init();
                }
            });
            watcher.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public HostAndPort getHostAndPort(Class iface){
        String name = iface.getName();

        RpcServiceInfo info = rpcMap.get(name);

        List<String> hosts = info.getHostInfos();

        System.out.println(hosts);
        String _hap = hosts.get(new java.util.Random().nextInt(hosts.size()));
        return new HostAndPort(_hap.split(":")[0],Integer.parseInt(_hap.split(":")[1]));
        //return list.get(new java.util.Random().nextInt(size));
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
