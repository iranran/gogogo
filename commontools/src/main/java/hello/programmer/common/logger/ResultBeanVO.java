package hello.programmer.common.logger;

import java.io.Serializable;

/**
 * @author liwei
 * @Description
 * @date 2017/9/1 9:42
 */
public class ResultBeanVO<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final int NO_PERMISSION = 2;
    private String msg = "success";
    private int code = SUCCESS;
    private T data;
    public ResultBeanVO() {
        super();
    }

    public ResultBeanVO(int code,String msg) {
        this.msg = msg;
        this.code = code;
    }

    public ResultBeanVO(T data) {
        super();
        this.data = data;
    }
    public ResultBeanVO(Throwable e) {
        super();
        this.msg = e.toString();
        this.code = FAIL ;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
