package hello.programmer.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用来进行格式转换的工具
 * @author 张伯文
 * @date 2017年8月29日下午3:10:28
 */
public class TransFormUtil {

	/**
	 * 转换list为map
	 */
	@Deprecated  //废弃，因为
	public static  Map<Object,Object> listToMap(List<Map<String,Object>> ls,String keyName,String valueName){
		Map<Object,Object> retMap=new HashMap<>();
		for(Map<String,Object> map:ls){
			Object key=map.get(keyName);
				if(key==null){
					continue;	
				}else{
					retMap.put(key.toString(), map.get(valueName));
				}
			}
		return retMap;
	}
	/**
	 * 转换list<String,Object>为map<String,Object>
	 */
	public static  Map<String,Object> listToStringMap(List<Map<String,Object>> ls,String keyName,String valueName){
		Map<String,Object> retMap=new HashMap<>();
		for(Map<String,Object> map:ls){
			Object key=map.get(keyName);
			if(key==null){
				continue;
			}else{
				retMap.put(key.toString(), map.get(valueName));
			}
		}
		return retMap;
	}
	/**
	 * 转换list<String,Object>为map<String,Object>
	 */
	public static  Map<Integer,Object> listToIntegerMap(List<Map<String,Object>> ls,String keyName,String valueName){
		Map<Integer,Object> retMap=new HashMap<>();
		for(Map<String,Object> map:ls){
			Object key=map.get(keyName);
			if(key==null){
				continue;
			}else{
				retMap.put(Integer.valueOf(key.toString()), map.get(valueName));
			}
		}
		return retMap;
	}
	public static void main(String[] args){
//		Map tmp_map1;
//		Map tmp_map2;
//		
//		List ls=new ArrayList<>();
//		Map<Object,String> map1=new HashMap<>();
//		map1.put("key", "1");
//		map1.put("name", "aas2");
//		Map<Object,String> map2=new HashMap<>();
//		map2.put("key", "2");
//		map2.put("name", "aas2");
//		ls.add(map1);
//		ls.add(map2);
//		
//		tmp_map1=TransFormUtil.listToMap(ls, "key", "name");
//		
//		List ls2=new ArrayList<>();
//		Map<String,String> map3=new HashMap<>();
//		map1.put("key", "1");
//		map1.put("name", "aas2");
//		Map<String,String> map4=new HashMap<>();
//		map2.put("key", "2");
//		map2.put("name", "aas2");
//		ls.add(map1);
//		ls.add(map2);
//		
//		tmp_map2=TransFormUtil.listToMap(ls, "key", "name");
//		
//		int ii=1;
		
	}
}
