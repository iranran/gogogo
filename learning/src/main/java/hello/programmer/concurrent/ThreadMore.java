package hello.programmer.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author liwei
 * @Description
 * @date 2017/7/26 11:36
 */
public class ThreadMore {
    public static void ha() throws Exception{
        ExecutorService service = Executors.newFixedThreadPool(2);

        Future future = service.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    //Thread.sleep(10000);
                }  catch (Exception e){

                }

            }
        });
        System.out.println(future.get());

        //if(future.get() != null){
            service.shutdown();
        //}
    }

    public static void main(String[] args) throws Exception{
        for (int i=0 ; i<10; i++){
            new Thread(new Runnable() {
                @Override public void run() {
                    try {
                        ha();
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }
            });
        }
        for (int i=0 ; i<100; i++){
            Thread.sleep(100);
            System.out.println(Thread.activeCount());
        }

    }
}
