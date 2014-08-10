package nomadic.coders;

import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

/**
 * Created by jay on 10/08/2014.
 */
public class SumNumbers {

    private static final int LIMIT = 50_000_000;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);

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


}
