package hello.programmer.distribute.rpc.optsimple.api;

/**
 * Created  on 2017-7-11.
 */
public interface EchoService {

    String echo(String ping);

    String echo(EchoDomain echoDomain);
}
