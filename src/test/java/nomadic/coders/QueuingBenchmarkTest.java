package nomadic.coders;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import com.carrotsearch.junitbenchmarks.annotation.LabelType;
import org.junit.*;
import org.junit.rules.TestRule;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;

/**
 * Created by jay on 16/08/2014.
 */
@AxisRange(min = 0, max = 1)
@BenchmarkMethodChart(filePrefix = "benchmark-queue")
@BenchmarkHistoryChart(labelWith = LabelType.CUSTOM_KEY, maxRuns = 100)
public class QueuingBenchmarkTest {

     private int nConsumers = 50;
     private int queueSize = 50_000_000;

    @Rule
    public TestRule benchmarkRun = new BenchmarkRule();

//    @Param({"5", "10", "20"}) int nConsumers;
//    @Param({"100", "1000", "10000"}) int queueSize;
    ArrayBlockingQueue<Integer> queue;
    Random rng = new SecureRandom();
    ArrayBlockingQueue<Integer>[] queues;

    @Before
    public  void setUp() throws Exception {
        Queuing.executor = Executors.newFixedThreadPool(nConsumers);
        queue = new ArrayBlockingQueue<Integer>(queueSize + nConsumers);
        for(int i =0; i < queueSize; i++){
            queue.put(rng.nextInt(Integer.MAX_VALUE));
        }
         queues = new ArrayBlockingQueue[nConsumers];
         split(queues);
         poison(queue, nConsumers);
      //  System.out.printf("running test with %s consumers and queue size of %s\n", nConsumers, queueSize);
      //  System.out.println("queue: " + queue);
    //    System.out.println("queue: " + queue);
      //  System.out.println("queues: " + Arrays.toString(queues));

    }

    @After
    public  void tearDown() throws Exception {
        Queuing.executor.shutdownNow();
    }

    @Test
    public void oneQueueOneConsumer() throws Exception{
        Queuing.onQueueOneConsumer(queue);
    }

    @Test
    public void oneQueueManyConsumers() throws Exception{
        Queuing.oneQueueManyConsumers(queue, nConsumers);

    }

    @Test
    public void QueuePerConsumer() throws Exception{
        Queuing.QueuePerConsumer(nConsumers, queues);
    }

    private  void split(ArrayBlockingQueue<Integer>[] queues) {
        int size = queueSize/nConsumers;
        for(int i = 0; i < queues.length; i++){
            queues[i] = new ArrayBlockingQueue<Integer>(size+1);
            for(int j = 0; j < size; j++){
                queues[i].add(rng.nextInt(Integer.MAX_VALUE));
            }
            poison(queues[i], 1);
        }
    }

    private  void poison(ArrayBlockingQueue<Integer> queue, int nConsumers) {
        try {
            for(int i = 0; i < nConsumers; i++)
                queue.put(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            // do nothing
        }
    }

}
