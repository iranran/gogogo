package hello.programmer.chain;

import hello.programmer.designpatterns.chain.IProcessor;
import hello.programmer.designpatterns.chain.JContext;
import hello.programmer.designpatterns.chain.JResponse;

/**
 * @author liwei
 * @Description
 * @date 2017/7/25 14:54
 */
public class ProcessDemo {
    public static class Demo1 implements IProcessor{
        public JResponse process(JContext context) {

            int loaninfoid = context.getLoanInfoId();
            System.out.println("Demo1 " + loaninfoid);
            context.put("Demo1","ahahaha" + loaninfoid);

            HDomain domain = new HDomain();
            domain.setId(1233);
            domain.setName("hahahha");
            context.put("domain",domain);
            JResponse response = new JResponse();
            response.setCode(0);
            response.setMessage("跑错误了，再来一次吧");
            return response;
        }
    }

    public static class Demo2 implements IProcessor{
        public JResponse process(JContext context) {

            int loaninfoid = context.getLoanInfoId();
            System.out.println("Demo1 " + loaninfoid);
            context.put("Demo1","ahahaha" + loaninfoid);

            HDomain domain = new HDomain();
            domain.setId(1233);
            domain.setName("hahahha");
            context.put("domain",domain);
            JResponse response = new JResponse();
            response.setCode(1);
            response.setMessage("跑错误了，再来一次吧");
            return response;
        }
    }
}
