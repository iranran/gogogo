package hello.programmer.distribute.rpc.simple.provider;

import hello.programmer.distribute.rpc.simple.api.EchoDomain;
import hello.programmer.distribute.rpc.simple.api.EchoService;

/**
 * Created on 2017-7-11.
 */
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String ping) {
        return ping != null ? ping + "--> I am OK!" : "I am OK!";
    }

    @Override
    public String echo(EchoDomain echoDomain) {
        return echoDomain.getName() + " " + echoDomain.getPassword();
    }
}
