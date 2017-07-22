package hello.programmer.distribute.rpc.optsimple.provider;

import hello.programmer.distribute.rpc.optsimple.api.HelloService;

/**
 * Created by lenovo on 2017-7-22.
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public void hello(String halo) {
        System.out.println("halo->"+halo);
    }
}
