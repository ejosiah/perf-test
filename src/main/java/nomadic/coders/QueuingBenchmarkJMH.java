package nomadic.coders;


import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;

/**
 * Created by jay on 16/08/2014.
 */
public class QueuingBenchmarkJMH {

    private int nConsumers = 5;
    private int queueSize = 10;

//    @Param({"5", "10", "20"}) int nConsumers;
//    @Param({"100", "1000", "10000"}) int queueSize;
    ArrayBlockingQueue<Integer> queue;
    Random rng = new SecureRandom();

    protected void setUp() throws Exception {
        Queuing.executor = Executors.newFixedThreadPool(nConsumers);
        queue = new ArrayBlockingQueue<Integer>(queueSize + 1);
        for(int i =0; i < queueSize; i++){
            queue.put(rng.nextInt(Integer.MAX_VALUE));
        }
      //  System.out.printf("running test with %s consumers and queue size of %s\n", nConsumers, queueSize);
      //  System.out.println("queue: " + queue);

    }

    protected void tearDown() throws Exception {
        Queuing.executor.shutdownNow();
    }

    @Benchmark
    public void timeOneQueueManyConsumers(int reps) throws Exception{
        setUp();
        poison(queue);
        for(int i = 0; i < reps; i++){
            Queuing.oneQueueManyConsumers(queue, nConsumers);
        }
        tearDown();
    }

    @Benchmark
    public void timeQueuePerConsumer(int reps) throws Exception{
        setUp();
        ArrayBlockingQueue<Integer>[] queues = new ArrayBlockingQueue[nConsumers];
        split(queue, queues);
        for(int i = 0; i < reps; i++){
            Queuing.QueuePerConsumer(nConsumers, queues);
        }
        tearDown();
    }

    private void split(ArrayBlockingQueue<Integer> queue, ArrayBlockingQueue<Integer>[] queues) {
        int size = queueSize/nConsumers;
        for(int i = 0; i < queues.length; i++){
            queues[i] = new ArrayBlockingQueue<Integer>(size+1);
            for(int j = 0; j < size; j++){
                queues[i].add(queue.remove());
            }
            poison(queues[i]);
        }
    }

    private static void poison(ArrayBlockingQueue<Integer> queue) {
        try {
            queue.put(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            // do nothing
        }
    }

    public static void main(String...args) throws Exception{
        Options opts = new OptionsBuilder().include(".*" + QueuingBenchmarkJMH.class.getSimpleName() + ".*")
                .forks(1).build();
        System.out.println(opts);
        new Runner(opts).run();
    }
}
