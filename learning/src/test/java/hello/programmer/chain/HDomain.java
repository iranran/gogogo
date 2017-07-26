package hello.programmer.chain;

/**
 * @author liwei
 * @Description
 * @date 2017/7/25 16:47
 */
public class HDomain {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return "HDomain{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
