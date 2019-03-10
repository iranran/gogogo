package hello.programmer.reflection;

import io.netty.util.internal.PlatformDependent;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;

public class Reflection {

    public static void main(String[] args) {
        Field field = ReflectionUtils.findField(PlatformDependent.class, "DIRECT_MEMORY_COUNTER");
        field.setAccessible(true);

        try{
            AtomicLong directMemory = ((AtomicLong)field.get(PlatformDependent.class));
            System.out.println("dm:" + directMemory);
        } catch (Exception e) {

        }
    }
}
