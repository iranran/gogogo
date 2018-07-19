package hello.programmer.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.asset.framework.utils.date.DateUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;


/**
 * @Description <span style="color:red;font-size:20px;">大部分基于反射实现，无法兼容所有类型，请测试后使用!</span>
 * <br>类似jquery，用于便捷操作java数据，提供常用逻辑和简化的语法。
 * <br>有很多是重方法，一些操作可能很费时间
 * @author 张伯文
 * @date 2016年12月8日下午3:58:47
 */
public class $ {

	
	private Object val;
	// -----常量-----//
	public static $ $_NULL=new $(null);
	public static boolean isDebug=true;
	
	public static String dueTimeStr = "2017-05-15 19:59:59";//debug标志过期时间，需要手动设置
	public static String jvmStartTimeStamp= "";//部署时间的时间戳，自动初始化
	// -----静态初始化-----//
	static {
		// 设置一个debug标志过期时间，一般是上线前，过期则为false，这样就不需要别人维护这个标志了
		try {
			Date dueDate = DateUtils.parseDate(dueTimeStr, DateUtils.PATTERN_DATETIME);
			
			if (new Date().getTime() > dueDate.getTime()) {
				$.isDebug = false;
			}
			
			jvmStartTimeStamp=DateUtils.formatDate(new Date(), DateUtils.PATTERN_DATETIME_STAMP);
		} catch (ParseException e) {
			$.isDebug = false;
			jvmStartTimeStamp= "20161218220001";
		}
		$.log("$初始化完成，设置isDebug=" + $.isDebug + ",dueTimeStr=" + dueTimeStr);
	}

	// -----构造方法-----//
	public $(Object val) {
		this.val = val;
	}


	// -----静态方法-----//
	public static void log(Object x){
		System.out.println(x);
	}
	
	/**
	 * 判断对象是否为真，和js的逻辑类似
	 * <br>null,0,"",size=0的集合为false，其他为true<br>
	 * 对于集合（list,map，数组，字符串），长度不为0返回true
	 * 对于Integer，不为0返回true
	 * 对于null返回false
	 * 对于boolean，返回boolean
	 * <br>方法判断较多，速度较慢，不宜在循环中使用，不宜高频调用<br>
	 * @author 张伯文
	 * @date 2016年12月10日下午1:53:19
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isTrue(Object val) {
		if($.hasVal(val)){
			if (val instanceof Integer) {
				return ((Integer) val) != 0;
			}
			if (val instanceof Boolean) {
				return ((Boolean) val);
			}
			return true;
		}else{
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
		return LogicUtil.hasVal(val);
	}
	
	/**
	 * 判断Object是否为0或空集合
	 * 对于集合，空集合返回true
	 * 对于String，为"0"返回true
	 * @param val
	 * @return
	 */
	public static boolean isZero(Object val) {
		if (val == null)
			return false;

		if (val instanceof String) {
			try{
				return Integer.valueOf(val.toString())==0;
			}catch(Exception e){
				return false;
			}
		}

		if (val instanceof Boolean) {
			return !((Boolean) val);
		}
		
		if (val instanceof Integer) {
			return ((Integer) val) == 0;
		}
		
		if (val instanceof BigDecimal) {
			return ((BigDecimal)val).compareTo(BigDecimal.ZERO)==0;
		}
		
		if (val instanceof Collection) {
			return ((Collection) val).isEmpty();
		}
		
		if (val instanceof Map) {
			return ((Map) val).isEmpty();
		}
		
		return false;
	}
	
	public static boolean hasNull(Object... vals) {
		for(Object val:vals){
			if(val==null)return true;
		}
		return false;
	}
	//toXXX
	public static String toString(Object o) {
		if(o==null)return "";

		return JSONArray.toJSONString(o, SerializerFeature.WriteMapNullValue);
	}
	public static String toStringSimple(Object o) {
		return (o == null) ? "" : o.toString();
	}
	public static int toInt(String s) {
		return Integer.valueOf(s).intValue();
	}
	

	public static Map toMap(Object o) {
		try {
			return PropertyUtils.describe(o);
		} catch (Exception e) {}
		return new HashMap();
	}
	public static Object toBean(Object bean,Map map) {
		try {
			Object newBean=BeanUtils.cloneBean(bean);
			BeanUtils.populate(newBean, map);
			return newBean;
		} catch (Exception e) {}
		return bean;
	}
	
	public static String toJsonString(Object o) {
		return JSONArray.toJSONString(o);
	}
	public static JSON toJSON(Object o) {
		return (JSON)JSONObject.toJSON(o);
	}

