package hello.programmer.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * 日期格式器 <br/>
 * 该格式器不是线程安全的，在每个线程中使用DateFormat时都应该使用DateFormat的相关getInstance方法获取。<br/>
 * 
 * 试图将当前线程中获取的DateFormat实例传给另一个线程使用，可能会出现并发问题。
 * <p>
 * 
 * 例如：
 * 
 * <pre>
 * Date date = DateFormat.getInstance(&quot;yyyy-MM-dd HH.mm.ss&quot;).parse(&quot;2011-06-22 19.49.28&quot;);
 * String str = DateFormat.getInstance(&quot;yyyy-MM-dd HH.mm.ss&quot;).format(date);
 * </pre>
 * 
 * @author xs
 * @created 2011-6-14 下午01:42:18
 * @since v1.1
 * @history
 * @see
 */
public class DateFormat
{

	public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH.mm.ss";

    public static final SimpleDateFormat FORMAT2= new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat FORMAT3= new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
    public static final SimpleDateFormat FORMAT4= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final String FORMAT1 = "yyyy-MM-dd";
	/**
	 * 内置的日期格式器
	 */
	private SimpleDateFormat dateFormat;

	/**
	 * 语言环境
	 */
	private Locale locale;

	/**
	 * 用来在线程内缓存日期格式器
	 */
	private static ThreadLocal<Map<Key, DateFormat>> formatMapThreadLocal = new ThreadLocal<Map<Key, DateFormat>>();

	/**
	 * 
	 * 构造函数，设成了私有来强制用户使用DateFormat.getXXInstance()等方法获取格式器
	 * 
	 * @param dateFormat
	 * @param locale
	 * @author xs
	 * @created 2011-6-14 下午01:42:18
	 */
	private DateFormat(SimpleDateFormat dateFormat, Locale locale)
	{
		this.dateFormat = dateFormat;
		this.locale = locale;
	}

	/**
	 * 获取格式器实例,该格式器的格式采用"yyyy-MM-dd HH.mm.ss", 时区、语言环境从服务器的当前环境中获取。
	 * 
	 * @return
	 * @author xs
	 * @created 2011-6-22 下午07:57:35
	 */
	public static DateFormat getInstance()
	{
		return getInstance(DEFAULT_FORMAT);
	}

	/**
	 * 
	 * 获取格式器实例
	 * 
	 * @param pattern
	 *            模式
	 * @param timeZone
	 *            时区
	 * @param locale
	 *            语言环境
	 * @return 格式器
	 * @author xs
	 * @created 2011-6-22 下午04:02:46
	 */
	public static DateFormat getInstance(String pattern, TimeZone timeZone, Locale locale)
	{
		Map<Key, DateFormat> formatMap = formatMapThreadLocal.get();
		if (formatMap == null)
		{
			formatMap = new HashMap<Key, DateFormat>();
			formatMapThreadLocal.set(formatMap);
		}

		Key key = new Key(pattern, locale);

		DateFormat format = formatMap.get(key);
		if (format == null)
		{
			format = new DateFormat(new SimpleDateFormat(pattern, locale), locale);
			formatMap.put(key, format);
		}
		format.setTimeZone(timeZone);
		return format;
	}

	/**
	 * 
	 * 获取格式器实例,该格式器的时区、语言环境从服务器的当前环境中获取。
	 * 
	 * @param pattern
	 *            模式
	 * @return
	 * @author xs
	 * @created 2011-6-22 下午04:02:46
	 */
	public static DateFormat getInstance(String pattern)
	{
		return getInstance(pattern, TimeZone.getDefault(), Locale.getDefault());
	}

	/**
	 * 
	 * 将java.util.Date对象格式化成String
	 * 
	 * @param date
	 *            日期
	 * @return 字符串
	 * @author xs
	 * @created 2011-6-14 下午01:42:18
	 */
	public String format(Date date)
	{
		return dateFormat.format(date);
	}

	/**
	 * 
	 * 将long型（从标准基准时间即 1970 年 1 月 1 日 00:00:00 GMT以来的指定毫秒数）格式化成String<br/>
	 * 
	 * @param date
	 *            从标准基准时间即 1970 年 1 月 1 日 00:00:00 GMT以来的指定毫秒数
	 * @return 字符串
	 * @author xs
	 * @created 2011-6-14 下午01:42:18
	 */
	public String format(long date)
	{
		return dateFormat.format(new Date(date));
	}

	/**
	 * 
	 * 将时间串解析成java.util.Date,解析失败将抛出RuntimeException
	 * 
	 * @param source
	 *            时间串
	 * @return
	 * @author xs
	 * @created 2011-6-14 下午01:42:18
	 */
	public Date parse(String source) throws ParseException
	{
		return dateFormat.parse(source);
	}

