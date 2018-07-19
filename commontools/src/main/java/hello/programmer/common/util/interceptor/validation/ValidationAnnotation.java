/**
 * @title ScheduleValidation
 * @package com.jianlc.asset.util.interceptor.validation
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/5/23 11:03
 */
package hello.programmer.common.util.interceptor.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * (类描述：)
 * @author liwei
 * @create 2018/5/23 11:03
 */
@Target({TYPE, FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidationAnnotation {

    String biz() default "";

    String[] switches() default "";

    boolean holidayCheck() default false;

}