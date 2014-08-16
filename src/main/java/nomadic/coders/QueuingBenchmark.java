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

    @Param({"1"}) private int nConsumers;
    @Param({"10"}) private int queueSize;

//    @Param({"5", "10", "20"}) int nConsumers;
//    @Param({"100", "1000", "10000"}) int queueSize;
    ArrayBlockingQueue<Integer> queue;
    Random rng = new SecureRandom();

    @Override
    protected void setUp() throws Exception {
        Queuing.executor = Executors.newFixedThreadPool(nConsumers);
        queue = new ArrayBlockingQueue<Integer>(queueSize + 100);
        for(int i =0; i < queueSize; i++){
            queue.put(rng.nextInt(Integer.MAX_VALUE));
        }
      //  System.out.printf("running test with %s consumers and queue size of %s\n", nConsumers, queueSize);
      //  System.out.println("queue: " + queue);

    }

    @Override
    protected void tearDown() throws Exception {
        Queuing.executor.shutdownNow();
    }

    public void timeOneQueueManyConsumers(int reps) throws Exception{
        poison(queue);
        for(int i = 0; i < reps; i++){
            Queuing.oneQueueManyConsumers(queue, nConsumers);
        }

    }

    public void timeQueuePerConsumer(int reps) throws Exception{
        ArrayBlockingQueue<Integer>[] queues = new ArrayBlockingQueue[nConsumers];
        split(queue, queues);
        for(int i = 0; i < reps; i++){
            Queuing.QueuePerConsumer(nConsumers, queues);
        }
    }

    private void split(ArrayBlockingQueue<Integer> queue, ArrayBlockingQueue<Integer>[] queues) {
        int size = queueSize/nConsumers;
        for(int i = 0; i < queues.length; i++){
            queues[i] = new ArrayBlockingQueue<Integer>(size+50);
            for(int j = 0; j < size; j++){
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
        CaliperMain.main(QueuingBenchmark.class, new String[]{"--verbose", "-l", "5 minutes"});
    }
}
