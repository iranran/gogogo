package hello.programmer.distribute.rpc.optsimple.provider;

import hello.programmer.distribute.rpc.optsimple.api.EchoDomain;
import hello.programmer.distribute.rpc.optsimple.api.EchoService;

/**
 * Created on 2017-7-11.
 */
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String ping) {
        System.out.println(ping);
        return ping != null ? ping + "--> I am OK!" : "I am OK!";
    }

    @Override
    public String echo(EchoDomain echoDomain) {
        return echoDomain.getName() + " " + echoDomain.getPassword();
    }
}
