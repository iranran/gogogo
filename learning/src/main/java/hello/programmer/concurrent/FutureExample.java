package hello.programmer.concurrent;

import sun.nio.ch.ThreadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureExample {

    public static void main(String[] args) throws Exception{
        ExecutorService service = Executors.newCachedThreadPool();

        long t0 = System.currentTimeMillis();
        Runnable r = () -> {
            System.out.println("i am runnable");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Future f1 = service.submit(r);

        Callable<Integer> callable = () -> {

            System.out.println("i am callable");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        };
        Future<Integer> f2 = service.submit(callable);

        Thread.sleep(2000);

        f1.get();
        System.out.println(f2.get());
        System.out.println(System.currentTimeMillis() - t0);
        service.shutdownNow();
    }

}
