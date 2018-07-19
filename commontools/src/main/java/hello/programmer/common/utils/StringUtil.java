package hello.programmer.common.utils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static final String charSet_utf8="UTF-8";
	public static final String charSet_gbk="GBK";
	
	
	
	public static boolean isValid(Object ... str){
		if(str.length==0)return false;
		for(Object oo:str){
			if(oo==null || oo.toString().equals(""))return false;
		}
		return true;
	}
	
	
	/**
	 * 处理数据库获取的ids，去重复，去$,如$,1,3,2,2,1,$
	 * @author meepo
	 * @date 2017年4月21日下午10:16:16
	 * 
	 * @param str
	 * @return
	 */
	public static String trimIds$(String str) {

		if (str == null || str.equals("")) {
			return "";
		}

		String[] strBff = str.split(",");
		Map<String, String> map = new HashMap<>();
		StringBuffer sb = new StringBuffer();
		boolean isFirstId = true;
		for (String idStr : strBff) {
			if (idStr.equals("$")) {
				continue;
			}

			if (map.get(idStr) == null) {
				map.put(idStr, idStr);
				if (isFirstId) {
					isFirstId = false;
					sb.append(idStr);
				} else {
					sb.append(",").append(idStr);
				}
			}
		}

		return sb.toString();
	}
	
	/**
	 * 判断id是否在逗号分隔的id字符串中
	 */
	public static boolean isInIds(int id,String idsStr) {
		return (","+idsStr+",").indexOf(","+id+",")>0;
	}
	
	

    /**
     * 交换驼峰和下划线
     */
	public static String exchangeUnderlineAndCamel(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char cc = str.charAt(i);

			if (cc == '_') {// 下划线的
				sb.append(String.valueOf(str.charAt(i + 1)).toUpperCase());
				i++;
			} else if (Character.isUpperCase(cc)) {// 驼峰的
				if (Character.isUpperCase(str.charAt(i - 1))) {// 如果前一个也是大写就跳过
					sb.append(String.valueOf(cc));
					continue;
				}
				sb.append("_" + String.valueOf(cc).toLowerCase());
			} else {
				sb.append(String.valueOf(cc));
			}
		}
		return sb.toString();
	}
    /**
     * 下划线转驼峰法
     * @param line 源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line,boolean smallCamel){
        if(line==null||"".equals(line)){
            return "";
        }
        StringBuffer sb=new StringBuffer();
        Pattern pattern=Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(smallCamel&&matcher.start()==0?Character.toLowerCase(word.charAt(0)):Character.toUpperCase(word.charAt(0)));
            int index=word.lastIndexOf('_');
            if(index>0){
                sb.append(word.substring(1, index).toLowerCase());
            }else{
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }
    /**
     * 驼峰法转下划线
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line,boolean isUpperCase){
        if(line==null||"".equals(line)){
            return "";
        }
        line=String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb=new StringBuffer();
        Pattern pattern=Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            
            if(isUpperCase){
            	sb.append(word.toUpperCase());
            }else{
            	sb.append(word.toLowerCase());
            }
            sb.append(matcher.end()==line.length()?"":"_");
        }
        return sb.toString();
    }
    /**
     * 首字母大写
     */
	public static String firstCharUpper(String str) {
		return str.substring(0,1).toUpperCase()+str.substring(1,str.length());
	}
    /**
     * 首字母xiao写
     */
	public static String firstCharLower(String str) {
		return str.substring(0,1).toLowerCase()+str.substring(1,str.length());
	}
	
	/**
	 * 返回正则匹配的字符串，使用分隔符分隔
	 */
	public static String getMatchStr(String str,String regx,String seperator) {
		
        StringBuffer sb=new StringBuffer();
        Pattern pattern=Pattern.compile(regx);
        Matcher matcher=pattern.matcher(str);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(word).append(seperator);
        }
        return sb.substring(0, sb.length()-seperator.length());
	}
	
//	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();  
//	public static String bytesToHex(byte[] bytes) {  
//	    char[] hexChars = new char[bytes.length * 2];  
//	    for ( int j = 0; j < bytes.length; j++ ) {  
//	        int v = bytes[j] & 0xFF;  
//	        hexChars[j * 2] = hexArray[v >>> 4];  
//	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//	    }  
//	    return new String(hexChars);  
//	}
	
	public static String bytesToHex(byte[] bytes) {
		StringBuffer md5str = new StringBuffer();
		// 把数组每一字节换成16进制连成md5字符串
		int digital;
		for (int i = 0; i < bytes.length; i++) {
			digital = bytes[i];

			if (digital < 0) {
				digital += 256;
			}
			if (digital < 16) {
				md5str.append("0");
			}
			md5str.append(Integer.toHexString(digital));
		}
		return md5str.toString().toUpperCase();
	}
	

	public static String findEncode(String ogiStr,String receive){
		Set<String> charsetNames = Charset.availableCharsets().keySet();
		Set<String> charsetNames1 = Charset.availableCharsets().keySet();
        System.out.println("---The Number of charset is " + charsetNames.size() + "---");
        System.out.println("ogiStr= " +ogiStr);
        Iterator<String> it = charsetNames.iterator();

        StringBuilder sb=new StringBuilder();
        while(it.hasNext()){
            String charsetName = it.next();
            Iterator<String> it1 = charsetNames1.iterator();
            while(it1.hasNext()){
                String charsetName1 = it1.next();
            	String tryStr;
            	try{
            		 tryStr=new String(ogiStr.getBytes(charsetName),charsetName1);
            	}catch(Exception e){
            		continue;
            	}
                if(tryStr.equals(receive)){
                	System.out.println("get:"+charsetName+",to:"+charsetName1+",looks like"+receive);
                	sb.append(charsetName).append(",").append(charsetName1).append(";");
                }
            }
        }
		
		return sb.toString();
	}
	
	
	public static void main(String[] args)throws Exception {
		
		String regx="[0-9]";
		String str="1a2a3aa";
		String seperator=",";
		String ret=StringUtil.getMatchStr(str, regx, seperator);
		ret=StringUtil.getMatchStr(str, regx, seperator);
	}// main
	

}
