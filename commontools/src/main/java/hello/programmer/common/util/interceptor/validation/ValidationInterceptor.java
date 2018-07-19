/**
 * @title ValidationInterceptor
 * @package com.jianlc.asset.util.interceptor.validation
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/6/1 14:32
 */
package hello.programmer.common.util.interceptor.validation;

import com.jianlc.asset.configCenter.JianlcConfigCenter;
import com.jianlc.fund.api.RemoteResponse;
import com.jianlc.fund.api.remote.HolidaysRemote;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * (类描述：)
 * @author liwei
 * @create 2018/6/1 14:32
 */
@Aspect
@Component
@Lazy(false)
public class ValidationInterceptor {
    private Logger logger = LoggerFactory.getLogger(ValidationInterceptor.class);
    @Resource
    private HolidaysRemote holidaysRemote;


    @Resource
    private JianlcConfigCenter jianlcConfigCenter;

    @Pointcut(value="@annotation(ValidationAnnotation)")
    public void validationPointCut() {

    }


    @Around(value = "validationPointCut()")
    public Object springScheduleTaskBefore(ProceedingJoinPoint joinPoint)  throws Exception{
        try{
            MethodSignature methodSignature = ((MethodSignature)(joinPoint.getSignature()));
            Method targetMethod = methodSignature.getMethod();
            ValidationAnnotation va = targetMethod.getAnnotation(ValidationAnnotation.class);
            if(validate(va)){
                return joinPoint.proceed();
            }
        }
        catch (Exception e){
            throw e;
        }
        catch (Throwable e2){
            throw new Exception("aop error happens");
        }
        finally {

        }
        return new Object();
    }

    public boolean validate(ValidationAnnotation va) throws Exception{
        String biz = va.biz();
        if(va.holidayCheck()){
//                RemoteResponse<Boolean> holidayResponse = holidaysRemote.isHoliday(new Date());
//                if(holidayResponse.getData() == true){
//                    logger.warn("今天是假期不能操作:{}", biz);
//                    return false;
//                }
        }

        String[] switches = va.switches();
        if(switches != null && switches.length > 0){
            ExpressionParser parser = new SpelExpressionParser();
            StandardEvaluationContext context = new StandardEvaluationContext();

            Field[] as = JianlcConfigCenter.class.getDeclaredFields();
            for(Field f : as){
                String methodName = "get" + f.getName().substring(0,1).toUpperCase() + f.getName().substring(1);
                Method method = jianlcConfigCenter.getClass().getDeclaredMethod(methodName);
                context.setVariable(f.getName(), method.invoke(jianlcConfigCenter));
            }

            for(String switcher : switches){
                Expression expression = parser.parseExpression(switcher);
                String switchValue = expression.getValue(context,String.class);
                if(!"on".equals(switchValue)){
                    logger.warn("{}的开关:{}不是打开的，请检查", biz, switcher);
                    return false;
                }
            }
        }

        return true;
    }

}