package hello.programmer.common.logger;

//import com.asset.LogAopSkipAnnotation;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author liwei
 * @Description
 * @date 2017/9/1 9:42
 */

//http://blog.csdn.net/zhengchao1991/article/details/53391244

//@Component
//@Aspect
public class ControllerAOP {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAOP.class);

    private static final String RETURN_TYPE_VOID = "void";

    private static final String RETURN_TYPE_STRING = "String";



    public Object handlerControllerMethod(ProceedingJoinPoint pjp) throws Exception{
        long startTime = System.currentTimeMillis();
        Object result;
        String paramString = "";
        try {

            Object[] args = pjp.getArgs();

            MethodSignature methodSignature = ((MethodSignature)(pjp.getSignature()));
            String[] paramNames = methodSignature.getParameterNames();
            Class[] paramTypes = methodSignature.getParameterTypes();

            if(args != null && args.length > 0){
                for(int i=0 ; i < args.length; i++){
                    paramString += (paramTypes[i]+" "+paramNames[i] + "=" + args[i]) + ",";
                }
            }

            result =  pjp.proceed();

            Method targetMethod = methodSignature.getMethod();
            Annotation[]  classAnnotation = targetMethod.getAnnotations();
//            for (int i = 0; i < classAnnotation.length; i++) {
//                if(classAnnotation[i] instanceof LogAopSkipAnnotation){
//                    return result;
//                }
//            }


            String resultStr;
            String returnType = pjp.getSignature().toString().split(" ")[0];
            if(RETURN_TYPE_VOID.equals(returnType)){
                resultStr = "void";
            }
            else {
                resultStr = ToStringBuilder.reflectionToString(result);
            }

            logger.info("function: params:{} result:{} consumetime:{}", paramString, resultStr,(System.currentTimeMillis() - startTime));
        }
        catch (Exception e) {
            //result = handlerException(pjp, e);
            logger.info("function: params:{} consumetime:{}", paramString,(System.currentTimeMillis() - startTime),e);
            throw e;
        }
        catch (Throwable e2){
            throw new Exception("aop error happens");
        }
        return result;
    }

    private ResultBeanVO<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
        ResultBeanVO<?> result = new ResultBeanVO();
        // 已知异常
        if (e instanceof CheckException) {
            result.setMsg(e.getMessage());
            result.setCode(1);
        }
        else {
            logger.error(pjp.getSignature() + " error ", e);
            result.setMsg(e.getMessage());
            result.setCode(ResultBeanVO.FAIL);
            // 未知异常是应该重点关注的，这里可以做其他操作，如通知邮件，单独写到某个文件等等。
        }
        return result;
    }
}