	/**
	 * 
	 * 将时间串解析成距离标准时间的毫秒数,解析失败将抛出RuntimeException
	 * 
	 * @param source
	 *            时间串
	 * @return
	 * @author xs
	 * @created 2011-6-14 下午01:42:18
	 */
	public long parseToLong(String source) throws ParseException
	{
		return parse(source).getTime();
	}

	/**
	 * 
	 * 获取格式器的格式
	 * 
	 * @return 格式
	 * @author xs
	 * @created 2011-6-14 下午01:42:18
	 */
	public String getPattern()
	{
		return dateFormat.toPattern();
	}

	/**
	 * 
	 * 获取时区
	 * 
	 * @return
	 * @author xs
	 * @created 2011-6-14 下午01:42:18
	 */
	public TimeZone getTimeZone()
	{
		return dateFormat.getTimeZone();
	}

	/**
	 * 
	 * 设置时区
	 * 
	 * @param zone
	 * @author xs
	 * @created 2011-6-14 下午01:42:18
	 */
	public void setTimeZone(TimeZone zone)
	{
		dateFormat.setTimeZone(zone);
	}

	/**
	 * 
	 * 获取格式器内部的语言环境
	 * 
	 * @return
	 * @author xs
	 * @created 2011-6-14 下午01:42:18
	 */
	public Locale getLocale()
	{
		return this.locale;
	}

	/**
	 * 用来标示一个Format
	 * 
	 * @author xs
	 * @created 2011-6-22 下午04:02:46
	 * @since v1.0
	 * @history
	 * @see
	 */
	private static class Key
	{
		String pattern;

		Locale locale;

		Key(String pattern, Locale locale)
		{
			this.pattern = pattern;
			this.locale = locale;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((locale == null) ? 0 : locale.hashCode());
			result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (locale == null)
			{
				if (other.locale != null)
					return false;
			}
			else if (!locale.equals(other.locale))
				return false;
			if (pattern == null)
			{
				if (other.pattern != null)
					return false;
			}
			else if (!pattern.equals(other.pattern))
				return false;
			return true;
		}
	}
	
	/** 
	 * 获得指定日期的前一天 
	 *  
	 * @param specifiedDay 
	 * @return 
	 * @throws Exception 
	 */  
	public static String getSpecifiedDayBefore(String specifiedDay) {//可以用new Date().toLocalString()传递参数  
		Calendar c = Calendar.getInstance();  
		Date date = null;  
		try {  
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);  
		} catch (ParseException e) {  
			e.printStackTrace();  
		}  
		c.setTime(date);  
		int day = c.get(Calendar.DATE);  
		c.set(Calendar.DATE, day - 1);  

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c  
				.getTime());  
		return dayBefore;  
	} 
	
	 /**
	     * addDate <p>日期的加减；No 时，分，秒</p>   
	     * <p>于2015年1月10日 由 hz 创建 </p>
	     * @author  <p>当前负责人 hz</p>
	     * @param date 系统时间
	     * @param type 加减的类型 D 日期 M 月份 Y 年
	     * @param into 加减的数量
	     * @return
	     */
	    public static String addDate(Date date, String type, int into) {
	      GregorianCalendar grc = new GregorianCalendar();
	      grc.setTime(date);
	      if (type.equals("D")) {
	        grc.add(GregorianCalendar.DATE, into);
	      } else if (type.equals("M")) {
	        grc.add(GregorianCalendar.MONTH, into);
	      } else if (type.equals("Y")) {
	        grc.add(GregorianCalendar.YEAR, into);
	      } else {
	      }
	      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	      String Sdate = new String(formatter.format(grc.getTime()));
	      return Sdate;
	    }

    /**
     * 获取指定日期的后n天的日期
     * @param specifiedDay 指定日期
     * @param days 指定多少天后
     * @return
     */
    public static Date getSpecifiedDayAfterDays(Date specifiedDay,int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(specifiedDay);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + days);
        return c.getTime();
    }

      /**
       * 获取当前时间的昨日的日期字符串
       * @description （用一句话描述该方法的适用条件、执行流程、适用方法、注意事项 - 可选）
       * @return
       * @return String
       * @throws
       * @author zhourf
       * @create 2016年8月2日 上午11:39:12
       */
      public static String getYestDayString() {
	    Calendar   cal   =   Calendar.getInstance();
	    cal.add(Calendar.DATE,   -1);
	    String yesterday = new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
	    return yesterday;
      }


        public static void main(String[] args) {
            try {
                Date createTime = DateFormat.FORMAT3.parse(DateFormat.FORMAT3.format(new Date()));
                Date targetDate = DateFormat.getSpecifiedDayAfterDays(new Date(),-15);
                System.out.println(createTime + "   " + targetDate);
                if(createTime.before(targetDate)){
                    System.out.println("1");
                }else {
                    System.out.println("2");

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
}
