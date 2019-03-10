package hello.programmer.netty;

import io.netty.util.HashedWheelTimer;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class HashedWheelTimerTest {
    public static void main(String[] args) throws Exception{
        //创建Timer, 精度为100毫秒,
        HashedWheelTimer timer = new HashedWheelTimer(1500, TimeUnit.MILLISECONDS, 16);

        System.out.println(LocalTime.now());

        timer.newTimeout((timeout) -> {
            System.out.println(LocalTime.now());
            System.out.println(timeout);
        }, 5, TimeUnit.SECONDS);

        timer.newTimeout((timeout) -> {
            System.out.println(LocalTime.now());
            System.out.println(timeout);
        }, 15, TimeUnit.SECONDS);

        //阻塞main线程
        System.in.read();
    }
}
