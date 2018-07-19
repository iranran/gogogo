/**
 * 
 */
package hello.programmer.common.utils;

import com.asset.framework.utils.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * @Description 时间解析相关方法
 * 时间可以用Date 表示，如new Date()
 * 可以用字符串表示，"00:00:00"-"23:59:59"
 * 可以用秒表示，0-86400
 * @author 张伯文
 * @date 2016年12月9日上午11:21:54
 */
public class TimeUtil {
	private static Logger LOGGER = LoggerFactory.getLogger(TimeUtil.class);
	
	public static final int ValidStartTimeSec=0;
	public static final int ValidEndTimeSec=86400;
	public static final String ValidStartTimeStr="00:00:00";
	public static final String ValidEndTimeStr="23:59:59";

	
	/**
	 * @Description 获取表示时间的字符串
	 * @author 张伯文
	 * @date 2016年12月9日上午11:50:03
	 * 
	 * @param date
	 * @return
	 */
	public static String toTime(Date date){
		try{
			if($.isTrue(date)){
				return DateUtils.formatDate(date, "HH:mm:ss");
			}
		}catch(Exception e){
			LOGGER.error("解析时间失败，输入date="+date);
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * @Description 获取表示时间的字符串
	 * @author 张伯文
	 * @date 2016年12月9日下午4:53:26
	 * 
	 * @param timeSec
	 * @return
	 */
	public static String toTime(int timeSec){
		try{
			if(timeSec>=ValidStartTimeSec && ValidStartTimeSec<ValidEndTimeSec){
				String hour=String.valueOf(timeSec/3600);
				String minute=String.valueOf((timeSec%3600)/60);
				String second=String.valueOf(timeSec%60);
				if(hour.length()==1)hour="0"+hour;
				if(minute.length()==1)minute="0"+minute;
				if(second.length()==1)second="0"+second;
				return hour+":"+minute+":"+second;
			}
		}catch(Exception e){
			LOGGER.error("解析时间失败，输入timeSec="+timeSec);
			e.printStackTrace();
		}

		return ValidStartTimeStr;
	}
	/**
	 * @Description 时间字符串to秒数
	 * @author 张伯文
	 * @date 2016年12月9日上午11:51:21
	 * 
	 * @param timeStr
	 * @return
	 */
	public static int toTimeSec(String timeStr){
		try{
			if($.isTrue(timeStr)){
				String[] arr=timeStr.split(":");
				return $.toInt(arr[0])*3600+$.toInt(arr[1])*60+$.toInt(arr[2]);
			}
		}catch(Exception e){
			LOGGER.error("解析时间失败，输入timeStr="+timeStr);
			e.printStackTrace();
		}

		return 0;
	}
	/**
	 * @Description 时间Date to秒数
	 * @author 张伯文
	 * @date 2016年12月9日上午11:51:50
	 * 
	 * @param time
	 * @return
	 */
	public static int toTimeSec(Date time){
		return toTimeSec(toTime(time));
	}

	public static boolean isValidTime(Date time){
		int timeSec=toTimeSec(time);
		return timeSec>=ValidStartTimeSec && timeSec<ValidEndTimeSec;
	}
	
	public static boolean isValidTime(String time){
		int timeSec=toTimeSec(time);
		return timeSec>=ValidStartTimeSec && timeSec<ValidEndTimeSec;
	}
	
	public static boolean isValidTime(int timeSec){
		return timeSec>=ValidStartTimeSec && timeSec<ValidEndTimeSec;
	}
	
	
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		TimeUtil t=new TimeUtil();
		Date now=new Date();
		String time1="01:00:00";
		String time2="10:00:00";
		int time3=1;
		int time4=86401;
		
//		$.log(t.isValidTime(time1));
//		$.log(t.isValidTime(time2));
//		$.log(t.isValidTime(time3));
//		$.log(t.isValidTime(time4));
//		$.log(t.isValidTime(now));
		
//		$.log(t.toTimeSec(time1));
//		$.log(t.toTimeSec(time2));
//		$.log(t.toTimeSec(time3));
//		$.log(t.toTimeSec(time4));
		$.log(t.toTime(3601));
	}// main
	
}
