package hello.programmer.common.util;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;
import org.apache.poi.ss.formula.functions.T;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;


/**
 * User: xs
 * Date: 13-4-1
 * Time: 上午9:39
 * To change this template use File | Settings | File Templates.
 */
public class JSONUtil {


    // 将String转换成JSON
    public static String string2json(String key, String value) {
        JSONObject object = new JSONObject();
        object.put(key, value);
        return object.toString();
    }

    // 将JSON转换成数组,其中valueClz为数组中存放的对象的Class
    @SuppressWarnings("rawtypes")
	public static Object json2Array(String json, Class valueClz) {
        JSONArray jsonArray = JSONArray.fromObject(json);
        return JSONArray.toArray(jsonArray, valueClz);
    }

    // 将Collection转换成JSON
    public static String collection2json(Object object) {
        JSONArray jsonArray = JSONArray.fromObject(object);
        return jsonArray.toString();
    }


    // 将数组转换成JSON
    public static String array2json(Object object) {
        JSONArray jsonArray = JSONArray.fromObject(object);
        return jsonArray.toString();
    }

    // 将Map转换成JSON
    public static String map2json(Object object) {
        JSONObject jsonObject = JSONObject.fromObject(object);
        return jsonObject.toString();
    }

    // 将JSON转换成Map,其中valueClz为Map中value的Class,keyArray为Map的key
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map json2Map(Object[] keyArray, String json, Class valueClz) {
        JSONObject jsonObject = JSONObject.fromObject(json);
        Map classMap = new HashMap();

        for (int i = 0; i < keyArray.length; i++) {
            classMap.put(keyArray[i], valueClz);
        }

        return (Map) JSONObject.toBean(jsonObject, Map.class, classMap);
    }

    // 将POJO转换成JSON
    public static String bean2json(Object object) {
        JSONObject jsonObject = JSONObject.fromObject(object);
        return jsonObject.toString();
    }

    // 将JSON转换成POJO,其中beanClz为POJO的Class
    @SuppressWarnings("rawtypes")
	public static Object json2Object(String json, Class beanClz) {
        return JSONObject.toBean(JSONObject.fromObject(json), beanClz);
    }
    
    // 将JSON转换成POJO,其中beanClz为POJO的Class
    @SuppressWarnings("rawtypes")
	public static Object json2Object(JSONObject json, Class beanClz) {
    	JsonConfig jsonConfig=new JsonConfig();
    	jsonConfig.setRootClass(beanClz);
    	JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"}));
        return JSONObject.toBean(json, beanClz);
    }

    // 将JSON转换成String
    public static String json2String(String json, String key) {
        JSONObject jsonObject = JSONObject.fromObject(json);
        return jsonObject.get(key).toString();
    }
    
    // 将JSON转换成Map,其中valueClz为Map中value的Class,keyArray为Map的key
    public static List<Map<String, Object>> jsonStringToList(String rsContent) throws Exception 
    { 
        JSONArray arry = JSONArray.fromObject(rsContent);
        List<Map<String, Object>> rsList = new ArrayList<Map<String, Object>>(); 
        for (int i = 0; i < arry.size(); i++) 
        { 
            JSONObject jsonObject = arry.getJSONObject(i); 
            Map<String, Object> map = new HashMap<String, Object>(); 
            for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();) 
            { 
                String key = (String) iter.next(); 
                String value = jsonObject.get(key).toString(); 
                map.put(key, value); 
            } 
            rsList.add(map); 
        } 
        return rsList; 
    }
    
    /**
     * 通过返回clazz类型对象List，要求clazz类型的成员变量名与sql中返回的字段名称相同
     * 
     * @param sql
     * @param args
     * @param clazz
     * @return
     */
    public static <T> List<T> queryForListByNamed(List<Map<String, Object>> datas, final Class<T> clazz) {

        List<T> retList = new ArrayList<T>();
        try {
            // 遍历List中的所有数据
            for (Map<String, Object> data : datas) {
                // 以clazz中的成员变量为基准，遍历clazz类型的所有成员变量，依次到Map中取值，然后赋值，
                // 没有取到值的不进行赋值
                T distObj = mapObject(data, clazz);
                retList.add(distObj);
            }
            return retList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
            // throw new RuntimeException(e);
        }
    }
    
    /**
     * 返回clazz类型的实例，数据来源data，data中的key名称要与clazz的成员变量名称一致
     * 
     * @param clazz
     * @return
     * @throws
     * @throws Exception
     * @paramsql
     * @paramargs
     */
    @SuppressWarnings("rawtypes")
	private static <T> T mapObject(final Map<String, Object> data, final Class<T> clazz)
            throws Exception {
        // 获取所有成员变量
        Field[] fields = clazz.getDeclaredFields();
        T distObj = clazz.newInstance();
        for (Field field : fields) {
            String fieldName = field.getName();
            Object fieldValue = data.get(fieldName);

            // 如果存在值，则进行赋值
            if (fieldValue != null) {
                PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
                Method setMethod = pd.getWriteMethod();
                // 获取字段类型
                Class type = field.getType();
                if (Integer.class.equals(type)) {
                    // Integer
                    Integer integerVal = Integer.valueOf(fieldValue.toString());
                    setMethod.invoke(distObj, integerVal);
                } else if (Date.class.equals(type)) {
                    try {
						DateFormat format = DateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
						// Date
						setMethod.invoke(distObj, format.parse(fieldValue.toString()));
					} catch (Exception e) {
						DateFormat format = DateFormat.getInstance("yyyy-MM-dd");
						// Date
						setMethod.invoke(distObj, format.parse(fieldValue.toString()));
					}
                }/*
                  * else if (DateTime.class.equals(type)) { // Date setMethod
                  * .invoke(distObj, DateUtil.parseDate(fieldValue.toString()));
                  * }
                  */else if (Short.class.equals(type)) {
                    // Short
                    if (fieldValue.getClass().equals(Boolean.class)) {
                        if (fieldValue.equals(Boolean.TRUE)) {
                            fieldValue = new Integer(1);
                        }
                        if (fieldValue.equals(Boolean.FALSE)) {
                            fieldValue = new Integer(0);
                        }
                    }
                    Short shortVal = Short.valueOf(fieldValue.toString());
                    setMethod.invoke(distObj, shortVal);
                } else if (String.class.equals(type)) {
                    String fieldValueStr = String.valueOf(fieldValue);
                    setMethod.invoke(distObj, fieldValueStr);
                } else if (BigDecimal.class.equals(type)) {
                	BigDecimal fieldValueStr = new BigDecimal(fieldValue.toString());
                    setMethod.invoke(distObj, fieldValueStr);
                } else {
                    setMethod.invoke(distObj, fieldValue);
                }
            }
        }
        return distObj;
    }
    
    /****
	 * 
	 * json串转list
	 * 
	 * @description （用一句话描述该方法的适用条件、执行流程、适用方法、注意事项 - 可选）
	 * @param jsonObject
	 * @param clazz
	 * @return
	 * @return List
	 * @throws @author zhengchao
	 * @create 2016年9月19日 下午6:51:03
	 */
	public static List getValue(String jsonObject, Class clazz) {
		JSONArray jsonArray = JSONArray.fromObject(jsonObject);
		List<T> list = (List) JSONArray.toCollection(jsonArray, clazz);
		return list;
	}
}