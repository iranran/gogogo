package hello.programmer.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.asset.exception.BizException;
import org.slf4j.Logger;

public class LogUtil {

    public static void log(Object x){
        System.out.println(x);
    }

    public static  void logErrorAndThrow(Logger logger, String msg) throws BizException {
        logger.error(msg);
        throw new BizException(msg);
    }

    public static String toJsonString(Object o) {
        if(o==null)return "";
        return JSONArray.toJSONString(o, SerializerFeature.WriteMapNullValue);
    }

    public static String msgWithPara(String msg,Object ... context){
        StringBuilder sb=new StringBuilder(msg);
        sb.append(JSONObject.toJSONString(context));
        return sb.toString();
    }

    public static String paramsToString(Object ... param){
        return JSONObject.toJSONString(param);
    }
}
