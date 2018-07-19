package hello.programmer.common.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MD5Util {

	public static void addJsonSign(JSONObject jsonObj,String appendStr) throws UnsupportedEncodingException{
		String md5=nanjingMakeJsonSign(jsonObj,appendStr);
		jsonObj.put("sign", md5);
	}
	
	public static String jsonToHtmlParaStr(JSONObject jsonObj,boolean isEncode) throws UnsupportedEncodingException {
		List<String> list = new ArrayList<>(jsonObj.keySet());

		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (String key : list) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append("&");
			}
			String val = jsonObj.getString(key);
			if(isEncode){
				sb.append(URLEncoder.encode(key, "utf-8")).append("=").append(URLEncoder.encode(val, "utf-8"));
			}else{
				sb.append(key).append("=").append(val);
			}
			
		}
		return sb.toString();
	}
	
	public static String nanjingJsonToHtmlParaStr(JSONObject jsonObj,boolean isEncode) throws UnsupportedEncodingException {
		List<String> list = new ArrayList<>(jsonObj.keySet());

		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (String key : list) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append("&");
			}
			String val = jsonObj.getString(key);
			if(isEncode){
				sb.append(URLEncoder.encode(key, "ISO-8859-1")).append("=").append(URLEncoder.encode(val, "ISO-8859-1"));
//				if(key.equals("ref_product_sn")){
//					sb.append(key).append("=").append("NJJ%B7NFY2017075758H");
//				}else{
//					sb.append(URLEncoder.encode(key, "ISO-8859-1")).append("=").append(URLEncoder.encode(val, "ISO-8859-1"));
//				}
				
			}else{
				sb.append(key).append("=").append(val);
			}
			
		}
		return sb.toString();
	}
	public static String makeJsonSign(JSONObject jsonObj,String appendStr) throws UnsupportedEncodingException{
		String str=MD5Util.jsonToHtmlParaStr(jsonObj,true);
        str+=appendStr;
        return getMD5(str);
	}
	public static String nanjingMakeJsonSign(JSONObject jsonObj,String appendStr) throws UnsupportedEncodingException{
		String str=MD5Util.nanjingJsonToHtmlParaStr(jsonObj,true);
        str+=appendStr;
        return getMD5(str);
	}
	public static boolean checkJsonSign(JSONObject jsonObj,String appendStr) throws UnsupportedEncodingException{
		String sign=jsonObj.getString("sign");
		jsonObj.remove("sign");
		String md5=makeJsonSign(jsonObj,appendStr);
		if(md5==null || sign==null || !md5.equals(sign)){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 生成md5
	 * 
	 * @param message
	 * @return
	 */
	public static String getMD5(String message) {
		String md5str = "";
		try {
			// 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
			MessageDigest md = MessageDigest.getInstance("MD5");

			// 2 将消息变成byte数组
			byte[] input = message.getBytes();

			// 3 计算后获得字节数组,这就是那128位了
			byte[] buff = md.digest(input);

			// 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
			md5str = bytesToHex(buff);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5str;
	}

	/**
	 * 二进制转十六进制
	 * 
	 * @param bytes
	 * @return
	 */
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
	
	public static void main(String[] args) {
		
		
        String jsonStr = "{a1:1,a3:3,a2:2}";


//        JSONObject jsonObj = JSON.parseObject(jsonStr);
//        makeJsonSign(jsonObj,"appendStr");

        String val="app_id=BB1PoH4g&apply_id=1&apr=5.10&duration=12&duration_unit=month"
        		+ "&min_invest=500000.00&product_sn=njj.nfw2017H00312956&target=10000000.00";
        val+="&app_secret=5JRRLmeIXdLmNIQCIXyeFmQljjMKgREQ";
        
        val="app_id=jlc&apply_id=30060&apr=4.8&callback_url=http%3A%2F%2Flocalhost%3A8080product%2Fapplication%2Fcreate%2FnanJingBourse%2FapplyCallBack&duration=100&duration_unit=year&min_invest=0.01&ref_product_sn=30060&target=15000.00";
        val+="&app_secret=5JRRLmeIXdLmNIQCIXyeFmQljjMKgREQ";
        
        val="app_id=jlc&apply_id=30060&apr=4.8&callback_url=http://localhost:8080product/application/create/nanJingBourse/applyCallBack&duration=100&duration_unit=year&min_invest=0.01&ref_product_sn=30060&target=15000.00&app_secret=5JRRLmeIXdLmNIQCIXyeFmQljjMKgREQ";
        $.log(getMD5(val));
        
    }
	
}
