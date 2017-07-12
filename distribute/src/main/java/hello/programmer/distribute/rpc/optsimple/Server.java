package hello.programmer.distribute.rpc.optsimple;

import java.io.IOException;

/**
 * @author liwei
 * @Description
 * @date 2017/7/12 13:19
 */
public interface Server {
    public void stop();

    public void start() throws IOException;

    public void register(Class serviceInterface, Class impl);

    public boolean isRunning();

    public int getPort();
}
