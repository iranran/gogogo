package hello.programmer.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 手机号正则验证
 * @author zcs
 *
 */
public class MobileRegix {

  private static int mobileLength = 11;
  
  private String regex;
	
	/** 校验11位手机号
	 * @param mobile
	 * @return
	 */
  	public MobileRegix(String regex){
  		this.regex = regex;
  	}
  	
	public  boolean validate(String mobile){
		if(StringUtils.isBlank(mobile)){
			return false;
		}
		String trimMobile = mobile.trim();
		if(trimMobile.length() != mobileLength){
			return false;
		}
		
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(trimMobile);
		
		return matcher.matches();
	}
}
