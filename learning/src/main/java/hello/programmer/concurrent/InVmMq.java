package hello.programmer.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class InVmMq {

    private BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(100);

    public class Producer{

        BlockingQueue<Integer> blockingQueue;

        public Producer(BlockingQueue<Integer> blockingQueue){
            this.blockingQueue = blockingQueue;
        }

        public void produce(Integer integer){
            System.out.println(blockingQueue);
            blockingQueue.add(integer);
            System.out.println("produce->"+integer);
        }
    }

    public class Consumer{

        BlockingQueue<Integer> blockingQueue;

        public Consumer(BlockingQueue<Integer> blockingQueue){
            this.blockingQueue = blockingQueue;
        }

        public void consume() throws Exception{
            for(;;){
                Integer integer = blockingQueue.take();
                System.out.println("consume->"+integer);
            }
        }
    }

    public void produce() {
        Producer producer = new Producer(blockingQueue);
        for(int i=0; i<100; i++){
            producer.produce(i);
        }
    }

    public void consume() throws Exception{
        Consumer consumer = new Consumer(blockingQueue);
        consumer.consume();
    }


    public static void main(String[] args) throws Exception{
        InVmMq inVmMq = new InVmMq();
        inVmMq.produce();
        inVmMq.consume();
    }
}
