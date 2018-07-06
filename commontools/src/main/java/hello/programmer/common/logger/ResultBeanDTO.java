package hello.programmer.common.logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liwei
 * @Description
 * @date 2017/9/1 13:47
 */
public class ResultBeanDTO <T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int SUCCESS = 0;

    public static final int FAIL = 1;

    public static final int NO_PERMISSION = 2;

    private String message = "success";

    private List<String> messages;

    private int code = SUCCESS;


    public ResultBeanDTO() {
        super();
    }

    public ResultBeanDTO(int code,String message) {
        this.message = message;
        this.code = code;
    }


    public ResultBeanDTO(Throwable e) {
        super();
        this.message = e.toString();
        this.code = FAIL ;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void addMessage(String message){
        if(messages == null){
            messages = new ArrayList<>();
        }
        messages.add(message);
    }
}
