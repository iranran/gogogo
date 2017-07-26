package hello.programmer.designpatterns.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwei
 * @Description
 * @date 2017/7/25 18:52
 */
public class ProcessFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessFactory.class);

    List<IProcessor> processors;

    private JContext context;

    private JResponse response;

    private ProcessFactory(JContext init){
        this.context = init;
        processors = new ArrayList<>();
    }

    public static ProcessFactory create(JContext init){
        return new ProcessFactory(init);
    }

    public ProcessFactory doProcess(){
        for(IProcessor processor : processors){
            response = processor.process(context);
            LOGGER.info("{}",response.getCode());
            if(response.getCode() > 0){
                return this;
            }
        }
        return this;
    }

    public ProcessFactory addProcess(IProcessor processor){
        processors.add(processor);
        return this;
    }

    public JResponse getResponse(){
        return response;
    }
}
