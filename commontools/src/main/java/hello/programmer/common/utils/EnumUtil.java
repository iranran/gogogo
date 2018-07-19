/**
 * 
 */
package hello.programmer.common.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * @Description 枚举相关工具方法
 * @author 张伯文
 * @date 2016年12月13日上午11:33:11
 */
public class EnumUtil {

	

	/**
	 * 转换枚举value为枚举描述
	 * @author 张伯文
	 * @date 2016年11月23日下午6:12:57
	 * 
	 * @param enumName 枚举完整类名如com.asset.enums.IsPrechargeInterestEnum
	 * com.asset.enums包下可直接写枚举如IsPrechargeInterestEnum
	 * @param value 枚举的value
	 * @return 翻译的枚举描述，如果发生任何异常，返回"".
	 */
	public static String tranEnumName(String enumName, int value) {
		return tranEnumName(getEnumClass(enumName), value);
	}
	
	/**
	 * 转换枚举value为枚举描述
	 * @author 张伯文
	 * @date 2016年11月23日下午6:12:57
	 * 
	 * @param enumName 枚举完整类名如com.asset.enums.IsPrechargeInterestEnum
	 * com.asset.enums包下可直接写枚举如IsPrechargeInterestEnum
	 * @param value 枚举的value
	 * @return 翻译的枚举描述，如果发生任何异常，返回"".
	 */
	public static String tranEnumName(String enumName, String value) {
		return tranEnumName(getEnumClass(enumName), Integer.valueOf(value));
	}
	
	/**
	 * 转换枚举value为枚举描述
	 * @author 张伯文
	 * @date 2016年11月23日下午6:12:57
	 * 
	 * @param enumClass 枚举完整类名如com.asset.enums.IsPrechargeInterestEnum
	 * com.asset.enums包下可直接写枚举如IsPrechargeInterestEnum
	 * @param value 枚举的value
	 * @return 翻译的枚举描述，如果发生任何异常，返回"".
	 */
	public static String tranEnumName(Class<?> enumClass, String value) {
		return tranEnumName(enumClass, Integer.valueOf(value));
	}
	/**
	 * 翻译枚举value值为枚举描述
	 * @author 张伯文
	 * @date 2017年2月27日下午8:38:27
	 * 
	 * @param enumClass
	 * @param value
	 * @return
	 */
	public static String tranEnumName(Class<?> enumClass, int value) {
		try {
			Method method = enumClass.getMethod("values");
			Method method_getDesc = enumClass.getMethod("getDesc");
			Method method_getValue = enumClass.getMethod("getValue");
			Object enumArray = method.invoke(null);

			for (int i = 0; i < Array.getLength(enumArray); i++) {
				Object enumInstance = Array.get(enumArray, i);
				int key = Integer.valueOf(method_getValue.invoke(enumInstance).toString());
				if (value == key) {
					return method_getDesc.invoke(enumInstance).toString();
				}
			}
		} catch (Exception e) {
		}
		return "";
	}
	
	/**
	 * 用desc获取value
	 * @author 张伯文
	 * @date 2017年6月25日下午6:33:58
	 * 
	 * @param enumClass
	 * @return
	 */
	public static Integer getEnumValue(Class<?> enumClass, String desc) {
		try {
			Method method = enumClass.getMethod("values");
			Method method_getDesc = enumClass.getMethod("getDesc");
			Method method_getValue = enumClass.getMethod("getValue");
			Object enumArray = method.invoke(null);

			for (int i = 0; i < Array.getLength(enumArray); i++) {
				Object enumInstance = Array.get(enumArray, i);
				String desc_tmp=method_getDesc.invoke(enumInstance).toString();
				if(desc_tmp.equals(desc)){
					return Integer.valueOf(method_getValue.invoke(enumInstance).toString());
				}
			}
		} catch (Exception e) {
		}
		return -1;
	}
	
	/**
	 * 生成根据枚举生成&lt;option&gt;标签
	 * @author 张伯文
	 * @date 2016年11月23日下午7:10:20
	 * 
	 * @param enumName 枚举完整类名如com.asset.enums.IsPrechargeInterestEnum
	 * com.asset.enums包下可直接写枚举如IsPrechargeInterestEnum
	 * @param value 枚举的value
	 * @return <option>标签的字符串，如果发生任何异常，返回"".
	 */
	public static String getEnumOptions(String enumName, String value) {
		return getEnumOptions(getEnumClass(enumName), value);
	}
	
	/**
	 * 生成根据枚举生成&lt;option&gt;标签
	 * @author 张伯文
	 * @date 2017年2月27日下午8:39:11
	 * 
	 * @param enumClass
	 * @param value
	 * @return
	 */
	public static String getEnumOptions(Class<?> enumClass, String value) {
		try {

			Method method = enumClass.getMethod("values");
			Method method_getDesc = enumClass.getMethod("getDesc");
			Method method_getValue = enumClass.getMethod("getValue");
			Object enumArray = method.invoke(null);
			StringBuffer sb=new StringBuffer("");
			for (int i = 0; i < Array.getLength(enumArray); i++) {
				Object enumInstance = Array.get(enumArray, i);
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
			return "";
		}
	}
	
	/**
	 * 类路径字符串获取class
	 * com.asset.enums包下可直接写枚举如IsPrechargeInterestEnum
	 * @author 张伯文
	 * @date 2017年2月27日下午8:39:23
	 * 
	 * @param enumName
	 * @return
	 */
	public static Class<?> getEnumClass(String enumName) {
		Class<?> clazz=null;
		try {
			if(enumName.indexOf('.')<0){
				enumName="com.asset.enums."+enumName;
			}
			clazz = Class.forName(enumName);
		} catch (Exception e) {
		}
		return clazz;
	}
	
	
}
