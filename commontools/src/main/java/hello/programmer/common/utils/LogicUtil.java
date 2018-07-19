package hello.programmer.common.utils;

import com.asset.framework.utils.date.DateUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 容纳逻辑运算相关方法(加减乘除与或非相等不等)
 * @author 张伯文
 * @date 2017年8月31日下午6:00:22
 */
public class LogicUtil {

	/**
	 * 使用equals判断相等，可以为null,其中null==null
	 * @author 张伯文
	 * @date 2017年8月31日下午6:04:51
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isEqual(Object a,Object b){
		if(a==null && b==null){
			return true;
		}else if(a==null || b==null){
			return false;
		}else{
			return a.equals(b);
		}
	}
	public static boolean isEqual(BigDecimal a,BigDecimal b){
		if(a==null && b==null){
			return true;
		}else if(a==null || b==null){
			return false;
		}else{
			return a.compareTo(b)==0;
		}
	}
	public static boolean isNotEqual(BigDecimal a,BigDecimal b){
		return !LogicUtil.isEqual(a, b);
	}
	public static boolean isNotEqual(Object a,Object b){
		return !LogicUtil.isEqual(a, b);
	}
	public static boolean isEqualDate(Date a,Date b){
		if(a==null && b==null){
			return true;
		}else if(a==null || b==null){//只有一个为空，不等
			return false;
		}else{
			try {
				return DateUtils.compareDate(a, b)==0;
			} catch (ParseException e) {
				return false;
			}
		}
	}
	
	public static BigDecimal add(BigDecimal a,BigDecimal b){
		a=a==null?BigDecimal.ZERO:a;
		b=b==null?BigDecimal.ZERO:b;
		return a.add(b);
	}
	
	public static boolean isNumeric(String str) {
		try {
			return new BigDecimal(str) != null;
		} catch (Exception e) {
			return false;
		}
	}


	/**
	 * 判断Object是否为空或空集合
	 * 对于集合，空集合返回false
	 * 对于String，长度为0返回false
	 * @param val
	 * @return
	 */
	public static boolean hasVal(Object val) {
		if (val == null) return false;
		if (val instanceof String) {
			return ((String) val).length() >0;
		}
		if (val instanceof Collection) {
			return !((Collection) val).isEmpty();
		}
		if (val instanceof Map) {
			return !((Map) val).isEmpty();
		}
		if (val.getClass().isArray()) {
			return Array.getLength(val)!=0;
		}
		return true;
	}


	/**
	 * 相当于hasVal(val)&&hasVal(val)&&hasVal(val)。。。
	 * @author 张伯文
	 * createTime 2018/7/3-11:08
	 */
	public static boolean allHasVal(Object... vals) {
		if(vals==null||vals.length==0){
			return false;
		}
		for (Object val : vals) {
			if(!hasVal(val)){
				return false;
			}
		}
		return true;
	}
	/**
	 * 相当于hasVal(val)||hasVal(val)||hasVal(val)。。。
	 * @author 张伯文
	 * createTime 2018/7/3-11:08
	 */
	public static boolean anyHasVal(Object... vals) {
		if(vals==null||vals.length==0){
			return false;
		}
		for (Object val : vals) {
			if(hasVal(val)){
				return true;
			}
		}
		return false;
	}
    public static boolean isNotNull(Object o){
        return o!=null;
    }
    public static boolean isNull(Object o){
        return o==null;
    }


    /**
     * 判斷val 是否和inVals中任意一个相等，实现类似sql中 val in(val1,val2...)的逻辑
	 * 没有空指针检查，假设都不为空
     * @author 张伯文
     * createTime 2018/7/2-16:19
     */
    public static <T> boolean isIn(T val,T... inVals ){
		for (T data : inVals) {
			if (val.equals(data)) {
				return true;
			}
		}
		return false;
	}

}
