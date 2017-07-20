package hello.programmer.distribute.rpc.optsimple.loadbalence;

/**
 * @author liwei
 * @Description
 * @date 2017/7/20 12:48
 */
public class HostAndPort{
    String host;
    int port;
    long brokenTime = 0;



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

    public long getBrokenTime() {
        return brokenTime;
    }

    public void setBrokenTime(long brokenTime) {
        this.brokenTime = brokenTime;
    }

    @Override
    public String toString() {
        return "HostAndPort{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
