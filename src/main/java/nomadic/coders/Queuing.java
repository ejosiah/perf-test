package nomadic.coders;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

/**
 * Created by jay on 16/08/2014.
 */
public class Queuing {

    static ExecutorService executor;

    public static void oneQueueManyConsumers(ArrayBlockingQueue<Integer> queue, int nConsumers){
        CountDownLatch latch = new CountDownLatch(nConsumers);
        IntStream.range(0, nConsumers).forEach( (i) -> executor.execute(new Consumer(queue, latch)));
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void QueuePerConsumer(int nConsumers, ArrayBlockingQueue<Integer>...queues){
        CountDownLatch latch = new CountDownLatch(nConsumers);
        IntStream.range(0, nConsumers).forEach( (i) -> executor.execute(new Consumer(queues[i], latch)));
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static class Consumer implements Runnable{

        private final CountDownLatch latch;
        ArrayBlockingQueue<Integer> queue;

        private Consumer(ArrayBlockingQueue<Integer> queue, CountDownLatch latch){
            this.queue = queue;
            this.latch = latch;
        }

        @Override
        public void run() {
            while (true) {
                Integer i = null;
                try {
                    i = queue.take();
                } catch (InterruptedException e) {
                }
                if(i == Integer.MAX_VALUE){
                    latch.countDown();
                 //   System.out.printf("conusmer %s doing processing now exiting...", Thread.currentThread().getName());
                    break;
                }
            }
        }
    }
}
