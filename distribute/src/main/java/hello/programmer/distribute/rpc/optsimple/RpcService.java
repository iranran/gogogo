package hello.programmer.distribute.rpc.optsimple;

import java.lang.annotation.*;

/**
 * @author liwei
 * @Description
 * @date 2017/7/17 17:40
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcService
{
    /** Rpc服务名 */
    String value() default "";

    /** 是否允许使用RpcServerContext.getCurrent()方法, 打开会降低性能, 建议使用直接继承RpcServiceBase的方式实现 */
    boolean threadContext() default false;
}
