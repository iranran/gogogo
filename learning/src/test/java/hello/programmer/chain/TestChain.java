package hello.programmer.chain;

import hello.programmer.designpatterns.chain.JContext;
import hello.programmer.designpatterns.chain.JResponse;
import hello.programmer.designpatterns.chain.ProcessFactory;
import org.junit.Test;

/**
 * @author liwei
 * @Description
 * @date 2017/7/25 14:30
 */
public class TestChain {

    @Test
    public void dop(){
        JContext context = new JContext();
        context.setLoanInfoId(10);
        ProcessFactory factory = ProcessFactory.create(context);
        factory.addProcess(new AProcess())
                .addProcess(new ProcessDemo.Demo1())
                .addProcess(new BProcess())
                .addProcess(new ProcessDemo.Demo2());

        JResponse response = factory.doProcess().getResponse();
        System.out.println(response.getCode()+" "+response.getMessage());

        Integer i = Integer.parseInt("3");
        System.out.println(380 == new Integer(380));

        System.out.println(i == new Integer(3));
        System.out.println(new Integer(3) == new Integer(3));
        System.out.println(new Integer(1234123) .equals(new Integer(1234123)));
        System.out.println(new Integer(1234123) == new Integer(1234123));

    }
}
