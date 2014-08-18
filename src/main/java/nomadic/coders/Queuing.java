package nomadic.coders;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

/**
 * Created by jay on 16/08/2014.
 */
public class Queuing {

    static ExecutorService executor;

    public static void oneQueueManyConsumers(ArrayBlockingQueue<Integer> queue, int nConsumers) {
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(nConsumers);
        IntStream.range(0, nConsumers).forEach((i) -> executor.execute(new Consumer(queue, startGate, endGate)));
        try {
            startGate.countDown();
            endGate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void QueuePerConsumer(int nConsumers, ArrayBlockingQueue<Integer>... queues) {
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(nConsumers);
        IntStream.range(0, nConsumers).forEach((i) -> executor.execute(new Consumer(queues[i], startGate, endGate)));

        try {
            startGate.countDown();
            endGate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void onQueueOneConsumer(ArrayBlockingQueue<Integer> queue) {
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(1);
        executor.execute(new Consumer(queue, startGate, endGate));

        try {
            startGate.countDown();
            endGate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static class Consumer implements Runnable {

        private final CountDownLatch endGate;
        private final CountDownLatch startGate;
        ArrayBlockingQueue<Integer> queue;

        private Consumer(ArrayBlockingQueue<Integer> queue, CountDownLatch startGate, CountDownLatch endGate) {
            this.queue = queue;
            this.endGate = endGate;
            this.startGate = startGate;
        }

        @Override
        public void run() {
            int count = 0;
            try {
                startGate.await();
                while (true) {
                    Integer i = null;
                    i = queue.take();
                    ++count;
                    //   System.out.printf("%s took %s from queue\n", Thread.currentThread().getName(), i);
                    if (i == Integer.MAX_VALUE) {

                        //      System.out.printf("conusmer %s doing processing now exiting...\n", Thread.currentThread().getName());
                      //  System.out.printf("consumer %s finished processing %s operations\n", Thread.currentThread().getName(), count);
                        endGate.countDown();
                        break;
                    }
                }
            } catch (InterruptedException e) {

            }
        }
    }
}
