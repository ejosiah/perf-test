package nomadic.coders;

import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.google.caliper.runner.CaliperMain;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * Created by jay on 16/08/2014.
 */
public class QueuingBenchmark extends Benchmark {

    @Param({"5", "10", "20"}) int nConsumers;
    @Param({"100", "1000", "10000"}) int queueSize;
    ArrayBlockingQueue<Integer> queue;
    Random rng = new SecureRandom();
    CountDownLatch latch;

    @Override
    protected void setUp() throws Exception {
        Queuing.executor = Executors.newFixedThreadPool(nConsumers);
        queue = new ArrayBlockingQueue<Integer>(queueSize);
        for(int i =0; i < queueSize; i++){
            queue.put(rng.nextInt(Integer.MAX_VALUE));
        }
        latch = new CountDownLatch(nConsumers);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void timeOneQueueManyConsumers(int reps) throws Exception{
        poison(queue);
        for(int i = 0; i < reps; i++){
            Queuing.oneQueueManyConsumers(queue, nConsumers, latch);
        }
        latch.await();
        Queuing.executor.shutdownNow();
    }

    public void timeQueuePerConsumer(int reps) throws Exception{
        ArrayBlockingQueue<Integer>[] queues = new ArrayBlockingQueue[nConsumers];
        split(queue, queues);
        for(int i = 0; i < reps; i++){
            Queuing.QueuePerConsumer(nConsumers, latch, queues);
        }
        latch.await();
        Queuing.executor.shutdownNow();
    }

    private void split(ArrayBlockingQueue<Integer> queue, ArrayBlockingQueue<Integer>[] queues) {
        int size = queueSize/nConsumers;
        for(int i = 0; i < queues.length; i++){
            queues[i] = new ArrayBlockingQueue<Integer>(size);
            for(int j = 0; j < size; i++){
                queues[i].add(queue.remove());
            }
            poison(queues[i]);
        }
    }

    private static void poison(ArrayBlockingQueue<Integer> queue) {
        for(int i = 0; i < 50; i++){
            try {
                queue.put(Integer.MAX_VALUE);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }

    public static void main(String...args){
        CaliperMain.main(QueuingBenchmark.class, args);
    }
}
