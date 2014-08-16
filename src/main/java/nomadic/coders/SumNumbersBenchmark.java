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

    public long timeSumSynchronizedNumbersAndSingleThread(int reps){
        long dummy = 0L;
        for(int i = 0; i < reps; i++){
            dummy |= SumNumbers.singleThreadSynchronizedSum();
        }
        return dummy;
    }

    public long timeSumAtomicNumbersAndSingleThread(int reps){
        long dummy = 0L;
        for(int i = 0; i < reps; i++){
            dummy |= SumNumbers.singleThreadAtomicSum();
        }
        return dummy;
    }

    public long timeSumSynchronizedNumbersAndTwoThread(int reps){
        long dummy = 0L;
        for(int i = 0; i < reps; i++){
            dummy |= SumNumbers.TwoThreadsAndSynchronizedSum();
        }
        return dummy;
    }
    public long timeSumAtomicNumbersAndTwoThread(int reps){
        long dummy = 0L;
        for(int i = 0; i < reps; i++){
            dummy |= SumNumbers.TwoThreadsAndAtomicSum();
        }
        return dummy;
    }

    public long timeSumSynchronizedNumbersAndThreeThread(int reps){
        long dummy = 0L;
        for(int i = 0; i < reps; i++){
            dummy |= SumNumbers.ThreeThreadsAndSynchronizedSum();
        }
        return dummy;
    }
    public long timeSumAtomicNumbersAndThreeThread(int reps){
        long dummy = 0L;
        for(int i = 0; i < reps; i++){
            dummy |= SumNumbers.ThreeThreadsAndAtomicSum();
        }
        return dummy;
    }


    public static void main(String[] args){
        CaliperMain.main(SumNumbersBenchmark.class, new String[]{"-l='5 min'"});
    }
}
