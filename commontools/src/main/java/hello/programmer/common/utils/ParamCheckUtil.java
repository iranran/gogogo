package hello.programmer.common.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 参数检查，如果失败直接抛出异常
 * @author liwei
 * @Description
 * @date 2017/6/30 10:19
 */
public class ParamCheckUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParamCheckUtil.class);

    /**
     * 检查是否为null
     * @param value 要检测的值
     * @param errMessage 错误信息
     * @throws Exception
     */
    public static void checkNull(Object value,String errMessage) throws Exception{
        if(value == null){
            LOGGER.info(errMessage);
            throw new Exception(errMessage);
        }
    }

    /**
     * 检查是否为null或者其值为空字符串,
     * @param value 要检测的值
     * @param errMessage 错误信息
     * @throws Exception
     */
    public static void checkEmpty(String value,String errMessage) throws Exception{
        checkNull(value,errMessage);
        if(value.length() == 0){
            LOGGER.info(errMessage);
            throw new Exception(errMessage);
        }
    }

    /**
     * 检查是否为null或者其值为空字符串,
     * @param entities 要检测的实体
     * @param errMessage 错误信息
     * @throws Exception  异常,需调用方自行处理
     */
    public static <T> void checkEmpty(List<T> entities, String errMessage) throws Exception{
        if(CollectionUtils.isEmpty(entities)){
            LOGGER.info(errMessage);
            throw new Exception(errMessage);
        }
    }

    /**
     * 检查Map集合是否为null或者其值为空字符串,
     * @param entries 要检测的实体
     * @param errMessage 错误信息
     * @throws Exception  异常,需调用方自行处理
     */
    public static <T, V> void checkEmpty(Map<T, V> entries, String errMessage) throws Exception{
        if(MapUtils.isEmpty(entries)){
            LOGGER.info(errMessage);
            throw new Exception(errMessage);
        }
    }
}
