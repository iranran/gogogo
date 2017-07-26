package hello.programmer.chain;

import hello.programmer.designpatterns.chain.IProcessor;
import hello.programmer.designpatterns.chain.JContext;
import hello.programmer.designpatterns.chain.JResponse;

/**
 * @author liwei
 * @Description
 * @date 2017/7/25 14:33
 */
public class BProcess  implements IProcessor {

    @Override
    public JResponse process(JContext context) {

        int loaninfoid = context.getLoanInfoId();
        System.out.println("bp " + loaninfoid);
        System.out.println("from ap " + context.get("aprocess",String.class));
        context.put("bprocess", "bhahaha" + loaninfoid);

        HDomain domain = context.get("domain",HDomain.class);
        System.out.println(domain);

        if(true){
            //throw new RuntimeException("hsdfsdfsdfsdf");
        }

        JResponse response = new JResponse();
        response.setCode(0);
        response.setMessage("go");
        return response;

    }
}
