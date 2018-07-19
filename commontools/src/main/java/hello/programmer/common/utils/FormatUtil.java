package hello.programmer.common.utils;

import com.asset.framework.utils.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 一些格式化方法，用于转换输入为安全值
 * toxxx一般为遇到空或非法值转换为安全的值
 * toxxxNull一般为遇到非法值输出null
 * 全部捕获异常
 * @author 张伯文
 * @date 2017年9月26日下午8:37:08
 */
public class FormatUtil {
	private static Logger logger = LoggerFactory.getLogger(FormatUtil.class);
	
	/**
	 * 以下一组toString转换常见类型为字符串，如果空则为""
	 * @author 张伯文
	 * @date 2017年8月30日下午8:46:41
	 * 
	 * @param val
	 * @return
	 */
	public static String toString(Integer val) {
		return val==null?"":val.toString();
	}
	public static String toString(String val) {
		return val==null?"":val;
	}
	public static String toString(BigDecimal val) {
		return val==null?"":val.toString();
	}
	public static String toString(Date val) {
		return val==null?"":DateUtils.toDateTimeStr(val);
	}
	public static String toString(Date val,String pattern) {
		return val==null?"":DateUtils.formatDate(val, pattern);
	}
	public static String toString(Object val) {
		return val==null?"":val.toString();
	}
	
	/**
	 * 如果为空则填字符串0，用来输出一些可能为null的数字
	 * @author 张伯文
	 * @date 2017年9月26日下午3:49:17
	 * 
	 * @param val
	 * @return
	 */
	public static String toStringZero(Integer val) {
		return val==null?"0":val.toString();
	}
	public static String toStringZero(String val) {
		return val==null?"0":val;
	}
	public static String toStringZero(BigDecimal val) {
		return val==null?"0":val.toString();
	}
	public static String toStringZero(Date val) {
		return val==null?"0":DateUtils.toDateTimeStr(val);
	}
	public static String toStringZero(Date val,String pattern) {
		return val==null?"0":DateUtils.formatDate(val, pattern);
	}
	public static String toStringZero(Object val) {
		return val==null?"0":val.toString();
	}
	
	/**
	 * 转换输入为2位小数点字符串
	 * 如果空则为0.00
	 * @author 张伯文
	 * @date 2017年10月10日下午4:08:27
	 * 
	 * @param val
	 * @return
	 */
	public static String toBigDecimalStrDotTwo(Object val) {
		try{
			return val==null?"0.00":new BigDecimal(val.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		}catch(Exception e){
			return "0.00";
		}
	}
	public static String toBigDecimalStrDotTwo(BigDecimal val) {
		try{
			return val==null?"0.00":val.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		}catch(Exception e){
			return "0.00";
		}
	}
	/**
	 * 以下一组方法转换obj为BigDecimal，如果null或者异常则返回BigDecimal.ZERO
	 * @author 张伯文
	 * @date 2017年8月30日下午8:47:37
	 * 
	 * @param val
	 * @return
	 */
	public static BigDecimal toBigDecimal(Object val) {
		try{
			return val==null?BigDecimal.ZERO:new BigDecimal(val.toString());
		}catch(Exception e){
			return BigDecimal.ZERO;
		}
	}
	public static BigDecimal toBigDecimal(Integer val) {
		return val==null?BigDecimal.ZERO:new BigDecimal(val);
	}
	public static BigDecimal toBigDecimal(String val) {
		try{
			return val==null?BigDecimal.ZERO:new BigDecimal(val);
		}catch(Exception e){
			return BigDecimal.ZERO;
		}
	}
	public static BigDecimal toBigDecimalException(Object val) {
		return new BigDecimal(val.toString());
	}
	/**
	 * 以下一组方法转换字符串为整数，如果异常或null则返回0
	 * @author 张伯文
	 * @date 2017年8月30日下午9:23:27
	 * 
	 * @param val
	 * @return
	 */
	public static Integer toInteger(Object val) {
		return toInteger(val,0);
	}
	public static Integer toInteger(String val) {
		return toInteger(val,0);
	}
	public static Integer toIntegerNegOne(Object val) {
		return toInteger(val,-1);
	}
	public static Integer toIntegerNegOne(String val) {
		return toInteger(val,-1);
	}
	public static Integer toIntegerNull(Object val) {
		return toInteger(val,null);
	}
	public static Integer toIntegerNull(String val) {
		return toInteger(val,null);
	}
	public static Integer toIntegerException(Object val) {
		return Integer.valueOf(val.toString());
	}
	public static Integer toInteger(Object val,Integer defaultVal) {
		try{
			return val==null?defaultVal:new Integer(val.toString());
		}catch(Exception e){
			return defaultVal;
		}
	}
	public static Integer toInteger(String val,Integer defaultVal) {
		try{
			return val==null?defaultVal:new Integer(val);
		}catch(Exception e){
			return defaultVal;
		}
	}
	public static Date toDateFromDateStrErrNull(Object val) {
		try{
			return val==null?null:DateUtils.parseDate(val.toString(), DateUtils.PATTERN_DATE);
		}catch(Exception e){
			return null;
		}
	}
	public static Date toDateFromDateStrErrNull(String val) {
		try{
			return val==null?null:DateUtils.parseDate(val, DateUtils.PATTERN_DATE);
		}catch(Exception e){
			return null;
		}
	}
	public static String toDateStrFromDateErrNull(Object val) {
		try{
			return val==null?null:DateUtils.formatDate((Date)val, DateUtils.PATTERN_DATE);
		}catch(Exception e){
			return null;
		}
	}
	public static String toDateStrFromDateErrNull(Date val) {
		try{
			return val==null?null:DateUtils.formatDate(val, DateUtils.PATTERN_DATE);
		}catch(Exception e){
			return null;
		}
	}
	public static String toDateStrFromDateErrBlank(Object val) {
		try{
			return val==null?"":DateUtils.formatDate((Date)val, DateUtils.PATTERN_DATE);
		}catch(Exception e){
			return "";
		}
	}
	public static String toDateStrFromDateErrBlank(Date val) {
		try{
			return val==null?"":DateUtils.formatDate(val, DateUtils.PATTERN_DATE);
		}catch(Exception e){
			return "";
		}
	}

	public static void main(String[] args)throws Exception {

//		System.out.println(Integer.valueOf("1"));
//		System.out.println(Integer.valueOf(1));
//		System.out.println(Integer.valueOf((Object)new Exception("asdasd")));
//		System.out.println(Integer.valueOf(null));







	}
}
