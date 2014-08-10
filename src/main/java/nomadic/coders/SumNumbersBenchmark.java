package nomadic.coders;

import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;

/**
 * Created by jay on 10/08/2014.
 */
public class SumNumbersBenchmark extends Benchmark {

    public long timeSumDefaultNumbersAndSingleThread(int reps){
        long dummy = 0L;
        for(int i = 0; i < reps; i++){
            dummy |= SumNumbers.singleThreadDefaultSum();
        }
        return dummy;
    }
    public long timeSumSynchronizedNumberrsAndSingleThread(int reps){
        long dummy = 0L;
        for(int i = 0; i < reps; i++){
            dummy |= SumNumbers.singleThreadSynchronizedSum();
        }
        return dummy;
    }


    public static void main(String[] args){
        CaliperMain.main(SumNumbersBenchmark.class, args);
    }
}
