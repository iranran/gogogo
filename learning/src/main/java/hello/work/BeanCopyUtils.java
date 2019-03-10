package hello.work;

import lombok.Data;
import org.springframework.beans.*;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.annotation.Nullable;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

public class BeanCopyUtils extends BeanUtils{

    /**
     * Copy the property values of the given source bean into the given target bean.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * @param source the source bean
     * @param target the target bean
     * @param editable the class (or interface) to restrict property setting to
     * @param ignoreProperties array of property names to ignore
     * @throws BeansException if the copying failed
     * @see BeanWrapper
     */
    private static void copyProperties(Object source, Object target, @Nullable Class<?> editable,
                                       @Nullable String... ignoreProperties) throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();

                    if (readMethod != null) {
                        boolean sameType = ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType());
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            if (sameType) {
                                writeMethod.invoke(target, value);
                            }
                            else {
                                BiFunction<Class,Class, Boolean> function = (leftClass,rightClass) ->
                                        ClassUtils.isAssignable(readMethod.getReturnType(), leftClass)
                                        && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], rightClass);

                                if (function.apply(String.class, BigDecimal.class)) {
                                    writeMethod.invoke(target, new BigDecimal(String.valueOf(value)));
                                } else if(function.apply(BigDecimal.class, String.class)) {
                                    writeMethod.invoke(target, new BigDecimal(String.valueOf(value))
                                            .setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
                                } else if(function.apply(Timestamp.class, Long.class)) {
                                    writeMethod.invoke(target, ((Timestamp)value).getTime());
                                } else if(function.apply(Long.class, Timestamp.class)) {
                                    writeMethod.invoke(target, new Timestamp(Long.valueOf(value.toString())));
                                }
                            }
                        }
                        catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }



    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        copyProperties(src, target,null, getNullPropertyNames(src));
    }

    public static void copyPropertiesIgnoreNull2(Object src, Object target) {
        copyProperties(src, target, getNullPropertyNames(src));


    }

    public static void main(String[] args) {
        for(int i=0 ;i< 100; i++) {
            A a = new A();
        a.setA("3.0");
        a.setB("B");
        a.setC(new BigDecimal(1.2323232009445));
        a.setD("4.565643234234234");
        a.setE(new Timestamp(System.currentTimeMillis()));
        B b = new B();

            long t0 = System.currentTimeMillis();
            copyPropertiesIgnoreNull(a, b);
            System.out.println(System.currentTimeMillis() - t0);
        }


        
    }

    @Data
    public static class A{
        private String a;
        private String b;
        private BigDecimal c;
        private String d;
        private Timestamp e;

    }

    @Data
    public static class B{
        private BigDecimal a;
        private String b;
        private String c;
        private BigDecimal d;
        private Long e;
        private Integer f;
    }
}
