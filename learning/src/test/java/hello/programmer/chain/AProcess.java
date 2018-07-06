package hello.programmer.chain;

import hello.programmer.designpatterns.chain.IProcessor;
import hello.programmer.designpatterns.chain.JContext;
import hello.programmer.designpatterns.chain.JResponse;

/**
 * @author liwei
 * @Description
 * @date 2017/7/25 14:30
 */
public class AProcess implements IProcessor {

    @Override
    public JResponse process(JContext context) {

        int loaninfoid = context.getLoanInfoId();
        System.out.println("ap " + loaninfoid);
        context.put("aprocess","ahahaha" + loaninfoid);

        JResponse response = new JResponse();
        response.setCode(0);
        response.setMessage("go");
        return response;
    }
}
