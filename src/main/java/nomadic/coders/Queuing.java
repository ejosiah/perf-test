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

    public static void oneQueueManyConsumers(ArrayBlockingQueue<Integer> queue, int nConsumers, CountDownLatch latch){
        IntStream.range(0, nConsumers).forEach( (i) -> executor.execute(new Consumer(queue, latch)));
    }

    public static void QueuePerConsumer(int nConsumers, CountDownLatch latch, ArrayBlockingQueue<Integer>...queues){
        IntStream.range(0, nConsumers).forEach( (i) -> executor.execute(new Consumer(queues[i], latch)));
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
                    break;
                }
            }
        }
    }
}
