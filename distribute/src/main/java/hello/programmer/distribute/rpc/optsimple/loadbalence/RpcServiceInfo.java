package hello.programmer.distribute.rpc.optsimple.loadbalence;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwei
 * @Description
 * @date 2017/7/19 19:39
 */
public class RpcServiceInfo {

    private String serviceName;

    private String servceClass;

    private String interfaceName;

    private List<String> hostInfos;

    public RpcServiceInfo(){
        hostInfos = new ArrayList<>();
    }

    public void addHost(String hostAndPort){
        hostInfos.add(hostAndPort);
    }
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServceClass() {
        return servceClass;
    }

    public void setServceClass(String servceClass) {
        this.servceClass = servceClass;
    }

    public List<String> getHostInfos() {
        return hostInfos;
    }

    public void setHostInfos(List<String> hostInfos) {
        this.hostInfos = hostInfos;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }
}
