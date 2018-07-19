package hello.programmer.common.utils;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 容纳数据库查询相关通用逻辑
 * @author 张伯文
 * @date 2017年11月7日下午4:45:41
 */
public class DaoUtil {

	/**
	 * 把数据库查询到的key，value对转换为普通map备用
	 * 例如：查询项目名称和项目id，生成通过名称查询id的map
	 * @author 张伯文
	 * @date 2017年11月7日下午4:50:46
	 * 
	 * @param resultMapList
	 * @param keyName
	 * @param valueName
	 * @return
	 */
	public static Map<String,Object> mapResultToMap(List<Map<String,Object>> resultMapList,
			String keyName,String valueName){
		Map<String,Object> retMap=new HashMap<>();
		
		for(Map<String,Object> map:resultMapList){
			retMap.put(FormatUtil.toString(map.get(keyName)), FormatUtil.toString(map.get(valueName)));
		}

		return retMap;
	}
	/**
	 * 把数据库查询到的key，value对转换为普通map<String,map<String,Object>>备用
	 * value字段名称可以有多个
	 * 例如：查询项目名称和项目id，生成通过名称查询id的map
	 * @author 张伯文
	 * @date 2017年11月7日下午4:50:46
	 *
	 * @param resultMapList
	 * @param keyName
	 * @param valueNames
	 * @return
	 */
	public static Map<String,Map<String,Object>> mapResultToMapMultiValue(List<Map<String,Object>> resultMapList,
													String keyName,String... valueNames){
		Map<String,Map<String,Object>> retMap=new HashMap<>();

		for(Map<String,Object> map:resultMapList){
			Map<String,Object> valMap=new HashMap<>();
			for(String valName:valueNames){
				valMap.put(valName,map.get(valName));
			}
			retMap.put(FormatUtil.toString(map.get(keyName)),valMap);
		}

		return retMap;
	}
	/**
	 * 提取查询结果某一字段为list《Integer》
	 * @author 张伯文
	 * @date 2017年11月7日下午4:59:25
	 * @return
	 */
	public static List<Integer> mapResultToList(List<Map<String,Object>> resultMapList,String valueName){
		List<Integer> list = new LinkedList<>();

		for (Map<String, Object> map : resultMapList) {
			list.add(FormatUtil.toInteger( map.get(valueName)));
		}

		return list;
	}
	/**
	 * 提取查询结果的字段为逗号分隔id，用来拼接sql中的in（1，3，2）这样的逻辑
	 * 返回示例【1，2，3】
	 * @author 张伯文
	 * @date 2017年11月7日下午4:59:25
	 * @return
	 */
	public static String mapResultToIds(List<Map<String,Object>> resultMapList,String valueName){
		StringBuilder sb=new StringBuilder();
		boolean isFirstLoop=true;

		for (Map<String, Object> map : resultMapList) {
			if(isFirstLoop){
				isFirstLoop=false;
			}else{
				sb.append(",");
			}
			sb.append("'").append(map.get(valueName)).append("'");
		}
		return sb.toString();
	}


	/**
	 * 拼接sql参数，用于sql 的 in（1，2，3）条件
	 * 如输入【1，2，3】，输出1：?,?,?.输出2:new Object[]{1,2,3}
	 * @author 张伯文
	 * createTime 2018/4/23-18:49
	 */
	public static Pair<String,Object[]> makeInSql(List<Integer> idList){
		StringBuilder sql=new StringBuilder();

		if(idList.size()==0){
			return Pair.of("",new Object[]{});
		}
		sql.append("?");
		if(idList.size()>1){
			for (int i = 1; i < idList.size(); i++) {
				sql.append(",").append("?");
			}
		}
		return Pair.of(sql.toString(),idList.toArray(new Object[idList.size()]));
	}

	public static void main(String[] args)throws Exception {
		List<Integer> idList=new LinkedList<>();
		idList.add(1);
		Pair<String,Object[]> pp=makeInSql(idList);
		idList.add(1);
		idList.add(1);
		pp=makeInSql(idList);


	}
}
