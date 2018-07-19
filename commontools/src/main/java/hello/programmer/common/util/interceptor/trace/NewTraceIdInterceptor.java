/**
 * @title NewTraceIdInterceptor
 * @package com.jianlc.asset.util.interceptor.trace
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/1/24 10:35
 */
package hello.programmer.common.util.interceptor.trace;

import com.jianlc.tc.jtracker.client.TraceContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 主要实现在方法执行将原有跟踪链切断，防止链过长无法在zipkin上查询
 * 1.consumer单例模式、2.多线程处理、3.for循环处理
 * @author liwei
 * @create 2018/1/24 10:35
 */
@Aspect
@Component
@Lazy(false)
public class NewTraceIdInterceptor {

    @Pointcut(value="@annotation(com.jianlc.asset.util.interceptor.trace.NewTraceIdAnnotation)")
    public void offSpanPointCut() {

    }

    @Pointcut(value="execution(* com.jianlc.schedule.service.IScheduleTaskDeal.execute(*))")
    public void tbScheduleTaskPointCut() {

    }

    @Pointcut(value="@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void springSchedulePointCut() {

    }

    @Before(value = "offSpanPointCut()")
    public void before(JoinPoint joinPoint) {
        TraceContext.offSpan();
    }

    @Before(value = "tbScheduleTaskPointCut()")
    public void tbScheduleTaskBefore(JoinPoint joinPoint) {
        TraceContext.offSpan();
    }

    @Before(value = "springSchedulePointCut()")
    public void springScheduleTaskBefore(JoinPoint joinPoint) {
        TraceContext.offSpan();
    }
}