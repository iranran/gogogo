package hello.programmer.common.utils;

import com.asset.enums.RetCodeEnum;

/**
 * @Description  统一返回工具类
 */
public class Result {

	 private Integer code;
	 private Object data;
	 private String message;
	 
	public Result() {
	}

	public Result(Integer code) {
		this.code = code;
	}
	
	public Result(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Result(Integer code, Object data, String message) {
		this.code = code;
		this.data = data;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static Result retSuccessMsg(String message) {
        return new Result(RetCodeEnum.SUCCESS.getCode(), null, message);
	}
	
	public static Result retSuccessMsg(String message, Object data) {
        return new Result(RetCodeEnum.SUCCESS.getCode(), data, message);
	}
	
	public static Result retFailureMsg(String message) {
        return new Result(RetCodeEnum.FAILED.getCode(), null, message);
    }
	
	public static Result retFailureMsg(String message, Object data) {
        return new Result(RetCodeEnum.FAILED.getCode(), data, message);
    }
	    
}
