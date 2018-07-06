/**
 * 
 */
package hello.programmer.common.codec;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;



/**
 * 对commonc-codec中的Base64进行简单的封装实现base64编码和解码
 *
 */
public class Base64Util{

	public static String encode(byte[] data) {
	   if (data == null) return null;
	   return Base64.encodeBase64String(data);
	}
	
	public static byte[] decode(String base64str){
		return Base64.decodeBase64(base64str);
	}
	
//	public static String encode(File file) throws IOException{
//		return encode(FileUtil.toByteArray(file));
//	}
	
	
	 
	public static void main(String args[]){
		  
			   String str = "ask for you a question r";
			   str = encode(str.getBytes());
			   System.out.println("=="+str+"\n");
		
	   }
}
