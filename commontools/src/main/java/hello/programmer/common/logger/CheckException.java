/**
 * @title CheckException
 * @package com.jianlc.basic.asset.util.exception
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2017/12/1 14:49
 */
package hello.programmer.common.logger;

/**
 * (类描述：CheckException)
 * @author liwei
 * @create 2017/12/1 14:49
 */
public class CheckException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CheckException() {
    }

    public CheckException(String message) {
        super(message);
    }

    public CheckException(Throwable cause) {
        super(cause);
    }

    public CheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}