package hello.programmer.designpatterns.chain;

import java.util.List;
import java.util.Map;

/**
 * @author liwei
 * @Description
 * @date 2017/7/25 14:11
 */
public class JResponse {

    private int code;

    private String message;

    private List<Map<Integer,String>> messages;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<Integer, String>> getMessages() {
        return messages;
    }

    public void setMessages(List<Map<Integer, String>> messages) {
        this.messages = messages;
    }
}
