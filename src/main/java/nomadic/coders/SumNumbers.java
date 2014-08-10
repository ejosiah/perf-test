package nomadic.coders;

import lombok.SneakyThrows;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

/**
 * Created by jay on 10/08/2014.
 */
public class SumNumbers {

    private static final int LIMIT = 5_000_000;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @SneakyThrows
    public static long singleThreadDefaultSum(){
        final NumberProvider numberProvider = new DefaultNumberProvider();
        return executorService.submit( () -> {
            long sum = 0L;
            for(int i = 0; i < LIMIT; i++){
                sum += numberProvider.next();
            }
            return sum;
        }).get();

    }

    @SneakyThrows
    public static long singleThreadSynchronizedSum(){
        final NumberProvider numberProvider = new SynchronizedNumberProvider();
        return executorService.submit( () -> {
            long sum = 0L;
            for(int i = 0; i < LIMIT; i++){
                sum += numberProvider.next();
            }
            return sum;
        }).get();
    }

    @SneakyThrows
    public static long singleThreadAtomicSum(){
        final NumberProvider numberProvider = new AtomicNumberProvider();
        return executorService.submit( () -> {
            long sum = 0L;
            for(int i = 0; i < LIMIT; i++){
                sum += numberProvider.next();
            }
            return sum;
        }).get();
    }

    @SneakyThrows
    public static long TwoThreadsAndSynchronizedSum(){
        final NumberProvider numberProvider = new SynchronizedNumberProvider();
        final CountDownLatch latch = new CountDownLatch(2);
        long sum = 0;
        executorService.submit( new SumTask(LIMIT, sum, numberProvider, latch));
        executorService.submit( new SumTask(LIMIT, sum, numberProvider, latch));
        latch.await();

        return sum;
    }

    @SneakyThrows
    public static long TwoThreadsAndAtomicSum(){
        final NumberProvider numberProvider = new AtomicNumberProvider();
        final CountDownLatch latch = new CountDownLatch(2);
        long sum = 0;
        executorService.submit( new SumTask(LIMIT, sum, numberProvider, latch));
        executorService.submit( new SumTask(LIMIT, sum, numberProvider, latch));
        latch.await();

        return sum;
    }
    @SneakyThrows
    public static long ThreeThreadsAndSynchronizedSum(){
        final NumberProvider numberProvider = new SynchronizedNumberProvider();
        final CountDownLatch latch = new CountDownLatch(3);
        long sum = 0;
        executorService.submit( new SumTask(LIMIT, sum, numberProvider, latch));
        executorService.submit( new SumTask(LIMIT, sum, numberProvider, latch));
        executorService.submit( new SumTask(LIMIT, sum, numberProvider, latch));
        latch.await();

        return sum;
    }

    @SneakyThrows
    public static long ThreeThreadsAndAtomicSum(){
        final NumberProvider numberProvider = new AtomicNumberProvider();
        final CountDownLatch latch = new CountDownLatch(3);
        long sum = 0;
        executorService.submit( new SumTask(LIMIT, sum, numberProvider, latch));
        executorService.submit( new SumTask(LIMIT, sum, numberProvider, latch));
        executorService.submit( new SumTask(LIMIT, sum, numberProvider, latch));
        latch.await();

        return sum;
    }

    public static long usingSumOfNaturalNumbers(){
        return sumOfNaturalNumbers(LIMIT);
    }

    public static long sumOfNaturalNumbers(long n){
        return n * (n + 1)/2;
    }

    private static class SumTask implements Runnable{

        private final int limit;
        private final NumberProvider numberProvider;
        private final CountDownLatch latch;
        private long sum;

        public SumTask(int limit, long sum, NumberProvider numberProvider, CountDownLatch latch){
            this.limit = limit;
            this.sum = sum;
            this.numberProvider = numberProvider;
            this.latch = latch;
        }

        @Override
        public void run() {
            long expected = sumOfNaturalNumbers(limit);

            while(sum != expected){
                sum += numberProvider.next();
            }
            latch.countDown();
        }
    }
}