	public static void throwDebugException(String... msg) throws Exception{

		if(msg.length>0){
			throw new Exception(msg[0]);
		}else{
			throw new Exception("开发调试异常");
		}
		
	}
	/**
	 * 抛异常带参数
	 * @author 张伯文
	 * @date 2017年8月31日下午5:24:42
	 * 
	 * @param msg
	 * @param paras
	 * @throws Exception
	 */
	public static void throwException(String msg,Object ... paras) throws Exception{
		throw new Exception(String.format(msg, paras));
	}
	//getXXX

	
	
	
	
	
	// -----实例方法-----//
	// isXXX

	public boolean isNull() {
		return val == null;
	}



	//类似js的判断是否为真
	public boolean isTrue() {
		return $.isTrue(val);
	}

	// toXXX
	// 支持Object,String转换为基础类型
	// 支持map、bean、json、list、数组互相转换
	// tostring，转换为json输出

	
	// get,setXXX

	public $ get(String key){
		if(val==null)return $_NULL;
		try {
			if(val instanceof Map){
				return new $(((Map)val).get(key));
			}
			return new $(PropertyUtils.getProperty(val, key));
		} catch (Exception e) {}
		
		return $_NULL;
	}
	public $ get(int index){
		if(val==null)return $_NULL;
		
		if (val instanceof List) {
			return new $(((List) val).get(index));
		}
		
		if (val.getClass().isArray()) {
			return new $(Array.get(val, index));
		}

		return $_NULL;
	}
	
	public Object Val(){
		return val;
	}
	
	public $ put(String key,Object value){
		if(val==null)return this;
		try {
			if(val instanceof Map){
				((Map)val).put(key,value);
			}
			PropertyUtils.setProperty(val, key, value);
		} catch (Exception e) {}
		return this;
	}
	
	public $ add(Object value){
		if(val==null)return $_NULL;
		
		if (val instanceof List) {
			((List) val).add(value);
		}

		return this;
	}
	public int intVal(){
		
		if(val==null)return 0;
		
		if (val instanceof Integer) {
			return ((Integer) val).intValue();
		}
		
		if (val instanceof String) {
			return $.toInt(val.toString());
		}
		
		try{
			return Integer.valueOf(val.toString());
		}catch(Exception e){
			return 0;
		}
		
	}
	
	public String StringVal(){
		
		if(val==null)return "";
		
		return val.toString();
		
	}
	

