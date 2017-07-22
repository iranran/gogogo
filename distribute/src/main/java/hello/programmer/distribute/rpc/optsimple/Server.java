package hello.programmer.distribute.rpc.optsimple;

import org.apache.curator.framework.CuratorFramework;

import java.io.IOException;

/**
 * @author liwei
 * @Description
 * @date 2017/7/12 13:19
 */
public interface Server {
    public void stop();

    public void start() throws IOException;

    public void register(Class serviceInterface, Class impl,CuratorFramework client) throws Exception;

    public boolean isRunning();

    public int getPort();
}
