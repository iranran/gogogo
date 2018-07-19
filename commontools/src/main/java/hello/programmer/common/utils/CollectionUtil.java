package hello.programmer.common.utils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 关于集合相关操作的逻辑
 * @author 张伯文
 * @date 2017年11月26日下午5:26:18
 */
public class CollectionUtil {

	
	/**
	 * 去掉list中重复的元素（完全去掉，一个不留）
	 * 例如，｛1，2，3，3｝返回｛1，2｝
	 * @author 张伯文
	 * @date 2017年11月26日下午5:29:00
	 * 
	 * @param list
	 * @return
	 */
	public static <T> void deleteDublideElement(List<T> list){
		HashSet<T> set = new HashSet<>();
		List<T> list_delete=new LinkedList<>();
		for(T ele:list){
			if(set.contains(ele)){
				list_delete.add(ele);
			}else{
				set.add(ele);
			}
		}

		for(T e:list_delete){
			set.remove(e);
		}
		list.clear();
		list.addAll(set);

	}

	/**
	 * 判断是否含有重复元素
	 * @author 张伯文
	 * @date 2017年11月26日下午5:29:00
	 * 
	 * @param list
	 * @return
	 */
	public static <T> boolean isHasSameElement(List<T> list){
		HashSet<T> set = new HashSet<>(list);
		return set.size()!=list.size();
	}
	
	public static String mapGetString(Map<String,Object> map,String key,String defaultVal){
		Object val=map.get(key);
		if(val==null){
			return defaultVal;
		}else{
			return val.toString();
		}
	}
	public static BigDecimal mapGetBigDecimap(Map<String,Object> map,BigDecimal key,BigDecimal defaultVal){
		Object val=map.get(key);
		if(val==null){
			return defaultVal;
		}else{
			return new BigDecimal(val.toString());
		}
	}
	public static Integer mapGetInteger(Map<String,Object> map,Integer key,Integer defaultVal){
		Object val=map.get(key);
		if(val==null){
			return defaultVal;
		}else{
			return Integer.valueOf(val.toString());
		}
	}


	/**
	 * 提取list《map》的字段，保存为list<Objcet>
	 * @author 张伯文
	 * createTime 2018/6/22-11:43
	 */
	public static <T> List<T> extractMapColumn(List<Map<String, Object>> listMap,Object key,Class<T> destClazz) throws Exception {
		List<T> list=new LinkedList<>();
		if(!LogicUtil.hasVal(listMap)){
			return list;
		}

		for(Map<String, Object> map:listMap){
			T val=simpleConverter(map.get(key),destClazz);
			list.add(val);
		}
		return list;
	}

	/**
	 * 简单数据转换器，仅仅支持几种类型
	 * @author 张伯文
	 * createTime 2018/6/22-11:59
	 */
	public static <T> T simpleConverter(Object val,Class<T> destClazz) throws Exception {
		if(val==null){
			return null;
		}
		if(val.getClass() == destClazz){
			return (T)val;
		}

		try{
			//具体转换逻辑
			if(destClazz == Integer.class){
				return (T)new Integer(val.toString());
			}
			if(destClazz == String.class) {
				return (T) val.toString();
			}
		}catch(Exception e){
			throw new Exception("格式转换异常，尝试转换"+val.getClass()+"为"+destClazz+"其中val="+val);
		}



		throw new Exception("不支持的转换类型");
	}
	
	
	
	
	
	
	public static void main(String[] args)throws Exception {

		Map<String,Object> map=new HashMap<>();
		map.put("str","str1");
		map.put("int",123);
		List<Map<String,Object>> list=new ArrayList<>();
		list.add(map);

		List ls=extractMapColumn(list,"str",String.class);

		ls=extractMapColumn(list,"int",Integer.class);
		ls=extractMapColumn(list,"int",String.class);
		ls=extractMapColumn(list,"str",Integer.class);

//		List<String> list=new ArrayList<>();
////		list.add("1");
//		list.add("2");
//		list.add("1");
//		$.log($.toString(list));
//		;
		$.log(CollectionUtil.isHasSameElement(list));
	}// main
}
