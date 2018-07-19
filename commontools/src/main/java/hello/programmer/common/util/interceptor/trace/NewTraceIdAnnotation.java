package hello.programmer.common.util.interceptor.trace;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * @author liwei
 * @title NewTraceIdAnnotation
 * @package com.jianlc.asset.util.interceptor.trace
 * @description: 生成新的traceid，防止一个链过长
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @date 2018/1/24 10:28
 */
@Target({TYPE, FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NewTraceIdAnnotation {
    String value() default "";
}
