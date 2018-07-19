package hello.programmer.common.utils;

import com.asset.framework.utils.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * @Description jstl标签库实现方法
 * @author 张伯文
 * @date 2016年11月23日下午4:47:07
 */
public class JstlFunctions {


	/**
	 * @Description 转换枚举value为枚举描述
	 * @author 张伯文
	 * @date 2016年11月23日下午6:12:57
	 * 
	 * @param enumName 枚举完整类名如com.asset.enums.IsPrechargeInterestEnum
	 * com.asset.enums包下可直接写枚举如IsPrechargeInterestEnum
	 * @param value 枚举的value
	 * @return 翻译的枚举描述，如果发生任何异常，返回"".
	 */
	public static String getAnyEnumItemName(String enumName, String value) {
		try {
			if(enumName.indexOf('.')<0){
				enumName="com.asset.enums."+enumName;
			}
			if(StringUtils.isEmpty(value)){
				return "";
			}
			int value_int = Integer.valueOf(value);
			Class<?> threadClazz = Class.forName(enumName);
			Method method = threadClazz.getMethod("values");
			Method method_getDesc = threadClazz.getMethod("getDesc");
			Method method_getValue = threadClazz.getMethod("getValue");
			Object enumArray = method.invoke(null);

			for (int i = 0; i < Array.getLength(enumArray); i++) {
				Object enumInstance = Array.get(enumArray, i);
				int key = Integer.valueOf(method_getValue.invoke(enumInstance).toString());
				if (value_int == key) {
					return method_getDesc.invoke(enumInstance).toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * @Description 生成根据枚举生成&lt;option&gt;标签
	 * @author 张伯文
	 * @date 2016年11月23日下午7:10:20
	 * 
	 * @param enumName 枚举完整类名如com.asset.enums.IsPrechargeInterestEnum
	 * com.asset.enums包下可直接写枚举如IsPrechargeInterestEnum
	 * @param value 枚举的value
	 * @return <option>标签的字符串，如果发生任何异常，返回"".
	 */
	public static String getEnumOptions(String enumName, String value) {
		try {
			if(enumName.indexOf('.')<0){
				enumName="com.asset.enums."+enumName;
			}

			Class<?> threadClazz = Class.forName(enumName);
			Method method = threadClazz.getMethod("values");
			Method method_getDesc = threadClazz.getMethod("getDesc");
			Method method_getValue = threadClazz.getMethod("getValue");
			Object enumArray = method.invoke(null);
			StringBuffer sb=new StringBuffer("");
			for (int i = 0; i < Array.getLength(enumArray); i++) {
				Object enumInstance = Array.get(enumArray, i);
				
//				<option value="1">预收利息</option>
				sb.append("<option value='");
				String key = method_getValue.invoke(enumInstance).toString();
				sb.append(key);
				sb.append("'");
				if (key.equals(value)) {
					sb.append(" selected='selected'");
				}
				sb.append(">");
				sb.append(method_getDesc.invoke(enumInstance).toString());
				sb.append("</option>");			
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