	/**
	 * @Description 判断$(val)是否在传入参数中
	 * <br>相同数据类型比较才有意义
	 * <br>逗号分隔传入多个参数，或者传入一个数组
	 * @author 张伯文
	 * @date 2016年12月27日下午9:13:19
	 * 
	 * @param values
	 * @return
	 */
	public boolean isIn(Object... values){
		if(val==null)return values==null;
		for(Object o:values){
			if(o.getClass().equals(val.getClass())){
				if(o.equals(val))return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	private static long timeStamp=0;
	/**
	 * 输出距离上次调用本方法过去了多少ms
	 * @author 张伯文
	 * @date 2016年12月27日下午9:13:19
	 * 
	 * @param values
	 * @return
	 */
	public static long showTimeMs(String msg){
		long currentTime = System.currentTimeMillis();
		long useTime=currentTime-timeStamp;
		$.log(msg+" 距离上次调用耗时"+useTime);
		timeStamp=currentTime;
		return useTime;
	}
	public static long showTimeMs(String msg,boolean isReset){
		if(isReset){
			timeStamp=System.currentTimeMillis();
			$.log(msg+" 初始化计时");
			return timeStamp;
		}else{
			return showTimeMs(msg);
		}
	}
	
//	public static timeCalBuff=new 
//	public static void calUseTime(String mark,int group,boolean isFirst,boolean loopMode,int phaseNumber){
//		
//	}

	private static long[] timeStampBuff=new long[10];
	private static int[] loopCountBuff=new int[10];
	private static int[] loopStepBuff=new int[10];
	private static long[][] timeBuff=new long[10][10];
	public static void calLoopTime(String mark,int group){
		long useTime=System.currentTimeMillis()-timeStampBuff[group];
		timeStampBuff[group]=System.currentTimeMillis();
		
		timeBuff[group][loopStepBuff[group]]=timeBuff[group][loopStepBuff[group]]+useTime;
		loopStepBuff[group]=loopStepBuff[group]+1;
		
	}
	public static void initLoopTime(String mode,int group){
		if(mode.equals("init")){
			timeStampBuff[group]=System.currentTimeMillis();
			loopCountBuff[group]=0;
			loopStepBuff[group]=0;
			timeBuff[group]=new long[10];
		}
		if(mode.equals("enterLoop")){
			timeStampBuff[group]=System.currentTimeMillis();
			loopCountBuff[group]=loopCountBuff[group]+1;
			loopStepBuff[group]=0;
		}
	}
	public static void reportLoopTime(int group){
		int loopCount=0;
		for(long time:timeBuff[group]){
			if(time>0){
				loopCount++;
			}
		}
		$.log("group"+group+"循环了"+loopCountBuff[group]+"次");
		for(int i=0;i<loopCount;i++){
			$.log("step"+i+"执行了"+timeBuff[group][i]+"ms");
			$.log("平均每次"+(timeBuff[group][i]/loopCountBuff[group])+"ms");
		}
	}

	public static void costTimeTask(int ms){
		for(int i=0;i<ms;i++){
			for(int j=0;j<116666;j++){
				System.currentTimeMillis();
			}
		}
	}
	
	private static long[] timeStampSlaceBuff=new long[100];
	private static long[] useTimeSlaceBuff=new long[100];
	private static long[] loopCountSlaceBuff=new long[100];
	public static void startCalTimeSlice(int group){
		timeStampSlaceBuff[group]=System.currentTimeMillis();
		loopCountSlaceBuff[group]=loopCountSlaceBuff[group]+1;
	}
	public static void finishCalTimeSlice(int group){
		useTimeSlaceBuff[group]=useTimeSlaceBuff[group]+System.currentTimeMillis()-timeStampSlaceBuff[group];
	}
	public static void reportSliceTime(int group){
		if(loopCountSlaceBuff[group]==0){
			return;
		}
		$.log("slicegroup"+group+"循环了"+loopCountSlaceBuff[group]+"次");
		$.log("执行了"+useTimeSlaceBuff[group]+"ms");
		if(loopCountSlaceBuff[group]>0){
			$.log("平均每次"+(useTimeSlaceBuff[group]/loopCountSlaceBuff[group])+"ms");
		}

	}
	public static void reportAllSliceTime(){
		for(int i=0;i<100;i++){
			reportSliceTime(i);
		}
	}
	public static void resetAllSliceTime(){
		for(int i=0;i<100;i++){
			timeStampSlaceBuff[i]=0;
			useTimeSlaceBuff[i]=0;
			loopCountSlaceBuff[i]=0;
		}
	}
	
	/**
	 * 如果val!=true抛异常
	 * @author 张伯文
	 */
	public static boolean checkTrue(boolean val,String msg,Object... paras) throws Exception{
		if(!val){
			throw new Exception(String.format(msg, paras));
		}
		return val;
	}
	/**
	 * 如果val!=false抛异常
	 * @author 张伯文
	 */
	public static boolean checkFalse(boolean val,String msg,Object... paras) throws Exception{
		if(val){
			throw new Exception(String.format(msg, paras));
		}
		return val;
	}
	/**
	 * 如果val!=1抛异常
	 * @author 张伯文
	 */
	public static int checkOne(int val,String msg,Object... paras) throws Exception{
		if(val!=1){
			throw new Exception(String.format(msg, paras));
		}
		return val;
	}
	/**
	 * 如果val!=0抛异常
	 * @author 张伯文
	 */
	public static int checkZero(int val,String msg,Object... paras) throws Exception{
		if(val!=0){
			throw new Exception(String.format(msg, paras));
		}
		return val;
	}
	/**
	 * 如果null抛异常
	 * @author 张伯文
	 */
	public static <T> T checkNotNull(T val,String msg,Object... paras) throws Exception{
		if(val==null){
			throw new Exception(String.format(msg, paras));
		}
		return val;
	}
	
	//日志相关
//	public static final int LOG_ERR=1;
//	public static final int LOG_WARN=2;
//	public static final int LOG_INFO=3;
	
	public static String toFormatLog(String title,String reason,String suggest,String keyPara,String context,Object... args){
		StringBuffer sb=new StringBuffer("\r\n#日志标题：").append(title).append("\r\n");
		sb.append("原因：").append(reason).append("\r\n");
		sb.append("排查建议：").append(suggest).append("\r\n");
		sb.append("关键参数：").append(keyPara).append("\r\n");
		sb.append("代码位置：").append(new Throwable().getStackTrace()[1].toString()).append("\r\n");
		sb.append("上下文参数：").append(context);
		return String.format(sb.toString(), args);
	}


	/**
	 * 每1秒返回真一次，用来在循环中输出程序状态
	 * 元祖timeMs存储上次触发时的ms值，初始化为0即可
	 * @author 张伯文
	 * @date 2017年8月2日上午11:52:45
	 * 
	 * @param timeMs
	 * @return
	 */
	public static boolean isTrueEverySec(Unit<Long> timeMs){
		long curMs=System.currentTimeMillis();
		if(curMs-timeMs.getValue()>1000){//每1秒为真一次
			timeMs.setValue(curMs);
			return true;
		}else{
			return false;
		}
	}
	
	// -----测试用方法-----//
	/**
	 * @Description 测试用
	 * @author 张伯文
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public static void main(String[] args)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		
		JSONObject obj=JSONObject.parseObject("{}");
		JSONArray arr=JSONObject.parseArray("[]");
		
		obj.put("aa", 123);
		arr.add(obj);
		$.log(arr.toJSONString());
		obj.put("aa", 456);
		$.log(arr.toJSONString());
		 
	}// main
	


	
}// class
