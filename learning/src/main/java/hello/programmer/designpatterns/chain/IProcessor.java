package hello.programmer.designpatterns.chain;

/**
 * @author liwei
 * @Description
 * @date 2017/7/25 19:04
 */
public interface IProcessor {

    /**
     *
     * @param context
     * @return
     */
    public JResponse process(JContext context);
}
