package hello.programmer.common.utils;

import java.util.HashMap;
import java.util.Map;

public class ControllerUtil {


    /**
     * 控制器层的返回参数封装
     * @author 张伯文
     * createTime 2018/6/25-18:31
     */
    public static Map<String, Object> packageReturnInfo(int code, String msg,Object ... context){
        Map<String, Object> rs = new HashMap<>();
        rs.put("code", code);
        rs.put("msg", LogUtil.msgWithPara(msg,context));
        return rs;
    }
    /**
     * 控制器层的返回参数封装-error
     * @author 张伯文
     * createTime 2018/6/25-18:31
     */
    public static Map<String, Object> packageErrorInfo(String msg,Object ... context){
        return packageReturnInfo(-1,msg,context);
    }
    public static Map<String, Object> packageErrorInfo(){
        return packageReturnInfo(-1,"操作异常，请联系管理员!");
    }
    /**
     * 控制器层的返回参数封装-success
     * @author 张伯文
     * createTime 2018/6/25-18:31
     */
    public static Map<String, Object> packageSuccessInfo(String msg,Object ... context){
        return packageReturnInfo(1,msg,context);
    }
}
