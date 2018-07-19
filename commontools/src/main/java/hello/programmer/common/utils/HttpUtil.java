package hello.programmer.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jianlc.util.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {
	
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error("发送GET请求出现异常！", e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				logger.error("关闭输入流出现异常！", e2);
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String data) {
		// PrintWriter out = null;
		OutputStreamWriter out = null;
		BufferedReader in = null;
		StringBuilder sb = new StringBuilder();
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();

			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Accept-Charset", StringUtil.charSet_utf8);
			conn.setRequestProperty("contentType", StringUtil.charSet_utf8);
			conn.setRequestProperty("aas", "aaa123");
			out = new OutputStreamWriter(conn.getOutputStream(), StringUtil.charSet_utf8);
			// out.write("jsonData=" + jsonData);
			out.write(data);
//			conn.
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StringUtil.charSet_utf8));
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line).append("\r\n");
				// result += line;
			}
		} catch (Exception e) {
			logger.error("发送 POST 请求出现异常！", e);
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				logger.error("关闭io流异常", ex);
			}
		}
		return sb.substring(0, sb.length() - "\r\n".length());
	}

	public static Map<String,String> htmlParaToMap(String htmlPara){
		Map<String,String> map=new HashMap<>();
		if(htmlPara==null || htmlPara.equals("")){
			return map;
		}
		String[] paras=htmlPara.split("&");
		for(String pair:paras){
			String[] keyValue=pair.split("=");
			map.put(keyValue[0], keyValue[1]);
		}
		return map;
	}
	
	public static void main(String[] args) throws Exception {
        String paraHtml="app_id=jlc&apply_id=30110&product_sn=NJJ·NFY2017071641H&product_title=南金交·宁富盈2017071641号&status=SUCCESS&sign=FCCB00586DF97C7F93AB2EBCF683AAF9";

        Map<String,String> map=HttpUtil.htmlParaToMap(paraHtml);
        if(true){
        	return;
        }
        
        
        //		String baseUrl="http://localhost:8080/jianlc-asset-mgmt/tianjinBourseProduct/applyCallBack";
		String baseUrl="http://10.1.12.123:8080/tianjinBourseProduct/applyCallBack";
//		String baseUrl="https://test.api.njfae.com.cn/";

		String sendData="{app_id:\"jlc\",apply_id:30091,status:\"BATCH_REPAY_LOAN_PHASE\","
				+ "status_message:\"\",product_sn:\"NJJ.NFW2017H11100089\"}";
		JSONObject jsonObj = JSON.parseObject(sendData);
    	MD5Util.addJsonSign(jsonObj, "&app_secret=5JRRLmeIXdLmNIQCIXyeFmQljjMKgREQ");
    	
//		bbs=cc123&eee=23423
		try {
//			String ret=HttpUtil.sendPost(baseUrl, "bdfawbtan");
			Object ret=HttpClientUtils.postRequest(baseUrl, jsonObj.toJSONString());
			$.log(ret);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		$.log("finish");
	}
}
