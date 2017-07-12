package hello.programmer.distribute.rpc.simple.api;

import java.io.Serializable;

/**
 * @author liwei
 * @Description
 * @date 2017/7/12 10:17
 */
public class EchoDomain implements Serializable{



    private String name;

    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
